package com.beabravedude.commandlib.command.converter;

import com.beabravedude.commandlib.exception.CommandParameterConversionException;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Optional;

public interface Converter<T> {

    T convert(String input);

    String getDisplayName();

    class StringConverter implements Converter<String> {

        @Override
        public String convert(String input) {
            return input;
        }

        @Override
        public String getDisplayName() {
            return "Text";
        }
    }

    class IntegerConverter implements Converter<Integer> {

        @Override
        public Integer convert(String input) {
            return Integer.parseInt(input);
        }

        @Override
        public String getDisplayName() {
            return "Integer";
        }
    }

    class DoubleConverter implements Converter<Double> {

        @Override
        public Double convert(String input) {
            return Double.parseDouble(input);
        }

        @Override
        public String getDisplayName() {
            return "Decimal";
        }
    }

    class PlayerConverter implements Converter<Player> {

        @Override
        public Player convert(String input) {
            return Optional.ofNullable(Bukkit.getPlayer(input)).orElseThrow(() -> new CommandParameterConversionException("&cPlayer " + input + " not found"));
        }

        @Override
        public String getDisplayName() {
            return "Player";
        }
    }

}
