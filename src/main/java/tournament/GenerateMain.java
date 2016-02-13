package tournament;

import tournament.logic.Calc;

public class GenerateMain {
  public static void main(String[] args) {
    int numOfGame = 1;
    if (args.length != 0) {
      numOfGame = Integer.parseInt(args[0]);
    }
    Calc.generateAllTornamentToFile(numOfGame);
  }
}

