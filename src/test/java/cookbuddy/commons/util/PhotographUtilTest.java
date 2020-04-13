package cookbuddy.commons.util;

import static cookbuddy.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.InvalidPathException;

import org.junit.jupiter.api.Test;

public class PhotographUtilTest {

    @Test
    public void isValidPlaceHolderImagePath() {
        // valid path
        assertTrue(PhotographUtil.isPlaceHolderImage("/images/recipe_placeholder.jpg"));

        // invalid path
        assertThrows(InvalidPathException.class, () -> PhotographUtil.isPlaceHolderImage("a\0"));
    }

}
