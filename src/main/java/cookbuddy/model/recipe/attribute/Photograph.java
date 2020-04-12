package cookbuddy.model.recipe.attribute;

import static cookbuddy.commons.util.CollectionUtil.requireAllNonNull;
import static java.util.Objects.requireNonNull;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;

import cookbuddy.commons.util.FileUtil;
import cookbuddy.commons.util.PhotographUtil;
import cookbuddy.model.recipe.Recipe;

/**
 * Wrapper class for {@link BufferedImage}. Represents a photo of a
 * {@link Recipe} that a user can possibly provide to the application.
 */
public class Photograph {

    public static final PhotographUtil IMAGE_UTIL = PhotographUtil.imageUtil();
    public static final String MESSAGE_CONSTRAINTS = IMAGE_UTIL.messageConstraints;
    public static final Photograph PLACEHOLDER_PHOTOGRAPH = new Photograph(IMAGE_UTIL.placeholderImage);

    private final BufferedImage photoData;

    /**
     * Constructs a {@code Photograph}, by reading from {@code relativePath}, and
     * parsing the file at the path.
     *
     * @param imagePath a comma-separated list of strings representing the path
     *                  tree.
     * @throws IOException
     */
    public Photograph(String... imagePath) throws IOException {
        this(FileUtil.absolutePathFrom(imagePath));
    }

    /**
     * Constructs a {@code Photograph}, by reading from {@code imagePath}, and
     * parsing the file at the path.
     *
     * @param imagePath A {@link Path} that directs to the photograph file in
     *                  question.
     * @throws IOException
     */
    public Photograph(Path imagePath) throws IOException {
        this(FileUtil.streamFromPath(imagePath));
    }

    /**
     * Constructs a {@link Photograph} by reading from {@code imageInputStream}.
     *
     * @param imageInputStream The {@link InputStream} of this image; buffered and
     *                         high-performance.
     */
    public Photograph(InputStream imageInputStream) {
        this(IMAGE_UTIL.getImage(imageInputStream));
    }

    private Photograph(BufferedImage image) {
        requireNonNull(image);
        this.photoData = image;
    }

    /**
     * Constructs a {@link Photograph} by reading data from {@code URL}.
     *
     * @param url The {@link URL} that this recipe's photograph is located at.
     */
    public Photograph(URL url) {
        requireAllNonNull(url);
        this.photoData = IMAGE_UTIL.getImage(url);
    }

    /**
     * Returns the {@link Path} that {@code recipe}'s {@link Photograph} is to be
     * stored as, for use by CookBuddy. Uses the recipe name to generate an image.
     * <p>
     * If {@code recipe} only has a placeholder image, then a default path is
     * returned.
     *
     * @param recipe The {@link Recipe} whose photograph file path is to be
     *               returned.
     * @return The file name that this {@link Photograph} is to be stored on disk
     *         as.
     */
    public Path getImageFileName(Recipe recipe) {
        if (IMAGE_UTIL.isSameImage(this.photoData, IMAGE_UTIL.placeholderImage)) {
            return FileUtil.relativePathFrom("placeholder");
        } else {
            return FileUtil.relativePathFrom(
                    recipe.getName().toString().replaceAll("\\s+", "").toLowerCase() + "_" + this.hashCode() + ".png");
        }
    }

    /**
     * Returns an {@link InputStream} from this {@link Photograph}'s image data, by
     * calling {@link PhotographUtil#getImageInputStream(BufferedImage)}.
     *
     * @return An {@link InputStream} of this {@link Photograph}, buffered and
     *         high-performance.
     */
    public InputStream getInputStream() {
        return IMAGE_UTIL.getImageInputStream(this.photoData);
    }

    public BufferedImage getData() {
        return this.photoData;
    }

    @Override
    public String toString() {
        return "Photograph@" + Integer.toHexString(this.photoData.hashCode()) + ": image =" + this.photoData.toString();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Photograph // instanceof handles nulls
                        && IMAGE_UTIL.isSameImage(this.photoData, ((Photograph) other).photoData)); // state check
    }

    @Override
    public int hashCode() {
        return IMAGE_UTIL.hashImage(this.photoData);
    }
}
