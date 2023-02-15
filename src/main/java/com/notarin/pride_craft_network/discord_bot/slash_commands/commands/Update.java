package com.notarin.pride_craft_network.discord_bot.slash_commands.commands;

import com.notarin.pride_craft_network.ConfigHandler;
import com.notarin.pride_craft_network.discord_bot.slash_commands.SlashCommandHandler;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Repository;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

/**
 * A command for updating the bot.
 */
@SuppressWarnings("unused")
public class Update implements SlashCommandHandler {

    @Override
    public String getName() {
        return "update";
    }

    @Override
    public String getDescription() {
        return "Rebase to most recent commit";
    }

    @Override
    public void handle(final SlashCommandInteractionEvent event) {
        final Map<String, Object> config = ConfigHandler.loadConfig();

        if (Objects.requireNonNull(
                        event.getMember()).getId()
                .equals(config.get("owner-id").toString())) {
            final String dir = System.getProperty("user.dir");
            try (final Repository botRepo = new FileRepository(dir + "/.git")) {
                final Git botGit = new Git(botRepo);
                final PullCommand gitPull = botGit.pull()
                        .setRemote("origin")
                        .setRemoteBranchName("main")
                        .setRebase(true);
                gitPull.call();
                event.reply("Finished!").setEphemeral(true).queue();
            } catch (@SuppressWarnings("OverlyBroadCatchBlock")
            final IOException | GitAPIException e) {
                throw new RuntimeException(e);
            }
        } else {
            event.reply("Uh-oh, you don't seem to have the required " +
                    "permissions!").setEphemeral(true).queue();
        }
    }

    @Override
    public void setOptions(final SlashCommandData command) {
    }
}
