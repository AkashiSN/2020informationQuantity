package s4.B181863; // ここは、かならず、自分の名前に変えよ。

import java.lang.*;
import s4.specification.*;

/*package s4.specification;
  ここは、１回、２回と変更のない外部仕様である。
  public interface FrequencerInterface {     // This interface provides the design for frequency counter.
  void setTarget(byte  target[]); // set the data to search.
  void setSpace(byte  space[]);  // set the data to be searched target from.
  int frequency(); //It return -1, when TARGET is not set or TARGET's length is zero
  //Otherwise, it return 0, when SPACE is not set or SPACE's length is zero
  //Otherwise, get the frequency of TAGET in SPACE
  int subByteFrequency(int start, int end);
  // get the frequency of subByte of taget, i.e target[start], taget[start+1], ... , target[end-1].
  // For the incorrect value of START or END, the behavior is undefined.
  }
*/

public class Frequencer implements FrequencerInterface {
    // Code to start with: This code is not working, but good start point to work.
    byte[] myTarget;
    byte[] mySpace;
    boolean targetReady = false;
    boolean spaceReady = false;

    int[] suffixArray; // Suffix Arrayの実装に使うデータの型をint []とせよ。

    // The variable, "suffixArray" is the sorted array of all suffixes of mySpace.
    // Each suffix is expressed by a integer, which is the starting position in
    // mySpace.

    // The following is the code to print the contents of suffixArray.
    // This code could be used on debugging.

    // この関数は、デバッグに使ってもよい。mainから実行するときにも使ってよい。
    // リポジトリにpushするときには、mainメッソド以外からは呼ばれないようにせよ。
    //
    private void printSuffixArray() {
        if (spaceReady) {
            for (int i = 0; i < mySpace.length; i++) {
                int s = suffixArray[i];
                System.out.printf("suffixArray[%2d]=%2d:", i, s);
                for (int j = s; j < mySpace.length; j++) {
                    System.out.write(mySpace[j]);
                }
                System.out.write('\n');
            }
        }
    }

    private int suffixCompare(int m, int n) {
        // suffixCompareはソートのための比較メソッドである。
        // 次のように定義せよ。
        //
        // comparing two suffixes by dictionary order.
        // suffix_i is a string starting with the position i in "byte [] mySpace".
        // When mySpace is "ABCD", suffix_0 is "ABCD", suffix_1 is "BCD",
        // suffix_2 is "CD", and sufffix_3 is "D".
        // Each i and j denote suffix_i, and suffix_j.
        // Example of dictionary order
        // "i" < "o" : compare by code
        // "Hi" < "Ho" ; if head is same, compare the next element
        // "Ho" < "Ho " ; if the prefix is identical, longer string is big
        //
        // The return value of "int suffixCompare" is as follows.
        // if suffix_i > suffix_j, it returns 1
        // if suffix_i < suffix_j, it returns -1
        // if suffix_i = suffix_j, it returns 0;

        // ここにコードを記述せよ
        //

        byte[] suffix_m = subBytes(mySpace, m, mySpace.length);
        byte[] suffix_n = subBytes(mySpace, n, mySpace.length);

        return compareTwoString(suffix_m, suffix_n);
    }

    byte[] subBytes(byte[] x, int start, int end) {
        // corresponding to substring of String for byte[],
        // It is not implement in class library because internal structure of byte[]
        // requires copy.
        byte[] result = new byte[end - start];
        for (int i = 0; i < end - start; i++) {
            result[i] = x[start + i];
        }
        ;
        return result;
    }

    int compareTwoString(byte[] a, byte[] b) {
        int i = 0;
        while (i < a.length && i < b.length) {
            if (a[i] > b[i])
                return 1;
            if (a[i] < b[i])
                return -1;
            i++;
        }
        if (a.length > b.length)
            return 1;
        if (a.length < b.length)
            return -1;
        return 0;
    }

    public void setSpace(byte[] space) {
        // suffixArrayの前処理は、setSpaceで定義せよ。
        mySpace = space;
        if (mySpace.length > 0)
            spaceReady = true;
        // First, create unsorted suffix array.
        suffixArray = new int[space.length];
        // put all suffixes in suffixArray.
        for (int i = 0; i < space.length; i++) {
            suffixArray[i] = i; // Please note that each suffix is expressed by one integer.
        }
        //
        // ここに、int suffixArrayをソートするコードを書け。
        // もし、mySpace が"ABC"ならば、
        // suffixArray = { 0, 1, 2} となること求められる。
        // このとき、printSuffixArrayを実行すると
        // suffixArray[ 0]= 0:ABC
        // suffixArray[ 1]= 1:BC
        // suffixArray[ 2]= 2:C
        // のようになるべきである。
        // もし、mySpace が"CBA"ならば
        // suffixArray = { 2, 1, 0} となることが求めらる。
        // このとき、printSuffixArrayを実行すると
        // suffixArray[ 0]= 2:A
        // suffixArray[ 1]= 1:BA
        // suffixArray[ 2]= 0:CBA
        // のようになるべきである。
        merge_sort(suffixArray, 0, space.length - 1);
    }

