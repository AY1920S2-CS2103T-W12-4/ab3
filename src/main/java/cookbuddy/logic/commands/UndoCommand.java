package cookbuddy.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import cookbuddy.commons.core.Messages;
import cookbuddy.commons.core.index.Index;
import cookbuddy.logic.commands.exceptions.CommandException;
import cookbuddy.model.Model;
import cookbuddy.model.recipe.Recipe;

/**
 * Un-Marks a recipe as done, identified using it's displayed index from the recipe book.
 */
public class UndoCommand extends Command {

    public static final String COMMAND_WORD = "undo";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Un-Marks the recipe identified by the index number shown in the displayed recipe list as done.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_UNDONE_RECIPE_SUCCESS = "Un-Marked Recipe: %1$s";

    private final Index targetIndex;

    public UndoCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Recipe> lastShownList = model.getFilteredRecipeList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_RECIPE_DISPLAYED_INDEX);
        }

        Recipe recipeToDo = lastShownList.get(targetIndex.getZeroBased());
        model.unAttemptRecipe(recipeToDo);
        return new CommandResult(String.format(MESSAGE_UNDONE_RECIPE_SUCCESS, recipeToDo.getName()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UndoCommand // instanceof handles nulls
                && targetIndex.equals(((UndoCommand) other).targetIndex)); // state check
    }
}
