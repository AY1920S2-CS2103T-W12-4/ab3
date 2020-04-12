package cookbuddy.commons.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

import cookbuddy.Main;

/**
 * Writes and reads files
 */
public class FileUtil {

    public static final String CORRUPTED = "Your JAR file is corrupt, and possibly missing files. "
        + "Please re-download from the source.";
    private static final String CHARSET = "UTF-8";

    public static boolean isFileExists(Path file) {
        return Files.exists(file) && Files.isRegularFile(file);
    }

    /**
     * Returns {@code true} if {@code pathString} can be represented as a valid
     * {@link Path}.
     *
     * @param pathString A {@link String} of a filesystem path.
     * @return duh.
     */
    public static boolean isValidPathString(String pathString) {
        try {
            relativePathFrom(pathString);
        } catch (InvalidPathException e) {
            return false;
        }
        return true;
    }

    /**
     * Returns true if {@code pathStrings} can be converted into a {@code Path} via
     * {@link Paths#get(String)}, otherwise returns false.
     *
     * @param pathStrings A comma-separated list of {@link String}s representing the
     *                    file path. Cannot be null.
     */
    public static boolean isValidPathStrings(String... pathStrings) {
        try {
            relativePathFrom(pathStrings);
        } catch (InvalidPathException ipe) {
            return false;
        }
        return true;
    }

    /**
     * Returns a <em>relative</em> {@link Path} from {@code pathStrings} by using
     * the OS's file system separator ({@code /} on *nix, {@code \} on Windows).
     * <p>
     * For example, {@code "path", "foo", "bar"} is returned as
     * {@code path/foo/bar}.
     *
     * @param pathStrings A comma-separated list of {@link Strings} which are
     *                    concatenated
     * @return An <em>absolute</em> file path.
     */
    public static Path relativePathFrom(String... pathStrings) {
        return Paths.get("", pathStrings).normalize();
    }

    /**
     * Returns an <em>absolute</em> {@link Path} from {@code pathStrings} by using
     * the OS's file system separator ({@code /} on *nix, {@code \} on Windows).
     * <p>
     * For example, {@code "path", "foo", "bar"} is returned as
     * {@code path/foo/bar}.
     *
     * @param pathStrings A comma-separated list of {@link Strings} which are
     *                    concatenated
     * @return An <em>absolute</em> file path.
     */
    public static Path absolutePathFrom(String... pathStrings) {
        return Paths.get("", pathStrings).toAbsolutePath().normalize();
    }

    /**
     * Appends {@code paths} to {@code p1}, in the order of appearance.
     *
     * @param p1    The highest path
     * @param paths A comma-separated list of {@link Path}s to append to {@code p1}.
     * @return An appended {@link Path}.
     */
    public static Path joinPaths(Path p1, Path... paths) {
        Path returnable = p1;
        for (Path path : paths) {
            returnable = returnable.resolve(path);
        }
        return returnable;
    }

    /**
     * Creates a file if it does not exist along with its missing parent
     * directories.
     *
     * @throws IOException if the file or directory cannot be created.
     */
    public static void createIfMissing(Path file) throws IOException {
        if (!isFileExists(file)) {
            createFile(file);
        }
    }

    /**
     * Creates a file if it does not exist along with its missing parent
     * directories.
     */
    public static void createFile(Path file) throws IOException {
        if (Files.exists(file)) {
            return;
        }

        createParentDirsOfFile(file);
        Files.createFile(file);
    }

    /**
     * Creates parent directories of file if it has a parent directory
     */
    public static void createParentDirsOfFile(Path file) throws IOException {
        Path parentDir = file.getParent();

        if (parentDir != null) {
            Files.createDirectories(parentDir);
        }
    }

    /**
     * Assumes file exists
     */
    public static String readFromFile(Path file) throws IOException {
        return new String(Files.readAllBytes(file), CHARSET);
    }

    /**
     * Writes given string to a file. Will create the file if it does not exist yet.
     */
    public static void writeToFile(Path file, String content) throws IOException {
        Files.write(file, content.getBytes(CHARSET));
    }

    /**
     * Returns an {@link InputStream} from {@code file}; the stream is buffered.
     *
     * @param file A {@link Path} to the file in question
     * @return
     * @throws FileNotFoundException if the file is not found.
     */
    public static InputStream streamFromFile(File file) throws FileNotFoundException {
        return new BufferedInputStream(new FileInputStream(file));
    }

    /**
     * Returns an {@link InputStream} from the file located at {@code filePath}.
     *
     * @param filePath A {@link Path} to a file in the filesystem.
     * @return An input stream of {@code filePath}.
     * @throws IOException As specified by
     *                     {@link Files#newInputStream(Path, java.nio.file.OpenOption...)}.
     */
    public static InputStream streamFromPath(Path filePath) throws IOException {
        return new BufferedInputStream(Files.newInputStream(filePath));
    }

    public static InputStream getResourceAsInputStream(String resourceString) {
        return new BufferedInputStream(Main.class.getResourceAsStream(resourceString));
    }
}
