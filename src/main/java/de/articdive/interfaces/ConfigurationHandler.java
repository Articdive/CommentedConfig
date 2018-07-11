package de.articdive.interfaces;

import de.articdive.CommentedConfiguration;
import de.articdive.config.CommentedConfig;

import java.io.File;

public abstract class ConfigurationHandler {

	protected CommentedConfiguration main;
	protected CommentedConfig newConfig;

	public ConfigurationHandler(CommentedConfiguration main) {
		this.main = main;
	}

	public abstract CommentedConfig setDefaults(CommentedConfig memoryconfig, File file);
}
