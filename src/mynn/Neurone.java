package mynn;

import java.lang.reflect.InvocationTargetException;
import java.util.stream.IntStream;

/**
 * Singolo neurone: somma pesata degli input più un threshold, seguita da una funzione di attivazione.
 *
 * @author Damiano
 */
public class Neurone
{
	/**
	 * Pesi degli input; l'ultimo elemento è il threshold
	 */
	private double[] pesiT;
	/**
	 * Funzione utilizzata dal neurone
	 */
	private String funzione;
	/**
	 * Classe contenente tutte le funzioni di attivazione
	 */
	private static FunzioniDiAttivazione func = new FunzioniDiAttivazione(); 
	/**
	 * Costante di apprendimento
	 */
	private final double C_APPRENDIMENTO = 0.2;
	
	/**
	 * @param funzione Funzione di attivazione del neurone
	 * @param pesiT Array dei pesi più il threshold, se presente, altrimenti = 0 in fase di process 
	 */
	protected Neurone(String funzione, double[] pesiT)
	{
		this.funzione = funzione;
		this.pesiT = pesiT;
	}
	
	/**
	 * Calcola f(w·x + threshold), dove f è la funzione di attivazione del neurone.
	 *
	 * @param values Input del Neurone
	 * @return Output calcolato del Neurone
	 */
	public double process(double[] values) 
	{		
		double risultato = IntStream.range(0, pesiT.length-1).mapToDouble(n -> values[n] * pesiT[n]).sum();
		
		try {
			risultato = (Double) func.getClass().getMethod(funzione, Double.class).invoke(func, risultato + pesiT[pesiT.length-1]);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException	| SecurityException e) {
			throw new RuntimeException();
		}
		return risultato;
	}

	/**
	 * Calcola f'(w·x + threshold), dove f' è la derivata della funzione di attivazione del neurone.
	 *
	 * @param values Input del Neurone
	 * @return Derivata della funzione di attivazione calcolata sugli input del Neurone
	 */
	public double derivata(double[] values)
	{
		double risultato = IntStream.range(0, pesiT.length-1).mapToDouble(n -> values[n] * pesiT[n]).sum();

		try {
			risultato = (Double) func.getClass().getMethod(funzione + "D", Double.class).invoke(func, risultato + pesiT[pesiT.length-1]);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			throw new RuntimeException();
		}
		return risultato;
	}

	/**
	 * Aggiorna i pesi con la regola del percettrone: w += C * (atteso - effettivo) * x.
	 *
	 * @param values Array di valori in input alla rete per i quali addestrare il neurone
	 * @param output Valore di output atteso dal neurone
	 * @param outputRete Valore di output effettivo del neurone
	 */
	public void trainPercettrone(double[] values, double output, double outputRete)
	{
		for (int i = 0; i < pesiT.length-1; i++)
			pesiT[i] = pesiT[i] + C_APPRENDIMENTO * (output - outputRete) * values[i] ; 		
		
		pesiT[pesiT.length-1] = pesiT[pesiT.length-1] + C_APPRENDIMENTO * (output - outputRete);
	}
	
	/**
	 * Aggiorna i pesi con la delta rule: w += C * (atteso - effettivo) * f'(net) * x,
	 * dove f' è la derivata della funzione di attivazione.
	 *
	 * @param values Array di valori in input alla rete per i quali addestrare il neurone
	 * @param output Valore di output atteso dal neurone
	 * @param outputRete Valore di output effettivo del neurone
	 */
	public void trainOneLayer(double[] values, double output, double outputRete)
	{
		double derivata = derivata(values);
		for (int i = 0; i < pesiT.length-1; i++)
			pesiT[i] = pesiT[i] + C_APPRENDIMENTO * (output - outputRete) * derivata * values[i];

		pesiT[pesiT.length-1] = pesiT[pesiT.length-1] + C_APPRENDIMENTO * (output - outputRete) * derivata;
	}

	/**
	 * Aggiorna i pesi con l'errore delta calcolato dalla backpropagation: w += C * delta * x.
	 *
	 * @param values Input del Neurone
	 * @param delta Errore del Neurone, già moltiplicato per la derivata della funzione di attivazione
	 */
	public void trainMultiLayer(double[] values, double delta)
	{
		for (int i = 0; i < pesiT.length-1; i++)
			pesiT[i] = pesiT[i] + C_APPRENDIMENTO * delta * values[i];

		pesiT[pesiT.length-1] = pesiT[pesiT.length-1] + C_APPRENDIMENTO * delta;
	}

	/**
	 * @return Array dei pesi più il threshold
	 */
	public double[] getPesiT()
	{
		return pesiT;
	}
}
