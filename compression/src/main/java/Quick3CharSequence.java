import edu.princeton.cs.algs4.StdRandom;

// princeton String sort implementation generalized for CharSequence so that it can be used with constant-time
// subSequence method provided by StringBuffer or custom char[] wrapper
public class Quick3CharSequence {

    private static final int CUTOFF = 15;   // cutoff to insertion sort

    // do not instantiate
    private Quick3CharSequence() {
    }

    /**
     * Rearranges the array of CharSequences in ascending order.
     *
     * @param a the array to be sorted
     */
    public static void sort(CharSequence[] a) {
        StdRandom.shuffle(a);
        sort(a, 0, a.length - 1, 0);
    }

    // return the dth character of s, -1 if d = length of s
    private static int charAt(CharSequence s, int d) {
        assert d >= 0 && d <= s.length();
        if (d == s.length()) {
            return -1;
        }
        return s.charAt(d);
    }


    // 3-way CharSequence quicksort a[lo..hi] starting at dth character
    private static void sort(CharSequence[] a, int lo, int hi, int d) {

        // cutoff to insertion sort for small subarrays
        if (hi <= lo + CUTOFF) {
            insertion(a, lo, hi, d);
            return;
        }

        int lt = lo, gt = hi;
        int v = charAt(a[lo], d);
        int i = lo + 1;
        while (i <= gt) {
            int t = charAt(a[i], d);
            if (t < v) {
                exch(a, lt++, i++);
            } else if (t > v) {
                exch(a, i, gt--);
            } else {
                i++;
            }
        }

        // a[lo..lt-1] < v = a[lt..gt] < a[gt+1..hi]. 
        sort(a, lo, lt - 1, d);
        if (v >= 0) {
            sort(a, lt, gt, d + 1);
        }
        sort(a, gt + 1, hi, d);
    }

    // sort from a[lo] to a[hi], starting at the dth character
    private static void insertion(CharSequence[] a, int lo, int hi, int d) {
        for (int i = lo; i <= hi; i++) {
            for (int j = i; j > lo && less(a[j], a[j - 1], d); j--) {
                exch(a, j, j - 1);
            }
        }
    }

    // exchange a[i] and a[j]
    private static void exch(CharSequence[] a, int i, int j) {
        CharSequence temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    // is v less than w, starting at character d
    private static boolean less(CharSequence v, CharSequence w, int d) {
        for (int i = d; i < Math.min(v.length(), w.length()); i++) {
            if (v.charAt(i) < w.charAt(i)) {
                return true;
            }
            if (v.charAt(i) > w.charAt(i)) {
                return false;
            }
        }
        return v.length() < w.length();
    }
}
