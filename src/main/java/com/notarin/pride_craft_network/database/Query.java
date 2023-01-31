package com.notarin.pride_craft_network.database;

import org.neo4j.driver.*;

import java.util.Map;
import java.util.Random;

import static com.notarin.pride_craft_network.ConfigHandler.loadConfig;

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
        driver = GraphDatabase.driver(uri, AuthTokens.basic(user, pass));
        return driver;
    }

    static void createAccount(String minecraftUuid) {
        Driver driver = openConnection();
        try (Session session = driver.session()) {
            try (Transaction tx = session.beginTransaction()) {
                String query = "" +
                        "CREATE (account:PrideAccount {name: \"Pride Account\", id: \"" + generateId() + "\"})\n" +
                        "CREATE (minecraftaccount:MinecraftAccount {name: \"" + minecraftUuid + "\"})\n" +
                        "CREATE (account)-[r1:OWNS]->(minecraftaccount)\n" +
                        "return account, minecraftaccount" +
                        "";
                Result run = tx.run(query);
                System.out.println(run);
            }
        }
    }

    public static String generateId() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 30;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

}
