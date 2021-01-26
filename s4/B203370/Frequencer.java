package s4.B203370; // Please modify to s4.Bnnnnnn, where nnnnnn is your student ID.
import java.lang.*;
import s4.specification.*;

/*
	 interface FrequencerInterface {  // This interface provides the design for frequency counter.
	 void setTarget(byte[] target);  // set the data to search.
	 void setSpace(byte[] space);  // set the data to be searched target from.
	 int frequency(); // It return -1, when TARGET is not set or TARGET's length is zero
// Otherwise, it return 0, when SPACE is not set or Space's length is zero
// Otherwise, get the frequency of TAGET in SPACE
int subByteFrequency(int start, int end);
// get the frequency of subByte of taget, i.e. target[start], taget[start+1], ... , target[end-1].
// For the incorrect value of START or END, the behavior is undefined.
	 }
	 */


public class Frequencer implements FrequencerInterface {
	// Code to Test, *warning: This code contains intentional problem*
	byte[] myTarget;
	byte[] mySpace;

	boolean targetReady = false;
	boolean spaceReady = false;

	int []  suffixArray;

	private void printSuffixArray() {
		if(spaceReady) {
			for(int i=0; i< mySpace.length; i++) {
				int s = suffixArray[i];
				System.out.printf("suffixArray[%2d]=%2d:", i, s);
				for(int j=s;j<mySpace.length;j++) {
					System.out.write(mySpace[j]);
				}
				System.out.write('\n');
			}
		}
	}

	private int suffixCompare(int i, int j) {
		// suffixCompareはソートのための比較メソッドである。
		// 次のように定義せよ。
		//
		// comparing two suffixes by dictionary order.
		// suffix_i is a string starting with the position i in "byte [] mySpace".
		// When mySpace is "ABCD", suffix_0 is "ABCD", suffix_1 is "BCD",
		// suffix_2 is "CD", and sufffix_3 is "D".
		// Each i and j denote suffix_i, and suffix_j.
		// Example of dictionary order
		// "i"      <  "o"        : compare by code
		// "Hi"     <  "Ho"       ; if head is same, compare the next element
		// "Ho"     <  "Ho "      ; if the prefix is identical, longer string is big
		//
		//The return value of "int suffixCompare" is as follows.
		// if suffix_i > suffix_j, it returns 1
		// if suffix_i < suffix_j, it returns -1
		// if suffix_i = suffix_j, it returns 0;

		while (i < mySpace.length && j < mySpace.length) {
			byte si = mySpace[i], sj = mySpace[j];
			// 完全辞書順(大文字と小文字を区別しない場合のため、このコメントは残す
			/*
				 if (si >= 'A' && si <= 'Z') si = (byte)(si - (byte)'A' + (byte)'a');
				 if (sj >= 'A' && sj <= 'Z') sj = (byte)(sj - (byte)'A' + (byte)'a');
				 */

			if (si == sj) {
				i++;j++;
			} else {
				if (si > sj) return 1;
				else if (si < sj) return -1;
			}
		}

		if (i == j) {
			return 0;
		} else if (i == mySpace.length) {
			return -1;
		} else {
			return 1;
		}
	}

	private int rapSC(int i, int j) {
		return suffixCompare(suffixArray[i], suffixArray[j]);
	}

	private void swapSA(int i, int j) {
		int tmp = suffixArray[i];
		suffixArray[i] = suffixArray[j];
		suffixArray[j] = tmp;
	}

	private void quickSuffixSort(int l, int r) {
		if(r < l) return; // 明らかな範囲外は何もせずに終了
		if(r - l <= 1) { // lとrはindexなので、差が1未満=検査して終わり
			if(rapSC(l, r) == 1) swapSA(l, r);
			return;
		}
		int pivot = suffixArray[(r+l)/2]; // pivotの最悪選択は許容する
		int tl = l, tr = r;
		while(true) {
			while(suffixCompare(suffixArray[tl], pivot) == -1)
				tl++; // pivotと同じでも止まる
			while(suffixCompare(pivot, suffixArray[tr]) == -1)
				tr--;
			if(tl >= tr) break;
			swapSA(tl, tr);
			tl++; tr--;
		}
		quickSuffixSort(l, tl-1);
		quickSuffixSort(tr+1, r);
	}

