package ro.ase.csie.findAway.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import net.sf.javaml.core.kdtree.KDTree;
import ro.ase.csie.findAway.server.helpers.AirportsGraphBuilder;
import ro.ase.csie.findAway.server.helpers.PathsKDTreeBuilder;
import ro.ase.csie.findAway.server.model.AirportNode;
import ro.ase.csie.findAway.server.model.ExtendedPath;
import ro.ase.csie.findAway.server.model.PathNode;
import ro.ase.csie.findAway.server.model.VehicleNode;
import ro.ase.csie.findAway.server.model.api.Airport;
import ro.ase.csie.findAway.server.model.api.Position;

public class ExtendedSillySearch {

	KDTree kdTree;
	List<ExtendedPath> allPaths;
	Stack<PathNode> nodesStack;
	Set<PathNode> visitedNodes;
	Set<Airport> visitedAirports;
	Map<Airport, List<AirportNode>> flights;

	public ExtendedSillySearch() {
		kdTree = new PathsKDTreeBuilder().pathsKDTree;
		allPaths = new ArrayList<ExtendedPath>();
		nodesStack = new Stack<PathNode>();
		visitedNodes = new HashSet<PathNode>();
		visitedAirports = new HashSet<Airport>();
		flights = new AirportsGraphBuilder().airports;
	}

	public void getShortestRoutes(String sPlace, String dPlace) {
		// GeocodeApiHandler geocode = new GeocodeApiHandler();
		// Position source = geocode.getPlacePosition(sPlace);
		// Position dest = geocode.getPlacePosition(dPlace);

		Position source = new Position("45.652549743652344,25.609699249267578");
		Position dest = new Position("50.087440490722656,14.421259880065918");
		if (source != null && dest != null) {
			extendedSearch(source, dest);
			int maxPaths = (allPaths.size() < 40) ? allPaths.size() : 40;
			Collections.sort(allPaths);
			for (int i = 0; i < maxPaths; i++) {

				ExtendedPath path = allPaths.get(i);
				System.out.println("ROUTE " + (i + 1) + " at "
						+ path.getPathPrice() + "�, in "
						+ path.getPathDuration() + " min");
				path.printPath();
				System.out
						.println("---------------------------------------------------------");
			}
		}
	}

	// public void extendedSearch(Position source, Position dest) {
	// VehicleNode vNode = new VehicleNode();
	// vNode.setsPos(source);
	// List<PathNode> nearestNodes = vNode.getNearestNodesFromSource(kdTree);
	// Collections.reverse(nearestNodes);
	// nodesStack.addAll(nearestNodes);
	// ExtendedPath path = new ExtendedPath();
	//
	// while (nodesStack.size() > 0) {
	// PathNode actual = nodesStack.pop();
	// if (!visitedNodes.contains(actual)) {
	// visitedNodes.add(actual);
	// path.add(actual);
	// if (actual instanceof AirportNode) {
	// AirportNode an = (AirportNode) actual;
	// if (flights.get(an.getSource()) != null) {
	// if (isDestinationNode(an.gettPos(), dest)
	// || isDestinationNode(an.getfPos(), dest)) {
	// ExtendedPath newPath = new ExtendedPath(path);
	// // newPath.add(an);
	// allPaths.add(newPath);
	// restorePreviousPath(path);
	// }
	// AirportNode predNode = null;
	// for (int i = 0; i < flights.get(an.getSource()).size(); i++) {
	// AirportNode node = flights.get(an.getSource()).get(
	// i);
	// if (!visitedNodes.contains(node))
	// predNode = node;
	// }
	// if (predNode != null) {
	// nodesStack.add(predNode);
	// nearestNodes = predNode
	// .getNearestNodesFromTarget(kdTree);
	// Collections.reverse(nearestNodes);
	// nodesStack.addAll(nearestNodes);
	// } else {
	// if (path.size() > 0)
	// restorePreviousPath(path);
	// }
	// } else {
	// if (path.size() > 0) {
	// restorePreviousPath(path);
	// }
	// }
	//
	// } else {
	//
	// if (isDestinationNode(actual.gettPos(), dest)) {
	// allPaths.add(new ExtendedPath(path));
	// } else {
	// nearestNodes = actual.getNearestNodesFromTarget(kdTree);
	// Collections.reverse(nearestNodes);
	// nodesStack.addAll(nearestNodes);
	// }
	// }
	// }
	// }
	//
	// }

