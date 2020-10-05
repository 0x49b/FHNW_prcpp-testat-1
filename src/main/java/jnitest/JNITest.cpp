#include <iostream>
#include <stdio.h>
#include <jni.h>
#include "JNITest.h"

using namespace std;

JNIEXPORT void JNICALL Java_JNITest_display(JNIEnv *, jclass){
    cout << "C++: Hello World!" << endl;
}

JNIEXPORT jint JNICALL Java_JNITest_increment (JNIEnv *, jclass, jint i){
    return i+1;
}