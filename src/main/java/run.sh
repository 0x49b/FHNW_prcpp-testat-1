echo "[DEBUG] compile cpp library"
./generatejnilib.sh

cp libjnimatrix.so /Users/florian/Library/Java/Extensions/libjnimatrix.so
cp libjnimatrix.so /Users/florian/Library/Java/Extensions/libjnimatrix.jnilib

echo "[DEBUG] compile Java"
javac MatrixMain.java

echo "[DEBUG] run java"
echo ""
java -Djava.path.library=. MatrixMain
