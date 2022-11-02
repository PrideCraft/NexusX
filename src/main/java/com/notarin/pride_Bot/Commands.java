package com.notarin.pride_Bot;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.Objects;

public class Commands {
    public static void slashcommand(String command, SlashCommandInteractionEvent event) {
        switch (command) {
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
            default -> event.reply("I can't handle that command right now :(").setEphemeral(true).queue();
        }
    }
}
