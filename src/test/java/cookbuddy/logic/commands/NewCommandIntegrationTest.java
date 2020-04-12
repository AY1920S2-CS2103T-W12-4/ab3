package cookbuddy.logic.commands;

import static cookbuddy.logic.commands.CommandTestUtil.assertCommandFailure;
import static cookbuddy.logic.commands.CommandTestUtil.assertCommandSuccess;
import static cookbuddy.testutil.TypicalRecipes.getTypicalRecipeBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import cookbuddy.model.Model;
import cookbuddy.model.ModelManager;
import cookbuddy.model.UserPrefs;
import cookbuddy.model.recipe.Recipe;
import cookbuddy.testutil.RecipeBuilder;


/**
 * Contains integration tests (interaction with the Model) for {@code NewCommand}.
 */
public class NewCommandIntegrationTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalRecipeBook(), new UserPrefs());
    }

    @Test
    public void execute_newRecipe_success() {
        Recipe validRecipe = new RecipeBuilder().build();

        Model expectedModel = new ModelManager(model.getRecipeBook(), new UserPrefs());
        expectedModel.addRecipe(validRecipe);

        assertCommandSuccess(new NewCommand(validRecipe), model,
                String.format(NewCommand.MESSAGE_SUCCESS, validRecipe), expectedModel);
    }

    @Test
    public void execute_duplicateRecipe_throwsCommandException() {
        Recipe recipeInList = model.getRecipeBook().getRecipeList().get(0);
        assertCommandFailure(new NewCommand(recipeInList), model, NewCommand.MESSAGE_DUPLICATE_RECIPE);
    }

}
