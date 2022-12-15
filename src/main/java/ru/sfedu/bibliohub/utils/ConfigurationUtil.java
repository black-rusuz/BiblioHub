package ru.sfedu.bibliohub.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Configuration utility. Allows to get configuration properties from the
 * default configuration file
 *
 * @author Boris Jmailov
 */
public class ConfigurationUtil {
    private static final String DEFAULT_CONFIG_PATH = "./src/main/resources/environment.properties";
    private static final Properties configuration = new Properties();
    private static final Logger log = LogManager.getLogger(ConfigurationUtil.class);

    /**
     * Hides default constructor
     */
    public ConfigurationUtil() {
    }

    private static Properties getConfiguration() throws IOException {
        if (configuration.isEmpty()) loadConfiguration();
        return configuration;
    }

    /**
     * Loads configuration from <code>DEFAULT_CONFIG_PATH</code>
     *
     * @throws IOException In case of the configuration file read failure
     */
    private static void loadConfiguration() throws IOException {
        File file = new File(DEFAULT_CONFIG_PATH);
        String userPath = System.getProperty(Constants.ENVIRONMENT_VARIABLE);

        if (userPath != null) {
            File userFile = new File(userPath);
            if (userFile.exists()) file = userFile;
            else log.error("Your environment configuration file not found. Default loaded.");
        }

        try (InputStream in = new FileInputStream(file)) {
            configuration.load(in);
        } catch (IOException e) {
            throw new IOException(e);
        }
    }

    /**
     * Gets configuration entry value
     *
     * @param key Entry key
     * @return Entry value by key
     * @throws IOException In case of the configuration file read failure
     */
    public static String getConfigurationEntry(String key) throws IOException {
        return getConfiguration().getProperty(key);
    }
}
