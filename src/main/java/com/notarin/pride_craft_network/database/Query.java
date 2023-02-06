package com.notarin.pride_craft_network.database;

import com.notarin.pride_craft_network.database.objects.PrideUser;
import org.neo4j.driver.Record;
import org.neo4j.driver.*;

import java.util.HashMap;
import java.util.Map;

/**
 * This class handles all the queries to the database.
 */
public class Query {

    /**
     * Creates a new account in the database.
     *
     * @param minecraftUuid The UUID of the Minecraft account
     * @return The created account
     */
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
            return Util.parsePrideUserFromRecord(generatedId, record);
        }
    }

    /**
     * Gets an account from the database.
     *
     * @param prideId The ID of the account
     * @return The account
     */
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
            return Util.parsePrideUserFromRecord(prideId, record);
        } catch (Exception e) {
            return null;
        }
    }

}
