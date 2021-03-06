package player;

import game.model.Action;
import game.model.BoardPosition;
import game.model.GameTreeNode;

public interface IStrategy {

  /**
   * Finds the first available board space to place a penguin based on a zig zag pattern starting
   * from the top left hand corner, searching left to right in each row until a free spot is found
   * @param gt The GameTree that will be used to search the game board found in its contained
   *           GameState.
   * @return The board position to place the penguin at
   */
  BoardPosition placePenguin(GameTreeNode gt);

  /**
   * Given a number n > 0, returns the Action (a Move, or Pass if no valid Move) for the performing
   * player that will allow for a minimal-maximal gain after looking ahead n turns for the player.
   * The performing player is the currentPlayer in the GameState contained in the root of the given
   * GameTree.
   * @param gt The GameTree used to calculate the minimal-maximal gain.
   * @param numTurns The number of turns for the performing player to look ahead
   * @return The action that will allow for the minimal-maximal gain after looking ahead numTurns
   *         turns
   */
  Action getMinMaxAction(GameTreeNode gt, int numTurns);
}
