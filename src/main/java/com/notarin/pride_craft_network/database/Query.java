package com.notarin.pride_craft_network.database;

import com.notarin.pride_craft_network.database.objects.PrideUser;
import org.jetbrains.annotations.NotNull;
import org.neo4j.driver.Record;
import org.neo4j.driver.*;

import java.util.HashMap;
import java.util.Map;

public class Query {

    public static PrideUser createAccount(String minecraftUuid) {
        String query = """
                CREATE (account:PrideAccount\s
                {name: "Pride Account", name: $id})
                CREATE (minecraftAccount:MinecraftAccount\s
                {name: $MinecraftUUID})
                CREATE (account)-[r1:OWNS]->(minecraftAccount)
                RETURN r1, account, minecraftAccount""";
        Map<String, Object> params = new HashMap<>();
        String generatedId = Util.generateId();
        params.put("id", generatedId);
        params.put("MinecraftUUID", minecraftUuid);
        Driver driver = Util.openConnection();
        try (Session session = driver.session()) {
            Result run = session.run(query, params);
            Record record = run.single();
            return getPrideUser(minecraftUuid, generatedId, record);
        }
    }

    @NotNull
    private static PrideUser getPrideUser(String minecraftUuid, String generatedId, Record record) {
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
            Record record = run.single();
            String minecraftUuid = null;
            return getPrideUser(minecraftUuid, prideId, record);
        } catch (Exception e) {
            return null;
        }
    }

}
