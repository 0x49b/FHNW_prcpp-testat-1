import javax.sound.midi.Soundbank;
import java.util.Arrays;

public class MatrixMain {

    static {
        System.loadLibrary("jnimatrix");
    }

    public static void main(String[] args) {

        System.out.println("Performance Tests End-2-End for multiplication and exponentiate");
        System.out.println("-------------------------------------------------------------------------");
        System.out.println("Running Performance Test 500x400 Matrix multiply");

        Matrix m = new Matrix(500, 6000);
        Matrix n = new Matrix(6000, 400);

        // Performance Test Java
        long startJava = System.currentTimeMillis();
        Matrix o = m.multiply(n);
        long endJava = System.currentTimeMillis();

        // Performance Test Native Interface
        long cppStart = System.currentTimeMillis();
        Matrix co = m.multiplyCpp(n);
        long cppEnd = System.currentTimeMillis();


        if(o.equals(co)) System.out.println("Java and C++ are equals");
        System.out.println("Run JavaImpl takes : " + (endJava - startJava) + "ms");
        System.out.println("Run CPPImpl takes : " + (cppEnd - cppStart) + "ms");

        System.out.println();
        System.out.println("Running Performance Test 250x250 Matrix pow by 93");

        // Powering Java
        Matrix p = new Matrix(250, 250);
        long startJavaPow = System.currentTimeMillis();
        Matrix q = p.power(93);
        long endJavaPow = System.currentTimeMillis();

        //Powering C++
        long startCPPPow = System.currentTimeMillis();
        Matrix cq = p.powerCpp(93);
        long endCPPPow = System.currentTimeMillis();


        if(q.equals(cq)) System.out.println("Java and C++ are equals");
        System.out.println("Run JavaImpl takes : " + (endJavaPow - startJavaPow) + "ms");
        System.out.println("Run CPPImpl takes : " + (endCPPPow - startCPPPow) + "ms");


        /**
         * Performance Tests End-2-End for multiplication and exponentiate
         * -------------------------------------------------------------------------
         * Running Performance Test 500x400 Matrix multiply
         * Java and C++ are equals
         * Run JavaImpl takes : 4967ms
         * Run CPPImpl takes : 4982ms
         *
         * Running Performance Test 250x250 Matrix pow by 93
         * Java and C++ are equals
         * Run JavaImpl takes : 1799ms
         * Run CPPImpl takes : 1485ms
         */

    }
}
