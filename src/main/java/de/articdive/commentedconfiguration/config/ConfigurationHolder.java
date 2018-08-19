package de.articdive.commentedconfiguration.config;

import de.articdive.commentedconfiguration.CommentedConfiguration;
import de.articdive.commentedconfiguration.config.file.CommentedConfig;
import de.articdive.commentedconfiguration.util.FileMgmt;

import java.io.File;

public class ConfigurationHolder {
	private CommentedConfiguration main;
	private CommentedConfig config;

	public ConfigurationHolder(CommentedConfiguration main) {
		this.main = main;
		loadConfig(main.getFilepath());
	}

	private void loadConfig(String filepath) {

		File file = FileMgmt.checkYMLExists(new File(filepath));

		config = new CommentedConfig(file);
		if (!config.load()) {
			System.out.print("Failed to load Config!");
		}

		config = main.getHandler().setDefaults(config, file);

		config.save();
	}

	public CommentedConfig getConfig() {
		return config;
	}
}
