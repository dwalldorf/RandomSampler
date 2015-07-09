package dwalldorf.domain;

import java.util.Random;

/**
 * Domain object for one sample of a stream.
 * Contains a char and the it's original position in the stream. Also holds information abouts it's random number.
 */
public class StreamSample {

  private char c;

  private long originalPos;

  private int rand;

  public StreamSample(long originalPos, char c) {
    this.originalPos = originalPos;
    this.c = c;
    this.rand = new Random().nextInt(Integer.MAX_VALUE);
  }

  public char getChar() {
    return c;
  }

  public long getOriginalPos() {
    return originalPos;
  }

  public int getRand() {
    return rand;
  }
}
