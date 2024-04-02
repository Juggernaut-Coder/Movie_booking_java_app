package CreateMovDatabase;

import java.io.File;
import java.util.Random;
import java.sql.Time;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

import connect_mysql.Mysql;

public class movData extends Mysql {

    public void turncateTable(String table) throws Exception {
        String mysql = "TRUNCATE TABLE ";
        mysql = mysql + table;
        try {
            connect = connect_db();
            statement = connect.createStatement();
            statement.executeUpdate(mysql);
        } catch (Exception e) {
            throw e;
        } finally {
            close();
        }
    }
    
    public void resetTable() throws Exception {
        turncateTable("movieList");
        turncateTable("reservedSeat");
        String mysql = "INSERT INTO movieList (movieName, theatreID, screen ) VALUES (?, ?, ?)";
        try {
            File myObj = new File("./CreateMovDatabase/movlist.txt");
            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            Scanner myReader = new Scanner(myObj);
            ArrayList<String> dates = new ArrayList<String>();
            final Random random = new Random();    
            final int millisInDay = 24*60*60*1000;
            connect = connect_db();
            for(int i = 0; i < 7; i++) {
                LocalDate date = LocalDate.now().plusDays(i);
                dates.add(date.format(format));
            }
            while (myReader.hasNextLine()) {
                Time time = new Time((long)random.nextInt(millisInDay));
                String datetime = dates.get(random.nextInt(dates.size())) + " " + time.toString();
                String theatreID = String.valueOf(random.nextInt(2));
                preparedUpdate(mysql,myReader.nextLine(),theatreID,datetime);
            }
            myReader.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close();
        }
    }


     public static void main(String[] args) throws Exception {
        new movData().resetTable();
    }
}
