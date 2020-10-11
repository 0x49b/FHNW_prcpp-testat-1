g++ -I"$JAVA_HOME/include" -I"$JAVA_HOME/include/darwin/" -o libjnimatrix.jnilib -shared -O3 Matrix.cpp
g++ -I"$JAVA_HOME/include" -I"$JAVA_HOME/include/darwin/" -o libjnimatrix.so -shared -O3 Matrix.cpp
