package cookbuddy.model.recipe.attribute;

import static cookbuddy.commons.util.AppUtil.checkArgument;
import static java.util.Objects.requireNonNull;

import java.util.regex.Pattern;

/**
 * A class to model quantities used in creating {@code Ingredient}s used in each
 * recipe.
 */
public class Quantity {
    public static final String MESSAGE_CONSTRAINTS = "Ingredient quantity must be a number"
            + "followed by a unit; spaces are optional. E.g: 12 g";

    public static final Pattern VALID_QUANTITY_PATTERN = Pattern.compile("\\d+(\\.(\\d+))? ?(\\w+)?");
    public static final Pattern VALID_NUMERIC_PATTERN = Pattern.compile("\\d+(\\.(\\d+))?");

    private Float value;
    private Unit unit;

    /**
     * Constructs a Quantity from {@code quantityString}.
     *
     * @param quantityString a {@link String} that represents an
     *                       {@link Ingredient}'s quantity.
     */
    public Quantity(String quantityString) {
        requireNonNull(quantityString);
        checkArgument(isValidQuantity(quantityString), MESSAGE_CONSTRAINTS);

        this.value = Float.parseFloat(VALID_NUMERIC_PATTERN.matcher(quantityString).group(1));
        //this.unit = new Unit();
    }

    public Quantity(float value, Unit unit) {
        this.value = value;
        this.unit = unit;
    }

    /**
     * Returns a scaled quantity according to the given size.
     * @param size size of scaling.
     * @return scaled quantity.
     */
    public Quantity scaledQuantity(int size) {
        float scaledValue = (float) size * this.value;
        return new Quantity(scaledValue, this.unit);
    }

    public String stringRep() {
        return this.value + ", " + this.unit;
    }

    /**
     * Returns {@code true} if {@code quantityString} is a valid quantity String.
     *
     * @param quantityString a string representing an {@link Ingredient} quantity
     */
    public static boolean isValidQuantity(String quantityString) {
        return VALID_QUANTITY_PATTERN.matcher(quantityString).matches();
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return super.toString();
    }
}
