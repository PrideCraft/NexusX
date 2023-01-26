package com.notarin.pride_craft_network.discord_bot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.util.EnumSet;
import java.util.Map;

public class Main extends ListenerAdapter {
    public static void init(Map<String, Object> config) {

        JDA jda;

        //login
        try {
            jda = JDABuilder.createLight(config.get("token").toString(), EnumSet.noneOf(GatewayIntent.class)) // At this point in time we need no intents.
                    .addEventListeners(new Main()).build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        //register the slash commands
        SlashCommandRegistrar.register(jda);
    }


    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        // Only accept commands from guilds
        if (event.getGuild() == null) return;
        String command = event.getName();
        System.out.println("Received command " + event.getCommandString() + " from " + event.getUser().getAsTag());
        Commands.slashCommand(command, event);
    }
}