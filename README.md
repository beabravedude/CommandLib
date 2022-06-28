# CommandLib
[![Maven Central](https://img.shields.io/maven-central/v/com.beabravedude/commandlib.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.beabravedude%22%20AND%20a:%22commandlib%22)

A simple light-weight utility to make creating Spigot commands easier!

With CommandLib, you can:
* Easily create commands without manually setting executors
* Verify command parameters without endless conditions and statements
* Automatically convert command parameters to desired types
* Create infinite sub commands for any command including chained sub commands
* Reduce command execution errors

## Installation
Maven:
```xml
<dependency>
    <groupId>com.beabravedude</groupId>
    <artifactId>command-lib</artifactId>
    <version>1.1.2</version>
</dependency>
```
Gradle:
```
implementation 'com.beabravedude:command-lib:1.1.2'
```
## Usage
To enable CommandLib, insert the following in your plugin `onEnable()` method:
```java
CommandLib.enable(this);
```
If your main plugin class is different from the current one, Replace `this` with an instance of that class.
### Creating Commands
The `Command` interface should only be used for root commands that are defined in your `plugin.yml` file.
#### Parent Command
To create a parent command, create a class that implements the `Command` interface and call it whatever you want.

**Note: Make sure you're using the `Command` interface from `com.beabravedude.commandlib.command`**

Your class should look like this:
```java
public class SampleCommand implements Command {
    @Override
    public void execute(CommandSender commandSender, Map<String, Optional<?>> map) {
        
    }

    @Nonnull
    @Override
    public CommandSyntax getSyntax() {
        return null;
    }

    @Override
    public void onError(CommandSender commandSender, CommandSyntax commandSyntax, List<String> strings) {
        
    }
}
```
There are 3 methods to implement:

`execute(CommandSender, Map<String, Optional<?>>)`: This method is executed if all parameter requirements have been met and all parameters have been converted to the desired type.
* `CommandSender`: The sender of the command
* `Map<String, Optional<?>>`: A list of parameters with their assigned names in the `CommandSyntax`

`getSyntax()`: This method defines the "meta" of the command such as parameters and parameter types

`onError(CommandSender, CommandSyntax, List<String>)`: This method is executed when the command fails to execute. This is not meant to be confused with a conversion error, which is only thrown when a parameter fails to be converted by the associated converter.
* `CommandSender`: The sender of the command
* `CommandSyntax`: The syntax of the command derived from `getSyntax()`
* `List<String>`: The parameters provided to the command
#### Sub Command
To create a sub command, create a class and implement the `SubCommand` interface.

**Note: Make sure you're using the `SubCommand` interface from `com.beabravedude.commandlib.command.subcommand`**

Your class should look like this:
```java
public class SampleCommand implements SubCommand {
    @Override
    public void execute(CommandSender commandSender, Map<String, Optional<?>> map) {
        
    }

    @Nonnull
    @Override
    public CommandSyntax getSyntax() {
        return null;
    }

    @Override
    public void onError(CommandSender commandSender, CommandSyntax commandSyntax, List<String> strings) {
        
    }
}
```
The same 3 methods need to be implemented:

`execute(CommandSender, Map<String, Optional<?>>)`: This method is executed if all parameter requirements have been met and all parameters have been converted to the desired type.
* `CommandSender`: The sender of the command
* `Map<String, Optional<?>>`: A list of parameters with their assigned names in the `CommandSyntax`

`getSyntax()`: This method defines the "meta" of the command such as parameters and parameter types

`onError(CommandSender, CommandSyntax, List<String>)`: This method is executed when the command fails to execute. This is not meant to be confused with a conversion error, which is only thrown when a parameter fails to be converted by the associated converter.
* `CommandSender`: The sender of the command
* `CommandSyntax`: The syntax of the command derived from `getSyntax()`
* `List<String>`: The parameters provided to the command
### Building a `CommandSyntax` Object
**A `CommandSyntax` object should only be created using a `CommandSyntaxBuilder`**

`CommandSyntaxBuilder` can be used to build a `Command` and `SubCommand` syntax.

Use the `create(String)` method to initialize a builder with any command name. If this is a syntax for a parent command, ensure the command name is exactly as it appears in your `plugin.yml` file.
```java
CommandSyntaxBuilder.create("example")
```
After the `CommandSyntaxBuilder` instance has been created, the following methods are available for you to use:
* `addRequiredParameter(String parameterName, Converter<?> parameterConverter)`: Creates a non-optional command parameter with the given name and type converter.
* `addOptionalParameter(String parameterName, Converter<?> parameterConverter, Object defaultValue)`: Creates an optional parameter with the given name and type converter. If the parameter is not supplied by the command sender, the nullable default value will be used. **No non-optional parameters can be added after an optional parameter has been added**
* `addEndlessListParameter(String parameterName, Converter<?> parameterConverter, boolean requireAtLeastOneValue)`: Creates a parameter with the given name and type converter that accepts an endless list of values. If `requireAtLeastOneValue` is true, at least one or more values must be supplied. **No other parameters can be added after an endless list has been added**
* `addSubCommand(SubCommand subCommand)`: Adds a sub command to the current command.
* `build()`: Returns the configured `CommandSyntax` object;

For example:
```java
@Nonnull
@Override
public CommandSyntax getSyntax() {
    return CommandSyntaxBuilder.create("example")
        .addRequiredParameter("player", new Converter.PlayerConverter())
        .addRequiredParameter("time", new Converter.DoubleConverter())
        .addOptionalParameter("repeat", new Converter.IntegerConverter(), 0)
        .addSubCommand(new ExampleDeletePlayer())
        .build();
}
```
### Converters
By default, CommandLib provides 4 converters to convert command parameters to a desired type:
* `Converter.StringConverter()`: Converts parameter to a `String`
* `Converter.IntegerConverter()`: Converts parameter to a `Integer`
* `Converter.DoubleConverter()`: Converts parameter to a `Double`
* `Converter.PlayerConverter()`: Converts parameter to a `Player`
#### Creating your own converter
To create your own converter, create a class and implement the `Converter<>` interface.
Set the type parameter (`<>`) to the class that the convert is converting to.

Implement the following methods:
* `convert(String input)`: Logic to return the desired class from the `String` input.
* `getDisplayName()`: Returns a user-friendly name for the converter.

For example:
```java
public class ItemStackConverter implements Converter<ItemStack> {
    @Override
    public ItemStack convert(String s) {
        try {
            return new ItemStack(Material.matchMaterial(s));
        } catch (Exception ignored) {
            throw new CommandParameterConversionException(ChatColor.translateAlternateColorCodes('&', "&cItem " + s + " not found"));
        }
    }

    @Override
    public String getDisplayName() {
        return "Item";
    }
}
```
If the conversion fails for any reason, throw a `CommandParameterConversionException(String message)` with a message to send to the command sender.

Enjoy :)


 

