package de.articdive;

import de.articdive.config.CommentedConfig;
import de.articdive.interfaces.ConfigNodes;
import de.articdive.interfaces.ConfigurationHandler;

import java.io.File;

public class DefaultConfigurationHandler extends ConfigurationHandler {

	public DefaultConfigurationHandler(CommentedConfiguration main) {
		super(main);
	}

	@Override
	public CommentedConfig setDefaults(File file) {

		CommentedConfig newConfig = new CommentedConfig(file);
		newConfig.load();

		for (Enum<> root : main.getConfigNodes()) {
			if (root.getComments().length > 0) {
				addComment(root.getRoot(), root.getComments());
			}
			if (root.getRoot().equals(ConfigNodes.VERSION.getRoot())) {
				setNewProperty(root.getRoot(), version);
			} else if (root.getRoot().equals(ConfigNodes.LAST_RUN_VERSION.getRoot())) {
				setNewProperty(root.getRoot(), getLastRunVersion(version));
			} else
				setNewProperty(root.getRoot(), (config.get(root.getRoot().toLowerCase()) != null) ? config.get(root.getRoot().toLowerCase()) : root.getDefault());

		}

		return newConfig;
	}

	private void addComment(String root, String... comments) {

		main.getHolder().addComment(root.toLowerCase(), comments);
	}

	private void setNewProperty(String root, Object value) {

		if (value == null) {
			// System.out.print("value is null for " + root.toLowerCase());
			value = "";
		}
		main.getHolder().setNewProperty(root.toLowerCase(), value.toString());
	}

}
