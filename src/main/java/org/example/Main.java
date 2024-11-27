package org.example;

import org.apache.catalina.LifecycleException;
import org.example.timbering.Timberland;
import org.apache.catalina.startup.Tomcat;
import java.io.File;

public class Main {

  private static final String TAG   = "Main";
  private static final Integer PORT = 9055;

  public static void main(String... args) {
    try {
      startTomcat().getServer().await();
    } catch (LifecycleException le) {
      Timberland.cutDEAD(TAG, "Couldn't start the server!" + le, le.toString());
    }
  }

  private static Tomcat startTomcat() throws LifecycleException {
    Tomcat tomcat = new Tomcat();
    tomcat.setPort(PORT);
    tomcat.getConnector();
    tomcat.addWebapp("", new File("src/main/webapp").getAbsolutePath());
    tomcat.start();
    return tomcat;
  }
}