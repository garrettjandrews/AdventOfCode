import java.util.*;
import java.io.*;

public class Part1 {
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

    int result = 0;
    for (int i = 0; i < lines.size(); i++) {
      String line = lines.get(i);
      result += ProcessLine(line);
    }

    System.out.println(result);
  }

  public static int ProcessLine(String line) {
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

    if (numMatches == 0) {
      return 0;
    }

    return (int) Math.pow(2, numMatches - 1);
  }
}
