package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CALORIE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INGREDIENTS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INSTRUCTIONS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_RATING;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SERVING;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Set;
import java.util.stream.Stream;

import seedu.address.logic.commands.NewCommand;
import seedu.address.logic.parser.exceptions.ParseException;

import seedu.address.model.recipe.Recipe;
import seedu.address.model.recipe.attribute.Calorie;
import seedu.address.model.recipe.attribute.IngredientList;
import seedu.address.model.recipe.attribute.InstructionList;
import seedu.address.model.recipe.attribute.Name;
import seedu.address.model.recipe.attribute.Rating;
import seedu.address.model.recipe.attribute.Serving;
import seedu.address.model.recipe.attribute.Tag;

/**
 * Parses input arguments and creates a new NewCommand object
 */
public class NewCommandParser implements Parser<NewCommand> {

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

    /**
     * Parses the given {@code String} of arguments in the context of the NewCommand
     * and returns an NewCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public NewCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_INGREDIENTS,
                PREFIX_INSTRUCTIONS, PREFIX_CALORIE, PREFIX_SERVING, PREFIX_RATING, PREFIX_TAG);

        if (!arePrefixesPresent(argMultimap, PREFIX_NAME, PREFIX_INGREDIENTS, PREFIX_INSTRUCTIONS) || !argMultimap
                .getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, NewCommand.MESSAGE_USAGE));
        }

        Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get());
        IngredientList ingredients = ParserUtil.parseIngredients(argMultimap.getValue(PREFIX_INGREDIENTS).get());
        InstructionList instructions = ParserUtil.parseInstructions(argMultimap.getValue(PREFIX_INSTRUCTIONS).get());
        Calorie calorie = ParserUtil.parseCalorie(argMultimap.getValue(PREFIX_CALORIE).orElse("0"));
        Serving serving = ParserUtil.parseServing(argMultimap.getValue(PREFIX_SERVING).orElse("1"));
        Rating rating = ParserUtil.parseRating(argMultimap.getValue(PREFIX_RATING).orElse("0"));
        Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));

        Recipe recipe = new Recipe(name, ingredients, instructions, calorie, serving, rating, tagList);

        return new NewCommand(recipe);
    }
}
