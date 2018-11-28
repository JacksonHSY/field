package com.yuminsoft.ams.system;
import javax.servlet.ServletException;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * tomcat start
 * 
 * @author fuhongxing
 */
public class JobApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobApplication.class);

    private JobApplication() {
    }

    public static void main(String[] args) throws ServletException, LifecycleException {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8081);
        tomcat.getConnector().setURIEncoding("UTF-8");
        String path = JobApplication.class.getResource("/").getPath();
        tomcat.addWebapp("", path.substring(0, path.indexOf("target")) + "src/main/webapp");
        tomcat.start();
        LOGGER.info("Tomcat Started Success");
        tomcat.getServer().await();
    }
}