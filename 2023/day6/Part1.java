
import java.util.*;
import java.io.*;

public class Part1 {
  public static void main(String[] args) {
    if (args.length != 1) {
      System.out.println("Usage: <input_filepath");
    }

    String input_filepath = args[0];
    BufferedReader br;
    ArrayList<String> lines = new ArrayList<>();

    try {
      br = new BufferedReader(new FileReader(input_filepath));
      String line;
      while ((line = br.readLine()) != null) {
        lines.add(line);
      }
    } catch (IOException e) {
      System.out.println(e);
      return;
    }

    // build an easier structure to hold the games
    ArrayList<ArrayList<Integer>> games = getGames(lines);

    long result = 1;
    for (ArrayList<Integer> game : games) {
      result *= (long) waysToWinGame(game);
    }

    System.out.println(result);
  }

  // start from the middle, iterate both ways to find the number of ways we could
  // win
  public static int waysToWinGame(ArrayList<Integer> game) {
    int time = game.get(0);
    int distance = game.get(1);
    int waysToWin = 0;
    int starting_point = time / 2;

    // evaluate starting point
    if (winsGame(starting_point, time, distance)) {
      waysToWin++;
    }

    // go left until a failure
    for (int i = starting_point - 1; i >= 0; i--) {
      if (winsGame(i, time, distance)) {
        waysToWin++;
      } else {
        break;
      }
    }

    // go right until a failure
    for (int i = starting_point + 1; i <= time; i++) {
      if (winsGame(i, time, distance)) {
        waysToWin++;
      } else {
        break;
      }
    }

    return waysToWin;
  }

  public static boolean winsGame(int holdDownTime, int totalTime, int recordDistance) {
    int timeLeft = totalTime - holdDownTime;
    int distanceTraveled = timeLeft * holdDownTime;
    return distanceTraveled > recordDistance;
  }

  public static ArrayList<ArrayList<Integer>> getGames(ArrayList<String> lines) {

    String[] timeStrings = lines.get(0).split(" ");
    String[] distanceStrings = lines.get(1).split(" ");

    ArrayList<String> filteredTimeStrings = new ArrayList<>();
    ArrayList<String> filteredDistanceStrings = new ArrayList<>();

    for (String s : timeStrings) {
      if ((s.length() == 0) || (!(Character.isDigit(s.charAt(0))))) {
        continue;
      }
      filteredTimeStrings.add(s);
    }
    for (String s : distanceStrings) {
      if ((s.length() == 0) || (!(Character.isDigit(s.charAt(0))))) {
        continue;
      }
      filteredDistanceStrings.add(s);
    }

    if (filteredDistanceStrings.size() != filteredTimeStrings.size()) {
      System.out.println("Error in parsing input");
      return null;
    }

    ArrayList<ArrayList<Integer>> games = new ArrayList<>();
    for (int i = 0; i < filteredTimeStrings.size(); i++) {
      ArrayList<Integer> game = new ArrayList<>();
      game.add(Integer.parseInt(filteredTimeStrings.get(i)));
      game.add(Integer.parseInt(filteredDistanceStrings.get(i)));
      games.add(game);
    }

    return games;
  }
}
