package com.notarin.pride_Bot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.EnumSet;
import java.util.Map;

import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

public class Main extends ListenerAdapter {
    public static void main(String[] args) throws FileNotFoundException {
        //Loading config file
        InputStream inputStream = new FileInputStream("config.yml");
        Yaml yaml = new Yaml();
        //create the config object
        Map<String, Object> config = yaml.load(inputStream);

        //login
        JDA jda = JDABuilder.createLight((String) config.get("token"), EnumSet.noneOf(GatewayIntent.class)) // At this point in time we need no intents.
                .addEventListeners(new Main()).build();

        // Prepare building command list
        CommandListUpdateAction commands = jda.updateCommands();

        //noinspection ResultOfMethodCallIgnored
        commands.addCommands(net.dv8tion.jda.api.interactions.commands.build.Commands.slash("say", "Makes the bot say what you tell it to").addOption(STRING, "content", "What the bot should say", true) // you can add required options like this too
        );

        //noinspection ResultOfMethodCallIgnored
        commands.addCommands(net.dv8tion.jda.api.interactions.commands.build.Commands.slash("leave", "Make the bot leave the server").setGuildOnly(true) // Allow only in Guilds/Servers
                .setDefaultPermissions(DefaultMemberPermissions.DISABLED)
        );

        // Send the new set of commands to discord, this will override any existing global commands with the new set provided here
        commands.queue();
    }


    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        // Only accept commands from guilds
        if (event.getGuild() == null) return;
        String command = event.getName();
        Commands.slashcommand(command, event);
    }
}