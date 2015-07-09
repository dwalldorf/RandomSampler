package helper;

import org.junit.Before;
import org.mockito.MockitoAnnotations;

/**
 * Base test for unit tests
 */
public class BaseTest {

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
  }

}
