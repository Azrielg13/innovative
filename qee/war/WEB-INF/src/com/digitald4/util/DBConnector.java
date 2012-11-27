/*
 * DBConnector.java
 *
 *
 */

package com.digitald4.util;

/**
 *
 * @author
 */

// Necessary classes to get Pooled Database Connection
import java.sql.*;
//import java.sql.Connection;
//import java.sql.SQLException;



/**
 * This class defines getConnection
 * and releaseConnection methods. In getConnection method, connection
 * caching feature of JDBC 2.0 is implemented to get the database connection.
 */
public class DBConnector {

    private static DBConnector connector_ = null;

    private String url;
    private String user;
    private String password;


    // Create a sinlgeton object
    public static DBConnector getInstance() throws Exception {
        return connector_;
    }

    // Create a sinlgeton object
    public static DBConnector getInstance(String url, String user, String password) throws Exception {
        if(connector_ == null)
            connector_ = new DBConnector(url, user, password);
        return connector_;
    }

    private DBConnector(String url, String user, String password)  throws Exception {
        //
        // First, we'll need a ObjectPool that serves as the
        // actual pool of connections.
        //
        // We'll use a GenericObjectPool instance, although
        // any ObjectPool implementation will suffice.
        //
		Class.forName("org.gjt.mm.mysql.Driver").newInstance();
		this.url = url;
		this.user = user;
		this.password = password;


        //
        // Next, we'll create a ConnectionFactory that the
        // pool will use to create Connections.
        // We'll use the DriverManagerConnectionFactory,
        // using the connect string passed in the command line
        // arguments.
        //
        System.out.println("DB URL: "+url);
    }

    public Connection getConnection() throws Exception {
        return DriverManager.getConnection(url,user,password);
    }

}
