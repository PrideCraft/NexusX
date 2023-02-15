package com.notarin.pride_craft_network.discord_bot.slash_commands.commands;

import com.notarin.pride_craft_network.discord_bot.slash_commands.SlashCommandHandler;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * A command for batch deleting messages in a channel.
 */
@SuppressWarnings("unused")
public class BatchDelete implements SlashCommandHandler {

    @Override
    public String getName() {
        return "batch-delete";
    }

    @Override
    public String getDescription() {
        return "Deletes N messages from the channel.";
    }

    @Override
    public void handle(@NotNull final SlashCommandInteractionEvent event) {
        if (Objects.requireNonNull(event.getMember()).getPermissions()
                .contains(Permission.MESSAGE_MANAGE)) {
            final int amount = Objects.requireNonNull(event.getOption("amount"))
                    .getAsInt();
            event.getChannel().getIterableHistory().takeAsync(amount)
                    .thenAcceptAsync(messages -> {
                        event.getChannel()
                                .purgeMessages(messages);
                        event.reply("Deleting " + amount + " messages.")
                                .queue();
                    });
        } else {
            event.reply("You do not have permission to use this command.")
                    .setEphemeral(true).queue();
        }

    }

    @Override
    public void setOptions(@NotNull final SlashCommandData command) {
        command.addOption(OptionType.INTEGER, "amount",
                "The amount of messages to delete.", true);
        command.setDefaultPermissions(DefaultMemberPermissions.DISABLED);
    }
}