    // merge()
    private void merge(int arr[], int l, int m, int r) {
        // Find sizes of two subarrays to be merged
        int n1 = m - l + 1;
        int n2 = r - m;

        /* Create temp arrays */
        int L[] = new int[n1];
        int R[] = new int[n2];

        /* Copy data to temp arrays */
        for (int i = 0; i < n1; ++i)
            L[i] = arr[l + i];
        for (int j = 0; j < n2; ++j)
            R[j] = arr[m + 1 + j];

        /* Merge the temp arrays */

        // Initial indexes of first and second subarrays
        int i = 0, j = 0;

        // Initial index of merged subarry array
        int k = l;
        while (i < n1 && j < n2) {
            // if (L[i] <= R[j]) {
            if (suffixCompare(L[i], R[j]) == -1 || suffixCompare(L[i], R[j]) == 0) {
                arr[k] = L[i];
                i++;
            } else {
                arr[k] = R[j];
                j++;
            }
            k++;
        }

        /* Copy remaining elements of L[] if any */
        while (i < n1) {
            arr[k] = L[i];
            i++;
            k++;
        }

        /* Copy remaining elements of R[] if any */
        while (j < n2) {
            arr[k] = R[j];
            j++;
            k++;
        }
    }

    // mergeのsort()
    private void merge_sort(int arr[], int l, int r) {
        if (l < r) {
            // Find the middle point
            int m = (l + r) / 2;

            // Sort first and second halves
            merge_sort(arr, l, m);
            merge_sort(arr, m + 1, r);

            // Merge the sorted halves
            merge(arr, l, m, r);
        }
    }
    // ここから始まり、指定する範囲までは変更してはならないコードである。

    public void setTarget(byte[] target) {
        myTarget = target;
        if (myTarget.length > 0)
            targetReady = true;
    }

    public int frequency() {
        if (targetReady == false)
            return -1;
        if (spaceReady == false)
            return 0;
        return subByteFrequency(0, myTarget.length);
    }

    public int subByteFrequency(int start, int end) {
        // start, and end specify a string to search in myTarget,
        // if myTarget is "ABCD",
        // start=0, and end=1 means string "A".
        // start=1, and end=3 means string "BC".
        // This method returns how many the string appears in my Space.
        //
        /*
         * This method should be work as follows, but much more efficient. int
         * spaceLength = mySpace.length; int count = 0; for(int offset = 0; offset<
         * spaceLength - (end - start); offset++) { boolean abort = false; for(int i =
         * 0; i< (end - start); i++) { if(myTarget[start+i] != mySpace[offset+i]) {
         * abort = true; break; } } if(abort == false) { count++; } }
         */
        // The following the counting method using suffix array.
        // 演習の内容は、適切なsubByteStartIndexとsubByteEndIndexを定義することである。
        int first = subByteStartIndex(start, end);
        int last1 = subByteEndIndex(start, end);
        return last1 - first;
    }
    // 変更してはいけないコードはここまで。

    private int targetCompare(int i, int j, int k) {
        // subByteStartIndexとsubByteEndIndexを定義するときに使う比較関数。
        // 次のように定義せよ。
        // suffix_i is a string starting with the position i in "byte [] mySpace".
        // When mySpace is "ABCD", suffix_0 is "ABCD", suffix_1 is "BCD",
        // suffix_2 is "CD", and sufffix_3 is "D".
        // target_j_k is a string in myTarget start at j-th postion ending k-th
        // position.
        // if myTarget is "ABCD",
        // j=0, and k=1 means that target_j_k is "A".
        // j=1, and k=3 means that target_j_k is "BC".
        // This method compares suffix_i and target_j_k.
        // if the beginning of suffix_i matches target_j_k, it return 0.
        // if suffix_i > target_j_k it return 1;
        // if suffix_i < target_j_k it return -1;
        // if first part of suffix_i is equal to target_j_k, it returns 0;
        //
        // Example of search
        // suffix target
        // "o" > "i"
        // "o" < "z"
        // "o" = "o"
        // "o" < "oo"
        // "Ho" > "Hi"
        // "Ho" < "Hz"
        // "Ho" = "Ho"
        // "Ho" < "Ho " : "Ho " is not in the head of suffix "Ho"
        // "Ho" = "H" : "H" is in the head of suffix "Ho"
        // The behavior is different from suffixCompare on this case.
        // For example,
        // if suffix_i is "Ho Hi Ho", and target_j_k is "Ho",
        // targetCompare should return 0;
        // if suffix_i is "Ho Hi Ho", and suffix_j is "Ho",
        // suffixCompare should return -1.
        //
        // ここに比較のコードを書け
        //

        int suffix_i_len = mySpace.length - i;
        int target_len = k - j;
        byte[] suffix_i = subBytes(mySpace, i, mySpace.length);
        byte[] target_j_k = subBytes(myTarget, j, k);
        if (target_len > suffix_i_len || target_len == suffix_i_len) {
            return compareTwoString(suffix_i, target_j_k);
        }
        if (target_len < suffix_i_len) {
            for (int x = 0; x < target_len; x++) {
                if (suffix_i[x] > target_j_k[x])
                    return 1;
                if (suffix_i[x] < target_j_k[x])
                    return -1;
            }
        }
        return 0; // この行は変更しなければならない。
    }

