package cookbuddy.logic.commands;

import static cookbuddy.model.Model.PREDICATE_SHOW_ALL_RECIPES;
import static java.util.Objects.requireNonNull;

import cookbuddy.model.Model;

/**
 * Lists all recipes in the recipe book to the user.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Displays all the recipes in CookBuddy.\n"
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = "Listed all available recipes.";


    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredRecipeList(PREDICATE_SHOW_ALL_RECIPES);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
