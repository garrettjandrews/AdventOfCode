import java.util.*;
import java.io.*;

public class Part2 {
  public static void main(String[] args) {
    if (args.length != 1) {
      System.out.println("Usage: <input_path>");
      return;
    }

    String input_path = args[0];
    BufferedReader br;
    ArrayList<String> lines = new ArrayList<>();

    // read all lines into an arraylist for easier use
    try {
      br = new BufferedReader(new FileReader(input_path));
      String line;
      while ((line = br.readLine()) != null) {
        lines.add(line);
      }
      br.close();
    } catch (IOException e) {
      System.out.println(e);
      return;
    }

    // build a hashmap with each card and the number of them that we have
    HashMap<Integer, Integer> cardCounts = new HashMap<>();
    for (int i = 0; i < lines.size(); i++) {
      cardCounts.put(i + 1, 1);
    }

    int result = 0;
    for (int i = 1; i <= lines.size(); i++) {
      String line = lines.get(i - 1);
      int numMatches = getNumMatches(line);
      if (numMatches == 0) {
        continue;
      }

      Integer copiesToMake = cardCounts.get(i);
      int start_card = i + 1;
      int end_card = i + numMatches;

      for (int j = start_card; j <= end_card; j++) {
        cardCounts.put(j, cardCounts.get(j) + copiesToMake);
      }
    }

    // finally sum up the values of the hashmap
    for (Integer count : cardCounts.values()) {
      result += count;
    }

    System.out.println(result);
  }

  public static int getNumMatches(String line) {
    line = line.substring(line.indexOf(":") + 2); // get rid of the "Card N: " part
    int numMatches = 0;
    String winningNumbers = line.substring(0, line.indexOf("|"));
    String ourNumbers = line.substring(line.indexOf("|") + 1);

    // convert numbers to arrayLists and filter
    String[] winningStringList = winningNumbers.split(" ");
    String[] ourStringList = ourNumbers.split(" ");

    ArrayList<Integer> winningNumberList = new ArrayList<>();
    ArrayList<Integer> ourNumberList = new ArrayList<>();

    for (int i = 0; i < winningStringList.length; i++) {
      String s = winningStringList[i];
      if (s.length() > 0) {
        winningNumberList.add(Integer.parseInt(s));
      }
    }
    for (int i = 0; i < ourStringList.length; i++) {
      String s = ourStringList[i];
      if (s.length() > 0) {
        ourNumberList.add(Integer.parseInt(s));
      }
    }

    for (int i = 0; i < ourNumberList.size(); i++) {
      Integer thisInt = ourNumberList.get(i);
      if (winningNumberList.contains(thisInt)) {
        numMatches++;
      }
    }
    return numMatches;
  }
}
