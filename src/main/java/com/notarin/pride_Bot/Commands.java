package com.notarin.pride_Bot;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.Objects;

public class Commands {
    public static void say(SlashCommandInteractionEvent event, String content) {
        event.reply(content).queue();
    }

    public static void leave(SlashCommandInteractionEvent event) {
        if (!Objects.requireNonNull(event.getMember()).hasPermission(Permission.KICK_MEMBERS))
            event.reply("You do not have permissions to kick me.").setEphemeral(true).queue();
        else event.reply("Leaving the server... :wave:")
                .flatMap(v -> Objects.requireNonNull(event.getGuild()).leave()) // Leave server after acknowledging the command
                .queue();
    }

}
