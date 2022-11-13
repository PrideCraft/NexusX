package com.notarin.pride_Bot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;

import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

public class SlashCommandRegistrar {
    public static void register(JDA jda) {
        // Prepare building command list
        CommandListUpdateAction commands = jda.updateCommands();

        //noinspection ResultOfMethodCallIgnored
        commands.addCommands(net.dv8tion.jda.api.interactions.commands.build.Commands.slash("ping", "Test connectivity").setGuildOnly(true) // Allow only in Guilds/Servers
        );

        //noinspection ResultOfMethodCallIgnored
        commands.addCommands(net.dv8tion.jda.api.interactions.commands.build.Commands.slash("say", "Makes the bot say what you tell it to").addOption(STRING, "content", "What the bot should say", true) // you can add required options like this too
        );

        //noinspection ResultOfMethodCallIgnored
        commands.addCommands(net.dv8tion.jda.api.interactions.commands.build.Commands.slash("leave", "Make the bot leave the server").setGuildOnly(true) // Allow only in Guilds/Servers
                .setDefaultPermissions(DefaultMemberPermissions.DISABLED)
        );

        //noinspection ResultOfMethodCallIgnored
        commands.addCommands(net.dv8tion.jda.api.interactions.commands.build.Commands.slash("suggest", "For when you want to make a suggestion").setGuildOnly(true) // Allow only in Guilds/Servers
        );

        //noinspection ResultOfMethodCallIgnored
        commands.addCommands(net.dv8tion.jda.api.interactions.commands.build.Commands.slash("bug", "For when you want to report a bug").setGuildOnly(true) // Allow only in Guilds/Servers
        );

        //noinspection ResultOfMethodCallIgnored
        commands.addCommands(net.dv8tion.jda.api.interactions.commands.build.Commands.slash("update", "Rebase to most recent commit")
        );

        // Send the new set of commands to discord, this will override any existing global commands with the new set provided here
        commands.queue();
    }
}
