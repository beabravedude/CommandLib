package com.beabravedude.commandlib.command.executor;

import com.beabravedude.commandlib.CommandLib;
import com.beabravedude.commandlib.command.parameter.CommandParameter;
import com.beabravedude.commandlib.command.parameter.EndlessListCommandParameter;
import com.beabravedude.commandlib.command.parameter.OptionalCommandParameter;
import com.beabravedude.commandlib.command.syntax.CommandSyntax;
import com.beabravedude.commandlib.exception.CommandParameterValidationException;
import com.beabravedude.commandlib.exception.CommandParameterConversionException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;

public class CommandLibExecutor implements org.bukkit.command.CommandExecutor {

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command cmd, @Nonnull String label, @Nonnull String[] args) {
        List<String> params = new LinkedList<>(Arrays.asList(args));
        StringBuilder builder = new StringBuilder(label);
        String commandString = builder.toString();
        for (String param : params) {
            builder.append(" ").append(param);
            if (CommandLib.COMMANDS.containsKey(builder.toString())) {
                commandString = builder.toString();
            }
        }
        com.beabravedude.commandlib.command.Command command = CommandLib.COMMANDS.get(commandString);
        params.removeAll(Arrays.asList(commandString.split(" ")));
        CommandSyntax commandSyntax = command.getSyntax();
        if (commandSyntax.getCommandParameters().isEmpty()) {
            if (params.isEmpty()) {
                command.execute(sender, Collections.emptyMap());
                return true;
            } else {
                throw new CommandParameterValidationException();
            }
        }

        try {
            command.execute(sender, insertCommandParameters(commandSyntax, params));
        } catch (CommandParameterConversionException e) {
            sender.sendMessage(e.getMessage());
            return false;
        } catch (CommandParameterValidationException e) {
            command.onError(sender, commandSyntax, params);
            return false;
        }
        return true;
    }

    private Map<String, Optional<?>> insertCommandParameters(CommandSyntax commandSyntax, List<String> args) {
        List<CommandParameter> commandParameters = new LinkedList<>(commandSyntax.getCommandParameters());
        if (args.size() < commandParameters.stream().filter(commandParameter -> !(commandParameter instanceof OptionalCommandParameter || commandParameter instanceof EndlessListCommandParameter)).count())
            throw new CommandParameterValidationException();
        if (!(commandParameters.get(commandParameters.size()-1) instanceof EndlessListCommandParameter) && args.size() > commandParameters.size())
            throw new CommandParameterValidationException();
        return commandParameters.stream()
                .collect(Collectors.toMap(CommandParameter::getName, commandParameter -> {
                    try {
                        return attemptConversion(commandParameter,
                                commandParameter instanceof EndlessListCommandParameter ?
                                        args.subList(commandParameters.indexOf(commandParameter), args.size()) :
                                        args.get(commandParameters.indexOf(commandParameter)));
                    } catch (IndexOutOfBoundsException ignored) {
                        return commandParameter instanceof OptionalCommandParameter ? Optional.ofNullable(((OptionalCommandParameter)commandParameter).getDefaultValue()) : Optional.empty();
                    }
                }));
    }

    private Optional<?> attemptConversion(CommandParameter commandParameter, Object arg) {
        if (commandParameter instanceof EndlessListCommandParameter && arg instanceof List<?>) {
            if (((EndlessListCommandParameter) commandParameter).isRequireAtLeastOneValue() && ((List<?>) arg).isEmpty()) throw new CommandParameterValidationException();
            return Optional.of(((List<?>) arg).stream()
                    .map(param -> commandParameter.getConverter().convert(String.valueOf(param)))
                    .collect(Collectors.toList()));
        }
        return Optional.of(commandParameter.getConverter().convert(String.valueOf(arg)));
    }

}
