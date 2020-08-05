package deployment;

import static org.junit.Assert.*;

import java.awt.image.RasterFormatException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Stream;

import javax.lang.model.element.Element;

import org.junit.Test;

public class TestAlgorithmX {
	public List<Point> readFile1(String dataFile) throws IOException {
		Stream<String> rows = Files.lines(Paths.get(dataFile));
//		Stream<String> rows1 = Files.lines(Paths.get("data/1.in"));
		List<Point> points = new ArrayList<>();
		int size = 0;
		rows.map(x -> x.split(",")).filter(x -> x.length == 2).forEach(ele -> {
			Point point = new Point();
			point.x = Integer.parseInt(ele[0]);
			point.y = Integer.parseInt(ele[1]);
			points.add(point);
		});

		return points;
	}

	@SuppressWarnings("boxing")
	public List<Point> readFile(String dataFile) throws IOException {
		@SuppressWarnings("resource")
		Stream<String> rows = Files.lines(Paths.get(dataFile));
		List<Point> points = new ArrayList<>();
		List<Integer> size = new ArrayList<>();

		rows.filter((ele) -> !ele.isEmpty()).map(x -> x.split(",")).forEach(ele -> {
			if (ele.length == 1) {
				size.add(Integer.parseInt(ele[0]));
			} else if (ele.length == 2) {
				Point point = new Point();
				point.x = Integer.parseInt(ele[0]);
				point.y = Integer.parseInt(ele[1]);
				points.add(point);
			}
		});

		assertEquals(size.get(0).intValue(), points.size());
		return points;
	}

	@Test
	public void test_1() throws IOException {
		assertDeployment("data/1.in", 5);
	}

	@Test
	public void test_2() throws IOException {
		assertDeployment("data/2.in", 7);
	}

	@Test
	public void test_3() throws IOException {
		assertDeployment("data/3.in", 7);
	}

	@Test
	public void test_4() throws IOException {
		assertDeployment("data/4.in", 8);
	}

	@Test
	public void test_5() throws IOException {
		assertDeployment("data/5.in", 704);
	}

	@Test
	public void test_6() throws IOException {
		assertDeployment("data/6.in", 84675);
	}

	@Test
	public void test_7() throws IOException {
		assertDeployment("data/7.in", 31);
	}

	@Test
	public void test_8() throws IOException {
		assertDeployment("data/8.in", 37);
	}

	@Test
	public void test_9() throws IOException {
		assertDeployment("data/9.in", 116);
	}

	@Test
	public void test_10() throws IOException {
		assertDeployment("data/10.in", 167);
	}

	@Test
	public void test_11() throws IOException {
		assertDeployment("data/11.in", 1026);
	}

	@Test
	public void test_12() throws IOException {
		assertDeployment("data/12.in", 2877);
	}

	@Test
	public void test_13() throws IOException {
		assertDeployment("data/13.in", 4121);
	}

	@Test
	public void test_14() throws IOException {
		assertDeployment("data/14.in", 11718);
	}

	@Test
	public void test_15() throws IOException {
		assertDeployment("data/15.in", 16256);
	}

	@Test
	public void test_16() throws IOException {
		assertDeployment("data/16.in", 72951);
	}

	@Test
	public void test_() throws IOException {
		assertDeployment("data/17.in", 5);
	}

	@Test
	public void test_18() throws IOException {
		assertDeployment("data/18.in", 149517);
	}

	@Test
	public void test_19() throws IOException {
		assertDeployment("data/19.in", 149494);
	}

	@Test
	public void test_20() throws IOException {
		assertDeployment("data/20.in", 182386);
	}

	@Test
	public void test_21() throws IOException {
		assertDeployment("data/21.in", 44);
	}

	@Test
	public void test_22() throws IOException {
		assertDeployment("data/22.in", 20);
	}

	@Test
	public void test_23() throws IOException {
		assertDeployment("data/23.in", 11);
	}

	@Test
	public void test_24() throws IOException {
		assertDeployment("data/24.in", 13);
	}

	@Test
	public void test_25() throws IOException {
		assertDeployment("data/25.in", 14);
	}

