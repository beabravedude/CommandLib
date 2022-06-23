package com.beabravedude.commandlib.command.parameter;

import com.beabravedude.commandlib.command.converter.Converter;

import java.util.Objects;

public abstract class CommandParameter {

    private final String name;
    private final Converter<?> converter;

    public CommandParameter(String name, Converter<?> converter) {
        this.name = name;
        this.converter = converter;
    }

    public String getName() {
        return name;
    }

    public Converter<?> getConverter() {
        return converter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommandParameter)) return false;
        CommandParameter that = (CommandParameter) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

}
