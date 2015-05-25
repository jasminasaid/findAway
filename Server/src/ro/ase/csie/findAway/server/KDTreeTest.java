package ro.ase.csie.findAway.server;

import java.util.ArrayList;
import java.util.List;

import ro.ase.csie.findAway.server.helpers.GeocodeApiHandler;
import ro.ase.csie.findAway.server.helpers.PathsKDTreeBuilder;
import ro.ase.csie.findAway.server.model.PathNode;
import ro.ase.csie.findAway.server.model.api.Position;

public class KDTreeTest {

	public double[] getCartesians(double lat, double lon) {
		final int R = 6371;
		return new double[] { R * Math.cos(lat) * Math.cos(lon),
				R * Math.cos(lat) * Math.sin(lon), R * Math.sin(lat) };
	}

	public static void main(String[] args) {

		PathsKDTreeBuilder kdTree = new PathsKDTreeBuilder();
		GeocodeApiHandler geocode = new GeocodeApiHandler();
		 Position sPos = geocode.getPlacePosition("Sibiu");
		// Position tPos = geocode.getPlacePosition("prague");

		 // Bucuresti
		// double[] points = { 44.43614, 26.10274 };

		 //Brasov
		// double[] points = { 45.652549743652344,25.609699249267578 };

//		double[] points = { 45.65255, 25.6097 };

		double[] points = sPos.getPositionArray();
		System.out.println(points[0] + ", " + points[1]);
		Object[] results = kdTree.pathsKDTree.nearest(points, 200);

		// paths with starting position within 80km radius from origin point
		List<PathNode> paths = new ArrayList<PathNode>();
		double distance = 0.0;
		for (int i = 0; i < results.length; i++) {
			PathNode pn = (PathNode) results[i];
			distance = pn.getsPos().getDistance(new Position(points));
			if (distance < 80.0) {
				paths.add(pn);
				System.out.println(distance);
				pn.printNode();
			}
		}

	}

}
