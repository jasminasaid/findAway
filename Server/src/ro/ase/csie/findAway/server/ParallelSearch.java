package ro.ase.csie.findAway.server;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;

import ro.ase.csie.findAway.server.helpers.AirportsGraphBuilder;
import ro.ase.csie.findAway.server.model.AirportNode;
import ro.ase.csie.findAway.server.model.Path;
import ro.ase.csie.findAway.server.model.api.Airport;

public class ParallelSearch {
	
	ConcurrentHashMap<Airport, List<AirportNode>> airports;

	public ParallelSearch() {
		this.airports = new ConcurrentHashMap<Airport, List<AirportNode>>(
				new AirportsGraphBuilder().build());
	}

	public Airport getAirportByID(int airportID) {
		Airport airport = null;
		for (Airport a : airports.keySet()) {
			if (a.getId() == airportID)
				airport = a;
		}
		return airport;
	}

	public void getShortestPaths(int sourceID, int destID) {
//		Airport source = graph.getAirportByID(sourceID);
//		Airport dest = graph.getAirportByID(destID);
		
		Airport source = null, dest = null;
		
		for(Airport a: airports.keySet()){
			if(a.getId() == sourceID)
				source = a;
			if(a.getId() == destID)
				dest = a;
		}
		
		if (source != null && dest != null) {
			int processors = Runtime.getRuntime().availableProcessors();
			ForkJoinPool pool = new ForkJoinPool(processors);
			SearchTask task = new SearchTask(source, dest, airports);
			pool.execute(task);

			List<Path> paths = task.join();
			int maxPaths = (paths.size() < 30) ? paths.size() : 30;
			Collections.sort(paths);
			for (int i = 0; i < maxPaths; i++) {

				Path path = paths.get(i);
				System.out.println("ROUTE " + (i + 1) + " at "
						+ path.getPathPrice() + "�, in "
						+ path.getPathDuration() + " min");
				path.printPath();
				System.out
						.println("---------------------------------------------------------");
			}
		}
	}

	public static void main(String[] args) {
		long startTime = System.nanoTime();
		ParallelSearch s = new ParallelSearch();
		//OTP-1, IST-17, CTA-446, RNG-144, ZTH-2866, VIE-2, ATH-5, AMS-11, JFK-689, DXB-22
		s.getShortestPaths(1, 446);
		System.out.println((System.nanoTime() - startTime) / 1000000000.0);
	}

}
