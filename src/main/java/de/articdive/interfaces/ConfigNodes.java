package de.articdive.interfaces;

public interface ConfigNodes {
	String getNode();

	default String getDefaultValue() {
		return "";
	}

	default String[] getComments() {
		return new String[]{""};
	}

	String getFile();
}
