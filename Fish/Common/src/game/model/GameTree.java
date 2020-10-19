package game.model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Class to represent a tree of all possible game states resulting from an initial game state,
 * which should be the state of the game immediately after all penguins have been placed onto
 * the game board. GameTrees can be queried in order to obtain future potential game states.
 * A GameTree stores the initial GameState object from which it was made.
 */
public class GameTree {
  private final GameState initialState;

  /**
   * Constructor for a GameTree. Takes an initial game state and makes a copy of it to be stored
   * as the root state for this tree.
   * @param init The state that represented the root of this tree.
   */
  public GameTree(GameState init) {
    initialState = new GameState(init);
  }

  /**
   * Queries for the resultant GameState from an arbitrary given GameState and a potential
   * action (the action may be invalid, and a action can either be a penguin move or a pass).
   * @param checkFrom The GameState to check the action from.
   * @param a The acton to apply to the GameState, which may be illegal in the current context.
   * @return An IllegalState if the action is not legal in the current conditions, else the
   * resulting GameState that is produced by performing the move.
   */
  public IState lookAhead(GameState checkFrom, Action a) {
    GameState copy = new GameState(checkFrom);
    boolean isValid = doAction(copy, a);
    if(isValid) {
      return copy;
    }
    else {
      return new IllegalState();
    }
  }

  /**
   * Applies a function to all children of an arbitrary input GameState, and returns a list of
   * resultant
   * GameState objects.
   * @param input The input GameState to apply a function to the children of.
   * @param func Function to apply to the list of GameState children of the input GameState; a
   *             Consumer is a generic interface representing functions accepting 1 input type
   *             and 0 output types (void functions).
   * @return the list of GameState objects that are generated by applying the function to each of
   * the children.
   */
  public List<GameState> applyAllChildren(GameState input, Consumer<List<GameState>> func) {
    List<GameState> children = this.getAllChildren(input);
    func.accept(children);
    return children;
  }

  /**
   * Gets all the children of the passed-in GameState by applying each possible action to it and
   * returns.
   * @param g The input GameState to obtain the children of.
   * @return The list of GameState objects representing the children of the passed-in GameState.
   */
  private List<GameState> getAllChildren(GameState g) {
    ArrayList<GameState> childrenList = new ArrayList<>();
    for (Action a : g.getPossibleActions()) {
      GameState nextState = new GameState(g);
      this.doAction(nextState, a);
      childrenList.add(nextState);
    }
    return childrenList;
  }

  /**
   * Applies a player's action to a passed-in GameState object. Returns true if successful, false
   * if not successful.
   * @param g The GameState to do the action on.
   * @param a The action to perform on the GameState - either a Move or a Pass.
   * @return Whether or not the action could be performed on the given game state
   */
  private boolean doAction(GameState g, Action a) {
    // Figure out if we want to throw exceptions here. This is so far only called
    // in a private method where we should only be giving it legal actions.
    if (a != null) {
      // legality checking, etc.
      if(g.getPossibleActions().contains(a)) {
        // do the move
        a.perform(g);
        return true;
      }
      else {
        return false;
      }
      // advance to next player
    }
    else {
      return false;
    }
  }
}
