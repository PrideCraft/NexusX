package com.notarin.pride_Bot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.EnumSet;
import java.util.Map;
import java.util.Objects;

import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

public class Main extends ListenerAdapter {
    public static void main(String[] args) throws FileNotFoundException {
        InputStream inputStream = new FileInputStream("config.yml");
        Yaml yaml = new Yaml();
        Map<String, Object> config = yaml.load(inputStream);

        JDA jda = JDABuilder.createLight((String) config.get("token"), EnumSet.noneOf(GatewayIntent.class)) // slash commands don't need any intents
                .addEventListeners(new Main()).build();

        // These commands might take a few minutes to be active after creation/update/delete
        CommandListUpdateAction commands = jda.updateCommands();

        // Simple reply commands
        //noinspection ResultOfMethodCallIgnored
        commands.addCommands(Commands.slash("say", "Makes the bot say what you tell it to").addOption(STRING, "content", "What the bot should say", true) // you can add required options like this too
        );

        // Commands without any inputs
        //noinspection ResultOfMethodCallIgnored
        commands.addCommands(Commands.slash("leave", "Make the bot leave the server").setGuildOnly(true) // this doesn't make sense in DMs
                .setDefaultPermissions(DefaultMemberPermissions.DISABLED) // only admins should be able to use this command.
        );

        // Send the new set of commands to discord, this will override any existing global commands with the new set provided here
        commands.queue();
    }


    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        // Only accept commands from guilds
        if (event.getGuild() == null) return;
        switch (event.getName()) {
            case "say" ->
                    say(event, Objects.requireNonNull(event.getOption("content")).getAsString()); // content is required so no null-check here
            case "leave" -> leave(event);
            default -> event.reply("I can't handle that command right now :(").setEphemeral(true).queue();
        }
    }

    public void say(SlashCommandInteractionEvent event, String content) {
        event.reply(content).queue(); // This requires no permissions!
    }

    public void leave(SlashCommandInteractionEvent event) {
        if (!Objects.requireNonNull(event.getMember()).hasPermission(Permission.KICK_MEMBERS))
            event.reply("You do not have permissions to kick me.").setEphemeral(true).queue();
        else event.reply("Leaving the server... :wave:") // Yep we received it
                .flatMap(v -> Objects.requireNonNull(event.getGuild()).leave()) // Leave server after acknowledging the command
                .queue();
    }
}