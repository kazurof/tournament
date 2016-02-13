package tournament;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class FractionTest {

  static final Logger LOGGER = LogManager.getLogger(FractionTest.class);

  @Test
  public void testFactorize() {
    assertThat(Fraction.factorize(1L), is("1"));
    assertThat(Fraction.factorize(2L), is("2"));
    assertThat(Fraction.factorize(3L), is("3"));
    assertThat(Fraction.factorize(4L), is("2x2"));
    assertThat(Fraction.factorize(15L), is("3x5"));
    assertThat(Fraction.factorize(100L), is("2x2x5x5"));
    assertThat(Fraction.factorize(144L), is("2x2x2x2x3x3"));
    assertThat(Fraction.factorize(455L), is("5x7x13"));
    assertThat(Fraction.factorize(457L), is("457"));
  }

  @Test
  public void testFactorizeSquareNumber() {
    assertThat(Fraction.factorize(49L), is("7x7"));

    assertThat(Fraction.factorize(16L), is("2x2x2x2"));
  }

  @Test
  public void testFactorizeLargeCase() {
    assertThat(Fraction.factorize(23753L), is("23753"));
    assertThat(Fraction.factorize(5724247L), is("5724247"));
    assertThat(Fraction.factorize(5724248L), is("2x2x2x353x2027"));
    assertThat(Fraction.factorize(638512875L), is("3x3x3x3x3x3x5x5x5x7x7x11x13"));
  }

  @Test
  public void testToStringWithFactorized() {
    Fraction sut = new Fraction(15, 30);
    assertThat(sut.toStringWithFactorized(), is("1 , 2"));

    sut = new Fraction(202078800, 638512875);
    assertThat(sut.toStringWithFactorized(), is("2x2x2x2x3x3 , 5x7x13"));
  }
}
