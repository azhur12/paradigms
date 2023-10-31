package queue;

public interface Queue {
    //Pred: element != null
    //Post: n' = n + 1 && immutable(n) && a'[n'] = element
    void enqueue(Object element);
    //Pred: n > 0
    //Post: R = a[0] && forall 0 <= i < n, a'[i] = a[i + 1] && immutable(1, n)
    Object dequeue();
    //Pred: true
    //Post: R = a[0]
    Object element();
    //Pred: true
    //Post: R = n
    int size();
    //Pred: true
    //Post: R = (n == 0)
    boolean isEmpty();
    //Pred: true
    //Post: queue is cleared
    void clear();
    //Pred: true
    //Post: massive with elements of queue in queue's order
    Object[] toArray();

    //Pred: Ob != null;
    //Post: first index of ob || -1

    int indexOf(Object ob);

    //Pred: Ob != null;
    //Post: last index of ob || -1

    int lastIndexOf(Object ob);

}
