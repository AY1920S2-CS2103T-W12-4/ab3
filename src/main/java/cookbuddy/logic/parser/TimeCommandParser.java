package cookbuddy.logic.parser;

import static cookbuddy.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import cookbuddy.commons.core.index.Index;
import cookbuddy.logic.commands.TimeCommand;
import cookbuddy.logic.parser.exceptions.ParseException;
import cookbuddy.model.recipe.attribute.Time;

/**
 * Parses input arguments and creates a new FavCommand object
 */
public class TimeCommandParser implements Parser<TimeCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FavCommand
     * and returns a FavCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public TimeCommand parse(String args) throws ParseException {
        try {
            String[] argz = args.substring(1).split(" ");
            Index index = ParserUtil.parseIndex(argz[0]);
           // String[] argz = args.split(" ");
            Time time = ParserUtil.parseTime(argz[1]);
            return new TimeCommand(index, time);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, TimeCommand.MESSAGE_USAGE), pe);
        }
    }

}
