package com.vincent.mahjong;

import org.sqlite.SQLiteDataSource;
import org.sqlite.SQLiteJDBCLoader;

import java.sql.ResultSet;
import java.util.Objects;

public class Test {
    public static void main(String[] args) throws Exception {
        SQLiteJDBCLoader.initialize();
        SQLiteDataSource dataSource = new SQLiteDataSource();

        dataSource.setUrl(
            "jdbc:sqlite:" + Objects.requireNonNull(Test.class.getResource("../../../resources/kiru.sqlite")));
        try {
            ResultSet rs =
                dataSource.getConnection().createStatement().executeQuery("select * from \"questions\"");
            while (rs.next()) {
                System.out.println(rs.getString("questionNo"));
                System.out.println(rs.getString("situation"));
                System.out.println(rs.getString("doraIndicator"));
                System.out.println(rs.getString("question"));
                System.out.println(rs.getString("answer"));
                System.out.println(rs.getString("explanation"));
                System.out.println("================================");
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}


