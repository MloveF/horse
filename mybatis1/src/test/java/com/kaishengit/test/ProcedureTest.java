package com.kaishengit.test;

import org.junit.Test;

import java.sql.*;

public class ProcedureTest {

    @Test
    public void testJDBC() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql:///mysql_adv","root","022512");

            String sql = "select * from products where prod_id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            preparedStatement.setString(1,"ANV02");
            ResultSet rs = preparedStatement.executeQuery();

            if(rs.next()) {
                System.out.println(rs.getString("prod_name"));
            }



        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    @Test
    public void testPro1() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql:///mysql_adv","root","022512");

            String sql = "{call p1()}";
            CallableStatement callableStatement = conn.prepareCall(sql);
            ResultSet resultSet= callableStatement.executeQuery();
            while(resultSet.next()) {
                System.out.println(resultSet.getString("prod_name"));
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Test
    public void testPro2() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql:///mysql_adv","root","022512");

            String sql = "{call p2(?,?)}";
            CallableStatement callableStatement = conn.prepareCall(sql);

            callableStatement.registerOutParameter(1, Types.DECIMAL);
            callableStatement.registerOutParameter(2, Types.DECIMAL);

            callableStatement.executeUpdate();

            float max = callableStatement.getFloat(1);
            float min = callableStatement.getFloat(2);

            System.out.println(max);
            System.out.println(min);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Test
    public void testPro4() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql:///mysql_adv","root","022512");

            String sql = "{call p4(?,?)}";
            CallableStatement callableStatement = conn.prepareCall(sql);

            callableStatement.setString(1,"ANV02");
            callableStatement.registerOutParameter(2, Types.DECIMAL);

            callableStatement.executeUpdate();

            float price = callableStatement.getFloat(2);

            System.out.println(price);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testPro5() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql:///mysql_adv","root","022512");

            String sql = "{call p5(?,?)}";
            CallableStatement callableStatement = conn.prepareCall(sql);

            callableStatement.setString(1,"ANV02");
            callableStatement.setString(2,"900");

            callableStatement.executeUpdate();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Test
    public void testPro6() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql:///mysql_adv","root","022512");

            String sql = "{call p6(?,?)}";
            CallableStatement callableStatement = conn.prepareCall(sql);

            callableStatement.setString(1,"10 ");
            callableStatement.registerOutParameter(2, Types.INTEGER);

            callableStatement.executeUpdate();

            int count = (int) callableStatement.getFloat(2);

            System.out.println(count);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
