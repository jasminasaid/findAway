package ro.ase.csie.findAway.server;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
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
				new AirportsGraphBuilder().build());
		this.kdTree = new PathsKDTreeBuilder().build();
	}

	public void getShortestPaths(String sPlace, String dPlace) {
		// GeocodeApiHandler geocode = new GeocodeApiHandler();
		// Position source = geocode.getPlacePosition(sPlace);
		// Position dest = geocode.getPlacePosition(dPlace);

//		 Position source = new
//		 Position("45.652549743652344,25.609699249267578");
//		 Position dest = new
//		 Position("50.087440490722656,14.421259880065918");

		Position source = new Position("44.43614,26.10274");
		Position dest = new Position("40.39251,49.84668");
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
						+ path.getPathPrice() + "�, in "
						+ path.getPathDuration() / 60 + " hours");
				path.printPath();
				System.out
						.println("---------------------------------------------------------");
			}
		}
	}

	public static void main(String[] args) throws InterruptedException,
			ExecutionException {
		long startTime = System.nanoTime();
		// ExtendedParallelSearch s = new ExtendedParallelSearch();
		// s.getShortestPaths("brasov", "prague");
		// ExecutorService executor = Executors.newSingleThreadExecutor();
		// Future<String> future = executor.submit(new Task());

		ExtendedParallelSearch s = new ExtendedParallelSearch();
		s.getShortestPaths("brasov", "prague");
		System.out.println((System.nanoTime() - startTime) / 1000000000.0);

		// try {
		// System.out.println("Started..");
		// System.out.println(future.get(3, TimeUnit.SECONDS));
		// System.out.println("Finished!");
		// } catch (TimeoutException ex) {
		// future.cancel(true);
		// System.out.println("Terminated"); 
		// } finally {
		// executor.shutdown();
		// System.out.println((System.nanoTime() - startTime) / 1000000000.0);
		// }
	}

}

class Task implements Callable<String> {

	@Override
	public String call() throws Exception {
		while (!Thread.interrupted()) {
			ExtendedParallelSearch s = new ExtendedParallelSearch();
			s.getShortestPaths("brasov", "prague");
			Thread.sleep(1000);
		}
		return "Ready";
	}

}
