package com.j256.ormlite.examples.spring;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.j256.ormlite.jdbc.spring.TableCreator;

/**
 * Main sample routine to show how to do basic operations with the package and Spring.
 */
public class Main {

	// here is a list of XML files to be loaded by Spring
	private String[] configNames = new String[] { "classpath:/com/j256/ormlite/examples/spring/db.xml",
			"classpath:/com/j256/ormlite/examples/spring/someBeans.xml" };

	public static void main(String[] args) throws Exception {
		// turn our static method into an instance of Main
		new Main().doMain(args);
	}

	private void doMain(String[] args) throws Exception {

		/*
		 * the following system properties tell TableCreator to auto-create and drop our test tables. These are probably
		 * only useful for testing. You would _not_ want them in production.
		 */
		System.setProperty(TableCreator.AUTO_CREATE_TABLES, Boolean.toString(true));
		System.setProperty(TableCreator.AUTO_DROP_TABLES, Boolean.toString(true));

		ClassPathXmlApplicationContext context = null;
		try {
			context = new ClassPathXmlApplicationContext(configNames);
			System.out.println("Seemed to have loaded the context correctly");
			// we exit immediately after loading the context -- usually main hangs around
		} catch (Throwable th) {
			throw new Exception("Unable to load the main context", th);
		} finally {
			// the LocalApplicationContext handles he double close problems
			if (context != null) {
				context.close();
				context = null;
			}
		}
	}
}
