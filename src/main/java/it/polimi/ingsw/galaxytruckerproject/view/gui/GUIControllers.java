package it.polimi.ingsw.galaxytruckerproject.view.gui;

/**
 * Abstract class serving as a base for GUI controllers in the application.
 * GUIControllers defines a set of methods that subclasses can implement to manage
 * and update various graphical components in the user interface.
 */
public abstract class GUIControllers {

    /**
     * Updates the progress bar during an operation by calculating and displaying the current
     * progress based on the provided parameters.
     *
     * @param index The current step or progress point, indicating the current position within the operation.
     * @param max   The maximum value or total number of steps for the operation, used to calculate the progress percentage.
     * @param turns The number of turns or iterations in the operation, potentially used for additional progress calculations.
     */
    public void goProgressBar(int index, int max, int turns){}

    /**
     * Displays the result of a dice roll in the GUI.
     * The implementation defines how the result is visually represented based on the given dice value.
     *
     * @param diceRoll the numerical result of the dice roll to be displayed
     */
    public void showRoll(){}

    /**
     * Updates the display or state of all visible cards in the graphical interface.
     * This method is intended to refresh or re-render card elements, ensuring the UI
     * reflects the current state of the game or application data related to cards.
     * Specific implementation details should be defined by subclasses.
     */
    public void updateCards(){}

    /**
     * Updates the EDTC with the latest player
     * details and scores after the game has concluded. This method typically
     * reflects final game statistics, rankings, penalties, and other end-game
     * information in the graphical user interface.
     *
     * The implementation of this method should ensure that the data displayed
     * aligns with the current state of the game and adheres to any UI or
     * application-specific formatting requirements.
     */
    public void updateEDTC(){}

    /**
     * Updates the flight board display in the graphical user interface.
     * This method is intended to refresh or modify the visual representation
     * of the flight board based on the current state of the application or game.
     * It typically ensures that the flight board reflects the latest data or changes
     * from the model layer, such as updated player states or board configurations.
     * The specific implementation is dependent on the subclass overriding this method.
     */
    public void updateFlightBoard(){}

    /**
     * UpdatesMDT within the user interface.
     * This method is typically responsible for refreshing or re-rendering the visual representation
     * of the main display components, incorporating the most up-to-date data from the model layer.
     * It ensures the accuracy and consistency of the information shown to the user.
     * Subclasses of GUIControllers should override this method to implement
     * custom behavior specific to their respective use cases or display requirements.
     */
    public void updateMDT(){}
}
