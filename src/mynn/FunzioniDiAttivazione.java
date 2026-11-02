package mynn;

/**
 * Funzioni di attivazione dei neuroni e relative derivate.
 * <p>
 * I metodi sono richiamati per nome dal file della rete; la derivata ha suffisso "D".
 *
 * @author Damiano
 */
public class FunzioniDiAttivazione
{
	/**
	 * Funzione gradino: 0 se x è negativo, altrimenti 1. Usata dal percettrone.
	 */
	public static double Step(Double x)
	{
		return x < 0 ? 0 : 1 ;
	}

	/**
	 * Funzione identità: restituisce x invariato.
	 */
	public static double Identity(Double x)
	{
		return x;
	}

	/**
	 * Sigmoide logistica, con output in (0, 1).
	 */
	public static double Logistic(Double x)
	{
		return 1 / (1+ Math.exp(-x));
	}

	/**
	 * Tangente iperbolica, con output in (-1, 1).
	 */
	public static double TanH(Double x)
	{
		return (2 / (1 + Math.exp(-2*x))) -1;
	}

	/**
	 * Rectified Linear Unit: 0 se x è negativo, altrimenti x.
	 */
	public static double ReLU(Double x)
	{
		return x < 0 ? 0 : x;
	}

	/**
	 * Derivata di {@link #Identity}.
	 */
	public static double IdentityD(Double x)
	{
		return 1;
	}

	/**
	 * Derivata di {@link #Logistic}: f(x) * (1 - f(x)).
	 */
	public static double LogisticD(Double x)
	{
		return Logistic(x)*(1-Logistic(x));
	}

	/**
	 * Derivata di {@link #TanH}: 1 - f(x)^2.
	 */
	public static double TanHD(Double x)
	{
		return 1-Math.pow(TanH(x),2);
	}

	/**
	 * Derivata di {@link #ReLU}.
	 */
	public static double ReLUD(Double x)
	{
		return x < 0 ? 0 : 1;
	}
}
