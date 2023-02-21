package com.notarin.pride_craft_network.database;

import com.notarin.pride_craft_network.database.objects.PrideUser;
import com.notarin.pride_craft_network.database.objects.Role;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.exceptions.NoSuchRecordException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class handles all the queries to the database.
 */
public class Query {

    /**
     * Creates a new account in the database.
     *
     * @return The account created
     */
    public static PrideUser createAccount() {
        final String query = """
                CREATE (account:PrideAccount\s
                {name: "Pride Account", name: $id, secret: $secret})
                RETURN account""";
        final Map<String, Object> params = new HashMap<>();
        final String generatedId = Util.generateId();
        final String secret = Util.generateSecret();
        params.put("id", generatedId);
        params.put("secret", secret);
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
     * @param uuid The UUID of the Minecraft account
     * @return The account
     */
    public static PrideUser getAccountByUUID(final String uuid) {
        final String query = """
                MATCH r=(account:PrideAccount)-->(minecraftAccount:MinecraftAccount\s
                {name: $UUID})\s
                RETURN r, account""";
        final Map<String, Object> params = new HashMap<>();
        params.put("UUID", uuid);
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
     * @param discordId The ID of the Discord account
     * @return The account
     */
    public static PrideUser getAccountByDiscordId(final String discordId) {
        final String query = """
                MATCH r=(account:PrideAccount)-->(discordAccount:DiscordAccount\s
                {name: $DiscordId})\s
                RETURN account""";
        final Map<String, Object> params = new HashMap<>();
        params.put("DiscordId", discordId);
        final Driver driver = Util.openConnection();
        try (final Session session = driver.session()) {
            final Result run = session.run(query, params);
            final List<Record> records = run.list();
            // Check if there are multiple accounts with the same discord ID
            if (records.size() > 1) {
                throw new IllegalStateException("There are multiple accounts " +
                        "with the same discord ID");
            }
            if (records.isEmpty()) {
                return null;
            }
            final Record record = records.get(0);
            final String userId = record.get("account").get("name").asString();
            return getAccount(userId);
        } catch (final NoSuchRecordException e) {
            return null;
        }
    }

    /**
     * Links a Minecraft UUID to an account.
     *
     * @param account This is the account that is being linked
     * @param uuid    The new Minecraft UUID to be linked
     * @return Whether the link was successful
     */
    public static Boolean linkUUIDQuery(final PrideUser account,
                                        final String uuid) {
        final String query = """
                MATCH (account:PrideAccount {name: $id})
                MERGE (minecraftAccount:MinecraftAccount\s
                {name: $UUID})
                CREATE (account)-[r1:OWNS {since: datetime()}]->(minecraftAccount)
                """;
        final Map<String, Object> params = new HashMap<>();
        params.put("id", account.id());
        params.put("UUID", uuid);
        final Driver driver = Util.openConnection();
        try (final Session session = driver.session()) {
            session.run(query, params);
            return true;
        } catch (final NoSuchRecordException e) {
            return false;
        }
    }

    /**
     * Links a Discord ID to an account.
     *
     * @param account   This is the account that is being linked
     * @param discordId The new Discord ID to be linked
     * @return Whether the link was successful
     */
    public static Boolean linkDiscordIdQuery(final PrideUser account,
                                             final String discordId) {
        final String query = """
                MATCH (account:PrideAccount {name: $id})
                MERGE (discordAccount:DiscordAccount\s
                {name: $DiscordId})
                CREATE (account)-[r1:OWNS {since: datetime()}]->(discordAccount)
                """;
        final Map<String, Object> params = new HashMap<>();
        params.put("id", account.id());
        params.put("DiscordId", discordId);
        final Driver driver = Util.openConnection();
        try (final Session session = driver.session()) {
            session.run(query, params);
            return true;
        } catch (final NoSuchRecordException e) {
            return false;
        }
    }

    /**
     * Creates a new role.
     *
     * @param name The name of the role
     * @return The role
     */
    public static Role makeRole(final String name) {
        final String query = """
                CREATE (role:Role {name: $name})
                RETURN role
                """;
        final Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        final Driver driver = Util.openConnection();
        try (final Session session = driver.session()) {
            final Result run = session.run(query, params);
            final Record record = run.single();
            return Util.parseRoleFromRecord(record);
        }
    }

    /**
     * Gets a role from the database.
     *
     * @param name The name of the role
     * @return The role
     */
    public static Role getRole(final String name) {
        final String query = """
                MATCH (role:Role {name: $name})
                RETURN role
                """;
        final Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        final Driver driver = Util.openConnection();
        try (final Session session = driver.session()) {
            final Result run = session.run(query, params);
            final Record record = run.single();
            return Util.parseRoleFromRecord(record);
        } catch (final NoSuchRecordException e) {
            return null;
        }
    }

    /**
     * Gets all roles.
     *
     * @return A list of all roles
     */
    public static List<Role> getRoles() {
        final String query = """
                MATCH (role:Role)
                RETURN role
                """;
        final Driver driver = Util.openConnection();
        try (final Session session = driver.session()) {
            final Result run = session.run(query);
            final List<Record> records = run.list();
            final List<Role> roles = new ArrayList<>();
            for (final Record record : records) {
                final Role role = Util.parseRoleFromRecord(record);
                roles.add(role);
            }
            return roles;
        }
    }

    /**
     * Sets a role as a child of another role.
     * This means that that parent role had administrative privileges over the
     * child role.
     * @param child The child role
     * @param parent The parent role
     * @return The child role
     */
    public static Role childRole(final Role child, final Role parent) {
        final String query = """
                MATCH (child:Role {name: $child})
                MATCH (parent:Role {name: $parent})
                CREATE (child)-[r1:PROMOTES_TO {since: datetime()}]->(parent)
                """;
        final Map<String, Object> params = new HashMap<>();
        params.put("child", child.name());
        params.put("parent", parent.name());
        final Driver driver = Util.openConnection();
        try (final Session session = driver.session()) {
            session.run(query, params);
            return getRole(child.name());
        }
    }

    /**
     * Removes a role as a child of another role.
     *
     * @param child The child role
     * @param parent The parent role
     * @return The child role
     */
    public static Role unChildRole(final Role child, final Role parent) {
        final String query = """
                MATCH (child:Role {name: $child})
                MATCH (parent:Role {name: $parent})
                MATCH (child)-[r1:PROMOTES_TO]->(parent)
                DELETE r1
                """;
        final Map<String, Object> params = new HashMap<>();
        params.put("child", child.name());
        params.put("parent", parent.name());
        final Driver driver = Util.openConnection();
        try (final Session session = driver.session()) {
            session.run(query, params);
            return getRole(child.name());
        }
    }

    /**
     * Checks to see if the user has the PROMOTES_TO in reverse relationship
     * with the other user or if it has it in multiple steps.
     * @param admin The admin role
     * @param user The user role
     * @return Whether the user is an administrator
     */
    public static Boolean
    checkAdministrator(final Role admin, final Role user) {
        final String query = """
                MATCH\s
                (admin:Role {name: $admin})
                <-[:PROMOTES_TO*]-
                (user:Role {name: $user})
                RETURN EXISTS((admin)<-[:PROMOTES_TO*]-(user)) AS path_exists
                """;
        final Map<String, Object> params = new HashMap<>();
        params.put("admin", admin.name());
        params.put("user", user.name());
        final Driver driver = Util.openConnection();
        try (final Session session = driver.session()) {
            final Result run = session.run(query, params);
            final Record record = run.single();
            return record.get("path_exists").asBoolean();
        } catch (final NoSuchRecordException e) {
            return false;
        }
    }

    /**
     * Sets the role of a user.
     *
     * @param user The user
     * @param role The role
     * @return The user
     */
    public static PrideUser setUserRole(final PrideUser user, final Role role) {
        final String query = """
                MATCH (account:PrideAccount {name: $id})
                MATCH (role:Role {name: $role})
                CREATE (account)-[r1:IS_ROLE {since: datetime()}]->(role)
                """;
        final Map<String, Object> params = new HashMap<>();
        params.put("id", user.id());
        params.put("role", role.name());
        final Driver driver = Util.openConnection();
        try (final Session session = driver.session()) {
            session.run(query, params);
            return getAccount(user.id());
        }
    }

    /**
     * Unsets the role of a user.
     *
     * @param user The user
     * @return The user
     */
    public static PrideUser unsetUserRole(final PrideUser user) {
        final String query = """
                MATCH (account:PrideAccount {name: $id})
                MATCH (role:Role)
                MATCH (account)-[r1:IS_ROLE]->(role)
                DELETE r1
                """;
        final Map<String, Object> params = new HashMap<>();
        params.put("id", user.id());
        final Driver driver = Util.openConnection();
        try (final Session session = driver.session()) {
            session.run(query, params);
            return getAccount(user.id());
        }
    }

}
