package cookbuddy.logic.parser;

import static java.util.Objects.requireNonNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import cookbuddy.commons.core.index.Index;
import cookbuddy.commons.util.StringUtil;
import cookbuddy.logic.parser.exceptions.ParseException;
import cookbuddy.model.recipe.attribute.Calorie;
import cookbuddy.model.recipe.attribute.Difficulty;
import cookbuddy.model.recipe.attribute.Image;
import cookbuddy.model.recipe.attribute.Ingredient;
import cookbuddy.model.recipe.attribute.IngredientList;
import cookbuddy.model.recipe.attribute.Instruction;
import cookbuddy.model.recipe.attribute.InstructionList;
import cookbuddy.model.recipe.attribute.Name;
import cookbuddy.model.recipe.attribute.Rating;
import cookbuddy.model.recipe.attribute.Serving;
import cookbuddy.model.recipe.attribute.Tag;
import cookbuddy.model.recipe.attribute.Time;

/**
 * Contains utility methods used for parsing strings in the various *Parser
 * classes.
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "Index is not a non-zero unsigned integer.";
    public static final String MESSAGE_INVALID_FILEPATH = "Image not found or invalid image path given";

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
            throw new ParseException(Ingredient.MESSAGE_CONSTRAINTS);
        }

        try {
            List<Ingredient> ingredientList = Stream.of(ingredientString.trim().split(";"))
                    .map(String::trim).map(Ingredient::new).collect(Collectors.toList());

            if (ingredientList.isEmpty()) {
                throw new ParseException(Ingredient.MESSAGE_CONSTRAINTS);
            }
            return new IngredientList(ingredientList);
        } catch (IllegalArgumentException e) {
            throw new ParseException("No ingredient name has been provided for one or more ingredients!");
        } catch (IndexOutOfBoundsException e) {
            throw new ParseException("No quantity has been provided for one or more ingredients!");
        }
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
            throw new ParseException("Recipes need to have instructions; please enter some instructions.");
        }

        return new InstructionList(Stream.of(instructionString.trim().split(";")).map(String::trim)
            .map(Instruction::new).collect(Collectors.toList()));
    }

    /**
     * Parses a {@code String input} into an {@code Image}.
     */
    public static Image parseFilePath(String input) throws ParseException {
        requireNonNull(input);
        String trimmedPath = input.trim();
        if (!Image.isValidImageFilePath(trimmedPath)) {
            throw new ParseException(MESSAGE_INVALID_FILEPATH);
        }
        return new Image(trimmedPath);
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
        int rating;
        try {
            rating = Integer.parseInt(ratingString.trim());
        } catch (NumberFormatException e) {
            throw new ParseException("Input provided is not a valid integer!");
        }
        if (!Rating.isValidRating(rating)) {
            throw new ParseException(Rating.MESSAGE_CONSTRAINTS);
        }
        return new Rating(rating);
    }

    /**
     * Parses a {@code String difficultyString} into a {@code Difficulty}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code difficultyString} is invalid.
     */
    public static Difficulty parseDifficulty(String difficultyString) throws ParseException {
        requireNonNull(difficultyString);
        int difficulty = Integer.parseInt(difficultyString.trim());
        if (!Difficulty.isValidDifficulty(difficulty)) {
            throw new ParseException(Difficulty.MESSAGE_CONSTRAINTS);
        }
        return new Difficulty(difficulty);
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
     * Parses a {@code String timeString} into a {@code Time}
     *
     * @param timeString the string to be parsed
     * @return the Time of the recipe
     * @throws ParseException if the given {@timeString} is invalid.
     */
    public static Time parseTime(String timeString) throws ParseException {
        int hour;
        int min = 0;
        int sec = 0;
        requireNonNull(timeString);
        String trimmedTime = timeString.trim();
        String[] timeArray = trimmedTime.split(":");
        hour = Integer.parseInt(timeArray[0]);
        if (timeArray.length > 1) {
            min = Integer.parseInt(timeArray[1]);
            if (timeArray.length > 2) {
                sec = Integer.parseInt(timeArray[2]);
            }
        }
        if (!Time.isValidHour(hour)) {
            throw new ParseException(Time.MESSAGE_CONSTRAINTS_HOUR);
        }

        if (!Time.isValidMin(min)) {
            throw new ParseException(Time.MESSAGE_CONSTRAINTS_MIN);
        }

        if (!Time.isValidSec(sec)) {
            throw new ParseException(Time.MESSAGE_CONSTRAINTS_SEC);
        }

        return new Time(hour, min, sec);

    }

    /**
     * Parses {@code Optional<String> tags} into a {@code Set<Tag>}.
     */
    public static Set<Tag> parseTags(Optional<String> tags) throws ParseException {
        final Set<Tag> tagSet = new HashSet<>();
        if (tags.isPresent() && !tags.get().equals("")) {
            String tagsString = tags.get();
            List<String> tagList = Arrays.asList(tagsString.split(",")).stream().map(String::trim).collect(
                Collectors.toList());
            for (String tagName : tagList) {
                tagSet.add(parseTag(tagName));
            }
        }

        return tagSet;
    }
}
