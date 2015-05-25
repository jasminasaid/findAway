package ro.ase.csie.findAway.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import ro.ase.csie.findAway.server.helpers.AirportsGraphBuilder;
import ro.ase.csie.findAway.server.model.AirportNode;
import ro.ase.csie.findAway.server.model.Path;
import ro.ase.csie.findAway.server.model.api.Airport;

public class SillySearch {

	Map<Airport, List<AirportNode>> airports;
	List<Path> allPaths;
	Stack<Airport> airportsStack;
	Set<Airport> visitedAirports;

	public SillySearch() {
		airports = new AirportsGraphBuilder().build();
		allPaths = new ArrayList<Path>();
		airportsStack = new Stack<Airport>();
		visitedAirports = new HashSet<Airport>();
	}

	public void getShortestRoutes(int sourceID, int destID) {
		// Airport source = graph.getAirportByID(sourceID);
		// Airport dest = graph.getAirportByID(destID);
		Airport source = null, dest = null;

		for (Airport a : airports.keySet()) {
			if (a.getId() == sourceID)
				source = a;
			if (a.getId() == destID)
				dest = a;
		}

		if (source != null && dest != null) {
			iterativeDFS(source, dest);
			int maxPaths = (allPaths.size() < 30) ? allPaths.size() : 30;
			Collections.sort(allPaths);
			for (int i = 0; i < maxPaths; i++) {

				Path path = allPaths.get(i);
				System.out.println("ROUTE " + (i + 1) + " at "
						+ path.getPathPrice() + "�, in "
						+ path.getPathDuration() + " min");
				path.printPath();
				System.out
						.println("---------------------------------------------------------");
			}
		}
	}

	public void iterativeDFS(Airport source, Airport dest) {
		Set<AirportNode> visitedNodes = new HashSet<AirportNode>();
		Path path = new Path();

		airportsStack.push(source);
		while (airportsStack.size() > 0) {
			Airport actual = airportsStack.pop();
			if (!visitedAirports.contains(actual)) {
				visitedAirports.add(actual);
				AirportNode predecessor = null;
				if (airports.get(actual) != null) {
					for (int i = 0; i < airports.get(actual).size(); i++) {
						AirportNode an = airports.get(actual).get(i);
						if (!visitedNodes.contains(an)) {
							predecessor = an;
							if (isDestinationNode(an, dest)) {
								Path newPath = new Path(path);
								newPath.add(predecessor);
								if (isEligiblePath(newPath)) {
									allPaths.add(newPath);
									visitedNodes.add(predecessor);
									visitedAirports.remove(predecessor
											.getSource());
									airportsStack.add(predecessor.getSource());
									break;
								}
							}
							// if (!visitedAirports.contains(an.gettDest()))
							// airportsStack.add(an.gettDest());
						}
					}
					if (predecessor != null) {
						if (!isDestinationNode(predecessor, dest)) {
							path.add(predecessor);
							visitedNodes.add(predecessor);
							airportsStack.add(predecessor.gettDest());
						}
					} else {
						if (path.size() > 0) {
							restorePreviousPath(path);
						}
					}
				} else {
					if (path.size() > 0) {
						restorePreviousPath(path);
					}
				}
			} else {
				Path existingPath = getBestExistingPath(actual, dest);
				if (existingPath != null) {
					Path newPath = new Path(path);
					newPath.addSubPath(existingPath);
					if (isEligiblePath(newPath) && existingPath.size() > 0)
						allPaths.add(newPath);
				}
				if (path.size() > 0) {
					restorePreviousPath(path);
				}
				// else {
				// visitedAirports.remove(source);
				// airportsStack.add(source);
				// }
			}
		}
	}

	public boolean isDestinationNode(AirportNode node, Airport dest) {
		return (node.gettDest().getId() == dest.getId() || node.getfDest()
				.getId() == dest.getId()) ? true : false;
	}

	public void restorePreviousPath(Path path) {
		AirportNode lastVisited = path.remove(path.size() - 1);
		airportsStack.add(lastVisited.getSource());
		visitedAirports.remove(lastVisited.getSource());
	}

	public Path getBestExistingPath(Airport source, Airport dest) {
		List<Path> existingPaths = new ArrayList<Path>();
		for (int i = 0; i < allPaths.size(); i++) {
			Path path = allPaths.get(i).getSubPath(source, dest);
			existingPaths.add(path);
		}

		Path bestPath = null;
		if (existingPaths.size() > 0) {
			Collections.sort(existingPaths);
			bestPath = existingPaths.get(0);
		}

		return bestPath;

	}

	public boolean isEligiblePath(Path path) {
		if (pathHasCycles(path))
			return false;
		if (isDuplicatePath(path))
			return false;
		return true;
	}

	public boolean isDuplicatePath(Path path) {
		int duplicates = 0;
		for (int i = 0; i < allPaths.size(); i++) {
			duplicates = 0;
			if (allPaths.get(i).size() == path.size()) {
				for (int j = 0; j < path.size(); j++) {
					if (allPaths.get(i).get(j).compareTo(path.get(j)) == 0)
						duplicates++;
				}
				if (duplicates == path.size())
					return true;
			}
		}
		return false;
	}

	public boolean pathHasCycles(Path path) {
		List<Integer> sourceIDs = new ArrayList<Integer>();
		for (int i = 0; i < path.size(); i++) {
			if (sourceIDs.indexOf((path.get(i).getSource().getId())) != -1)
				return true;
			sourceIDs.add(path.get(i).getSource().getId());
		}
		return false;
	}

	public static void main(String[] args) {
		long startTime = System.nanoTime();
		SillySearch s = new SillySearch();
		s.getShortestRoutes(1, 446);
		System.out.println((System.nanoTime() - startTime) / 1000000000.0);
	}
}