    // binary search for the first index
    private int first_search(int low, int high, int start, int end) {
        if (high >= low) {
            int mid = low + (high - low) / 2;
            // if ((mid == 0 || x > arr[mid - 1]) && arr[mid] == x)
            if ((mid == 0 || (targetCompare(suffixArray[mid - 1], start, end) == -1))
                    && (targetCompare(suffixArray[mid], start, end) == 0))
                return mid;
            // else if (x > arr[mid])
            else if (targetCompare(suffixArray[mid], start, end) == -1)
                return first_search((mid + 1), high, start, end);
            else
                return first_search(low, (mid - 1), start, end);
        }
        return -1;
    }

    // binary search for the last index
    private int last_search(int low, int high, int start, int end) {
        if (high >= low) {
            int mid = low + (high - low) / 2;
            // if ((mid == n - 1 || x < arr[mid + 1]) && arr[mid] == x)
            if ((mid == suffixArray.length - 1 || (targetCompare(suffixArray[mid + 1], start, end) == 1))
                    && (targetCompare(suffixArray[mid], start, end) == 0))
                return mid;
            // else if (x < arr[mid])
            else if (targetCompare(suffixArray[mid], start, end) == 1)
                return last_search(low, (mid - 1), start, end);
            else
                return last_search((mid + 1), high, start, end);
        }
        return -1;
    }

    private int subByteStartIndex(int start, int end) {
        // suffix arrayのなかで、目的の文字列の出現が始まる位置を求めるメソッド
        // 以下のように定義せよ。
        // The meaning of start and end is the same as subByteFrequency.
        /*
         * Example of suffix created from "Hi Ho Hi Ho" 0: Hi Ho 1: Ho 2: Ho Hi Ho 3:Hi
         * Ho 4:Hi Ho Hi Ho 5:Ho 6:Ho Hi Ho 7:i Ho 8:i Ho Hi Ho 9:o 10:o Hi Ho
         */

        // It returns the index of the first suffix
        // which is equal or greater than target_start_end.
        // Suppose target is set "Ho Ho Ho Ho"
        // if start = 0, and end = 2, target_start_end is "Ho".
        // if start = 0, and end = 3, target_start_end is "Ho ".
        // Assuming the suffix array is created from "Hi Ho Hi Ho",
        // if target_start_end is "Ho", it will return 5.
        // Assuming the suffix array is created from "Hi Ho Hi Ho",
        // if target_start_end is "Ho ", it will return 6.
        //
        // ここにコードを記述せよ。
        //

        return first_search(0, suffixArray.length - 1, start, end);
    }

    private int subByteEndIndex(int start, int end) {
        // suffix arrayのなかで、目的の文字列の出現しなくなる場所を求めるメソッド
        // 以下のように定義せよ。
        // The meaning of start and end is the same as subByteFrequency.
        /*
         * Example of suffix created from "Hi Ho Hi Ho" 0: Hi Ho 1: Ho 2: Ho Hi Ho 3:Hi
         * Ho 4:Hi Ho Hi Ho 5:Ho 6:Ho Hi Ho 7:i Ho 8:i Ho Hi Ho 9:o 10:o Hi Ho
         */
        // It returns the index of the first suffix
        // which is greater than target_start_end; (and not equal to target_start_end)
        // Suppose target is set "High_and_Low",
        // if start = 0, and end = 2, target_start_end is "Hi".
        // if start = 1, and end = 2, target_start_end is "i".
        // Assuming the suffix array is created from "Hi Ho Hi Ho",
        // if target_start_end is "Ho", it will return 7 for "Hi Ho Hi Ho".
        // Assuming the suffix array is created from "Hi Ho Hi Ho",
        // if target_start_end is"i", it will return 9 for "Hi Ho Hi Ho".
        //
        // ここにコードを記述せよ
        //

        int result = last_search(0, suffixArray.length - 1, start, end);
        if (result == -1)
            return result;
        return result + 1;
    }

