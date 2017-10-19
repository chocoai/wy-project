package com.test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Test {
	public static void main(String[] args) {
		String databaseName="user";//数据库名字
        String filename="sstud.xlsx";
        try {
            export(databaseName, filename);
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}
	
	private static void export(String databaseName,String filename) throws SQLException {
        String url="jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=utf-8";
        String user="wuye";
        String password="IdsagdpYKFZEOwmvgeO";
        Connection con=DriverManager.getConnection(url, user, password);//获得连接
        DatabaseMetaData dmd=con.getMetaData();//获取数据库元数据
        ResultSet rs=dmd.getTables("user", "user", "", new String[]{"TABLE"});
        List<String> list=new ArrayList<String>();
        while(rs.next()){
            list.add(rs.getString("TABLE_NAME"));
        }
        Statement st=con.createStatement();
        Workbook book=new XSSFWorkbook();
        for(String tbName:list){
            String sql="select * from "+databaseName+"."+tbName;
            ResultSet rs2=st.executeQuery(sql);
            ResultSetMetaData rsmd=rs2.getMetaData();
            int coloums=rsmd.getColumnCount();
            Sheet sheet =book.createSheet(tbName);//创建一个表格
            int rowNum=0;
            Row row=sheet.createRow(rowNum++);
            for(int i=0;i<coloums;i++){
                Cell cell=row.createCell(i);
                cell.setCellValue(rsmd.getColumnName(i+1));
            }
            while(rs2.next()){
                Row row2=sheet.createRow(rowNum++);
                for(int i=0;i<coloums;i++){
                    Cell cell2=row2.createCell(i);
                    cell2.setCellValue(rs2.getString(i+1));
                }
            }

        }
        	System.out.println(book.toString());
        try {
            book.write(new FileOutputStream(filename));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
