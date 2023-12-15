
import java.util.*;
import java.io.*;

public class Part1 {
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
        String[] values = line.split(",");
        for (String val : values) {
          lines.add(val);
        }
      }
    } catch (IOException e) {
      System.out.println(e);
      return;
    }

    long res = 0;
    for (String s : lines) {
      res += hashValue(s);
    }
    System.out.println(res);
  }

  private static long hashValue(String line) {
    long res = 0;
    for (int i = 0; i < line.length(); i++) {
      Character ch = line.charAt(i);
      res = hashChar((int) res, ch);
    }
    return res;
  }

  private static long hashChar(int start, char c) {
    long res = start + (int) c;
    res *= 17;
    res = res % 256;
    return res;
  }
}
