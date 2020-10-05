import java.util.Arrays;

public class MatrixMain {

    static{
        System.loadLibrary("jnimatrix");
    }

    public static void main(String[] args) {

        System.out.println("Running Performance Test 500x400 Matrix multiply");

        Matrix m = new Matrix(500,6000);
        Matrix n = new Matrix(6000,400);

        // Performance Test Java
        long startJava = System.currentTimeMillis();
        Matrix o = m.multiply(n);
        long endJava = System.currentTimeMillis();

        // Performance Test Native Interface
        long cppStart = System.currentTimeMillis();
        Matrix co = m.multiplyCpp(n);
        long cppEnd = System.currentTimeMillis();

        System.out.println("Results of Multiply");
        System.out.println(Arrays.toString(o.matrix));
        System.out.println(Arrays.toString(co.matrix));

        System.out.println("Run JavaImpl takes : " + (endJava-startJava) + "ms");
        System.out.println("Run CPPImpl takes : " + (cppEnd-cppStart) + "ms");

        System.out.println();
        System.out.println("Running Performance Test 250x250 Matrix pow by 93");

        // Powering Java
        Matrix p = new Matrix(250,250);
        long startJavaPow = System.currentTimeMillis();
        Matrix q = p.power(93);
        long endJavaPow = System.currentTimeMillis();

        //Powering C++
        long startCPPPow = System.currentTimeMillis();
        Matrix cq = p.powerCpp(93);
        long endCPPPow = System.currentTimeMillis();

        System.out.println("Results of Exponentiate");
        System.out.println(Arrays.toString(q.matrix));
        System.out.println(Arrays.toString(cq.matrix));


        System.out.println("Run JavaImpl takes : " + (endJavaPow-startJavaPow) + "ms");
        System.out.println("Run CPPImpl takes : " + (endCPPPow-startCPPPow) + "ms");


        /**
         * Running Performance Test 500x400 Matrix multiply
         * Run JavaImpl takes : 6478ms
         * Run CPPImpl takes : 10411ms
         *
         * Running Performance Test 250x250 Matrix pow by 93
         * Run JavaImpl takes : 1990ms
         * Run CPPImpl takes : 4489ms
         */

    }

    //https://stackoverflow.com/questions/45061338/get-human-readable-time-from-nanoseconds
    private static String getReadableTime(Long nanos){

        long tempSec    = nanos/(1000*1000*1000);
        long sec        = tempSec % 60;
        long min        = (tempSec /60) % 60;
        long hour       = (tempSec /(60*60)) % 24;
        long day        = (tempSec / (24*60*60)) % 24;

        return String.format("%dd %dh %dm %ds", day,hour,min,sec);

    }

}
