package cookbuddy.model;

import static java.util.Objects.requireNonNull;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import cookbuddy.commons.core.GuiSettings;
import cookbuddy.commons.util.PhotographUtil;

/**
 * Represents User's preferences.
 */
public class UserPrefs implements ReadOnlyUserPrefs {

    private GuiSettings guiSettings = new GuiSettings();
    private Path dataFilePath = Paths.get("data" , "recipebook.json");
    private Path recipeImagePath = PhotographUtil.imageUtil().defaultStoragePath;

    /**
     * Creates a {@code UserPrefs} with default values.
     */
    public UserPrefs() {}

    /**
     * Creates a {@code UserPrefs} with the prefs in {@code userPrefs}.
     */
    public UserPrefs(ReadOnlyUserPrefs userPrefs) {
        this();
        resetData(userPrefs);
    }

    /**
     * Resets the existing data of this {@code UserPrefs} with {@code newUserPrefs}.
     */
    public void resetData(ReadOnlyUserPrefs newUserPrefs) {
        requireNonNull(newUserPrefs);
        setGuiSettings(newUserPrefs.getGuiSettings());
        setDataFilePath(newUserPrefs.getDataFilePath());
        setImagesPath(newUserPrefs.getImagesPath());
    }


    public GuiSettings getGuiSettings() {
        return guiSettings;
    }

    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        this.guiSettings = guiSettings;
    }

    public Path getDataFilePath() {
        return dataFilePath;
    }

    public void setDataFilePath(Path dataFilePath) {
        requireNonNull(dataFilePath);
        this.dataFilePath = dataFilePath;
    }

    @Override
    public Path getImagesPath() {
        return recipeImagePath;
    }

    public void setImagesPath(Path recipeImagePath) {
        requireNonNull(recipeImagePath);
        this.recipeImagePath = recipeImagePath;
    }


    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof UserPrefs)) { //this handles null as well.
            return false;
        }

        UserPrefs o = (UserPrefs) other;

        return guiSettings.equals(o.guiSettings)
               && dataFilePath.equals(o.dataFilePath)
               && recipeImagePath.equals(o.recipeImagePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(guiSettings, dataFilePath, recipeImagePath);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Gui Settings : " + guiSettings);
        sb.append("\nLocal data file location : " + dataFilePath);
        sb.append("\nRecipes image location : " + recipeImagePath);
        return sb.toString();
    }

}
