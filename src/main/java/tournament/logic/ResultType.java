package tournament.logic;

public enum ResultType {
  RAW_VALUE, FRACTION, FRACTION_IN_FACTORED;

  public static ResultType valueOfShortName(String name) {

    if ("raw".equals(name)) {
      return RAW_VALUE;
    }
    if ("frac".equals(name)) {
      return FRACTION;
    }
    if ("factor".equals(name)) {
      return FRACTION_IN_FACTORED;
    }
    return RAW_VALUE;
  }
}
