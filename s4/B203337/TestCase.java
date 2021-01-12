package s4.B203337; // Please modify to s4.Bnnnnnn, where nnnnnn is your student ID. 
import java.lang.*;
import s4.specification.*;

/*
interface FrequencerInterface {     // This interface provides the design for frequency counter.
    void setTarget(byte[]  target); // set the data to search.
    void setSpace(byte[]  space);  // set the data to be searched target from.
    int frequency(); //It return -1, when TARGET is not set or TARGET's length is zero
                    //Otherwise, it return 0, when SPACE is not set or Space's length is zero
                    //Otherwise, get the frequency of TAGET in SPACE
    int subByteFrequency(int start, int end);
    // get the frequency of subByte of taget, i.e target[start], taget[start+1], ... , target[end-1].
    // For the incorrect value of START or END, the behavior is undefined.
}
*/

/*
package s4.specification;
public interface InformationEstimatorInterface{
    void setTarget(byte target[]); // set the data for computing the information quantities
    void setSpace(byte space[]); // set data for sample space to computer probability
    double estimation(); // It returns 0.0 when the target is not set or Target's length is zero;
// It returns Double.MAX_VALUE, when the true value is infinite, or space is not set.
// The behavior is undefined, if the true value is finete but larger than Double.MAX_VALUE.
// Note that this happens only when the space is unreasonably large. We will encounter other problem anyway.
// Otherwise, estimation of information quantity, 
}                        
*/


public class TestCase {
    public static void main(String[] args) {
	int c;
	c = 0;
	try {
	    FrequencerInterface  myObject;
	    int freq;
		    c = 0;
	    System.out.println("checking Frequencer");

	    // This is smoke test
	    myObject = new Frequencer();
	    myObject.setSpace("Hi Ho Hi Ho".getBytes());
	    myObject.setTarget("H".getBytes());
	    freq = myObject.frequency();
	    if(4 != freq) {System.out.println("frequency() for Hi_Ho_Hi_Ho, should return 4, when taget is H. But it returns "+freq); c++; }

	    // Write your testCase here
		FrequencerInterface test = new Frequencer();

		// 正常な動作
		test.setSpace("abcdefg".getBytes());
		test.setTarget("a".getBytes());
		freq = test.frequency();
		if (freq != 1) {
			System.out.println("frequency() should return 1. But it returns " + freq);
			c++;
		}

		// spaceとtargetの長さが0
		test.setSpace("".getBytes());
		test.setTarget("".getBytes());
		freq = test.frequency();
		if (freq > 0) {
			System.out.println("frequency() should return 0 or -1. But it returns " + freq);
			c++;
		}

		// targetの長さが0のときに-1が返されない
		test.setSpace("abcdefg".getBytes());
		freq = test.frequency();
		if (freq != -1) {
			System.out.println("frequency() should return -1. But it returns " + freq);
			c++;
		}

		// spaceの長さが0
		test.setSpace("".getBytes());
		test.setTarget("a".getBytes());
		freq = test.frequency();
		if (freq != 0) {
			System.out.println("frequency() should return 0. But it returns " + freq);
			c++;
		}

		// subByteFrequency()は未実装であるため値によらず常に-1を返す
		int subByteFreq = 0;
		test.setSpace("abcdefg".getBytes());
		freq = test.frequency();
		subByteFreq = test.subByteFrequency(0, "a".length());
		if (freq != 1) {
			System.out.println("frequency() should return 1. But it returns " + freq);
			c++;
		}
		if (subByteFreq < 0) {
			System.out.println("subByteFrequency() should return greater than 0. But it returns " + subByteFreq);
			c++;
		}

		// spaceやtargetがnullの場合を想定した処理が記述されていないためfrequency()で例外が発生する
		test.setSpace(null);
		test.setTarget(null);
		freq = test.frequency();
		if (freq != 0) {
			System.out.println("frequency() should return 0. But it returns " + freq);
			c++;
		}
	}
	catch(Exception e) {
	    System.out.println("Exception occurred in Frequencer Object");
	    c++;
	}

	try {
	    InformationEstimatorInterface myObject;
	    double value;
	    System.out.println("checking InformationEstimator");
	    myObject = new InformationEstimator();
	    myObject.setSpace("3210321001230123".getBytes());
	    myObject.setTarget("0".getBytes());
	    value = myObject.estimation();
	    if((value < 1.9999) || (2.0001 <value)) { System.out.println("IQ for 0 in 3210321001230123 should be 2.0. But it returns "+value); c++; }
	    myObject.setTarget("01".getBytes());
	    value = myObject.estimation();
	    if((value < 2.9999) || (3.0001 <value)) { System.out.println("IQ for 01 in 3210321001230123 should be 3.0. But it returns "+value); c++; }
	    myObject.setTarget("0123".getBytes());
	    value = myObject.estimation();
	    if((value < 2.9999) || (3.0001 <value)) { System.out.println("IQ for 0123 in 3210321001230123 should be 3.0. But it returns "+value); c++; }
	    myObject.setTarget("00".getBytes());
	    value = myObject.estimation();
	    if((value < 3.9999) || (4.0001 <value)) { System.out.println("IQ for 00 in 3210321001230123 should be 4.0. But it returns "+value); c++; }

		// targetの長さが0のときに0.0が返されない
	    myObject.setTarget("".getBytes());
	    value = myObject.estimation();
		if((value < -0.0001) || (0.0001 <value)) { System.out.println("IQ for Target none in 3210321001230123 should be 0.0. But it returns "+value); c++; }

		// spaceが設定されていないときにDouble.MAX_VALUEが返されず例外が発生する
		InformationEstimatorInterface test = new InformationEstimator();
		test.setTarget("0".getBytes());
		value = test.estimation();
		if((value < Double.MAX_VALUE) || (Double.MAX_VALUE <value)) { System.out.println("IQ for 0 in Space none should be Double.MAX_VALUE. But it returns "+value); c++; }
	}
	catch(Exception e) {
	    System.out.println("Exception occurred in InformationEstimator Object");
	    c++;
	}
	if(c == 0) { System.out.println("TestCase OK"); }
    }
}	    
	    
