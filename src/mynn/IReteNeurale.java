package mynn;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Rete neurale feed-forward composta da una sequenza di strati di neuroni.
 */
public interface IReteNeurale {

	/**
	 * Calcola l'output della rete propagando l'input attraverso tutti gli strati.
	 *
	 * @param values Input da processare
	 * @return Risultato della rete
	 */
	public double[] process(double[] values) ;

	/**
	 * Esegue un singolo passo di addestramento su una coppia input/output attesa.
	 *
	 * @param values Array di valori di input per i quali addestrare la rete
	 * @param output Array di valori di output attesi associati agli input per i quali addestrare la rete
	 * @return Errore calcolato tra l'output atteso e l'output effettivo della rete
	 */
	public double trainIstanza(double[] values, double output[]);

	/**
	 * Addestra la rete ripetendo epoche sull'intero insieme di esempi
	 * finché l'errore totale di un'epoca non scende sotto 0.3.
	 *
	 * @param inputs Matrice degli input associati agli output attesi dalla rete per l'addestramento
	 * @param outputs Matrice degli output associati agli input dalla rete per l'addestramento
	 */
	public void train(double[][] inputs, double[][] outputs);

	/**
	 * @return Nome della rete
	 */
	public String getNome();

	/**
	 * Crea una rete a partire da un file di testo. La prima riga contiene il nome
	 * ({@code nome=...}), ogni riga successiva descrive uno strato:
	 * <pre>
	 * layer={ nome=input activationFunction=Logistic inputUnits=2 outputUnits=2 weights=[[1,1,0],[2,2,0]] }
	 * </pre>
	 * Ogni neurone ha {@code inputUnits + 1} pesi, l'ultimo dei quali è il threshold.
	 * Se {@code weights} è assente i pesi vengono inizializzati casualmente in [0, 1).
	 *
	 * @param filename Nome del file da cui caricare la rete
	 * @return ReteNeurale creata dal file
	 * @throws IOException se il file non è leggibile o se il numero di neuroni di uno strato
	 *                     non coincide con il numero di input dello strato successivo
	 */
	public static IReteNeurale carica(String filename) throws IOException
	{
		
		BufferedReader scan = new BufferedReader(new FileReader(filename));
		ArrayList<String> lines = scan.lines().collect(Collectors.toCollection(ArrayList::new));
		ArrayList<StratoDiNeuroni> layers = new ArrayList<>();
		scan.close();
		
		for (String l : lines.subList(1,lines.size()))
		{
			List<String> elementi = Arrays.asList(l.split(" "));
			String nome = elementi.stream().filter(s -> s.startsWith("nome=")).map(s -> s.substring(5)).findFirst().get();
			String funzione = elementi.stream().filter(s -> s.startsWith("activationFunction=")).map(s -> s.substring(19)).findFirst().get();
			int nInput = Integer.parseInt(elementi.stream().filter(s -> s.startsWith("inputUnits=")).map(s -> s.substring(11)).findFirst().get());
			int nOutput = Integer.parseInt(elementi.stream().filter(s -> s.startsWith("outputUnits=")).map(s -> s.substring(12)).findFirst().get());
			
			ArrayList<Neurone> neuroni = new ArrayList<>();
			double[] pesiT; 
			Optional<String> strPesi = elementi.stream().filter(s -> s.startsWith("weights=")).map(s -> s.substring(8)).findFirst();
			
			if (strPesi.isPresent())
			{
				String st = strPesi.get().substring(1,strPesi.get().length()-1);
				String[] items = st.split("\\]");
				items = Arrays.stream(items).map(i -> i.replaceAll("\\[","")).toArray(String[]::new);
				for (int i = 0; i < items.length; i++)
				{
					if (items[i].startsWith(","))
						items[i] = items[i].substring(1);
					
					pesiT = Arrays.stream(items[i].split(",")).mapToDouble(Double::valueOf).toArray();
					neuroni.add(new Neurone(funzione,pesiT));
				}
			}
			else
			{
				neuroni = IntStream.range(0,nOutput).mapToObj(i -> new Neurone(funzione, new Random().doubles(nInput+1, 0, 1).toArray()))
						.collect(Collectors.toCollection(ArrayList::new));
			}
			
			StratoDiNeuroni strato = new StratoDiNeuroni(funzione, neuroni, nome, nInput);
			layers.add(strato);
		}
		
		for (int i = 0;i < layers.size()-1;i++)
		{
			if (layers.get(i).getStrato().size() != layers.get(i+1).getnInput())
				throw new IOException();
		}
		String nomeRete = lines.get(0).substring(5);
		return new ReteNeurale(nomeRete,layers);
	}
	

}
