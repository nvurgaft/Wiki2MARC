package com.protowiki.model;

import com.protowiki.beans.Author;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Kobi
 */
public class SparqlModel {
    
    private static Logger logger = LoggerFactory.getLogger(SparqlModel.class);
    
    private static String GET_AUTHORS = "";
    private static String INSERT_AUTHORS = "";
    
    public String sendRawQuery(String query) {
        
        return null;
    }
    
    public List<Author> getAuthorsWithViafIds(Connection conn) {        
        List<Author> results = new ArrayList<>();
        try (Statement stmt = conn.createStatement()) {        
            try (ResultSet rs = stmt.executeQuery(GET_AUTHORS)) {
                while(rs.next()) {
                    Author author = new Author();
                    author.setName(rs.getString(1));
                    author.setViafId(rs.getLong(2));
                    results.add(author);
                }
            }
        } catch(SQLException ex) {
            logger.error("SQLException while fetching authors from database", ex);
        }
        return results;
    }
    
    public int insertAuthor(Connection conn, Author author) {
        try (PreparedStatement stmt = conn.prepareStatement(INSERT_AUTHORS)) {
            stmt.setString(1, author.getName());
            stmt.setLong(2, author.getViafId());
            stmt.executeUpdate();
        } catch(SQLException ex) {
            logger.error("SQLException while inserting authors to database", ex);
        }
        return 0;
    }
}
