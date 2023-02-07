package com.notarin.pride_craft_network.web_server;

import com.notarin.pride_craft_network.database.objects.PrideUser;
import spark.Spark;

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
                final String regex = "^[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}$";
                if (!req.params(":uuid").matches(regex)) {
                    res.status(400);
                    return BuildJson.error("Invalid UUID");
                }
                final PrideUser account = createAccountByUUID(req.params(":uuid"));
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
                final String regex = "^[0-9]{18}$";
                if (!req.params(":id").matches(regex)) {
                    res.status(400);
                    return BuildJson.error("Invalid Discord ID");
                }
                final PrideUser account = createAccountByDiscordId(req.params(":id"));
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
                return BuildJson.error("User not found");
            }
            return BuildJson.user(account);
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
                return BuildJson.error("User not found");
            }
            return BuildJson.user(account);
        });
    }

    /**
     * This route pings the server to check if it is online.
     */
    static void ping() {
        Spark.get("/ping", (req, res) -> "Pong!");
    }
}
