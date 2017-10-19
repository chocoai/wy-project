package com.test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;

public class Test2 {

	public static void main(String[] args) throws IOException {
		
		//创建HSSFWorkbook对象  
		HSSFWorkbook wb = new HSSFWorkbook();  
		HSSFSheet sheet = wb.createSheet("sheet0");  
		//创建HSSFRow对象  
		HSSFRow row = sheet.createRow(0);  
		//创建HSSFCell对象  
		HSSFCell cell=row.createCell(0);  
		row.createCell(0).setCellValue("123");
		FileOutputStream output=new FileOutputStream("d:\\workbook.xls");
		wb.write(output);  
		output.flush();  
	}

}
