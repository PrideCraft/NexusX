package com.notarin.pride_craft_network.discord_bot;

import com.notarin.pride_craft_network.discord_bot.slash_commands.SlashCommandRegistrar;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

public class Main extends ListenerAdapter {

    public static void init(Map<String, Object> config) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
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