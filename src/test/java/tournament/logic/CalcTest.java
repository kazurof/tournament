package tournament.logic;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.JUnitCore;


public class CalcTest {
  static final Logger LOGGER = LogManager.getLogger(CalcTest.class);

  public static void main(String[] args) {
    JUnitCore.main(CalcTest.class.getName());
  }


  @Test
  @Ignore
  public void test() {

//    int[][] result = Calc.calcStatistics(1);
//    assertArrayEquals(new int[] {2}, result[0]);
//    assertArrayEquals(new int[] {2}, result[1]);

  }



//  @Test
//  public void testCalcStatistics() {
//
//    // LOGGER.info("---2");
//    // Calc.calcStatistics(2);
//    LOGGER.info("--- 3");
//    Calc.calcStatistics(3);
//    // LOGGER.info("--- 4");
//    // Calc.calcStatistics(4 );
//  }


//  @Test
//  public void testGenerateAllTornament() {
//
//    System.out.println(Calc.generateAllTornament(1));
//    assertThat(Calc.generateAllTornament(1).toString(), is("[[0, 1]]"));
//
//    System.out.println(Calc.generateAllTornament(2));
//
//    assertThat(Calc.generateAllTornament(2).toString(),
//        is("[[0, 1, 2, 3], [0, 2, 1, 3], [0, 3, 1, 2]]"));
//
//
//    List<LinkedList<Integer>> resultBy3 = Calc.generateAllTornament(3);
//
//    // System.out.println(resultBy3);
//
//    assertThat(resultBy3.size(), is(315));
//
//    assertThat(resultBy3.get(0).toString(), is("[0, 1, 2, 3, 4, 5, 6, 7]"));
//    assertThat(resultBy3.get(314).toString(), is("[0, 7, 3, 4, 1, 6, 2, 5]"));
//  }

  @Test
  public void testCalcDoublePermutation() {
    List<List<Integer>> result = new ArrayList<>();
    List<Integer> candidate = new ArrayList<>();
    List<Integer> remains = new ArrayList<>();

    for (int i = 1; i <= 8; i++) {
      remains.add(i);
    }

    Calc.calcDoublePermutation(result, candidate, remains);
    // result.stream().
    for (List<Integer> perm : result) {
      System.out.println(perm);
    }
    System.out.println(result.size());
  }



  @Test
  public void testExecuteTornament() {
    LinkedList<Integer> tornament1 = new LinkedList<>();
    tornament1.addAll(Arrays.asList(3, 2, 1, 0));
    Calc.executeTornament(tornament1, tornament1.size());
    assertThat(tornament1.toString(), is("[0, 2, 3, 1]"));

    LinkedList<Integer> tornament2 = new LinkedList<>();
    tornament2.addAll(Arrays.asList(3, 0, 1, 2));
    Calc.executeTornament(tornament2, tornament2.size());
    assertThat(tornament2.toString(), is("[0, 1, 3, 2]"));


    LinkedList<Integer> tornament3 = new LinkedList<>();
    // tornament3.addAll(Arrays.asList(1, 6, 2, 8, 3, 4, 5, 7));
    tornament3.addAll(Arrays.asList(0, 5, 1, 7, 2, 3, 4, 6));
    Calc.executeTornament(tornament3, tornament3.size());
    LOGGER.info(tornament3);
    assertThat(tornament3.toString(), is("[0, 2, 1, 4, 5, 7, 3, 6]"));
  }



//  @Test
//  public void testAnalyse() {
//    int numOfGame = 3;
//    List<LinkedList<Integer>> allTornament = Calc.generateAllTornament(numOfGame);
//
//
//
//    List<List<Integer>> allResult = new ArrayList<>();
//    for (LinkedList<Integer> oneTornament : allTornament) {
//
//      List<Integer> result = Calc.executeTornament(oneTornament, oneTornament.size());
//      allResult.add(result);
//    }
//
//    int[][] result = Calc.analyse(allResult, numOfGame);
//
//    LOGGER.info(Arrays.deepToString(result));
//
//  }


  @Test
  public void testAnalyseFromFile() {
    // Calc.analyseFromFile(2);
    // Calc.analyseFromFile(3);
    int[][] result = Calc.analyseFromTournamentDataFile(3);
    LOGGER.info(Arrays.deepToString(result));
  }

  @Test
  public void testGenerateResultToFile() {
    // Calc.analyseFromFile(2);
    // Calc.analyseFromFile(3);
    int[][] result = Calc.analyseFromTournamentDataFile(4);
//    int[][] result = Calc.analyseFromTournamentDataFile(4);
//    Calc.generateResultToFile(result, ResultType.RAW_VALUE);
//    Calc.generateResultToFile(result, ResultType.FRACTION);
    Calc.generateResultToFile(result, ResultType.FRACTOIN_IN_FACTORIZATED);
     LOGGER.info(Arrays.deepToString(result));
  }

  @Test
  public void convertResultFile() {
    Calc.convertResultFile(4, ResultType.FRACTION);
    Calc.convertResultFile(8, ResultType.FRACTION);
    Calc.convertResultFile(16, ResultType.FRACTION);
    Calc.convertResultFile(32, ResultType.FRACTION);

  }


  @Test
  public void testCalcNumOfGameByNumOfMember() {
    
    assertThat(Calc.calcNumOfGameByNumOfMember(2), is(1));
    assertThat(Calc.calcNumOfGameByNumOfMember(4), is(2));
    assertThat(Calc.calcNumOfGameByNumOfMember(8), is(3));
    assertThat(Calc.calcNumOfGameByNumOfMember(16), is(4));
    assertThat(Calc.calcNumOfGameByNumOfMember(32), is(5));
    
  }

}

