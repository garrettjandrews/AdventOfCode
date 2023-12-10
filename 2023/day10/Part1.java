
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
        lines.add(line);
      }
    } catch (IOException e) {
      System.out.println(e);
      return;
    }

    Loop loop = new Loop(lines);
    loop.fillDistanceMap();
    System.out.println(loop.totalSteps / 2);
  }
}

class Loop {
  public int startRow;
  public int startCol;
  ArrayList<String> loopMap;
  ArrayList<ArrayList<Integer>> distances;
  Queue<int[]> loopSteps;
  int totalSteps;

  public Loop(ArrayList<String> map) {
    loopMap = map;
    totalSteps = 0;

    // find the indices of the starting point
    for (int i = 0; i < loopMap.size(); i++) {
      String line = loopMap.get(i);
      int poss_col;
      if ((poss_col = line.indexOf("S")) != -1) {
        startRow = i;
        startCol = poss_col;
        break;
      }
    }

    // create a mapping of all the distances from the starting point
    int numRows = loopMap.size();
    int numCols = loopMap.get(0).length();
    distances = new ArrayList<>();
    for (int i = 0; i < numRows; i++) {
      ArrayList<Integer> thisRow = new ArrayList<>();
      for (int j = 0; j < numCols; j++) {
        thisRow.add(-1);
      }
      distances.add(thisRow);
    }
    // the only change we have to make to the above is to start off by making the
    // 'S' position a 0
    editDistanceMap(startRow, startCol, 0);

    // add in the starting position to the queue
    loopSteps = new LinkedList<>();
    loopSteps.add(new int[] { startRow, startCol });
  }

  public void fillDistanceMap() {
    while (loopSteps.size() > 0) {
      takeStep();
    }
  }

  public void takeStep() {
    // pop from the queue
    int[] position = loopSteps.remove();
    totalSteps++;
    Character currChar = getLoopMapValue(position[0], position[1]);

    // figure out where we can go from here
    PipeMap pipeMap = new PipeMap();
    ArrayList<Character> possibleMoves = pipeMap.pipeMap.get(currChar);

    // foe each possible step, decide whether to take it in the future or not
    // by adding it to the queue if we need to go there still
    for (Character move : possibleMoves) {
      int nextRow = -1;
      int nextCol = -1;
      if (move == 'U') {
        nextRow = position[0] - 1;
        nextCol = position[1];
      }
      if (move == 'D') {
        nextRow = position[0] + 1;
        nextCol = position[1];
      }
      if (move == 'R') {
        nextRow = position[0];
        nextCol = position[1] + 1;
      }
      if (move == 'L') {
        nextRow = position[0];
        nextCol = position[1] - 1;
      }

      if (isValidSquare(nextRow, nextCol, move)) {
        loopSteps.add(new int[] { nextRow, nextCol });
      }
    }

    editDistanceMap(position[0], position[1], 0);
  }

  private boolean isValidSquare(int row, int col, Character move) {
    if ((row == -1) || (row >= loopMap.size())) {
      return false; // we went outside of the map
    }

    if ((col <= -1) || (col >= loopMap.get(0).length())) {
      return false; // we went outside of the map
    }

    if (getDistanceValue(row, col) != -1) {
      return false; // we have already been to this square
    }

    if (getLoopMapValue(row, col) == '.') {
      return false; // don't move onto the ground
    }

    Character piece = getLoopMapValue(row, col);
    PipeMap pipeMap = new PipeMap();
    Character reverseMove = 'X';
    if (move == 'U') {
      reverseMove = 'D';
    } else if (move == 'D') {
      reverseMove = 'U';
    } else if (move == 'L') {
      reverseMove = 'R';
    } else if (move == 'R') {
      reverseMove = 'L';
    }

    if (!(pipeMap.pipeMap.get(piece).contains(reverseMove))) {
      return false;
    }

    return true;
  }

  public void editDistanceMap(int row, int col, int newVal) {
    ArrayList<Integer> distanceRowToEdit = distances.get(row);
    distanceRowToEdit.set(col, newVal);
    distances.set(row, distanceRowToEdit);
  }

  public int getDistanceValue(int row, int col) {
    return distances.get(row).get(col);
  }

  public Character getLoopMapValue(int row, int col) {
    return loopMap.get(row).charAt(col);
  }

  public void printInfo() {
    System.out.println(loopMap);
    System.out.println("Starts at row " + startRow);
    System.out.println("Starts at col " + startCol);
  }
}

class PipeMap {
  public HashMap<Character, ArrayList<Character>> pipeMap;

  public PipeMap() {
    pipeMap = new HashMap<>();

    pipeMap.put('|', new ArrayList<Character>());
    pipeMap.get('|').add('U');
    pipeMap.get('|').add('D');

    pipeMap.put('-', new ArrayList<Character>());
    pipeMap.get('-').add('L');
    pipeMap.get('-').add('R');

    pipeMap.put('L', new ArrayList<Character>());
    pipeMap.get('L').add('U');
    pipeMap.get('L').add('R');

    pipeMap.put('J', new ArrayList<Character>());
    pipeMap.get('J').add('U');
    pipeMap.get('J').add('L');

    pipeMap.put('7', new ArrayList<Character>());
    pipeMap.get('7').add('D');
    pipeMap.get('7').add('L');

    pipeMap.put('F', new ArrayList<Character>());
    pipeMap.get('F').add('D');
    pipeMap.get('F').add('R');

    pipeMap.put('S', new ArrayList<Character>());
    pipeMap.get('S').add('U');
    pipeMap.get('S').add('D');
    pipeMap.get('S').add('L');
    pipeMap.get('S').add('R');
  }
}
