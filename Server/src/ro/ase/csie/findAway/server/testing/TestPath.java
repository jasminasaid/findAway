package ro.ase.csie.findAway.server.testing;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ro.ase.csie.findAway.server.helpers.AirportsGraphBuilder;
import ro.ase.csie.findAway.server.model.AirportNode;
import ro.ase.csie.findAway.server.model.Path;
import ro.ase.csie.findAway.server.model.api.Airport;

public class TestPath {

	List<AirportNode> existingNodes;
	static List<AirportNode> testNodes;
	static Airport source, dest;
	static AirportsGraphBuilder mockBuilder;
	static Map<Airport, List<AirportNode>> airports;
	static Path testPath;


	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		mockBuilder = mock(AirportsGraphBuilder.class);
		when(mockBuilder.getAirportByID(1)).thenReturn(new Airport(1));
		when(mockBuilder.getAirportByID(2)).thenReturn(new Airport(2));
		airports = new AirportsGraphBuilder().build();

		source = mockBuilder.getAirportByID(1);
		dest = mockBuilder.getAirportByID(2);

		testNodes = new ArrayList<AirportNode>();
		for (int i = 0; i < 2; i++) {
			testNodes.add(airports.get(source).get(i));
		}

		testPath = new Path(airports.get(mockBuilder.getAirportByID(1)));
	}

	@Before
	public void setUp() throws Exception {
		existingNodes = new ArrayList<AirportNode>(testNodes);
		source = mockBuilder.getAirportByID(1);
		dest = mockBuilder.getAirportByID(2);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void testSimpleConstructorNormalValues() {
		List<AirportNode> expectedNodes = new ArrayList<AirportNode>();
		Path actualPath = new Path();
		assertEquals("Path simple contructor fails for normal values",
				expectedNodes, actualPath.getNodes());
	}

	@Test
	public void testListConstructorNormalValues() {
		Path actualPath = new Path(existingNodes);
		assertEquals("Path list constructor fails for normal values",
				existingNodes, actualPath.getNodes());
	}

	@Test
	public void testListConstructorNullValues() {
		existingNodes = null;
		Path actualPath = new Path(existingNodes);
		assertEquals("Path list constructor fails for null values",
				new ArrayList<AirportNode>(), actualPath.getNodes());
	}

	@Test
	public void testPathConstructorNormalValues() {
		Path expectedPath = new Path(existingNodes);
		Path actualPath = new Path(expectedPath);
		assertEquals("Path path constructor fails for normal values",
				expectedPath.getNodes(), actualPath.getNodes());
	}

	@Test
	public void testPathConstructorNullValues() {
		Path expectedPath = new Path();
		Path nullPath = null;
		Path actualPath = new Path(nullPath);

		assertEquals("Path path constructor fails for null values",
				expectedPath.getNodes(), actualPath.getNodes());
	}

	@Test
	public void testAddNormalValues() {
		List<AirportNode> expectedNodes = new ArrayList<AirportNode>(
				existingNodes);
		expectedNodes.add(airports.get(source).get(2));
		Path actualPath = new Path(existingNodes);
		actualPath.add(airports.get(source).get(2));
		assertEquals("Path add() method fails for normal values",
				expectedNodes, actualPath.getNodes());
	}

	@Test
	public void testAddNullValues() {
		List<AirportNode> expectedNodes = new ArrayList<AirportNode>(
				existingNodes);
		AirportNode newNode = null;
		Path actualPath = new Path(existingNodes);
		actualPath.add(newNode);
		assertEquals("Path add() method fails for null values", expectedNodes,
				actualPath.getNodes());
	}

	@Test
	public void testRemoveNormalValues() {
		Path actualPath = new Path(existingNodes);
		AirportNode expectedNode = existingNodes
				.remove(existingNodes.size() - 1);
		assertEquals("Path remove() method fails for normal values",
				expectedNode, actualPath.remove(1));
	}

	@Test
	public void testRemoveNullValues() {
		AirportNode expectedNode = null;
		List<AirportNode> nullNodes = null;
		Path actualPath = new Path();
		actualPath.setNodes(nullNodes);

		assertEquals("Path remove() method fails for null values",
				expectedNode, actualPath.remove(2));
	}

	@Test
	public void testRemoveIndexOutOfRange() {
		Path actualPath = new Path(existingNodes);
		AirportNode expectedNode = null;
		assertEquals("Path remove() method fails for index out of range",
				expectedNode, actualPath.remove(10));
	}

	@Test
	public void testSizeNormalValues() {
		int expectedSize = 2;
		Path actualPath = new Path(existingNodes);
		assertEquals("Path size() method fails for normal values",
				expectedSize, actualPath.size());
	}

	@Test
	public void testSizeNullValues() {
		int expectedSize = 0;
		List<AirportNode> nullNodes = null;
		Path actualPath = new Path();
		actualPath.setNodes(nullNodes);

		assertEquals("Path size() method fails for null values", expectedSize,
				actualPath.size());
	}

	@Test
	public void testGetNormalValues() {
		AirportNode expectedNode = existingNodes.get(0);
		Path actualPath = new Path(existingNodes);
		assertEquals("Path get() method fails for normal values", expectedNode,
				actualPath.get(0));
	}

	@Test
	public void testGetNullValues() {
		AirportNode expectedNode = null;
		List<AirportNode> nullNodes = null;
		Path actualPath = new Path();
		actualPath.setNodes(nullNodes);

		assertEquals("Path get() method fails for null values", expectedNode,
				actualPath.get(2));
	}

	@Test
	public void testGetIndexOutOfRange() {
		AirportNode expectedNode = null;
		Path actualPath = new Path(existingNodes);
		assertEquals("Path get() method fails for index out of range values",
				expectedNode, actualPath.get(10));
	}

	@Test
	public void testGetSubPathNormalValues() {

		List<AirportNode> expected = new ArrayList<AirportNode>();
		expected.add(airports.get(source).get(0));
		// Path expected = new Path(paths);
		assertEquals("Path getSubPath() method fails for normal values",
				expected, testPath.getSubPath(source, dest).getNodes());
	}

	@Test
	public void testGetSubPathNullValues() {
		source = null;
		dest = null;
		Path expected = null;
		assertEquals("Path getSubPath() method fails for null values",
				expected, testPath.getSubPath(source, dest));
	}

	@Test
	public void testAddSubPathNormalValues() {
		List<AirportNode> expectedNodes = new ArrayList<AirportNode>(
				existingNodes);
		List<AirportNode> newNodes = new ArrayList<AirportNode>();

		expectedNodes.add(airports.get(source).get(2));
		newNodes.add(airports.get(source).get(2));

		Path actualPath = new Path(existingNodes);
		Path newPath = new Path(newNodes);
		Path expectedPath = new Path(expectedNodes);

		actualPath.addSubPath(newPath);
		assertEquals("Path addSubPath() fails for normal values",
				expectedPath.getNodes(), actualPath.getNodes());
	}

	@Test
	public void testAddSubPathNullPaths() {
		source = mockBuilder.getAirportByID(1);

		Path actualPath = new Path(existingNodes);
		Path newPath = null;
		Path expectedPath = new Path(existingNodes);
		actualPath.addSubPath(newPath);
		assertEquals(
				"Path addSubPath() method fails for null path object values",
				expectedPath.getNodes(), actualPath.getNodes());
	}

	@Test
	public void testAddSubPathNullNodes() {
		List<AirportNode> newNodes = null;
		Path actualPath = new Path(existingNodes);
		Path newPath = new Path(newNodes);
		Path expectedPath = new Path(existingNodes);
		actualPath.addSubPath(newPath);
		assertEquals("Path addSubPath() method fails for null nodes values",
				expectedPath.getNodes(), actualPath.getNodes());
	}
}
