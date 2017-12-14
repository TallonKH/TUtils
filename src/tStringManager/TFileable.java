package tStringManager;

public interface TFileable {
	/**
	 * Load information from a formatted String
	 */
	void tFileParse(String str);
	/**
	 * Save information to a String
	 */
	String tFileCompose();
}
