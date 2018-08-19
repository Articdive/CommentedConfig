package de.articdive.commentedconfiguration;

import de.articdive.commentedconfiguration.config.ConfigurationHolder;
import de.articdive.commentedconfiguration.config.file.CommentedConfig;
import de.articdive.commentedconfiguration.interfaces.ConfigNodes;
import de.articdive.commentedconfiguration.interfaces.ConfigurationHandler;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

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

	@SuppressWarnings("unused")
	public String getString(ConfigNodes node) {
		return holder.getConfig().getString(node.getNode().toLowerCase());
	}

	@SuppressWarnings("unused")
	public int getInt(ConfigNodes node) {
		return holder.getConfig().getInt(node.getNode().toLowerCase());
	}

	@SuppressWarnings("unused")
	public boolean getBoolean(ConfigNodes node) {
		return holder.getConfig().getBoolean(node.getNode().toLowerCase());
	}

	@SuppressWarnings("unused")
	public double getDouble(ConfigNodes node) {
		return holder.getConfig().getDouble(node.getNode().toLowerCase());
	}

	@SuppressWarnings("unused")
	public long getLong(ConfigNodes node) {
		return holder.getConfig().getLong(node.getNode().toLowerCase());
	}

	@SuppressWarnings("unused")
	public List<?> getList(ConfigNodes node) {
		return holder.getConfig().getList(node.getNode().toLowerCase());
	}

	@SuppressWarnings("unused")
	public List<String> getStringList(ConfigNodes node) {
		return holder.getConfig().getStringList(node.getNode().toLowerCase());
	}

	@SuppressWarnings("unused")
	public List<Boolean> getBooleanList(ConfigNodes node) {
		return holder.getConfig().getBooleanList(node.getNode().toLowerCase());
	}

	@SuppressWarnings("unused")
	public List<Byte> getByteList(ConfigNodes node) {
		return holder.getConfig().getByteList(node.getNode().toLowerCase());
	}

	@SuppressWarnings("unused")
	public List<Character> getCharacterList(ConfigNodes node) {
		return holder.getConfig().getCharacterList(node.getNode().toLowerCase());
	}

	@SuppressWarnings("unused")
	public List<Double> getDoubleList(ConfigNodes node) {
		return holder.getConfig().getDoubleList(node.getNode().toLowerCase());
	}

	@SuppressWarnings("unused")
	public List<Integer> getIntegerList(ConfigNodes node) {
		return holder.getConfig().getIntegerList(node.getNode().toLowerCase());
	}

	@SuppressWarnings("unused")
	public List<Float> getFloatList(ConfigNodes node) {
		return holder.getConfig().getFloatList(node.getNode().toLowerCase());
	}

	@SuppressWarnings("unused")
	public List<Short> getShortList(ConfigNodes node) {
		return holder.getConfig().getShortList(node.getNode().toLowerCase());
	}

	@SuppressWarnings("unused")
	public List<Long> getLongList(ConfigNodes node) {
		return holder.getConfig().getLongList(node.getNode().toLowerCase());
	}

	@SuppressWarnings("unused")
	public List<Map<?, ?>> getMapList(ConfigNodes node) {
		return holder.getConfig().getMapList(node.getNode().toLowerCase());
	}

	@SuppressWarnings("unused")
	public void setNode(ConfigNodes node, Object obj) {
		holder.getConfig().set(node.getNode().toLowerCase(), obj);
		holder.getConfig().save();
	}
}