	public void extendedSearch(Position source, Position dest) {
		VehicleNode vNode = new VehicleNode();
		vNode.setsPos(source);
		List<PathNode> nearestNodes = vNode.getNearestNodesFromSource(kdTree);
		Collections.reverse(nearestNodes);
		nodesStack.addAll(nearestNodes);
		ExtendedPath path = new ExtendedPath();

		while (nodesStack.size() > 0) {
			PathNode actual = nodesStack.pop();
			if (!visitedNodes.contains(actual)) {
				if (actual instanceof AirportNode) {
					AirportNode airpNode = (AirportNode) actual;
					if (!visitedAirports.contains(airpNode.getSource())) {
						visitedAirports.add(airpNode.getSource());
						// if (flights.get(key) != null)
						nodesStack.addAll(flights.get(airpNode.getSource()));
					}
					else {
						visitedNodes.add(actual);
						path.add(actual);
						if (isDestinationNode(actual.gettPos(), dest)) {
							ExtendedPath newPath = new ExtendedPath(path);
							if (isEligiblePath(newPath)) {
								allPaths.add(newPath);
								if (path.size() > 0)
									restorePreviousPath(path);
							}
						} else {
							nearestNodes = actual.getNearestNodesFromTarget(kdTree);
							Collections.reverse(nearestNodes);
							nodesStack.addAll(nearestNodes);
						}
					}
				} else {
					visitedNodes.add(actual);
					path.add(actual);
					if (isDestinationNode(actual.gettPos(), dest)) {
						ExtendedPath newPath = new ExtendedPath(path);
						if (isEligiblePath(newPath)) {
							allPaths.add(newPath);
							if (path.size() > 0)
								restorePreviousPath(path);
						}
					} else {
						nearestNodes = actual.getNearestNodesFromTarget(kdTree);
						Collections.reverse(nearestNodes);
						nodesStack.addAll(nearestNodes);
					}
				}
			} else {
				path.add(actual);
				ExtendedPath existingPath = getBestExistingPath(
						actual.getsPos(), dest);
				if (existingPath != null) {
					ExtendedPath newPath = new ExtendedPath(path);
					newPath.addSubPath(existingPath);
					if (isEligiblePath(newPath)) {
						allPaths.add(newPath);
					}
				}
				if (path.size() > 0)
					restorePreviousPath(path);
			}
		}

	}

	public ExtendedPath getBestExistingPath(Position source, Position dest) {
		List<ExtendedPath> existingPaths = new ArrayList<ExtendedPath>();
		for (int i = 0; i < allPaths.size(); i++) {
			ExtendedPath path = allPaths.get(i).getSubPath(source, dest);
			existingPaths.add(path);
		}

		ExtendedPath bestPath = null;
		if (existingPaths.size() > 0) {
			Collections.sort(existingPaths);
			bestPath = existingPaths.get(0);
		}

		return bestPath;
 
	}

	public boolean isEligiblePath(ExtendedPath path) {
		if (pathHasCycles(path))
			return false;
		if (isDuplicatePath(path))
			return false;
		return true;
	}

	public boolean pathHasCycles(ExtendedPath path) {
		List<Position> positions = new ArrayList<Position>();
		for (int i = 0; i < path.size(); i++) {
			if (positions.contains((path.get(i).getsPos())))
				return true;
			positions.add(path.get(i).getsPos());
		}
		return false;
	}

	public boolean isDuplicatePath(ExtendedPath path) {
		int duplicates = 0;
		for (int i = 0; i < allPaths.size(); i++) {
			duplicates = 0;
			if (allPaths.get(i).size() == path.size()) {
				for (int j = 0; j < path.size(); j++) {
					if (allPaths.get(i).get(j).getsPos()
							.isSame(path.get(j).getsPos())
							&& allPaths.get(i).get(j).gettPos()
									.isSame(path.get(j).gettPos()))
						duplicates++;
					else
						break;
				}
				if (duplicates == path.size())
					return true;
			}
		}
		return false;
	}

	public boolean isDestinationNode(Position actualPos, Position destPos) {
		return (actualPos.getDistance(destPos) < 30.0) ? true : false;
	}

	public void restorePreviousPath(ExtendedPath path) {
		 path.remove(path.size() - 1);
		 PathNode lastVisited = path.get(path.size()-1);
		nodesStack.addAll(lastVisited.getNearestNodesFromTarget(kdTree));
		// nodesStack.add(lastVisited);
		// visitedNodes.remove(lastVisited);
	}

	public static void main(String[] args) {
		long startTime = System.nanoTime();
		ExtendedSillySearch s = new ExtendedSillySearch();
		s.getShortestRoutes("Brasov", "prague");
		System.out.println((System.nanoTime() - startTime) / 1000000000.0);
	}

}
