package com.notarin.pride_craft_network.discord_bot.slash_commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public interface SlashCommandHandler {
    void handle(SlashCommandInteractionEvent event);
    String getName();
    String getDescription();
    void setOptions(SlashCommandData command);

}
