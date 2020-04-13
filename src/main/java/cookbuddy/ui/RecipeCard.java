package cookbuddy.ui;

import java.util.Comparator;

import cookbuddy.model.recipe.Recipe;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * An UI component that displays information of a {@code Recipe}.
 */
public class RecipeCard extends UiPart<Region> {

    private static final String FXML = "RecipeListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on RecipeBook level 4</a>
     */

    public final Recipe recipe;

    @FXML
    private VBox cardPane;
    @FXML
    private HBox title;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label ingredients;
    @FXML
    private Label instructions;
    @FXML
    private Label calorie;
    @FXML
    private Label fav;
    @FXML
    private Label done;
    @FXML
    private Label timing;
    @FXML
    private Label serving;
    @FXML
    private Label rating;
    @FXML
    private Label diff;
    @FXML
    private FlowPane tags;
    public RecipeCard(Recipe recipe, int displayedIndex) {
        super(FXML);
        this.recipe = recipe;

        this.cardPane.setStyle("-fx-background-color: transparent;");
        this.title.setStyle("-fx-background-color: transparent;");
        this.tags.setStyle("-fx-background-color: transparent;");

        id.setText(displayedIndex + ". ");
        name.setText(recipe.getName().toString());
        calorie.setText("Calorie: " + recipe.getCalorie().toString() + " kcal");
        fav.setText(recipe.getFavStatus().toString());
        done.setText("Attempted: " + recipe.getDoneStatus().toString());
        timing.setText("\uD83D\uDD52: " + recipe.getPrepTime().toString());
        serving.setText(recipe.getServing().toString());
        rating.setText(recipe.getRating().toString());
        diff.setText("Difficulty: " + recipe.getDifficulty().toString());
        recipe.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof RecipeCard)) {
            return false;
        }

        // state check
        RecipeCard card = (RecipeCard) other;
        return id.getText().equals(card.id.getText())
                && recipe.equals(card.recipe);
    }
}
