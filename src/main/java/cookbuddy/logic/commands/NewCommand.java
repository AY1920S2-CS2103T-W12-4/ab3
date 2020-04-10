package cookbuddy.logic.commands;

import static cookbuddy.logic.parser.CliSyntax.PREFIX_CALORIE;
import static cookbuddy.logic.parser.CliSyntax.PREFIX_DIFFICULTY;
import static cookbuddy.logic.parser.CliSyntax.PREFIX_IMAGEFILEPATH;
import static cookbuddy.logic.parser.CliSyntax.PREFIX_INGREDIENTS;
import static cookbuddy.logic.parser.CliSyntax.PREFIX_INSTRUCTIONS;
import static cookbuddy.logic.parser.CliSyntax.PREFIX_NAME;
import static cookbuddy.logic.parser.CliSyntax.PREFIX_RATING;
import static cookbuddy.logic.parser.CliSyntax.PREFIX_SERVING;
import static cookbuddy.logic.parser.CliSyntax.PREFIX_TAG;
import static java.util.Objects.requireNonNull;

import cookbuddy.logic.commands.exceptions.CommandException;
import cookbuddy.model.Model;
import cookbuddy.model.recipe.Recipe;

/**
 * Adds a recipe to the recipe book.
 */
public class NewCommand extends Command {

    public static final String COMMAND_WORD = "new";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a recipe to the recipe book.\n"
        + "Parameters: "
        + PREFIX_NAME + "NAME "
        + PREFIX_INGREDIENTS + "INGREDIENT 1, QUANTITY; INGREDIENT 2, QUANTITY... "
        + PREFIX_INSTRUCTIONS + "INSTRUCTION 1, INSTRUCTION 2... "
        + "[" + PREFIX_IMAGEFILEPATH + "PATH] "
        + "[" + PREFIX_CALORIE + "CALORIES] "
        + "[" + PREFIX_SERVING + "SERVING SIZE] "
        + "[" + PREFIX_RATING + "RATING] "
        + "[" + PREFIX_DIFFICULTY + "DIFFICULTY] "
        + "[" + PREFIX_TAG + "TAG [TAG]...]\n"
        + "Example: " + COMMAND_WORD + " "
        + PREFIX_NAME + "Ham Sandwich "
        + PREFIX_INGREDIENTS + "bread, 2 slices; ham, 1 slice "
        + PREFIX_INSTRUCTIONS + "put ham between bread; eat sandwich "
        + PREFIX_IMAGEFILEPATH + "absolute/path/to/image "
        + PREFIX_CALORIE + "169 "
        + PREFIX_SERVING + "2 "
        + PREFIX_RATING + "4 "
        + PREFIX_DIFFICULTY + "2 "
        + PREFIX_TAG + "breakfast";

    public static final String MESSAGE_SUCCESS = "New recipe added: %1$s";
    public static final String MESSAGE_DUPLICATE_RECIPE = "This recipe already exists in the recipe book";

    private final Recipe toAdd;

    /**
     * Creates an NewCommand to add the specified {@code Recipe}
     */
    public NewCommand(Recipe recipe) {
        requireNonNull(recipe);
        toAdd = recipe;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasRecipe(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_RECIPE);
        }

        model.addRecipe(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
            || (other instanceof NewCommand // instanceof handles nulls
            && toAdd.equals(((NewCommand) other).toAdd));
    }
}
