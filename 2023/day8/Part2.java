
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

    ArrayList<Character> instructions = new ArrayList<>();
    String thisLine;
    int i = 0;
    while ((thisLine = lines.get(i++)).length() > 0) {
      for (Character c : thisLine.toCharArray()) {
        instructions.add(c);
      }
    }

    HashMap<String, Node> nodeMap = new HashMap<>();

    for (; i < lines.size(); i++) {
      Node node = new Node(lines.get(i));
      nodeMap.put(node.node, node);
    }

    // build a valid starting map (where the last digit is A)
    ArrayList<Node> nodesStartingWithA = new ArrayList<>();
    for (Node node : nodeMap.values()) {
      if (node.node.charAt(2) == 'A') {
        nodesStartingWithA.add(node);
      }
    }
    NodeSet nodes = new NodeSet(nodesStartingWithA, nodeMap);
    System.out.println("Starting with this many nodes: " + nodes.nodes.size());

    // now loop through the results
    long steps = 1;
    ArrayList<Long> shortestSteps = nodes.shortestSteps(instructions);

    System.out.println(LCM(shortestSteps));
  }

  public static long gcd(long a, long b) {
    while (b > 0) {
      long tmp = b;
      b = a % b;
      a = tmp;
    }
    return a;
  }

  public static long lcm(long a, long b) {
    return a * (b / gcd(a, b));
  }

  public static long LCM(ArrayList<Long> numbers) {
    long result = numbers.get(0);
    for (int i = 1; i < numbers.size(); i++) {
      result = lcm(result, numbers.get(i));
    }
    return result;
  }

}

class NodeSet {
  public ArrayList<Node> nodes;
  public HashMap<String, Node> nodeMap;

  public NodeSet(ArrayList<Node> startingNodes, HashMap<String, Node> nm) {
    nodes = startingNodes;
    nodeMap = nm;
  }

  public ArrayList<Long> shortestSteps(ArrayList<Character> instructions) {
    ArrayList<Long> result = new ArrayList<>();
    for (Node node : nodes) {
      result.add(node.stepsUntilZ(instructions, nodeMap));
    }
    return result;
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

  public long stepsUntilZ(ArrayList<Character> instructions, HashMap<String, Node> nodeMap) {
    long res = 0;
    String curr = node;
    int i = 0;

    while (curr.charAt(2) != 'Z') {
      res++;
      Character inst = instructions.get(i++ % instructions.size());
      if (inst == 'L') {
        curr = nodeMap.get(curr).left;
        continue;
      }

      if (inst == 'R') {
        curr = nodeMap.get(curr).right;
        continue;
      }
    }

    return res;
  }
}
