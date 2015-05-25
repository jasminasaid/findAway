package ro.ase.csie.findAway.server.testing;

import static org.junit.Assert.*;

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

public class AutomatedTestPositionConstructors {

	private static final String CONSTRUCTOR_VALUES_FILE = "PositionConstructorsNormalValues.txt";
	private static BufferedReader reader;
	private static List<String> strings = new ArrayList<String>();
	private static List<double[]> doubles = new ArrayList<double[]>();
	private static List<float[]> floats = new ArrayList<float[]>();

	Position position;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		File file = new File(CONSTRUCTOR_VALUES_FILE);
		if (!file.exists())
			throw new FileNotFoundException();

		reader = new BufferedReader(new FileReader(file));
		String line = "";
		while ((line = reader.readLine()) != null) {
			if (line.startsWith("#"))
				continue;
			line.trim();

			if (line.startsWith("strings")) {
				while (!(line = reader.readLine()).trim().isEmpty()) {
					strings.add(line);
				}
			}
			if (line.startsWith("doubles")) {
				while (!(line = reader.readLine()).trim().isEmpty()) {
					String[] doublesString = line.split("\t");
					double[] doublesArray = new double[doublesString.length];
					for (int i = 0; i < doublesString.length; i++) {
						doublesArray[i] = Double.parseDouble(doublesString[i]);
					}
					doubles.add(doublesArray);
				}
			}
			if (line.startsWith("floats")) {
				while (!(line = reader.readLine()).trim().isEmpty()) {
					String[] floatsString = line.split("\t");
					float[] floatsArray = new float[floatsString.length];
					for (int i = 0; i < floatsString.length; i++) {
						floatsArray[i] = Float.parseFloat(floatsString[i]);
					}
					floats.add(floatsArray);
				}
			}
		}
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		reader.close();
	}

	@Test
	public void testStringConstructor() {
		for (String pos : strings) {
			if (pos.equals("null"))
				pos = null;
			position = new Position(pos);
			assertTrue("Position string constructor fails for string " + pos,
					true);
		}
	}

}
