package com.protowiki.db;

import java.sql.Connection;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Kobi
 */
public class ConnectionManager {

    private static Logger logger = LoggerFactory.getLogger(ConnectionManager.class);

    public static Connection getConnection() {
        return null;
    }

    public static void close(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException ex) {
            logger.error("SQLException occured while attempting to close SQL connection");
        }
    }
}
