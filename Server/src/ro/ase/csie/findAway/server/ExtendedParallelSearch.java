package ro.ase.csie.findAway.server;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;

import net.sf.javaml.core.kdtree.KDTree;
import ro.ase.csie.findAway.server.helpers.AirportsGraphBuilder;
import ro.ase.csie.findAway.server.helpers.PathsKDTreeBuilder;
import ro.ase.csie.findAway.server.model.AirportNode;
import ro.ase.csie.findAway.server.model.ExtendedPath;
import ro.ase.csie.findAway.server.model.api.Airport;
import ro.ase.csie.findAway.server.model.api.Position;

public class ExtendedParallelSearch {

	ConcurrentHashMap<Airport, List<AirportNode>> flights;
	KDTree kdTree;

	public ExtendedParallelSearch() {
		this.flights = new ConcurrentHashMap<Airport, List<AirportNode>>(
				new AirportsGraphBuilder().airports);
		this.kdTree = new PathsKDTreeBuilder().pathsKDTree;
	}

	public void getShortestPaths(String sPlace, String dPlace) {
		// GeocodeApiHandler geocode = new GeocodeApiHandler();
		// Position source = geocode.getPlacePosition(sPlace);
		// Position dest = geocode.getPlacePosition(dPlace);

		Position source = new Position("45.652549743652344,25.609699249267578");
		Position dest = new Position("50.087440490722656,14.421259880065918");
		if (source != null && dest != null) {
			int processors = Runtime.getRuntime().availableProcessors();
			ForkJoinPool pool = new ForkJoinPool(processors);
			ExtendedSearchTask task = new ExtendedSearchTask(source, dest,
					kdTree, flights);
			pool.execute(task);

			List<ExtendedPath> paths = task.join();
			int maxPaths = (paths.size() < 40) ? paths.size() : 40;
			Collections.sort(paths);
			for (int i = 0; i < maxPaths; i++) {

				ExtendedPath path = paths.get(i);
				System.out.println("ROUTE " + (i + 1) + " at "
						+ path.getPathPrice() + "€, in "
						+ path.getPathDuration() + " min");
				path.printPath();
				System.out
						.println("---------------------------------------------------------");
			}
		}
	}

	public static void main(String[] args) {
		long startTime = System.nanoTime();
		ExtendedParallelSearch s = new ExtendedParallelSearch();
		s.getShortestPaths("brasov", "prague");
		System.out.println((System.nanoTime() - startTime) / 1000000000.0);
	}

}
