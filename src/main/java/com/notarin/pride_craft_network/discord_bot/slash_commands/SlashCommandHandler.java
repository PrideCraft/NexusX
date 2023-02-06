package com.notarin.pride_craft_network.discord_bot.slash_commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

/**
 * A handler for slash commands.
 */
public interface SlashCommandHandler {
    /**
     * Handles a slash command event.
     *
     * @param event The event to handle
     */
    void handle(SlashCommandInteractionEvent event);

    /**
     * This defines the name of the command.
     * @return The name of the command
     */
    String getName();

    /**
     * This defines the description of the command.
     *
     * @return The description of the command
     */
    String getDescription();

    /**
     * This defines the options of the command.
     *
     * @param command The command to set options for
     */
    void setOptions(SlashCommandData command);

}
