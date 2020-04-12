package cookbuddy.commons.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class PhotographUtilTest {

    @Test
    public void isValidPlaceHolderImagePath() {
        // valid path
        assertTrue(PhotographUtil.isPlaceHolderImage("/images/recipe_placeholder.jpg"));

        // invalid path
        assertFalse(PhotographUtil.isPlaceHolderImage("a\0"));
    }

}
