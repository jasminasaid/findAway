package ro.ase.csie.findAway.server;

import java.util.ArrayList;
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

	@Override
	protected List<ExtendedPath> compute() {
		VehicleNode vNode = new VehicleNode();
		vNode.setsPos(source);
		vNode.settPos(dest);
		List<ExtendedPath> paths = new ArrayList<ExtendedPath>();
		List<ExtendedSearchTask> tasks = new ArrayList<ExtendedSearchTask>();
		List<PathNode> nearestNodes = vNode.getNearestNodesFromSource(kdTree);

		for (PathNode node : nearestNodes) {
//			 System.out.println("Thread " + threadID + " computing: ");
//			 node.printNode();
			if (!visitedNodes.contains(node)) {
				visitedNodes.add(node);
				ExtendedPath newPath = new ExtendedPath(path);
				newPath.add(node);
				if (node instanceof AirportNode) {
					AirportNode actual = (AirportNode) node;
					if (isDestinationNode(actual.gettPos(), dest)
							|| isDestinationNode(actual.getfPos(), dest)) {
						paths.add(newPath);
						allPaths.add(newPath);
					} else {
						if (flights.get(actual) != null) {
							for (AirportNode an : flights.get(actual)) {
//								if (!visitedNodes.contains(an)) {
									ExtendedSearchTask newTask = new ExtendedSearchTask(
											an.gettPos(), dest, kdTree,
											flights, visitedNodes, newPath,
											allPaths);
									newTask.fork();
									tasks.add(newTask);
//								}
							}
						}
					}
				} else {
					if (isDestinationNode(node.gettPos(), dest)) {
						paths.add(newPath);
						allPaths.add(newPath);
					} else {
						ExtendedSearchTask newTask = new ExtendedSearchTask(
								node.gettPos(), dest, kdTree, flights,
								visitedNodes, newPath, allPaths);
						newTask.fork();
						tasks.add(newTask);
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

	public boolean isDestinationNode(Position actualPos, Position destPos) {
		return (actualPos.getDistance(destPos) < 30.0) ? true : false;
	}

}
