package seedu.address.model.recipe.attribute;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents the Rating of the recipe in the recipe book.
 * Guarantees: immutable; rating is valid as declared in {@link #isValidRating(int)}
 */
public class Rating {

    public static final String MESSAGE_CONSTRAINTS = "Rating should be an integer >= 0 and <= 5";

    public final int rating;

    /**
     * Constructs a {@code Serving size}.
     *
     * @param rating A valid rating.
     */
    public Rating(int rating) {
        requireNonNull(rating);
        checkArgument(isValidRating(rating), MESSAGE_CONSTRAINTS);
        this.rating = rating;
    }


    /**
     * Returns true if a given string is a valid serving size.
     */
    public static boolean isValidRating(int test) {
        return (test >= 0 && test <= 5);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Rating // instanceof handles nulls
                && rating == (((Rating) other).rating)); // state check
    }

    @Override
    public int hashCode() {
        return String.valueOf(rating).hashCode();
    }

    /**
     * Format state as text for viewing.
     */
    public String toString() {
        String ratingString = "";
        for (int i = 0; i < rating; i++) {
            ratingString += "\u2605";
        }
        for (int i = rating; i < 5; i++) {
            ratingString += "\u2606";
        }
        return ratingString;
    }
}
