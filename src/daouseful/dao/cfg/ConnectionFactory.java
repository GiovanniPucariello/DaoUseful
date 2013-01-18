package daouseful.dao.cfg;

import daouseful.useful.Useful;
import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Daniel Röhers Moura
 */
public class ConnectionFactory {

    public static DB createMongoDbConnection(String database) {
        try {
            Mongo mongo = new Mongo("localhost", 27017);
            DB db = mongo.getDB(database);
            return db;
        } catch (MongoException e) {
            Useful.exceptionMessageConsole(e);
            return null;
        } catch (UnknownHostException e) {
            Useful.exceptionMessageConsole(e);
            return null;
        }
    }

    public static Connection createPostgresqlConnection(String database, String role, String password) {
        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:5432/" + database;
            Connection connection = DriverManager.getConnection(url, role, password);
            return connection;
        } catch (ClassNotFoundException e) {
            Useful.exceptionMessageConsole(e);
            return null;
        } catch (SQLException e) {
            Useful.exceptionMessageConsole(e);
            return null;
        }
    }

    public static Connection createMysqlConnection(String database, String role, String password) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://localhost/" + database + "?user=" + role + "&password=" + password;
            Connection connection = DriverManager.getConnection(url, role, password);
            return connection;
        } catch (ClassNotFoundException e) {
            Useful.exceptionMessageConsole(e);
            return null;
        } catch (SQLException e) {
            Useful.exceptionMessageConsole(e);
            return null;
        }
    }

    public static void closePreparedStatementAndConnection(PreparedStatement preparedStatement, Connection connection) {
        try {
            preparedStatement.close();
        } catch (SQLException e) {
            Useful.exceptionMessageConsole(e);
        }
        try {
            connection.close();
        } catch (SQLException e) {
            Useful.exceptionMessageConsole(e);
        }
    }

    public static void closeResultSetAndPreparedStatementAndConnection(ResultSet resultSet,
            PreparedStatement preparedStatement,
            Connection connection) {
        try {
            resultSet.close();
        } catch (SQLException e) {
            Useful.exceptionMessageConsole(e);
        }
        try {
            preparedStatement.close();
        } catch (SQLException e) {
            Useful.exceptionMessageConsole(e);
        }
        try {
            connection.close();
        } catch (SQLException e) {
            Useful.exceptionMessageConsole(e);
        }
    }
}