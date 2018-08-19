package de.articdive.commentedconfiguration.interfaces;

import de.articdive.commentedconfiguration.CommentedConfiguration;
import de.articdive.commentedconfiguration.config.file.CommentedConfig;

import java.io.File;

public abstract class ConfigurationHandler {

	protected CommentedConfiguration main;
	protected CommentedConfig newConfig;

	public ConfigurationHandler(CommentedConfiguration main) {
		this.main = main;
	}

	public abstract CommentedConfig setDefaults(CommentedConfig memoryconfig, File file);


	protected void addComment(String root, String... comments) {

		newConfig.addComment(root.toLowerCase(), comments);
	}

	protected void setNewProperty(String root, Object value) {

		if (value == null) {
			value = "";
		}
		newConfig.set(root.toLowerCase(), value.toString());
	}
}
