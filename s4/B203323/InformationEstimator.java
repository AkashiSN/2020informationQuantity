package s4.B203323; // Please modify to s4.Bnnnnnn, where nnnnnn is your student ID.
import java.lang.*;
import s4.specification.*;

/* What is imported from s4.specification
package s4.specification;
public interface InformationEstimatorInterface {
    void setTarget(byte target[]);  // set the data for computing the information quantities
    void setSpace(byte space[]);  // set data for sample space to computer probability
    double estimation();  // It returns 0.0 when the target is not set or Target's length is zero;
    // It returns Double.MAX_VALUE, when the true value is infinite, or space is not set.
    // The behavior is undefined, if the true value is finete but larger than Double.MAX_VALUE.
    // Note that this happens only when the space is unreasonably large. We will encounter other problem anyway.
    // Otherwise, estimation of information quantity,
}
*/


public class InformationEstimator implements InformationEstimatorInterface {
    // Code to test, *warning: This code contains intentional problem*
    byte[] myTarget; // data to compute its information quantity (情報量を計算するためのデータ)
    byte[] mySpace;  // Sample space to compute the probability (確率を計算するサンプルデータ)
    FrequencerInterface myFrequencer;  // Object for counting frequency (頻度をカウントする)

    byte[] subBytes(byte[] x, int start, int end) {
        // corresponding to substring of String for byte[], (byte[]の部分文字列に対応)
        // It is not implement in class library because internal structure of byte[] requires copy.
        byte[] result = new byte[end - start];
        for(int i = 0; i<end - start; i++) { result[i] = x[start + i]; }; // startからendまでの文字列をコピー
        return result;
    }

    // IQ: information quantity for a count, -log2(count/sizeof(space)) (カウントの情報量)
    double iq(int freq) {
        return  - Math.log10((double) freq / (double) mySpace.length)/ Math.log10((double) 2.0);
    }

    // 情報量を計算するためのデータを設定
    @Override
    public void setTarget(byte[] target) {
        myTarget = target;
    }

    // 検索する文字データを設定
    @Override
    public void setSpace(byte[] space) {
        myFrequencer = new Frequencer();
        mySpace = space; myFrequencer.setSpace(space);
    }

    // DPによるiq(int freq)の計算
    @Override
    public double estimation(){
        // Targetが設定されてない場合や長さが０の時は０を返す
        if(myTarget == null || myTarget.length == 0) return 0;
        // Spaceが設定されてない場合はDouble.MAX＿VALUEを返す
        if(mySpace == null) return Double.MAX_VALUE;

        // 計算したの結果を格納する。新たに計算するときに、計算済みの結果があったら取り出す
        double[] estimation_values =  new double[myTarget.length + 1];

        myFrequencer.setTarget(subBytes(myTarget, 0, 1));
        estimation_values[0] = iq(myFrequencer.frequency());

        for(int i=0; i<myTarget.length; i++){
          myFrequencer.setTarget(subBytes(myTarget, 0, i+1));
          estimation_values[i] = iq(myFrequencer.frequency());
          for(int j=0; j<i; j++){
            myFrequencer.setTarget(subBytes(myTarget, j+1, i+1));
            if(estimation_values[i] > estimation_values[j] + iq(myFrequencer.frequency())){
              estimation_values[i] = estimation_values[j] + iq(myFrequencer.frequency());
            }
          }
        }

        // 値が無限の時はDouble.MAX＿VALUEを返す
        if(Double.isInfinite(estimation_values[myTarget.length - 1])) return Double.MAX_VALUE;
        return estimation_values[myTarget.length - 1];
    }

    // @Override
    // public double estimation(){
    //     // Targetが設定されてない場合や長さが０の時は０を返す
    //     if(myTarget == null || myTarget.length == 0) return 0;
    //     // Spaceが設定されてない場合はDouble.MAX＿VALUEを返す
    //     if(mySpace == null) return Double.MAX_VALUE;
    //
    //     boolean [] partition = new boolean[myTarget.length+1];
    //     int np = 1<<(myTarget.length-1); // np : 文字列の分割数, length : 文字の数
    //     // System.out.println("np="+np+" length="+myTarget.length);
    //     double value = Double.MAX_VALUE; // value = mininimum of each "value1".
    //
    //     for(int p=0; p<np; p++) { // There are 2^(n-1) kinds of partitions. (文字列の全ての分割は2^(n-1)の分割の仕方がある)
    //         // binary representation of p forms partition.
    //         // for partition {"ab" "cde" "fg"}
    //         // a b c d e f g   : myTarget
    //         // T F T F F T F T : partition:
    //         partition[0] = true; // I know that this is not needed, but..
    //         for(int i=0; i<myTarget.length -1;i++) {
    //             partition[i+1] = (0 !=((1<<i) & p));
    //         }
    //         partition[myTarget.length] = true;
    //
    //         // Compute Information Quantity for the partition, in "value1"
    //         // value1 = IQ(#"ab")+IQ(#"cde")+IQ(#"fg") for the above example
    //         double value1 = (double) 0.0;
    //         int end = 0;
    //         int start = end;
    //         while(start<myTarget.length) {
    //             System.out.write(myTarget[end]);
    //             end++;;
    //             while(partition[end] == false) {
    //                 System.out.write(myTarget[end]);
    //                 end++;
    //             }
    //             System.out.print("("+start+","+end+")");
    //             myFrequencer.setTarget(subBytes(myTarget, start, end));
    //             value1 = value1 + iq(myFrequencer.frequency());
    //             start = end;
    //         }
    //         System.out.println(" "+ value1);
    //
    //         // Get the minimal value in "value"
    //         if(value1 < value) value = value1;
    //     }
    //     // 値が無限の時はDouble.MAX＿VALUEを返す
    //     if(Double.isInfinite(value)) return Double.MAX_VALUE;
    //     return value;
    // }

    public static void main(String[] args) {
        InformationEstimator myObject;
        double value;

        // Original TestCase
        myObject = new InformationEstimator();
        value = myObject.estimation();
        System.out.println(">not set Target : "+value); // value = 0
        myObject.setTarget("0".getBytes());
        value = myObject.estimation();
        System.out.println(">not set Space : "+value); // value = Double.MAX＿VALUE
        myObject.setSpace("3210321001230123".getBytes());
        myObject.setTarget("".getBytes());
        value = myObject.estimation();
        System.out.println(">Target's length = 0 : "+value); // value = 0

        myObject.setSpace("".getBytes());
        myObject.setTarget("0".getBytes());
        value = myObject.estimation();
        System.out.println(">Space's length = 0 : "+value); // value = Double.MAX＿VALUE

        // Existing TestCase
        myObject = new InformationEstimator();
        myObject.setSpace("3210321001230123".getBytes());
        value = myObject.estimation();
        myObject.setTarget("0".getBytes());
        value = myObject.estimation();
        System.out.println(">0 : "+value);
        myObject.setTarget("01".getBytes());
        value = myObject.estimation();
        System.out.println(">01 : "+value);
        myObject.setTarget("0123".getBytes());
        value = myObject.estimation();
        System.out.println(">0123 : "+value);
        myObject.setTarget("00".getBytes());
        value = myObject.estimation();
        System.out.println(">00 : "+value);
    }
}
