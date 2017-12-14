package tStringManager;

import java.util.regex.Pattern;

public abstract class TStringChooser {
	private Object[] args;
	public abstract boolean matches(String group, String type);

	public abstract String toString();

	public final void constructor(String arg, Object[] argList){
		args = argList;
	}
	
	public Object getArg(int index){
		return args[index];
	}
	
	protected abstract void construct(String arg);

	public static class ConditionalString extends TStringChooser {
		String a = "";
		String b = "";

		@Override
		public void construct(String arg) {
			int split = arg.indexOf('|');
			a = arg.substring(0, split);
			if (split < arg.length()) {
				b = arg.substring(split + 1);
			}
		}

		@Override
		public boolean matches(String group, String type) {
			return Pattern.matches("^\\(.*\\|.*\\)$", group) && Pattern.matches("^#\\d+$", type);
		}

		@Override
		public String toString() {
			return ((boolean) getArg(0)) ? a : b;
		}
	}

	public static class SwitchString extends TStringChooser {
		private String[] options;

		@Override
		public void construct(String arg) {
			options = arg.split("|");
		}

		@Override
		public boolean matches(String group, String type) {
			return Pattern.matches("^\\((?:.*\\|){2,}.*\\)$", group) && Pattern.matches("^#\\d+$", type);
		}

		@Override
		public String toString() {
			return options[((int) getArg(0))];
		}
	}

	public static class RepeatString extends TStringChooser {
		private String str;

		@Override
		public void construct(String arg) {
			str = arg;
		}

		@Override
		public boolean matches(String group, String type) {
			return Pattern.matches(".+", group) && Pattern.matches("^<\\d+>$", type);
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			for (int i = 0; i < (int) getArg(0); i++) {
				builder.append(str);
			}
			return builder.toString();
		}
	}
}
