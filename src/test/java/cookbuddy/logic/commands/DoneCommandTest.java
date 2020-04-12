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
public class DoneCommandTest {

    private Model model = new ModelManager(getTypicalRecipeBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Recipe recipeToDo = model.getFilteredRecipeList().get(INDEX_FIRST_RECIPE.getZeroBased());
        DoneCommand doneCommand = new DoneCommand(INDEX_FIRST_RECIPE);

        String expectedMessage = String.format(DoneCommand.MESSAGE_DONE_RECIPE_SUCCESS, recipeToDo.getName());

        ModelManager expectedModel = new ModelManager(model.getRecipeBook(), new UserPrefs());
        expectedModel.attemptRecipe(recipeToDo);

        assertCommandSuccess(doneCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredRecipeList().size() + 1);
        DoneCommand doneCommand = new DoneCommand(outOfBoundIndex);

        assertCommandFailure(doneCommand, model, Messages.MESSAGE_INVALID_RECIPE_DISPLAYED_INDEX);
    }

    @Test
    public void testEquality() {
        DoneCommand doneCommand = new DoneCommand(INDEX_FIRST_RECIPE);
        assertEquals(doneCommand, new DoneCommand(INDEX_FIRST_RECIPE));
    }
}
