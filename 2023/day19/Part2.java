
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

    // construct the workflows
    int i = 0;
    HashMap<String, Workflow> workflows = new HashMap<>();
    while (true) {
      if (lines.get(i).length() == 0) {
        i++;
        break;
      }

      String line = lines.get(i);
      Workflow workflow = new Workflow(line);
      workflows.put(workflow.name, workflow);
      i++;
    }

    long res = 0;

    // for each workflow, we are going to figure out the range of values that can
    // lead us there

    System.out.println(res);
  }
}

class Rule {
  public String partName;
  public String operator;
  public Integer value;
  public String destination;

  public Rule(String line) {
    String[] splitLine = line.split(":");
    destination = splitLine[1];
    line = splitLine[0];

    if (line.contains("<")) {
      operator = "<";
      partName = line.split("<")[0];
      value = Integer.parseInt(line.split("<")[1]);
    } else {
      operator = ">";
      partName = line.split(">")[0];
      value = Integer.parseInt(line.split(">")[1]);
    }
  }
}

class Workflow {
  public ArrayList<Rule> rules;
  public String name;
  public String lastStep;

  public Workflow(String line) {
    rules = new ArrayList<>();
    name = line.substring(0, line.indexOf("{"));

    // substring to get rid of stuff we don't need
    line = line.substring(line.indexOf("{") + 1, line.indexOf("}"));
    String[] stringRules = line.split(",");
    for (int i = 0; i < stringRules.length; i++) {
      if (i == stringRules.length - 1) {
        lastStep = stringRules[i];
        continue;
      }

      rules.add(new Rule(stringRules[i]));
    }
  }

  public String processWorkflow(PartCollection pc) {
    for (Rule rule : rules) {
      if (rule.operator.equals(">")) {
        if (pc.partValues.get(rule.partName) > rule.value) {
          return rule.destination;
        }
      } else if (rule.operator.equals("<")) {
        if (pc.partValues.get(rule.partName) < rule.value) {
          return rule.destination;
        }
      }
    }

    return lastStep;
  }
}

class PartCollection {
  public HashMap<String, Integer> partValues;
  public Integer partSum;

  public PartCollection(String line) {
    line = line.replace("{", "");
    line = line.replace("}", "");

    partValues = new HashMap<>();

    String[] splitLine = line.split(",");
    partValues.put("x", Integer.parseInt(splitLine[0].split("=")[1]));
    partValues.put("m", Integer.parseInt(splitLine[1].split("=")[1]));
    partValues.put("a", Integer.parseInt(splitLine[2].split("=")[1]));
    partValues.put("s", Integer.parseInt(splitLine[3].split("=")[1]));

    partSum = 0;
    for (Integer val : partValues.values()) {
      partSum += val;
    }
  }
}
