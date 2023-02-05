package com.notarin.pride_craft_network.database;

import com.notarin.pride_craft_network.database.objects.PrideUser;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Value;

import java.util.HashMap;
import java.util.Map;

public class Query {

    public static void createAccount(String minecraftUuid) {
        String query = """
                CREATE (account:PrideAccount\s
                {name: "Pride Account", name: $id})
                CREATE (minecraftAccount:MinecraftAccount\s
                {name: $MinecraftUUID})
                CREATE (account)-[r1:OWNS]->(minecraftAccount)""";
        Driver driver = Util.openConnection();
        try (Session session = driver.session()) {

            session.executeWrite(q -> {
                Map<String, Object> params = new HashMap<>();
                params.put("id", Util.generateId());
                params.put("MinecraftUUID", minecraftUuid);
                q.run(query, params);
                return null;
            });

        }
    }

    public static PrideUser getAccount(String prideId) {
        String query = """
                MATCH r=(account:PrideAccount\s
                {name: $id})-->(minecraftAccount:MinecraftAccount)\s
                RETURN r, account, minecraftAccount""";
        Map<String, Object> params = new HashMap<>();
        params.put("id", prideId);
        Driver driver = Util.openConnection();
        try (Session session = driver.session()) {
            Result run = session.run(query, params);
            Record test = run.single();
            String minecraftUuid = null;
            for (Value value : test.values()) {
                boolean node = value.type().name().equals("NODE");
                if (node && value.asNode().hasLabel("MinecraftAccount")) {
                    Value name = value.asNode().get("name");
                    minecraftUuid = name.asString();
                }
            }
            return new PrideUser(
                    prideId,
                    minecraftUuid

            );
        } catch (Exception e) {
            return null;
        }
    }

}
