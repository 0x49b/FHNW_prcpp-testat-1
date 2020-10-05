import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class Tests {
	Matrix a, b, c, d, p, q;

	@BeforeEach
	void setUp() throws Exception {
		a = new Matrix(3, 5, new double[] {1,2,3,4,5,2,3,4,5,6,3,4,5,6,7});
		b = new Matrix(5, 2, new double[] {1,2,3,4,5,6,7,8,9,0});
		c = new Matrix(3, 2, new double[] {95,60,120,80,145,100});
		d = new Matrix(3, 3, new double[] {1,0,0,0,1,0,0,0,1});
		p = new Matrix(3, 3, new double[] {1,2,3,4,5,6,7,8,9});
		q = new Matrix(3, 3, new double[] {31644432,38881944,46119456,71662158,88052265,104442372,111679884,137222586,162765288});
	}

	@Test
	void testEqualsMatrix() {
		Matrix r1 = new Matrix(3,3);
		Matrix r2 = new Matrix(3,3);
		Matrix zeros = new Matrix(5,5,0);
		Matrix ones = new Matrix(5,5,1);

		assertTrue(a.equals(a));
		assertTrue(p.equals(new Matrix(3,3, new double[] {1,2,3,4,5,6,7,8,9})));
		assertFalse(a.equals(b));
		assertFalse(a.equals(null));
		assertFalse(r1.equals(r2));
		assertFalse(ones.equals(zeros));
	}

	@Test
	void testMultiply() {
		assertEquals(a.multiply(b), c);
		assertEquals(d.multiply(d), d);
		assertEquals(q.multiply(d), q);
	}

	/*
	@Test
	void testMultiplyCpp() {
		assertEquals(a.multiplyCpp(b), c);
		assertEquals(d.multiplyCpp(d), d);
		assertEquals(q.multiplyCpp(d), q);
	}
*/

	@Test
	void testPower() {
		assertEquals(p.power(0), d);
		assertEquals(p.power(1), p);
		// assertEquals(p.power(7), q);
		assertEquals(d.power(2), d);
		assertEquals(d.power(111), d);
	}

/*
	@Test
	void testPowerCpp() {
		assertEquals(p.powerCpp(0), d);
		assertEquals(p.powerCpp(1), p);
		assertEquals(p.powerCpp(7), q);
		assertEquals(d.powerCpp(2), d);
		assertEquals(d.powerCpp(111), d);
	}

 */
}
