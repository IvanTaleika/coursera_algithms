package quiz.bst;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class DocumentSearch {

  private int m;
  private Map<String, Integer> queryWords;

  private class Interval {

    private final long start;
    private final long end;
    private final long length;

    Interval(long s, long e) {
      start = s;
      end = e;
      length = end - start + 1;
    }
  }

  public DocumentSearch(String[] keys) {
    m = keys.length;
    if (m < 2) {
      throw new IllegalArgumentException("At least 2 query words required");
    }
    queryWords = new HashMap<>();
    for (int i = 0; i < keys.length; i++) {
      queryWords.put(keys[i], i);
    }
  }

  private long search(Iterator<String> document) {
    Map<Integer, Interval> searchTree = new RedBlackTreeMap<>();
//    Map<Integer, Interval> searchTree = new TreeMap<>();

    long pos = 0;
    long fullIntervalLength = Long.MAX_VALUE;

    while (document.hasNext()) {
      String word = document.next();
      pos++;
      Integer queryWordPos = queryWords.get(word);
      if (queryWordPos != null) {
        if (queryWordPos == 0) {
          searchTree.put(0, new Interval(pos, pos));
        } else {
          Interval floorInterval = searchTree.get(queryWordPos - 1);
          if (floorInterval != null) {
            Interval newInterval = new Interval(floorInterval.start, pos);
            if (queryWordPos == m - 1 && newInterval.length < fullIntervalLength) {
              fullIntervalLength = newInterval.length;
            } else {
              searchTree.put(queryWordPos, newInterval);
            }
          }
        }
      }
    }
    return fullIntervalLength != Long.MAX_VALUE ? fullIntervalLength : -1;
  }


  public static void main(String[] args) throws FileNotFoundException {
    String fileName = "documentSearchTest/noFullSequence.txt";
    String[] queryWords = {"a", "b", "c", "d"};
    DocumentSearch documentSearch = new DocumentSearch(queryWords);
    try (Scanner scanner = new Scanner(new File(fileName))) {
      long interval = documentSearch.search(scanner);
      System.out.println(interval);
    }
  }

}
