import java.util.*;
import java.util.concurrent.RecursiveTask;
import java.io.*;
import java.sql.Array;

public class Part2 {
  public static void main(String[] args) {
    if (args.length != 2) {
      System.out.println("Usage: <input_filepath> <step_size>");
      return;
    }
    if (args[0] == "-h") {
      System.out.println(
          "Adjust step size according to the size of the input file.  If it runs for too long without counting down, restart with a smaller step.");
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
    ArrayList<Long> sourceSeeds = new ArrayList<>();
    seedLine = seedLine.replace("seeds: ", "");
    String[] stringSeeds = seedLine.split(" ");
    for (String s : stringSeeds) {
      sourceSeeds.add(Long.parseLong(s));
    }

    // for part 2, we add an additional step to make ranges of seeds
    ArrayList<List<Long>> seedRanges = new ArrayList<>();
    for (int i = 0; i < sourceSeeds.size(); i += 2) {
      seedRanges.add(sourceSeeds.subList(i, i + 2));
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

    // now going to convert the maps to int lists
    ArrayList<List<List<Long>>> numberMaps = new ArrayList<>();
    for (List<String> map : maps) {
      ArrayList<List<Long>> numberMap = new ArrayList<>();
      for (String l : map) {
        String[] stringDigits = l.split(" ");
        ArrayList<Long> nl = new ArrayList<>();
        for (String s : stringDigits) {
          nl.add(Long.parseLong(s));
        }
        numberMap.add(nl);
      }
      numberMaps.add(numberMap);
    }

    // loop through seeds
    // for (List<Long> range : seedRanges) {
    // for (long i = range.get(0); i < range.get(0) + range.get(1); i++) {
    // System.out.println("Working on seed " + i);
    // long location = getLocation(i, numberMaps);
    // if ((result == -1) || (location < result)) {
    // result = location;
    // }
    // }
    // }
    long location = 0;
    boolean seed_found = false;
    while (true) {
      System.out.println("Searching for location " + location);
      long this_seed = getSeedFromLocation(location, numberMaps);

      // check if this_seed is in our ranges. If it is, break.
      for (List<Long> range : seedRanges) {
        if ((this_seed >= range.get(0)) && (this_seed < range.get(0) + range.get(1))) {
          seed_found = true;
          break;
        }
      }

      if (seed_found) {
        System.out.println("Found it!");
        break;
      }
      location += Long.parseLong(args[1]);
    }

    // now that we found a minimum, go backwards to find the true minimum
    long result = location;
    for (long i = 0; i <= Long.parseLong(args[1]); i++) {
      location--;
      System.out.println("Searching for location " + location);
      long this_seed = getSeedFromLocation(location, numberMaps);

      // check if this_seed is in our ranges. If it is, break.
      seed_found = false;
      for (List<Long> range : seedRanges) {
        if ((this_seed >= range.get(0)) && (this_seed < range.get(0) + range.get(1))) {
          seed_found = true;
          break;
        }
      }

      if (seed_found && location < result) {
        result = location;
      }
    }

    System.out.println(result);
  }

  public static long getSeedFromLocation(long location, ArrayList<List<List<Long>>> maps) {
    for (int i = maps.size() - 1; i >= 0; i--) {
      location = reverseMapValue(location, maps.get(i));
    }
    return location;
  }

  public static long reverseMapValue(long value, List<List<Long>> map) {
    for (List<Long> mapline : map) {
      if ((value >= mapline.get(0)) && (value < mapline.get(0) + mapline.get(2))) {
        return value + mapline.get(1) - mapline.get(0);
      }
    }
    return value;
  }

  public static long getLocation(Long seed, ArrayList<List<List<Long>>> maps) {
    Long result = seed;

    for (List<List<Long>> map : maps) {
      result = getValueFromMap(result, map);
    }
    return result;
  }

  public static long getValueFromMap(Long seed, List<List<Long>> map) {
    for (List<Long> digits : map) {
      if ((seed >= digits.get(1)) && (seed < digits.get(1) + digits.get(2))) {
        return seed + (digits.get(0) - digits.get(1));
      }

    }
    return seed;
  }
}
