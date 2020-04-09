package cookbuddy.model.recipe.attribute;

import static cookbuddy.commons.util.AppUtil.checkArgument;
import static java.util.Objects.requireNonNull;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a Recipe's ingredient in its {@code IngredientList}. Guarantees:
 * immutable; is valid as declared in {@link #isValidName(String)}
 */
public class Ingredient {

    public static final String MESSAGE_CONSTRAINTS = "Each ingredient should be of the form: "
            + "'ing1, ing1qty'.\n"
            + "Each ingredient-quantity pair must be separated by ';'. Spaces are optional.\n"
            + "Example: 'ing/bread, 2 slices; ham, 3 slices'";

    public final String name;
    private String quantity;
    private Quantity quantity2;

    /**
     * Construct an Ingredient from {@code ingredientString}; the parameter is
     * required to be non-null, and must follow a pattern.
     *
     * @param ingredientString the {@link String} to be decoded
     */
    public Ingredient(String ingredientString) {
        requireNonNull(ingredientString);

        List<String> ingredientParts = Arrays.stream(ingredientString.split(",")).map(String::trim).collect(
                Collectors.toList());


        checkArgument(isValidName(ingredientParts.get(0)), MESSAGE_CONSTRAINTS);
        checkArgument(isValidName(ingredientParts.get(1)), MESSAGE_CONSTRAINTS);

        this.name = ingredientParts.get(0);
        this.quantity = ingredientParts.get(1);
        // new Quantity(ingredientParts.get(1));
    }

    /**
     * Returns {@code true} if {@code nameString} is <em>not</em> blank.
     *
     * @param nameString the {@link String} to be tested.
     */
    private Boolean isValidName(String nameString) {
        return !nameString.isBlank();
    }

    public String getQuantity() {
        return this.quantity;
    }

    public void setQuantity(String quantityString) {
        this.quantity = quantityString;
        // new Quantity(quantityString);
    }

    public String getName() {
        return this.name;
    }

    public Unit getUnit() {
        String unit = this.quantity.split(" ")[1];
        return new Unit(unit);
    }

    public float getValue() {
        Float value = Float.parseFloat(this.quantity.split(" ")[0]);
        return value;
    }

    public Quantity getQuantity2() {
        float value = this.getValue();
        Unit unit = this.getUnit();
        return new Quantity(value, unit);
    }

    @Override
    public String toString() {
        return this.name + ", " + this.quantity;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Ingredient // instanceof handles nulls
                        && name.equals(((Ingredient) other).name)); // state check
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
