package cookbuddy.logic.commands;

import static java.lang.Math.random;
import static java.util.Objects.requireNonNull;

import java.util.List;

import cookbuddy.commons.core.Messages;
import cookbuddy.commons.core.index.Index;
import cookbuddy.logic.commands.exceptions.CommandException;
import cookbuddy.model.Model;
import cookbuddy.model.recipe.Recipe;
import cookbuddy.ui.UiManager;


/**
 * Displays a random recipe from the recipe book to the user.
 */
public class RandomCommand extends Command {

    public static final String COMMAND_WORD = "random";

    public static final String MESSAGE_USAGE = COMMAND_WORD
        + ": Displays a random recipe \n"
        + "Example: " + COMMAND_WORD;


    public static final String MESSAGE_VIEW_RECIPE_SUCCESS = "Viewing Recipe: %1$s";

    private static int maxVal = 1;
    private final Index targetIndex;

    public RandomCommand() {
        double randomVal = random();
        randomVal *= maxVal;
        int randomNum = (int) randomVal;
        Index randomIndex = Index.fromZeroBased(randomNum);
        this.targetIndex = randomIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Recipe> lastShownList = model.getFilteredRecipeList();
        maxVal = (int) model.count();

        if (maxVal == 0) {
            throw new CommandException(Messages.MESSAGE_EMPTY_RECIPE_BOOK);
        }

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_RECIPE_DISPLAYED_INDEX);
        }

        Recipe recipeToView = lastShownList.get(targetIndex.getZeroBased());
        UiManager.changeRecipe(recipeToView);
        return new CommandResult(String.format(MESSAGE_VIEW_RECIPE_SUCCESS, recipeToView.getName()));
    }

    public Index getTargetIndex() {
        return targetIndex;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
            || (other instanceof RandomCommand // instanceof handles nulls
            && targetIndex.equals(((RandomCommand) other).targetIndex)); // state check
    }
}
