package de.articdive.config;

import de.articdive.CommentedConfiguration;
import de.articdive.interfaces.ConfigNodes;
import de.articdive.interfaces.ConfigurationHandler;
import de.articdive.utilities.FileMgmt;
import org.bukkit.Bukkit;

import java.io.File;

public class ConfigurationHolder {
	private CommentedConfig config, newConfig;
	private String filepath;
	private Class<? extends ConfigurationHandler> handler;

	public ConfigurationHolder(CommentedConfiguration main) {
		this.filepath = main.getFilepath();
		this.handler = main.getHandler();
	}


	private void loadConfig(String filepath, String version) {

		File file = FileMgmt.CheckYMLExists(new File(filepath));
		if (file != null) {

			// read the config.yml into memory
			config = new CommentedConfig(file);
			if (!config.load()) {
				System.out.print("Failed to load Config!");
			}

			// set defaults.

			config.save();
		}
	}


	public void addComment(String root, String... comments) {

		newConfig.addComment(root.toLowerCase(), comments);
	}

	public void setNewProperty(String root, Object value) {

		if (value == null) {
			// System.out.print("value is null for " + root.toLowerCase());
			value = "";
		}
		newConfig.set(root.toLowerCase(), value.toString());
	}

	private static String getLastRunVersion(String currentVersion) {

		return getString(ConfigNodes.LAST_RUN_VERSION.getRoot(), currentVersion);
	}

	private static String getString(String root, String def) {

		String data = config.getString(root.toLowerCase(), def);
		if (data == null) {
			sendError(root.toLowerCase() + " from config.yml");
			return "";
		}
		return data;
	}

	private static void sendError(String msg) {

		Bukkit.getLogger().info("Error could not read " + msg);
	}


	public static String getString(ConfigNodes node) {

		return config.getString(node.getRoot().toLowerCase(), node.getDefault());

	}

	public static boolean getBoolean(ConfigNodes node) {

		return Boolean.parseBoolean(config.getString(node.getRoot().toLowerCase(), node.getDefault()));
	}

	public static double getDouble(ConfigNodes node) {

		try {
			return Double.parseDouble(config.getString(node.getRoot().toLowerCase(), node.getDefault()).trim());
		} catch (NumberFormatException e) {
			sendError(node.getRoot().toLowerCase() + " from config.yml");
			return 0.0;
		}
	}

	public static int getInt(ConfigNodes node) {

		try {
			return Integer.parseInt(config.getString(node.getRoot().toLowerCase(), node.getDefault()).trim());
		} catch (NumberFormatException e) {
			sendError(node.getRoot().toLowerCase() + " from config.yml");
			return 0;
		}
	}

	public static String getConfigVersion() {
		return getString(ConfigNodes.VERSION);
	}
}

}
