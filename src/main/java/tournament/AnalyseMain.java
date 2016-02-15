package tournament;

import tournament.logic.Calc;
import tournament.logic.ResultType;


public class AnalyseMain {
  public static void main(String[] args) {
    int numOfGame = 3;
    if (args.length != 0) {
      numOfGame = Integer.parseInt(args[0]);
    }


    int[][] result = Calc.analyseFromTournamentDataFile(numOfGame);
    Calc.generateResultToFile(result, ResultType.FRACTOIN_IN_FACTORIZATED);
  }
}
