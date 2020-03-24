package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CALORIE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INGREDIENTS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INSTRUCTIONS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_RATING;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SERVING;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.ModifyCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.recipe.attribute.Tag;

/**
 * Parses input arguments and creates a new ModifyCommand object
 */
public class ModifyCommandParser implements Parser<ModifyCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ModifyCommand
     * and returns an ModifyCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ModifyCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_INGREDIENTS, PREFIX_INSTRUCTIONS,
                    PREFIX_CALORIE, PREFIX_SERVING, PREFIX_RATING, PREFIX_TAG);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ModifyCommand.MESSAGE_USAGE), pe);
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
        parseTagsForEdit(argMultimap.getAllValues(PREFIX_TAG)).ifPresent(editRecipeDescriptor::setTags);

        if (!editRecipeDescriptor.isAnyFieldEdited()) {
            throw new ParseException(ModifyCommand.MESSAGE_NOT_EDITED);
        }

        return new ModifyCommand(index, editRecipeDescriptor);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>} if {@code tags} is non-empty.
     * If {@code tags} contain only one element which is an empty string, it will be parsed into a
     * {@code Set<Tag>} containing zero tags.
     */
    private Optional<Set<Tag>> parseTagsForEdit(Collection<String> tags) throws ParseException {
        assert tags != null;

        if (tags.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> tagSet = tags.size() == 1 && tags.contains("") ? Collections.emptySet() : tags;
        return Optional.of(ParserUtil.parseTags(tagSet));
    }

}
