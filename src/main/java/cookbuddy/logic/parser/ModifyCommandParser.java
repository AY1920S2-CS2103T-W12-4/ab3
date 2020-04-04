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
import static java.util.Objects.requireNonNull;

import java.util.Optional;
import java.util.Set;

import cookbuddy.commons.core.index.Index;
import cookbuddy.logic.commands.ModifyCommand;
import cookbuddy.logic.parser.exceptions.ParseException;
import cookbuddy.model.recipe.attribute.Tag;

/**
 * Parses input arguments and creates a new ModifyCommand object
 */
public class ModifyCommandParser implements Parser<ModifyCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ModifyCommand
     * and returns an ModifyCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public ModifyCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
            ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_INGREDIENTS, PREFIX_INSTRUCTIONS, PREFIX_IMAGEFILEPATH,
                PREFIX_CALORIE, PREFIX_SERVING, PREFIX_RATING, PREFIX_DIFFICULTY, PREFIX_TAG);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, pe.getLocalizedMessage())
                    + "\nFor a command summary, type \"help modify\"");
        }

        ModifyCommand.EditRecipeDescriptor editRecipeDescriptor = new ModifyCommand.EditRecipeDescriptor();
        if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
            editRecipeDescriptor.setName(ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get()));
        }
        if (argMultimap.getValue(PREFIX_INGREDIENTS).isPresent()) {
            editRecipeDescriptor.setIngredients(
                ParserUtil.parseIngredients(argMultimap.getValue(PREFIX_INGREDIENTS).get()));
        }
        if (argMultimap.getValue(PREFIX_INSTRUCTIONS).isPresent()) {
            editRecipeDescriptor.setInstructions(
                ParserUtil.parseInstructions(argMultimap.getValue(PREFIX_INSTRUCTIONS).get()));
        }

        if (argMultimap.getValue(PREFIX_IMAGEFILEPATH).isPresent()) {
            editRecipeDescriptor
                .setImageFilePath(ParserUtil.parseFilePath(argMultimap.getValue(PREFIX_IMAGEFILEPATH).get()));
        }

        if (argMultimap.getValue(PREFIX_SERVING).isPresent()) {
            editRecipeDescriptor.setServing(
                ParserUtil.parseServing(argMultimap.getValue(PREFIX_SERVING).get()));
        }

        if (argMultimap.getValue(PREFIX_CALORIE).isPresent()) {
            editRecipeDescriptor.setCalorie(
                ParserUtil.parseCalorie(argMultimap.getValue(PREFIX_CALORIE).get()));
        }

        if (argMultimap.getValue(PREFIX_RATING).isPresent()) {
            editRecipeDescriptor.setRating(
                ParserUtil.parseRating(argMultimap.getValue(PREFIX_RATING).get()));
        }

        if (argMultimap.getValue(PREFIX_DIFFICULTY).isPresent()) {
            editRecipeDescriptor.setDifficulty(
                ParserUtil.parseDifficulty(argMultimap.getValue(PREFIX_DIFFICULTY).get()));
        }
        parseTagsForEdit(argMultimap.getValue(PREFIX_TAG)).ifPresent(editRecipeDescriptor::setTags);

        if (!editRecipeDescriptor.isAnyFieldEdited()) {
            throw new ParseException(ModifyCommand.MESSAGE_NOT_EDITED);
        }

        return new ModifyCommand(index, editRecipeDescriptor);
    }

    /**
     * Parses {@code Optional<String> tags} into a {@code Set<Tag>} if {@code tags} is non-empty.
     * If {@code tags} is an empty string, it will be parsed into a
     * {@code Set<Tag>} containing zero tags.
     */
    private Optional<Set<Tag>> parseTagsForEdit(Optional<String> tags) throws ParseException {
        assert tags != null;

        if (tags.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(ParserUtil.parseTags(tags));
    }

}
