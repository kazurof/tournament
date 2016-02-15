package tournament.logic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Calc {
  static final Logger LOGGER = LogManager.getLogger(Calc.class);

  static final Logger CONSOLE_MESSAGE = LogManager.getLogger("tournament.consolemessage");
  static String determineTornamentDataFileName(int numOfGame) {
    return String.format("data.%d.tsv", numOfGame);
  }


  /**
   * @param numOfGame height of the tournament. or maximum number of game for one team.
   */
  public static void generateAllTournamentToFile(int numOfGame) {
    CONSOLE_MESSAGE.info(() -> "generating Tournament combination for number of game => " + numOfGame);
    Path outFile = Paths.get(determineTornamentDataFileName(numOfGame));
    if (Files.exists(outFile, LinkOption.NOFOLLOW_LINKS)) {
      CONSOLE_MESSAGE.info(() -> outFile + " is already generated.");
      return ;
    }

    if (numOfGame == 1) {
      Path path = Paths.get(determineTornamentDataFileName(1));
      try (BufferedWriter bw = Files.newBufferedWriter(path, StandardOpenOption.CREATE)) {
        bw.append("0\t1");
      } catch (IOException ex) {
        LOGGER.info(ex.getMessage(), ex);
      }
      return;
    }

    Path path = Paths.get(determineTornamentDataFileName(numOfGame - 1));

    if (Files.notExists(path, LinkOption.NOFOLLOW_LINKS)) {
      generateAllTournamentToFile(numOfGame - 1);
    }


    int member = (int) Math.pow(2, numOfGame);
    List<Integer> remains = new ArrayList<>();
    for (int i = 0; i < member; i++) {
      remains.add(i);
    }
    List<List<Integer>> thisPatterns = new LinkedList<>();
    Calc.calcDoublePermutation(thisPatterns, new ArrayList<>(), remains);



    String prev;

    try (BufferedReader br = Files.newBufferedReader(path);
        BufferedWriter writer = Files.newBufferedWriter(outFile, StandardOpenOption.CREATE)) {
      while ((prev = br.readLine()) != null) {

        String[] oneLine = prev.split("\t");
        for (List<Integer> thispat : thisPatterns) {
          StringBuilder outputSb = new StringBuilder();
          for (String index : oneLine) {
            int startPos = Integer.parseInt(index) * 2;
            outputSb.append(thispat.get(startPos));
            outputSb.append("\t");
            outputSb.append(thispat.get(startPos + 1));
            outputSb.append("\t");
          }

          writer.append(outputSb.substring(0, outputSb.length() - 1));
          writer.append("\n");

        }
      }
    } catch (IOException ex) {
      LOGGER.info(ex.getMessage(), ex);
    }
  }

  /**
   * トーナメントを実施した際の、各チームの勝ち抜き数分布を返します。
   * 
   * @param numOfGame １チームの試合数 あるいはトーナメント表の高さ
   * @return
   */
  public static int[][] calcStatistics(int numOfGame) {
    List<LinkedList<Integer>> allTornament = generateAllTornament(numOfGame);

    LOGGER.info("allTornament.size() " + allTornament.size());

    List<List<Integer>> allResult = new ArrayList<>();
    for (LinkedList<Integer> oneTornament : allTornament) {

      List<Integer> result = executeTornament(oneTornament, oneTornament.size());
      // LOGGER.info(result);
      allResult.add(result);
    }

    return null;
  }


  /**
   * data.[numOfGame].tsv を読み込み、 トーナメントを実行し、参加者ごとの結果を返します。
   */
  static int[][] analyseFromTournamentDataFile(int numOfGame) {

    int member = (int) Math.pow(2, numOfGame);
    int[][] totalResult = new int[member][];
    for (int i = 0; i < member; i++) {
      totalResult[i] = new int[numOfGame + 1];
    }

    String tornament;
    Path path = Paths.get(determineTornamentDataFileName(numOfGame));
    int count = 0;
    try (BufferedReader br = Files.newBufferedReader(path)) {
      while ((tornament = br.readLine()) != null) {
        String[] splitted = tornament.split("\t");

//        LinkedList<Integer> tornament1 = new LinkedList<>();
        LinkedList<Integer> tornament1 = 
            Arrays.stream(splitted).map(Integer::parseInt).collect( 
                LinkedList::new, LinkedList::add ,LinkedList::addAll );
        
//        for (String num : splitted) {
//          tornament1.add(Integer.parseInt(num));
//        }
        executeTornament(tornament1, tornament1.size());

        int numOfWin = 0;
        int thisMenber = member;

        while (thisMenber != 0) {
          int from = thisMenber / 2;
          int to = thisMenber;
          for (int i = from; i < to; i++) {
            int person = tornament1.get(i);
            totalResult[person][numOfWin]++;
          }
          numOfWin++;
          thisMenber = from;
        }

        if (count % 1000000 == 0) {
          LOGGER.info("count  " + count);
        }
        count++;
      }
    } catch (IOException ex) {
      LOGGER.info(ex.getMessage(), ex);
    }

    return totalResult;
  }


  /**
   * 
   */
  static int[][] analyse(List<List<Integer>> allTarget, int numOfGame) {

    int menber = allTarget.get(0).size();
    int[][] totalResult = new int[menber][];
    for (int i = 0; i < menber; i++) {
      totalResult[i] = new int[numOfGame + 1];
    }


    for (List<Integer> target : allTarget) {
      int numOfWin = 0;
      int thisMenber = menber;

      while (thisMenber != 0) {
        int from = thisMenber / 2;
        int to = thisMenber;
        for (int i = from; i < to; i++) {
          int person = target.get(i);
          totalResult[person][numOfWin]++;
        }
        numOfWin++;
        thisMenber = from;
      }

    }
    return totalResult;

  }

  static List<LinkedList<Integer>> generateAllTornament(int numOfGame) {
    if (numOfGame == 1) {
      LinkedList<Integer> oneTornament = new LinkedList<>(Arrays.asList(0, 1));
      List<LinkedList<Integer>> result = Arrays.asList(oneTornament);
      return result;
    }

    int menber = (int) Math.pow(2, numOfGame);
    List<Integer> remains = new ArrayList<>();
    for (int i = 0; i < menber; i++) {
      remains.add(i);
    }
    List<List<Integer>> thisPatterns = new LinkedList<>();
    Calc.calcDoublePermutation(thisPatterns, new ArrayList<>(), remains);

    List<LinkedList<Integer>> prevTornaments = generateAllTornament(numOfGame - 1);
    List<LinkedList<Integer>> result = new ArrayList<>();
    for (List<Integer> tornament : prevTornaments) {
      for (List<Integer> thispat : thisPatterns) {
        LinkedList<Integer> newTornament = new LinkedList<>();
        for (int index : tornament) {
          int startPos = index * 2;
          newTornament.add(thispat.get(startPos));
          newTornament.add(thispat.get(startPos + 1));
        }

        int size = result.size();
        if (size % 10000 == 0) {
          LOGGER.info(result.size());
        }
        result.add(newTornament);
      }
    }
    return result;
  }


  static void calcDoublePermutation(List<List<Integer>> result, List<Integer> candidate,
      List<Integer> remains) {
    int n = remains.size();
    if (n == 0) {
      result.add(candidate);
    } else {
      int num = remains.remove(0);
      List<Integer> addedCandidate = new ArrayList<>();
      if (candidate.isEmpty()) {
        addedCandidate.add(num);
      } else {
        addedCandidate.addAll(candidate);
        addedCandidate.add(num);
      }
      for (int i = 0; i < remains.size(); i++) {
        List<Integer> removed = new ArrayList<>(remains);
        List<Integer> forNext = new ArrayList<>(addedCandidate);
        forNext.add(removed.remove(i));
        // String forNext = addedCandidate + removed.remove(i);
        calcDoublePermutation(result, forNext, removed);
      }
    }
  }

  static List<Integer> executeTornament(LinkedList<Integer> tornament, int end) {
    int until = end / 2;
    for (int i = end; until < i; i--) {
      int left = tornament.get(i - 2);
      int right = tornament.get(i - 1);

      if (left < right) {
        int winner = tornament.remove(i - 2);
        tornament.addFirst(winner);
      } else {
        int winner = tornament.remove(i - 1);
        tornament.addFirst(winner);
      }
    }

    int newEnd = end / 2;
    if (newEnd == 1) {
      return tornament;
    }
    return executeTornament(tornament, newEnd);
  }

  static void generateResultToFile(int[][] result, ResultType type) {
    int numOfGame = calcNumOfGameByNumOfMember(result.length);
    Path outFile = Paths.get(String.format("result.%d.%s.tsv", numOfGame, type));
    // Path outFile = Paths.get("result." + type + ".tsv");
    int max = 1000;
    // int i = 0;
    try (BufferedWriter writer = Files.newBufferedWriter(outFile, StandardOpenOption.CREATE)) {
      for (int[] onePerson : result) {
        StringBuilder sb = new StringBuilder();

        List<String> newList = null;

        if (type == ResultType.RAW_VALUE) {
          newList = IntStream.of(onePerson).mapToObj(String::valueOf).collect(Collectors.toList());
        }
        if (type == ResultType.FRACTION) {
          int dominator = result[0][result[0].length - 1];
          newList = IntStream.of(onePerson).mapToObj(a -> new Fraction(a, dominator))
              .map(Fraction::toString).collect(Collectors.toList());
        }
        if (type == ResultType.FRACTOIN_IN_FACTORIZATED) {
          int dominator = result[0][result[0].length - 1];
          newList = IntStream.of(onePerson).mapToObj(a -> new Fraction(a, dominator))
              .map(Fraction::toStringWithFactorized).collect(Collectors.toList());
        }
        writer.append(String.join("\t", newList));
        writer.append("\n");
      }
    } catch (IOException ex) {
      LOGGER.info(ex.getMessage(), ex);
    }

  }

  static void convertResultFile(int numOfGame, ResultType type) {
    int numOfMember = 0;

    while (numOfGame != 1) {
      numOfMember++;
      numOfGame /= 2;
    }

    LOGGER.info(numOfMember);
  }

  /**
   * @return トーナメントでの１チームの最大試合数。あるいはトーナメント表の高さ
   */

  static int calcNumOfGameByNumOfMember(int numOfMember) {
    int numOfGame = 0;
    while (numOfMember != 1) {
      numOfGame++;
      numOfMember /= 2;
    }
    return numOfGame;
  }

}


