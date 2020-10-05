package jnitest;

public class JNITest {

    public static native void display();

    public static native int increment(int value);

    public static void main(String[] args) {
        display();
        increment(1);
    }

    static {
        System.loadLibrary("jnitest");
    }

}