	@Test
	public void test_26() throws IOException {
		assertDeployment("data/26.in", 27);
	}

	@Test
	public void test_27() throws IOException {
		assertDeployment("data/27.in", 28);
	}

	@Test
	public void test_28() throws IOException {
		assertDeployment("data/28.in", 14);
	}

	@Test
	public void test_29() throws IOException {
		assertDeployment("data/29.in", 14);
	}

	@Test
	public void test_30() throws IOException {
		assertDeployment("data/30.in", 112);
	}

	@Test
	public void test_31() throws IOException {
		assertDeployment("data/31.in", 166);
	}

	@Test
	public void test_32() throws IOException {
		assertDeployment("data/32.in", 735);
	}

	@Test
	public void test_33() throws IOException {
		assertDeployment("data/33.in", 1029);
	}

	@Test
	public void test_34() throws IOException {
		assertDeployment("data/34.in", 2036);
	}

	@Test
	public void test_35() throws IOException {
		assertDeployment("data/35.in", 2951);
	}

	@Test
	public void test_36() throws IOException {
		assertDeployment("data/36.in", 4082);
	}

	@Test
	public void test_37() throws IOException {
		assertDeployment("data/37.in", 8173);
	}

	@Test
	public void test_38() throws IOException {
		assertDeployment("data/38.in", 11603);
	}

	@Test
	public void test_39() throws IOException {
		assertDeployment("data/39.in", 16282);
	}

	@Test
	public void test_40() throws IOException {
		assertDeployment("data/40.in", 50844);
	}

	@Test
	public void test_41() throws IOException {
		assertDeployment("data/41.in", 72673);
	}

//	@Test
	public void test_42() throws IOException {
		assertDeployment("data/42.in", 50844);
	}

	@Test
	public void test_43() throws IOException {
		assertDeployment("data/43.in", 203073);
	}

	@Test
	public void test_44() throws IOException {
		assertDeployment("data/44.in", 160);
	}

	@Test
	public void test_45() throws IOException {
		assertDeployment("data/45.in", 165);
	}

	@Test
	public void test_46() throws IOException {
		assertDeployment("data/46.in", 157);
	}

	@Test
	public void test_47() throws IOException {
		assertDeployment("data/47.in", 164);
	}

	@Test
	public void test_48() throws IOException {
		assertDeployment("data/48.in", 164);
	}

	@Test
	public void test_49() throws IOException {
		assertDeployment("data/49.in", 164);
	}

	@Test
	public void test_50() throws IOException {
		assertDeployment("data/50.in", 174);
	}

//	@Test
	public void test_51() throws IOException {
		assertDeployment("data/51.in", 174);
	}

//	@Test
	public void test_52() throws IOException {
		assertDeployment("data/52.in", 174);
	}

//	@Test
	public void test_53() throws IOException {
		assertDeployment("data/53.in", 174);
	}

//	@Test
	public void test_54() throws IOException {
		assertDeployment("data/54.in", 174);
	}

//	@Test
	public void test_55() throws IOException {
		assertDeployment("data/55.in", 174);
	}

	private void assertDeployment(String dataFile, int baseSize) throws IOException {
//		solution1(dataFile, baseSize);
//		solution2(dataFile, baseSize);
//		solution3(dataFile, baseSize);
		testAlgorithm(dataFile, baseSize);
	}

	private void testAlgorithm(String dataFile, int baseSize) throws IOException {
		List<Point> points = readFile(dataFile);
		System.out.println("list's size : " + points.size() + ", test " + dataFile);
		List<List<Point>> solution1 = new AlgorithmX().solution(points);

//		int solutionSize =1;
//		for (List<Point> solution: solution1) {
//			if (solution.size() == 1) {
//				System.out.println("solution size : " + solutionSize + ", count: " + solutionSize);
//				solutionSize++;
//			}
//		}

		List<String> resultPrint = new ArrayList<>();
		List<String> singleResult = new ArrayList<>();
		List<Point> testPoints = new ArrayList<Point>();
		System.out.println("solution1' size : " + Integer.toString(solution1.size()));

		solution1.forEach((ele) -> {
			if (ele.size() == 1) {
				singleResult.add(ele.toString());
			}
			StringBuilder out = new StringBuilder();
			ele.forEach((point) -> {
				out.append(point.x + "," + point.y);
				out.append(";");
				testPoints.add(point);
			});

			resultPrint.add(out.toString().substring(0, out.length() - 1));
		});

		testPoints.sort((o1, o2) -> o1.x -o2.x);
		testPoints.sort((o1, o2) -> o1.y -o2.y);
		resultPrint.forEach(System.out::println);
		System.out.println("all points's size: " + testPoints.size());
		System.out.println("print the test points : ");
		testPoints.forEach(System.out::println);
		System.out.println(testPoints.toString());
		System.out.println("single result: \n" + singleResult.toString());

		writeToFile(dataFile, resultPrint);

		assertEquals(baseSize, solution1.size());
	}

