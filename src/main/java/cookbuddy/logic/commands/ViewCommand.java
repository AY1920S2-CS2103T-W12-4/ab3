package cookbuddy.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import cookbuddy.commons.core.Messages;
import cookbuddy.commons.core.index.Index;
import cookbuddy.logic.commands.exceptions.CommandException;
import cookbuddy.model.Model;
import cookbuddy.model.recipe.Recipe;
import cookbuddy.ui.UiManager;


/**
 * Lists all recipes in the recipe book to the user.
 */
public class ViewCommand extends Command {

    public static final String COMMAND_WORD = "view";

    public static final String MESSAGE_USAGE = COMMAND_WORD
        + ": Views the recipe identified by the index number shown in the displayed recipe list.\n"
        + "Parameters: INDEX (must be a positive integer)\n"
        + "Example: " + COMMAND_WORD + " 1";


    public static final String MESSAGE_VIEW_RECIPE_SUCCESS = "Viewing Recipe: %1$s";

    private final Index targetIndex;

    public ViewCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Recipe> lastShownList = model.getFilteredRecipeList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_RECIPE_DISPLAYED_INDEX);
        }

        Recipe recipeToView = lastShownList.get(targetIndex.getZeroBased());
        UiManager.changeRecipe(recipeToView);
        return new CommandResult(String.format(MESSAGE_VIEW_RECIPE_SUCCESS, recipeToView.getName()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
            || (other instanceof ViewCommand // instanceof handles nulls
            && targetIndex.equals(((ViewCommand) other).targetIndex)); // state check
    }
}
