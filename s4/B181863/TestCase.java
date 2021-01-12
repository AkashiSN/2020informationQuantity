package s4.B181863; // Please modify to s4.Bnnnnnn, where nnnnnn is your student ID. 

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
			FrequencerInterface myObject;
			int freq;
			c = 0;
			System.out.println("checking Frequencer");

			// This is smoke test
			myObject = new Frequencer();
			myObject.setSpace("Hi Ho Hi Ho".getBytes());
			myObject.setTarget("H".getBytes());
			freq = myObject.frequency();
			if (4 != freq) {
				System.out.println(
						"frequency() for Hi_Ho_Hi_Ho, should return 4, when taget is H. But it returns " + freq);
				c++;
			}

			// Write your testCase here
			/* ブラックボックステスト */
			// TestCase 1
			myObject.setSpace("Toyohashi University of Technology".getBytes());
			myObject.setTarget("T".getBytes());
			freq = myObject.frequency();
			if (2 != freq) {
				System.out.println(
						"frequency() for Toyohashi_University_of_Technology, should return 2, when taget is T. But it returns "
								+ freq);
				c++;
			}

			// TestCase 2
			myObject.setSpace("Software 4: Write Test Case".getBytes());
			myObject.setTarget("S".getBytes());
			freq = myObject.frequency();
			if (1 != freq) {
				System.out.println(
						"frequency() for Software_4:_Write_Test_Case, should return 2, when taget is T. But it returns "
								+ freq);
				c++;
			}

			// TestCase 3
			myObject.setSpace("".getBytes());
			myObject.setTarget("S".getBytes());
			freq = myObject.frequency();
			if (0 != freq) {
				System.out.println(
						"frequency() for null string, should return 0, when taget is S. But it returns " + freq);
				c++;
			}

			/* ホワイトボックステスト */
			// TestCase 1
			myObject.setSpace("Write Test Case For Wrong Case".getBytes());
			myObject.setTarget("".getBytes());
			freq = myObject.frequency();
			if (0 != freq) {
				System.out.println(
						"frequency() for Write_Test_Case_For_Wrong_Case, should return 0, when taget is \"\". But it returns "
								+ freq);
				c++;
			}

			// TestCase 2:Case Exception occurred in Frequencer Object
			myObject.setSpace("BBBBBBBBB".getBytes());
			myObject.setTarget("BB".getBytes());
			freq = myObject.frequency();
			if (1 != freq) {
				System.out.println(
						"frequency() for BBBBBBBBB, should return 0, when taget is BB. But it returns " + freq);
				c++;
			}

			// TestCase 3
			myObject.setSpace("Write Test Case For Wrong Case".getBytes());
			myObject.setTarget(null);
			freq = myObject.frequency();
			if (0 != freq) {
				System.out.println(
						"Test 3: frequency() for Write_Test_Case_For_Wrong_Case, should return 0, when taget is null. But it returns "
								+ freq);
				c++;
			}
		} catch (Exception e) {
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
			if ((value < 1.9999) || (2.0001 < value)) {
				System.out.println("IQ for 0 in 3210321001230123 should be 2.0. But it returns " + value);
				c++;
			}
			myObject.setTarget("01".getBytes());
			value = myObject.estimation();
			if ((value < 2.9999) || (3.0001 < value)) {
				System.out.println("IQ for 01 in 3210321001230123 should be 3.0. But it returns " + value);
				c++;
			}
			myObject.setTarget("0123".getBytes());
			value = myObject.estimation();
			if ((value < 2.9999) || (3.0001 < value)) {
				System.out.println("IQ for 0123 in 3210321001230123 should be 3.0. But it returns " + value);
				c++;
			}
			myObject.setTarget("00".getBytes());
			value = myObject.estimation();
			if ((value < 3.9999) || (4.0001 < value)) {
				System.out.println("IQ for 00 in 3210321001230123 should be 4.0. But it returns " + value);
				c++;
			}

			// Test 1: Target's length is 0
			InformationEstimatorInterface myObject1;
			double value1;
			myObject1 = new InformationEstimator();
			myObject1.setSpace("3210321001230123".getBytes());
			myObject1.setTarget("".getBytes());
			value1 = myObject1.estimation();
			if ((value1 < -0.0001) || (0.0001 < value1)) {
				System.out.println("Test1: IQ for null in 3210321001230123 should be 0.0. But it returns " + value1);
				c++;
			}

			// Test 2: Target is not set
			InformationEstimatorInterface myObject2;
			double value2;
			myObject2 = new InformationEstimator();
			myObject2.setSpace("3210321001230123".getBytes());
			value2 = myObject1.estimation();
			if ((value2 < -0.0001) || (0.0001 < value2)) {
				System.out.println("Test2: IQ when Target is not set should be 0.0. But it returns " + value2);
				c++;
			}

			// Test 3: Space is not set
			InformationEstimatorInterface myObject3;
			myObject3 = new InformationEstimator();
			myObject3.setTarget("0".getBytes());
		} catch (Exception e) {
			System.out.println("Exception occurred in InformationEstimator Object");
			c++;
		}
		if (c == 0) {
			System.out.println("TestCase OK");
		}
	}
}
