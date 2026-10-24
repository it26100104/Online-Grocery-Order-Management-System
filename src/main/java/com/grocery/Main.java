package com.grocery;

import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

import java.io.File;


public class Main {

    public static void main(String[] args) throws Exception {
        int port = 8080;
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(port);
        tomcat.getConnector(); // Initializes default HTTP connector

        // Base directory for Tomcat temp files
        String baseDir = new File("target/tomcat").getAbsolutePath();
        tomcat.setBaseDir(baseDir);

        // Webapp source directory
        String docBase = new File("src/main/webapp").getAbsolutePath();
        StandardContext ctx = (StandardContext) tomcat.addWebapp("", docBase);

        // Load compiled classes into Tomcat's resource loader
        File additionWebInfClasses = new File("target/classes");
        if (additionWebInfClasses.exists()) {
            WebResourceRoot resources = new StandardRoot(ctx);
            resources.addPreResources(new DirResourceSet(
                    resources,
                    "/WEB-INF/classes",
                    additionWebInfClasses.getAbsolutePath(),
                    "/"
            ));
            ctx.setResources(resources);
        }

        System.out.println("==================================================");
        System.out.println(" Grocery App starting on http://localhost:" + port);
        System.out.println(" Default Admin:    admin@grocery.com / Admin@123");
        System.out.println(" Default Customer: john@grocery.com  / Customer@123");
        System.out.println("==================================================");

        tomcat.start();
        tomcat.getServer().await();
    }
}
