package tournament.logic;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ResultTypeTest {

  @Test
  public void testValueOfShortName() {
    assertThat(ResultType.valueOfShortName("raw"), is(ResultType.RAW_VALUE));
    assertThat(ResultType.valueOfShortName("frac"), is(ResultType.FRACTION));
    assertThat(ResultType.valueOfShortName("factor"), is(ResultType.FRACTION_IN_FACTORED));
    assertThat(ResultType.valueOfShortName(null), is(ResultType.RAW_VALUE));
  }
}
