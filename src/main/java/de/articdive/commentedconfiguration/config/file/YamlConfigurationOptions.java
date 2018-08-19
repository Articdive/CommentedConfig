package de.articdive.commentedconfiguration.config.file;

import de.articdive.commentedconfiguration.interfaces.Configuration;
import de.articdive.commentedconfiguration.interfaces.ConfigurationSection;
import org.apache.commons.lang.Validate;

/**
 * Various settings for controlling the input and output of a {@link
 * Configuration}
 */
public class YamlConfigurationOptions {
	private char pathSeparator = '.';
	private boolean copyDefaults = false;
	private final Configuration configuration;
	private int indent = 2;

	protected YamlConfigurationOptions(Configuration configuration) {
		this.configuration = configuration;
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	/**
	 * Gets the char that will be used to separate {@link
	 * ConfigurationSection}s
	 * <p>
	 * This value does not affect how the {@link Configuration} is stored,
	 * only in how you access the data. The default value is '.'.
	 *
	 * @return Path separator
	 */
	public char getPathSeparator() {
		return pathSeparator;
	}

	/**
	 * Sets the char that will be used to separate {@link
	 * ConfigurationSection}s
	 * <p>
	 * This value does not affect how the {@link Configuration} is stored,
	 * only in how you access the data. The default value is '.'.
	 *
	 * @param pathSeparator Path separator
	 *
	 * @return This object, for chaining
	 */
	public void setPathSeparator(char pathSeparator) {
		this.pathSeparator = pathSeparator;
	}

	/**
	 * Checks if the {@link Configuration} should copy values from its default
	 * {@link Configuration} directly.
	 * <p>
	 * If this is true, all values in the default Configuration will be
	 * directly copied, making it impossible to distinguish between values
	 * that were set and values that are provided by default. As a result,
	 * {@link ConfigurationSection#contains(java.lang.String)} will always
	 * return the same value as {@link
	 * ConfigurationSection#isSet(java.lang.String)}. The default value is
	 * false.
	 *
	 * @return Whether or not defaults are directly copied
	 */
	public boolean isCopyDefaults() {
		return copyDefaults;
	}

	public void setCopyDefaults(boolean copyDefaults) {
		this.copyDefaults = copyDefaults;
	}

	/**
	 * Gets how much spaces should be used to indent each line.
	 * <p>
	 * The minimum value this may be is 2, and the maximum is 9.
	 *
	 * @return How much to indent by
	 */
	public int indent() {
		return indent;
	}

	/**
	 * Sets how much spaces should be used to indent each line.
	 * <p>
	 * The minimum value this may be is 2, and the maximum is 9.
	 *
	 * @param value New indent
	 *
	 * @return This object, for chaining
	 */
	public YamlConfigurationOptions indent(int value) {
		Validate.isTrue(value >= 2, "Indent must be at least 2 characters");
		Validate.isTrue(value <= 9, "Indent cannot be greater than 9 characters");

		this.indent = value;
		return this;
	}
}
