
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

    // turn each line into a History object
    int result = 0;
    for (String line : lines) {
      History history = new History(line);
      result += history.predictNextValue();
    }
    System.out.println(result);
  }
}

class History {
  public ArrayList<Integer> values;

  public History(String line) {
    values = new ArrayList<>();
    String[] splitLine = line.split(" ");
    for (int i = splitLine.length - 1; i >= 0; i--) {
      values.add(Integer.parseInt(splitLine[i]));
    }
  }

  public Integer predictNextValue() {
    if (endReached(values)) {
      return 0;
    }
    ArrayList<ArrayList<Integer>> changeArray = new ArrayList<>();
    changeArray.add(values);
    while (true) {
      ArrayList<Integer> thisArr = new ArrayList<>();
      ArrayList<Integer> priorArr = changeArray.get(changeArray.size() - 1);

      for (int i = 1; i < priorArr.size(); i++) {
        thisArr.add(priorArr.get(i) - priorArr.get(i - 1));
      }

      changeArray.add(thisArr);

      if ((thisArr.size() == 1) || (endReached(thisArr))) {
        break;
      }
    }

    Integer res = 0;
    for (int i = changeArray.size() - 1; i >= 0; i--) {
      ArrayList<Integer> thisArr = changeArray.get(i);
      res += thisArr.get(thisArr.size() - 1);
    }
    return res;
  }

  private static boolean endReached(ArrayList<Integer> vals) {
    for (Integer val : vals) {
      if (!(val.equals(0))) {
        return false;
      }
    }
    return true;
  }
}
