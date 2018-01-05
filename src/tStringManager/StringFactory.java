package tStringManager;

import tMethods.*;

import java.util.*;

public abstract class StringFactory {
	public static void main(String args[]) {
		StringFactory fac = StringFactory.compile("#?(That is #?(an apple|an orange|a banana)|Those are #?%(49:2|50:3|1:infinite) #?(apples|oranges|bananas))");
		System.out.println();
		for (int i = 0; i < 10; i++) {
			System.out.println(fac.build(null));
		}
	}

	public final String pattern;

	public StringFactory(String pattern) {
		this.pattern = pattern;
	}

	public static StringFactory compile(String pattern) {
		List<StringFactory> factories = new ArrayList<>();
		char[] chars = pattern.toCharArray();

		boolean inQuotes = false;
		boolean literalNext = false;
		int currentGroupDepth = 0;
		String currentFactoryType = null;

		StringBuilder currentString = new StringBuilder();
		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];

			if (literalNext) { // \\?
				currentString.append(c);
				literalNext = false;
				continue;
			}

			if (inQuotes) {
				switch (c) {
					case '"':
						inQuotes = false;
						break;
					case '\\':
						literalNext = true;
						break;
					default:
						currentString.append(c);
						break;
				}
			} else {
				switch (c) {
					case '\\':
						literalNext = true;
						break;
					case '"':
						inQuotes = true;
						break;
					case '#':
						currentGroupDepth++;
						if (currentGroupDepth == 1) {
							if (currentString.length() > 0) {
								factories.add(new PlainStrFactory(currentString.toString()));
							}
							currentString = new StringBuilder();
							// in "#abc(...)", find "abc" and set i to skip "("
							for (int i2 = i; i2 < pattern.length(); i2++) {
								if (chars[i2] == '(') {
									currentFactoryType = pattern.substring(i + 1, i2);
									i = i2;
									break;
								}
							}
						} else {
							currentString.append('#');
						}
						break;
					case ')':
						if (currentGroupDepth > 0) {
							currentGroupDepth--;
							if (currentGroupDepth == 0) {
								if (currentFactoryType == null) {
									System.out.println("ERROR: No factory type");
									break;
								}

								switch (currentFactoryType) {
									case "?":
										factories.add(new RandStrFactory(currentString.toString()));
										break;
									case "?%":
										factories.add(new WeightedRandStrFactory(currentString.toString()));
										break;
									case "*":
//										factories.add(new RepeaterStrFactory(currentString.toString()));
										break;
								}
								currentFactoryType = null;
								currentString = new StringBuilder();
							} else {
								currentString.append(')');
							}
						} else {
							currentString.append(')');
						}
						break;
					default:
						currentString.append(c);
				}
			}
		}
		if (currentString.length() > 0) {
			factories.add(new PlainStrFactory(currentString.toString()));
		}

		return factories.size() == 1 ? factories.get(0) : new SequenceStrFactory(pattern, factories);
	}

	/**
	 * "abc"	// interprets plain Strings
	 */
	private static class PlainStrFactory extends StringFactory {
		public PlainStrFactory(String pattern) {
			super(pattern);
		}

		@Override
		public String build(Object... params) {
			return pattern;
		}
	}

	/**
	 * "abc #(...) def"	// combines multiple StringFactories
	 */
	private static class SequenceStrFactory extends StringFactory {
		List<StringFactory> vals = new ArrayList<>();

		public SequenceStrFactory(String pattern, List<StringFactory> factories) {
			super(pattern);
			vals = factories;
		}

		@Override
		public String build(Object[] params) {
			StringBuilder builder = new StringBuilder();
			for (StringFactory fac : vals) {
				builder.append(fac.build(params));
			}
			return builder.toString();
		}
	}

	/**
	 * "#*[4]()"		// repeat 4 times
	 * "#*[1:4]()"		// repeat between 1 and 4 times, inclusive
	 * "#*[!1:4]()"	// repeat built string, not builder
	 */
	private static class RepeaterStrFactory extends StringFactory {
		private static Random random = new Random();
		private StringFactory val;
		private boolean persistant;
		private int min;
		private int max;

		public RepeaterStrFactory(String pattern, String param) {
			super(pattern);
			val = compile(pattern);

			if (param.charAt(0) == '!') {
				param = param.substring(1);
				persistant = true;
			}

			int split = param.indexOf(':');
			if (split < 0) {
				min = Integer.parseInt(param);
				max = min;
			} else {
				min = Integer.parseInt(param.substring(0, split));
				max = Integer.parseInt(param.substring(split + 1));
			}
		}

		@Override
		public String build(Object[] params) {
			int repeat = TMath.randInt(min, max);
			StringBuilder builder;
			if (persistant) {
				String str = val.build(params);
				builder = new StringBuilder(str.length() * repeat);
				for (int i = 0; i < repeat; i++) {
					builder.append(str);
				}
			} else {
				builder = new StringBuilder();
				for (int i = 0; i < repeat; i++) {
					builder.append(val.build(params));
				}
			}
			return builder.toString();
		}
	}

	/**
	 * "#?(a|b|c)"	// picks random value from group
	 */
	private static class RandStrFactory extends StringFactory {
		private static Random random = new Random();
		private List<StringFactory> vals = new ArrayList<>();

		public RandStrFactory(String pattern) {
			super(pattern);
			int prev = 0;
			int next = 0;
			int depth = 0;
			char[] chars = pattern.toCharArray();
			for (; next < pattern.length(); next++) {
				char c = chars[next];
				if (depth == 0) {
					if (c == '|') {
						vals.add(StringFactory.compile(pattern.substring(prev, next)));
						next += 1;
						prev = next;
					}
				} else {
					if (c == ')') {
						depth--;
					}
				}
				if (c == '#') {
					depth++;
				}
			}
			vals.add(StringFactory.compile(pattern.substring(prev)));
		}

		@Override
		public String build(Object[] params) {
			return vals.get(random.nextInt(vals.size())).build(params);
		}
	}

	/**
	 * "#?%(15:a|5:b|0.1:c)"	// picks random value from group, weighted - total weight can be any positive number
	 */
	private static class WeightedRandStrFactory extends StringFactory {
		private static Random random = new Random();
		private List<Double> weights = new ArrayList<>();
		private List<StringFactory> vals = new ArrayList<>();

		public WeightedRandStrFactory(String pattern) {
			super(pattern);
			int prev = 0;
			int next = 0;
			int depth = 0;
			char[] chars = pattern.toCharArray();

			List<String> strs = new ArrayList<>();
			for (; next < pattern.length(); next++) {
				char c = chars[next];
				if (depth == 0) {
					if (c == '|') {
						strs.add(pattern.substring(prev, next));
						next += 1;
						prev = next;
					}
				} else {
					if (c == ')') {
						depth--;
					}
				}
				if (c == '#') {
					depth++;
				}
			}
			strs.add(pattern.substring(prev));

			double totalWeight = 0;
			for (String str : strs) {
				int split = str.indexOf(':');
				double weight = Double.parseDouble(str.substring(0, split));
				totalWeight += weight;
				weights.add(weight);
				vals.add(compile(str.substring(split + 1)));
			}
			double prevWeight = 0;
			for (int i = 0; i < weights.size(); i++) {
				double weight = weights.get(i);
				weight /= totalWeight;
				weight += prevWeight;
				prevWeight = weight;
				weights.set(i, weight);
			}
		}

		@Override
		public String build(Object[] params) {
			float rand = random.nextFloat();
			for (int i = 0; i < weights.size(); i++) {
				if (rand <= weights.get(i)) {
					return vals.get(i).build(params);
				}
			}
			return vals.get(vals.size() - 1).build(params);
		}
	}

	public abstract String build(Object[] params);
}