g++ -I"$JAVA_HOME/include" -I"$JAVA_HOME/include/darwin/" -o libjnimatrix.jnilib -shared Matrix.cpp
g++ -I"$JAVA_HOME/include" -I"$JAVA_HOME/include/darwin/" -o libjnimatrix.so -shared Matrix.cpp
