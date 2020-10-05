// imports from example jni gist for mac:
// https://gist.github.com/DmitrySoshnikov/8b1599a5197b5469c8cc07025f600fdb
#include <iostream>
#include <stdio.h>
#include <jni.h>
// end imports for jni gist for mac
#include "Matrix.h"

//https://docs.oracle.com/javase/7/docs/technotes/guides/jni/spec/functions.html

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
(JNIEnv *envPow, jobject jobj1, jdoubleArray matrixA, jdoubleArray matrixREsult, jint of, jint matrixSize)
{

	jboolean copMatrixA;
	jboolean copMatrixResult;

    // get the Array elements
	jdouble* newMatrixA = envPow->GetDoubleArrayElements(matrixA, &copMatrixA);
	jdouble* newMatrixResult = envPow->GetDoubleArrayElements(matrixREsult, &copMatrixResult);

    // Make temporary Array with same length as original array
    jint length = envPow->GetArrayLength(matrixA);
	jdouble* temp = new jdouble[length];

	//Copy all from MatrixA to a temporary place
	memcpy(temp, newMatrixA, length * sizeof(jdouble));

    // do the multiply as in of
	for (int i = 0; i < of; i++) {
		doMultiply(temp,newMatrixA,newMatrixResult,matrixSize,matrixSize,matrixSize);
		//swap the two arrays for next iteration
		std::swap(temp, newMatrixResult);
	}

	// inform that we are finished
	envPow->ReleaseDoubleArrayElements(matrixA, newMatrixA, JNI_ABORT);
	envPow->ReleaseDoubleArrayElements(matrixREsult, newMatrixResult, 0);
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


	// Multiply like in JavaImpl --> Refactored to new Function doMultiply
	/*
	jdouble multiRes = 0.0;
	for (int i = 0; i<zeilenMatrixA; i++) {
		int c = i * spaltenMatrixA, d = i * spaltenMatrixB;
		for (int j = 0; j<spaltenMatrixB; j++) {
			multiRes = 0.0;
			for (int k = 0; k<spaltenMatrixA; k++) {
				multiRes = multiRes + newMatrixA[c + k] * newMatrixB[k * spaltenMatrixB + j];
			}
			newMatrixResult[d + j ] = multiRes;
		}
	}*/

	doMultiply(newMatrixA, newMatrixB, newMatrixResult, zeilenMatrixA, spaltenMatrixA, spaltenMatrixB);

    //https://docs.oracle.com/javase/7/docs/technotes/guides/jni/spec/functions.html --> Release<PrimitveType>ArrayElements
    env->ReleaseDoubleArrayElements(matrixA, newMatrixA, JNI_ABORT);
    env->ReleaseDoubleArrayElements(matrixB, newMatrixB, JNI_ABORT);
    env->ReleaseDoubleArrayElements(matrixResult, newMatrixResult, JNI_ABORT);
}
