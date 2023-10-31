package search;


//cyclic shift is forall 0 <= i <= k a[i] < a[i + 1] until k, 0 <= k <= a.length, forall k < j < a.length, a[i] > a[j] for all i, j
//sorted by increasing is a[i] < a[i + 1] forall i, 0 <= i < a.length
public class BinarySearchMax {
    //Pred(P1): args.len > 0, args[0..arg.len-1] is numbers && args[0..arg.len-1] is sorted by increasing with cycling shift
    //Post: printed max(args[0...arg.len-1]
    public static void main(String[] args) {
        //Pred: P1
        int[] a = new int[args.length];
        int sum = 0;
        //Post: sum = 0 && a = args[0..arg.len-1]

        //Pred: True
        for (int i = 0; i < args.length; i++) {
            //Pred: 0 < i < args.length
            a[i] = Integer.parseInt(args[i]);
            //Post: a[i] = int(args[i])

            //Pred: sum + a[i] < Integer.Max_Value
            sum += a[i];
            //Post: sum = sum + a[i]
        }
        //Post: sum = summa(args) && a[i] = args[i] forall i, 0 <= i < a.length


        int result;

        //pred: sum == summa(args)
        if (sum % 2 == 0) {
            //pred: sum == summa(args) && sum % 2 == 0
            result = recursiveSearch(a, -1, a.length - 1);
            //post: recursive Search is launched
        } else {
            //pred: sum == summa(args) && sum % 2 != 0 && a is sorted by increasing with cyclic shift
            result = iterativeSearch(a);
            //Post: iterative Search is launched
        }
        //Post:  0 <= R < len(a) && a[R] == max(a_i) forall i, 0 <= i < a.length && result = R
        System.out.println(a[result]);
    }

    //Pred: a is sorted by increasing with cyclic shift
    //Post:  0 <= R < len(a) && a[R] == max(a_i) forall i, 0 <= i < a.length
    public static int iterativeSearch(int[] a) {
        //Pred: a is sorted by increasing with cyclic shift
        int left = -1;
        int right = a.length;
        //Post: left == -1 && right  == a.length

        //Pred: left < right
        while(right - left > 1) {
            //Pred: (right - left > 1)
            int mid = (right + left)/2;
            //Post: mid = (right + left)/2

            //Inv: left < mid < right && left <= R < right

            //Pred: 0 < mid < a.length
            if (a[mid] > a[a.length - 1]) {
                //Pred: 0 < mid < a.length && (a[mid] > a[a.length - 1])
                left = mid;
                //Post: left = mid
            } else {
                //Pred: 0 < mid < a.length && !(a[mid] > a[a.length - 1])
                right = mid;
                //Post: right = mid
            }
            //Post: -1 <= left <= R <= right <= a.length the array is sorted to a cyclic shift

        }
        //Post: (left + 1 >= right && left < right) && ((left = R && 0 <= R <= a.length - 1) || (left == -1))
        //Pred (P1): (left == R && (0 <= R <= a.length - 1)) || (left == -1)
        if (left == -1) {
            //Pred: left == -1 (This means that the array was not cyclically shifted and our algorithm,
            //assuming that a cyclic shift exists, moved to left edge
            left = a.length - 1;
            //Post: R = last index of array
        } else {
            //Pred: (left == R && (0 <= R <= a.length - 1)) cyclic shift is exist and we found it
            //Post: R = max(a)
        }
        //Post: R = max(a)


        return left;

    }
    //Pred: a is sorted with cycling shift && -1 <= left && right <= a.length && left < right
    //Post: //Post:  0 <= R < len(a) && a[R] == max(a_i) forall i, 0 <= i < a.length
    public static int recursiveSearch(int[] a, int left, int right) {
        //Pred a is sorted with cycling shift && -1 <= left && right <= a.length && left < right
        if (right - left <= 1) {
            //Pred: (left + 1 >= right && left < right) && ((left = R && 0 <= R <= a.length - 1) || (left == -1))
            if (left == -1) {
                //Pred: left == -1 (This means that the array was not cyclically shifted and our algorithm,
                //assuming that a cyclic shift exists, moved to left edge
                left = a.length - 1;
                //Post: R = last index of array
            } else {
                //Pred: (left == R && (0 <= R <= a.length - 1)) cyclic shift is exist and we found it
                //Post: R = max(a)
            }
            //Post: R = max(a)

            return left;
        }

        //Pred: left + right <= Integer.MAX_VALUE
        int mid = (right + left)/2;
        //Post: mid = (right + left)/2

        //Pred: 0 <= mid <= a.length
        if (a[mid] > a[a.length - 1]) {
            //Pred: 0 <= mid <= a.length && (a[mid] > a[a.length - 1]) && mid <= right <= a.length
            return recursiveSearch(a, mid,  right);
        } else {
            //Pred: 0 <= mid <= a.length && (a[mid] < a[a.length - 1]) && -1 <= left <= mid
            return recursiveSearch(a, left, mid);
        }

    }

}
