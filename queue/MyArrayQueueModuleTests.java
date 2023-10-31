package queue;

public class MyArrayQueueModuleTests {
    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            ArrayQueueModule.enqueue("e_" + i);
        }
        while (!ArrayQueueModule.isEmpty()) {
            System.out.println(ArrayQueueModule.dequeue() + " size: " + ArrayQueueModule.size());
        }
    }
}
