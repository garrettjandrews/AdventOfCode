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

    int result = 0;
    HashMap<Integer, HashMap<Integer, Integer>> numberTable = BuildTable(lines);

    // now we iterate through the lines and look at each *.
    // If we find a *, then we look for the numbers around it.
    for (int i = 0; i < lines.size(); i++) {
      String line = lines.get(i);
      for (int j = 0; j < line.length(); j++) {
        if (line.charAt(j) == '*') {
          result += handleGear(lines, i, j);
        }
      }
    }
    System.out.println(result);
  }

  // returns an integer of the gear value
  // note that this can be 0 if we don't have the right number of components
  public static Integer handleGear(ArrayList<String> lines, int row, int col) {
    // build a table with the components
    ArrayList<Integer> components = new ArrayList<>();

    // determine left bound and right bound to consider
    int leftBound = Math.max(0, col - 1);
    int rightBound = Math.min(col + 1, lines.get(0).length() - 1);

    // look at the top row
    if (row > 0) {
      String topLine = lines.get(row - 1);
      Integer priorNumber = -1;
      for (int i = leftBound; i <= rightBound; i++) {
        if (Character.isDigit(topLine.charAt(i))) {
          Integer thisNumber = getNumberOnLine(topLine, i);
          if (!(thisNumber.equals(priorNumber))) {
            components.add(thisNumber);
            priorNumber = thisNumber.intValue();
          } else if (!(Character.isDigit(topLine.charAt(i)))) {
            priorNumber = -1;
          }
        }
      }
    }

    // look at the home row
    String thisLine = lines.get(row);
    if (Character.isDigit(thisLine.charAt(leftBound))) {
      components.add(getNumberOnLine(thisLine, leftBound));
    }
    if (Character.isDigit(thisLine.charAt(rightBound))) {
      components.add(getNumberOnLine(thisLine, rightBound));
    }

    // look at bottom row
    if (row < lines.size() - 1) {
      String topLine = lines.get(row + 1);
      Integer priorNumber = -1;
      for (int i = leftBound; i <= rightBound; i++) {
        if (Character.isDigit(topLine.charAt(i))) {
          Integer thisNumber = getNumberOnLine(topLine, i);
          if (!(thisNumber.equals(priorNumber))) {
            components.add(thisNumber);
            priorNumber = thisNumber.intValue();
          } else if (!(Character.isDigit(topLine.charAt(i)))) {
            priorNumber = -1;
          }
        }
      }
    }

    if (components.size() != 2) {
      System.out.print(row + ": ");
      for (int i = 0; i < components.size(); i++) {
        System.out.print(components.get(i) + ", ");
      }
      System.out.println("");
      return 0;
    } else {
      // System.out.println(components.get(0) + " and " + components.get(1));
      return components.get(0) * components.get(1);
    }
  }

  // function that builds a table with all numbers and their positions in the
  // input file;
  public static HashMap<Integer, HashMap<Integer, Integer>> BuildTable(ArrayList<String> lines) {
    HashMap<Integer, HashMap<Integer, Integer>> results = new HashMap<>();

    for (int i = 0; i < lines.size(); i++) {
      String line = lines.get(i);
      HashMap<Integer, Integer> this_line = new HashMap<>();

      for (int j = 0; j < line.length(); j++) {
        if (!(Character.isDigit(line.charAt(j)))) {
          continue;
        }

        Integer n = getNumberOnLine(line, j);
        this_line.put(j, n);
      }
      results.put(i, this_line);
    }

    return results;
  }

  // takes an input line and an index and returns the number that has a digit at a
  // given index.
  public static Integer getNumberOnLine(String line, int index) {
    // go left until we hit the beginning of the line or a non-digit
    StringBuilder stringBuilder = new StringBuilder();

    while (index > 0 && Character.isDigit(line.charAt(index))) {
      index--;
    }

    if (!(Character.isDigit(line.charAt(index)))) {
      index++;
    }

    while (index < line.length() && Character.isDigit(line.charAt(index))) {
      stringBuilder.append(line.charAt(index));
      index++;
    }

    return Integer.parseInt(stringBuilder.toString());
  }
}
