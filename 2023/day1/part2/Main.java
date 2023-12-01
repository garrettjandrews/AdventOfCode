import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {
  public static void main(String[] args) {
    File input_file = new File("./input.txt");
    BufferedReader br;
    try {
      br = new BufferedReader(new FileReader(input_file));
    } catch (FileNotFoundException e) {
      System.out.println(e);
      return;
    }
    String line;

    // set up a hashmap to store results
    ArrayList<Integer> results = new ArrayList<>();
    while (true) {
      try {
        line = br.readLine();
        if (line == null) {
          break;
        }

        int line_result = processLine(line);
        results.add(line_result);

      } catch (IOException e) {
        break;
      }
    }
    // now that we have all results, we can add on the array and print the result
    int sum = 0;
    for (int elem : results) {
      sum += elem;
    }
    System.out.println(sum);

  }

  public static int processLine(String line) {
    Integer[] first = new Integer[2];
    Integer[] second = new Integer[2];

    first[0] = -1;
    second[0] = -1;
    first[1] = -1;
    second[1] = -1;

    for (int i = 0; i < line.length(); i++) {
      if (Character.isDigit(line.charAt(i))) {
        int digit = line.charAt(i) - '0';

        if ((first[1] == -1) || (i < first[1])) {
          first[0] = digit;
          first[1] = i;
          continue;
        }

        second[0] = digit;
        second[1] = i;
      }
    }

    if (second[1] == -1) {
      second[0] = first[0];
      second[1] = first[1];
    }
    // second step will be to look at the strings
    HashMap<String, Integer> strings = new HashMap<>();
    strings.put("one", 1);
    strings.put("two", 2);
    strings.put("three", 3);
    strings.put("four", 4);
    strings.put("five", 5);
    strings.put("six", 6);
    strings.put("seven", 7);
    strings.put("eight", 8);
    strings.put("nine", 9);

    for (HashMap.Entry<String, Integer> entry : strings.entrySet()) {
      String s = entry.getKey();
      Integer i = entry.getValue();

      int line_index;
      if ((line_index = line.indexOf(s)) != -1) {
        if ((first[1] == -1) || (line_index < first[1])) {
          first[0] = i;
          first[1] = line_index;
        }
      }

      if ((line_index = line.lastIndexOf(s)) != -1) {
        if ((second[1] == -1) || (line_index > second[1])) {
          second[0] = i;
          second[1] = line_index;
        }
      }
    }
    int res = first[0] * 10 + second[0];
    if (second[1] == -1) {
      res = first[0] * 10 + first[0];
    }
    System.out.println(line + " : " + res);
    return res;
  }
}
