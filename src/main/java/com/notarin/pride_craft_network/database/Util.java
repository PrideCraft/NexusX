package com.notarin.pride_craft_network.database;

import com.notarin.pride_craft_network.database.objects.PrideUser;
import org.jetbrains.annotations.NotNull;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Config;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Record;
import org.neo4j.driver.Value;

import java.util.Map;
import java.util.Random;

import static com.notarin.pride_craft_network.ConfigHandler.loadConfig;
import static com.notarin.pride_craft_network.LogHandler.logError;

/**
 * This class contains utility methods for database related tasks.
 */
public class Util {
    static Driver driver;

    /**
     * Opens a connection to the database.
     *
     * @return The Neo4j driver
     */
    public static Driver openConnection() {
        if (!(driver == null)) {
            return driver;
        }

        Map<String, Object> config = loadConfig();
        String host = (String) config.get("db_host");
        Integer port = (Integer) config.get("db_port");
        String uri = "bolt://" + host + ":" + port;
        String user = (String) config.get("db_username");
        String pass = (String) config.get("db_password");
        if (config.get("db_encryption").equals(false)) {
            driver = GraphDatabase.driver(uri, AuthTokens.basic(user, pass));
        } else if (config.get("db_encryption").equals(true)) {
            Config builder = Config.builder().withEncryption().build();
            driver = GraphDatabase.driver(uri, AuthTokens.basic(user, pass),
                    builder);
        } else {
            logError(
                    "Database",
                    "Invalid encryption value in config.yml");
        }
        return driver;
    }

    /**
     * Generates a random ID.
     *
     * @return A random ID
     */
    public static String generateId() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 30;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint,
                        StringBuilder::append)
                .toString();
    }

    @NotNull
    static PrideUser parsePrideUserFromRecord(String generatedId, Record record) {
        String minecraftUuid = null;
        for (Value value : record.values()) {
            boolean node = value.type().name().equals("NODE");
            if (node && value.asNode().hasLabel("MinecraftAccount")) {
                Value name = value.asNode().get("name");
                minecraftUuid = name.asString();
            }
        }
        return new PrideUser(
                generatedId,
                minecraftUuid
        );
    }
}
