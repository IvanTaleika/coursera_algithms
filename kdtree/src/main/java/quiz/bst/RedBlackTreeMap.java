package quiz.bst;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedMap;

public class RedBlackTreeMap<K extends Comparable<K>, V> implements NavigableMap<K, V> {

  private class RbEntry implements Entry<K, V> {

    private final K key;
    private V value;
    RbEntry left;
    RbEntry right;
    boolean isRed;

    RbEntry(K key, V value) {
      this.key = key;
      this.value = value;
      isRed = true;
    }

    @Override
    public K getKey() {
      return key;
    }

    @Override
    public V getValue() {
      return value;
    }

    @Override
    public V setValue(V value) {
      V oldValue = this.value;
      this.value = value;
      return oldValue;
    }
  }

  private class Update {

    RbEntry node;
    final V oldValue;

    Update(RbEntry node, V oldValue) {
      this.node = node;
      this.oldValue = oldValue;
    }
  }

  private RbEntry rotateLeft(RbEntry node) {
    RbEntry tmp = node.right;
    node.right = tmp.left;
    tmp.left = node;
    tmp.isRed = node.isRed;
    node.isRed = true;
    return tmp;
  }

  private RbEntry rotateRight(RbEntry node) {
    RbEntry tmp = node.left;
    node.left = tmp.right;
    tmp.right = node;
    tmp.isRed = node.isRed;
    node.isRed = true;
    return tmp;
  }

  private void flipColors(RbEntry node) {
    node.isRed = true;
    node.left.isRed = false;
    node.right.isRed = false;
  }

  private RbEntry root;
  private int s;

  public RedBlackTreeMap() {
    s = 0;
    root = null;
  }

  @Override
  public Entry<K, V> lowerEntry(K key) {
    return null;
  }

  @Override
  public K lowerKey(K key) {
    return null;
  }

  @Override
  public Entry<K, V> floorEntry(K key) {
    return null;
  }

  @Override
  public K floorKey(K key) {
    return null;
  }

  @Override
  public Entry<K, V> ceilingEntry(K key) {
    return null;
  }

  @Override
  public K ceilingKey(K key) {
    return null;
  }

  @Override
  public Entry<K, V> higherEntry(K key) {
    return null;
  }

  @Override
  public K higherKey(K key) {
    return null;
  }

  @Override
  public Entry<K, V> firstEntry() {
    if (root == null) {
      return null;
    }
    RbEntry first = root;
    while (first.left != null) {
      first = first.left;
    }
    return first;
  }

  @Override
  public Entry<K, V> lastEntry() {
    if (root == null) {
      return null;
    }
    RbEntry first = root;
    while (first.right != null) {
      first = first.right;
    }
    return first;
  }

  @Override
  public Entry<K, V> pollFirstEntry() {
    return null;
  }

  @Override
  public Entry<K, V> pollLastEntry() {
    return null;
  }

  @Override
  public NavigableMap<K, V> descendingMap() {
    return null;
  }

  @Override
  public NavigableSet<K> navigableKeySet() {
    return null;
  }

  @Override
  public NavigableSet<K> descendingKeySet() {
    return null;
  }

  @Override
  public NavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
    return null;
  }

  @Override
  public NavigableMap<K, V> headMap(K toKey, boolean inclusive) {
    return null;
  }

  @Override
  public NavigableMap<K, V> tailMap(K fromKey, boolean inclusive) {
    return null;
  }

  @Override
  public Comparator<? super K> comparator() {
    return null;
  }

  @Override
  public SortedMap<K, V> subMap(K fromKey, K toKey) {
    return null;
  }

  @Override
  public SortedMap<K, V> headMap(K toKey) {
    return null;
  }

  @Override
  public SortedMap<K, V> tailMap(K fromKey) {
    return null;
  }

  @Override
  public K firstKey() {
    return firstEntry().getKey();
  }

  @Override
  public K lastKey() {
    return lastEntry().getKey();
  }

  @Override
  public int size() {
    return s;
  }

  @Override
  public boolean isEmpty() {
    return s == 0;
  }

  @Override
  public boolean containsKey(Object key) {
    return get(key) != null;
  }

  @Override
  public boolean containsValue(Object value) {
    for (V val : values()) {
      if (val.equals(value)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public V get(Object key) {
    K comparableKey = (K) key;
    RbEntry curr = root;
    while (curr != null) {
      int compareResult = comparableKey.compareTo(curr.key);
      if (compareResult == 0) {
        return curr.getValue();
      }
      if (compareResult < 0) {
        curr = curr.left;
      } else {
        curr = curr.right;
      }
    }
    return null;
  }

  @Override
  public V put(K key, V value) {
    Update update = put(root, key, value);
    if (update.node != null) {
      root = update.node;
    }
    root.isRed = false;
    return update.oldValue;
  }

  private Update put(RbEntry in, K key, V value) {
    if (in == null) {
      s++;
      return new Update(new RbEntry(key, value), null);
    }
    Update update;
    int compare = key.compareTo(in.key);
    if (compare < 0) {
      update = put(in.left, key, value);
      in.left = update.node;
    } else if (compare > 0) {
      update = put(in.right, key, value);
      in.right = update.node;
    } else {
      return new Update(in, in.setValue(value));
    }
    if (isRed(in.right) && !isRed(in.left)) {
      in = rotateLeft(in);
    }
    if (isRed(in.left) && isRed(in.left.left)) {
      in = rotateRight(in);
    }
    if (isRed(in.left) && isRed(in.right)) {
      flipColors(in);
    }
    update.node = in;
    return update;
  }

  @Override
  public V remove(Object key) {
    return null;
  }

  @Override
  public void putAll(Map<? extends K, ? extends V> m) {

  }

  @Override
  public void clear() {
    root = null;
    s = 0;
  }

  @Override
  public Set<K> keySet() {
    return null;
  }

  @Override
  public Collection<V> values() {
    return values(root);
  }

  private List<V> values(RbEntry node) {
    List<V> mapValues = new ArrayList<>(s);
    addValues(node, mapValues);
    return mapValues;
  }

  private void addValues(RbEntry node, List<V> mapValues) {
    if (node == null) {
      return;
    }
    addValues(node.left, mapValues);
    mapValues.add(node.getValue());
    addValues(node.right, mapValues);
  }

  @Override
  public Set<Entry<K, V>> entrySet() {
    return null;
  }

  private boolean isRed(RbEntry e) {
    return e != null && e.isRed;
  }

  public int depth() {
    return depth(root, 0, 0);
  }

  private int depth(RbEntry node, int currentDepth, int maxDepth) {
    if (node == null) {
      return maxDepth;
    }
    currentDepth++;
    if (currentDepth > maxDepth) {
      maxDepth = currentDepth;
    }
    maxDepth = depth(node.left, currentDepth, maxDepth);
    return depth(node.right, currentDepth, maxDepth);
  }

  public static void main(String[] args) {
    RedBlackTreeMap<Integer, Integer> map = new RedBlackTreeMap<>();
    int n = 1024;
    for (int i = 0; i < n; i++) {
      map.put(i, i);
    }
    System.out.println(map.size());
    System.out.println(map.depth());
    System.out.println(map.values());
  }
}
