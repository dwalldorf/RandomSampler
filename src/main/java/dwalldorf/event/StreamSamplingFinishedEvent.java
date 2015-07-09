package dwalldorf.event;

import dwalldorf.domain.StreamSampleResult;
import org.springframework.context.ApplicationEvent;

/**
 * Thrown as the {@link dwalldorf.service.RandomStreamSampler} finishes it's work.
 */
public class StreamSamplingFinishedEvent extends ApplicationEvent {

  private StreamSampleResult result;

  public StreamSamplingFinishedEvent(Object source, StreamSampleResult sampleResult) {
    super(source);
    this.result = sampleResult;
  }

  public StreamSampleResult getResult() {
    return result;
  }
}
