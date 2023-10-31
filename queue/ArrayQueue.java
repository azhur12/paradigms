package queue;

import java.util.Arrays;

//Model: a[0]...a[n - 1]
//Invariant: forall 0 <= i < n,  a[i] != null
public class ArrayQueue extends AbstractQueue {
    private Object[] elements = new Object[5];
    private int tall;
    private int head;

    private int fictIndex = head;


    //Pred: element != null
    //Post: n' = n + 1 && immutable(n) && a'[n'] = element
    public void enqueueImpl(Object element) {
        ensureCapacity();
        this.elements[this.tall++] = element;
        if (this.tall >= this.elements.length) {
            this.tall = this.tall % this.elements.length;
        }
    }

    private void ensureCapacity() {
        if (this.head == (this.tall + 1) % this.elements.length) {
            if (this.tall + 1 == this.elements.length) {
                this.elements = Arrays.copyOf(this.elements, this.elements.length * 2);
            } else {
                Object[] result = new Object[this.elements.length * 2];
                System.arraycopy(this.elements, this.head, result, 0, this.elements.length - this.head);
                System.arraycopy(this.elements, 0, result, this.elements.length - this.head, this.tall + 1);
                this.head = 0;
                this.tall = this.elements.length - 1;
                this.elements = result;
            }
        } else if (this.tall == this.elements.length) {
            this.tall = 0;
        }
    }

    //Pred: n > 0
    //Post: R = a[0] && forall 0 <= i < n, a'[i] = a[i + 1] && immutable(1, n)
    public Object dequeueImpl() {
        Object result = this.elements[this.head];
        this.elements[this.head] = null;
        this.head++;
        if (this.head >= this.elements.length) {
            this.head = this.head % this.elements.length;
        }
        return result;
    }

    //Pred: true
    //Post: R = a[0]
    public Object elementImpl() {
        return this.elements[this.head];
    }

    //Pred: true
    //Post: R = n
    public int sizeImpl() {
        if (this.tall > this.head) {
            return this.tall - this.head;
        } else if (this.head > this.tall) {
            return this.tall + this.elements.length - this.head ;
        } else {
            return 0;
        }
    }

    //Pred: true
    //Post: R = (n == 0)
    public boolean isEmptyImpl() {
        return this.tall == this.head;
    }

    //Pred: true
    //Post: queue is cleared
    public void clearImpl() {
        this.elements = new Object[5];
        this.head = 0;
        this.tall = 0;
    }

    //Pred: true
    //Post: massive with elements of queue in queue's order
    /*
    public Object[] toArray() {
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

     */

    @Override
    protected Object nextElement() {
        Object result = elements[fictIndex];
        fictIndex = (fictIndex + 1) % elements.length;
        return result;
    }

    protected void refreshFictiveIndex() {
        fictIndex = head;
    }
}

