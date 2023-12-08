
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

    ArrayList<Character> instructions = new ArrayList<>();
    String thisLine;
    int i = 0;
    while ((thisLine = lines.get(i++)).length() > 0) {
      for (Character c : thisLine.toCharArray()) {
        instructions.add(c);
      }
    }

    HashMap<String, Node> nodeMap = new HashMap<>();
    String firstNodeVal;

    for (; i < lines.size(); i++) {
      Node node = new Node(lines.get(i));
      nodeMap.put(node.node, node);
    }

    // now loop through the results
    int steps = 0;
    String currNode = "AAA";
    int instructionCount = 0;
    while (!(currNode.equals("ZZZ"))) {
      steps++;
      char inst = instructions.get(instructionCount++ % instructions.size());
      if (inst == 'L') {
        currNode = nodeMap.get(currNode).left;
        continue;
      }

      if (inst == 'R') {
        currNode = nodeMap.get(currNode).right;
        continue;
      }

      System.out.println("Error: invalid instruction");
    }

    System.out.println(steps);
  }
}

class Node {
  public String node;
  public String left;
  public String right;

  public Node(String line) {
    node = line.substring(0, 3);
    left = line.substring(7, 10);
    right = line.substring(12, 15);
  }

  public void whoAmI() {
    System.out.println("Node " + node);
    System.out.println("Left " + left);
    System.out.println("Right " + right);
  }
}
