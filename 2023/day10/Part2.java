
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

    Loop loop = new Loop(lines);
    loop.drawLoop();
    System.out.println(loop.enclosedCount());
  }
}

class Loop {
  public int startRow;
  public int startCol;
  ArrayList<String> loopMap;
  ArrayList<String> originalLoopMap;
  Queue<int[]> loopSteps;

  public Loop(ArrayList<String> map) {
    loopMap = map;

    originalLoopMap = new ArrayList<>();
    for (String s : map) {
      originalLoopMap.add(s);
    }

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

    // add in the starting position to the queue
    loopSteps = new LinkedList<>();
    loopSteps.add(new int[] { startRow, startCol });
  }

  public void printLoop() {
    for (String line : loopMap) {
      System.out.println(line);
    }
  }

  public int enclosedCount() {
    replaceS();
    int res = 0;
    for (int i = 0; i < originalLoopMap.size(); i++) {
      for (int j = 0; j < originalLoopMap.get(0).length(); j++) {
        if (isEnclosed(i, j)) {
          res++;
        }
      }
    }
    return res;
  }

  public boolean isEnclosed(int row, int col) {
    if (getLoopMapValue(row, col) != '.') {
      return false;
    }

    ArrayList<Character> patterns = new ArrayList<>();
    patterns.add('J');
    patterns.add('L');
    patterns.add('|');

    String originalLine = originalLoopMap.get(row);
    String line = loopMap.get(row);
    int vertCount = 0;
    for (int i = 0; i < col; i++) {
      Character originalChar = originalLine.charAt(i);
      Character ch = line.charAt(i);

      if ((ch == 'X') && (patterns.contains(originalChar))) {
        vertCount++;
      }
    }

    return vertCount % 2 != 0;
  }

  public void replaceS() {
    Character replacementChar = 'X';
    Character up = getOriginalValue(startRow - 1, startCol);
    Character down = getOriginalValue(startRow + 1, startCol);
    Character left = getOriginalValue(startRow, startCol - 1);
    Character right = getOriginalValue(startRow, startCol + 1);
    boolean u = ((up == '|') || (up == '7') || (up == 'F'));
    boolean r = ((right == '-') || (right == '7') || (right == 'J'));
    boolean l = ((left == '-') || (left == 'F') || (left == 'L'));
    boolean d = ((down == '|') || (down == 'L') || (down == 'J'));

    if (l && r) {
      replacementChar = '-';
    }
    if (l && u) {
      replacementChar = 'J';
    }
    if (l && d) {
      replacementChar = '7';
    }
    if (r && u) {
      replacementChar = 'L';
    }
    if (r && d) {
      replacementChar = 'F';
    }
    if (u && d) {
      replacementChar = '|';
    }

    String line = originalLoopMap.get(startRow);
    line = line.substring(0, startCol) + replacementChar + line.substring(startCol + 1);
    originalLoopMap.set(startRow, line);

    System.out.println("Replaced S with " + replacementChar);
  }

  public Character getOriginalValue(int row, int col) {
    return originalLoopMap.get(row).charAt(col);
  }

  public void drawLoop() {
    while (loopSteps.size() > 0) {
      takeStep();
    }
    for (int i = 0; i < loopMap.size(); i++) {
      String line = loopMap.get(i);
      for (int j = 0; j < line.length(); j++) {
        if (line.charAt(j) != 'X') {
          line = line.substring(0, j) + '.' + line.substring(j + 1);
        }
      }
      loopMap.set(i, line);
    }
  }

  public void takeStep() {
    // pop from the queue
    int[] position = loopSteps.remove();
    Character currChar = getLoopMapValue(position[0], position[1]);

    // figure out where we can go from here
    PipeMap pipeMap = new PipeMap();
    ArrayList<Character> possibleMoves = pipeMap.pipeMap.get(currChar);
    if (possibleMoves == null) {
      editLoopMap(position[0], position[1], 'X');
      return;
    }

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

    editLoopMap(position[0], position[1], 'X');
  }

  private boolean isValidSquare(int row, int col, Character move) {
    if ((row == -1) || (row >= loopMap.size())) {
      return false; // we went outside of the map
    }

    if ((col <= -1) || (col >= loopMap.get(0).length())) {
      return false; // we went outside of the map
    }

    Character piece = getLoopMapValue(row, col);
    PipeMap pipeMap = new PipeMap();

    if (piece == 'X') {
      return false;
    }
    if (!(pipeMap.pipeMap.containsKey(piece))) {
      return false;
    }

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

  public void editLoopMap(int row, int col, char ch) {
    String rowToChange = loopMap.get(row);
    rowToChange = rowToChange.substring(0, col) + ch + rowToChange.substring(col + 1);
    loopMap.set(row, rowToChange);
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
