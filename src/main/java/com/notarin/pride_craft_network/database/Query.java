package com.notarin.pride_craft_network.database;

import com.notarin.pride_craft_network.database.objects.PrideUser;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.exceptions.NoSuchRecordException;

import java.util.HashMap;
import java.util.Map;

/**
 * This class handles all the queries to the database.
 */
public class Query {

    /**
     * Creates a new account in the database using a minecraft UUID.
     *
     * @param minecraftUuid The UUID of the Minecraft account
     * @return The created account
     */
    public static PrideUser createAccountByUUID(final String minecraftUuid) {
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
     * Creates a new account in the database using a discord ID.
     *
     * @param discordId The ID of the Discord account
     * @return The created account
     */
    public static PrideUser createAccountByDiscordId(final String discordId) {
        final String query = """
                CREATE (account:PrideAccount\s
                {name: "Pride Account", name: $id})
                CREATE (discordAccount:DiscordAccount\s
                {name: $DiscordId})
                CREATE (account)-[r1:OWNS]->(discordAccount)
                RETURN r1, account, discordAccount""";
        final Map<String, Object> params = new HashMap<>();
        final String generatedId = Util.generateId();
        params.put("id", generatedId);
        params.put("DiscordId", discordId);
        final Driver driver = Util.openConnection();
        try (final Session session = driver.session()) {
            final Result run = session.run(query, params);
            final Record record = run.single();
            return Util.parsePrideUserFromRecord(record);
        }
    }


    /**
     * Gets an account from the database using a pride ID.
     *
     * @param prideId The ID of the account
     * @return The account
     */
    public static PrideUser getAccount(final String prideId) {
        final String query = """
                MATCH r=(account:PrideAccount {name: $id})
                OPTIONAL MATCH (account)-->(minecraftAccount:MinecraftAccount)
                OPTIONAL MATCH (account)-->(discordAccount:DiscordAccount)
                RETURN account,
                COALESCE(minecraftAccount, null) as minecraftAccount,
                COALESCE(discordAccount, null) as discordAccount""";
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
     * Gets an account from the database using a minecraft UUID.
     *
     * @param UUID The UUID of the Minecraft account
     * @return The account
     */
    public static PrideUser getAccountByUUID(final String UUID) {
        final String query = """
                MATCH r=(account:PrideAccount)-->(minecraftAccount:MinecraftAccount\s
                {name: $UUID})\s
                RETURN r, account""";
        final Map<String, Object> params = new HashMap<>();
        params.put("UUID", UUID);
        final Driver driver = Util.openConnection();
        try (final Session session = driver.session()) {
            final Result run = session.run(query, params);
            final Record record = run.single();
            final String userId = record.get("account").get("name").asString();
            return getAccount(userId);
        } catch (final NoSuchRecordException e) {
            return null;
        }
    }

    /**
     * Gets an account from the database using a discord ID.
     *
     * @param DiscordId The ID of the Discord account
     * @return The account
     */
    public static PrideUser getAccountByDiscordId(final String DiscordId) {
        final String query = """
                MATCH r=(account:PrideAccount)-->(discordAccount:DiscordAccount\s
                {name: $DiscordId})\s
                RETURN account""";
        final Map<String, Object> params = new HashMap<>();
        params.put("DiscordId", DiscordId);
        final Driver driver = Util.openConnection();
        try (final Session session = driver.session()) {
            final Result run = session.run(query, params);
            final Record record = run.single();
            final String userId = record.get("account").get("name").asString();
            return getAccount(userId);
        } catch (final NoSuchRecordException e) {
            return null;
        }
    }

}
