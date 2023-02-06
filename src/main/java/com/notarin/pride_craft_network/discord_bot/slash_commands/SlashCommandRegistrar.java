package com.notarin.pride_craft_network.discord_bot.slash_commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * A class for registering slash commands.
 */
public abstract class SlashCommandRegistrar {
    /**
     * Registers the slash commands.
     *
     * @param jda The JDA instance to register the commands to
     * @param commandClasses The classes to register
     * @throws NoSuchMethodException Should never be thrown, but if it does,
     * the reflector found a method that doesn't exist
     * @throws InvocationTargetException Thrown when registering slash commands
     * fails
     * @throws InstantiationException Thrown when registering slash commands
     * fails
     * @throws IllegalAccessException Thrown when registering slash commands
     * fails
     */
    public static void register(JDA jda, List<Class<?>> commandClasses) throws
            NoSuchMethodException, InvocationTargetException,
            InstantiationException, IllegalAccessException {
        CommandListUpdateAction updateCommands;
        Collection<CommandData> commands = new ArrayList<>();
        for (Class<?> clazz : commandClasses) {
            // Get the constructor for the class
            Constructor<?> constructor = clazz.getConstructor();
            // Create a new instance of the class
            SlashCommandHandler command =
                    (SlashCommandHandler) constructor.newInstance();
            String commandName = command.getName();
            String commandDescription = command.getDescription();
            SlashCommandData slashCommand =
                    Commands.slash(
                            commandName.toLowerCase(),
                            commandDescription);
            command.setOptions(slashCommand);
            commands.add(slashCommand);
        }
        updateCommands = jda.updateCommands().addCommands(
                commands
        );
        updateCommands.queue();
    }

    /**
     * Loads the classes that extend SlashCommandHandler.
     *
     * @return A list of classes that extend SlashCommandHandler
     */
    public static List<Class<?>> loadCommands() {

        Reflections reflections = new Reflections(
                "com.notarin.pride_craft_network." +
                        "discord_bot.slash_commands.commands"
        );

        Set<Class<? extends SlashCommandHandler>> commandClassSet =
                reflections.getSubTypesOf(SlashCommandHandler.class);

        return new ArrayList<>(commandClassSet);
    }
}
