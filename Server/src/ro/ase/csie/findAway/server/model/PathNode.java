package ro.ase.csie.findAway.server.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import net.sf.javaml.core.kdtree.KDTree;
import ro.ase.csie.findAway.server.model.api.Airport;
import ro.ase.csie.findAway.server.model.api.Position;

public abstract class PathNode implements Serializable {

	private static final long serialVersionUID = 1L;

	protected Position sPos;
	protected Position tPos;
	protected float price;
	protected float duration;

	public Position getsPos() {
		return sPos;
	}

	public void setsPos(Position sPos) {
		this.sPos = sPos;
	}

	public Position gettPos() {
		return tPos;
	}

	public void settPos(Position tPos) {
		this.tPos = tPos;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public float getDuration() {
		return duration;
	}

	public void setDuration(float duration) {
		this.duration = duration;
	}

	public List<PathNode> getNearestNodesFromSource(KDTree kdTree, Map<Airport, List<AirportNode>> flights) {
		// paths with starting position within 60km radius from origin point
		List<PathNode> nodes = new ArrayList<PathNode>();
		double distance = 0.0;
		final double[] coord = this.getsPos().getPositionArray();
		Object[] results = kdTree.nearest(coord, 100);
		for (int i = 0; i < results.length; i++) {
			PathNode pn = (PathNode) results[i];
			distance = pn.getsPos().getDistance(new Position(coord));
			if (distance < 40.0) {
				if (pn instanceof AirportNode) {
					List<AirportNode> flightNodes = flights
							.get(((AirportNode) pn).getSource());
					if (flightNodes != null) {
						nodes.addAll(flightNodes);
					}
				} else
					nodes.add(pn);
			}
		}

		Collections.sort(nodes, new Comparator<PathNode>() {

			@Override
			public int compare(PathNode pn1, PathNode pn2) {
				return (pn1.getsPos().getDistance(new Position(coord)) <= pn2
						.getsPos().getDistance(new Position(coord))) ? -1 : 1;
			}

		});

		return nodes;
	}

	public List<PathNode> getNearestNodesFromTarget(KDTree kdTree,
			Map<Airport, List<AirportNode>> flights) {
		// paths with starting position within 60km radius from destination
		// point
		List<PathNode> nodes = new ArrayList<PathNode>();
		double distance = 0.0;
		final double[] coord = this.gettPos().getPositionArray();
		Object[] results = kdTree.nearest(coord, 100);
		for (int i = 0; i < results.length; i++) {
			PathNode pn = (PathNode) results[i];
			distance = pn.getsPos().getDistance(new Position(coord));
			if (distance < 40.0) {
				if (pn instanceof AirportNode) {
					List<AirportNode> flightNodes = flights
							.get(((AirportNode) pn).getSource());
					if (flightNodes != null) {
						nodes.addAll(flightNodes);
					}
				} else
					nodes.add(pn);
			}
		}

		Collections.sort(nodes, new Comparator<PathNode>() {

			@Override
			public int compare(PathNode pn1, PathNode pn2) {
				return (pn1.getsPos().getDistance(new Position(coord)) <= pn2
						.getsPos().getDistance(new Position(coord))) ? -1 : 1;
			}

		});

		return nodes;
	}

	public abstract void printNode();
}
