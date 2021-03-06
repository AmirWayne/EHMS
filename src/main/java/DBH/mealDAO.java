package DBH;


import Model.Allergy;
import Model.Meal;
import Model.Medicine;
import Util.DatabaseConnector;
import Util.JPQLHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class mealDAO  {

    static Connection con = DatabaseConnector.getConnection();


    public int insertMeal(Meal m) throws SQLException {

        String sql = "insert into meal(name, weight) values(?,?)";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, m.getName());
        ps.setInt(2, m.getWeight());

        int rows = ps.executeUpdate();

        ps.close();

        return rows;
    }



    public ArrayList<Meal> selectAll() throws SQLException {

        ArrayList<Meal> list = new ArrayList<Meal>();
        String sql = "select * from meal";

        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
           Meal m  = new Meal(rs.getString("name"),rs.getInt("weight"));
           list.add(m);
        }

        ps.close();
        rs.close();
        return list;
    }

    public ObservableList<Meal> selectAllObservable() throws SQLException {

        ObservableList list =FXCollections.observableArrayList();
        String sql = "select * from meal";

        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Meal m  = new Meal(rs.getString("name"),rs.getInt("weight"));
            list.add(m);
        }

        ps.close();
        rs.close();
        return list;
    }

    public void UpdateMeal(Meal m ,String oldname) throws SQLException {

        String sql1 = "update meal SET name = ?, weight=? where name=?";
        PreparedStatement ps = con.prepareStatement(sql1);

        ps.setString(1,m.getName());
        ps.setInt(2, m.getWeight());
        ps.setString(3, oldname);

        int rows = ps.executeUpdate();

        ps.close();
    }

    public void removeMealByName(String name) throws SQLException {
        String sql1 = "DELETE FROM meal WHERE meal.name=? ";
        PreparedStatement ps = con.prepareStatement(sql1);

        ps.setString(1 , name);

        ps.executeUpdate();
        ps.close();
    }

    public int getCount() throws SQLException {
        int numberRow = 0;
        String query = "select count(*) from meal";
        PreparedStatement st = con.prepareStatement(query);
        ResultSet rs = st.executeQuery();
        while (rs.next()) {
            numberRow = rs.getInt("count(*)");
        }
        return numberRow;
    }


    public ArrayList<String> MealPDF() throws SQLException {
        ArrayList<String> list = new ArrayList<String>();
        String sql = "select * from meal";
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        String str="";

        while (rs.next()) {
            Meal m  = new Meal(rs.getString("name"),rs.getInt("weight"));
            str= m.getName() + "          |          " + m.getWeight();
            list.add(str);
        }
        ps.close();
        rs.close();

        return list;
    }
}
