package com.beabravedude.commandlib.command.parameter;

import com.beabravedude.commandlib.command.converter.Converter;

public class RequiredCommandParameter extends CommandParameter {

    public RequiredCommandParameter(String name, Converter<?> converter) {
        super(name, converter);
    }
}
