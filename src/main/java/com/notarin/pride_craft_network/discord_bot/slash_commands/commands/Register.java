package com.notarin.pride_craft_network.discord_bot.slash_commands.commands;

import com.notarin.pride_craft_network.database.Query;
import com.notarin.pride_craft_network.database.objects.PrideUser;
import com.notarin.pride_craft_network.discord_bot.slash_commands.SlashCommandHandler;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

/**
 * A command for testing the bot connectivity.
 */
@SuppressWarnings("unused")
public class Register implements SlashCommandHandler {

    @Override
    public String getName() {
        return "register";
    }

    @Override
    public String getDescription() {
        return "register to the pride craft network";
    }

    @Override
    public void handle(final SlashCommandInteractionEvent event) {
        try {
            final String discordId = event.getUser().getId();
            if (Query.getAccountByDiscordId(discordId) != null) {
                event.reply("You are already registered!").queue();
                return;
            }
            final PrideUser account = Query.createAccount();
            Query.linkDiscordIdQuery(account, discordId);
            event.reply("You have been registered!").queue();
        } catch (@SuppressWarnings("OverlyBroadCatchBlock") final Exception e) {
            event.reply("An error occurred!").queue();
        }
    }

    @Override
    public void setOptions(final SlashCommandData command) {}
}
