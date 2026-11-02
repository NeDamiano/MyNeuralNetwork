package mynn;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Assert;
import org.junit.Test;

public class TestReteNeuraleNoTrain {
	
	@Test
	public void testCaricaReteAnd() throws IOException {
		try {
			StringBuilder str = new StringBuilder();
			Files.newBufferedReader(Paths.get(new File(".").getCanonicalPath() + "/reti/reteAnd")).lines().forEach(l -> {
				str.append(l);
				str.append("\n");
			});
			IReteNeurale rete = IReteNeurale.carica(new File(".").getCanonicalPath() + "/reti/reteAnd");
			Assert.assertTrue(Math.abs(rete.process(new double[]{0.0,0.0})[0]) < 0.01);
			Assert.assertTrue(Math.abs(rete.process(new double[]{1.0,0.0})[0]) < 0.01);
			Assert.assertTrue(Math.abs(rete.process(new double[]{0.0,1.0})[0]) < 0.01);
			Assert.assertTrue(Math.abs(1.0 - rete.process(new double[]{1.0,1.0})[0]) < 0.01);
		
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	@Test
	public void testCaricaToStringAnd() throws IOException {
		try {
			StringBuilder str = new StringBuilder();
			Files.newBufferedReader(Paths.get(new File(".").getCanonicalPath() + "/reti/reteAnd")).lines().forEach(l -> {
				str.append(l);
				str.append("\n");
			});
			IReteNeurale rete = IReteNeurale.carica(new File(".").getCanonicalPath() + "/reti/reteAnd");
			
			Assert.assertEquals(str.toString().trim(), rete.toString());
			
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	@Test
	public void testCaricaNome() throws IOException {
		try {
			StringBuilder str = new StringBuilder();
			Files.newBufferedReader(Paths.get(new File(".").getCanonicalPath() + "/reti/reteAnd")).lines().forEach(l -> {
				str.append(l);
				str.append("\n");
			});
			IReteNeurale rete = IReteNeurale.carica(new File(".").getCanonicalPath() + "/reti/reteAnd");

			Assert.assertEquals("ReteAnd", rete.getNome());
			
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	@Test
	public void testCaricaReteOr() throws IOException {
		try {
			StringBuilder str = new StringBuilder();
			Files.newBufferedReader(Paths.get(new File(".").getCanonicalPath() + "/reti/reteOr")).lines().forEach(l -> {
				str.append(l);
				str.append("\n");
			});
			IReteNeurale rete = IReteNeurale.carica(new File(".").getCanonicalPath() + "/reti/reteOr");
			Assert.assertTrue(Math.abs(rete.process(new double[]{0.0,0.0})[0]) < 0.001);
			Assert.assertTrue(Math.abs(1.0 - rete.process(new double[]{1.0,0.0})[0]) < 0.01);
			Assert.assertTrue(Math.abs(1.0 -rete.process(new double[]{0.0,1.0})[0]) < 0.01);
			Assert.assertTrue(Math.abs(1.0 - rete.process(new double[]{1.0,1.0})[0]) < 0.01);
		
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	@Test
	public void testCaricaToStringOr() throws IOException {
		try {
			StringBuilder str = new StringBuilder();
			Files.newBufferedReader(Paths.get(new File(".").getCanonicalPath() + "/reti/reteOr")).lines().forEach(l -> {
				str.append(l);
				str.append("\n");
			});
			IReteNeurale rete = IReteNeurale.carica(new File(".").getCanonicalPath() + "/reti/reteOr");
			
			Assert.assertEquals(str.toString().trim(), rete.toString());
			
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	@Test
	public void testCaricaReteXor() throws IOException {
		try {
			StringBuilder str = new StringBuilder();
			Files.newBufferedReader(Paths.get(new File(".").getCanonicalPath() + "/reti/reteXor")).lines().forEach(l -> {
				str.append(l);
				str.append("\n");
			});
			IReteNeurale rete = IReteNeurale.carica(new File(".").getCanonicalPath() + "/reti/reteXor");
			Assert.assertTrue(Math.abs(rete.process(new double[]{0.0,0.0})[0]) < 0.001);
			Assert.assertTrue(Math.abs(1.0 - rete.process(new double[]{1.0,0.0})[0]) < 0.01);
			Assert.assertTrue(Math.abs(1.0 - rete.process(new double[]{0.0,1.0})[0]) < 0.01);
			Assert.assertTrue(Math.abs(rete.process(new double[]{1.0,1.0})[0]) < 0.01);
		
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

}
