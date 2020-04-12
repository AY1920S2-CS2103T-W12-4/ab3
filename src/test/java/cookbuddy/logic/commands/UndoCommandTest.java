package cookbuddy.logic.commands;

import static cookbuddy.logic.commands.CommandTestUtil.assertCommandFailure;
import static cookbuddy.logic.commands.CommandTestUtil.assertCommandSuccess;
import static cookbuddy.testutil.TypicalIndexes.INDEX_FIRST_RECIPE;
import static cookbuddy.testutil.TypicalRecipes.getTypicalRecipeBook;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import cookbuddy.commons.core.Messages;
import cookbuddy.commons.core.index.Index;
import cookbuddy.model.Model;
import cookbuddy.model.ModelManager;
import cookbuddy.model.UserPrefs;
import cookbuddy.model.recipe.Recipe;


/**
 * Contains integration tests (interaction with the Model) and unit tests for ListCommand.
 */
public class UndoCommandTest {

    private Model model = new ModelManager(getTypicalRecipeBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Recipe recipeToUndo = model.getFilteredRecipeList().get(INDEX_FIRST_RECIPE.getZeroBased());
        UndoCommand undoCommand = new UndoCommand(INDEX_FIRST_RECIPE);

        String expectedMessage = String.format(UndoCommand.MESSAGE_UNDONE_RECIPE_SUCCESS, recipeToUndo.getName());

        ModelManager expectedModel = new ModelManager(model.getRecipeBook(), new UserPrefs());
        expectedModel.unAttemptRecipe(recipeToUndo);

        assertCommandSuccess(undoCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredRecipeList().size() + 1);
        UndoCommand undoneCommand = new UndoCommand(outOfBoundIndex);

        assertCommandFailure(undoneCommand, model, Messages.MESSAGE_INVALID_RECIPE_DISPLAYED_INDEX);
    }

    @Test
    public void testEquality() {
        UndoCommand undoCommand = new UndoCommand(INDEX_FIRST_RECIPE);
        assertEquals(undoCommand, new UndoCommand(INDEX_FIRST_RECIPE));
    }
}
