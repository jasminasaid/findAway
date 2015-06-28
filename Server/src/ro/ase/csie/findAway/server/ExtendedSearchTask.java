package ro.ase.csie.findAway.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RecursiveTask;

import net.sf.javaml.core.kdtree.KDTree;
import ro.ase.csie.findAway.server.model.AirportNode;
import ro.ase.csie.findAway.server.model.ExtendedPath;
import ro.ase.csie.findAway.server.model.PathNode;
import ro.ase.csie.findAway.server.model.VehicleNode;
import ro.ase.csie.findAway.server.model.api.Airport;
import ro.ase.csie.findAway.server.model.api.Position;

public class ExtendedSearchTask extends RecursiveTask<List<ExtendedPath>> {

	private static final long serialVersionUID = 1L;

	Position source, dest;
	KDTree kdTree;
	List<ExtendedPath> allPaths;
	ConcurrentHashMap<Airport, List<AirportNode>> flights;
	// ConcurrentSkipListSet<PathNode> visitedNodes;
	Set<PathNode> visitedNodes;
	ExtendedPath path;
	long threadID;

	public ExtendedSearchTask(Position source, Position dest, KDTree kdTree,
			ConcurrentHashMap<Airport, List<AirportNode>> flights) {
		this.source = source;
		this.dest = dest;
		this.kdTree = kdTree;
		// this.visitedNodes = new ConcurrentSkipListSet<PathNode>();
		this.visitedNodes = new HashSet<PathNode>();
		this.allPaths = new ArrayList<ExtendedPath>();
		this.flights = flights;
		this.threadID = Thread.currentThread().getId();
	}

	public ExtendedSearchTask(Position source, Position dest, KDTree kdTree,
			ConcurrentHashMap<Airport, List<AirportNode>> flights,
			Set<PathNode> visitedNodes, ExtendedPath path,
			List<ExtendedPath> allPaths) {
		this.source = source;
		this.dest = dest;
		this.kdTree = kdTree;
		this.flights = flights;
		this.visitedNodes = visitedNodes;
		this.path = new ExtendedPath(path);
		this.allPaths = allPaths;
		this.threadID = Thread.currentThread().getId();
	}

	//
	@Override
	protected List<ExtendedPath> compute() {
		VehicleNode vNode = new VehicleNode();
		vNode.setsPos(source);
		vNode.settPos(dest);
		List<ExtendedPath> paths = new ArrayList<ExtendedPath>();
		List<ExtendedSearchTask> tasks = new ArrayList<ExtendedSearchTask>();
		List<PathNode> nearestNodes = vNode.getNearestNodesFromSource(kdTree,
				flights);
		ExtendedPath newPath = new ExtendedPath(path);
		for (PathNode node : nearestNodes) {
			// System.out.println("Thread " + threadID + " computing: ");
			// node.printNode();
			if (!visitedNodes.contains(node)) {
				visitedNodes.add(node);
				newPath = new ExtendedPath(path);
				newPath.add(node);
				if (isDestinationNode(node.gettPos(), dest)) {
					if (isEligiblePath(newPath)) {
						paths.add(newPath);
						allPaths.add(newPath);
					}
				} else {
					ExtendedSearchTask newTask = new ExtendedSearchTask(
							node.gettPos(), dest, kdTree, flights,
							visitedNodes, newPath, allPaths);
					newTask.fork();
					tasks.add(newTask);
				}
			} else {
				ExtendedPath existingPath = getBestExistingPath(node.getsPos(),
						dest);
				if (existingPath != null && existingPath.size() > 0) {
					ExtendedPath newExistingPath = new ExtendedPath(newPath);
					newExistingPath.addSubPath(existingPath);
					if (isEligiblePath(newExistingPath)) {
						paths.add(newExistingPath);
						allPaths.add(newExistingPath);
					}

				}
			}
		}
		joinResultsFromTasks(paths, tasks);
		return paths;
	}

	public void joinResultsFromTasks(List<ExtendedPath> paths,
			List<ExtendedSearchTask> tasks) {
		for (ExtendedSearchTask task : tasks) {
			paths.addAll(task.join());
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

}
