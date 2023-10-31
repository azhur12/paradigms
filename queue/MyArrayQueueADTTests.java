package queue;

public class MyArrayQueueADTTests {
    public static void main(String[] args) {
        ArrayQueueADT queue1 = new ArrayQueueADT();
        ArrayQueueADT queue2 = new ArrayQueueADT();
        for (int i = 0; i < 5; i++) {
            ArrayQueueADT.enqueue(queue1,"e_1_" + (i + 1));
            ArrayQueueADT.enqueue(queue2, "e_2_" + (i + 1));
        }
        while (!ArrayQueueADT.isEmpty(queue1)) {
            System.out.println(ArrayQueueADT.dequeue(queue1) + " size: " + ArrayQueueADT.size(queue1));
        }

        while (!ArrayQueueADT.isEmpty(queue2)) {
            System.out.println(ArrayQueueADT.dequeue(queue2) + " size: " + ArrayQueueADT.size(queue2));
        }
    }
}
