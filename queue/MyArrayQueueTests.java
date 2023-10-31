package queue;

public class MyArrayQueueTests {
    public static void main(String[] args) {
        ArrayQueue queue = new ArrayQueue();
        queue.enqueue(new String[]{"b"});
        queue.enqueue(new String[]{"b"});
        Object[] result = queue.toArray();
        for(Object o : result) {
            System.out.println(o.toString());
        }
        /*
        ArrayQueue queue1 = new ArrayQueue();
        ArrayQueue queue2 = new ArrayQueue();
        for (int i = 0; i < 5; i++) {
            queue1.enqueue("e_1_" + (i + 1));
            queue2.enqueue( "e_2_" + (i + 1));
        }
        while (!queue1.isEmpty()) {
            System.out.println(queue1.dequeue() + " size: " + queue1.size());
        }

        while (!queue2.isEmpty()) {
            System.out.println(queue2.dequeue() + " size: " + queue2.size());
        }

         */
    }
}
