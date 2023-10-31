package queue;

public class MyTestsLinkedQueue {
    public static void main(String[] args) {
        LinkedQueue queuelink = new LinkedQueue();
        for (int i = 0; i < 6; i++) {
            queuelink.enqueue("e_" + i);
        }
        queuelink.dequeue();
        Object[] test = queuelink.toArray();
        for (Object o : test) {
            System.out.println(o);
        }
    }
}
