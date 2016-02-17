package tournament;

import tournament.logic.Calc;
import tournament.logic.ResultType;


public class AnalyseMain {
  public static void main(String[] args) {
    int numOfGame = 3;
    if (1 <= args.length ) {
      numOfGame = Integer.parseInt(args[0]);
    }
    ResultType type = ResultType.RAW_VALUE;
    if (2 <= args.length ) {
      type = ResultType.valueOfShortName(args[1]);
    }

    int[][] result = Calc.analyseFromTournamentDataFile(numOfGame);
    Calc.generateResultToFile(result, type);
  }
}
