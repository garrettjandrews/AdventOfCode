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
    HashMap<String, Integer> numbers = new HashMap<>();
    ArrayList<Integer> results = new ArrayList<>();
    while (true) {
      try {
        line = br.readLine();
        if (line == null) {
          break;
        }

        numbers.put("first", -1);
        numbers.put("second", -1);
        // if the line wasn't null, then we start scanning through the line and finding
        // numbers as we can
        for (int i = 0; i < line.length(); i++) {
          if (Character.isDigit(line.charAt(i))) {
            int digit = line.charAt(i) - '0';
            if (numbers.get("first") == -1) {
              numbers.put("first", digit);
              continue;
            }

            numbers.put("second", digit);
          }
        }

        // now that we have broken out of the for loop, have to consider two cases: we
        // have one digit or two digits, then we concatenate them
        int line_result;
        if (numbers.get("second") == -1) {
          line_result = numbers.get("first") * 10 + numbers.get("first");
        } else {
          line_result = numbers.get("first") * 10 + numbers.get("second");
        }

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
}
