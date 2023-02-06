package com.notarin.pride_craft_network.discord_bot.slash_commands.commands;

import com.notarin.pride_craft_network.discord_bot.slash_commands.SlashCommandHandler;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

/**
 * A command for testing the bot connectivity.
 */
@SuppressWarnings("unused")
public class Ping implements SlashCommandHandler {

    @Override
    public String getName() {
        return "ping";
    }

    @Override
    public String getDescription() {
        return "Returns Pong";
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        event.reply("Pong!").queue();
    }

    @Override
    public void setOptions(SlashCommandData command) {}
}
