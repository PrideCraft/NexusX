package com.notarin.pride_craft_network.web_server;

import com.notarin.pride_craft_network.Regex;
import com.notarin.pride_craft_network.database.Query;
import com.notarin.pride_craft_network.database.objects.PrideUser;
import com.notarin.pride_craft_network.database.objects.Role;
import org.yaml.snakeyaml.Yaml;
import spark.Spark;

import java.util.List;
import java.util.Map;

import static com.notarin.pride_craft_network.database.Query.createAccount;
import static com.notarin.pride_craft_network.database.Query.getAccount;
import static com.notarin.pride_craft_network.database.Query.getAccountByDiscordId;
import static com.notarin.pride_craft_network.database.Query.getAccountByUUID;
import static com.notarin.pride_craft_network.database.Query.linkDiscordIdQuery;
import static com.notarin.pride_craft_network.database.Query.linkUUIDQuery;

/**
 * The routes class for the web server.
 * This class contains all the routes for the web server.
 */
@SuppressWarnings("unused")
public class Routes {
    /**
     * This route is used to create an account from a Minecraft UUID.
     */
    static void makeUserFromMinecraftUuid() {
        Spark.post("/make-user/minecraft-uuid/:uuid", (req, res) -> {
            res.header("Content-Type", "application/x-yaml");
            if (Main.elevatedTransaction(req)) {
                final String regex = Regex.uuidValidate;
                if (!req.params(":uuid").matches(regex)) {
                    res.status(400);
                    return BuildYaml.error("Invalid UUID");
                }
                final PrideUser account = createAccount();
                if (linkUUIDQuery(account, req.params(":uuid"))) {
                    return Main.getUserByPrideId(res, account.id());
                } else {
                    res.status(500);
                    return BuildYaml.error("Failed to link UUID");
                }
            } else return Main.denyTransaction(res);
        });
    }

    /**
     * This route is used to create an account from a Discord ID.
     */
    static void makeUserFromDiscordId() {
        Spark.post("/make-user/discord-id/:id", (req, res) -> {
            res.header("Content-Type", "application/x-yaml");
            if (Main.elevatedTransaction(req)) {
                final String regex = Regex.discordIdValidate;
                if (!req.params(":id").matches(regex)) {
                    res.status(400);
                    return BuildYaml.error("Invalid Discord ID");
                }
                final PrideUser account = createAccount();
                if (linkDiscordIdQuery(account, req.params(":id"))) {
                    return Main.getUserByPrideId(res, account.id());
                } else {
                    res.status(500);
                    return BuildYaml.error("Failed to link Discord ID");
                }
            } else return Main.denyTransaction(res);
        });
    }

    /**
     * This route is used to get a user from their Pride ID.
     */
    static void getUser() {
        Spark.get("/user/:id", (req, res) -> {
            res.header("Content-Type", "application/x-yaml");
            final String params = req.params(":id");
            return Main.getUserByPrideId(res, params);
        });
    }

    /**
     * This route is used to get a user from their Minecraft UUID.
     */
    static void getUserFromMinecraftUuid() {
        Spark.get("/uuid/:uuid", (req, res) -> {
            res.header("Content-Type", "application/x-yaml");
            final String params = req.params(":uuid");
            final PrideUser account = getAccountByUUID(params);
            if (account == null) {
                res.status(404);
                return BuildYaml.error("User not found");
            }
            return BuildYaml.user(account);
        });
    }

    /**
     * This route is used to get a user from their Discord ID.
     */
    static void getUserFromDiscordId() {
        Spark.get("/discord/:id", (req, res) -> {
            res.header("Content-Type", "application/x-yaml");
            final String params = req.params(":id");
            final PrideUser account = getAccountByDiscordId(params);
            if (account == null) {
                res.status(404);
                return BuildYaml.error("User not found");
            }
            return BuildYaml.user(account);
        });
    }

    /**
     * This route is used to link a new UUID or Discord ID.
     */
    static void linkAccount() {
        Spark.post("/link/", (req, res) -> {
            res.header("Content-Type", "application/x-yaml");
            if (!Main.elevatedTransaction(req)) {
                return Main.denyTransaction(res);
            }

            final String body = req.body();
            final Yaml yaml = new Yaml();
            final Map<String, Object> map = yaml.load(body);
            final String prideId = (String) map.get("Pride-ID");
            final String uuid = (String) map.get("UUID");
            final String discordId = map.get("Discord-ID") != null ?
                    ((Long) map.get("Discord-ID")).toString() : null;
            final PrideUser account = getAccount(prideId);

            // Check if the body is valid
            if (prideId == null || (uuid == null && discordId == null)) {
                res.status(400);
                return BuildYaml.error("Invalid body");
            }

            // Check if the account exists
            if (account == null) {
                res.status(404);
                return BuildYaml.error("Please specify a valid Pride ID");
            }

            // Link the UUID
            if (uuid != null) {
                if (account.minecraftUuid() != null) {
                    res.status(400);
                    return BuildYaml.error("This account already has a " +
                            "UUID linked");
                }
                if (getAccountByUUID(uuid) != null) {
                    res.status(400);
                    return BuildYaml.error("This UUID is already linked " +
                            "to an account");
                }
                final Boolean result = linkUUIDQuery(account, uuid);
                if (!result) {
                    res.status(500);
                    return BuildYaml.error("An error occurred while " +
                            "linking the UUID");
                }
            }

            // Link the Discord ID
            if (discordId != null) {
                if (account.discordId() != null) {
                    res.status(400);
                    return BuildYaml.error("This account already has a " +
                            "Discord ID linked");
                }
                if (getAccountByDiscordId(discordId) != null) {
                    res.status(400);
                    return BuildYaml.error("This Discord ID is already " +
                            "linked to an account");
                }
                final Boolean result = linkDiscordIdQuery(account,
                        discordId);
                if (!result) {
                    res.status(500);
                    return BuildYaml.error("An error occurred while " +
                            "linking the Discord ID");
                }
            }

            // Reload the account, then return it
            final PrideUser reloadedAccount = getAccount(prideId);
            // No worries of exception, this is always set, alright?
            // Nullness is but a myth, this code is rock solid, trust.
            assert reloadedAccount != null;
            return BuildYaml.user(reloadedAccount);
        });
    }

