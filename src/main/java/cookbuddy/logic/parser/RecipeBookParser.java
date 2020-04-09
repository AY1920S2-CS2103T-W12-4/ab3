package cookbuddy.logic.parser;

import static cookbuddy.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static cookbuddy.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cookbuddy.logic.commands.Command;
import cookbuddy.logic.commands.CountCommand;
import cookbuddy.logic.commands.DeleteCommand;
import cookbuddy.logic.commands.DoneCommand;
import cookbuddy.logic.commands.DuplicateCommand;
import cookbuddy.logic.commands.ExitCommand;
import cookbuddy.logic.commands.FavCommand;
import cookbuddy.logic.commands.FindCommand;
import cookbuddy.logic.commands.HelpCommand;
import cookbuddy.logic.commands.ListCommand;
import cookbuddy.logic.commands.ModifyCommand;
import cookbuddy.logic.commands.NewCommand;
import cookbuddy.logic.commands.RandomCommand;
import cookbuddy.logic.commands.ResetCommand;
import cookbuddy.logic.commands.ScaleCommand;
import cookbuddy.logic.commands.TimeCommand;
import cookbuddy.logic.commands.UnFavCommand;
import cookbuddy.logic.commands.UndoCommand;
import cookbuddy.logic.commands.ViewCommand;
import cookbuddy.logic.parser.exceptions.ParseException;

/**
 * Parses user input.
 */
public class RecipeBookParser {

    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");

    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     * @throws ParseException if the user input does not conform the expected format
     */
    public Command parseCommand(String userInput) throws ParseException {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");
        switch (commandWord) {

        case NewCommand.COMMAND_WORD:
            return new NewCommandParser().parse(arguments);

        case ModifyCommand.COMMAND_WORD:
            return new ModifyCommandParser().parse(arguments);

        case DeleteCommand.COMMAND_WORD:
            return new DeleteCommandParser().parse(arguments);

        case DoneCommand.COMMAND_WORD:
            return new DoneCommandParser().parse(arguments);

        case DuplicateCommand.COMMAND_WORD:
            return new DuplicateCommandParser().parse(arguments);

        case UndoCommand.COMMAND_WORD:
            return new UndoCommandParser().parse(arguments);

        case FavCommand.COMMAND_WORD:
            return new FavCommandParser().parse(arguments);

        case UnFavCommand.COMMAND_WORD:
            return new UnFavCommandParser().parse(arguments);

        case ResetCommand.COMMAND_WORD:
            if (!arguments.equals("")) {
                throw new ParseException(("The reset command does not take in any arguments!"));
            }
            return new ResetCommand();

        case ViewCommand.COMMAND_WORD:
            return new ViewCommandParser().parse(arguments);

        case FindCommand.COMMAND_WORD:
            return new FindCommandParser().parse(arguments);

        case ListCommand.COMMAND_WORD:
            return new ListCommand();

        case RandomCommand.COMMAND_WORD:
            return new RandomCommand();

        case ScaleCommand.COMMAND_WORD:
            return new ScaleCommandParser().parse(arguments);

        case TimeCommand.COMMAND_WORD:
            return new TimeCommandParser().parse(arguments);

        case CountCommand.COMMAND_WORD:
            if (!arguments.equals("")) {
                throw new ParseException("The count command does not take in any arguments!");
            }
            return new CountCommand();

        case ExitCommand.COMMAND_WORD:
            if (!arguments.equals("")) {
                throw new ParseException("The exit command does not take in any arguments!");
            }
            return new ExitCommand();

        case HelpCommand.COMMAND_WORD:
            if (arguments.equals("")) {
                return new HelpCommand("");
            } else {
                return new HelpCommandParser().parse(arguments);
            }

        default:
            throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        }
    }

}
