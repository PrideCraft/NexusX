package com.notarin.pride_craft_network.web_server;

import com.notarin.pride_craft_network.Regex;
import com.notarin.pride_craft_network.database.objects.PrideUser;
import org.yaml.snakeyaml.Yaml;
import spark.Spark;

import java.util.Map;

import static com.notarin.pride_craft_network.database.Query.*;

/**
 * The routes class for the web server.
 * This class contains all the routes for the web server.
 */
public class Routes {
    /**
     * This route is used to create an account from a Minecraft UUID.
     */
    static void makeUserFromMinecraftUuid() {
        Spark.post("/make-user/minecraft-uuid/:uuid", (req, res) -> {
            if (Main.elevatedTransaction(req)) {
                final String regex = Regex.uuidValidate;
                if (!req.params(":uuid").matches(regex)) {
                    res.status(400);
                    return BuildYaml.error("Invalid UUID");
                }
                final PrideUser account = createAccountByUUID(req.params(
                        ":uuid"));
                return Main.getUserByPrideId(res, account.id());
            } else return Main.denyTransaction(res);
        });
    }

    /**
     * This route is used to create an account from a Discord ID.
     */
    static void makeUserFromDiscordId() {
        Spark.post("/make-user/discord-id/:id", (req, res) -> {
            if (Main.elevatedTransaction(req)) {
                final String regex = Regex.discordIdValidate;
                if (!req.params(":id").matches(regex)) {
                    res.status(400);
                    return BuildYaml.error("Invalid Discord ID");
                }
                final PrideUser account =
                        createAccountByDiscordId(req.params(":id"));
                return Main.getUserByPrideId(res, account.id());
            } else return Main.denyTransaction(res);
        });
    }

    /**
     * This route is used to get a user from their Pride ID.
     */
    static void getUser() {
        Spark.get("/user/:id", (req, res) -> {
            final String params = req.params(":id");
            return Main.getUserByPrideId(res, params);
        });
    }

    /**
     * This route is used to get a user from their Minecraft UUID.
     */
    static void getUserFromMinecraftUuid() {
        Spark.get("/uuid/:uuid", (req, res) -> {
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
            if (Main.elevatedTransaction(req)) {
                final String body = req.body();
                final Yaml yaml = new Yaml();
                final Map<String, Object> map = yaml.load(body);
                final String prideId;
                final String uuid;
                final String discordId;
                // Check if the body is valid
                try {
                    prideId = (String) map.get("Pride-ID");
                    if (prideId == null) {
                        throw new NullPointerException();
                    }
                    uuid = (String) map.get("UUID");
                    final Long preDiscordId = (Long) map.get("Discord-ID");
                    // Ternary operator in Java.
                    // It's a shorthand way of writing an if-else statement.
                    discordId = (preDiscordId != null) ?
                            preDiscordId.toString() : null;
                    if (uuid == null && discordId == null) {
                        throw new NullPointerException();
                    }
                } catch (final NullPointerException e) {
                    res.status(400);
                    return BuildYaml.error("Invalid body");
                }
                final PrideUser account = getAccount(prideId);
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
            }
            return Main.denyTransaction(res);
        });
    }

    /**
     * This route pings the server to check if it is online.
     */
    static void ping() {
        Spark.get("/ping", (req, res) -> "Pong!");
    }
}
