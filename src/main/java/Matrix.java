import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Implementation of a Matrix to Multiply 2 Matrices together or to exponentiate a Matrix by a given exponent
 */
public class Matrix {

    private final int row;
    private final int col;
    public double[] matrix;

    /**
     * Initialize Matrix with given Parameters incl. Matrix
     *
     * @param row    how many rows in matrix
     * @param col    how many cols in matrix
     * @param matrix an array holds the matrix
     */
    public Matrix(int row, int col, double[] matrix) {
        this.row = row;
        this.col = col;
        this.matrix = matrix;
    }

    /**
     * Initialize Matrix with a fix fill
     *
     * @param row  how many rows in matrix
     * @param col  how many cols in matrix
     * @param fill initial value to fill the Matrix with
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
     * @param row how many rows in matrix
     * @param col how many cols in matrix
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
     * @param o the matrix as generic object
     * @return boolean
     */
    public boolean equals(Object o) {
        //First check the objects
        if (this == o) {
            return true;
        }
        if (!(o instanceof Matrix)) {
            return false;
        }
        //Check if the two matrix arrays are equal
        Matrix m = (Matrix) o; // o must be casted to Matrix
        return equalMatrix(m);
    }

    /**
     * Equals test only on the Arrays for the Matrix but not the object
     *
     * @param m Matrix to test equal with `this`
     * @return new Matrix
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
     * @param m the matrix to be multiplied with `this`
     * @return new Matrix
     */
    public Matrix multiply(Matrix m) {
        double[] resultm = new double[this.row * m.col];
        multiWithoutMatrix(this.matrix, m.matrix, resultm, this.row, this.col, m.col);
        return new Matrix(this.row, m.col, resultm);
    }

    /**
     * Do that multiply without generating a new matrix everytime
     *
     * @param matrixA    first matrix
     * @param matrixB    second matrix
     * @param matrixRes  result matrix (assigned to result Matrix Object)
     * @param rowMatrixA row from Matrix A
     * @param colMatrixA cols from Matrix A
     * @param colMatrixB cols from Matrix B
     * @return Matrix
     */
    private Matrix multiWithoutMatrix(double[] matrixA, double[] matrixB, double[] matrixRes, int rowMatrixA, int colMatrixA, int colMatrixB) {

        double multiRes;

        for (int i = 0; i < rowMatrixA; i++) {
            int c = i * colMatrixA, d = i * colMatrixB;
            for (int j = 0; j < colMatrixB; j++) {
                multiRes = 0.0;
                for (int k = 0; k < colMatrixA; k++) {
                    multiRes = multiRes + matrixA[c + k] * matrixB[k * colMatrixB + j];
                }
                matrixRes[d + j] = multiRes;
            }
        }
        return new Matrix(rowMatrixA, colMatrixB, matrixRes);
    }


    /**
     * Multiply 2 Matrices the JNIWay
     *
     * @param m matrix to be multiplied
     * @return new Matrix
     */
    public Matrix multiplyCpp(Matrix m) {
        double[] resultm = new double[this.row * m.col];
        multiplyC(this.matrix, m.matrix, resultm, this.row, this.col, m.col);
        return new Matrix(this.row, this.col, resultm);
    }

    /**
     * Powering a matrix by a given exponent. First the exponent is checked if 0 or 1 --> from Tests
     *
     * @param of exponent
     * @return new Matrix
     */
    public Matrix power(int of) {
        Matrix zero = new Matrix(this.row, this.row, 0);

        double[] empty = zero.matrix;
        double[] result = new double[zero.matrix.length];
        System.arraycopy(this.matrix, 0, result, 0, zero.matrix.length);

        // check if of is 0 or 1
        if (of == 0) return generateEinheitsmatrix();// first Test in Tests.testPower we need an "Einheitsmatrix"
        if (of == 1) return this;
        double[] exchange; //swap

        for (int i = 1; i < of; i++) {
            multiWithoutMatrix(result, this.matrix, empty, this.row, this.col, this.col);

            // Exchange arrays on round
            exchange = empty;
            empty = result;
            result = exchange;
        }

        //return a new Matrix
        return new Matrix(this.row, this.row, result);
    }

    /**
     * Powering with the use of native Implementation. Like the Java implementation the exponent gets checked first for 0 or 1
     *
     * @param of exponen
     * @return new matrix
     */
    public Matrix powerCpp(int of) {
        if (of == 0) return generateEinheitsmatrix();
        if (of == 1) return this;
        Matrix result = new Matrix(this.row, this.col, 0);
        powerC(this.matrix, result.matrix, of, this.col);
        return result;
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
     * Native Functions to be implemented in cpp, method signatures given by lecturer
     */
    native void multiplyC(double[] a, double[] b, double[] r, int m, int n, int o);
    native void powerC(double[] a, double[] res, int k, int columns);
}
