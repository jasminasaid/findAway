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
	List<ExtendedPath> alternativePaths;
	Stack<PathNode> nodesStack;
	Set<PathNode> visitedNodes;
	Set<Airport> visitedAirports;
	Map<Airport, List<AirportNode>> flights;

	public ExtendedSillySearch() {
		kdTree = new PathsKDTreeBuilder().build();
		allPaths = new ArrayList<ExtendedPath>();
		alternativePaths = new ArrayList<ExtendedPath>();
		nodesStack = new Stack<PathNode>();
		visitedNodes = new HashSet<PathNode>();
		visitedAirports = new HashSet<Airport>();
		flights = new AirportsGraphBuilder().build();
	}

	public void getShortestRoutes(String sPlace, String dPlace) {
		// GeocodeApiHandler geocode = new GeocodeApiHandler();
		// Position source = geocode.getPlacePosition(sPlace);
		// Position dest = geocode.getPlacePosition(dPlace);

		Position source = new Position("45.652549743652344,25.609699249267578");
		Position dest = new Position("50.087440490722656,14.421259880065918");
		if (source != null && dest != null) {
			extendedSearch(source, dest);
			int maxPaths = (allPaths.size() < 10) ? allPaths.size() : 10;
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

	// public void extendedSearch(Position source, Position dest) {
	// VehicleNode vNode = new VehicleNode();
	// vNode.setsPos(source);
	// List<PathNode> nearestNodes = vNode.getNearestNodesFromSource(kdTree,
	// flights);
	// Collections.reverse(nearestNodes);
	// nodesStack.addAll(nearestNodes);
	// ExtendedPath path = new ExtendedPath();
	//
	// while (nodesStack.size() > 0) {
	// PathNode actual = nodesStack.pop();
	// if (!visitedNodes.contains(actual)) {
	// if (actual instanceof AirportNode) {
	// AirportNode airpNode = (AirportNode) actual;
	// if (!visitedAirports.contains(airpNode.getSource())) {
	// visitedAirports.add(airpNode.getSource());
	// // if (flights.get(key) != null)
	// nodesStack.addAll(flights.get(airpNode.getSource()));
	// } else {
	// visitedNodes.add(actual);
	// // path.add(actual);
	// if (isDestinationNode(actual.gettPos(), dest)) {
	// path.add(actual);
	// ExtendedPath newPath = new ExtendedPath(path);
	// if (isEligiblePath(newPath)) {
	// allPaths.add(newPath);
	// if (path.size() > 0)
	// restorePreviousPath(path);
	// }
	// } else {
	// nearestNodes = actual.getNearestNodesFromTarget(
	// kdTree, flights);
	// if (nearestNodes.size() > 0) {
	// path.add(actual);
	// Collections.reverse(nearestNodes);
	// nodesStack.addAll(nearestNodes);
	// } else if (path.size() > 0)
	// path.remove(path.size() - 1);
	// }
	// }
	// } else {
	// visitedNodes.add(actual);
	// // path.add(actual);
	// if (isDestinationNode(actual.gettPos(), dest)) {
	// path.add(actual);
	// ExtendedPath newPath = new ExtendedPath(path);
	// if (isEligiblePath(newPath)) {
	// allPaths.add(newPath);
	// if (path.size() > 0)
	// restorePreviousPath(path);
	// }
	// } else {
	// nearestNodes = actual.getNearestNodesFromTarget(kdTree,
	// flights);
	// if (nearestNodes.size() > 0) {
	// path.add(actual);
	// Collections.reverse(nearestNodes);
	// nodesStack.addAll(nearestNodes);
	// } else if (path.size() > 0)
	// path.remove(path.size() - 1);
	// }
	// }
	// } else {
	// // path.add(actual);
	// ExtendedPath existingPath = getBestExistingPath(
	// actual.getsPos(), dest);
	// if (existingPath != null) {
	// ExtendedPath newPath = new ExtendedPath(path);
	// newPath.addSubPath(existingPath);
	// if (isEligiblePath(newPath)) {
	// allPaths.add(newPath);
	// }
	// }
	// if (path.size() > 0) {
	// // restorePreviousPath(path);
	// path.remove(path.size() - 1);
	// // PathNode lastVisited = path.get(path.size() - 1);
	// // nodesStack.addAll(lastVisited
	// // .getNearestNodesFromTarget(kdTree));
	// // // while()
	// }
	// }
	// }
	//
	// }

	public void extendedSearch(Position source, Position dest) {
		VehicleNode vNode = new VehicleNode();
		vNode.setsPos(source);
		List<PathNode> nearestNodes = vNode.getNearestNodesFromSource(kdTree,
				flights);
		// Collections.reverse(nearestNodes);
		nodesStack.addAll(nearestNodes);
		ExtendedPath path = new ExtendedPath();

		while (nodesStack.size() > 0) {
			PathNode actual = nodesStack.pop();
			if (!visitedNodes.contains(actual)) {
				visitedNodes.add(actual);
				if (isDestinationNode(actual.gettPos(), dest)) {
					if (isDestinationNode(path.get(0).getsPos(), source)) {
						path.add(actual);
						ExtendedPath newPath = new ExtendedPath(path);
						alternativePaths.add(newPath);
						if (isEligiblePath(newPath)) {
							allPaths.add(newPath);
							// if (path.size() > 0)
							// restorePreviousPath(path);
							// path.remove(path.size() - 1);
						}
						if (path.size() > 0)
							restorePreviousPath(path);
					}
				} else {
					nearestNodes = getUnvisitedNodes(actual
							.getNearestNodesFromTarget(kdTree, flights));
					while (nearestNodes.size() == 0 && path.size() > 0) {
						actual = path.remove(path.size() - 1);
						nearestNodes = getUnvisitedNodes(actual
								.getNearestNodesFromTarget(kdTree, flights));
					}
					path.add(actual);
					// Collections.reverse(nearestNodes);
					nodesStack.addAll(nearestNodes);
					// if (nearestNodes.size() > 0) {
					// path.add(actual);
					// Collections.reverse(nearestNodes);
					// nodesStack.addAll(nearestNodes);
					// }
					// // else if (path.size() > 0)
					// // path.remove(path.size() - 1);
					//
				}
			} else {
				// path.add(actual);
				ExtendedPath existingPath = getBestExistingPath(
						actual.getsPos(), dest);
				if (existingPath != null) {
					// path.add(actual);
					ExtendedPath newPath = new ExtendedPath(path);
					newPath.addSubPath(existingPath);
					alternativePaths.add(newPath);
					if (isEligiblePath(newPath)) {
						allPaths.add(newPath);
					}
				}
				if (path.size() > 0) {
					restorePreviousPath(path);
					// path.remove(path.size() - 1);
					// PathNode lastVisited = path.get(path.size() - 1);
					// nodesStack.addAll(lastVisited
					// .getNearestNodesFromTarget(kdTree));
					// // while()
				}
			}
		}

	}

	public ExtendedPath getBestExistingPath(Position source, Position dest) {
		List<ExtendedPath> existingPaths = new ArrayList<ExtendedPath>();
		for (int i = 0; i < allPaths.size(); i++) {
			ExtendedPath path = allPaths.get(i).getSubPath(source, dest);
			if (path.size() > 0)
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
		// path.remove(path.size() - 1);
		// PathNode lastVisited = path.get(path.size() - 1);
		PathNode lastVisited = path.get(path.size() - 1);
		List<PathNode> nearestNodes = getUnvisitedNodes(lastVisited
				.getNearestNodesFromSource(kdTree, flights));
		while (nearestNodes.size() == 0 && path.size() > 0) {
			lastVisited = path.remove(path.size() - 1);
			nearestNodes = getUnvisitedNodes(lastVisited
					.getNearestNodesFromTarget(kdTree, flights));
		}
		// Collections.reverse(nearestNodes);
		nodesStack.addAll(nearestNodes);
		// if (nearestNodes.size() == 0)
		// path.remove(path.size() - 1);
		// else {
		// // path.remove(path.size() - 1);
		// Collections.reverse(nearestNodes);
		// nodesStack.addAll(nearestNodes);
		// }
		// List<PathNode> unvisitedNodes = getUnvisitedNodes(nearestNodes);
		// if (unvisitedNodes.size() == 0) {
		// path.remove(path.size() - 1);
		//
		// } else {
		// nodesStack.addAll(unvisitedNodes);
		// }
		// nodesStack.add(lastVisited);
		// visitedNodes.remove(lastVisited);
	}

	public List<PathNode> getUnvisitedNodes(List<PathNode> nearestNodes) {
		List<PathNode> unvisited = new ArrayList<PathNode>();
		for (PathNode node : nearestNodes) {
			// if (node instanceof AirportNode) {
			// // unvisited.addAll(flights.get(((AirportNode)
			// // node).getSource()));
			// List<AirportNode> airportNodes = flights
			// .get(((AirportNode) node).getSource());
			// if (airportNodes != null) {
			// for (AirportNode an : airportNodes) {
			// if (!visitedNodes.contains(an))
			// unvisited.add(an);
			// }
			// }
			// } else
			if (!visitedNodes.contains(node))
				unvisited.add(node);
		}
		return unvisited;

	}

	public static void main(String[] args) {
		long startTime = System.nanoTime();
		ExtendedSillySearch s = new ExtendedSillySearch();
		s.getShortestRoutes("Brasov", "prague");
		System.out.println((System.nanoTime() - startTime) / 1000000000.0);
	}

}
