import java.util.*;
import java.io.*;

public class Part2 {
  public static void main(String[] args) {
    if (args.length != 1) {
      System.out.println("Usage: <input_file>");
      return;
    }

    String input_path = args[0];
    System.out.println(input_path);

    int result = 0; // stores the result
    BufferedReader br;
    String line;

    try {
      br = new BufferedReader(new FileReader(input_path));
      while ((line = br.readLine()) != null) {
        result += processLine(line);
      }
    } catch (IOException e) {
      System.out.println(e);
      return;
    }

    System.out.println(result);
  }

  // returns the game index if the game was valid
  public static int processLine(String line) {
    System.out.println(line);
    line = line.replace("Game ", "");

    // initialize a hashmap containing the maximum values for each of the colors
    HashMap<String, Integer> max_values = new HashMap<>();
    max_values.put("green", 0);
    max_values.put("red", 0);
    max_values.put("blue", 0);

    // now that we have the game index, we can substring the string just to make our
    // lives a bit easier.

    line = line.substring(line.indexOf(":") + 2);
    while ((line.indexOf(",") != -1) || (line.indexOf(";") != -1)) {
      int commaIndex = line.indexOf(",");
      int semicolonIndex = line.indexOf(";");

      String st;
      int selectedIndex;
      if (commaIndex == -1) {
        selectedIndex = semicolonIndex;
        st = line.substring(0, semicolonIndex);
      } else if (semicolonIndex == -1) {
        selectedIndex = commaIndex;
        st = line.substring(0, commaIndex);
      } else {
        selectedIndex = Math.min(commaIndex, semicolonIndex);
        st = line.substring(0, selectedIndex);
      }

      // now that we have the string to consider, subset the line
      line = line.substring(selectedIndex + 2);

      // now we handle this particular cube amount
      int cubeAmount = Integer.parseInt(st.substring(0, st.indexOf(" ")));
      String cubeColor = st.substring(st.indexOf(" ") + 1, st.length());

      if (cubeAmount > max_values.get(cubeColor)) {
        max_values.put(cubeColor, cubeAmount);
      }
    }

    int cubeAmount = Integer.parseInt(line.substring(0, line.indexOf(" ")));
    String cubeColor = line.substring(line.indexOf(" ") + 1, line.length());

    if (cubeAmount > max_values.get(cubeColor)) {
      max_values.put(cubeColor, cubeAmount);
    }
    System.out.println(max_values.get("blue") * max_values.get("red") * max_values.get("green"));
    return max_values.get("blue") * max_values.get("red") * max_values.get("green");
  }
}
