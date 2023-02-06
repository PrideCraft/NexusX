package com.notarin.pride_craft_network.database;

import com.notarin.pride_craft_network.database.objects.PrideUser;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.neo4j.driver.Result;
import org.neo4j.driver.Record;
import org.neo4j.driver.exceptions.NoSuchRecordException;

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
    public static PrideUser createAccount(final String minecraftUuid) {
        final String query = """
                CREATE (account:PrideAccount\s
                {name: "Pride Account", name: $id})
                CREATE (minecraftAccount:MinecraftAccount\s
                {name: $MinecraftUUID})
                CREATE (account)-[r1:OWNS]->(minecraftAccount)
                RETURN r1, account, minecraftAccount""";
        final Map<String, Object> params = new HashMap<>();
        final String generatedId = Util.generateId();
        params.put("id", generatedId);
        params.put("MinecraftUUID", minecraftUuid);
        final Driver driver = Util.openConnection();
        try (final Session session = driver.session()) {
            final Result run = session.run(query, params);
            final Record record = run.single();
            return Util.parsePrideUserFromRecord(record);
        }
    }

    /**
     * Gets an account from the database.
     *
     * @param prideId The ID of the account
     * @return The account
     */
    public static PrideUser getAccount(final String prideId) {
        final String query = """
                MATCH r=(account:PrideAccount\s
                {name: $id})-->(minecraftAccount:MinecraftAccount)\s
                RETURN r, account, minecraftAccount""";
        final Map<String, Object> params = new HashMap<>();
        params.put("id", prideId);
        final Driver driver = Util.openConnection();
        try (final Session session = driver.session()) {
            final Result run = session.run(query, params);
            final Record record = run.single();
            return Util.parsePrideUserFromRecord(record);
        } catch (final NoSuchRecordException e) {
            return null;
        }
    }

    /**
     * Gets an account from the database.
     *
     * @param UUID The UUID of the Minecraft account
     * @return The account
     */
    public static PrideUser getAccountByUUID(final String UUID) {
        final String query = """
                MATCH r=(account:PrideAccount)-->(minecraftAccount:MinecraftAccount\s
                {name: $UUID})\s
                RETURN r, account, minecraftAccount""";
        final Map<String, Object> params = new HashMap<>();
        params.put("UUID", UUID);
        final Driver driver = Util.openConnection();
        try (final Session session = driver.session()) {
            final Result run = session.run(query, params);
            final Record record = run.single();
            return Util.parsePrideUserFromRecord(record);
        } catch (final NoSuchRecordException e) {
            return null;
        }
    }

}
