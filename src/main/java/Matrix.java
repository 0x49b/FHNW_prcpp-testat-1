import java.util.concurrent.ThreadLocalRandom;

public class Matrix {

    private int row;
    private int col;
    public double[] matrix;

    /**
     * Initialize Matrix with given Parameters
     *
     * @param row
     * @param col
     * @param matrix
     */
    public Matrix(int row, int col, double[] matrix) {
        this.row = row;
        this.col = col;
        this.matrix = matrix;
    }

    /**
     * Initialize Matrix with a fix fill
     *
     * @param row
     * @param col
     * @param fill
     */
    public Matrix(int row, int col, double fill) {
        this.row = row;
        this.col = col;
        this.matrix = new double[row * col];
        for (int i = 0; i < (row * col) - 1; i++) {
            this.matrix[i] = fill;
        }
    }

    /**
     * Initialize Matrix with random double between 0 and 1
     *
     * @param row
     * @param col
     */
    public Matrix(int row, int col) {
        this.row = row;
        this.col = col;
        this.matrix = new double[row * col];
        for (int i = 0; i < (row * col) - 1; i++) {
            this.matrix[i] = ThreadLocalRandom.current().nextDouble(0, 1);
        }
    }

    /**
     * Checks two Arrays if they are equal, derived from Object.equals
     * https://www.baeldung.com/java-equals-hashcode-contracts#1-overriding-equals
     *
     * @param o
     * @return
     */
    public boolean equals(Object o) {
        //First check teh objects
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        //Check if the two matrix arrays are equal
        Matrix m = (Matrix) o; // o must be casted to Matrix
        return equalMatrix(m);
    }

    /**
     * Equals test only on the Arrays for the Matrix but not the object
     *
     * @param m
     * @return
     */
    public boolean equalMatrix(Matrix m) {
        for (int i = 0; i < this.matrix.length; i++) {
            if (this.matrix[i] != m.matrix[i]) {
                return false;
            }
        }
        return true;
    }


    /**
     * Multiply 2 Matrices the JavaWay
     *
     * @param b
     * @return
     */
    public Matrix multiply(Matrix b) {

        double[] resultm = new double[this.row * b.col];

        // https://www.java-programmieren.com/matrix-multiplikation-java.php
        for (int i = 0; i < this.row; i++) {
            for (int d = 0; d < b.col; d++) {
                for (int e = 0; e < b.row; e++) {
                    resultm[i * b.col + d] += this.matrix[i * this.col + e] * b.matrix[e * b.col + d];
                }
            }
        }

        return new Matrix(this.row, b.col, resultm);
    }

    /**
     * Multiply 2 Matrices the JNIWay
     *
     * @param b
     * @return
     */
    public Matrix multiplyCpp(Matrix b) {
        double[] resultm = new double[this.row * b.col];
        for (int i = 0; i < resultm.length; i++) {
            resultm[i] = 0.0;
        }
        multiplyC(this.matrix, b.matrix, resultm, this.row, this.col, b.col);
        return new Matrix(this.row, this.col, resultm);
    }

    /**
     * Powering
     * @param of
     * @return
     */
    public Matrix power(int of) {
        Matrix zero = new Matrix(this.row,this.row,0);
        double[] empty = zero.matrix; //emp
        double[] result = this.matrix; // temp

        // check if of is 0 or 1
        if (of == 0) return generateEinheitsmatrix();// as from first Test in Testpower we need an "Einheitsmatrix"
        if (of == 1) return this;
        double[] exchange; //swap

        for (int i = 1; i < of ; i++) {
            multiWithoutMatrix(result,this.matrix,empty,this.row);

            // Exhange arrays on round
            exchange = empty; empty = result; result = exchange;
        }

        //return a new Matrix
        return new Matrix(this.row,this.row, result);
    }

    public Matrix powerCpp(int of) {
        if (of == 0) return generateEinheitsmatrix();
        if (of == 1) return this;
        Matrix result = new Matrix(this.row,this.col,0);
        powerC(this.matrix, result.matrix, of, this.col);
        return result;
    }

    /**
     * Do that multiply
     * @param m1
     * @param m2
     * @param res
     * @param length
     */
    private void multiWithoutMatrix(double[] m1, double[] m2, double[] res, int length) {
        double sum = 0.0;
        for (int i = 0; i < length; i++) {
            int c = i * length, d = i * length;
            for (int j = 0; j < length; j++) {
                sum = 0.0;
                for (int k = 0; k < length; k++) {
                    sum = sum + m1[c + k] * m2[k * length + j];
                }
                res [d + j] = sum;
            }
        }
    }

    /**
     * Generate an Einheitsmatrix like
     * 1.0 | 0.0 | 0.0
     * -----|-----|-----
     * 0.0 | 1.0 | 0.0
     * -----|-----|-----
     * 0.0 | 0.0 | 1.0
     *
     * @return Matrix
     */
    private Matrix generateEinheitsmatrix() {
        double[] resultm = new double[this.matrix.length];
        int onesIndex = 0;
        for (int i = 0; i < this.row; i++) {
            resultm[onesIndex] = 1.0;
            onesIndex = onesIndex + this.col + 1; // shift one place down, one place to the right
        }
        return new Matrix(this.row, this.col, resultm);
    }

    /**
     * Helper to deep copy an entire Matrix
     *
     * @param m
     * @return
     */
    private Matrix copyMatrix(Matrix m) {
        //new Matrix with size of original
        Matrix copy = new Matrix(m.row, m.col, new double[m.matrix.length]);
        //copy all values (--> memorypointer)
        for (int i = 0; i < m.matrix.length; i++) {
            copy.matrix[i] = m.matrix[i];
        }
        return copy;
    }




    /**
     * Native Functions to be implemented in cpp, method signatures given by lecturer
     */
    native void multiplyC(double[] a, double[] b, double[] r, int m, int n, int o);
    native void powerC(double[] a, double[] res, int k, int columns);


}
