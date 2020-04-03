package cookbuddy.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import cookbuddy.commons.core.Messages;
import cookbuddy.commons.core.index.Index;
import cookbuddy.logic.commands.exceptions.CommandException;
import cookbuddy.model.Model;
import cookbuddy.model.recipe.Recipe;
import cookbuddy.model.recipe.attribute.Time;

/**
 * Adds a time to a recipe, identified using it's displayed index from the recipe book.
 */
public class TimeCommand extends Command {

    public static final String COMMAND_WORD = "time";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Provides a time to the recipe identified by the index number used in the displayed recipe list.\n"
            + "Parameters: INDEX (must be a positive integer), TIME (must be a positive integer, in minutes)\n"
            + "Example: " + COMMAND_WORD + " 1" + " 60 ";

    public static final String MESSAGE_FAV_RECIPE_SUCCESS = "Time for Recipe: %1$s %s min";

    private final Index targetIndex;
    private final Time prepTime;

    public TimeCommand(Index targetIndex, Time prepTime) {
        this.targetIndex = targetIndex;
        this.prepTime = prepTime;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Recipe> lastShownList = model.getFilteredRecipeList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_RECIPE_DISPLAYED_INDEX);
        }

        Recipe recipeToSet = lastShownList.get(targetIndex.getZeroBased());
        model.setTime(recipeToSet, prepTime);
        return new CommandResult(String.format(MESSAGE_FAV_RECIPE_SUCCESS, recipeToSet.getName()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof TimeCommand // instanceof handles nulls
                && targetIndex.equals(((TimeCommand) other).targetIndex)); // state check
    }
}
