package search;

//cyclic shift is forall 0 <= i <= k a[i] < a[i + 1] until k, 0 <= k <= a.length, forall k < j < a.length, a[i] > a[j] for all i, j
//sorted by increasing is a[i] < a[i + 1] forall i, 0 <= i < a.length
public class BinarySearch {
    //Pred: args.len > 0, args[1..arg.len-1] is numbers && args[1..arg.len-1] is sorted by nonincreasing
    public static void main(String[] args) {
        //Pred: args[0] is number
        int x = Integer.parseInt(args[0]);
        //Post: x = number

        //Pred: args.len > 0;
        int[] a = new int[args.length];
        //Post: a = new array;

        //Pred: a.len == args.len && args[1..arg.len-1] is numbers
        for (int i = 1; i < args.length; i++) {
            a[i-1] = Integer.parseInt(args[i]);
        }
        //Post: a[0...a.len - 2] is values

        //Pred: a.len == args.len
        a[a.length - 1] = Integer.MIN_VALUE;
        //Post: a[a.length - 1] = Min_Value

        //Pred: a.len > 0
        if (a.length == 1) {
            //Pred: a.len > 0 && a.len == 1
            System.out.println(0);
            //Post: 0 is printed
        } else {


            //Pred: a.len > 1 && a is sorted by nonincreasing
            int resultOfIter = iterativeSearch(x, a);
            //Post: result = index, a[index] <= x

            //Pred: a is sorted by nonincreasing, left = -1, right = a.length - 1
            int resultOfRecur = recursiveSearch(x, a, -1, a.length - 1);
            //Post: result = index, a[index] <= x


            System.out.println(resultOfIter);
        }
    }
    //Pred: a is sorted by nonincreasing
    //Post: result = index, a[index] <= x
    public static int iterativeSearch(int x, int[] a) {
        //Pred: True
        int left = -1;
        int right = a.length - 1;
        //Post: left == -1 && right == a.length;

        //Pred: left < right;
        while(right - left > 1) {
            //Pred: left + 1 < right;
            int mid = (right + left)/2;
            //Post: mid = (right + left)/2;

            //Inv: left < mid <= right
            if (a[mid] > x) {
                //Pred: Inv && a[mid] > x
                left = mid;
                //Post: a[left'] > x >= a[right]
            } else {
                //Pred: Inv && a[mid] <= x
                right = mid;
                //Post: a[left] > x >= a[right']
            }
            //Post: left < index <= right, index - гипотетический индекс, который хотим найти
        }
        //Post: Inv && right - left <= 1

        //Pred: right - left <= 1 --> index == right
        return right;
        //Post: result == right

    }
    //Pred: a is sorted by nonincreasing, left = -1, right = a.length - 1
    //Post: result = index, a[index] <= x
    public static int recursiveSearch(int x, int[] a, int left, int right) {


        //Inv: left < mid <= right;
        if (right - left <= 1) {  // -- condition
            //Inv && condition -> index == right, index - искомый индекс
            //Pred: right - left <= 1 --> a[right] >= x
            return right;
        }
        //Pred: left < right;
        int mid = (right + left)/2;
        //Post: mid = (right + left)/2;

        //Pred: Inv
        if (a[mid] > x) { // -- condition
            //Pred: Inv && condition
            return recursiveSearch(x, a, mid,  right);
        } else {
            //Pred: Inv && !condition
            return recursiveSearch(x, a, left, mid);
        }
        //Post: result == index || index doesn't exist
    }
}
