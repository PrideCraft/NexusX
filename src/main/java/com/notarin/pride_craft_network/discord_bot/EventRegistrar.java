package com.notarin.pride_craft_network.discord_bot;

import com.notarin.pride_craft_network.discord_bot.slash_commands.SlashCommandHandler;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * A class that registers all slash commands.
 */
public class EventRegistrar extends ListenerAdapter {
    // yes, I know there's no constructor,
    // but PMD can deal with it; It's unneeded
    final List<Class<?>> commandList;

    /**
     * Creates a new EventRegistrar.
     *
     * @param commandList The list of slash commands
     */
    public EventRegistrar(final List<Class<?>> commandList) {
        this.commandList = commandList;
    }

    public void onSlashCommandInteraction(
            @NotNull final SlashCommandInteractionEvent event) {

        for (final Class<?> clazz : commandList) {
            // Get the constructor for the class
            final Constructor<?> constructor;
            try {
                constructor = clazz.getConstructor();
            } catch (final NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
            // Create a new instance of the class
            final SlashCommandHandler command;
            try {
                command = (SlashCommandHandler) constructor.newInstance();
            } catch (final InstantiationException | IllegalAccessException |
                           InvocationTargetException e) {
                throw new RuntimeException(e);
            }
            if (command.getName().equalsIgnoreCase(
                    event.getInteraction().getName())) {
                command.handle(event);
                break;
            }
        }
    }
}
