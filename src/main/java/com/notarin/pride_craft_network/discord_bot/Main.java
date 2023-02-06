package com.notarin.pride_craft_network.discord_bot;

import com.notarin.pride_craft_network.ConfigHandler;
import com.notarin.pride_craft_network.discord_bot.slash_commands.SlashCommandRegistrar;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

/**
 * The main class for the discord bot.
 */
public class Main extends ListenerAdapter {

    /**
     * The main method for initializing the bot.
     *
     * @throws InvocationTargetException Thrown when registering slash commands
     * fails
     * @throws NoSuchMethodException Should never be thrown, but if it does,
     * the reflector found a method that doesn't exist
     * @throws InstantiationException Thrown when registering slash commands
     * fails
     * @throws IllegalAccessException Thrown when registering slash commands
     * fails
     */
    public static void init() throws InvocationTargetException,
            NoSuchMethodException, InstantiationException,
            IllegalAccessException {
        //load config
        Map<String, Object> config = ConfigHandler.loadConfig();

        //login
        final JDABuilder jdabuilder = JDABuilder
                .createDefault((String) config.get("token"))
                .enableIntents(GatewayIntent.MESSAGE_CONTENT);
        final JDA jda = jdabuilder.build();
        List<Class<?>> slashCommands = SlashCommandRegistrar.loadCommands();
        jda.addEventListener(new EventRegistrar(slashCommands));

        //register the slash commands
        SlashCommandRegistrar.register(jda, slashCommands);
    }

}