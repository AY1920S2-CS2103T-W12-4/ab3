package cookbuddy.logic.parser;

import static cookbuddy.commons.core.Messages.MESSAGE_INVALID_RECIPE_DISPLAYED_INDEX;

import cookbuddy.commons.core.index.Index;
import cookbuddy.logic.commands.ViewCommand;
import cookbuddy.logic.parser.exceptions.ParseException;


/**
 * Parses input arguments and creates a new UnFavCommand object
 */
public class ViewCommandParser implements Parser<ViewCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the UnFavCommand
     * and returns a UnFavCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public ViewCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new ViewCommand(index);
        } catch (ParseException pe) {
            throw new ParseException(
                String.format(MESSAGE_INVALID_RECIPE_DISPLAYED_INDEX, ViewCommand.MESSAGE_USAGE), pe);
        }
    }

}
