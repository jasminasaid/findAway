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
import java.util.List;

import ro.ase.csie.findAway.server.model.Constants;
import ro.ase.csie.findAway.server.model.VehicleNode;
import ro.ase.csie.findAway.server.model.api.Position;

public class VehicleGraphBuilder implements Constants {

	public List<VehicleNode> nodes;

	public VehicleGraphBuilder() {
		this.nodes = new ArrayList<VehicleNode>();
	}

	public List<VehicleNode> build() {
		deserializeGraph();
		return this.nodes;
	}

	public void seedVehicleGraph() throws SQLException {
		Connection con = DatabaseConnectionHandler.getInstance()
				.getConnection();
		try {
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM " + TABLE_OTHERSEGMENTS;
			ResultSet rs = stmt.executeQuery(sql);
			while (rs != null && rs.next()) {
				float price = getPriceByIndicativeID(rs
						.getInt("indicativePriceID"));
				VehicleNode newNode = new VehicleNode();
				newNode.setKind(rs.getString("kind"));
				newNode.setSubkind(rs.getString("subkind"));
				newNode.setVehicle(rs.getString("vehicle"));
				newNode.setsName(rs.getString("sName"));
				newNode.setsPos(new Position(rs.getString("sPos")));
				newNode.settName(rs.getString("tName"));
				newNode.settPos(new Position(rs.getString("tPos")));
				newNode.setDistance(rs.getFloat("distance"));
				newNode.setDuration(rs.getFloat("duration"));
				newNode.setPrice(price);
				newNode.setPath(rs.getString("path"));
				newNode.setSegmentID(rs.getInt("ID"));
				newNode.setRouteID(rs.getInt("routeID"));
				nodes.add(newNode);
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
			FileOutputStream fos = new FileOutputStream("VehicleGraph.ser");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(nodes);
			oos.close();
			fos.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void deserializeGraph() {

		try {
			FileInputStream fis = new FileInputStream("VehicleGraph.ser");
			ObjectInputStream ois = new ObjectInputStream(fis);
			this.nodes = (ArrayList<VehicleNode>) ois.readObject();
			ois.close();
			fis.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}
	}

	public float getPriceByIndicativeID(int indicativePriceID) {
		float price = 0;
		Connection con = DatabaseConnectionHandler.getInstance()
				.getConnection();
		String sql = "SELECT price FROM " + TABLE_INDICATIVEPRICES
				+ " WHERE ID=?";
		try {
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setInt(1, indicativePriceID);
			stmt.setMaxRows(1);
			ResultSet rs = stmt.executeQuery();
			if (rs != null && rs.next()) {
				price = rs.getFloat(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return price;
	}

}
