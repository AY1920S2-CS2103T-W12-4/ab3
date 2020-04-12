package cookbuddy.model.recipe.attribute;

import static cookbuddy.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class RatingTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Rating((Integer) null));
    }

    @Test
    public void constructor_invalidRating_throwsIllegalArgumentException() {
        int invalidRating = 1000;
        assertThrows(IllegalArgumentException.class, () -> new Rating(invalidRating));
    }

    @Test
    public void isValidRating() {
        // null rating
        assertThrows(NullPointerException.class, () -> Rating.isValidRating((Integer) null));

        // invalid rating
        assertFalse(Rating.isValidRating(-1)); // negative integer (integer below left boundary)
        assertFalse(Rating.isValidRating(9)); // integer above right boundary

        // valid rating
        assertTrue(Rating.isValidRating(3)); // valid integer only
        assertTrue(Rating.isValidRating(0)); // valid left boundary integer only
        assertTrue(Rating.isValidRating(5)); // valid right boundary integer only
    }

}
