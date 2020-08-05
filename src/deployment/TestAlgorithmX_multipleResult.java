package deployment;

import static org.junit.Assert.*;

import java.awt.image.RasterFormatException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javax.lang.model.element.Element;

import org.junit.Test;

public class TestAlgorithmX_multipleResult {
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
		assertDeployment("data/1_multiple.in", 0);
	}

	@Test
	public void test_2() throws IOException {
		assertDeployment("data/2_multiple.in", 0);
	}

	@Test
	public void test_3() throws IOException {
		assertDeployment("data/3_multiple.in", 2);
	}

	@Test
	public void test_4() throws IOException {
		assertDeployment("data/4_multiple.in", 0);
	}

	@Test
	public void test_5() throws IOException {
		assertDeployment("data/5_multiple.in", 704-640);
	}

	@Test
	public void test_6() throws IOException {
		assertDeployment("data/6_multiple.in", 84675-84583);
	}

	@Test
	public void test_7() throws IOException {
		assertDeployment("data/7_multiple.in", 31-31);
	}

	@Test
	public void test_8() throws IOException {
		assertDeployment("data/8_multiple.in", 37-27);
	}

	@Test
	public void test_9() throws IOException {
		assertDeployment("data/9_multiple.in", 116-105);
	}
	
	@Test
	public void test_10() throws IOException {
		assertDeployment("data/10_multiple.in", 167-54);
	}

	@Test
	public void test_12() throws IOException {
		assertDeployment("data/12_multiple.in", 2877-2661);
	}

	@Test
	public void test_17() throws IOException {
		assertDeployment("data/17_multiple.in", 5);
	}

	@Test
	public void test_18() throws IOException {
		assertDeployment("data/18_multiple.in", 149517-148526);
	}

	@Test
	public void test_19() throws IOException {
		assertDeployment("data/19_multiple.in", 149494-148563);
	}

	@Test
	public void test_20() throws IOException {
		assertDeployment("data/20_multiple.in", 182386-182368);
	}
	
	@Test
	public void test_21() throws IOException {
		assertDeployment("data/21_multiple.in", 182386-182368);
	}
	
	@Test
	public void test_22() throws IOException {
		assertDeployment("data/22_multiple.in", 0);
	}
	
	@Test
	public void test_23() throws IOException {
		assertDeployment("data/23_multiple.in", 0);
	}
	
	@Test
	public void test_24() throws IOException {
		assertDeployment("data/24_multiple.in", 13-8);
	}
	
	@Test
	public void test_25() throws IOException {
		assertDeployment("data/25_multiple.in", 14-6);
	}
	
	@Test
	public void test_26() throws IOException {
		assertDeployment("data/26_multiple.in", 27-7);
	}
	
	@Test
	public void test_27() throws IOException {
		assertDeployment("data/27_multiple.in", 28-7);
	}
	
	@Test
	public void test_28() throws IOException {
		assertDeployment("data/28_multiple.in", 14-6);
	}
	
	@Test
	public void test_29() throws IOException {
		assertDeployment("data/29_multiple.in", 14-6);
	}
	
	@Test
	public void test_30() throws IOException {
		assertDeployment("data/30_multiple.in", 112-87);
	}
	
	@Test
	public void test_31() throws IOException {
		assertDeployment("data/31_multiple.in", 166-43);
	}
	
	@Test
	public void test_32() throws IOException {
		assertDeployment("data/32_multiple.in", 735-628);
	}
	
	@Test
	public void test_34() throws IOException {
		assertDeployment("data/34_multiple.in", 2036-2000);
	}
	
	@Test
	public void test_35() throws IOException {
		assertDeployment("data/35_multiple.in", 2951-2715);
	}
	
	@Test
	public void test_37() throws IOException {
		assertDeployment("data/37_multiple.in", 8173-8016);
	}
	
	@Test
	public void test_40() throws IOException {
		assertDeployment("data/40_multiple.in", 50844-49933);
	}
	@Test
	public void test_42() throws IOException {
		assertDeployment("data/42_multiple.in", 50844-49933);
	}
	
	@Test
	public void test_44() throws IOException {
		assertDeployment("data/44_multiple.in", 160-47);
	}
	
	@Test
	public void test_46() throws IOException {
		assertDeployment("data/46_multiple.in", 157-77);
	}
	
	@Test
	public void test_47() throws IOException {
		assertDeployment("data/47_multiple.in", 164-45);
	}
	
	@Test
	public void test_48() throws IOException {
		assertDeployment("data/48_multiple.in", 164-14);
	}
	
	@Test
	public void test_50() throws IOException {
		assertDeployment("data/50_multiple.in", 174-34);
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
		List<List<Point>> solution1 = new AlgorithmX_generateMultipleResult().solution(points);
		
//		int solutionSize =1;
//		for (List<Point> solution: solution1) {
//			if (solution.size() == 1) {
//				System.out.println("solution size : " + solutionSize + ", count: " + solutionSize);
//				solutionSize++;
//			}
//		}
		
		List<String> resultPrint = new ArrayList<>();
		System.out.println("solution1' size : " + Integer.toString(solution1.size()));

		solution1.forEach((ele) -> {
			StringBuilder out = new StringBuilder();
			ele.forEach((point) -> {
				out.append(point.x + "," + point.y);
				out.append(";");
			});

			resultPrint.add(out.toString().substring(0, out.length() - 1));
		});

		resultPrint.forEach(System.out::println);
		
		writeToFile(dataFile, resultPrint);
		
		assertEquals(baseSize, solution1.size());
	}

	private void writeToFile(String dataFile, List<String> resultPrint) {
		StringBuilder  resultData = new StringBuilder();
		for (String resultString : resultPrint) {
			resultData.append(resultString.toString());
			resultData.append("\n");
		}
		
		int index = dataFile.indexOf(".");
		String fileName = dataFile.substring(0,index);
		String filePathString =fileName + "_result.out";
		
		File file = new File(filePathString);
        FileWriter fr = null;
        try {
            fr = new FileWriter(file);
            fr.write(resultData.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            //close resources
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
			String newString = temp.replaceAll("#", Integer.toString(i) + "_multiple");
			stringBuilder.append(newString);
		}

		System.out.println(stringBuilder.toString());
	}
}
