package com.notarin.pride_craft_network.discord_bot.slash_commands.commands;

import com.notarin.pride_craft_network.discord_bot.slash_commands.SlashCommandHandler;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

@SuppressWarnings("unused")
public class Bug implements SlashCommandHandler {

    @Override
    public String getName() {
        return "bug";
    }

    @Override
    public String getDescription() {
        return "For when you want to report a bug";
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        String url = "https://github.com/PrideCraft/Public-Issue-Tracker" +
                "/issues/new/choose";
        event.reply(url).queue();
    }

    @Override
    public void setOptions(SlashCommandData command) {}
}
