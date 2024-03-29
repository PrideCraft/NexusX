package com.notarin.pride_craft_network.discord_bot.slash_commands.commands;

import com.notarin.pride_craft_network.discord_bot.slash_commands.SlashCommandHandler;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.util.Objects;

/**
 * A command for making the bot leave the server.
 */
@SuppressWarnings("unused")
public class Leave implements SlashCommandHandler {

    @Override
    public String getName() {
        return "leave";
    }

    @Override
    public String getDescription() {
        return "Make the bot leave the server";
    }

    @Override
    public void handle(final SlashCommandInteractionEvent event) {
        if (Objects.requireNonNull(
                event.getMember()).hasPermission(Permission.KICK_MEMBERS)) {
            event.reply("Leaving the server... :wave:")
                    .flatMap(v ->
                            // Leave server after acknowledging the command
                            Objects.requireNonNull(event.getGuild()).leave())
                    .queue();
        } else {
            event.reply("You do not have permissions to kick me.")
                    .setEphemeral(true).queue();
        }
    }

    @Override
    public void setOptions(final SlashCommandData command) {
        command.setGuildOnly(true);
        command.setDefaultPermissions(DefaultMemberPermissions.DISABLED);
    }
}
