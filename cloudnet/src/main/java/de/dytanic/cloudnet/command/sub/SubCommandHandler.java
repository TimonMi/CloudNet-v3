package de.dytanic.cloudnet.command.sub;

import de.dytanic.cloudnet.CloudNet;
import de.dytanic.cloudnet.command.Command;
import de.dytanic.cloudnet.command.ConsoleCommandSender;
import de.dytanic.cloudnet.command.ICommandSender;
import de.dytanic.cloudnet.command.ITabCompleter;
import de.dytanic.cloudnet.common.Properties;
import de.dytanic.cloudnet.common.collection.Pair;
import de.dytanic.cloudnet.common.language.LanguageManager;
import de.dytanic.cloudnet.console.animation.questionlist.QuestionAnswerType;
import de.dytanic.cloudnet.console.animation.questionlist.answer.QuestionAnswerTypeBoolean;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class SubCommandHandler extends Command implements ITabCompleter {

    private Collection<SubCommand> subCommands = new ArrayList<>();

    public SubCommandHandler(String... names) {
        super(names);
    }

    public SubCommandHandler(Collection<SubCommand> subCommands, String... names) {
        super(names);
        this.subCommands = subCommands;
    }

    public SubCommandHandler(String[] names, String permission, Collection<SubCommand> subCommands) {
        super(names, permission);
        this.subCommands = subCommands;
    }

    public SubCommandHandler(String[] names, String permission, String description, Collection<SubCommand> subCommands) {
        super(names, permission, description);
        this.subCommands = subCommands;
    }

    public SubCommandHandler(String[] names, String permission, String description, String usage, String prefix, Collection<SubCommand> subCommands) {
        super(names, permission, description, usage, prefix);
        this.subCommands = subCommands;
    }

    @Override
    public String getUsage() {
        Collection<String> messages = new ArrayList<>();
        for (SubCommand subCommand : this.subCommands) {
            String message = super.getNames()[0] + " " + subCommand.getArgsAsString() + subCommand.getExtendedUsage();

            if (subCommand.getPermission() != null) {
                message += " | " + subCommand.getPermission();
            }

            if (subCommand.getDescription() != null) {
                message += " | " + subCommand.getDescription();
            }
            messages.add(message);
        }
        if (messages.isEmpty()) {
            return null;
        }
        if (messages.size() == 1) {
            return messages.iterator().next();
        }
        return "\n - " + String.join("\n - ", messages);
    }

    @Override
    public void execute(ICommandSender sender, String command, String[] args, String commandLine, Properties properties) {
        SubCommandResult result = getResult(args);
        if (!result.getCommand().isPresent()) {
            this.sendHelp(sender);
            return;
        }
        SubCommand subCommand = result.getCommand().get();
        Optional<String> invalidMessage = result.getErrorMessage();

        if (invalidMessage.isPresent()) {
            sender.sendMessage(invalidMessage.get());
            return;
        }

        SubCommandArgument<?>[] parsedArgs = subCommand.parseArgs(args);
        if (parsedArgs.length == 0) {
            this.sendHelp(sender);
            return;
        }

        if (subCommand.isOnlyConsole() && !(sender instanceof ConsoleCommandSender)) {
            sender.sendMessage(LanguageManager.getMessage("command-sub-only-console"));
            return;
        }

        if (subCommand.getPermission() != null && !sender.hasPermission(subCommand.getPermission())) {
            sender.sendMessage(LanguageManager.getMessage("command-sub-no-permission"));
            return;
        }

        if (subCommand.isAsync()) {
            CloudNet.getInstance().getTaskScheduler().schedule(() ->
                    subCommand.execute(
                            subCommand, sender, command, new SubCommandArgumentWrapper(parsedArgs),
                            commandLine, subCommand.parseProperties(args), new HashMap<>()
                    )
            );
        } else {
            subCommand.execute(
                    subCommand, sender, command, new SubCommandArgumentWrapper(parsedArgs),
                    commandLine, subCommand.parseProperties(args), new HashMap<>()
            );
        }
    }

    @NotNull
    private SubCommandResult getResult(String[] args) {
        Optional<Pair<SubCommand, String>> commandAndError = this.subCommands.stream()
                .filter(subCommand -> subCommand.checkValidArgsLength(args.length))
                .filter(subCommand -> validateArguments(args, subCommand))
                .map(subCommand -> {
                    String error = subCommand.getInvalidArgumentMessage(args);
                    if (error == null) {
                        return new Pair<>(subCommand, (String) null);
                    } else {
                        return new Pair<>(subCommand, error);
                    }
                }).findFirst();
        if (!commandAndError.isPresent()) return new SubCommandResult(null, null);
        return new SubCommandResult(commandAndError.get().getFirst(), commandAndError.get().getSecond());
    }

    private boolean validateArguments(String[] args, SubCommand subCommand) {
        QuestionAnswerType<?>[] requiredArguments = subCommand.getRequiredArguments();
        List<Pair<Integer, QuestionAnswerType<?>>> checkLater = new ArrayList<>();
        for (int i = 0; i < requiredArguments.length; i++) { // Firstly filter out the commands using the static arguments
            QuestionAnswerType<?> type = requiredArguments[i];
            if (type instanceof QuestionAnswerTypeStaticString
                    || type instanceof QuestionAnswerTypeStaticStringArray
                    || type.getClass().getPackage().equals(QuestionAnswerTypeBoolean.class.getPackage())) {
                if (!type.isValidInput(args[i])) {
                    return false;
                } else {
                    checkLater.add(new Pair<>(i, type));
                }
            }
        }
        for (Pair<Integer, QuestionAnswerType<?>> pair : checkLater) { //Then filter using the remaining variables
            if (!pair.getSecond().isValidInput(args[pair.getFirst()])) {
                return false;
            }
        }
        return true;
    }

    protected void sendHelp(ICommandSender sender) {
        for (String usageLine : this.getUsage().split("\n")) {
            sender.sendMessage(usageLine);
        }
    }

    @Override
    public Collection<String> complete(String commandLine, String[] args, Properties properties) {
        return this.subCommands.stream()
                .map(subCommand -> subCommand.getNextPossibleArgumentAnswers(args))
                .filter(Objects::nonNull)
                .filter(responses -> !responses.isEmpty())
                .flatMap(Collection::parallelStream)
                .collect(Collectors.toSet());
    }

    protected void setSubCommands(Collection<SubCommand> subCommands) {
        this.subCommands = subCommands;
    }
}
