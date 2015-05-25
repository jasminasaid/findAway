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
import ro.ase.csie.findAway.server.model.TransitNode;
import ro.ase.csie.findAway.server.model.api.Position;

public class TransitGraphBuilder implements Constants {

	public List<TransitNode> nodes;

	public TransitGraphBuilder() {
		this.nodes = new ArrayList<TransitNode>();
	}

	public List<TransitNode> build() {
		deserializeGraph();
		return this.nodes;
	}

	public void seedTransitGraph() throws SQLException {
		Connection con = DatabaseConnectionHandler.getInstance()
				.getConnection();
		try {
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM " + TABLE_TRANSITHOPS;
			ResultSet rs = stmt.executeQuery(sql);
			while (rs != null && rs.next()) {
				float price = getPriceByIndicativeID(rs
						.getInt("indicativePriceID"));
				TransitNode newNode = new TransitNode();
				newNode.setsName(rs.getString("sName"));
				newNode.setsPos(new Position(rs.getString("sPos")));
				newNode.settName(rs.getString("tName"));
				newNode.settPos(new Position(rs.getString("tPos")));
				newNode.setFrequency(rs.getFloat("frequency"));
				newNode.setDuration(rs.getFloat("duration"));
				newNode.setPrice(price);
				newNode.setTransitHopID(rs.getInt("ID"));
				newNode.setTransitLegID(rs.getInt("transitLegID"));
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
			FileOutputStream fos = new FileOutputStream("TransitGraph.ser");
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
			FileInputStream fis = new FileInputStream("TransitGraph.ser");
			ObjectInputStream ois = new ObjectInputStream(fis);
			this.nodes = (ArrayList<TransitNode>) ois.readObject();
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
