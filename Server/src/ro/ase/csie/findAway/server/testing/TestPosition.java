package ro.ase.csie.findAway.server.testing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import ro.ase.csie.findAway.server.model.api.Position;

public class TestPosition {

	private static final String CONSTRUCTOR_NORMAL_VALUES = "PositionConstructorsNormalValues.txt";
	private static BufferedReader reader;
	private static List<String> normalStrings = new ArrayList<String>();
	Position position;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		File file = new File(CONSTRUCTOR_NORMAL_VALUES);
		if (!file.exists())
			throw new FileNotFoundException();

		reader = new BufferedReader(new FileReader(file));
		String line = "";
		while ((line = reader.readLine()) != null) {
			if (line.startsWith("#"))
				continue;
			line.trim();

			if (line.startsWith("strings")) {
				while (!(line = reader.readLine()).isEmpty()) {
					normalStrings.add(line);
				}
			}
		}
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		reader.close();
	}

	@Test
	public void testStringConstructorNormalValues() {
		for (String pos : normalStrings) {
			position = new Position(pos);
			assertTrue("Position constructor fails for string " + pos, true);
		}
	}

	@Test
	public void testStringConstructorNullString() {
		String pos = null;
		try {
			position = new Position(pos);
			fail("Position constructor fails for string " + pos);
		} catch (NullPointerException ex) {
			assertTrue("Position constructor fails for string " + pos, true);
		}
	}

	@Test
	public void testStringConstructorEmptyString() {
		String pos = "";
		try {
			position = new Position(pos);
			fail("Position constructor fails for empty string");
		} catch (IllegalArgumentException ex) {
			assertTrue("Position constructor fails for empty string", true);
		}
	}

	@Test
	public void testStringConstructorNoCommaString() {
		String pos = "23.4355";
		try {
			position = new Position(pos);
			fail("Position constructor fails for string" + pos);
		} catch (NumberFormatException ex) {
			assertTrue("Position constructor fails for string" + pos, true);
		}
	}

	@Test
	public void testStringConstructorWrongFormat() {
		String pos = "12.3244sd,24.@332,-!S86.99";
		try {
			position = new Position(pos);
			fail("Position constructor fails for invalid string " + pos);
		} catch (NumberFormatException ex) {
			assertTrue("Position constructor fails for invalid string " + pos,
					true);
		}
	}

	@Test
	public void testStringConstructorOutOfRangeValues() {
		String pos = "-91.6473,181.27373";
		try {
			position = new Position(pos);
			fail("Position constructor fails for out of range lat or long");
		} catch (IllegalArgumentException ex) {
			assertTrue(
					"Position constructor fails for out of range lat or long",
					true);
		}
	}

	@Test
	public void testDoubleConstructorNormalValues() {
		double[] pos = { 24.343443, -22.394343 };
		position = new Position(pos);
		assertTrue(
				"Position constructor fails for double array with normal values",
				true);
	}

	@Test
	public void testDoubleConstructorInvalidArray() {
		double[] pos = { -23.34443 };
		try {
			position = new Position(pos);
			fail("Position constructor fails for invalid double array length");
		} catch (IllegalArgumentException ex) {
			assertTrue(
					"Position constructor fails for invalid double array length",
					true);
		}
	}

	@Test
	public void testDoubleConstructorOutOfRangeValues() {
		double[] pos = { -91.26383, 181.37433 };
		try {
			position = new Position(pos);
			fail("Position constructor fails for out of range double values");
		} catch (IllegalArgumentException ex) {
			assertTrue(
					"Position constructor fails for out of range double values",
					true);
		}
	}

	@Test
	public void testFloatConstructorNormalValues() {
		float lat = 21.3324F, lon = -23.323F;
		position = new Position(lat, lon);
		assertTrue("Position constructor fails for normal float values", true);
	}

	@Test
	public void testFloatConstructorOutOfRangeValues() {
		float lat = 91.3234F, lon = -181.3733F;
		try {
			position = new Position(lat, lon);
			fail("Position constructor fails for out of range float values");
		} catch (IllegalArgumentException ex) {
			assertTrue(
					"Position constructor fails for out of range float values",
					true);
		}
	}

	@Test
	public void testGetPositionArrayNormalValues() {
		position = new Position("24.344F, -12.334F");
		double[] pos = position.getPositionArray();
		assertEquals(
				"Position getPositionArray() method fails for normal values",
				pos, pos);
	}

	@Test
	public void testGetDistanceNormalValues() {
		position = new Position("40.7486,-73.9864");
		Position pos = new Position("49.8945, -89.7828");
		double expected = 1594;
		assertEquals("Position getDistance() method fails for normal values",
				expected, position.getDistance(pos), 10);
	}

	@Test
	public void testGetDistanceNullPosition() {
		position = new Position("40.7486,-73.9864");
		Position pos = null;
		try {
			position.getDistance(pos);
			fail("Position getDistance() method fails for null Position object");
		} catch (NullPointerException ex) {
			assertTrue(
					"Position getDistance() method fails for null Position object",
					true);
		}
	}

	@Test
	public void testIsSameNormalValues() {
		Position pos = new Position("12.3434,-23.32323");
		position = new Position("32.323, -23.332");
		assertEquals("Position isSame() method fails for normal values", false,
				position.isSame(pos));
	}

	@Test
	public void testIsSameNullValues() {
		position = null;
		Position pos = new Position("22.3373,-24.3774");
		try {
			position.isSame(pos);
			fail("Position isSame() method fails for null values");
		} catch (NullPointerException ex) {
			assertTrue("Position isSame() method fails for null values", true);
		}
	}
}
