package com.notarin.pride_craft_network.discord_bot;

import com.notarin.pride_craft_network.discord_bot.slash_commands.SlashCommandHandler;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class EventRegistrar extends ListenerAdapter {
    // yes, I know there's no constructor,
    // but PMD can deal with it; It's unneeded
    List<Class<?>> commandList;

    public EventRegistrar(List<Class<?>> commandList) {
        this.commandList = commandList;
    }

    public void onSlashCommandInteraction(
            @NotNull SlashCommandInteractionEvent event) {

        for (Class<?> clazz : commandList) {
            // Get the constructor for the class
            Constructor<?> constructor;
            try {
                constructor = clazz.getConstructor();
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
            // Create a new instance of the class
            SlashCommandHandler command;
            try {
                command = (SlashCommandHandler) constructor.newInstance();
            } catch (InstantiationException | IllegalAccessException |
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