	private void writeToFile(String dataFile, List<String> resultPrint) {
		StringBuilder resultData = new StringBuilder();
		for (String resultString : resultPrint) {
			resultData.append(resultString.toString());
			resultData.append("\n");
		}

		int index = dataFile.indexOf(".");
		String fileName = dataFile.substring(0, index);
		String filePathString = fileName + "_result.out";

		File file = new File(filePathString);
		FileWriter fr = null;
		try {
			fr = new FileWriter(file);
			fr.write(resultData.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// close resources
			try {
				fr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void solution1(String dataFile, int baseSize) throws IOException {
		List<Point> points = readFile(dataFile);
		System.out.println("list's size : " + points.size() + ", test " + dataFile);
		List<List<Point>> solution1 = new DeploymentForTurorials().solution(points);
		List<String> resultPrint = new ArrayList<>();
		resultPrint.add("solution1' size : " + Integer.toString(solution1.size()));

		solution1.forEach((ele) -> {
			StringBuilder out = new StringBuilder();
			ele.forEach((point) -> {
				out.append(point.x + "," + point.y);
				out.append(";");
			});

			resultPrint.add(out.toString().substring(0, out.length() - 1));
		});

		resultPrint.forEach(System.out::println);
	}

	private void solution2(String dataFile, int baseSize) throws IOException {
		List<Point> points = readFile(dataFile);
		System.out.println("list's size : " + points.size() + ", test " + dataFile);
		List<List<Point>> solution2 = new DeploymentForTurorials4().solution(points);
		List<String> resultPrint = new ArrayList<>();
		resultPrint.add("solution2' size : " + Integer.toString(solution2.size()));

		solution2.forEach((ele) -> {
			StringBuilder out = new StringBuilder();
			ele.forEach((point) -> {
				out.append(point.x + "," + point.y);
				out.append(";");
			});

			resultPrint.add(out.toString().substring(0, out.length() - 1));
		});

		resultPrint.forEach(System.out::println);
		assertEquals(baseSize, solution2.size());

	}

	private void solution3(String dataFile, int baseSize) throws IOException {
		List<Point> points = readFile(dataFile);
		System.out.println("list's size : " + points.size() + ", test " + dataFile);
		List<List<Point>> solution3 = new DeploymentForTurorials3().solution(points);
		List<String> resultPrint = new ArrayList<>();
		resultPrint.add("solution3' size : " + Integer.toString(solution3.size()));

		solution3.forEach((ele) -> {
			StringBuilder out = new StringBuilder();
			ele.forEach((point) -> {
				out.append(point.x + "," + point.y);
				out.append(";");
			});

			resultPrint.add(out.toString().substring(0, out.length() - 1));
		});

		resultPrint.forEach(System.out::println);
		assertEquals(baseSize, solution3.size());
	}

	public static void main(String[] args) {
		String fileContent = "	\n" + "	@Test\n" + "	public void test_#() throws IOException {\n"
				+ "		assertDeployment(\"data/#.in\", 5);\n" + "	}\n";
		StringBuilder stringBuilder = new StringBuilder();

		for (int i = 1; i <= 50; i++) {
			String temp = new String(fileContent);
			String newString = temp.replaceAll("#", Integer.toString(i));
			stringBuilder.append(newString);
		}

		System.out.println(stringBuilder.toString());
	}
}
