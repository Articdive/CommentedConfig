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
	public CommentedConfig setDefaults(CommentedConfig memoryconfig, File file) {

		newConfig.load();

		for (ConfigNodes root : main.getConfigNodes()) {
			if (root.getComments().length > 0) {
				addComment(root.getNode(), root.getComments());
			}
			setNewProperty(root.getNode(), (memoryconfig.get(root.getNode().toLowerCase()) != null) ? memoryconfig.get(root.getNode().toLowerCase()) : root.getDefaultValue());

		}

		return newConfig;
	}

	private void addComment(String root, String... comments) {

		newConfig.addComment(root.toLowerCase(), comments);
	}

	private void setNewProperty(String root, Object value) {

		if (value == null) {
			value = "";
		}
		newConfig.set(root.toLowerCase(), value.toString());
	}

}
