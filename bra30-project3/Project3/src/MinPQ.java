/******************************************************************************
 *  Compilation:  javac MinPQ.java
 *  Execution:    java MinPQ < input.txt
 *  Dependencies: StdIn.java StdOut.java
 *  Data files:   http://algs4.cs.princeton.edu/24pq/tinyPQ.txt
 *  
 *  Generic min priority queue implementation with a binary heap.
 *  Can be used with a comparator instead of the natural order.
 *
 *  % java MinPQ < tinyPQ.txt
 *  E A E (6 left on pq)
 *
 *  We use a one-based array to simplify parent and child calculations.
 *
 *  Can be optimized by replacing full exchanges with half exchanges
 *  (ala insertion sort).
 *
 ******************************************************************************/
import java.util.NoSuchElementException;

/**

 */
public class MinPQ{
    private Car[] pq;                    // store items at indices 1 to n
    private int n;                       // number of items on priority queue
    private char type;                   // p for price heap or m for mileage heap 
    private int arraySize = 911;         // 911 is a decently large prime number
    /**
     * Initializes an empty priority queue with the given initial capacity.
     *
     * @param  initCapacity the initial capacity of this priority queue
     */
    public MinPQ(int initCapacity, char t) {
        pq = new Car[initCapacity + 1];
        n = 0;
        type = t;
    }

    /**
     * Initializes an empty priority queue.
     */
    public MinPQ() {
        this(1, 'p');   //default to a price heap of size 1
    }
    
    public MinPQ(char t){
        this(1,t);
    }

    /**
     * Initializes a priority queue from the array of keys.
     * <p>
     * Takes time proportional to the number of keys, using sink-based heap construction.
     *
     * @param  keys the array of keys
     */
    public MinPQ(Car[] keys, TrieST trie) {
        this(keys, trie, 'p');
    }
    
    public MinPQ(Car[] keys, TrieST trie, char t) {
        n = keys.length;
        type = t;
        pq = new Car[keys.length + 1];
        for (int i = 0; i < n; i++)
            pq[i+1] = keys[i];
        for (int k = n/2; k >= 1; k--)
            sink(k, trie);
        assert isMinHeap();
    }

    /**
     * Returns true if this priority queue is empty.
     *
     * @return {@code true} if this priority queue is empty;
     *         {@code false} otherwise
     */
    public boolean isEmpty() {
        return n == 0;
    }

    /**
     * Returns the number of keys on this priority queue.
     *
     * @return the number of keys on this priority queue
     */
    public int size() {
        return n;
    }

    /**
     * Returns a smallest key on this priority queue.
     *
     * @return a smallest key on this priority queue
     * @throws NoSuchElementException if this priority queue is empty
     */
    public Car min() {
        if (isEmpty()) throw new NoSuchElementException("Priority queue underflow");
        return pq[1];
    }

    // helper function to double the size of the heap array
    private void resize(int capacity) {
        assert capacity > n;
        Car[] temp = new Car[capacity];
        for (int i = 1; i <= n; i++) {
            temp[i] = pq[i];
        }
        pq = temp;
    }

    /**
     * Adds a new key to this priority queue.
     *
     * @param  x the key to add to this priority queue
     */
    public void insert(Car x, TrieST trie) {
        // double size of array if necessary
        if (n == pq.length - 1) resize(2 * pq.length);

        // add x, and percolate it up to maintain heap invariant
        pq[++n] = x;
        swim(n, trie);
        assert isMinHeap();
    }

    /**
     * Removes and returns a smallest key on this priority queue.
     *
     * @return a smallest key on this priority queue
     * @throws NoSuchElementException if this priority queue is empty
     */
    public Car delMin(TrieST trie) {
        if (isEmpty()) throw new NoSuchElementException("Priority queue underflow");
        exch(1, n);
        Car min = pq[n--];
        sink(1, trie);
        pq[n+1] = null;         // avoid loitering and help with garbage collection
        if ((n > 0) && (n == (pq.length - 1) / 4)) resize(pq.length  / 2);
        assert isMinHeap();
        return min;
    }
    
    public Car getCarAtIndex(int i){
        return pq[i];
    }
    
    public void restoreHeapInvariant(int index, TrieST trie){
        swim(index, trie);
        sink(index, trie);
        assert isMinHeap();
    }

   /***************************************************************************
    * Helper functions to restore the heap invariant.
    ***************************************************************************/

    private void swim(int k, TrieST trie) {
        trie.put(pq[k].getVIN(), k);
        while (k > 1 && greater(k/2, k)) {
            exch(k, k/2);
            trie.put(pq[k/2].getVIN(), k/2);
            trie.put(pq[k].getVIN(), k);
            k = k/2;
        }
    }

    private void sink(int k, TrieST trie) {
        trie.put(pq[k].getVIN(), k);
        while (2*k <= n) {
            int j = 2*k;
            if (j < n && greater(j, j+1)) j++;
            if (!greater(k, j)) break;
            exch(k, j);
            trie.put(pq[k].getVIN(), k);
            trie.put(pq[j].getVIN(), j);
            k = j;
        }
    }

   /***************************************************************************
    * Helper functions for compares and swaps.
    ***************************************************************************/
    private boolean greater(int i, int j) {
            //boolean result;
            //System.out.println("i: " + pq[i] + "\n" + "j: " + pq[j]);
            //result = (pq[i]).compareTo(pq[j], type) > 0;
            //System.out.println("i is greater than j: " + result + "\n");
            //return result;
            return (pq[i]).compareTo(pq[j], type) > 0;
    }

    private void exch(int i, int j) {
        Car swap = pq[i];
        pq[i] = pq[j];
        pq[j] = swap;
    }
 
    // is pq[1..N] a min heap?
    private boolean isMinHeap() {
        return isMinHeap(1);
    }

    // is subtree of pq[1..n] rooted at k a min heap?
    private boolean isMinHeap(int k) {
        if (k > n) return true;
        int left = 2*k;
        int right = 2*k + 1;
        if (left  <= n && greater(k, left))  return false;
        if (right <= n && greater(k, right)) return false;
        return isMinHeap(left) && isMinHeap(right);
    }
    
    
    public void pukeOutArray(){
        for(int i = 1; i < pq.length; i++){
            System.out.println(pq[i]);
        }
    }
}
