package cookbuddy.model.recipe.attribute;

import static cookbuddy.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class DifficultyTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Difficulty((Integer) null));
    }

    @Test
    public void constructor_invalidDifficulty_throwsIllegalArgumentException() {
        int invalidDifficulty = 1000;
        assertThrows(IllegalArgumentException.class, () -> new Difficulty(invalidDifficulty));
    }

    @Test
    public void isValidDifficulty() {
        // null difficulty
        assertThrows(NullPointerException.class, () -> Difficulty.isValidDifficulty((Integer) null));

        // invalid difficulty
        assertFalse(Difficulty.isValidDifficulty(-1)); // negative integer (integer below left boundary)
        assertFalse(Difficulty.isValidDifficulty(9)); // integer above right boundary

        // valid difficulty
        assertTrue(Difficulty.isValidDifficulty(3)); // valid integer only
        assertTrue(Difficulty.isValidDifficulty(0)); // valid left boundary integer only
        assertTrue(Difficulty.isValidDifficulty(5)); // valid right boundary integer only
    }

}
