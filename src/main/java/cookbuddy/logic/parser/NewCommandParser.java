package cookbuddy.logic.parser;

import static cookbuddy.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static cookbuddy.logic.parser.CliSyntax.PREFIX_CALORIE;
import static cookbuddy.logic.parser.CliSyntax.PREFIX_DIFFICULTY;
import static cookbuddy.logic.parser.CliSyntax.PREFIX_IMAGEFILEPATH;
import static cookbuddy.logic.parser.CliSyntax.PREFIX_INGREDIENTS;
import static cookbuddy.logic.parser.CliSyntax.PREFIX_INSTRUCTIONS;
import static cookbuddy.logic.parser.CliSyntax.PREFIX_NAME;
import static cookbuddy.logic.parser.CliSyntax.PREFIX_RATING;
import static cookbuddy.logic.parser.CliSyntax.PREFIX_SERVING;
import static cookbuddy.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Set;
import java.util.stream.Stream;

import cookbuddy.logic.commands.NewCommand;
import cookbuddy.logic.parser.exceptions.ParseException;
import cookbuddy.model.recipe.Recipe;
import cookbuddy.model.recipe.testattribute.Calorie;
import cookbuddy.model.recipe.testattribute.Difficulty;
import cookbuddy.model.recipe.testattribute.IngredientList;
import cookbuddy.model.recipe.testattribute.InstructionList;
import cookbuddy.model.recipe.testattribute.Name;
import cookbuddy.model.recipe.testattribute.Photograph;
import cookbuddy.model.recipe.testattribute.Rating;
import cookbuddy.model.recipe.testattribute.Serving;
import cookbuddy.model.recipe.testattribute.Tag;

/**
 * Parses input arguments and creates a new NewCommand object
 */
public class NewCommandParser implements Parser<NewCommand> {

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values
     * in the given {@code ArgumentMultimap}.
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
                PREFIX_INSTRUCTIONS, PREFIX_IMAGEFILEPATH, PREFIX_CALORIE, PREFIX_SERVING, PREFIX_RATING,
                PREFIX_DIFFICULTY, PREFIX_TAG);

        if (!arePrefixesPresent(argMultimap, PREFIX_NAME, PREFIX_INGREDIENTS, PREFIX_INSTRUCTIONS)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, NewCommand.MESSAGE_USAGE));
        }

        Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get());
        IngredientList ingredients = ParserUtil.parseIngredients(argMultimap.getValue(PREFIX_INGREDIENTS).get());
        InstructionList instructions = ParserUtil.parseInstructions(argMultimap.getValue(PREFIX_INSTRUCTIONS).get());
        Photograph photograph = ParserUtil.parsePhotoFilePath(argMultimap.getValue(PREFIX_IMAGEFILEPATH)
                .orElse(Photograph.IMAGE_UTIL.PLACEHOLDER_IMAGE_PATH_STRING));
        Calorie calorie = ParserUtil.parseCalorie(argMultimap.getValue(PREFIX_CALORIE).orElse("0"));
        Serving serving = ParserUtil.parseServing(argMultimap.getValue(PREFIX_SERVING).orElse("1"));
        Rating rating = ParserUtil.parseRating(argMultimap.getValue(PREFIX_RATING).orElse("0"));
        Difficulty difficulty = ParserUtil.parseDifficulty(argMultimap.getValue(PREFIX_DIFFICULTY).orElse("0"));
        Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getValue(PREFIX_TAG));

        Recipe recipe = new Recipe(name, ingredients, instructions, photograph, calorie, serving, rating, difficulty,
                tagList);

        return new NewCommand(recipe);
    }
}
