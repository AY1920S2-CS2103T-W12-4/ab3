package cookbuddy.logic.commands;

import static java.util.Objects.requireNonNull;

import cookbuddy.model.Model;
import cookbuddy.model.RecipeBook;
import cookbuddy.ui.UiManager;

/**
 * Clears the recipe book.
 */
public class ResetCommand extends Command {

    public static final String COMMAND_WORD = "reset";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Deletes all recipes from CookBuddy.\n"
            + "Example: " + COMMAND_WORD;
    public static final String MESSAGE_SUCCESS = "The recipe book has been cleared!";


    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.setRecipeBook(new RecipeBook());
        if (model.getFilteredRecipeList().size() > 0) {
            UiManager.removeRecipe();
        }

        return new CommandResult(MESSAGE_SUCCESS);
    }
}
