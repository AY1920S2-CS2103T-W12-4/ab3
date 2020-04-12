package cookbuddy.logic.commands;

import static cookbuddy.logic.commands.CommandTestUtil.assertCommandFailure;
import static cookbuddy.logic.commands.CommandTestUtil.assertCommandSuccess;
import static cookbuddy.testutil.TypicalIndexes.INDEX_FIRST_RECIPE;
import static cookbuddy.testutil.TypicalRecipes.getTypicalRecipeBook;

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
public class UnFavCommandTest {

    private Model model = new ModelManager(getTypicalRecipeBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Recipe recipeToUnFav = model.getFilteredRecipeList().get(INDEX_FIRST_RECIPE.getZeroBased());
        UnFavCommand unfavCommand = new UnFavCommand(INDEX_FIRST_RECIPE);

        String expectedMessage = String.format(UnFavCommand.MESSAGE_UNFAV_RECIPE_SUCCESS, recipeToUnFav.getName());

        ModelManager expectedModel = new ModelManager(model.getRecipeBook(), new UserPrefs());
        expectedModel.unFavRecipe(recipeToUnFav);

        assertCommandSuccess(unfavCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredRecipeList().size() + 1);
        UnFavCommand unfavCommand = new UnFavCommand(outOfBoundIndex);

        assertCommandFailure(unfavCommand, model, Messages.MESSAGE_INVALID_RECIPE_DISPLAYED_INDEX);
    }
}
