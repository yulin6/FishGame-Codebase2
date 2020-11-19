package tmanager;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import player.BadWinnerPlayerComponent;
import player.ExceptionPlayerComponent;
import player.IPlayerComponent;
import player.IllogicalPlayerComponent;
import player.InfiniteLoopPlayerComponent;
import player.NullReturnPlayerComponent;
import player.PlayerComponent;

import static org.junit.Assert.*;

/**
 * Class handling unit tests for the TournamentManager class, which handles organization of full
 * tournaments given a list of external player components.
 */
public class TournamentManagerTest {
  PlayerComponent pc1, pc2, pc3, pc4, pc5, pc6, pc7, pc8;
  int seed = 83;

  IllogicalPlayerComponent illogical;
  InfiniteLoopPlayerComponent looperInGetAge, looperInJoinTournament;
  NullReturnPlayerComponent nuller;
  ExceptionPlayerComponent throwerInGetAge, throwerInJoinTournament;
  BadWinnerPlayerComponent badwinner1, badwinner2, badwinner3, badwinner4;

  @Before
  public void setUp() {
    pc1 = new PlayerComponent(1, seed);
    pc2 = new PlayerComponent(2, seed);
    pc3 = new PlayerComponent(3, seed);
    pc4 = new PlayerComponent(4, seed);
    pc5 = new PlayerComponent(5, seed);
    pc6 = new PlayerComponent(6, seed);
    pc7 = new PlayerComponent(7, seed);
    pc8 = new PlayerComponent(8, seed);

    badwinner1 = new BadWinnerPlayerComponent(1);
    badwinner2 = new BadWinnerPlayerComponent(2);
    badwinner3 = new BadWinnerPlayerComponent(3);
    badwinner4 = new BadWinnerPlayerComponent(4);

    illogical = new IllogicalPlayerComponent();
    looperInGetAge = new InfiniteLoopPlayerComponent(true,false);
    looperInJoinTournament = new InfiniteLoopPlayerComponent(false, true);
    nuller = new NullReturnPlayerComponent();
    throwerInGetAge = new ExceptionPlayerComponent(true, false);
    throwerInJoinTournament = new ExceptionPlayerComponent(false, true);
  }

  @Test (expected = IllegalArgumentException.class)
  public void notEnoughPlayersException() {
    ArrayList<IPlayerComponent> badList = new ArrayList<>(Arrays.asList(pc1));
    TournamentManager notEnoughPlayersExceptionTm = new TournamentManager(badList);
    notEnoughPlayersExceptionTm.runTournament(); // should not be reached
  }

  @Test
  public void runCleanSingleGameTournament() {
    ArrayList<IPlayerComponent> singleGameComponents = new ArrayList<>(Arrays.asList(pc1, pc2,
            pc3, pc4));
    TournamentManager singleGameTm = new TournamentManager(singleGameComponents);
    singleGameTm.runTournament();
    List<IPlayerComponent> winners = singleGameTm.getWinners();
    assertTrue(winners.size() > 0);
  }

  @Test
  public void runCleanMultiRoundTournament() {
    ArrayList<IPlayerComponent> allPlayersList = new ArrayList<>(Arrays.asList(pc1, pc2, pc3,
            pc4, pc5, pc6, pc7, pc8));
    TournamentManager cleanMultiRoundTm = new TournamentManager(allPlayersList);
    cleanMultiRoundTm.runTournament();
    List<IPlayerComponent> winners = cleanMultiRoundTm.getWinners();
    assertTrue(winners.size() > 0);
  }

  @Test
  public void runFailingCheatingMultiRoundTournament() {
    TournamentManager badMultiRoundTm = new TournamentManager(Arrays.asList(pc1, pc2, pc3, pc4,
            badwinner1, badwinner2, nuller, illogical, badwinner3));
    badMultiRoundTm.runTournament();
    List<IPlayerComponent> winners = badMultiRoundTm.getWinners();
    // Winners may be size 0 if bad-winning players eliminate properly-functioning ones, and
    // checking if a list is size 0 or greater doesn't test anything, so no check of size.
    assertTrue(Collections.disjoint(winners, Arrays.asList(badwinner1, badwinner2, nuller,
            illogical, badwinner3)));
  }

  @Test
  public void noGoodPlayersGame() {
    System.out.println("Entering a timeout-based test, a pause will occur.");
    TournamentManager noValidPlayersTm = new TournamentManager(Arrays.asList(badwinner1,
            badwinner2, nuller, illogical, looperInGetAge, looperInJoinTournament, throwerInGetAge,
            throwerInJoinTournament));
    noValidPlayersTm.runTournament();
    List<IPlayerComponent> winners = noValidPlayersTm.getWinners();
    assertTrue(winners.isEmpty());
  }

  @Test
  public void allBadWinners() {
    ArrayList<IPlayerComponent> badWinners = new ArrayList<>(Arrays.asList(badwinner1, badwinner2,
            badwinner3, badwinner4));
    TournamentManager badWinnersTm = new TournamentManager(badWinners);
    badWinnersTm.runTournament();
    List<IPlayerComponent> winners = badWinnersTm.getWinners();
    assertTrue(winners.isEmpty());
  }

  @Test
  public void runTournamentRound() {
    ArrayList<IPlayerComponent> allPlayersList = new ArrayList<>(Arrays.asList(pc1, pc2, pc3,
            pc4, pc5, pc6, pc7, pc8));
    TournamentManager cleanMultiRoundTm = new TournamentManager(allPlayersList);
    cleanMultiRoundTm.runTournamentRound();
    try {
      cleanMultiRoundTm.getWinners();
      fail("Expected an exception for trying to get winners in the wrong state of the game.");
    } catch (IllegalStateException e) {
      // Do nothing - expected this exception in the test
    }
    assertTrue(cleanMultiRoundTm.isFirstRoundRun());
  }

  @Test
  public void getWinnersOneGame() {
    ArrayList<IPlayerComponent> bunchOfPlayers = new ArrayList<>(Arrays.asList(pc1, pc3, pc5, pc7));
    TournamentManager someTm = new TournamentManager(bunchOfPlayers);
    someTm.runTournament();
    List<IPlayerComponent> winners = someTm.getWinners();
    assertTrue(winners.contains(pc1) ||
            winners.contains(pc3) ||
            winners.contains(pc5) ||
            winners.contains(pc7));
  }

  @Test
  public void getWinnersMultiGames() {
    ArrayList<IPlayerComponent> bunchOfPlayers = new ArrayList<>(Arrays.asList(pc1, pc2, pc3, pc5,
            pc7, pc8));
    TournamentManager someTm = new TournamentManager(bunchOfPlayers);
    someTm.runTournament();
    List<IPlayerComponent> winners = someTm.getWinners();
    assertTrue(winners.contains(pc1) ||
            winners.contains(pc2) ||
            winners.contains(pc3) ||
            winners.contains(pc5) ||
            winners.contains(pc7) ||
            winners.contains(pc8));
  }

  @Test (expected = IllegalStateException.class)
  public void getWinnersWrongState() {
    TournamentManager someTm = new TournamentManager(Arrays.asList(pc1, pc2));
    someTm.getWinners();
  }
}