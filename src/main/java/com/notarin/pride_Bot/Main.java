package com.notarin.pride_Bot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.EnumSet;
import java.util.Map;

public class Main extends ListenerAdapter {
    public static void main(String[] args) {

        JDA jda;
        Map<String, Object> config;

        try {
            //Loading config file
            InputStream inputStream = new FileInputStream("config.yml");
            Yaml yaml = new Yaml();
            //set the config object
            config = yaml.load(inputStream);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

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