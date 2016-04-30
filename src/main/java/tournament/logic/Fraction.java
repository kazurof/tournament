package tournament.logic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Fraction {

  static final Logger LOGGER = LogManager.getLogger(Fraction.class);

  long numerator;
  long dominator;

  public Fraction(long n, long d) {
    numerator = n;
    dominator = d;
    if (numerator != 0) {
      reduce();
    }
  }

  public String toString() {
    if (dominator == 1 || numerator == 0) {
      return numerator + "";
    }
    return numerator + " , " + dominator;
  }

  /**
   * factorize and convert to String as,
   * <pre>numerator, dominator</pre>
   */
  public String toStringWithFactorized() {
    if (dominator == 1 || numerator == 0) {
      return numerator + "";
    }
    return factorize(numerator) + " , " + factorize(dominator);
  }

  /**
   * factorize the value.
   * return "2x2x5x5" for 100.
   */
  static String factorize(long val) {
    if (val == 1L) {
      return "1";
    }
    List<Long> result = new ArrayList<>();
    BigInteger bi = BigInteger.ONE;
    double root = Math.sqrt(val);
    while (bi.doubleValue() <= root && val != 1L) {
      long prime = bi.nextProbablePrime().longValue();
      while (val % prime == 0L) {
        result.add(prime);
        val = val / prime;
      }
      bi = BigInteger.valueOf(prime);
    }
    if (1L < val) {
      result.add(val);
    }
    return result.stream().map(String::valueOf).collect(Collectors.joining("x"));
  }


  private void reduce() {
    long gcd = gcd(numerator, dominator);
    numerator /= gcd;
    dominator /= gcd;
  }

  private static long gcd(long a, long b) {
    while (a > 0 && b > 0) {
      if (a > b) {
        a = a % b;
      } else {
        b = b % a;
      }
      if (a == 0) {
        return b;
      } else if (b == 0) {
        return a;
      }
    }
    //illegal state.
    return -1;
  }
}
