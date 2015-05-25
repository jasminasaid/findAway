package ro.ase.csie.findAway.server.helpers;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ro.ase.csie.findAway.server.model.AirportNode;
import ro.ase.csie.findAway.server.model.Constants;
import ro.ase.csie.findAway.server.model.api.Airport;
import ro.ase.csie.findAway.server.model.api.FlightHop;
import ro.ase.csie.findAway.server.model.api.Position;

public class AirportsGraphBuilder implements Constants {

	public Map<Airport, List<AirportNode>> airports;

	public AirportsGraphBuilder() {
		this.airports = new HashMap<Airport, List<AirportNode>>();
	}

	public Map<Airport, List<AirportNode>> build() {
		deserializeGraph();
		return this.airports;
	}

	public void seedAirportsGraph() throws SQLException {
		Connection con = DatabaseConnectionHandler.getInstance()
				.getConnection();
		try {
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM " + TABLE_FLIGHTITINERARIES;
			ResultSet rs = stmt.executeQuery(sql);
			while (rs != null && rs.next()) {
				Airport sAirport = getAirportByID(rs.getInt("sID"));
				Airport tAirport = getAirportByID(rs.getInt("tID"));
				Airport fAirport = getAirportByID(rs.getInt("fID"));
				if (!airports.containsKey(sAirport))
					airports.put(sAirport, new ArrayList<AirportNode>());
				List<AirportNode> destinations = airports.get(sAirport);
				AirportNode newNode = new AirportNode();
				newNode.setSource(sAirport);
				newNode.setsPos(new Position(sAirport.getPos()));
				newNode.settDest(tAirport);
				newNode.settPos(new Position(tAirport.getPos()));
				newNode.setfDest(fAirport);
				newNode.setfPos(new Position(fAirport.getPos()));
				newNode.setDuration(rs.getFloat("duration"));
				newNode.setPrice(rs.getFloat("price"));
				newNode.setHasStop(rs.getBoolean("hasStop"));
				newNode.setFlightLegID(rs.getInt("flightLegID"));
				newNode.setIndicativePriceID(rs.getInt("indicativePriceID"));
				newNode.setFlightHopID(rs.getInt("flightHopID"));
				if (destinations.indexOf(newNode) == -1)
					destinations.add(newNode);
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			if (con != null)
				con.close();
		}

		serializeGraph();
	}

	private void serializeGraph() {
		try {
			FileOutputStream fos = new FileOutputStream("AirportsGraph.ser");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(airports);
			oos.close();
			fos.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void deserializeGraph() {
		this.airports = new HashMap<Airport, List<AirportNode>>();

		try {
			FileInputStream fis = new FileInputStream("AirportsGraph.ser");
			ObjectInputStream ois = new ObjectInputStream(fis);
			this.airports = (HashMap<Airport, List<AirportNode>>) ois
					.readObject();
			ois.close();
			fis.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}
	}

	public FlightHop getFlightHopByID(int flightHopID) {
		FlightHop hop = new FlightHop();
		Connection con = DatabaseConnectionHandler.getInstance()
				.getConnection();
		String sql = "SELECT * FROM " + TABLE_FLIGHTHOPS + " WHERE ID=?";
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement(sql);
			stmt.setInt(1, flightHopID);
			stmt.setMaxRows(1);
			rs = stmt.executeQuery();
			if (rs != null && rs.next()) {
				hop = fetchFlightHopFromResultSet(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return hop;
	}

	public Airport getAirportByID(int airportID) {
		Airport airport = new Airport();
		Connection con = DatabaseConnectionHandler.getInstance()
				.getConnection();
		String sql = "SELECT * FROM " + TABLE_AIRPORTS + " WHERE ID=?";
		try {
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setInt(1, airportID);
			stmt.setMaxRows(1);
			ResultSet rs = stmt.executeQuery();
			if (rs != null && rs.next()) {
				airport = fetchAirportFromResultSet(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return airport;
	}

	public Airport fetchAirportFromResultSet(ResultSet rs) {
		Airport airport = new Airport();
		try {
			airport.setId(rs.getInt("ID"));
			airport.setCode(rs.getString("code"));
			airport.setName(rs.getString("name"));
			airport.setPos(rs.getString("pos"));
			airport.setCountryCode(rs.getString("countryCode"));
			airport.setRegionCode(rs.getString("regionCode"));
			airport.setTimeZone(rs.getString("timeZone"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return airport;

	}

	public FlightHop fetchFlightHopFromResultSet(ResultSet rs) {
		FlightHop hop = new FlightHop();
		try {
			hop.setId(rs.getInt("ID"));
			hop.setsCode(rs.getString("sCode"));
			hop.settCode(rs.getString("tCode"));
			hop.setsTerminal(rs.getString("sTerminal"));
			hop.settTerminal(rs.getString("tTerminal"));
			hop.setsTime(rs.getString("sTime"));
			hop.settTime(rs.getString("tTime"));
			hop.setFlight(rs.getString("flight"));
			hop.setAircraft(rs.getString("aircraft"));
			hop.setAirline(rs.getString("airline"));
			hop.setDuration(rs.getFloat("duration"));
			hop.setlDuration(rs.getFloat("lDuration"));
			hop.setDayChange(rs.getInt("dayChange"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return hop;
	}

}
