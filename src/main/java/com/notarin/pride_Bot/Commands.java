package com.notarin.pride_Bot;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.util.Map;
import java.util.Objects;

public class Commands {
    public static void slashCommand(String command, SlashCommandInteractionEvent event) {
        Map<String, Object> config = ConfigHandler.loadConfig();
        switch (command) {
            case "ping" -> event.reply("Pong!").setEphemeral(true).queue();
            case "say" ->
                    event.reply(Objects.requireNonNull(event.getOption("content")).getAsString()).queue();
            case "leave" -> {
                if (Objects.requireNonNull(event.getMember()).hasPermission(Permission.KICK_MEMBERS)) {
                    event.reply("Leaving the server... :wave:")
                            .flatMap(v -> Objects.requireNonNull(event.getGuild()).leave()) // Leave server after acknowledging the command
                            .queue();
                } else {
                    event.reply("You do not have permissions to kick me.").setEphemeral(true).queue();
                }
            }
            case "suggest", "bug" ->
                event.reply("https://github.com/PrideCraft/Public-Issue-Tracker/issues/new/choose").queue();
            case "update" -> {
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
                    event.reply("Uh-oh, you don't seem to have the required permissions!").setEphemeral(true).queue();
                }
            }
            default -> {
                event.reply("Uh oh, I don't think I have the tools to handle that command :( \nPlease contact an administrator.").setEphemeral(true).queue();
                System.out.println("ERROR: Unhandled command received, info:\n" +
                        "\tCommand Name: " + event.getName() + "\n" +
                        "\tTime: " + event.getTimeCreated() + "\n" +
                        "\tServer: " + event.getGuild() + "\n" +
                        "\tChannel: " + event.getChannel() + "\n" +
                        "\tUser: " + event.getUser()
                );
            }
        }
    }
}
