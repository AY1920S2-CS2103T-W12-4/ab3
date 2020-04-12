package cookbuddy.model.recipe.attribute;

import static cookbuddy.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ServingTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Serving((Integer) null));
    }

    @Test
    public void constructor_invalidServing_throwsIllegalArgumentException() {
        int invalidServing = 0;
        assertThrows(IllegalArgumentException.class, () -> new Serving(invalidServing));
    }

    @Test
    public void isValidServing() {
        // null serving
        assertThrows(NullPointerException.class, () -> Serving.isValidServing((Integer) null));

        // invalid serving
        assertFalse(Serving.isValidServing(-1)); // invalid negative integer
        assertFalse(Serving.isValidServing(-2147483647)); // invalid large negative integer
        assertFalse(Serving.isValidServing(0)); // invalid number zero

        // valid serving
        assertTrue(Serving.isValidServing(1)); // valid integer
        assertTrue(Serving.isValidServing(2147483647)); // valid large integer
    }

}