    // Suffix Arrayを使ったプログラムのホワイトテストは、
    // privateなメソッドとフィールドをアクセスすることが必要なので、
    // クラスに属するstatic mainに書く方法もある。
    // static mainがあっても、呼びださなければよい。
    // 以下は、自由に変更して実験すること。
    // 注意：標準出力、エラー出力にメッセージを出すことは、
    // static mainからの実行のときだけに許される。
    // 外部からFrequencerを使うときにメッセージを出力してはならない。
    // 教員のテスト実行のときにメッセージがでると、仕様にない動作をするとみなし、
    // 減点の対象である。
    public static void main(String[] args) {
        Frequencer frequencerObject;
        try {
            // テストに使うのに推奨するmySpaceの文字は、"ABC", "CBA", "HHH", "Hi Ho Hi Ho".
            // Test case 1: mySpaceの文字は、"ABC"
            frequencerObject = new Frequencer();
            frequencerObject.setSpace("ABC".getBytes());
            frequencerObject.printSuffixArray();
            frequencerObject.setTarget("B".getBytes());
            // **** Print out subByteStartIndex, and subByteEndIndex
            int sub_start1 = frequencerObject.subByteStartIndex(0, frequencerObject.myTarget.length);
            System.out.print("subByteStartIndex = " + sub_start1 + " ");
            int sub_end1 = frequencerObject.subByteEndIndex(0, frequencerObject.myTarget.length);
            System.out.print("subByteEndIndex = " + sub_end1 + " ");
            int result1 = frequencerObject.frequency();
            System.out.print("Freq = " + result1 + " ");
            if (1 == result1) {
                System.out.println("OK");
            } else {
                System.out.println("WRONG");
            }

            // Test case 2: mySpaceの文字は、"CBA",
            frequencerObject = new Frequencer();
            frequencerObject.setSpace("CBA".getBytes());
            frequencerObject.printSuffixArray();
            frequencerObject.setTarget("A".getBytes());
            // **** Print out subByteStartIndex, and subByteEndIndex
            int sub_start2 = frequencerObject.subByteStartIndex(0, frequencerObject.myTarget.length);
            System.out.print("subByteStartIndex = " + sub_start2 + " ");
            int sub_end2 = frequencerObject.subByteEndIndex(0, frequencerObject.myTarget.length);
            System.out.print("subByteEndIndex = " + sub_end2 + " ");
            int result2 = frequencerObject.frequency();
            System.out.print("Freq = " + result2 + " ");
            if (1 == result2) {
                System.out.println("OK");
            } else {
                System.out.println("WRONG");
            }

            // Test case 3: mySpaceの文字は、"HHH"
            frequencerObject = new Frequencer();
            frequencerObject.setSpace("HHH".getBytes());
            frequencerObject.printSuffixArray();
            frequencerObject.setTarget("H".getBytes());
            // **** Print out subByteStartIndex, and subByteEndIndex
            int sub_start3 = frequencerObject.subByteStartIndex(0, frequencerObject.myTarget.length);
            System.out.print("subByteStartIndex = " + sub_start3 + " ");
            int sub_end3 = frequencerObject.subByteEndIndex(0, frequencerObject.myTarget.length);
            System.out.print("subByteEndIndex = " + sub_end3 + " ");
            int result3 = frequencerObject.frequency();
            System.out.print("Freq = " + result3 + " ");
            if (3 == result3) {
                System.out.println("OK");
            } else {
                System.out.println("WRONG");
            }

            // Test case 4: mySpaceの文字は、"Hi Ho Hi Ho"
            frequencerObject = new Frequencer();
            frequencerObject.setSpace("Hi Ho Hi Ho".getBytes());
            /*
             * Example from "Hi Ho Hi Ho" 0: Hi Ho 1: Ho 2: Ho Hi Ho 3:Hi Ho 4:Hi Ho Hi Ho
             * 5:Ho 6:Ho Hi Ho 7:i Ho 8:i Ho Hi Ho 9:o 10:o Hi Ho
             */
            frequencerObject.printSuffixArray();
            frequencerObject.setTarget("H".getBytes());
            // **** Print out subByteStartIndex, and subByteEndIndex
            int sub_start4 = frequencerObject.subByteStartIndex(0, frequencerObject.myTarget.length);
            System.out.print("subByteStartIndex = " + sub_start4 + " ");
            int sub_end4 = frequencerObject.subByteEndIndex(0, frequencerObject.myTarget.length);
            System.out.print("subByteEndIndex = " + sub_end4 + " ");
            int result4 = frequencerObject.frequency();
            System.out.print("Freq = " + result4 + " ");
            if (4 == result4) {
                System.out.println("OK");
            } else {
                System.out.println("WRONG");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
