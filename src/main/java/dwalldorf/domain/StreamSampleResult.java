package dwalldorf.domain;

import java.util.List;

/**
 * Domain object to wrap a list of {@link StreamSample}s and store additional information about the stream.
 */
public class StreamSampleResult {

  private List<StreamSample> sampleResult;

  private long processedBytes;

  public StreamSampleResult(List<StreamSample> sampleResult, long processedBytes) {
    this.sampleResult = sampleResult;
    this.processedBytes = processedBytes;
  }

  public List<StreamSample> getSamples() {
    return sampleResult;
  }

  public long getProcessedBytes() {
    return processedBytes;
  }

  public String getSampleString() {
    final StringBuffer b = new StringBuffer();
    sampleResult.forEach(r -> {
      b.append(r.getChar());
    });
    return b.toString();
  }

}
