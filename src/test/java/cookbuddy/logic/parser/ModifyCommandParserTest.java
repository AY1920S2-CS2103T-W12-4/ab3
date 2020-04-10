package cookbuddy.logic.parser;

import static cookbuddy.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static cookbuddy.logic.commands.CommandTestUtil.BLANK_INSTRUCTIONS_DESC;
import static cookbuddy.logic.commands.CommandTestUtil.CALORIE_DESC_HAM_SANDWICH;
import static cookbuddy.logic.commands.CommandTestUtil.DIFFICULTY_DESC_EGGS_ON_TOAST;
import static cookbuddy.logic.commands.CommandTestUtil.INGREDIENTS_DESC_EGGS_ON_TOAST;
import static cookbuddy.logic.commands.CommandTestUtil.INGREDIENTS_DESC_HAM_SANDWICH;
import static cookbuddy.logic.commands.CommandTestUtil.INSTRUCTIONS_DESC_EGGS_ON_TOAST;
import static cookbuddy.logic.commands.CommandTestUtil.INSTRUCTIONS_DESC_HAM_SANDWICH;
import static cookbuddy.logic.commands.CommandTestUtil.INVALID_INGREDIENTS_DESC;
import static cookbuddy.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static cookbuddy.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static cookbuddy.logic.commands.CommandTestUtil.NAME_DESC_EGGS_ON_TOAST;
import static cookbuddy.logic.commands.CommandTestUtil.NAME_DESC_HAM_SANDWICH;
import static cookbuddy.logic.commands.CommandTestUtil.PHOTOGRAPH_DESC_EGGS_ON_TOAST;
import static cookbuddy.logic.commands.CommandTestUtil.RATING_DESC_HAM_SANDWICH;
import static cookbuddy.logic.commands.CommandTestUtil.SERVING_DESC_HAM_SANDWICH;
import static cookbuddy.logic.commands.CommandTestUtil.TAG_DESC_BREAKFAST;
import static cookbuddy.logic.commands.CommandTestUtil.TAG_DESC_LUNCH;
import static cookbuddy.logic.commands.CommandTestUtil.VALID_CALORIE_HAM_SANDWICH;
import static cookbuddy.logic.commands.CommandTestUtil.VALID_DIFFICULTY_EGGS_ON_TOAST;
import static cookbuddy.logic.commands.CommandTestUtil.VALID_INGREDIENTS_EGGS_ON_TOAST;
import static cookbuddy.logic.commands.CommandTestUtil.VALID_INGREDIENTS_HAM_SANDWICH;
import static cookbuddy.logic.commands.CommandTestUtil.VALID_INSTRUCTIONS_EGGS_ON_TOAST;
import static cookbuddy.logic.commands.CommandTestUtil.VALID_INSTRUCTIONS_HAM_SANDWICH;
import static cookbuddy.logic.commands.CommandTestUtil.VALID_NAME_EGGS_ON_TOAST;
import static cookbuddy.logic.commands.CommandTestUtil.VALID_NAME_HAM_SANDWICH;
import static cookbuddy.logic.commands.CommandTestUtil.VALID_PHOTOGRAPH_EGGS_ON_TOAST;
import static cookbuddy.logic.commands.CommandTestUtil.VALID_RATING_HAM_SANDWICH;
import static cookbuddy.logic.commands.CommandTestUtil.VALID_SERVING_HAM_SANDWICH;
import static cookbuddy.logic.commands.CommandTestUtil.VALID_TAG_BREAKFAST;
import static cookbuddy.logic.commands.CommandTestUtil.VALID_TAG_LUNCH;
import static cookbuddy.logic.parser.CliSyntax.PREFIX_TAG;
import static cookbuddy.logic.parser.CommandParserTestUtil.assertParseFailure;
import static cookbuddy.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static cookbuddy.testutil.TypicalIndexes.INDEX_FIRST_RECIPE;
import static cookbuddy.testutil.TypicalIndexes.INDEX_SECOND_RECIPE;
import static cookbuddy.testutil.TypicalIndexes.INDEX_THIRD_RECIPE;

import org.junit.jupiter.api.Test;

import cookbuddy.commons.core.index.Index;
import cookbuddy.logic.commands.ModifyCommand;
import cookbuddy.logic.commands.ModifyCommand.EditRecipeDescriptor;
import cookbuddy.model.recipe.attribute.Ingredient;
import cookbuddy.model.recipe.attribute.Name;
import cookbuddy.model.recipe.attribute.Tag;
import cookbuddy.testutil.EditRecipeDescriptorBuilder;


public class ModifyCommandParserTest {


