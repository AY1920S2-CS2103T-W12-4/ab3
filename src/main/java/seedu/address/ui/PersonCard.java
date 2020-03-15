package seedu.address.ui;

import java.util.Comparator;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.recipe.Recipe;

/**
 * An UI component that displays information of a {@code Recipe}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on RecipeBook level 4</a>
     */

    public final Recipe recipe;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label name1;
    @FXML
    private Label id;
    @FXML
    private Label id1;
    @FXML
    private Label ingredients;
    @FXML
    private Label ingredients1;
    @FXML
    private Label instructions;
    @FXML
    private Label instructions1;
    @FXML
    private FlowPane tags;
    @FXML
    private FlowPane tags1;

    public PersonCard(Recipe recipe, int displayedIndex) {
        super(FXML);
        this.recipe = recipe;

        id.setText(displayedIndex + ". ");
        name.setText(recipe.getName().name);
        ingredients.setText(recipe.getIngredients().toString());
        instructions.setText(recipe.getInstructions().toString());
        recipe.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));
        id1.setText(displayedIndex + ". ");
        name1.setText(recipe.getName().name);
        ingredients1.setText(recipe.getIngredients().toString());
        instructions1.setText(recipe.getInstructions().toString());
        recipe.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags1.getChildren().add(new Label(tag.tagName)));
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof PersonCard)) {
            return false;
        }

        // state check
        PersonCard card = (PersonCard) other;
        return id.getText().equals(card.id.getText())
                && recipe.equals(card.recipe);
    }
}
