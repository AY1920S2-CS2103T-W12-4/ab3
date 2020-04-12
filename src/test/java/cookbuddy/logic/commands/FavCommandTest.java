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
public class FavCommandTest {

    private Model model = new ModelManager(getTypicalRecipeBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Recipe recipeToFav = model.getFilteredRecipeList().get(INDEX_FIRST_RECIPE.getZeroBased());
        FavCommand favCommand = new FavCommand(INDEX_FIRST_RECIPE);

        String expectedMessage = String.format(FavCommand.MESSAGE_FAV_RECIPE_SUCCESS, recipeToFav.getName());

        ModelManager expectedModel = new ModelManager(model.getRecipeBook(), new UserPrefs());
        expectedModel.favRecipe(recipeToFav);

        assertCommandSuccess(favCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredRecipeList().size() + 1);
        FavCommand favCommand = new FavCommand(outOfBoundIndex);

        assertCommandFailure(favCommand, model, Messages.MESSAGE_INVALID_RECIPE_DISPLAYED_INDEX);
    }
}
