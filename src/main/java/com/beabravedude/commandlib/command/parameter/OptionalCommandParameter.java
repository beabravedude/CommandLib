package com.beabravedude.commandlib.command.parameter;

import com.beabravedude.commandlib.command.converter.Converter;

public class OptionalCommandParameter extends CommandParameter {

    private final Object defaultValue;

    public OptionalCommandParameter(String name, Converter<?> converter, Object defaultValue) {
        super(name, converter);
        this.defaultValue = defaultValue;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

}
