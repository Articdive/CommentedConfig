package de.articdive.commentedconfiguration;

import de.articdive.commentedconfiguration.config.file.CommentedConfig;
import de.articdive.commentedconfiguration.interfaces.ConfigNodes;
import de.articdive.commentedconfiguration.interfaces.ConfigurationHandler;

import java.io.File;

public class DefaultConfigurationHandler extends ConfigurationHandler {

	public DefaultConfigurationHandler(CommentedConfiguration main) {
		super(main);
	}

	@Override
	public CommentedConfig setDefaults(CommentedConfig memoryconfig, File file) {

		newConfig = new CommentedConfig(file);
		newConfig.load();

		for (ConfigNodes root : main.getConfigNodes()) {
			if (root.getComments().length > 0) {
				addComment(root.getNode(), root.getComments());
			}

			setNewProperty(root.getNode(), (memoryconfig.get(root.getNode().toLowerCase()) != null) ? memoryconfig.get(root.getNode().toLowerCase()) : root.getDefaultValue());
		}

		return newConfig;
	}

}
