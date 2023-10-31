package queue;

import java.util.Arrays;
import java.util.Objects;


//Model: a[0]...a[n - 1]
//Invariant: forall 0 <= i < n,  a[i] != null
public class ArrayQueueADT {
    private Object[] elements = new Object[5];
    private int tall;
    private int head;


    //Pred: element != null
    //Post: n' = n + 1 && immutable(n) && a'[n'] = element
    public static void enqueue(ArrayQueueADT queue, Object element) {
        Objects.requireNonNull(element);
        ensureCapacity(queue);
        queue.elements[queue.tall++] = element;
        if (queue.tall >= queue.elements.length) {
            queue.tall = queue.tall % queue.elements.length;
        }
    }

    private static void ensureCapacity(ArrayQueueADT queue) {
        if (queue.head == (queue.tall + 1) % queue.elements.length) {
            if (queue.tall + 1 == queue.elements.length) {
                queue.elements = Arrays.copyOf(queue.elements, queue.elements.length * 2);
            } else {
                Object[] result = new Object[queue.elements.length * 2];
                System.arraycopy(queue.elements, queue.head, result, 0, queue.elements.length - queue.head);
                System.arraycopy(queue.elements, 0, result, queue.elements.length - queue.head, queue.tall + 1);
                queue.head = 0;
                queue.tall = queue.elements.length - 1;
                queue.elements = result;
            }
        } else if (queue.tall == queue.elements.length) {
            queue.tall = 0;
        }
    }

    //Pred: n > 0
    //Post: R = a[0] && forall 0 <= i < n, a'[i] = a[i + 1] && immutable(1, n)
    public static Object dequeue(ArrayQueueADT queue) {
        assert queue.tall != queue.head;
        Object result = queue.elements[queue.head];
        queue.elements[queue.head] = null;
        queue.head++;
        if (queue.head >= queue.elements.length) {
            queue.head = queue.head % queue.elements.length;
        }
        return result;
    }

    //Pred: true
    //Post: R = a[0]
    public static Object element(ArrayQueueADT queue) {
        assert queue.tall != queue.head;
        return queue.elements[queue.head];
    }

    //Pred: true
    //Post: R = n
    public static int size(ArrayQueueADT queue) {
        if (queue.tall > queue.head) {
            return queue.tall - queue.head;
        } else if (queue.head > queue.tall) {
            return queue.tall + queue.elements.length - queue.head ;
        } else {
            return 0;
        }
    }

    //Pred: true
    //Post: R = (n == 0)
    public static boolean isEmpty(ArrayQueueADT queue) {
        return queue.tall == queue.head;
    }

    //Pred: true
    //Post: R = queue is cleared
    public static void clear(ArrayQueueADT queue) {
        queue.elements = new Object[5];
        queue.head = 0;
        queue.tall = 0;
    }

    //Pred: true
    //Post: massive with elements of queue in queue's order
    public static Object[] toArray(ArrayQueueADT queue) {
        Object[] result = new Object[size(queue)];
        int OrigIndex = queue.head;
        int CurIndex = 0;
        while (CurIndex < size(queue)) {
            result[CurIndex] = queue.elements[OrigIndex];
            CurIndex++;
            OrigIndex++;
            if (OrigIndex == queue.elements.length) {
                OrigIndex = 0;
            }
        }
        return result;
    }
}

