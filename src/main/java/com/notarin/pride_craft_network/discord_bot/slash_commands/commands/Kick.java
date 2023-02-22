package com.notarin.pride_craft_network.discord_bot.slash_commands.commands;

import com.notarin.pride_craft_network.database.Query;
import com.notarin.pride_craft_network.database.objects.PrideUser;
import com.notarin.pride_craft_network.discord_bot.slash_commands.SlashCommandHandler;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.util.Objects;

import static com.notarin.pride_craft_network.database.Util.checkIfAdministratesByUser;

/**
 * A command for testing the bot connectivity.
 */
@SuppressWarnings("unused")
public class Kick implements SlashCommandHandler {

    @Override
    public String getName() {
        return "kick";
    }

    @Override
    public String getDescription() {
        return "kick a user";
    }

    @Override
    public void handle(final SlashCommandInteractionEvent event) {
        try {
            final String adminId =
                    Objects.requireNonNull(event.getMember()).getUser().getId();
            final PrideUser admin = Query.getAccountByDiscordId(adminId);
            final String userId =
                    Objects.requireNonNull(event.getOption("user"))
                            .getAsString();
            final PrideUser user = Query.getAccountByDiscordId(userId);
            if (admin == null) {
                event.reply("You are not registered!").queue();
                return;
            }
            if (user == null) {
                event.reply("This user is not registered!").queue();
                return;
            }
            final Boolean administrates =
                    checkIfAdministratesByUser(admin, user);
            if (!administrates) {
                event.reply("You can't kick this user!").queue();
            } else {
                final OptionMapping pickedUser = Objects.requireNonNull(
                        event.getOption("user")
                );
                final Member userToKick = Objects.requireNonNull(
                        pickedUser.getAsMember()
                );
                userToKick.kick().queue();
                event.reply("Kicked " + user.discordId()).queue();
            }

        } catch (@SuppressWarnings("OverlyBroadCatchBlock") final Exception e) {
            event.reply("An error occurred!").queue();
        }
    }

    @Override
    public void setOptions(final SlashCommandData command) {
        command.addOption(
                OptionType.MENTIONABLE,
                "user",
                "The user to kick",
                true);
    }
}
