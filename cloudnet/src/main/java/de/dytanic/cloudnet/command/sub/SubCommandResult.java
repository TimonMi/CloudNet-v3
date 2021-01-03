package de.dytanic.cloudnet.command.sub;


import java.util.Optional;

public class SubCommandResult {
    private final SubCommand command;
    private final String errorMessage;

    SubCommandResult(SubCommand command, String errorMessage) {
        this.command = command;
        this.errorMessage = errorMessage;
    }

    public Optional<SubCommand> getCommand() {
        return Optional.ofNullable(command);
    }

    public Optional<String> getErrorMessage() {
        return Optional.ofNullable(errorMessage);
    }
}
