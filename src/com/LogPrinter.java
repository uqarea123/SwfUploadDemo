﻿package com;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * 
 * @author 周勇
 *
 */
public class LogPrinter {
	private static Properties properties = new Properties();


	public synchronized static void info(String info){
		properties = getProperties();
		PropertyConfigurator.configure( properties );
		Logger.getLogger(LogPrinter.class).info(info);
		
	}
	
	public synchronized static void debug(String debug) {
		properties = getProperties();
		PropertyConfigurator.configure( properties);
		Logger.getLogger(LogPrinter.class).debug(debug);
	}
	
	public synchronized static void fatal(String fatal) {
		properties = getProperties();
		PropertyConfigurator.configure( properties);
		Logger.getLogger(LogPrinter.class).fatal(fatal);
	}

	public synchronized static void error(String error) {
		properties = getProperties();
		PropertyConfigurator.configure( properties);
		Logger.getLogger(LogPrinter.class).error(error);
	}
	
	//配置文件
	private static Properties getProperties(){
		Properties properties = new Properties();
		properties.setProperty("log4j.rootCategory", "debug,A1,A2");
		//输出控制台
		properties.setProperty("log4j.appender.A1", "org.apache.log4j.ConsoleAppender");
		properties.setProperty("log4j.appender.A1.Target", "System.out");
		properties.setProperty("log4j.appender.A1.layout", "org.apache.log4j.PatternLayout");
		properties.setProperty("log4j.appender.A1.layout.ConversionPattern", "[hongling] %d %5p[%t]  - %m%n");
		
		//文件类型
		properties.setProperty("log4j.appender.A2", "org.apache.log4j.FileAppender");
		//日志路径
		properties.setProperty("log4j.appender.A2.File", "d:/log.txt");
		properties.setProperty("log4j.appender.A2.File.Encoding", "UTF-8");
		
		properties.setProperty("log4j.appender.A2.layout", "org.apache.log4j.PatternLayout");
		properties.setProperty("log4j.appender.A2.layout.ConversionPattern", "[hongling] %d - %-5p %x - %m  -%-4r [%t] %n");
		
		return properties;
	}
}
