package ro.ase.csie.findAway.server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import ro.ase.csie.findAway.server.helpers.SearchApiHandler;

public class DatabaseSeeder {

	public void seedDatabase() {
		String file = "Routes.txt";
		BufferedReader br = null;
		String line = "";
		String splitter = "-";
		try {

			br = new BufferedReader(new FileReader(file));
			while ((line = br.readLine()) != null) {
				String[] cities = line.split(splitter);
				System.out
						.println(cities[0].trim() + " to " + cities[1].trim());
				SearchApiHandler handler = new SearchApiHandler();
				handler.handleRequest(cities[0].trim(), cities[1].trim());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
	}

//	private double distance(double lat1, double lat2, double lon1, double lon2,
//			double el1, double el2) {
//
//		final int R = 6371; // Radius of the earth
//
//		Double latDistance = deg2rad(lat2 - lat1);
//		Double lonDistance = deg2rad(lon2 - lon1);
//		Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
//				+ Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2))
//				* Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
//		Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
//		double distance = R * c; // in km
//
//		double height = el1 - el2;
//		distance = Math.pow(distance, 2) + Math.pow(height, 2);
//		return Math.sqrt(distance);
//	}

//	private double deg2rad(double deg) {
//		return (deg * Math.PI / 180.0);
//	}

	public static void main(String[] args) {
		 DatabaseSeeder seeder = new DatabaseSeeder();
//		AirportsGraphBuilder graph = new AirportsGraphBuilder();
//		TransitGraphBuilder tGraph = new TransitGraphBuilder();
//		VehicleGraphBuilder vGraph = new VehicleGraphBuilder();
		try {
			 seeder.seedDatabase();
			// handler.handleRequest("Bucharest", "Cappadocia");
			// System.out.println(seeder.distance(42.69771, 42.7093, 23.32306,
			// 23.32278, 0.0, 0.0) + " km");
			//
//			for (int i = 0; i < vGraph.nodes.size(); i++)
//				System.out.println(vGraph.nodes.get(i).getsName() + "--"
//						+ vGraph.nodes.get(i).gettName());

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
