package com.notarin.pride_craft_network.discord_bot.slash_commands.commands;

import com.notarin.pride_craft_network.discord_bot.slash_commands.SlashCommandHandler;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

/**
 * A command for making the bot say what you tell it to.
 */
@SuppressWarnings("unused")
public class Say implements SlashCommandHandler {

    @Override
    public String getName() {
        return "say";
    }

    @Override
    public String getDescription() {
        return "Makes the bot say what you tell it to";
    }

    @Override
    public void handle(final SlashCommandInteractionEvent event) {
        try {
            //noinspection DataFlowIssue
            event.reply(event.getOption("message").getAsString()).queue();
        } catch (final NullPointerException e) {
            event.reply("Invalid syntax, please provide a message.")
                    .setEphemeral(true).queue();
        }
    }

    @Override
    public void setOptions(final SlashCommandData command) {
        command.addOption(OptionType.STRING,
                "message",
                "What you wish for the bot to respond with");
    }}