	public void setSpace(byte []space) {
		// suffixArrayの前処理は、setSpaceで定義せよ。
		mySpace = space; if(mySpace.length>0) spaceReady = true;
		// First, create unsorted suffix array.
		suffixArray = new int[space.length];
		// put all suffixes in suffixArray.
		for(int i = 0; i< space.length; i++) {
			suffixArray[i] = i; // Please note that each suffix is expressed by one integer.
		}

		if (spaceReady) quickSuffixSort(0, mySpace.length-1);
	}

	// ここから始まり、指定する範囲までは変更してはならないコードである。

	public void setTarget(byte [] target) {
		myTarget = target; if(myTarget.length>0) targetReady = true;
	}

	public int frequency() {
		if(targetReady == false) return -1;
		if(spaceReady == false) return 0;
		return subByteFrequency(0, myTarget.length);
	}

	public int subByteFrequency(int start, int end) {
		// start, and end specify a string to search in myTarget,
		// if myTarget is "ABCD",
		//     start=0, and end=1 means string "A".
		//     start=1, and end=3 means string "BC".
		// This method returns how many the string appears in my Space.
		//
		/* This method should be work as follows, but much more efficient.
			 int spaceLength = mySpace.length;
			 int count = 0;
			 for(int offset = 0; offset< spaceLength - (end - start); offset++) {
			 boolean abort = false;
			 for(int i = 0; i< (end - start); i++) {
			 if(myTarget[start+i] != mySpace[offset+i]) { abort = true; break; }
			 }
			 if(abort == false) { count++; }
			 }
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
		// target_j_k is a string in myTarget start at j-th postion ending k-th position.
		// if myTarget is "ABCD",
		//     j=0, and k=1 means that target_j_k is "A".
		//     j=1, and k=3 means that target_j_k is "BC".
		// This method compares suffix_i and target_j_k.
		// if the beginning of suffix_i matches target_j_k, it return 0.
		// if suffix_i > target_j_k it return 1;
		// if suffix_i < target_j_k it return -1;
		// if first part of suffix_i is equal to target_j_k, it returns 0;
		//
		// Example of search
		// suffix          target
		// "o"       >     "i"
		// "o"       <     "z"
		// "o"       =     "o"
		// "o"       <     "oo"
		// "Ho"      >     "Hi"
		// "Ho"      <     "Hz"
		// "Ho"      =     "Ho"
		// "Ho"      <     "Ho "   : "Ho " is not in the head of suffix "Ho"
		// "Ho"      =     "H"     : "H" is in the head of suffix "Ho"
		// The behavior is different from suffixCompare on this case.
		// For example,
		//    if suffix_i is "Ho Hi Ho", and target_j_k is "Ho",
		//            targetCompare should return 0;
		//    if suffix_i is "Ho Hi Ho", and suffix_j is "Ho",
		//            suffixCompare should return -1.
		//
		// ここに比較のコードを書け
		int LOOP_NUM = mySpace.length - suffixArray[i];
		int p;
		if (k - j < LOOP_NUM) LOOP_NUM = k - j;
		for (p = 0; p < LOOP_NUM; p++) {
			if (mySpace[suffixArray[i] + p] == myTarget[j + p]) continue;
			else if (mySpace[suffixArray[i] + p] <  myTarget[j + p]) return -1; // l.172
			else return 1; // l.171
		}

		if (k - j <= mySpace.length - suffixArray[i]) return 0; // l.183, l.185
		else return -1; // l.184
	}


	private int subByteStartIndex(int start, int end) {
		//suffix arrayのなかで、目的の文字列の出現が始まる位置を求めるメソッド
		// 以下のように定義せよ。
		// The meaning of start and end is the same as subByteFrequency.
		/* Example of suffix created from "Hi Ho Hi Ho"
0: Hi Ho
1: Ho
2: Ho Hi Ho
3:Hi Ho
4:Hi Ho Hi Ho
5:Ho
6:Ho Hi Ho
7:i Ho
8:i Ho Hi Ho
9:o
10:o Hi Ho
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
		// 二分探査
		// 探査結果の保存が必要
		int find = mySpace.length;
		int top = 0, bottom = mySpace.length;
		while(top <= bottom) {
			int position = (top + bottom) / 2;
			if(position < 0 || mySpace.length <= position) break;
			int result = targetCompare(position , start, end);
			if(result == 0) { // 文字列を含む
				find = position; // 探査結果を保存
				bottom = position-1; // 辞書順で前方を探索
			} else if (result == 1) { // spaceよりも前に文字列がある
				bottom = position - 1; // 辞書順で前方を探索
			} else { // targetよりも後ろに文字列がある
				top = position + 1; // 辞書順で後方を探索
			}
		}
		return find;
	}

	private int subByteEndIndex(int start, int end) {
		//suffix arrayのなかで、目的の文字列の出現しなくなる場所を求めるメソッド
		// 以下のように定義せよ。
		// The meaning of start and end is the same as subByteFrequency.
		/* Example of suffix created from "Hi Ho Hi Ho"
0: Hi Ho
1: Ho
2: Ho Hi Ho
3:Hi Ho
4:Hi Ho Hi Ho
5:Ho
6:Ho Hi Ho
7:i Ho
8:i Ho Hi Ho
9:o
10:o Hi Ho
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
		//　ここにコードを記述せよ
		int find = mySpace.length;
		int top = 0, bottom = mySpace.length;
		while(top <= bottom) {
			int position = (top + bottom) / 2;
			if(position < 0 || mySpace.length <= position) break;
			int result = targetCompare(position , start, end);
			if(result == 0) { // 文字列を含む
				find = position + 1; // 探査結果を保存
				top = position + 1; // 辞書順で前方を探索
			} else if (result == 1) { // targetよりも前に文字列がある
				bottom = position - 1; // 辞書順で前方を探索
			} else { // targetよりも後ろに文字列がある
				top = position + 1; // 辞書順で後方を探索
			}
		}
		return find;
	}

	public static void main(String[] args) {
		Frequencer frequencerObject;
		try {
			frequencerObject = new Frequencer();
			frequencerObject.setSpace("Hi Ho Hi Ho".getBytes());
			frequencerObject.printSuffixArray(); // you may use this line for DEBUG
			/* Example from "Hi Ho Hi Ho"
0: Hi Ho
1: Ho
2: Ho Hi Ho
3:Hi Ho
4:Hi Ho Hi Ho
5:Ho
6:Ho Hi Ho
7:i Ho
8:i Ho Hi Ho
9:o
A:o Hi Ho
*/

			//
			// **** Please write code to check subByteStartIndex, and subByteEndIndex
			//
			frequencerObject.setTarget("H".getBytes());
			int result = frequencerObject.frequency();
			System.out.print("Freq = "+ result +" ");
			if(4 == result) {
				System.out.println("OK");
			} else {
				System.out.println("WRONG");
			}
			// check subByteStartIndex
			frequencerObject.setTarget("Ho Ho Ho Ho".getBytes());
			result = frequencerObject.frequency();
			System.out.print("Freq = "+ result +" ");
			if(0 == result) {
				System.out.println("OK");
			} else {
				System.out.println("WRONG");
			}
			result = frequencerObject.subByteStartIndex(3, 5);
			System.out.print("subByteStart = "+ result +" ");
			if(5 == result) {
				System.out.println("OK");
			} else {
				System.out.println("WRONG");
			}
			result = frequencerObject.subByteEndIndex(3, 5);
			System.out.print("subByteEnd = "+ result +" ");
			if(7 == result) {
				System.out.println("OK");
			} else {
				System.out.println("WRONG");
			}

			// AAA
			frequencerObject.setSpace("AAAA".getBytes());
			frequencerObject.setTarget("A".getBytes());
			result = frequencerObject.subByteStartIndex(0, 1);
			System.out.print("subByteStart = "+ result +" ");
			if(0 == result) {
				System.out.println("OK");
			} else {
				System.out.println("WRONG");
			}
			result = frequencerObject.subByteEndIndex(0, 1);
			System.out.print("subByteEnd = "+ result +" ");
			if(4== result) {
				System.out.println("OK");
			} else {
				System.out.println("WRONG");
			}
		}
		catch(Exception e) {
			System.out.println("STOP");
			e.printStackTrace();
		}
	}
}
