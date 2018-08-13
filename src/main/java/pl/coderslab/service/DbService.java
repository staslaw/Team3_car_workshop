package pl.coderslab.service;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class DbService {
    private static DataSource ds;

    public static Connection createConn() throws SQLException {
        return getInstance().getConnection();
    }
    private static DataSource getInstance() {
        if(ds == null) {
            try {
                Context ctx = new InitialContext();
                ds = (DataSource)ctx.lookup("java:comp/env/jdbc/school");
            } catch (NamingException e) {
                e.printStackTrace();}
        }
        return ds;
    }

    /**
     * Execute insert query and return id of created element, if not then null
     *
     * @param query
     * @param params
     * @return id or null
     * @throws SQLException
     */
    public static Integer insertIntoDatabase(String query, List<String> params) throws SQLException {
        try (Connection conn = createConn()) {
            String[] generatedColumns = {"id"};
            PreparedStatement pst = conn.prepareStatement(query, generatedColumns);
            if (params != null) {
                int i = 1;
                for (String p : params) {
                    pst.setString(i++, p);
                }
            }

            pst.executeUpdate();

            ResultSet res = pst.getGeneratedKeys();

            if (res.next())
                return res.getInt(1);
            else
                return null;

        } catch (SQLException e) {
            throw e;
        }

    }


    public static List<String[]> getData(String query, List<String> params) throws SQLException {
        try (Connection conn = createConn()) {

            //prepare query
            PreparedStatement st = getPreparedStatement(query, params, conn);

            //execute and get results
            ResultSet rs = st.executeQuery();

            //prepare list of results
            List<String[]> result = new ArrayList<>();

            while (rs.next()) {
                //New String array for row data
                String[] row = new String[rs.getMetaData().getColumnCount()];

                for (int i = 0; i < row.length; i++) {
                    row[i] = rs.getString(i + 1);
                }

                result.add(row);
            }

            return result;

        } catch (SQLException e) {
            throw e;
        }

    }

    private static PreparedStatement getPreparedStatement(String query, List<String> params, Connection conn) throws SQLException {
        PreparedStatement st = conn.prepareStatement(query);
        if (params != null) {
            int i = 1;
            for (String p : params) {
                st.setString(i++, p);
            }
        }

        return st;
    }


    public static int executeUpdate(String query, List<String> params)
            throws SQLException {
        try (Connection conn = createConn()) {
            PreparedStatement st = getPreparedStatement(query, params, conn);
            return st.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }

}