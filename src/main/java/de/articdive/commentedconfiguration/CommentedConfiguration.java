package de.articdive.commentedconfiguration;

import de.articdive.commentedconfiguration.config.ConfigurationHolder;
import de.articdive.commentedconfiguration.config.file.CommentedConfig;
import de.articdive.commentedconfiguration.interfaces.ConfigNodes;
import de.articdive.commentedconfiguration.interfaces.ConfigurationHandler;

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
	public CommentedConfig getConfig() {
		return holder.getConfig();
	}

	@SuppressWarnings({"unused", "WeakerAccess"})
	public String getString(ConfigNodes node) {
		return holder.getConfig().getString(node.getNode().toLowerCase(), node.getDefaultValue());
	}

	@SuppressWarnings("unused")
	public int getInt(ConfigNodes node) {
		return Integer.parseInt(getString(node));
	}

	@SuppressWarnings("unused")
	public boolean getBoolean(ConfigNodes node) {
		return Boolean.parseBoolean(getString(node));
	}

	@SuppressWarnings("unused")
	public double getDouble(ConfigNodes node) {
		return Double.parseDouble(getString(node));
	}

	@SuppressWarnings("unused")
	public long getLong(ConfigNodes node) {
		return Long.parseLong(getString(node));
	}

	@SuppressWarnings("unused")
	public void setNode(ConfigNodes node, Object obj) {
		holder.getConfig().set(node.getNode().toLowerCase(), obj);
		holder.getConfig().save();
	}
}
