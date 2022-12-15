package ru.sfedu.bibliohub.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertOneResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import ru.sfedu.bibliohub.model.HistoryContent;

import java.io.IOException;

public class MongoUtil {
    private static final Logger log = LogManager.getLogger(MongoUtil.class);

    public MongoUtil() {
    }

    private static MongoClient initClient() {
        try {
            String mongoDb = ConfigurationUtil.getConfigurationEntry(Constants.MONGO_CONNECTION);
            ConnectionString connectionString = new ConnectionString(mongoDb);
            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(connectionString)
                    .build();
            return MongoClients.create(settings);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    private static <T> void saveObject(T object) {
        try {
            String database = ConfigurationUtil.getConfigurationEntry(Constants.MONGO_DATABASE);
            String collection = ConfigurationUtil.getConfigurationEntry(Constants.MONGO_COLLECTION);
            MongoDatabase mongoDatabase = initClient().getDatabase(database);
            InsertOneResult insertOneResult = mongoDatabase.getCollection(collection)
                    .insertOne(Document.parse(objectToString(object)));
            if (!insertOneResult.wasAcknowledged())
                throw new IllegalArgumentException();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public static void saveToLog(HistoryContent historyContent) {
        saveObject(historyContent);
    }

    public static String objectToString(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public static <T> T stringToObject(String json, Class<T> objectType) {
        try {
            return new ObjectMapper().readValue(json, objectType);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
