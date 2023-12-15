
import java.util.*;
import java.util.stream.Collectors;
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
        String[] values = line.split(",");
        for (String val : values) {
          lines.add(val);
        }
      }
    } catch (IOException e) {
      System.out.println(e);
      return;
    }

    // construct a hashmap to hold the results
    HashMap<Integer, LensBox> boxes = new HashMap<>();

    // start handling lenses
    for (String line : lines) {
      // case when there is an equals sign in the string
      if (-1 != line.indexOf("=")) {
        // split the line
        String[] splitLine = line.split("=");
        Lens lens = new Lens(splitLine[0], Integer.valueOf(splitLine[1]));

        // figure out which box to put the lens in
        Integer boxNum = hashValue(lens.label);

        // if the box exists, then just add the lens to the end of the arraylist for the
        // box
        // otherwise, we will have to make a new box
        if (boxes.containsKey(boxNum)) {
          boxes.get(boxNum).addLens(lens);
        } else {
          LensBox newBox = new LensBox(lens, boxNum);
          boxes.put(boxNum, newBox);
        }
        continue;
      }

      line = line.replace("-", "");
      Integer boxNum = hashValue(line);

      // check to see if the box exists. if it does, try to remove the label
      if (boxes.containsKey(boxNum)) {
        boxes.get(boxNum).tryRemoveLabel(line);
      }
    }

    // iterate through the hashmap to get the final result
    long result = 0;
    for (Map.Entry<Integer, LensBox> entry : boxes.entrySet()) {
      LensBox lensBox = entry.getValue();
      result += lensBox.totalFocusingPower();
    }
    System.out.println(result);
  }

  private static Integer hashValue(String line) {
    Integer res = 0;
    for (int i = 0; i < line.length(); i++) {
      Character ch = line.charAt(i);
      res = hashChar((int) res, ch);
    }
    return res;
  }

  private static Integer hashChar(int start, char c) {
    Integer res = start + (int) c;
    res *= 17;
    res = res % 256;
    return res;
  }
}

class Lens {
  public String label;
  public Integer focalLength;

  public Lens(String label, Integer focalLength) {
    this.label = label;
    this.focalLength = focalLength;
  }
}

class LensBox {
  public List<Lens> lenses;
  public Integer boxNumber;

  public LensBox(Lens lens, Integer boxN) {
    lenses = new ArrayList<>();
    lenses.add(lens);

    boxNumber = boxN;
  }

  public void addLens(Lens lens) {
    // first check to see if the label is already in there
    for (Lens existingLens : lenses) {
      if (existingLens.label.equals(lens.label)) {
        existingLens.focalLength = lens.focalLength;
        return;
      }
    }

    // if the label wasn't in there, add to the end
    lenses.add(lens);
  }

  public void tryRemoveLabel(String l) {
    int indexToRemove = -1;
    for (int i = 0; i < lenses.size(); i++) {
      Lens lens = lenses.get(i);
      if (lens.label.equals(l)) {
        indexToRemove = i;
        break;
      }
    }

    if (indexToRemove != -1) {
      lenses.remove(indexToRemove);
    }
  }

  public long totalFocusingPower() {
    if (lenses.size() == 0) {
      return 0;
    }

    long boxVal = boxNumber + 1;
    long result = 0;

    for (int i = 0; i < lenses.size(); i++) {
      result += (boxVal * (i + 1) * (lenses.get(i).focalLength));
    }

    return result;
  }

  public void getInfo() {
    System.out.println("Box number " + boxNumber);
    for (Lens lens : lenses) {
      System.out.println("[" + lens.label + " " + lens.focalLength + "]");
    }
  }
}
