package com.notarin.pride_craft_network.web_server;

import com.notarin.pride_craft_network.database.objects.PrideUser;
import spark.Spark;

import static com.notarin.pride_craft_network.database.Query.createAccountByUUID;

/**
 * The routes class for the web server.
 * This class contains all the routes for the web server.
 */
public class Routes {
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

    static void getUser() {
        Spark.get("/user/:id", (req, res) -> {
            final String params = req.params(":id");
            return Main.getUserByPrideId(res, params);
        });
    }

    static void getUserFromMinecraftUuid() {
        Spark.get("/uuid/:uuid", (req, res) -> {
            final String params = req.params(":uuid");
            return Main.getUserByMinecraftUuid(res, params);
        });
    }

    static void ping() {
        Spark.get("/ping", (req, res) -> "Pong!");
    }
}
