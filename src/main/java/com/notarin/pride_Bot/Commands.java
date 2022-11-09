package com.notarin.pride_Bot;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.Objects;

public class Commands {
    public static void slashcommand(String command, SlashCommandInteractionEvent event) {
        switch (command) {
            case "ping" -> event.reply("Pong!").setEphemeral(true).queue();
            case "say" ->
                    event.reply(Objects.requireNonNull(event.getOption("content")).getAsString()).queue();
            case "leave" -> {
                if (Objects.requireNonNull(event.getMember()).hasPermission(Permission.KICK_MEMBERS)) {
                    event.reply("Leaving the server... :wave:")
                            .flatMap(v -> Objects.requireNonNull(event.getGuild()).leave()) // Leave server after acknowledging the command
                            .queue();
                } else {
                    event.reply("You do not have permissions to kick me.").setEphemeral(true).queue();
                }
            }
            case "suggest" ->
                event.reply("https://github.com/PrideCraft/Public-Issue-Tracker/issues/new/choose").queue();
            case "bug" ->
                event.reply("https://github.com/PrideCraft/Public-Issue-Tracker/issues/new/choose").queue();
            default -> {
                event.reply("Uh oh, I don't think I have the tools to handle that command :( \nPlease contact an administrator.").setEphemeral(true).queue();
                System.out.println("ERROR: Unhandled command recieved, info:\n" +
                        "\tCommand Name: " + event.getName() + "\n" +
                        "\tTime: " + event.getTimeCreated() + "\n" +
                        "\tServer: " + event.getGuild() + "\n" +
                        "\tChannel: " + event.getChannel() + "\n" +
                        "\tUser: " + event.getUser()
                );
            }
        }
    }
}
