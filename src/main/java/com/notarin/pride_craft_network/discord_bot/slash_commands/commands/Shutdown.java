package com.notarin.pride_craft_network.discord_bot.slash_commands.commands;

import com.notarin.pride_craft_network.ConfigHandler;
import com.notarin.pride_craft_network.discord_bot.slash_commands.SlashCommandHandler;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.util.Map;
import java.util.Objects;

@SuppressWarnings("unused")
public class Shutdown implements SlashCommandHandler {

    @Override
    public String getName() {
        return "shutdown";
    }

    @Override
    public String getDescription() {
        return "shuts the bot down";
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        Map<String, Object> config = ConfigHandler.loadConfig();

        if (Objects.requireNonNull(
                        event.getMember()).getId()
                .equals(config.get("owner-id").toString())) {
            event.reply("Shutting down... :wave:").setEphemeral(true).queue();
            System.exit(0);
        } else {
            event.reply(
                            "Uh-oh, you don't seem to have the required " +
                                    "permissions!")
                    .setEphemeral(true).queue();
        }
    }

    @Override
    public void setOptions(SlashCommandData command) {
    }
}
