package dwalldorf.service;

import dwalldorf.domain.StreamSample;
import dwalldorf.domain.StreamSampleResult;
import dwalldorf.event.StreamSamplingFinishedEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.apache.commons.io.input.CountingInputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * Component that takes random samples of a stream.
 */
@Component
public class RandomStreamSampler {

  @Autowired
  private ApplicationEventPublisher eventPublisher;

  /**
   * Map of {@link StreamSample}s. Stores every {@code StreamSample} with a random key.
   */
  List<StreamSample> resultList;

  int k;

  /**
   * Will parse {@code stream} and return a List of {@code StreamSamples} of size {@code k}.
   *
   * @param stream stream to consume
   * @param k      number of samples to return
   */
  public void consume(InputStream stream, final int k) {
    CountingInputStream countingInputStream = new CountingInputStream(stream);
    resultList = new ArrayList<>();
    this.k = k;

    BufferedReader reader = null;
    try {
      reader = new BufferedReader(new InputStreamReader(countingInputStream));

      // number of bytes processed
      long count = 0;
      int currentByte;
      StreamSample currentSample;

      while ((currentByte = reader.read()) != -1) {
        currentSample = new StreamSample(count, (char) currentByte);
        takeSample(currentSample);
        count++;
      }

      // processing finished -> throw event
      StreamSampleResult result = new StreamSampleResult(resultList, countingInputStream.getByteCount());
      StreamSamplingFinishedEvent finishedEvent = new StreamSamplingFinishedEvent(this, result);
      eventPublisher.publishEvent(finishedEvent);
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * Will add {@code sample} to the resultList.
   *
   * @param sample to evaluate and add
   */
  private void takeSample(final StreamSample sample) {
    long originalPos = sample.getOriginalPos();

    if (originalPos < k) {
      resultList.add(sample);
    } else {
      StreamSample entry = resultList.get(new Random().nextInt(resultList.size()));

      if (entry.getRand() < sample.getRand()) {
        resultList.remove(entry);
        resultList.add(sample);
      }
    }
  }
}
