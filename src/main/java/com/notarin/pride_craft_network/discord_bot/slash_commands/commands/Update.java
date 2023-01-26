package com.notarin.pride_craft_network.discord_bot.slash_commands.commands;

import com.notarin.pride_craft_network.ConfigHandler;
import com.notarin.pride_craft_network.discord_bot.slash_commands.SlashCommandHandler;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.util.Map;
import java.util.Objects;

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
    public void handle(SlashCommandInteractionEvent event) {
        Map<String, Object> config = ConfigHandler.loadConfig();

        if (Objects.requireNonNull(event.getMember()).getId().equals(config.get("owner-id").toString())) {
            String dir = System.getProperty("user.dir");
            try (Repository botRepo = new FileRepository(dir + "/.git")) {
                Git botGit = new Git(botRepo);
                PullCommand gitPull = botGit.pull()
                        .setRemote("origin")
                        .setRemoteBranchName("main")
                        .setRebase(true)
                        .setCredentialsProvider(
                                new UsernamePasswordCredentialsProvider(config.get("username").toString(), config.get("personal-access-token").toString())
                        );
                gitPull.call();
                event.reply("Finished!").setEphemeral(true).queue();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            event.reply("Uh-oh, you don't seem to have the required " +
                    "permissions!").setEphemeral(true).queue();
        }
    }

    @Override
    public void setOptions(SlashCommandData command) {
    }
}
