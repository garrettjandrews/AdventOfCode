
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

    Galaxy galaxy = new Galaxy(lines);

    System.out.println(galaxy.sumOfPairwiseDistances());
  }
}

class Galaxy {
  public ArrayList<String> image;
  public ArrayList<int[]> galaxies;
  public ArrayList<Integer> expansionRows;
  public ArrayList<Integer> expansionCols;

  public Galaxy(ArrayList<String> lines) {
    // figure out where to expand rows
    expansionRows = new ArrayList<>();
    expansionCols = new ArrayList<>();
    for (int i = 0; i < lines.get(0).length(); i++) {
      expansionCols.add(i);
    }

    for (int row = 0; row < lines.size(); row++) {
      String line = lines.get(row);
      if (-1 == line.indexOf("#")) {
        expansionRows.add(row);
      }

      for (int col = 0; col < line.length(); col++) {
        if (line.charAt(col) == '#') {
          expansionCols.remove(Integer.valueOf(col));
        }
      }
    }

    image = new ArrayList<>();
    for (int row = 0; row < lines.size(); row++) {
      String line = lines.get(row);
      String lineToAdd = "";
      for (int col = 0; col < line.length(); col++) {
        lineToAdd += Character.toString(line.charAt(col));
      }
      image.add(lineToAdd);
    }

    // also going to build the list of galaxies
    galaxies = new ArrayList<>();
    for (int row = 0; row < image.size(); row++) {
      String line = image.get(row);
      for (int col = 0; col < line.length(); col++) {
        if ('#' == line.charAt(col)) {
          galaxies.add(new int[] { row, col });
        }
      }
    }
  }

  public long sumOfPairwiseDistances() {
    long res = 0;
    for (int x = 0; x < galaxies.size() - 1; x++) {
      for (int y = x + 1; y < galaxies.size(); y++) {
        res += pairwiseDistance(galaxies.get(x), galaxies.get(y));
      }
    }
    return res;
  }

  public long pairwiseDistance(int[] x, int[] y) {
    int min_horizontal, min_vertical, max_horizontal, max_vertical;
    if (x[0] < y[0]) {
      min_vertical = x[0];
      max_vertical = y[0];
    } else {
      min_vertical = y[0];
      max_vertical = x[0];
    }

    if (x[1] < y[1]) {
      min_horizontal = x[1];
      max_horizontal = y[1];
    } else {
      min_horizontal = y[1];
      max_horizontal = x[1];
    }

    long h_dist, v_dist;
    h_dist = 0;
    v_dist = 0;
    for (int i = min_horizontal + 1; i <= max_horizontal; i++) {
      if (expansionCols.contains(i)) {
        h_dist += 1000000;
      } else {
        h_dist++;
      }
    }
    for (int i = min_vertical + 1; i <= max_vertical; i++) {
      if (expansionRows.contains(i)) {
        v_dist += 1000000;
      } else {
        v_dist++;
      }
    }
    return h_dist + v_dist;
  }

  public void printImage() {
    for (String line : image) {
      System.out.println(line);
    }
  }

  public void printGalaxyLocations() {
    for (int[] position : galaxies) {
      System.out.println(position[0] + "," + position[1]);
    }
  }

  public void printExpansions() {
    System.out.println(expansionRows);
    System.out.println(expansionCols);
  }
}
