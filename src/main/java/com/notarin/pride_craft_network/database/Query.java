package com.notarin.pride_craft_network.database;

import org.neo4j.driver.*;

import java.util.HashMap;
import java.util.Map;

public class Query {

    public static void createAccount(String minecraftUuid) {
        String query = """
                CREATE (account:PrideAccount\s
                {name: "Pride Account", id: $id})
                CREATE (minecraftAccount:MinecraftAccount\s
                {name: $MinecraftUUID})
                CREATE (account)-[r1:OWNS]->(minecraftAccount)""";
        Driver driver = Util.openConnection();
        try (Session session = driver.session()) {
            session.executeWriteWithoutResult(q -> {

                Map<String, Object> params = new HashMap<>();
                params.put("id", Util.generateId());
                params.put("MinecraftUUID", minecraftUuid);
                q.run(query, params);
            });
        }
    }

}
