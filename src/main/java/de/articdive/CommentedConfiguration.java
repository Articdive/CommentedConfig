package de.articdive;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import de.articdive.config.ConfigurationHolder;
import de.articdive.interfaces.ConfigNodes;
import de.articdive.interfaces.ConfigurationHandler;

import java.lang.reflect.InvocationTargetException;

public class CommentedConfiguration {
	private ConfigNodes configNodes;
	private String filepath;
	private ConfigurationHandler handler;
	private ConfigurationHolder holder;

	/**
	 * @param configNodes - Enum which contains the config nodes.
	 * @param filepath    - location of the config (should include the name).
	 *                    <p>
	 *                    This will load with defaultConfigurationHandler.
	 */
	public CommentedConfiguration(@NotNull ConfigNodes configNodes, @NotNull String filepath) {
		this(configNodes, filepath, null);
	}

	/**
	 * @param configNodes - Enum which contains the config nodes.
	 * @param filepath    - location of the config (should include the name).
	 * @param handler     - The setDefaults() handler.
	 */
	public CommentedConfiguration(@NotNull ConfigNodes configNodes, @NotNull String filepath, @Nullable Class<? extends ConfigurationHandler> handler) {
		this.configNodes = configNodes;
		this.filepath = filepath;
		if (handler == null) {
			this.handler = new DefaultConfigurationHandler(this);
		} else {
			try {
				handler = handler.getConstructor(CommentedConfiguration.class).newInstance(this);
			} catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
				e.printStackTrace();
			}
		}

	}

	public ConfigNodes getConfigNodes() {
		return configNodes;
	}

	public ConfigurationHandler getHandler() {
		return handler;
	}

	public String getFilepath() {
		return filepath;
	}

	ConfigurationHolder getHolder() {
		return holder;
	}
}
