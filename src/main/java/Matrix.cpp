// imports from example jni gist for mac:
// https://gist.github.com/DmitrySoshnikov/8b1599a5197b5469c8cc07025f600fdb
#include <iostream>
#include <stdio.h>
#include <jni.h>
// end imports for jni gist for mac
#include "Matrix.h"

//https://docs.oracle.com/javase/7/docs/technotes/guides/jni/spec/functions.html


// Multiply a
void doMultiply(jdouble* matrixA, jdouble* matrixB, jdouble* matrixC, jint rowMatrixA, jint colMatrixA, jint colMatrixB){
	// Multiply like in JavaImpl
    jdouble multiRes = 0.0;
    for (int i = 0; i<rowMatrixA; i++) {
        int c = i * colMatrixA, d = i * colMatrixB;
        for (int j = 0; j<colMatrixB; j++) {
            multiRes = 0.0;
            for (int k = 0; k<colMatrixA; k++) {
                multiRes = multiRes + matrixA[c + k] * matrixB[k * colMatrixB + j];
            }
            matrixC[d + j ] = multiRes;
        }
    }
}

JNIEXPORT void JNICALL Java_Matrix_powerC
(JNIEnv *envPow, jobject jobj1, jdoubleArray matrixA, jdoubleArray matrixResult, jint of, jint matrixSize)
{


	jboolean copMatrixA;
	jboolean copMatrixResult;

    // get length of originalArray
    jint length = envPow->GetArrayLength(matrixA);

    // create an empty Array
	jdouble* exchange = new jdouble[length];

	// Get all the ArrayElements from input
	jdouble* newMatrixA = envPow->GetDoubleArrayElements(matrixA, &copMatrixA);
	jdouble* newMatrixResult = envPow->GetDoubleArrayElements(matrixResult, &copMatrixResult);

	//copy all from newMatrixA to empty
	memcpy(exchange, newMatrixA, length * sizeof(jdouble));

	for (jint i = 0; i < of; i++) {
		doMultiply(exchange, newMatrixA, newMatrixResult, matrixSize, matrixSize, matrixSize);
		std::swap(exchange, newMatrixResult);
	}

	// inform that we are finished
	envPow->ReleaseDoubleArrayElements(matrixA, newMatrixA, JNI_ABORT);
	envPow->ReleaseDoubleArrayElements(matrixResult, newMatrixResult, 0);

	delete[] exchange;
}

JNIEXPORT void JNICALL Java_Matrix_multiplyC (
JNIEnv *env, jobject jobj,
jdoubleArray matrixA, jdoubleArray matrixB, jdoubleArray matrixResult,
jint zeilenMatrixA, jint spaltenMatrixA, jint spaltenMatrixB){

    jboolean copMatrixA;
	jboolean copMatrixB;
	jboolean copMatrixResult;

	// https://stackoverflow.com/questions/7318143/converting-between-jdoublearray-and-vectordouble-in-a-java-native-jni-method
	jdouble* newMatrixA = env->GetDoubleArrayElements(matrixA, &copMatrixA);
	jdouble* newMatrixB = env->GetDoubleArrayElements(matrixB, &copMatrixB);
	jdouble* newMatrixResult = env->GetDoubleArrayElements(matrixResult, &copMatrixResult);

	doMultiply(newMatrixA, newMatrixB, newMatrixResult, zeilenMatrixA, spaltenMatrixA, spaltenMatrixB);
	jint length = env->GetArrayLength(matrixResult);


    //https://docs.oracle.com/javase/7/docs/technotes/guides/jni/spec/functions.html --> Release<PrimitveType>ArrayElements
    if(copMatrixA == JNI_TRUE){ env->ReleaseDoubleArrayElements(matrixA, newMatrixA, JNI_ABORT);}
    if(copMatrixB == JNI_TRUE){ env->ReleaseDoubleArrayElements(matrixB, newMatrixB, JNI_ABORT);}
    if(copMatrixResult == JNI_TRUE){ env->ReleaseDoubleArrayElements(matrixResult, newMatrixResult, 0);}
}
