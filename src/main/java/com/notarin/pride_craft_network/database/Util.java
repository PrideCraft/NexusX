package com.notarin.pride_craft_network.database;

import com.notarin.pride_craft_network.database.objects.PrideUser;
import org.jetbrains.annotations.NotNull;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Config;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Record;
import org.neo4j.driver.Value;

import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

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

        final Map<String, Object> config = loadConfig();
        final String host = (String) config.get("db_host");
        final Integer port = (Integer) config.get("db_port");
        final String uri = "bolt://" + host + ":" + port;
        final String user = (String) config.get("db_username");
        final String pass = (String) config.get("db_password");
        if (config.get("db_encryption").equals(false)) {
            driver = GraphDatabase.driver(uri, AuthTokens.basic(user, pass));
        } else if (config.get("db_encryption").equals(true)) {
            final Config builder = Config.builder().withEncryption().build();
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
        final int leftLimit = 97; // letter 'a'
        final int rightLimit = 122; // letter 'z'
        final int targetStringLength = 30;
        final Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint,
                        StringBuilder::append)
                .toString();
    }

    /**
     * Generates a random secret with a combination of uppercase letters, lowercase letters, and numbers.
     *
     * @return A random secret
     */
    public static String generateSecret() {
        final int targetStringLength = 100;
        final int numberStart = 48; // digit '0'
        final int numberLimit = 57; // digit '9'
        final int uppercaseStart = 65; // letter 'A'
        final int upperLimit = 90; // letter 'Z'
        final int lowerLimit = 97; // letter 'a'
        final int asciiLimit = 123; // letter 'z'
        final Random random = new Random();

        return random.ints(numberStart, asciiLimit)
                .filter(i -> {
                    final boolean b = i <= numberLimit;
                    final boolean b1 = i >= uppercaseStart;
                    final boolean b2 = i <= upperLimit;
                    final boolean b3 = i >= lowerLimit;
                    final boolean b4 = i + 1 <= asciiLimit;
                    final boolean b5 = b1 && b2;
                    final boolean b6 = b3 && b4;
                    if (b) return true;
                    if (b5) return true;
                    return b6;
                })
                .limit(targetStringLength)
                .mapToObj(i -> String.valueOf((char) i))
                .collect(Collectors.joining());
    }

    /**
     * Parses a PrideUser from a record.
     *
     * @param record The record to parse
     * @return The parsed PrideUser
     */
    @NotNull
    static PrideUser parsePrideUserFromRecord(final Record record) {
        String minecraftUuid = null;
        String prideId = null;
        String discordId = null;
        String secret = null;
        for (final Value value : record.values()) {
            final boolean node = value.type().name().equals("NODE");
            if (node && value.asNode().hasLabel("MinecraftAccount")) {
                final Value name = value.asNode().get("name");
                minecraftUuid = name.asString();
            } else if (node && value.asNode().hasLabel("PrideAccount")) {
                final Value name = value.asNode().get("name");
                prideId = name.asString();
                final Value unParsedSecret = value.asNode().get("secret");
                secret = unParsedSecret.asString();
            } else if (node && value.asNode().hasLabel("DiscordAccount")) {
                final Value name = value.asNode().get("name");
                discordId = name.asString();
            }
        }
        return new PrideUser(
                prideId,
                minecraftUuid,
                discordId,
                secret
        );
    }
}
