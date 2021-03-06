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
import java.util.stream.Stream;

public class Calc {
  static final Logger LOGGER = LogManager.getLogger(Calc.class);

  static final Logger CONSOLE_MESSAGE = LogManager.getLogger("tournament.consolemessage");

  static String determineTournamentDataFileName(int numOfGame) {
    return String.format("data.%d.tsv", numOfGame);
  }


  /**
   * @param numOfGame height of the tournament. or maximum number of game for one team.
   */
  public static void generateAllTournamentToFile(int numOfGame) {
    CONSOLE_MESSAGE.info(() -> "generating Tournament combination data file for number of game => " + numOfGame);
    Path outFile = Paths.get(determineTournamentDataFileName(numOfGame));
    if (Files.exists(outFile, LinkOption.NOFOLLOW_LINKS)) {
      CONSOLE_MESSAGE.info(() -> outFile + " is already generated.");
      return;
    }

    if (numOfGame == 1) {
      Path path = Paths.get(determineTournamentDataFileName(1));
      try (BufferedWriter bw = Files.newBufferedWriter(path, StandardOpenOption.CREATE)) {
        bw.append("0\t1");
      } catch (IOException ex) {
        LOGGER.info(ex.getMessage(), ex);
      }
      return;
    }

    Path path = Paths.get(determineTournamentDataFileName(numOfGame - 1));

    if (Files.notExists(path, LinkOption.NOFOLLOW_LINKS)) {
      generateAllTournamentToFile(numOfGame - 1);
    }

    int member = (int) Math.pow(2, numOfGame);
    List<Integer> remains = Stream.iterate(0, i -> i + 1).limit(member).collect(Collectors.toList());

    List<List<Integer>> thisPatterns = new LinkedList<>();
    Calc.calcDoublePermutation(thisPatterns, new ArrayList<>(), remains);

    try (BufferedReader br = Files.newBufferedReader(path);
         BufferedWriter writer = Files.newBufferedWriter(outFile, StandardOpenOption.CREATE)) {
      String prev;
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
   * read tournament data(data.[numOfGame].tsv), execute , return result by member.
   */
  public static int[][] analyseFromTournamentDataFile(int numOfGame) {

    int member = (int) Math.pow(2, numOfGame);
    int[][] totalResult = new int[member][];
    for (int i = 0; i < member; i++) {
      totalResult[i] = new int[numOfGame + 1];
    }

    Path path = Paths.get(determineTournamentDataFileName(numOfGame));
    try (BufferedReader br = Files.newBufferedReader(path)) {
      br.lines().map(t -> t.split("\t")).forEach(ary -> {
        LinkedList<Integer> tournament = convert(ary);
        executeTournament(tournament, tournament.size());
        int numOfWin = 0;
        int thisMember = member;

        while (thisMember != 0) {
          int from = thisMember / 2;
          int to = thisMember;
          for (int i = from; i < to; i++) {
            totalResult[tournament.get(i)][numOfWin]++;
          }
          numOfWin++;
          thisMember = from;
        }
      });

    } catch (IOException ex) {
      LOGGER.info(ex.getMessage(), ex);
    }

    return totalResult;
  }

  static LinkedList<Integer> convert(String[] src) {
    return new LinkedList<>(Arrays.stream(src).map(Integer::parseInt).collect(Collectors.toList()));
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
        calcDoublePermutation(result, forNext, removed);
      }
    }
  }

  static List<Integer> executeTournament(LinkedList<Integer> tournament, int end) {
    int until = end / 2;
    for (int i = end; until < i; i--) {
      int left = tournament.get(i - 2);
      int right = tournament.get(i - 1);

      if (left < right) {
        int winner = tournament.remove(i - 2);
        tournament.addFirst(winner);
      } else {
        int winner = tournament.remove(i - 1);
        tournament.addFirst(winner);
      }
    }

    int newEnd = end / 2;
    if (newEnd == 1) {
      return tournament;
    }
    return executeTournament(tournament, newEnd);
  }

  public static void generateResultToFile(int[][] result, ResultType type) {
    int numOfGame = calcNumOfGameByNumOfMember(result.length);
    Path outFile = Paths.get(String.format("result.%d.%s.tsv", numOfGame, type));

    try (BufferedWriter writer = Files.newBufferedWriter(outFile, StandardOpenOption.CREATE)) {
      for (int[] onePerson : result) {
        StringBuilder sb = new StringBuilder();

        List<String> newList = null;

        if (type == ResultType.RAW_VALUE) {
          newList = IntStream.of(onePerson).mapToObj(String::valueOf).collect(Collectors.toList());
        }
        int dominator = result[0][result[0].length - 1];
        if (type == ResultType.FRACTION) {
          newList = IntStream.of(onePerson).mapToObj(a -> new Fraction(a, dominator))
            .map(Fraction::toString).collect(Collectors.toList());
        }
        if (type == ResultType.FRACTION_IN_FACTORED) {
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

  /**
   * @param numOfMember number of member of tournament. should be 2 , 4, 8 , 16 or some 2^n.
   * @return height of the tournament. or maximum number of game for one team.
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


