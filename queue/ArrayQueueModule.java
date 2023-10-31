package queue;

import java.util.Arrays;
import java.util.Objects;

//Model: a[0]...a[n - 1]
//Invariant: forall 0 <= i < n,  a[i] != null
public class ArrayQueueModule {
    private static Object[] elements = new Object[5];
    private static int tall;
    private static int head;

    //Pred: element != null
    //Post: n' = n + 1 && immutable(n) && a'[n'] = element
    public static void enqueue(Object element) {
        Objects.requireNonNull(element);
        ensureCapacity();
        elements[tall++] = element;
        if (tall >= elements.length) {
            tall = tall % elements.length;
        }
    }

    private static void ensureCapacity() {
        if (head == (tall + 1) % elements.length) {
            if (tall + 1 == elements.length) {
                elements = Arrays.copyOf(elements, elements.length * 2);
            } else {
                Object[] result = new Object[elements.length * 2];
                System.arraycopy(elements, head, result, 0, elements.length - head);
                System.arraycopy(elements, 0, result, elements.length - head, tall + 1);
                head = 0;
                tall = elements.length - 1;
                elements = result;
            }
        } else if (tall == elements.length) {
            tall = 0;
        }
    }

    //Pred: n > 0
    //Post: R = a[0] && forall 0 <= i < n, a'[i] = a[i + 1] && immutable(1, n)
    public static Object dequeue() {
        assert tall != head;
        Object result = elements[head];
        elements[head] = null;
        head++;
        if (head >= elements.length) {
            head = head % elements.length;
        }
        return result;
    }

    //Pred: true
    //Post: R = a[0]
    public static Object element() {
        assert tall != head;
        return elements[head];
    }

    //Pred: true
    //Post: R = n
    public static int size() {
        if (tall > head) {
            return tall - head;
        } else if (head > tall) {
            return tall + elements.length - head ;
        } else {
            return 0;
        }
    }

    //Pred: true
    //Post: R = (n == 0)
    public static boolean isEmpty() {
        return tall == head;
    }

    //Pred: true
    //Post: R = queue is cleared
    public static void clear() {
        elements = new Object[5];
        head = 0;
        tall = 0;
    }


    //Pred: true
    //Post: massive with elements of queue in queue's order
    public static Object[] toArray() {
        Object[] result = new Object[size()];
        int OrigIndex = head;
        int CurIndex = 0;
        while (CurIndex < size()) {
            result[CurIndex] = elements[OrigIndex];
            CurIndex++;
            OrigIndex++;
            if (OrigIndex == elements.length) {
                OrigIndex = 0;
            }
        }
        return result;
    }
}