    private static final String noIndex = "No index has been provided for the command!\n";
    private static final String invalidIndex = "Index must be a non-zero unsigned integer.\n";
    private static final String helpMessage = "For a command summary, type \"help modify\"";
    private static final String invalidFormat = "Invalid command format! \n";
    private static final String noInstructions = "Recipes need to have instructions; please enter some instructions.";
    private static final String TAG_EMPTY = " " + PREFIX_TAG;

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, ModifyCommand.MESSAGE_USAGE);

    private ModifyCommandParser parser = new ModifyCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no index specified
        assertParseFailure(parser, VALID_NAME_EGGS_ON_TOAST, invalidFormat + invalidIndex + helpMessage);

        // no field specified
        assertParseFailure(parser, "1", ModifyCommand.MESSAGE_NOT_EDITED);

        // no index and no field specified
        assertParseFailure(parser, "", invalidFormat + noIndex + helpMessage);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // negative index
        assertParseFailure(parser, "-5" + NAME_DESC_EGGS_ON_TOAST, invalidFormat + invalidIndex + helpMessage);

        // zero index
        assertParseFailure(parser, "0" + NAME_DESC_EGGS_ON_TOAST, invalidFormat + invalidIndex + helpMessage);

        // invalid arguments being parsed as preamble
        assertParseFailure(parser, "1 some random string", invalidFormat + invalidIndex + helpMessage);

        // invalid prefix being parsed as preamble
        assertParseFailure(parser, "1 i/ string", invalidFormat + invalidIndex + helpMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        assertParseFailure(parser, "1" + INVALID_NAME_DESC, Name.MESSAGE_CONSTRAINTS); // invalid name
        assertParseFailure(parser, "1" + INVALID_INGREDIENTS_DESC, Ingredient.MESSAGE_CONSTRAINTS); //invalid ingredient
        assertParseFailure(parser, "1" + BLANK_INSTRUCTIONS_DESC, noInstructions); // invalid instr
        assertParseFailure(parser, "1" + INVALID_TAG_DESC, Tag.MESSAGE_CONSTRAINTS); // invalid tag

        // invalid ingredient followed by valid instruction
        assertParseFailure(parser, "1" + INVALID_INGREDIENTS_DESC + INSTRUCTIONS_DESC_EGGS_ON_TOAST,
                Ingredient.MESSAGE_CONSTRAINTS);

        // valid ingredient followed by invalid instruction. The test case for invalid instruction followed by valid
        // ingredient is tested at {@code parse_invalidValueFollowedByValidValue_success()}
        assertParseFailure(parser, "1" + INGREDIENTS_DESC_HAM_SANDWICH + INVALID_INGREDIENTS_DESC,
                Ingredient.MESSAGE_CONSTRAINTS);

        // while parsing {@code PREFIX_TAG} alone will reset the tags of the {@code Recipe} being edited,
        // parsing it together with a valid tag results in error

        //ToDo: find a way to test tags.
        //assertParseFailure(parser, "1" + TAG_DESC_BREAKFAST + TAG_DESC_DINNER + TAG_EMPTY, Tag.MESSAGE_CONSTRAINTS);
        //assertParseFailure(parser, "1" + TAG_DESC_LUNCH + TAG_EMPTY + TAG_DESC_DINNER, Tag.MESSAGE_CONSTRAINTS);
        //assertParseFailure(parser, "1" + TAG_EMPTY + TAG_DESC_BREAKFAST + TAG_DESC_LUNCH, Tag.MESSAGE_CONSTRAINTS);

        // multiple invalid values, but only the first invalid value is captured
        assertParseFailure(parser, "1" + INVALID_NAME_DESC + INVALID_INGREDIENTS_DESC
                        + VALID_INSTRUCTIONS_EGGS_ON_TOAST, Name.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_allFieldsSpecified_success() {
        Index targetIndex = INDEX_SECOND_RECIPE;
        String userInput = targetIndex.getOneBased() + INGREDIENTS_DESC_EGGS_ON_TOAST + TAG_DESC_LUNCH
                + INSTRUCTIONS_DESC_HAM_SANDWICH + NAME_DESC_HAM_SANDWICH + CALORIE_DESC_HAM_SANDWICH
                + RATING_DESC_HAM_SANDWICH + DIFFICULTY_DESC_EGGS_ON_TOAST + SERVING_DESC_HAM_SANDWICH
                + PHOTOGRAPH_DESC_EGGS_ON_TOAST;

        ModifyCommand.EditRecipeDescriptor descriptor =
                new EditRecipeDescriptorBuilder().withName(VALID_NAME_HAM_SANDWICH)
                        .withInstructions(VALID_INSTRUCTIONS_HAM_SANDWICH)
                        .withIngredients(VALID_INGREDIENTS_EGGS_ON_TOAST)
                        .withDifficulty(VALID_DIFFICULTY_EGGS_ON_TOAST)
                        .withServing(VALID_SERVING_HAM_SANDWICH)
                        .withRating(VALID_RATING_HAM_SANDWICH)
                        .withCalorie(VALID_CALORIE_HAM_SANDWICH)
                        .withPhotograph(VALID_PHOTOGRAPH_EGGS_ON_TOAST)
                        .withTags(VALID_TAG_LUNCH).build();
        ModifyCommand expectedCommand = new ModifyCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_someFieldsSpecified_success() {
        Index targetIndex = INDEX_FIRST_RECIPE;
        String userInput =
                targetIndex.getOneBased() + INGREDIENTS_DESC_EGGS_ON_TOAST + INSTRUCTIONS_DESC_HAM_SANDWICH
                        + TAG_DESC_LUNCH;

        ModifyCommand.EditRecipeDescriptor descriptor =
                new EditRecipeDescriptorBuilder().withIngredients(VALID_INGREDIENTS_EGGS_ON_TOAST)
                .withInstructions(VALID_INSTRUCTIONS_HAM_SANDWICH).withTags(VALID_TAG_LUNCH).build();
        ModifyCommand expectedCommand = new ModifyCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_oneFieldSpecified_success() {
        // name
        Index targetIndex = INDEX_THIRD_RECIPE;
        String userInput = targetIndex.getOneBased() + NAME_DESC_HAM_SANDWICH;
        ModifyCommand.EditRecipeDescriptor descriptor =
                new EditRecipeDescriptorBuilder().withName(VALID_NAME_HAM_SANDWICH).build();
        ModifyCommand expectedCommand = new ModifyCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // ingredients
        userInput = targetIndex.getOneBased() + INGREDIENTS_DESC_HAM_SANDWICH;
        descriptor = new EditRecipeDescriptorBuilder().withIngredients(VALID_INGREDIENTS_HAM_SANDWICH).build();
        expectedCommand = new ModifyCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // instructions
        userInput = targetIndex.getOneBased() + INSTRUCTIONS_DESC_HAM_SANDWICH;
        descriptor = new EditRecipeDescriptorBuilder().withInstructions(VALID_INSTRUCTIONS_HAM_SANDWICH).build();
        expectedCommand = new ModifyCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // serving
        userInput = targetIndex.getOneBased() + SERVING_DESC_HAM_SANDWICH;
        descriptor = new EditRecipeDescriptorBuilder().withServing(VALID_SERVING_HAM_SANDWICH).build();
        expectedCommand = new ModifyCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // rating
        userInput = targetIndex.getOneBased() + RATING_DESC_HAM_SANDWICH;
        descriptor = new EditRecipeDescriptorBuilder().withRating(VALID_RATING_HAM_SANDWICH).build();
        expectedCommand = new ModifyCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // calorie
        userInput = targetIndex.getOneBased() + CALORIE_DESC_HAM_SANDWICH;
        descriptor = new EditRecipeDescriptorBuilder().withCalorie(VALID_CALORIE_HAM_SANDWICH).build();
        expectedCommand = new ModifyCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // tags
        userInput = targetIndex.getOneBased() + TAG_DESC_BREAKFAST;
        descriptor = new EditRecipeDescriptorBuilder().withTags(VALID_TAG_BREAKFAST).build();
        expectedCommand = new ModifyCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_multipleRepeatedFields_acceptsLast() {
        Index targetIndex = INDEX_FIRST_RECIPE;
        String userInput = targetIndex.getOneBased() + INGREDIENTS_DESC_HAM_SANDWICH + INSTRUCTIONS_DESC_HAM_SANDWICH
                + TAG_DESC_BREAKFAST + INGREDIENTS_DESC_HAM_SANDWICH + INSTRUCTIONS_DESC_HAM_SANDWICH
                + INGREDIENTS_DESC_EGGS_ON_TOAST + INSTRUCTIONS_DESC_EGGS_ON_TOAST + TAG_DESC_LUNCH;

        ModifyCommand.EditRecipeDescriptor descriptor =
                new EditRecipeDescriptorBuilder().withIngredients(VALID_INGREDIENTS_EGGS_ON_TOAST)
                .withInstructions(VALID_INSTRUCTIONS_EGGS_ON_TOAST).withTags(VALID_TAG_LUNCH)
                .build();
        ModifyCommand expectedCommand = new ModifyCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_invalidValueFollowedByValidValue_success() {
        // no other valid values specified
        Index targetIndex = INDEX_FIRST_RECIPE;
        String userInput = targetIndex.getOneBased() + INVALID_INGREDIENTS_DESC + INGREDIENTS_DESC_EGGS_ON_TOAST;
        EditRecipeDescriptor descriptor = new EditRecipeDescriptorBuilder()
                .withIngredients(VALID_INGREDIENTS_EGGS_ON_TOAST).build();
        ModifyCommand expectedCommand = new ModifyCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // other valid values specified
        userInput = targetIndex.getOneBased() + INSTRUCTIONS_DESC_EGGS_ON_TOAST + INVALID_INGREDIENTS_DESC
                + INGREDIENTS_DESC_EGGS_ON_TOAST;
        descriptor =
                new EditRecipeDescriptorBuilder().withIngredients(VALID_INGREDIENTS_EGGS_ON_TOAST)
                        .withInstructions(VALID_INSTRUCTIONS_EGGS_ON_TOAST).build();
        expectedCommand = new ModifyCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_resetTags_success() {
        Index targetIndex = INDEX_THIRD_RECIPE;
        String userInput = targetIndex.getOneBased() + TAG_EMPTY;

        ModifyCommand.EditRecipeDescriptor descriptor = new EditRecipeDescriptorBuilder().withTags().build();
        ModifyCommand expectedCommand = new ModifyCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }
}
