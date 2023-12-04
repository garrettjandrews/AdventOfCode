import java.util.*;
import java.io.*;

public class Part1 {
  public static void main(String[] args) {
    if (args.length != 1) {
      System.out.println("Usage: <input_path>");
      return;
    }

    // build a list of the symbols to look for;
    List<Character> charSymbols = Arrays.asList('*', '%', '$', '#', '-', '=', '+', '@', '^', '&', '/');

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
      System.out.println(line);
      ArrayList<ArrayList<Integer>> numbers = getNumbersInLine(line);

      for (int k = 0; k < numbers.size(); k++) {
        int number = numbers.get(k).get(0);
        int start_index = numbers.get(k).get(1);
        int end_index = numbers.get(k).get(2);

        boolean isValid = false;

        // get the bounds to check here
        int leftBound = Math.max(start_index - 1, 0);
        int rightBound = Math.min(end_index + 1, line.length() - 1);

        // check the top line
        if (i != 0) {
          String topLine = lines.get(i - 1);
          for (int j = leftBound; j < rightBound + 1; j++) {
            if (charSymbols.contains(topLine.charAt(j))) {
              isValid = true;
            }
          }
        }

        // check the left and right on the current line;
        String thisLine = lines.get(i);
        if ((charSymbols.contains(thisLine.charAt(leftBound))) || (charSymbols.contains(thisLine.charAt(rightBound)))) {
          isValid = true;
        }

        // check the bottom line;
        if (i != lines.size() - 1) {
          String bottomLine = lines.get(i + 1);

          for (int j = leftBound; j < rightBound + 1; j++) {
            if (charSymbols.contains(bottomLine.charAt(j))) {
              isValid = true;
            }
          }
        }

        // if we got a valid number, add it into the result;
        if (isValid) {
          result += number;
          System.out.print(number + ", ");
        } else {
        }
      }
      System.out.println("");
    }
    System.out.println(result);
  }

  public static ArrayList<ArrayList<Integer>> getNumbersInLine(String line) {
    ArrayList<ArrayList<Integer>> results = new ArrayList<>();

    int i = 0;
    while (i < line.length()) {
      Character ch = line.charAt(i);

      if (!(Character.isDigit(ch))) {
        i++;
        continue;
      }

      // found a digit
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(ch);
      int start_index, end_index;
      start_index = i;
      end_index = i;
      i++;

      while (i < line.length() && Character.isDigit(line.charAt(i))) {
        stringBuilder.append(line.charAt(i));
        i++;
        end_index++;
      }

      String result_string = stringBuilder.toString();
      Integer result_int = Integer.parseInt(result_string);
      ArrayList<Integer> result = new ArrayList<>();
      result.add(result_int);
      result.add(start_index);
      result.add(end_index);

      results.add(result);
    }

    return results;
  }
}
