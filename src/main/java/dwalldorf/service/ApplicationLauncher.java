package dwalldorf.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Launches the spring application and processes the specified stream (set with --Dfile= or --Dstring=).
 * Will use example input (see main/resources/example.txt), if no input specified.
 */
@Component
public class ApplicationLauncher {

  @Autowired
  private RandomStreamSampler randomStreamSampler;

  @Autowired
  private Environment env;

  public void start() {
    final int k = Integer.parseInt(env.getProperty("config.k"));

    // launch info
    System.out.println("starting");
    System.out.println("using k=" + k);

    InputStream inputStream = null;
    try {
      inputStream = getStream();
      randomStreamSampler.consume(inputStream, k);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (inputStream != null) {
        try {
          inputStream.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * Determines the specified input and returns a stream of it.
   *
   * @return stream of user-specified input
   */
  private InputStream getStream() throws FileNotFoundException {
    String inputFile = env.getProperty("config.file");
    String inputString = env.getProperty("config.string");

    // specified file
    if (!inputFile.isEmpty()) {
      System.out.println("using file: " + inputFile);
      return new FileInputStream(new File(inputFile));
    }
    // specified string
    else if (!inputString.isEmpty()) {
      System.out.println("using string: " + inputString);
      return IOUtils.toInputStream(inputString);
    }
    // no input specified, use example file
    else {
      System.out.println("using example file");
      return getExampleInputStream();
    }
  }

  private InputStream getExampleInputStream() throws FileNotFoundException {
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    return classLoader.getResourceAsStream("example.txt");
  }

}
