package dwalldorf.service;

import dwalldorf.domain.StreamSampleResult;
import dwalldorf.event.StreamSamplingFinishedEvent;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * Handles {@link StreamSamplingFinishedEvent}s.
 */
@Component
public class StreamSamplingFinishedEventHandler implements ApplicationListener<StreamSamplingFinishedEvent> {

  @Override
  public void onApplicationEvent(StreamSamplingFinishedEvent event) {
    logResult(event);
    // do what you need to do with this samples
  }

  private void logResult(StreamSamplingFinishedEvent event) {
    StreamSampleResult result = event.getResult();
    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.GERMAN);

    System.out.println();
    System.out.println("final input size: " + formatter.format(result.getProcessedBytes()));
    System.out.println("result:");
    result.getSamples().forEach(sample -> {
      System.out.println("char: " + sample.getChar() + ", pos in stream: " + formatter.format(sample.getOriginalPos()));
    });
  }

}
