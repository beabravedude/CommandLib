package com.beabravedude.commandlib.command.parameter;

import com.beabravedude.commandlib.command.converter.Converter;

public class EndlessListCommandParameter extends CommandParameter {

    private final boolean requireAtLeastOneValue;

    public EndlessListCommandParameter(String name, Converter<?> converter, boolean requireAtLeastOneValue) {
        super(name, converter);
        this.requireAtLeastOneValue = requireAtLeastOneValue;
    }

    public boolean isRequireAtLeastOneValue() {
        return requireAtLeastOneValue;
    }

}
