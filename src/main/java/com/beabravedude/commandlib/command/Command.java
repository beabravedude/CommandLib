package com.beabravedude.commandlib.command;

import com.beabravedude.commandlib.command.syntax.CommandSyntax;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface Command {

    void execute(CommandSender sender, Map<String, Optional<?>> parameters);

    @Nonnull
    CommandSyntax getSyntax();

    void onError(CommandSender commandSender, CommandSyntax commandSyntax, List<String> args);


}
