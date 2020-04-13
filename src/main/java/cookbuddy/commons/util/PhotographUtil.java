package cookbuddy.commons.util;

import static cookbuddy.commons.util.FileUtil.getResourceAsInputStream;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import cookbuddy.MainApp;
import cookbuddy.commons.core.LogsCenter;
import cookbuddy.model.recipe.Recipe;
import javafx.collections.ObservableList;

/**
 * A singleton class for working with images, converting between image objects,
 * reading and writing from image files, and to simplify and reduce calls to
 * {@link ImageIO} repeatedly.
 * <p>
 * This class follows the singleton pattern
 * ({@link https://www.geeksforgeeks.org/singleton-class-java/}), because
 * deadlocks were encountered when initialising static variables, when this was
 * originally a utility class. This pattern would originally fail several
 * CheckStyle checks, which have since been disabled.
 */
public class PhotographUtil {
    public static final String PLACEHOLDER_IMAGE_PATH_STRING = "/images/recipe_placeholder.jpg";
    public static final Path PLACEHOLDER_IMAGE_PATH = Paths.get(PLACEHOLDER_IMAGE_PATH_STRING);
    public final InputStream placeHolderImageStream;
    public final BufferedImage placeholderImage;
    public final Path defaultStoragePath = FileUtil.relativePathFrom("data", "images");
    public final String messageConstraints = "Image not found, or invalid image path given.";
    private final Logger logger = LogsCenter.getLogger(MainApp.class);

    private PhotographUtil() {
        this.placeHolderImageStream = getResourceAsInputStream(PLACEHOLDER_IMAGE_PATH_STRING);
        this.placeholderImage = getImage(placeHolderImageStream);
    }

    public static PhotographUtil imageUtil() {
        return new PhotographUtil();
    }

    /**
     * Returns a {@link BufferedImage} from the given {@code Path}
     *
     * @param path An image file path
     * @return An image object
     */
    public BufferedImage readImage(Path path) {
        try {
            return ImageIO.read(path.toFile());
        } catch (IOException e) {
            logger.warning(messageConstraints);
            return this.placeholderImage;
        }
    }

    /**
     * Returns an {@link InputStream} from the given {@code image}, by invoking a
     * {@link ByteArrayOutputStream} and copying the data out.
     *
     * @param image An image object
     * @return A data object representing that image
     */
    public InputStream getImageInputStream(BufferedImage image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream() {
            @Override
            public synchronized byte[] toByteArray() {
                return this.buf;
            }
        };

        try {
            ImageIO.write(image, "png", baos);
        } catch (IOException e) {
            logger.warning("An error has occurred. Using placeholder image instead.");
            return placeHolderImageStream;
        }
        return new BufferedInputStream(new ByteArrayInputStream(baos.toByteArray()));
    }

    /**
     * Returns a {@link BufferedImage} from the given {@code imageInputStream}
     *
     * @param imageInputStream A data object representing an image.
     * @return An image object.
     */
    public BufferedImage getImage(InputStream imageInputStream) {
        try {
            return ImageIO.read(imageInputStream);
        } catch (IOException e) {
            logger.warning("An error has occurred. Using placeholder image instead.");
            return placeholderImage;
        }
    }

    /**
     * Returns a {@link BufferedImage} from the specified {@code url}.
     *
     * @param url The {@link URL} that the image is located at.
     * @return The image at the URL.
     */
    public BufferedImage getImage(URL url) {
        try {
            return ImageIO.read(url);
        } catch (IOException e) {
            logger.warning("An error has occurred. Using placeholder image instead.");
            return placeholderImage;
        }
    }

    /**
     * Compares two {@link BufferedImage}s, specified by {@code i1} and {@code i2},
     * pixel-by-pixel, to determine if they are the same image.
     * <p>
     * Expensive O(n^2) operation; use sparingly.
     *
     * @return {@code true} if {@code i1}'s pixels are all equal to those of
     * {@code i2}'s.
     */
    public boolean isSameImage(BufferedImage i1, BufferedImage i2) {
        if (i1.getWidth() != i2.getWidth() || i1.getHeight() != i2.getHeight()) {
            return false;
        }

        for (int x = 0; x < i1.getWidth(); x++) {
            for (int y = 0; y < i2.getHeight(); y++) {
                if (i1.getRGB(x, y) != i2.getRGB(x, y)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * See {@link #isPlaceHolderImage(Path)}.
     */
    public static boolean isPlaceHolderImage(String... imageFilePathStrings) {
        return isPlaceHolderImage(FileUtil.relativePathFrom(imageFilePathStrings));
    }

    /**
     * Checks if {@link imageFilePath} refers to the placeholder image.
     *
     * @param imageFilePath A {@link Path} where an image is stored.
     * @return {@code true} if {@link imageFilePath} resolves to the placeholder.
     */
    public static boolean isPlaceHolderImage(Path imageFilePath) {
        return imageFilePath.compareTo(PLACEHOLDER_IMAGE_PATH) == 0
            || imageFilePath.compareTo(FileUtil.relativePathFrom("data", "images", "placeholder")) == 0;
    }

    /**
     * Writes all images in {@code recipeList} to disk; skips if the file already
     * exists.
     *
     * @param recipeList    An unmodifiable list of recipes whose images are to be
     *                      saved.
     * @param imageFilePath
     * @throws IOException
     */
    public void saveAllImages(ObservableList<Recipe> recipeList, Path imageFilePath) throws IOException {
        for (Recipe recipe : recipeList) {
            Path recipeImagePath = FileUtil.joinPaths(imageFilePath, recipe.getPhotograph().getImageFileName(recipe));
            if (!isPlaceHolderImage(recipeImagePath) && !FileUtil.isFileExists(recipeImagePath)) {
                FileUtil.createIfMissing(recipeImagePath);
                ImageIO.write(recipe.getPhotograph().getData(), "png", recipeImagePath.toFile());
            }
        }
    }

    /**
     * Generates a hashcode for {@code image}, based on its RGB pixel data. Two
     * identical images should return the same hashcode.
     *
     * @param image A {@link BufferedImage} to hash
     * @return The hashcode.
     */
    public int hashImage(BufferedImage image) {
        return Arrays.hashCode(image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth()));
    }
}
