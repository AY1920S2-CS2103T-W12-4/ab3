package cookbuddy.storage;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import cookbuddy.commons.core.LogsCenter;
import cookbuddy.commons.exceptions.DataConversionException;
import cookbuddy.commons.exceptions.IllegalValueException;
import cookbuddy.commons.util.FileUtil;
import cookbuddy.commons.util.JsonUtil;
import cookbuddy.commons.util.PhotographUtil;
import cookbuddy.model.ReadOnlyRecipeBook;

/**
 * A class to access RecipeBook data stored as a json file on the hard disk.
 */
public class JsonRecipeBookStorage implements RecipeBookStorage {

    private static final Logger logger = LogsCenter.getLogger(JsonRecipeBookStorage.class);

    private Path filePath;
    private Path imagesPath;

    public JsonRecipeBookStorage(Path filePath, Path imagesPath) {
        this.filePath = filePath;
        this.imagesPath = imagesPath;
    }

    public Path getRecipeBookFilePath() {
        return filePath;
    }

    @Override
    public Optional<ReadOnlyRecipeBook> readRecipeBook() throws DataConversionException {
        return readRecipeBook(filePath);
    }

    /**
     * Similar to {@link #readRecipeBook()}.
     *
     * @param filePath location of the data. Cannot be null.
     * @throws DataConversionException if the file is not in the correct format.
     */
    public Optional<ReadOnlyRecipeBook> readRecipeBook(Path filePath) throws DataConversionException {
        requireNonNull(filePath);

        Optional<JsonSerializableRecipeBook> jsonRecipeBook = JsonUtil.readJsonFile(
            filePath, JsonSerializableRecipeBook.class);
        if (!jsonRecipeBook.isPresent()) {
            return Optional.empty();
        }

        try {
            return Optional.of(jsonRecipeBook.get().toModelType(imagesPath));
        } catch (IllegalValueException ive) {
            logger.info("Illegal values found in " + filePath + ": " + ive.getMessage());
            throw new DataConversionException(ive);
        }
    }

    @Override
    public void saveRecipeBook(ReadOnlyRecipeBook recipeBook) throws IOException {
        saveRecipeBook(recipeBook, filePath);
    }

    /**
     * Similar to {@link #saveRecipeBook(ReadOnlyRecipeBook)}.
     */
    public void saveRecipeBook(ReadOnlyRecipeBook recipeBook, Path dataFilePath) throws IOException {
        requireNonNull(recipeBook);
        requireNonNull(dataFilePath);

        FileUtil.createIfMissing(dataFilePath);
        JsonUtil.saveJsonFile(new JsonSerializableRecipeBook(recipeBook), dataFilePath);
        PhotographUtil.imageUtil().saveAllImages(recipeBook.getRecipeList(), imagesPath);
    }
}