    /**
     * This route is used to get the secret of a user.
     */
    static void getSecret() {
        Spark.get("/secret/:id", (req, res) -> {
            res.header("Content-Type", "application/x-yaml");
            if (Main.elevatedTransaction(req)) {
                final String params = req.params(":id");
                final PrideUser account = getAccount(params);
                if (account == null) {
                    res.status(404);
                    return BuildYaml.error("User not found");
                }
                return BuildYaml.secret(account);
            } else return Main.denyTransaction(res);
        });
    }

    static void makeRole() {
        Spark.post("/role/:name", (req, res) -> {
            res.header("Content-Type", "application/x-yaml");
            if (Main.elevatedTransaction(req)) {
                final String param = req.params(":name");
                final Role role = Query.makeRole(param);
                return BuildYaml.role(role);
            } else return Main.denyTransaction(res);
        });
    }

    static void getRole() {
        Spark.get("/role/:name", (req, res) -> {
            res.header("Content-Type", "application/x-yaml");
            if (Main.elevatedTransaction(req)) {
                final String param = req.params(":name");
                final Role role = Query.getRole(param);
                return BuildYaml.role(role);
            } else return Main.denyTransaction(res);
        });
    }

    static void getAllRoles() {
        Spark.get("/roles", (req, res) -> {
            res.header("Content-Type", "application/x-yaml");
            if (Main.elevatedTransaction(req)) {
                final List<Role> roles = Query.getRoles();
                return BuildYaml.roles(roles);
            } else return Main.denyTransaction(res);
        });
    }

    static void childARole() {
        Spark.put("/role/link-roles/", (req, res) -> {
            res.header("Content-Type", "application/x-yaml");
            if (Main.elevatedTransaction(req)) {
                final String body = req.body();
                final Yaml yaml = new Yaml();
                final Map<String, Object> map = yaml.load(body);
                final Role parentRole;
                final Role childRole;
                try {
                    final String parent = (String) map.get("ParentRole");
                    final String child = (String) map.get("Role");
                    parentRole = Query.getRole(parent);
                    childRole = Query.getRole(child);
                } catch (final RuntimeException e) {
                    res.status(400);
                    return BuildYaml.error("Invalid body");
                }
                if (parentRole == null) {
                    res.status(404);
                    return BuildYaml.error("Parent role not found");
                }
                if (childRole == null) {
                    res.status(404);
                    return BuildYaml.error("Child role not found");
                }
                final Role result = Query.childRole(childRole, parentRole);
                return BuildYaml.role(result);
            } else return Main.denyTransaction(res);
        });
    }

    static void unChildARole() {
        Spark.put("/role/unlink-roles/", (req, res) -> {
            res.header("Content-Type", "application/x-yaml");
            if (Main.elevatedTransaction(req)) {
                final String body = req.body();
                final Yaml yaml = new Yaml();
                final Map<String, Object> map = yaml.load(body);
                final Role parentRole;
                final Role childRole;
                try {
                    final String parent = (String) map.get("ParentRole");
                    final String child = (String) map.get("Role");
                    parentRole = Query.getRole(parent);
                    childRole = Query.getRole(child);
                } catch (final RuntimeException e) {
                    res.status(400);
                    return BuildYaml.error("Invalid body");
                }
                if (childRole == null) {
                    res.status(400);
                    return BuildYaml.error("Child role does not exist");
                }
                if (parentRole == null) {
                    res.status(400);
                    return BuildYaml.error("Parent role does not exist");
                }
                final Role result = Query.unChildRole(childRole, parentRole);
                return BuildYaml.role(result);
            } else return Main.denyTransaction(res);
        });
    }

    static void checkIfRoleAdmins() {
        Spark.get("/role-admins", (req, res) -> {
            res.header("Content-Type", "application/x-yaml");
            if (Main.elevatedTransaction(req)) {
                final String body = req.body();
                final Yaml yaml = new Yaml();
                final Map<String, Object> map = yaml.load(body);
                final Role parentRole;
                final Role childRole;
                try {
                    final String parent = (String) map.get("ParentRole");
                    final String child = (String) map.get("Role");
                    parentRole = Query.getRole(parent);
                    childRole = Query.getRole(child);
                } catch (final RuntimeException e) {
                    res.status(400);
                    return BuildYaml.error("Invalid body");
                }
                if (parentRole == null) {
                    res.status(404);
                    return BuildYaml.error("Parent role not found");
                }
                if (childRole == null) {
                    res.status(404);
                    return BuildYaml.error("Child role not found");
                }
                final Boolean result = Query.checkAdministrator(parentRole,
                        childRole);
                return BuildYaml.booleanResponse(result);
            } else return Main.denyTransaction(res);
        });
    }

    /**
     * This route pings the server to check if it is online.
     */
    static void ping() {
        Spark.get("/ping", (req, res) -> "Pong!");
    }
}
