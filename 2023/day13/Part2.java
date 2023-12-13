
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

    long res = 0;
    // build pattern objects
    ArrayList<String> tempLines = new ArrayList<>();
    for (int i = 0; i < lines.size(); i++) {
      String line = lines.get(i);

      if (line.length() > 0) {
        tempLines.add(line);
      }

      if ((line.length() == 0) || (i == lines.size() - 1)) {
        Pattern pattern = new Pattern(tempLines);
        tempLines.clear();
        res += pattern.getReflectionValue();
        continue;
      }
    }

    System.out.println(res);
  }
}

class Pattern {
  public ArrayList<String> lines;

  public Pattern(ArrayList<String> lines) {
    this.lines = new ArrayList<>();
    for (String line : lines) {
      String newLine = line;
      this.lines.add(newLine);
    }

  }

  public Integer getReflectionValue() {
    Integer reflectionValue = -1;
    if ((reflectionValue = verticalReflection()) != -1) {
      // System.out.println("Found vertical reflection at " + reflectionValue);
      return reflectionValue;
    }

    if ((reflectionValue = horizontalReflection()) != -1) {
      // System.out.println("Found horizontal reflection at " + reflectionValue);
      return reflectionValue * 100;
    }

    System.out.println("No valid reflection found");
    return reflectionValue;
  }

  private boolean IsValidVerticalReflection(int LeftCols) {
    Integer maxLength = lines.get(0).length();

    if ((LeftCols <= 0) || (LeftCols >= maxLength)) {
      return false;
    }

    int charsOff = 0;
    for (String line : lines) {
      String left = line.substring(0, LeftCols);
      String right = line.substring(LeftCols);
      left = reverseString(left);

      int i = 0;
      while (true) {
        if ((i >= left.length()) || (i >= right.length())) {
          break;
        }

        if (left.charAt(i) != right.charAt(i)) {
          charsOff++;
        }
        i++;
      }
    }

    return charsOff == 1;
  }

  private String reverseString(String s) {
    if (s.length() == 0) {
      return "";
    }

    return reverseString(s.substring(1)) + s.charAt(0);
  }

  private Integer verticalReflection() {
    for (int i = 0; i < lines.get(0).length(); i++) {
      if (IsValidVerticalReflection(i)) {
        return i;
      }
    }
    return -1;
  }

  private Integer horizontalReflection() {
    for (int i = 1; i < lines.size(); i++) {
      if (IsValidHorizontalReflection(i)) {
        return i;
      }
    }
    return -1;
  }

  // changing this function to return true only if the reflection is one off
  private boolean IsValidHorizontalReflection(int TopRows) {
    if ((TopRows <= 0) || (TopRows >= lines.size())) {
      return false;
    }

    List<String> top = lines.subList(0, TopRows);
    List<String> bot = lines.subList(TopRows, lines.size());

    // reverse the top so it's easier to reason about

    int i = 0;
    int charsOff = 0;
    while (true) {
      if ((i > top.size() - 1) || (i > bot.size() - 1)) {
        break;
      }

      String topLine = top.get(top.size() - 1 - i);
      String botLine = bot.get(i);
      charsOff += charsOff(topLine, botLine);
      i++;
    }

    return charsOff == 1;
  }

  public int charsOff(String s1, String s2) {
    int i = 0;
    int res = 0;
    while (true) {
      if ((i >= s1.length()) || (i >= s2.length())) {
        break;
      }

      if (s1.charAt(i) != s2.charAt(i)) {
        res++;
      }

      i++;
    }

    return res;
  }
}
