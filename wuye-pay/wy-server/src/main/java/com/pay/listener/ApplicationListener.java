package com.pay.listener;

import java.io.File;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.pay.common.FileUtils;

public class ApplicationListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		//销毁二维码
		String path = arg0.getServletContext().getRealPath("");
		path = FileUtils.getParent(path)+"/wy-client/images/";
		FileUtils.deleteDirectory(path);
		
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		//创建二维码的文件夹
		String path = arg0.getServletContext().getRealPath("");
		path = FileUtils.getParent(path)+"/wy-client/images";
		File file = new File(path);
		if(!file.exists())
			file.mkdirs();
		
	}

}
