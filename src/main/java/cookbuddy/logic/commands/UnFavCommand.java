package cookbuddy.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import cookbuddy.commons.core.Messages;
import cookbuddy.commons.core.index.Index;
import cookbuddy.logic.commands.exceptions.CommandException;
import cookbuddy.model.Model;
import cookbuddy.model.recipe.Recipe;

/**
 * Un-favourites a recipe identified using it's displayed index from the recipe book.
 */
public class UnFavCommand extends Command {

    public static final String COMMAND_WORD = "unfav";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Un-favourites the recipe identified by the index number shown in the displayed recipe list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_UNFAV_RECIPE_SUCCESS = "Un-Favourited Recipe: %1$s";

    private final Index targetIndex;

    public UnFavCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Recipe> lastShownList = model.getFilteredRecipeList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_RECIPE_DISPLAYED_INDEX);
        }

        Recipe recipeToUnFav = lastShownList.get(targetIndex.getZeroBased());
        model.unFavRecipe(recipeToUnFav);
        return new CommandResult(String.format(MESSAGE_UNFAV_RECIPE_SUCCESS, recipeToUnFav.getName()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UnFavCommand // instanceof handles nulls
                && targetIndex.equals(((UnFavCommand) other).targetIndex)); // state check
    }
}
