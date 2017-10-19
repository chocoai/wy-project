package com.aibei.listener;
import java.io.File;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.aibei.common.FileUtils;

public class SessionListener implements HttpSessionListener {


	@Override
	public void sessionCreated(HttpSessionEvent arg0) {
		// TODO Auto-generated method stub
		String path = arg0.getSession().getServletContext().getRealPath("");
		path = FileUtils.getParent(path)+"/wy_cms/tmp";
		File file = new File(path);
		if(!file.exists())
			file.mkdirs();
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent arg0) {
		// TODO Auto-generated method stub
		String path = arg0.getSession().getServletContext().getRealPath("");
		path = FileUtils.getParent(path)+"/wy_cms/tmp/";
		FileUtils.deleteDirectory(path);
	}

}
