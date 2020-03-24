package seedu.address.model.recipe.attribute;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Calorie in the recipe book.
 * Guarantees: immutable; calorie is valid as declared in {@link #isValidCalorieAmount(String)}
 */
public class Calorie {

    public static final String MESSAGE_CONSTRAINTS = "Calorie amount should be a positive integer value.";
    public static final String VALIDATION_REGEX = "\\d+";

    public final String calorie;

    /**
     * Constructs a {@code Calorie}.
     *
     * @param calorie A valid calorie amount.
     */
    public Calorie(String calorie) {
        requireNonNull(calorie);
        checkArgument(isValidCalorieAmount(calorie), MESSAGE_CONSTRAINTS);
        this.calorie = calorie;
    }

    /**
     * Returns true if a given string is a valid calorie amount.
     */
    public static boolean isValidCalorieAmount(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Calorie // instanceof handles nulls
                && calorie.equals(((Calorie) other).calorie)); // state check
    }

    @Override
    public int hashCode() {
        return calorie.hashCode();
    }

    /**
     * Format state as text for viewing.
     */
    public String toString() {
        return calorie;
    }

}
