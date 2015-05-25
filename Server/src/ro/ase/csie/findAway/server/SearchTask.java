package ro.ase.csie.findAway.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.RecursiveTask;

import ro.ase.csie.findAway.server.model.AirportNode;
import ro.ase.csie.findAway.server.model.Path;
import ro.ase.csie.findAway.server.model.api.Airport;

public class SearchTask extends RecursiveTask<List<Path>> {

	private static final long serialVersionUID = 1L;

	Airport source, dest;
	ConcurrentHashMap<Airport, List<AirportNode>> graph;
	List<Path> allPaths;
	ConcurrentSkipListSet<Airport> visitedAirports;
	ConcurrentSkipListSet<AirportNode> visitedNodes;
	Path path;
	long threadID;

	public SearchTask(Airport source, Airport dest,
			ConcurrentHashMap<Airport, List<AirportNode>> graph) {
		this.source = source;
		this.dest = dest;
		this.graph = graph;
		this.visitedAirports = new ConcurrentSkipListSet<Airport>();
		this.visitedNodes = new ConcurrentSkipListSet<AirportNode>();
		this.path = new Path();
		// this.allPaths = new CopyOnWriteArrayList<Path>();
		this.allPaths = new ArrayList<Path>();
		this.threadID = Thread.currentThread().getId();
	}

	public SearchTask(Airport source, Airport dest,
			ConcurrentHashMap<Airport, List<AirportNode>> graph,
			ConcurrentSkipListSet<Airport> visitedAirports,
			ConcurrentSkipListSet<AirportNode> visitedNodes, Path path,
			List<Path> allPaths) {
		this.source = source;
		this.dest = dest;
		this.graph = graph;
		this.visitedAirports = visitedAirports;
		this.visitedNodes = visitedNodes;
		this.path = new Path(path);
		this.allPaths = allPaths;
		this.threadID = Thread.currentThread().getId();
	}

	@Override
	protected List<Path> compute() {
		List<Path> paths = new ArrayList<Path>();
		List<SearchTask> tasks = new ArrayList<SearchTask>();

		if (!visitedAirports.contains(source)) {
			visitedAirports.add(source);
			// System.out.println("Thread: " + threadID + " computing "
			// + source.getCode());
			List<AirportNode> nodes = graph.get(source);
			if (nodes != null) {
				for (AirportNode an : nodes) {
					if (!visitedNodes.contains(an)) {
						visitedNodes.add(an);
						Path newPath = new Path(path);
						newPath.add(an);
						if (isDestinationNode(an, dest)) {
							if (isEligiblePath(newPath)) {
								paths.add(newPath);
								allPaths.add(newPath);
							}
						} else {
							if (!visitedAirports.contains(an.gettDest())) {
								SearchTask newTask = new SearchTask(
										an.gettDest(), dest, graph,
										visitedAirports, visitedNodes, newPath,
										allPaths);
								newTask.fork();
								tasks.add(newTask);
							} else {
								Path existingPath = getBestExistingPath(
										an.gettDest(), dest);
								if (existingPath != null) {
									newPath.addSubPath(existingPath);
									if (isEligiblePath(newPath)
											&& existingPath.size() > 0) {
										paths.add(newPath);
										allPaths.add(newPath);
									}
								}
							}
						}
					}
				}
			}
		} else {
			Path existingPath = getBestExistingPath(source, dest);
			if (existingPath != null) {
				Path newPath = new Path(path);
				newPath.addSubPath(existingPath);
				if (isEligiblePath(newPath) && existingPath.size() > 0) {
					paths.add(newPath);
					allPaths.add(newPath);
				}
			}
		}
		joinResultsFromTasks(paths, tasks);
		return paths;
	}

	public void joinResultsFromTasks(List<Path> paths, List<SearchTask> tasks) {
		for (SearchTask task : tasks) {
			paths.addAll(task.join());
		}
	}

	public boolean isDestinationNode(AirportNode node, Airport dest) {
		return (node.gettDest().getId() == dest.getId() || node.getfDest()
				.getId() == dest.getId()) ? true : false;
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
}
