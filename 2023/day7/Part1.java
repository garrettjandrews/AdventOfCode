
import java.util.*;
import java.io.*;
import java.net.SocketTimeoutException;

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

    // create list of objects
    ArrayList<Hand> hands = new ArrayList<>();
    for (int i = 0; i < lines.size(); i++) {
      String line = lines.get(i);
      Hand hand = new Hand(line, i);
      hands.add(hand);
    }

    HandComparator handComparator = new HandComparator();
    Collections.sort(hands, handComparator);
    for (int i = 0; i < hands.size(); i++) {
      Hand hand = hands.get(i);
      hand.rank = i + 1;
    }

    long result = 0;
    for (Hand hand : hands) {
      result += (long) hand.rank * (long) hand.bid;
    }

    System.out.println(result);
  }

}

class HandComparator implements Comparator<Hand> {
  @Override
  public int compare(Hand handOne, Hand handTwo) {
    for (int i = 0; i < 6; i++) {
      if (handOne.sortingArray.get(i) == handTwo.sortingArray.get(i)) {
        continue;
      }
      return Integer.compare(handOne.sortingArray.get(i), handTwo.sortingArray.get(i));
    }
    return -1;
  }
}

class Hand {
  public int rank;
  public String hand;
  public Integer bid;
  public int hand_type;
  public HashMap<Character, Integer> CardRanks;
  public ArrayList<Integer> sortingArray;

  public static HashMap<Character, Integer> initCardRanks() {

    HashMap<Character, Integer> CardRanks = new HashMap<>();
    CardRanks.put('A', 13);
    CardRanks.put('K', 12);
    CardRanks.put('Q', 11);
    CardRanks.put('J', 10);
    CardRanks.put('T', 9);
    CardRanks.put('9', 8);
    CardRanks.put('8', 7);
    CardRanks.put('7', 6);
    CardRanks.put('6', 5);
    CardRanks.put('5', 4);
    CardRanks.put('4', 3);
    CardRanks.put('3', 2);
    CardRanks.put('2', 1);

    return CardRanks;
  }

  public Hand(String line, int index) {
    String[] splitLine = line.split(" ");
    hand = splitLine[0];
    rank = index;
    bid = Integer.parseInt(splitLine[1]);
    hand_type = handType(hand);
    CardRanks = initCardRanks();

    sortingArray = new ArrayList<>();
    sortingArray.add(hand_type);
    for (int i = 0; i < hand.length(); i++) {
      Character ch = hand.charAt(i);
      sortingArray.add(CardRanks.get(ch));
    }
  }

  public static int handType(String hand) {
    // construct a hashmap of the cards we have
    HashMap<Character, Integer> cardCounts = new HashMap<>();

    for (int i = 0; i < hand.length(); i++) {
      Character ch = hand.charAt(i);
      Integer currCount = cardCounts.getOrDefault(ch, 0);
      cardCounts.put(ch, currCount + 1);
    }

    ArrayList<Integer> counts = new ArrayList<>();
    for (Integer count : cardCounts.values()) {
      counts.add(count);
    }

    // now that we have counts, we can determine what sort of hand we have
    // instead of storing a hashmap with the string values, we will output an
    // integer value that can easily be compared
    // Five of a Kind = 7
    // Four of a Kind = 6
    // Full House = 5
    // Three of a kind = 4
    // Two Pair = 3
    // One Pair = 2
    // High Card = 1
    if (Collections.max(counts).equals(5)) {
      return 7;
    }

    if (Collections.max(counts).equals(4)) {
      return 6;
    }

    if ((counts.contains(3)) && (counts.contains(2))) {
      return 5;
    }

    if (Collections.max(counts).equals(3)) {
      return 4;
    }

    int numTwos = 0;
    for (Integer i : counts) {
      if (i == 2) {
        numTwos++;
      }
    }
    if (numTwos == 2) {
      return 3;
    }
    if (numTwos == 1) {
      return 2;
    }

    return 1;
  }
}
