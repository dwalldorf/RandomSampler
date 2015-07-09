package dwalldorf.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import dwalldorf.domain.StreamSampleResult;
import dwalldorf.event.StreamSamplingFinishedEvent;
import helper.BaseTest;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.context.ApplicationEventPublisher;

/**
 * Unit test for {@link RandomStreamSampler}.
 */
public class RandomStreamSamplerTest extends BaseTest {

  @InjectMocks
  private RandomStreamSampler sampler;

  @Mock
  private ApplicationEventPublisher eventPublisher;

  @Captor
  private ArgumentCaptor<StreamSamplingFinishedEvent> samplingFinishedEventCaptor;

  @Test
  public void throwsStreamSamplingFinishedEvent() {
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    InputStream resourceAsStream = classLoader.getResourceAsStream("4096.txt");
    sampler.consume(resourceAsStream, 5);

    verify(eventPublisher).publishEvent(any(StreamSamplingFinishedEvent.class));
  }

  @Test
  public void returnsSampleOfSizeK() {
    final int k = 12;

    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    InputStream resourceAsStream = classLoader.getResourceAsStream("4096.txt");
    sampler.consume(resourceAsStream, k);
    verify(eventPublisher).publishEvent(samplingFinishedEventCaptor.capture());

    StreamSampleResult sampleResult = samplingFinishedEventCaptor.getValue().getResult();
    assertEquals(k, sampleResult.getSamples().size());
  }

  @Test
  public void processesCompleteFile() throws Exception {
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    InputStream resourceAsStream = classLoader.getResourceAsStream("4096.txt");
    sampler.consume(resourceAsStream, 5);

    verify(eventPublisher).publishEvent(samplingFinishedEventCaptor.capture());
    assertEquals(4096, samplingFinishedEventCaptor.getValue().getResult().getProcessedBytes());
  }

  @Test
  public void sampleDiff() throws Exception {
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

    int i = 0;
    int x = 10;
    int k = 5;
    // process the same input x times
    for (; i < x; i++) {
      InputStream resourceAsStream = classLoader.getResourceAsStream("4096.txt");
      sampler.consume(resourceAsStream, k);
    }
    verify(eventPublisher, times(x)).publishEvent(samplingFinishedEventCaptor.capture());

    // calculate difference between every picket sample
    List<Long> posList = new ArrayList<>();
    samplingFinishedEventCaptor.getAllValues().forEach(e -> {
      e.getResult().getSamples().forEach(r -> {
        posList.add(r.getOriginalPos());
      });
    });

    // calculate average difference between every sample
    long diffSum = 0;
    long diffAmount = 0;
    for (i = 0; i < posList.size(); i++) {
      for (int i2 = i; i2 < posList.size(); i2++) {
        if (i2 == i) {
          continue;
        }

        long diff = Math.abs(posList.get(i) - posList.get(i2));
        diffSum = diffSum + diff;
        diffAmount++;
      }
    }
    long avgDiff = diffSum / diffAmount;
    assertTrue(avgDiff >= 1024);
  }

  @Test
  public void streamSmallerThanK() {
    int k = 10;
    String inputStr = "1234";

    InputStream stream = IOUtils.toInputStream(inputStr);
    sampler.consume(stream, k);
    verify(eventPublisher).publishEvent(samplingFinishedEventCaptor.capture());

    StreamSamplingFinishedEvent event = samplingFinishedEventCaptor.getValue();
    assertEquals(inputStr.length(), event.getResult().getSamples().size());
    assertEquals(inputStr, event.getResult().getSampleString());
  }

  @Test
  public void streamEqualToK() {
    int k = 5;
    String inputStr = "12345";

    InputStream stream = IOUtils.toInputStream(inputStr);
    sampler.consume(stream, k);
    verify(eventPublisher).publishEvent(samplingFinishedEventCaptor.capture());

    StreamSamplingFinishedEvent event = samplingFinishedEventCaptor.getValue();
    assertEquals(inputStr.length(), event.getResult().getSamples().size());
    assertEquals(inputStr, event.getResult().getSampleString());
  }

}
