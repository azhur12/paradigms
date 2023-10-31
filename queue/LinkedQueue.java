package queue;

import java.util.Objects;

public class LinkedQueue extends AbstractQueue {
    private Node head;
    private Node tall;
    private int size;

    private Node fictNode;

    //Pred: element != null
    //Post: n' = n + 1 && immutable(n) && a'[n'] = element
    public void enqueueImpl(Object element) {
        if (head == null) {
            head = new Node(element, null);
            tall = head;
        } else {
            Node newNode = new Node(element, null);
            tall.next = newNode;
            tall = newNode;
        }
        size++;
    }

    //Pred: n > 0
    //Post: R = a[0] && forall 0 <= i < n, a'[i] = a[i + 1] && immutable(1, n)
    public Object dequeueImpl() {
        Object result = head.value;
        head = head.next;
        size--;
        return result;
    }

    //Pred: true
    //Post: R = a[0]
    public Object elementImpl() {
        return head.value;
    }

    //Pred: true
    //Post: R = n
    public int sizeImpl() {
        return size;
    }

    //Pred: true
    //Post: R = (n == 0)
    public boolean isEmptyImpl() {

        return size == 0;
    }

    //Pred: true
    //Post: queue is cleared
    public void clearImpl() {
        this.size = 0;
        this.head = null;
        this.tall = null;
    }
    /*
    public Object[] toArray() {
        Object[] result = new Object[size];
        int index = 0;
        Node origHead = head;
        int origSize = size;
        while (size > 0) {
            result[index] = head.value;
            head = head.next;
            size--;
            index++;
        }
        head = origHead;
        size = origSize;
        return result;
    }

     */

    protected Object nextElement() {
        Object result = fictNode.value;
        fictNode = fictNode.next;
        return result;

    }

    protected void refreshFictiveIndex() {
        fictNode = head;
    }

    private class Node {
        private final Object value;
        private Node next;

        private Node(Object value, Node next) {
            this.value = value;
            this.next = next;
        }
    }

}


