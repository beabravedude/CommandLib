package com.beabravedude.commandlib;

import com.beabravedude.commandlib.command.Command;
import com.beabravedude.commandlib.command.executor.CommandLibExecutor;
import com.beabravedude.commandlib.command.subcommand.SubCommand;
import com.beabravedude.commandlib.exception.CommandInitializationException;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class CommandLib {

    public static final Map<String, Command> COMMANDS = new HashMap<>();

    public static void enable(JavaPlugin plugin) {
        new Reflections(ConfigurationBuilder.build()
                .setScanners(Scanners.SubTypes)).getSubTypesOf(Command.class)
                    .forEach(command -> {
                        if(!command.isInterface() && !command.isAnonymousClass() && !Arrays.asList(command.getInterfaces()).contains(SubCommand.class))
                            registerCommand(getInstance(command), plugin);
                    });
    }

    private static Command getInstance(Class<? extends Command> commandClass) {
        try {
            return commandClass.getDeclaredConstructor().newInstance();
        } catch (IllegalAccessException ignored) {
            throw new IllegalStateException("Could not access command '" + commandClass.getName() + "', ensure methods have public access");
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new RuntimeException();
    }

    private static void registerCommand(Command command, JavaPlugin plugin) {
        String commandName = command.getSyntax().getName();
        if (CommandLib.COMMANDS.containsKey(commandName)) throw new CommandInitializationException("Duplicate parent command '" + commandName + "'");
        setCommandExecutor(plugin, command);
        COMMANDS.put(commandName, command);
        addSubCommands(command, null,commandName + " ");
    }

    private static void addSubCommands(Command parent, SubCommand subCommand, String prev) {
        Map<String, SubCommand> subCommands;
        if (subCommand == null) subCommands = parent.getSyntax().getSubCommands();
        else subCommands = subCommand.getSyntax().getSubCommands();
        for (Map.Entry<String, SubCommand> subCommandEntry : subCommands.entrySet()) {
            COMMANDS.put(prev + subCommandEntry.getKey(), subCommandEntry.getValue());
            addSubCommands(parent, subCommandEntry.getValue(), prev + subCommandEntry.getKey() + " ");
        }
    }

    private static void setCommandExecutor(JavaPlugin plugin, Command command) {
        String commandName = command.getSyntax().getName();
        try {
            Objects.requireNonNull(plugin.getCommand(commandName)).setExecutor(new CommandLibExecutor());
        } catch (NullPointerException e) {
            throw new CommandInitializationException("Could not find command '" + commandName + "', make sure it's defined in your plugin.yml file");
        }
    }


}
