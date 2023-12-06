
import java.util.*;
import java.io.*;

public class Part2 {
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
    ArrayList<Long> game = getGame(lines);

    long result = waysToWinGame(game);
    System.out.println(result);
  }

  // start from the middle, iterate both ways to find the number of ways we could
  // win
  public static long waysToWinGame(ArrayList<Long> game) {
    long time = game.get(0);
    long distance = game.get(1);
    long waysToWin = 0;
    long starting_point = time / 2;

    // evaluate starting point
    if (winsGame(starting_point, time, distance)) {
      waysToWin++;
    }

    // go left until a failure
    for (long i = starting_point - 1; i >= 0; i--) {
      if (winsGame(i, time, distance)) {
        waysToWin++;
      } else {
        break;
      }
    }

    // go right until a failure
    for (long i = starting_point + 1; i <= time; i++) {
      if (winsGame(i, time, distance)) {
        waysToWin++;
      } else {
        break;
      }
    }

    return waysToWin;
  }

  public static boolean winsGame(long holdDownTime, long totalTime, long recordDistance) {
    long timeLeft = totalTime - holdDownTime;
    long distanceTraveled = timeLeft * holdDownTime;
    return distanceTraveled > recordDistance;
  }

  public static ArrayList<Long> getGame(ArrayList<String> lines) {
    String timeLine = lines.get(0);
    String distLine = lines.get(1);
    ArrayList<Long> game = new ArrayList<>();

    timeLine = timeLine.replace("Time:", "");
    distLine = distLine.replace("Distance:", "");
    timeLine = timeLine.replaceAll(" ", "");
    distLine = distLine.replaceAll(" ", "");

    game.add(Long.parseLong(timeLine));
    game.add(Long.parseLong(distLine));

    return game;
  }
}
