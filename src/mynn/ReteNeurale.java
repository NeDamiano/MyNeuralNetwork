package mynn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

/**
 * Implementazione di {@link IReteNeurale} come lista ordinata di {@link StratoDiNeuroni}.
 * <p>
 * Le reti a singolo strato vengono addestrate con la regola del percettrone se la funzione
 * di attivazione è Step e con la delta rule altrimenti; le reti con più strati con la backpropagation.
 *
 * @author Damiano
 */
public class ReteNeurale implements IReteNeurale
{
	/**
	 * Nome della rete
	 */
	private String nome;
	/**
	 * Lista degli strati contenuti nella rete
	 */
	private ArrayList<StratoDiNeuroni> layers;
	
	/**
	 * @param nome Nome della rete
	 * @param layers Lista degli strati di neuroni contenuti nella rete
	 */
	protected ReteNeurale(String nome, ArrayList<StratoDiNeuroni> layers)
	{
		this.nome = nome;
		this.layers = layers;
	}
	
	@Override
	public double[] process(double[] values) 
	{
		double[] risultato = values;
		for (StratoDiNeuroni s: layers)
			risultato = s.process(risultato);
		return risultato;
	}

	@Override
	public double trainIstanza(double[] values, double[] output)
	{
		double[] outputRete = process(values);
		
		double errore = 0;
		for (int i = 0; i < output.length; i++)
			errore += Math.abs(output[i] - outputRete[i]); 
		
		if (layers.size() == 1 && layers.get(0).getFunzioneDiAttivazione().equals("Step"))
			layers.get(0).trainPercettrone(values, output, outputRete);
		else if (layers.size() == 1)
			layers.get(0).trainOneLayer(values, output, outputRete);
		else
			trainMultiLayer(values, output, outputRete);

		return errore;
	}

	/**
	 * Addestra la rete con la backpropagation: l'errore dell'ultimo strato viene riportato all'indietro
	 * per calcolare l'errore delta di ogni neurone degli strati precedenti, poi ogni strato aggiorna i pesi.
	 *
	 * @param values Array di valori di input per i quali addestrare la rete
	 * @param output Array di valori di output attesi associati agli input per i quali addestrare la rete
	 * @param outputRete Array di valori di output effettivi della rete
	 */
	private void trainMultiLayer(double[] values, double[] output, double[] outputRete)
	{
		ArrayList<double[]> inputStrati = new ArrayList<>();
		double[] risultato = values;
		for (StratoDiNeuroni s : layers)
		{
			inputStrati.add(risultato);
			risultato = s.process(risultato);
		}

		StratoDiNeuroni ultimo = layers.get(layers.size()-1);
		double[] inputUltimo = inputStrati.get(layers.size()-1);
		double[] delta = IntStream.range(0, output.length)
				.mapToDouble(j -> (output[j] - outputRete[j]) * ultimo.getStrato().get(j).derivata(inputUltimo)).toArray();

		for (int l = layers.size()-1; l >= 0; l--)
		{
			StratoDiNeuroni strato = layers.get(l);
			double[] deltaStrato = delta;

			if (l > 0)
			{
				StratoDiNeuroni precedente = layers.get(l-1);
				double[] inputPrecedente = inputStrati.get(l-1);
				delta = IntStream.range(0, precedente.getStrato().size())
						.mapToDouble(k -> precedente.getStrato().get(k).derivata(inputPrecedente) *
								IntStream.range(0, deltaStrato.length).mapToDouble(j -> deltaStrato[j] * strato.getStrato().get(j).getPesiT()[k]).sum())
						.toArray();
			}
			strato.trainMultiLayer(inputStrati.get(l), deltaStrato);
		}
	}

	@Override
	public void train(double[][] inputs, double[][] outputs)
	{
		double sommaErrori = 0;
		do
		{
			sommaErrori = 0;
			for (int i = 0; i < outputs.length;i++)
				sommaErrori += trainIstanza(inputs[i], outputs[i]);
		} while (sommaErrori > 0.3);
	}
	
	@Override
	public String getNome()
	{	
		return nome;
	}
	
	/**
	 * Serializza la rete nello stesso formato letto da {@link IReteNeurale#carica(String)},
	 * pesi inclusi, così che possa essere salvata e ricaricata.
	 */
	@Override
	public String toString()
	{
		StringBuilder str = new StringBuilder("nome=").append(getNome()).append("\n");
		for (StratoDiNeuroni layer : layers)
		{
			str.append("layer={").append(" nome=").append(layer.getNome()).append(" activationFunction=").append(layer.getFunzioneDiAttivazione())
			.append(" inputUnits=").append(layer.getnInput()).append(" outputUnits=").append(layer.getStrato().size()).append(" weights=[");
			for (Neurone n : layer.getStrato())
			{
				// 1.0 -> 1, come nei file di rete
				if (layer.getStrato().indexOf(n) != (layer.getStrato().size()-1))
					str.append(Arrays.toString(n.getPesiT()).replaceAll(Pattern.quote(".0, "), ",")
					.replaceAll(Pattern.quote(".0]"), "\\]").replaceAll(" ", "")+ ",");		
				else
					str.append(Arrays.toString(n.getPesiT()).replaceAll(Pattern.quote(".0, "), ",")
					.replaceAll(Pattern.quote(".0]"), "\\]").replaceAll(" ", ""));
			}
			String s = layers.indexOf(layer) != (layers.size()-1) ? "] }\n" : "] }";
			str.append(s);	
		}	
		return 	str.toString();
	}
}
