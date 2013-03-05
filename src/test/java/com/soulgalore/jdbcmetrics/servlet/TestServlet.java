package com.soulgalore.jdbcmetrics.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.ConnectionPoolDataSource;
import javax.sql.DataSource;
import javax.sql.PooledConnection;
import javax.sql.XAConnection;
import javax.sql.XADataSource;

public class TestServlet extends HttpServlet {

	public static final String RESPONSE = "Hello, I am the servlet";
	
	private String dataSourceName = "java:comp/env/jdbc/testDS";
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		if (request.getParameter("read") != null) {
			read(Integer.parseInt(request.getParameter("read")));
		}
		if (request.getParameter("write") != null) {
			read(Integer.parseInt(request.getParameter("write")));
		}
		
		response.getWriter().print(RESPONSE);
	}

	//TODO: move to other class
	private void read(int num) {
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			con = getConnectionDS();
			stmt = con.createStatement();
			for (int i = 0; i < num; i++) { //TODO: maybe get new connection every time?
				rs = stmt.executeQuery("SELECT 1");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (stmt != null) {
					stmt.close();
				}
				if (con != null) {
//					System.out.println("Closing con " + con);
//					System.out.println("Closing con " + ((UserConnection)con).getConnection());
//					((UserConnection)con).getConnection().close(); //resin if wrapped
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private Connection getConnectionDM() throws SQLException {
		return DriverManager.getConnection("jdbc:h2:~/test");
	}

	private Connection getConnectionDS() throws SQLException {
		try {
			Context ctx = new InitialContext();
			DataSource ds = (DataSource) ctx.lookup(dataSourceName);
			return ds.getConnection();
		} catch (NamingException e) {
			throw new SQLException("Lookup of datasource failed", e);
		}
	}
	
	private Connection getConnectionPDS() throws SQLException {
		try {
			Context ctx = new InitialContext();
			ConnectionPoolDataSource ds = (ConnectionPoolDataSource) ctx.lookup(dataSourceName);
			PooledConnection pooledConnection = ds.getPooledConnection(); 
			return pooledConnection.getConnection();
		} catch (NamingException e) {
			throw new SQLException("Lookup of datasource failed", e);
		}
	}

	private Connection getConnectionXADS() throws SQLException {
		try {
			Context ctx = new InitialContext();
			XADataSource ds = (XADataSource) ctx.lookup(dataSourceName);
			XAConnection xaConnection = ds.getXAConnection(); 
			return xaConnection.getConnection();
		} catch (NamingException e) {
			throw new SQLException("Lookup of datasource failed", e);
		}
	}

}
