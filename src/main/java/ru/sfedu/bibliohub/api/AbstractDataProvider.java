package ru.sfedu.bibliohub.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sfedu.bibliohub.utils.ConfigurationUtil;
import ru.sfedu.bibliohub.utils.Constants;

import java.io.IOException;

public class AbstractDataProvider {
    protected final Logger log = LogManager.getLogger(this.getClass());
    private final boolean MONGO_ENABLE = Boolean.parseBoolean(ConfigurationUtil.getConfigurationEntry(Constants.MONGO_ENABLE));
    private final String MONGO_ACTOR = ConfigurationUtil.getConfigurationEntry(Constants.MONGO_ACTOR);

    public AbstractDataProvider() throws IOException {
    }
}
