package tMethods;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class TNoise {
	public static void main(String args[]) {
		char[] ascii = { ' ', '.', ',', '-', ':', '*', 'x', '%', '#', '@', 'M' };

		NoiseGen1D gen1 = new NoiseGen1D();
		NoiseGen2D gen2 = new NoiseGen2D();

		long t = System.currentTimeMillis();
		// for(double x = 0; x<100; x+= 0.1) {
		// int val = (int)(gen1.getVal(x) * 20 + 10);
		// StringBuilder str = new StringBuilder();
		// for(int i=0; i<val; i++) {
		// str.append('#');
		// }
		// System.out.println(str);
		// }
		
		int n = 128;
		int[][] vals = new int[n][n];
		for (int x = 0; x < n; x ++) {
			for (int y = 0; y < n; y ++) {
				vals[x][y] = (int) ((gen2.getVal(x / 16.0, y / 16.0) / 2 + 0.5) * 9);
				gen2.getVal(x/16.0, y/16.0);
			}
		}
		System.out.println((System.currentTimeMillis() - t) / 1000.0);

		for(int[] row : vals) {
			StringBuilder build = new StringBuilder();
			for(int c : row) {
				build.append(ascii[c] + " ");
			}
			System.out.println(build);
		}
	}

	public static class NoiseGen1D {
		private int tiling;
		private long seed;
		private Map<Integer, Double> scalars = new HashMap<Integer, Double>();

		public NoiseGen1D() {
			this(System.nanoTime(), 0);
		}

		public NoiseGen1D(long seed) {
			this(seed, 0);
		}

		public NoiseGen1D(long seed, int tiling) {
			this.seed = seed;
			this.tiling = Math.abs(tiling);
		}

		public void clearScalars() {
			scalars.clear();
		}
		
		public void clearScalars(Integer[] coords) {
			for(Integer coord : coords) {
				scalars.remove(coord);
			}
		}
		
		private double scalar(int index) {
			int fixedIndex = index;
			if (tiling != 0) {
				fixedIndex %= tiling;
			}
			Double d = scalars.get(fixedIndex);
			if (d == null) {
				Random rand = new Random(seed + index);
//				rand.nextBoolean();
				d = rand.nextDouble();
				scalars.put(fixedIndex, d);
			}
			return d;
		}

		public double getVal(double x) {
			int xInt = (int) x;
			double scalarA = scalar(xInt);
			double scalarB = scalar(xInt + 1);
			return TMath.lerp(scalarA, scalarB, x - xInt);
		}
	}

	public static class NoiseGen2D {
		private int tiling;
		private long seed;
		private Map<Integer[], Double[]> vectors = new HashMap<Integer[], Double[]>();
		private Random rand = new Random();
		/**
		 * Forget all calculated vectors
		 */
		public void clearVectors() {
			vectors.clear();
		}
		
		/**
		 * Forget specific vectors
		 */
		public void clearVectors(Integer[]...coords) {
			for(Integer[] coord : coords) {
				vectors.remove(coord);
			}
		}
			 	
		public NoiseGen2D() {
			this(System.nanoTime(), 0);
		}

		public NoiseGen2D(long seed) {
			this(seed, 0);
		}

		public NoiseGen2D(long seed, int tiling) {
			this.seed = seed;
			this.tiling = Math.abs(tiling);
		}

		private double vector(double x, double y, int xInd, int yInd) {
			int fixedX = xInd;
			int fixedY = yInd;
			if (tiling != 0) {
				fixedX %= tiling;
				fixedY %= tiling;
			}
			Integer[] coord = { fixedX, fixedY };
			Double[] vec = vectors.get(coord);
			if (vec == null) {
				rand.setSeed(seed + fixedX + fixedY * fixedY);
				rand.nextBoolean();
				double theta = rand.nextDouble() * 6.283153;
				vec = new Double[] { Math.sin(theta), Math.cos(theta) };
				vectors.put(coord, vec);
			}
			return vec[0] * (x - xInd) + vec[1] * (y - yInd);
		}

		public double getVal(double x, double y) {
			int xIntA = (int) x;
			int yIntA = (int) y;
			int xIntB = xIntA + 1;
			int yIntB = yIntA + 1;
			double nn = vector(x, y, xIntA, yIntA);
			double np = vector(x, y, xIntA, yIntB);
			double pn = vector(x, y, xIntB, yIntA);
			double pp = vector(x, y, xIntB, yIntB);
			double relX = x - xIntA;
			return TMath.lerp(TMath.lerp(nn, pn, relX), TMath.lerp(np, pp, relX), y - yIntA);
		}
	}
}
