package de.articdive.commentedconfiguration.interfaces;

public interface ConfigNodes {
	String getNode();

	default String getDefaultValue() {
		return "";
	}

	default String[] getComments() {
		return new String[]{""};
	}
}
