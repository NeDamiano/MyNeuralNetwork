package mynn;

import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test che stampano a console il comportamento delle reti di esempio,
 * prima e dopo l'addestramento.
 */
public class TestReteNeuraleStampa {

	private static final double[][] INPUT_LOGICI = {{0,0},{0,1},{1,0},{1,1}};

	private static String formatta(double[] valori)
	{
		return Arrays.stream(valori).mapToObj(v -> String.format(Locale.ROOT, "%.3f", v)).collect(Collectors.joining(", ", "[", "]"));
	}

	private static void stampaRisultati(IReteNeurale rete, double[][] inputs)
	{
		for (double[] in : inputs)
			System.out.println("  " + formatta(in) + " -> " + formatta(rete.process(in)));
	}

	/**
	 * Addestra la rete un'epoca alla volta, stampando l'errore, finché l'errore totale
	 * non scende sotto la soglia indicata.
	 */
	private static void addestraStampando(IReteNeurale rete, double[][] inputs, double[][] outputs, double soglia, int ogniEpoche)
	{
		int epoca = 0;
		double errore;
		do
		{
			errore = 0;
			for (int i = 0; i < inputs.length; i++)
				errore += rete.trainIstanza(inputs[i], outputs[i]);
			epoca++;
			if (epoca % ogniEpoche == 0 || errore <= soglia)
				System.out.println(String.format(Locale.ROOT, "  epoca %5d  errore %.4f", epoca, errore));
		} while (errore > soglia);
	}

	@Test
	public void testPorteLogiche() throws IOException
	{
		for (String nome : new String[]{"reteAnd", "reteOr", "reteXor"})
		{
			IReteNeurale rete = IReteNeurale.carica("reti/" + nome);
			System.out.println("== " + rete.getNome());
			stampaRisultati(rete, INPUT_LOGICI);
			System.out.println();
		}
	}

	@Test(timeout = 10000)
	public void testTrainingPercettroneAnd() throws IOException
	{
		IReteNeurale rete = IReteNeurale.carica("reti/reteAnd2");
		double[][] outputs = {{0},{0},{0},{1}};

		System.out.println("== Training percettrone: " + rete.getNome());
		System.out.println("Pesi iniziali (casuali):\n" + rete);
		addestraStampando(rete, INPUT_LOGICI, outputs, 0, 1);
		System.out.println("Pesi appresi:\n" + rete);
		stampaRisultati(rete, INPUT_LOGICI);
		System.out.println();

		for (int i = 0; i < INPUT_LOGICI.length; i++)
			Assert.assertEquals(outputs[i][0], rete.process(INPUT_LOGICI[i])[0], 0.01);
	}

	@Test(timeout = 30000)
	public void testTrainingDeltaRule() throws IOException
	{
		IReteNeurale rete = IReteNeurale.carica("reti/reteSum");
		double[][] inputs = {
				{0.15, 0.20}, {0.30, 0.25}, {0.20, 0.35}, {0.35, 0.10},
				{0.80, 0.75}, {0.70, 0.85}, {0.90, 0.65}, {0.75, 0.90}};
		double[][] outputs = {
				{0,1}, {0,1}, {0,1}, {0,1},
				{1,0}, {1,0}, {1,0}, {1,0}};

		System.out.println("== Training delta rule: " + rete.getNome());
		addestraStampando(rete, inputs, outputs, 0.3, 500);
		System.out.println("Pesi appresi:\n" + rete);
		System.out.println("Punti mai visti in addestramento:");
		stampaRisultati(rete, new double[][]{{0.25, 0.25}, {0.10, 0.40}, {0.75, 0.75}, {0.95, 0.80}});
		System.out.println();

		Assert.assertTrue(rete.process(new double[]{0.25, 0.25})[1] > 0.9);
		Assert.assertTrue(rete.process(new double[]{0.75, 0.75})[0] > 0.9);
	}
}
