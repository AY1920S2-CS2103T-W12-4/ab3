package cookbuddy.logic.parser;

import static cookbuddy.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import cookbuddy.commons.core.index.Index;
import cookbuddy.logic.commands.ScaleCommand;
import cookbuddy.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new DuplicateCommand object
 */
public class ScaleCommandParser implements Parser<ScaleCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DuplicateCommand
     * and returns a DuplicateCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public ScaleCommand parse(String args) throws ParseException {
        try {
            String[] splitArgs = args.substring(1).split(" ");
            Index index = ParserUtil.parseIndex(splitArgs[0]);
            int size = ParserUtil.parseSize(splitArgs[1]);
            return new ScaleCommand(index, size);
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, pe.getLocalizedMessage())
                    + "\nFor a command summary, type \"help scale\"");
        }
    }

}
