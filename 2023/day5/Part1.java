import java.util.*;
import java.io.*;
import java.sql.Array;

public class Part1 {
  public static void main(String[] args) {
    if (args.length != 1) {
      System.out.println("Usage: <input_filepathe");
      return;
    }

    String input_path = args[0];
    BufferedReader br;
    String line;
    ArrayList<String> lines = new ArrayList<>();
    try {
      br = new BufferedReader(new FileReader(input_path));
      while ((line = br.readLine()) != null) {
        lines.add(line);
      }
    } catch (IOException e) {
      System.out.println(e);
    }

    // now that we have our lines, we need to sort our data into seeds and maps
    String seedLine = lines.get(0);
    ArrayList<Long> seeds = new ArrayList<>();
    seedLine = seedLine.replace("seeds: ", "");
    String[] stringSeeds = seedLine.split(" ");
    for (String s : stringSeeds) {
      seeds.add(Long.parseLong(s));
    }

    // now that we have seeds, helps us to organize our lines into higher level maps
    ArrayList<List<String>> maps = new ArrayList<>();
    List<String> remaining_lines = lines.subList(2, lines.size());
    int index_to_split_from = 1;

    // build a list of list of the maps that we are going to iterate through
    for (int i = 0; i < remaining_lines.size(); i++) {
      if (remaining_lines.get(i).length() > 0) {
        continue;
      }
      maps.add(remaining_lines.subList(index_to_split_from, i));
      index_to_split_from = i + 2;
    }
    // make sure to add in the last map too
    index_to_split_from = remaining_lines.size() - 1;
    for (int i = remaining_lines.size() - 1; i > -1; i--) {
      if (Character.isDigit(remaining_lines.get(i).charAt(0))) {
        index_to_split_from--;
      } else {
        maps.add(remaining_lines.subList(index_to_split_from + 1, remaining_lines.size()));
        break;
      }
    }

    long result = -1;

    // loop through seeds
    for (Long seed : seeds) {
      long location = getLocation(seed, maps);
      if ((result == -1) || (location < result)) {
        result = location;
      }
    }

    System.out.println(result);
  }

  public static long getLocation(Long seed, ArrayList<List<String>> maps) {
    Long result = seed;

    for (List<String> map : maps) {
      result = getValueFromMap(result, map);
    }
    return result;
  }

  public static long getValueFromMap(Long seed, List<String> map) {
    for (String line : map) {
      String[] stringDigits = line.split(" ");
      ArrayList<Long> digits = new ArrayList<>();

      for (String s : stringDigits) {
        digits.add(Long.parseLong(s));
      }

      if ((seed >= digits.get(1)) && (seed < digits.get(1) + digits.get(2))) {
        return seed + (digits.get(0) - digits.get(1));
      }

    }
    return seed;
  }
}
