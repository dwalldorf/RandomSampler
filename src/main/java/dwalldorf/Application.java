package dwalldorf;

import dwalldorf.config.AppConfig;
import dwalldorf.service.ApplicationLauncher;
import java.util.Arrays;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Application entry point
 */
public class Application {

  public static void main(String[] args) {
    // setup application ctx
    AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
    ctx.register(AppConfig.class);
    ctx.refresh();

    // run
    ApplicationLauncher launcher = (ApplicationLauncher) ctx.getBean("applicationLauncher");
    launcher.start();
  }

}
