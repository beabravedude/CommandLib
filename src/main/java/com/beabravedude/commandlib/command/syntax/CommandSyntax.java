package com.beabravedude.commandlib.command.syntax;

import com.beabravedude.commandlib.command.parameter.CommandParameter;
import com.beabravedude.commandlib.command.subcommand.SubCommand;

import java.util.*;


public class CommandSyntax {

    private final String name;

    private final Set<CommandParameter> commandParameters = new LinkedHashSet<>();

    private final Map<String, SubCommand> subCommands = new HashMap<>();

    protected CommandSyntax(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Set<CommandParameter> getCommandParameters() {
        return commandParameters;
    }

    public Map<String, SubCommand> getSubCommands() {
        return subCommands;
    }


}
