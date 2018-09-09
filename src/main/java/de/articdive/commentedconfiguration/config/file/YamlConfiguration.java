package de.articdive.commentedconfiguration.config.file;

import com.google.common.base.Charsets;
import de.articdive.commentedconfiguration.exceptions.InvalidConfigurationException;
import de.articdive.commentedconfiguration.interfaces.Configuration;
import de.articdive.commentedconfiguration.interfaces.ConfigurationSection;
import org.apache.commons.lang.Validate;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.representer.Representer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Map;

/**
 * This is a base class for all File based implementations of {@link
 * Configuration}
 */
public class YamlConfiguration extends MemorySection implements Configuration {
	private static final String BLANK_CONFIG = "{}\n";
	private final DumperOptions yamlOptions = new DumperOptions();
	private final Representer yamlRepresenter = new YamlRepresenter();
	private final Yaml yaml = new Yaml(new YamlConstructor(), yamlRepresenter, yamlOptions);
	private Configuration defaults;
	private YamlConfigurationOptions options;

	/**
	 * Creates an empty {@link YamlConfiguration} with no default values.
	 */
	public YamlConfiguration() {
		super();
	}

	/**
	 * Saves this {@link YamlConfiguration} to the specified location.
	 * <p>
	 * If the file does not exist, it will be created. If already exists, it
	 * will be overwritten. If it cannot be overwritten or created, an
	 * exception will be thrown.
	 * <p>
	 * This method will save using the system default encoding, or possibly
	 * using UTF8.
	 *
	 * @param file File to save to.
	 *
	 * @throws IOException              Thrown when the given file cannot be written to for
	 *                                  any reason.
	 * @throws IllegalArgumentException Thrown when file is null.
	 */
	public void save(File file) throws IOException {
		Validate.notNull(file, "File cannot be null");

		File parent = file.getCanonicalFile().getParentFile();
		if (parent == null) {
			/*
			 * The given directory is a filesystem root. All zero of its ancestors exist. This doesn't
			 * mean that the root itself exists -- consider x:\ on a Windows machine without such a drive
			 * -- or even that the caller can create it, but this method makes no such guarantees even for
			 * non-root files.
			 */
			return;
		}
		parent.mkdirs();
		if (!parent.isDirectory()) {
			throw new IOException("Unable to create parent directories of " + file);
		}

		String data = saveToString();

		try (Writer writer = new OutputStreamWriter(new FileOutputStream(file), Charsets.UTF_8)) {
			writer.write(data);
		}
	}

	private String saveToString() {
		yamlOptions.setIndent(options().indent());
		yamlOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
		yamlOptions.setWidth(10000);
		yamlRepresenter.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

		String dump = yaml.dump(getValues(false));

		if (dump.equals(BLANK_CONFIG)) {
			dump = "";
		}

		return dump;
	}


	/**
	 * Loads this {@link YamlConfiguration} from the specified location.
	 * <p>
	 * All the values contained within this configuration will be removed,
	 * leaving only settings and defaults, and the new values will be loaded
	 * from the given file.
	 * <p>
	 * If the file cannot be loaded for any reason, an exception will be
	 * thrown.
	 *
	 * @param file File to load from.
	 *
	 * @throws FileNotFoundException         Thrown when the given file cannot be
	 *                                       opened.
	 * @throws IOException                   Thrown when the given file cannot be read.
	 * @throws InvalidConfigurationException Thrown when the given file is not
	 *                                       a valid Configuration.
	 * @throws IllegalArgumentException      Thrown when file is null.
	 */
	public void load(File file) throws FileNotFoundException, IOException, InvalidConfigurationException {
		Validate.notNull(file, "File cannot be null");

		final FileInputStream stream = new FileInputStream(file);

		load(new InputStreamReader(stream, Charsets.UTF_8));
	}

	/**
	 * Loads this {@link YamlConfiguration} from the specified reader.
	 * <p>
	 * All the values contained within this configuration will be removed,
	 * leaving only settings and defaults, and the new values will be loaded
	 * from the given stream.
	 *
	 * @param reader the reader to load from
	 *
	 * @throws IOException                   thrown when underlying reader throws an IOException
	 * @throws InvalidConfigurationException thrown when the reader does not
	 *                                       represent a valid Configuration
	 * @throws IllegalArgumentException      thrown when reader is null
	 */
	private void load(Reader reader) throws IOException, InvalidConfigurationException {

		StringBuilder builder = new StringBuilder();
		try (BufferedReader input = reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader)) {
			String line;

			while ((line = input.readLine()) != null) {
				builder.append(line);
				builder.append('\n');
			}
		}
		loadFromString(builder.toString());

	}

	private void loadFromString(String contents) throws InvalidConfigurationException {
		Validate.notNull(contents, "Contents cannot be null");

		Map<?, ?> input;
		try {
			input = yaml.load(contents);
		} catch (YAMLException e) {
			throw new InvalidConfigurationException(e);
		} catch (ClassCastException e) {
			throw new InvalidConfigurationException("Top level is not a Map.");
		}

		if (input != null) {
			convertMapsToSections(input, this);
		}
	}

	private void convertMapsToSections(Map<?, ?> input, ConfigurationSection section) {
		for (Map.Entry<?, ?> entry : input.entrySet()) {
			String key = entry.getKey().toString();
			Object value = entry.getValue();

			if (value instanceof Map) {
				convertMapsToSections((Map<?, ?>) value, section.createSection(key));
			} else {
				section.set(key, value);
			}
		}
	}

	@Override
	public void addDefault(String path, Object value) {
		Validate.notNull(path, "Path may not be null");

		if (defaults == null) {
			defaults = new YamlConfiguration();
		}

		defaults.set(path, value);
	}

	public void addDefaults(Map<String, Object> defaults) {
		Validate.notNull(defaults, "Defaults may not be null");

		for (Map.Entry<String, Object> entry : defaults.entrySet()) {
			addDefault(entry.getKey(), entry.getValue());
		}
	}

	public void addDefaults(Configuration defaults) {
		Validate.notNull(defaults, "Defaults may not be null");

		addDefaults(defaults.getValues(true));
	}

	public void setDefaults(Configuration defaults) {
		Validate.notNull(defaults, "Defaults may not be null");

		this.defaults = defaults;
	}

	public Configuration getDefaults() {
		return defaults;
	}

	@Override
	public ConfigurationSection getParent() {
		return null;
	}

	public YamlConfigurationOptions options() {
		if (options == null) {
			options = new YamlConfigurationOptions(this);
		}

		return options;
	}
}