package connect_mysql;

import java.sql.*;

public class Mysql {
    
    private final static boolean debug = true;
    protected Connection connect = null;
    protected Statement statement = null;
    protected ResultSet result = null;
    protected PreparedStatement pstatement = null; 

    public Mysql() {
    }

    public void show_table_columns(Connection connect) throws SQLException {
        if (debug) {
            ResultSet result = connect.createStatement().executeQuery("select * from movie_booking.booking_login"); 
            System.out.println("Table: " + result.getMetaData().getTableName(1));
            System.out.println("The columns in the table are: ");
            int column_count = result.getMetaData().getColumnCount();
            for(int i = 1; i <= column_count; i++) {
                System.out.println("Column " +i  + " "+ result.getMetaData().getColumnName(i));   
            }
        }
    }
    
    protected Connection connect_db() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection("jdbc:mysql://localhost/movie_booking?"+"user=dirtyvoid&password=password");
    }

    protected void preparedQuery(String... args) throws Exception {
        pstatement = connect.prepareStatement(args[0]);
        for(int i = 1; i < args.length; i++) {
            pstatement.setString(i, args[i]);
        }
        result = pstatement.executeQuery();
    }

    protected void preparedUpdate(String... args) throws Exception {
        pstatement = connect.prepareStatement(args[0]);
        for(int i = 1; i < args.length; i++) {
            pstatement.setString(i, args[i]);
        }
        pstatement.executeUpdate();
    }

    protected void close() {
        try {
            if(result != null) {
                result.close();
                result = null;
            }
            if(statement != null) {
                statement.close();
                statement = null;
            }
            if(pstatement != null) {
                pstatement.close();
                pstatement = null;
            }
            if(connect !=null) {
                connect.close();
                connect = null;
            }
        } catch (SQLException e) {/*ignore*/}
    }

}
