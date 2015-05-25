package ro.ase.csie.findAway.server.helpers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionHandler {

	private static DatabaseConnectionHandler instance = null;
	private static Connection con = null;

	private DatabaseConnectionHandler() {
		String url = "jdbc:mysql://localhost:3306/FindAway_DB";
		String username = "root";
		String password = "";
		try {
			// Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			Class.forName("com.mysql.jdbc.Driver");
			// con = DriverManager
			// .getConnection("jdbc:sqlserver://localhost:1433;databaseName=FindAway_DB;user=jasmina;password=1234%asd;");
			con = DriverManager.getConnection(url, username, password);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static synchronized DatabaseConnectionHandler getInstance() {
		try {
			if (DatabaseConnectionHandler.con == null
					|| DatabaseConnectionHandler.con.isClosed()) {
				instance = new DatabaseConnectionHandler();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return instance;
	}

	public Connection getConnection() {
		return con;
	}
}
