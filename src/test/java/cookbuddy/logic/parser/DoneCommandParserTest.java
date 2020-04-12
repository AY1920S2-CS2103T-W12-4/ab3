package cookbuddy.logic.parser;

import static cookbuddy.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static cookbuddy.logic.parser.CommandParserTestUtil.assertParseFailure;
import static cookbuddy.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static cookbuddy.testutil.TypicalIndexes.INDEX_FIRST_RECIPE;
import static cookbuddy.testutil.TypicalRecipes.EGGS_ON_TOAST;
import static cookbuddy.testutil.TypicalRecipes.HAM_SANDWICH;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import cookbuddy.commons.core.index.Index;
import cookbuddy.logic.commands.DoneCommand;
import cookbuddy.logic.commands.ModifyCommand;
import cookbuddy.logic.commands.UndoCommand;
import cookbuddy.model.recipe.attribute.Done;


public class DoneCommandParserTest {


    private static final String noIndex = "No index has been provided for the command!\n";
    private static final String invalidIndex = "Index must be a non-zero unsigned integer.\n";
    private static final String helpMessage = "For a command summary, type \"help done\"";
    private static final String helpMessage_undo = "For a command summary, type \"help undo\"";
    private static final String invalidFormat = "Invalid command format! \n";

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, ModifyCommand.MESSAGE_USAGE);

    private DoneCommandParser parser = new DoneCommandParser();
    private UndoCommandParser undoParser = new UndoCommandParser();


    @Test
    public void starting_recipe_equality() {
        assertTrue(EGGS_ON_TOAST.getDoneStatus().equals(EGGS_ON_TOAST.getDoneStatus()));
    }

    @Test
    public void starting_recipe_status() {
        assertTrue(EGGS_ON_TOAST.getDoneStatus().equals(new Done(false)));
    }

    @Test
    public void done_recipe_success() {
        HAM_SANDWICH.attemptRecipe();
        assertTrue(HAM_SANDWICH.getDoneStatus().equals(new Done(true)));
    }

    @Test
    public void undo_recipe_success() {
        HAM_SANDWICH.unAttemptRecipe();
        assertTrue(HAM_SANDWICH.getDoneStatus().equals(new Done(false)));
    }

    @Test
    public void parse_done_invalidIndex() {
        assertParseFailure(parser, "abc", invalidFormat + invalidIndex + helpMessage);
    }

    @Test
    public void parse_undo_invalidIndex() {
        assertParseFailure(undoParser, "abc", invalidFormat + invalidIndex + helpMessage_undo);
    }

    @Test
    public void parse_done_noIndex() {
        assertParseFailure(parser, "", invalidFormat + noIndex + helpMessage);
    }

    @Test
    public void parse_undo_noIndex() {
        assertParseFailure(undoParser, "", invalidFormat + noIndex + helpMessage_undo);
    }

    @Test
    public void parse_done_success() {
        Index targetIndex = INDEX_FIRST_RECIPE;
        DoneCommand expectedCommand = new DoneCommand(targetIndex);
        assertParseSuccess(parser, "1", expectedCommand);
    }

    @Test
    public void parse_undo_success() {
        Index targetIndex = INDEX_FIRST_RECIPE;
        UndoCommand expectedCommand = new UndoCommand(targetIndex);
        assertParseSuccess(undoParser, "1", expectedCommand);
    }
}
