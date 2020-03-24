package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.recipe.attribute.Calorie;
import seedu.address.model.recipe.attribute.Ingredient;
import seedu.address.model.recipe.attribute.IngredientList;
import seedu.address.model.recipe.attribute.Instruction;
import seedu.address.model.recipe.attribute.InstructionList;
import seedu.address.model.recipe.attribute.Name;
import seedu.address.model.recipe.attribute.Rating;
import seedu.address.model.recipe.attribute.Serving;
import seedu.address.model.recipe.attribute.Tag;

/**
 * Contains utility methods used for parsing strings in the various *Parser
 * classes.
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "Index is not a non-zero unsigned integer.";

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading
     * and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the specified index is invalid (not non-zero
     *                        unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses a {@code String name} into a {@code Name}. Leading and trailing
     * whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code name} is invalid.
     */
    public static Name parseName(String name) throws ParseException {
        requireNonNull(name);
        String trimmedName = name.trim();
        if (!Name.isValidName(trimmedName)) {
            throw new ParseException(Name.MESSAGE_CONSTRAINTS);
        }
        return new Name(trimmedName);
    }

    /**
     * Parses {@code ingredientString} into an {@link IngredientList}. Leading and
     * trailing whitespaces will be trimmed.
     *
     * @throws ParseException if {@code ingredientString} is invalid
     */
    public static IngredientList parseIngredients(String ingredientString) throws ParseException {
        requireNonNull(ingredientString);

        if (ingredientString.isBlank()) {
            throw new ParseException("Recipes need to have instructions; please enter some instructions.");
        }

        return new IngredientList(Stream.of(ingredientString.trim().split(";")).map(String::trim).map(Ingredient::new)
                .collect(Collectors.toList()));
    }

    /**
     * Parses {@code instructionString} into a {@link InstructionList}. Leading and
     * trailing whitespaces will be trimmed.
     *
     * @throws ParseException if {@code instructionString} is blank, as specified by
     *                        {@link String#isBlank()}
     */
    public static InstructionList parseInstructions(String instructionString) throws ParseException {
        requireNonNull(instructionString);

        if (instructionString.isBlank()) {
            throw new ParseException("Recipes need to have ingredients; please enter some ingredients.");
        }

        return new InstructionList(Stream.of(instructionString.trim().split(";")).map(String::trim)
                .map(Instruction::new).collect(Collectors.toList()));
    }

    /**
     * Parses a {@code String calorie} into a {@code Calorie}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code calorie} is invalid.
     */
    public static Calorie parseCalorie(String calorie) throws ParseException {
        requireNonNull(calorie);
        String trimmedCalorie = calorie.trim();
        if (!Calorie.isValidCalorieAmount(trimmedCalorie)) {
            throw new ParseException(Calorie.MESSAGE_CONSTRAINTS);
        }
        return new Calorie(trimmedCalorie);
    }

    /**
     * Parses a {@code String servingString} into a {@code Serving}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code servingString} is invalid.
     */
    public static Serving parseServing(String servingString) throws ParseException {
        requireNonNull(servingString);
        int serving = Integer.parseInt(servingString.trim());
        if (!Serving.isValidServing(serving)) {
            throw new ParseException(Serving.MESSAGE_CONSTRAINTS);
        }
        return new Serving(serving);
    }

    /**
     * Parses a {@code String ratingString} into a {@code Rating}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code ratingString} is invalid.
     */
    public static Rating parseRating(String ratingString) throws ParseException {
        requireNonNull(ratingString);
        int rating = Integer.parseInt(ratingString.trim());
        if (!Rating.isValidRating(rating)) {
            throw new ParseException(Rating.MESSAGE_CONSTRAINTS);
        }
        return new Rating(rating);
    }

    /**
     * Parses a {@code String tag} into a {@code Tag}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code tag} is invalid.
     */
    public static Tag parseTag(String tag) throws ParseException {
        requireNonNull(tag);
        String trimmedTag = tag.trim();
        if (!Tag.isValidTagName(trimmedTag)) {
            throw new ParseException(Tag.MESSAGE_CONSTRAINTS);
        }
        return new Tag(trimmedTag);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>}.
     */
    public static Set<Tag> parseTags(Collection<String> tags) throws ParseException {
        requireNonNull(tags);
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(parseTag(tagName));
        }
        return tagSet;
    }
}
