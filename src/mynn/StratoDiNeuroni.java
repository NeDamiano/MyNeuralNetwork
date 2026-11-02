package mynn;

import java.util.ArrayList;
import java.util.stream.IntStream;

/**
 * Strato della rete: un insieme di neuroni che ricevono lo stesso input
 * e condividono la stessa funzione di attivazione.
 *
 * @author Damiano
 */
public class StratoDiNeuroni
{
	/**
	 * Nome dello strato
	 */
	private String nome;
	/**
	 * Lista dei neuroni contenuti nello strato
	 */
	private ArrayList<Neurone> strato;
	/**
	 * Funzione di attivazione utilizzata dai neuroni dello strato
	 */
	private String funzione;
	/**
	 * Numeri di input che ciascun neurone dello strato richiede
	 */
	private int nInput;
	
	/**
	 * @param funzione Funzione di attivazione dei neuroni dello strato
	 * @param neuroni Lista dei neuroni dello strato
	 * @param nome Nome dello strato
	 * @param nInput Numero di input che i neuroni dello strato richiedono
	 */
	protected StratoDiNeuroni(String funzione, ArrayList<Neurone> neuroni, String nome, int nInput)
	{
		this.funzione = funzione;
		this.strato = neuroni;
		this.nome = nome;
		this.nInput = nInput;
	}
	
	/**
	 * Calcola l'output di ogni neurone dello strato sullo stesso input.
	 *
	 * @param values Input da passare ad ognuno dei neuroni dello strato
	 * @return Array degli output calcolati dai neuroni dello strato
	 */
	public double[] process(double[] values)  
	{
		return strato.stream().map(n -> n.process(values)).mapToDouble(Double::valueOf).toArray();
	}
	
	/**
	 * Addestra ogni neurone dello strato con la regola del percettrone.
	 *
	 * @param values Array di valori di input per i quali addestrare la rete
	 * @param output Array di valori di output attesi associati agli input per i quali addestrare la rete
	 * @param outputRete Array di valori di output effettivi della rete
	 */
	public void trainPercettrone(double[] values, double[] output, double[] outputRete)
	{
		IntStream.range(0, output.length).forEach(j -> strato.get(j).trainPercettrone(values, output[j], outputRete[j]));
	}
	
	/**
	 * Addestra ogni neurone dello strato con la delta rule.
	 *
	 * @param values Array di valori di input per i quali addestrare la rete
	 * @param output Array di valori di output attesi associati agli input per i quali addestrare la rete
	 * @param outputRete Array di valori di output effettivi della rete
	 */
	public void trainOneLayer(double[] values, double[] output, double[] outputRete)
	{
		IntStream.range(0, output.length).forEach(j -> strato.get(j).trainOneLayer(values, output[j], outputRete[j]));
	}

	/**
	 * Addestra ogni neurone dello strato con il proprio errore delta calcolato dalla backpropagation.
	 *
	 * @param values Array di valori di input dello strato
	 * @param delta Array degli errori delta dei neuroni dello strato
	 */
	public void trainMultiLayer(double[] values, double[] delta)
	{
		IntStream.range(0, delta.length).forEach(j -> strato.get(j).trainMultiLayer(values, delta[j]));
	}
	
	/**
	 * @return Nome dello strato
	 */
	public String getNome()
	{
		return nome;
	}
	
	/**
	 * @return Funzione di attivazione in comune a tutti i neuroni dello strato
	 */
	public String getFunzioneDiAttivazione()
	{
		return funzione;
	}
	
	/**
	 * @return Numero di input richiesto dai neuroni dello strato
	 */
	public int getnInput()
	{
		return nInput;
	}
	
	/**
	 * @return Lista dei neuroni dello strato
	 */
	public ArrayList<Neurone> getStrato()
	{
		return strato;
	}
}
