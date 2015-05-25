package ro.ase.csie.findAway.server.helpers;

import net.sf.javaml.core.kdtree.KDTree;
import ro.ase.csie.findAway.server.model.AirportNode;
import ro.ase.csie.findAway.server.model.TransitNode;
import ro.ase.csie.findAway.server.model.VehicleNode;
import ro.ase.csie.findAway.server.model.api.Airport;
import ro.ase.csie.findAway.server.model.api.Position;

public class PathsKDTreeBuilder {
	public KDTree pathsKDTree;

	public PathsKDTreeBuilder() {
		pathsKDTree = new KDTree(2);
	}

	public KDTree build() {
		buildKDTree();
		return this.pathsKDTree;
	}

	public void buildKDTree() {

		AirportsGraphBuilder aGraph = new AirportsGraphBuilder();
		for (Airport a : aGraph.airports.keySet()) {
			AirportNode an = aGraph.airports.get(a).get(0);
			pathsKDTree.insert(getKeyFromPosition(new Position(an.getSource()
					.getPos())), an);
		}

		TransitGraphBuilder tGraph = new TransitGraphBuilder();
		for (TransitNode t : tGraph.nodes) {
			pathsKDTree.insert(getKeyFromPosition(t.getsPos()), t);
		}

		VehicleGraphBuilder vGraph = new VehicleGraphBuilder();
		for (VehicleNode v : vGraph.nodes) {
			pathsKDTree.insert(getKeyFromPosition(v.getsPos()), v);
		}
	}

	private double[] getKeyFromPosition(Position pos) {
		return new double[] { pos.getLat(), pos.getLon() };
	}

}
