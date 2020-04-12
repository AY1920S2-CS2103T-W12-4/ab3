package cookbuddy.logic.commands;

import static cookbuddy.logic.commands.CommandTestUtil.assertCommandFailure;
import static cookbuddy.logic.commands.CommandTestUtil.assertCommandSuccess;
import static cookbuddy.testutil.TypicalIndexes.INDEX_FIRST_RECIPE;
import static cookbuddy.testutil.TypicalIndexes.INDEX_SECOND_RECIPE;
import static cookbuddy.testutil.TypicalRecipes.getTypicalRecipeBook;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import cookbuddy.commons.core.Messages;
import cookbuddy.commons.core.index.Index;
import cookbuddy.model.Model;
import cookbuddy.model.ModelManager;
import cookbuddy.model.UserPrefs;
import cookbuddy.model.recipe.Recipe;
import cookbuddy.model.recipe.attribute.Time;



/**
 * Contains integration tests (interaction with the Model) and unit tests for ListCommand.
 */
public class TimeCommandTest {

    private Model model = new ModelManager(getTypicalRecipeBook(), new UserPrefs());

    @Test
    public void equalityCheck() {
        Index targetIndex = INDEX_FIRST_RECIPE;
        Time targetTime = new Time(20, 20, 20);
        TimeCommand timeCommand = new TimeCommand(targetIndex, targetTime);

        assertEquals(timeCommand, new TimeCommand(targetIndex, targetTime));

    }

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Recipe recipeToSet = model.getFilteredRecipeList().get(INDEX_SECOND_RECIPE.getZeroBased());
        TimeCommand timeCommand = new TimeCommand(INDEX_SECOND_RECIPE, new Time(20, 20, 20));
        Time timeToSet = new Time(20, 20, 20);

        String expectedMessage = String.format(TimeCommand.MESSAGE_TIME_RECIPE_SUCCESS,
                recipeToSet.getName(), timeToSet);

        ModelManager expectedModel = new ModelManager(model.getRecipeBook(), new UserPrefs());
        expectedModel.setTime(recipeToSet, new Time(20, 20, 20));

        assertCommandSuccess(timeCommand, model, expectedMessage, expectedModel);
    }


    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredRecipeList().size() + 1);
        TimeCommand timeCommand = new TimeCommand(outOfBoundIndex, new Time(20, 20 , 20));

        assertCommandFailure(timeCommand, model, Messages.MESSAGE_INVALID_RECIPE_DISPLAYED_INDEX);
    }

}
