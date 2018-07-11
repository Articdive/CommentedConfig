package de.articdive;

import de.articdive.config.ConfigurationHolder;
import de.articdive.interfaces.ConfigNodes;
import de.articdive.interfaces.ConfigurationHandler;

import java.lang.reflect.InvocationTargetException;

public class CommentedConfiguration {
	private ConfigNodes[] configNodes;
	private String filepath;
	private ConfigurationHandler handler;
	private ConfigurationHolder holder;

	/**
	 * @param configNodes - Enum which contains the config nodes.
	 * @param filepath    - location of the config (should include the name).
	 *                    <p>
	 *                    This will load with defaultConfigurationHandler.
	 */
	public <T extends Enum<T> & ConfigNodes> CommentedConfiguration(Class<T> configNodes, String filepath) {
		this(configNodes, filepath, null);
	}

	/**
	 * @param configNodes - Enum which contains the config nodes.
	 * @param filepath    - location of the config (should include the name).
	 * @param handler     - The setDefaults() handler.
	 */
	public <T extends Enum<T> & ConfigNodes> CommentedConfiguration(Class<T> configNodes, String filepath, Class<? extends ConfigurationHandler> handler) {
		this.configNodes = configNodes.getEnumConstants();
		this.filepath = filepath;
		if (handler == null) {
			this.handler = new DefaultConfigurationHandler(this);
		} else {
			try {
				this.handler = handler.getConstructor(CommentedConfiguration.class).newInstance(this);
			} catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
				e.printStackTrace();
			}
		}
		holder = new ConfigurationHolder(this);
	}

	public ConfigNodes[] getConfigNodes() {
		return configNodes;
	}

	public ConfigurationHandler getHandler() {
		return handler;
	}

	public String getFilepath() {
		return filepath;
	}

	@SuppressWarnings("unused")
	public String getString(ConfigNodes node) {
		return holder.getConfig().getString(node.getNode().toLowerCase(), node.getDefaultValue());
	}
}
