package game.model;

import java.util.Objects;

/**
 * Class to represent a Player of the Fish game.
 * A player is composed of information about the player, specifically
 * - their age in years (used in determining turn order)
 * - their assigned penguin-color
 * - their number of fish caught
 */
public class Player {
  private final int age;
  private final Penguin.PenguinColor color;
  private int fish;
  public static int STARTING_FISH = 0;

  /**
   * Constructs a player with the given age and assigned color with a default Fish count.
   * @param playerAge The player's age in years.
   * @param assignedColor The penguin-color of the player assigned by the referee.
   */
  public Player(int playerAge, Penguin.PenguinColor assignedColor) {
    if(playerAge < 0) {
      throw new IllegalArgumentException("Player cannot be less than 0 years old!");
    }
    this.age = playerAge;
    this.color = assignedColor;
    this.fish = STARTING_FISH;
  }

  /**
   * Copy constructor for Player objects. Copies the age, assigned color, and number of fish
   * belonging to the player.
   * @param p The Player object to make a copy of.
   */
  public Player(Player p) {
    this.age = p.age;
    this.color = p.color;
    this.fish = p.fish;
  }

  /**
   * Gets the age of this player in years.
   * @return The player's age in years as an integer.
   */
  public int getAge() {
    return this.age;
  }

  /**
   * Gets the number of fish that the player currently has.
   * @return The number of fish the player has acquired as an integer.
   */
  public int getFish() {
    return this.fish;
  }

  /**
   * Gets the penguin-color assigned to this player.
   * @return The penguin-color of the player.
   */
  public Penguin.PenguinColor getColor() {
    return color;
  }

  /**
   * Adds a number of fish to the player's count.
   * @param fishObtained Integer number of fish to give to this player.
   */
  public void addFish(int fishObtained) {
    this.fish += fishObtained;
  }

  @Override
  public boolean equals(Object obj) {
    if(obj instanceof Player) {
      Player p = (Player) obj;
      return (this.age == p.age
              && this.color == p.color
              && this.fish == p.fish);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(age, color, fish);
  }
}
