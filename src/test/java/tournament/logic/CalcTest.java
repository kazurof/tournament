package tournament.logic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.BufferedReader;
import java.io.StringReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Files.class})
@PowerMockIgnore("javax.management.*")
public class CalcTest {
  static final Logger LOGGER = LogManager.getLogger(CalcTest.class);

  public static void main(String[] args) {
    JUnitCore.main(CalcTest.class.getName());
  }


  @Test
  public void testAnalyseFromTournamentDataFile() throws Exception {
    PowerMockito.mockStatic(Files.class);

    StringBuilder sb = new StringBuilder();
    sb.append("0\t1\t2\t3" + System.lineSeparator());
    sb.append("0\t2\t1\t3" + System.lineSeparator());
    sb.append("0\t3\t1\t1" + System.lineSeparator());

    StringReader sr = new StringReader(sb.toString());
    when(Files.newBufferedReader(any())).thenReturn(new BufferedReader(sr));
    int[][] result = Calc.analyseFromTournamentDataFile(2);

    assertArrayEquals(result[0], new int[]{0,0,3});
    assertArrayEquals(result[1], new int[]{1,2,0});
    assertArrayEquals(result[2], new int[]{2,1,0});
    assertArrayEquals(result[3], new int[]{3,0,0});
    assertThat(result.length , is(4));
  }


  @Test
  public void testCalcDoublePermutation() {
    List<List<Integer>> result = new ArrayList<>();
    List<Integer> remains = Stream.iterate(1, i -> i + 1).limit(2).collect(Collectors.toList());

    Calc.calcDoublePermutation(result, new ArrayList<>(), remains);

    assertThat(result.get(0).toString(), is("[1, 2]"));
    assertThat(result.size(), is(1));
  }

  @Test
  public void testCalcDoublePermutation_4() {
    List<List<Integer>> result = new ArrayList<>();
    List<Integer> remains = Stream.iterate(1, i -> i + 1).limit(4).collect(Collectors.toList());

    Calc.calcDoublePermutation(result, new ArrayList<>(), remains);

    assertThat(result.get(0).toString(), is("[1, 2, 3, 4]"));
    assertThat(result.get(1).toString(), is("[1, 3, 2, 4]"));
    assertThat(result.get(2).toString(), is("[1, 4, 2, 3]"));
    assertThat(result.size(), is(3));
  }


  @Test
  public void testCalcDoublePermutation_8() {
    List<List<Integer>> result = new ArrayList<>();
    List<Integer> remains = Stream.iterate(1, i -> i + 1).limit(8).collect(Collectors.toList());

    Calc.calcDoublePermutation(result, new ArrayList<>(), remains);

    assertThat(result.get(0).toString(), is("[1, 2, 3, 4, 5, 6, 7, 8]"));
    assertThat(result.get(1).toString(), is("[1, 2, 3, 4, 5, 7, 6, 8]"));
    assertThat(result.get(2).toString(), is("[1, 2, 3, 4, 5, 8, 6, 7]"));
    assertThat(result.get(100).toString(), is("[1, 8, 2, 6, 3, 5, 4, 7]"));
    assertThat(result.get(104).toString(), is("[1, 8, 2, 7, 3, 6, 4, 5]"));

    assertThat(result.size(), is(105));
  }


  @Test
  public void testExecuteTournament() {
    LinkedList<Integer> tournament1 = new LinkedList<>();
    tournament1.addAll(Arrays.asList(3, 2, 1, 0));
    Calc.executeTournament(tournament1, tournament1.size());
    assertThat(tournament1.toString(), is("[0, 2, 3, 1]"));

    LinkedList<Integer> tornament2 = new LinkedList<>();
    tornament2.addAll(Arrays.asList(3, 0, 1, 2));
    Calc.executeTournament(tornament2, tornament2.size());
    assertThat(tornament2.toString(), is("[0, 1, 3, 2]"));


    LinkedList<Integer> tornament3 = new LinkedList<>();
    tornament3.addAll(Arrays.asList(0, 5, 1, 7, 2, 3, 4, 6));
    Calc.executeTournament(tornament3, tornament3.size());
    assertThat(tornament3.toString(), is("[0, 2, 1, 4, 5, 7, 3, 6]"));
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

