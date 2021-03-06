package cookbuddy.ui;

import java.util.logging.Logger;

import cookbuddy.commons.core.GuiSettings;
import cookbuddy.commons.core.LogsCenter;
import cookbuddy.logic.Logic;
import cookbuddy.logic.commands.CommandResult;
import cookbuddy.logic.commands.exceptions.CommandException;
import cookbuddy.logic.parser.exceptions.ParseException;
import cookbuddy.model.recipe.Recipe;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

/**
 * The Main Window. Provides the basic application layout containing
 * a menu bar and space where other JavaFX elements can be placed.
 */
public class MainWindow extends UiPart<Stage> {

    public static final JMetro JMETRO = new JMetro(Style.LIGHT);


    private static final String FXML = "MainWindow.fxml";
    private static String commandDescription = "";

    private final Logger logger = LogsCenter.getLogger(getClass());

    private Stage primaryStage;
    private Logic logic;

    // Independent Ui parts residing in this Ui container
    private RecipeListPanel recipeListPanel;
    private ResultDisplay resultDisplay;
    private HelpWindow helpWindow;
    private RecipeView recipeView;


    @FXML
    private StackPane commandBoxPlaceholder;

    @FXML
    private MenuItem helpMenuItem;

    @FXML
    private StackPane recipeListPanelPlaceholder;

    @FXML
    private StackPane recipeViewPanelPlaceholder;

    @FXML
    private StackPane resultDisplayPlaceholder;

    @FXML
    private StackPane statusbarPlaceholder;

    public MainWindow(Stage primaryStage, Logic logic) {
        super(FXML, primaryStage);

        // Set dependencies
        this.primaryStage = primaryStage;
        this.logic = logic;

        // Configure the UI
        setWindowDefaultSize(logic.getGuiSettings());

        JMETRO.setAutomaticallyColorPanes(true);
        JMETRO.setScene(this.primaryStage.getScene());

        setAccelerators();

        helpWindow = new HelpWindow();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    private void setAccelerators() {
        setAccelerator(helpMenuItem, KeyCombination.valueOf("F1"));
    }

    /**
     * Sets the accelerator of a MenuItem.
     *
     * @param keyCombination the KeyCombination value of the accelerator
     */
    private void setAccelerator(MenuItem menuItem, KeyCombination keyCombination) {
        menuItem.setAccelerator(keyCombination);

        /*
         * TODO: the code below can be removed once the bug reported here
         * https://bugs.openjdk.java.net/browse/JDK-8131666
         * is fixed in later version of SDK.
         *
         * According to the bug report, TextInputControl (TextField, TextArea) will
         * consume function-key events. Because CommandBox contains a TextField, and
         * ResultDisplay contains a TextArea, thus some accelerators (e.g F1) will
         * not work when the focus is in them because the key event is consumed by
         * the TextInputControl(s).
         *
         * For now, we add following event filter to capture such key events and open
         * help window purposely so to support accelerators even when focus is
         * in CommandBox or ResultDisplay.
         */
        getRoot().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getTarget() instanceof TextInputControl && keyCombination.match(event)) {
                menuItem.getOnAction().handle(new ActionEvent());
                event.consume();
            }
        });
    }

    /**
     * Fills up all the placeholders of this window.
     */
    public void defaultFill(Recipe recipe) {
        if (logic.getFilteredRecipeList().size() == 0) {
            recipeView = new RecipeView();
        } else {
            recipeView = new RecipeView(recipe);
        }
        fillInfo();
    }


    /**
     * fills in the ingredient/instruction fields
     */
    public void fillInfo() {
        this.recipeViewPanelPlaceholder.getChildren().add(recipeView.getRoot());

        recipeListPanel = new RecipeListPanel(logic.getFilteredRecipeList());
        recipeListPanelPlaceholder.getChildren().add(recipeListPanel.getRoot());

        resultDisplay = new ResultDisplay();
        resultDisplayPlaceholder.getChildren().add(resultDisplay.getRoot());

        StatusBarFooter statusBarFooter = new StatusBarFooter(logic.getRecipeBookFilePath());
        statusbarPlaceholder.getChildren().add(statusBarFooter.getRoot());

        CommandBox commandBox = new CommandBox(this::executeCommand);
        commandBoxPlaceholder.getChildren().add(commandBox.getRoot());
    }

    /**
     * Fills in information for the recipe
     */
    public void fillInnerParts() {
        if (logic.getFilteredRecipeList().size() == 0) {
            recipeView = new RecipeView();
            fillInfo();
        } else {
            defaultFill(logic.getFilteredRecipeList().get(0));
        }
    }

    /**
     * Sets the default size based on {@code guiSettings}.
     */
    private void setWindowDefaultSize(GuiSettings guiSettings) {
        primaryStage.setHeight(guiSettings.getWindowHeight());
        primaryStage.setWidth(guiSettings.getWindowWidth());
        if (guiSettings.getWindowCoordinates() != null) {
            primaryStage.setX(guiSettings.getWindowCoordinates().getX());
            primaryStage.setY(guiSettings.getWindowCoordinates().getY());
        }
    }

    /**
     * Sets the command description of the help window
     * @param commandDescription the new command description.
     */
    public static void setCommandDescription(String commandDescription) {
        MainWindow.commandDescription = commandDescription;
    }

    /**
     * Opens the help window or focuses on it if it's already opened.
     */
    @FXML
    public void handleHelp() {
        String toDisplay = UiManager.getCommandDescription();
        helpWindow = new HelpWindow();
        if (!toDisplay.equals("")) {
            helpWindow.show(toDisplay);
        } else {
            helpWindow.show(HelpWindow.HELP_MESSAGE);
        }
        if (!helpWindow.isShowing()) {
            helpWindow.show();
        } else {
            helpWindow.focus();
        }
    }

    void show() {
        primaryStage.show();
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        GuiSettings guiSettings = new GuiSettings(primaryStage.getWidth(), primaryStage.getHeight(),
            (int) primaryStage.getX(), (int) primaryStage.getY());
        logic.setGuiSettings(guiSettings);
        helpWindow.hide();
        primaryStage.hide();
    }

    public RecipeListPanel getPersonListPanel() {
        return recipeListPanel;
    }

    /**
     * Executes the command and returns the result.
     *
     * @see cookbuddy.logic.Logic#execute(String)
     */
    private CommandResult executeCommand(String commandText) throws CommandException, ParseException {
        try {
            CommandResult commandResult = logic.execute(commandText);
            logger.info("Result: " + commandResult.getFeedbackToUser());
            resultDisplay.setFeedbackToUser(commandResult.getFeedbackToUser());

            if (commandResult.isShowHelp()) {
                handleHelp();
            }

            if (commandResult.isExit()) {
                handleExit();
            }

            return commandResult;
        } catch (CommandException | ParseException e) {
            logger.info("Invalid command: " + commandText);
            resultDisplay.setFeedbackToUser(e.getMessage());
            throw e;
        }
    }
}
