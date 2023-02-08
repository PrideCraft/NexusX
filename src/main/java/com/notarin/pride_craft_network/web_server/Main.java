package com.notarin.pride_craft_network.web_server;

import com.notarin.pride_craft_network.database.objects.PrideUser;
import org.jetbrains.annotations.NotNull;
import spark.Request;
import spark.Response;
import spark.Spark;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.notarin.pride_craft_network.ConfigHandler.loadConfig;
import static com.notarin.pride_craft_network.LogHandler.logWarn;
import static com.notarin.pride_craft_network.database.Query.getAccount;

/**
 * The main class for the web server.
 */
public class Main {

    /**
     * Initializes the web server.
     */
    public static void init() {
        final Map<String, Object> config = loadConfig();
        configureServer(config);
        Spark.init();
        defineServerPaths();
    }

    private static String[] parseArray(final String inputString) {
        final String removedOpenBracket =
                inputString.replace("[", "");
        final String removedCloseBracket =
                removedOpenBracket.replace("]", "");
        final String removedSpaces =
                removedCloseBracket.replace(" ", "");
        return removedSpaces.split(",");
    }

    private static void defineServerPaths() {
        Routes.ping();
        Routes.makeUserFromMinecraftUuid();
        Routes.makeUserFromDiscordId();
        Routes.getUser();
        Routes.getUserFromMinecraftUuid();
        Routes.getUserFromDiscordId();
        Routes.linkAccount();
    }

    static String getUserByPrideId(final Response res, final String id) {
        final PrideUser account = getAccount(id);
        if (account == null) {
            res.status(404);
            return BuildJson.error("User not found");
        }
        return BuildJson.user(account);
    }

    static boolean elevatedTransaction(final Request req) {
        final String token;
        try {
            token = req.headers("Authorization").replace("Bearer ", "");
        } catch (final RuntimeException e) {
            return false;
        }
        final Map<String, Object> config = loadConfig();
        final String[] secrets = parseArray(config.get("secrets").toString());
        final List<String> secretList = Arrays.asList(secrets);
        return secretList.contains(token);
    }

    @NotNull
    static String denyTransaction(final Response res) {
        res.status(401);
        return BuildJson.error("Unauthorized");
    }

    private static void configureServer(final Map<String, Object> config) {
        final Integer port = getPort(config);
        Spark.port(port);
    }

    private static Integer getPort(final Map<String, Object> config) {
        final Integer port;
        try {
            port = (Integer) config.get("port");
        } catch (final ClassCastException e) {
            logWarn("WebServer", "Invalid port in config.yml, defaulting to 8080");
            return 8080;
        }
        return port;
    }

}
