package com.notarin.pride_craft_network.database;

import org.neo4j.driver.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static com.notarin.pride_craft_network.ConfigHandler.loadConfig;
import static com.notarin.pride_craft_network.LogHandler.logError;

public class Query {

    static Driver driver;

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

    public static void createAccount(String minecraftUuid) {
        String query = """
                CREATE (account:PrideAccount\s
                {name: "Pride Account", id: $id})
                CREATE (minecraftAccount:MinecraftAccount\s
                {name: $MinecraftUUID})
                CREATE (account)-[r1:OWNS]->(minecraftAccount)""";
        Driver driver = openConnection();
        try (Session session = driver.session()) {
            session.executeWriteWithoutResult(q -> {

                Map<String, Object> params = new HashMap<>();
                params.put("id", generateId());
                params.put("MinecraftUUID", minecraftUuid);
                q.run(query, params);
            });
        }
    }

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

}
