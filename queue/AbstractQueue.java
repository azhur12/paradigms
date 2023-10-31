package queue;

public abstract class AbstractQueue implements Queue {
    public void enqueue(Object element) {
        assert element != null;
        enqueueImpl(element);
        refresh();
    }

    abstract void enqueueImpl(Object element);

    public Object dequeue() {
        assert size() > 0;
        Object result = dequeueImpl();
        refresh();
        return result;
    }
    abstract Object dequeueImpl();

    public Object element() {
        assert size() > 0;
        return elementImpl();
    }

    abstract Object elementImpl();

    public int size() {
        return sizeImpl();
    }
    abstract int sizeImpl();

    @Override
    public boolean isEmpty() {
        return isEmptyImpl();
    }

    abstract boolean isEmptyImpl();

    public void clear() {
        clearImpl();
        refresh();
    }

    abstract void clearImpl();

    private void refresh() {
        refreshFictiveIndex();
    }
    abstract void refreshFictiveIndex();

    public Object[] toArray() {
        int queueSize = size();
        Object[] result = new Object[queueSize];
        for (int i = 0; i < queueSize; i++) {
            result[i] = nextElement();
        }
        refresh();
        return result;
    }
    //Pred: Ob != null;
    //Post: first index of ob || -1
    public int indexOf(Object ob) {
        //assert size() > 0;
        int index = 0;
        while (index < size()) {
            if (nextElement().equals(ob)) {
                refresh();
                return index;
            }
            index++;
        }
        refresh();
        return -1;
    }

    //Pred: Ob != null;
    //Post: last index of ob || -1

    public int lastIndexOf(Object ob) {
        //assert size() > 0;
        int index = 0;
        int result = -1;
        while (index < size()) {
            if (nextElement().equals(ob)) {
                result = index;
            }
            index++;
        }
        refresh();
        return result;
    }

    abstract Object nextElement();
}
