package com.beabravedude.commandlib.command.syntax;

import com.beabravedude.commandlib.command.converter.Converter;
import com.beabravedude.commandlib.command.parameter.EndlessListCommandParameter;
import com.beabravedude.commandlib.command.parameter.OptionalCommandParameter;
import com.beabravedude.commandlib.command.parameter.RequiredCommandParameter;
import com.beabravedude.commandlib.command.subcommand.SubCommand;
import com.beabravedude.commandlib.exception.CommandInitializationException;
import com.beabravedude.commandlib.exception.CommandParameterSequenceException;

import javax.annotation.Nonnull;

public class CommandSyntaxBuilder {

    private final CommandSyntax commandSyntax;
    private boolean optionalParameterAdded = false;
    private boolean endlessListAdded = false;

    private CommandSyntaxBuilder(@Nonnull String name) {
        this.commandSyntax = new CommandSyntax(name);
    }

    public static CommandSyntaxBuilder create(@Nonnull String commandName) {
        return new CommandSyntaxBuilder(commandName);
    }

    public CommandSyntaxBuilder addSubCommand(@Nonnull  SubCommand subCommand) {
        if (this.commandSyntax.getSubCommands().containsValue(subCommand)) throw new CommandInitializationException("Duplicate sub command '" + subCommand.getSyntax().getName() + "'");
        this.commandSyntax.getSubCommands().put(subCommand.getSyntax().getName(), subCommand);
        return this;
    }

    public CommandSyntaxBuilder addRequiredParameter(@Nonnull String parameterName, @Nonnull Converter<?> parameterConverter) {
        checkEndlessList();
        if (optionalParameterAdded) throw new CommandParameterSequenceException("Required parameters cannot be added after an optional parameter has been added");
        if (!this.commandSyntax.getCommandParameters().add(new RequiredCommandParameter(parameterName, parameterConverter))) throw new CommandParameterSequenceException("Duplicate parameter name '" + parameterName + "'");
        return this;
    }

    public CommandSyntaxBuilder addOptionalParameter(@Nonnull String parameterName, @Nonnull Converter<?> parameterConverter, Object defaultValue) {
        checkEndlessList();
        if (!this.commandSyntax.getCommandParameters().add(new OptionalCommandParameter(parameterName, parameterConverter, defaultValue))) throw new CommandParameterSequenceException("Duplicate parameter name '" + parameterName + "'");
        this.optionalParameterAdded = true;
        return this;
    }

    public CommandSyntaxBuilder addEndlessListParameter(@Nonnull String parameterName, @Nonnull Converter<?> parameterConverter, boolean requireAtLeastOneValue) {
        checkEndlessList();
        if (!this.commandSyntax.getCommandParameters().add(new EndlessListCommandParameter(parameterName, parameterConverter, requireAtLeastOneValue))) throw new CommandParameterSequenceException("Duplicate parameter '" + parameterName + "'");
        this.endlessListAdded = true;
        return this;
    }

    private void checkEndlessList() {
        if (endlessListAdded) throw new CommandParameterSequenceException("No parameters can be added after an endless list has been added");
    }

    public CommandSyntax build() {
        return this.commandSyntax;
    }

}
