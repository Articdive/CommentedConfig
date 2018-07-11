package de.articdive.config;

import de.articdive.CommentedConfiguration;
import de.articdive.utilities.FileMgmt;

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
		if (file != null) {

			config = new CommentedConfig(file);
			if (!config.load()) {
				System.out.print("Failed to load Config!");
			}

			config = main.getHandler().setDefaults(config, file);

			config.save();
		}
	}

	public CommentedConfig getConfig() {
		return config;
	}
}
