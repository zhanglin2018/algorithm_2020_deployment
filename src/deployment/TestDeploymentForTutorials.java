package deployment;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.Test;

public class TestDeploymentForTutorials {
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
			} else if (ele.length == 2){
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
		assertDeployment("data/5.in", 5);
	}
	
	@Test
	public void test_6() throws IOException {
		assertDeployment("data/6.in", 5);
	}
	
	@Test
	public void test_7() throws IOException {
		assertDeployment("data/7.in", 5);
	}
	
	@Test
	public void test_8() throws IOException {
		assertDeployment("data/8.in", 5);
	}
	
	@Test
	public void test_9() throws IOException {
		assertDeployment("data/9.in", 5);
	}
	
	@Test
	public void test_10() throws IOException {
		assertDeployment("data/10.in", 5);
	}
	
	@Test
	public void test_11() throws IOException {
		assertDeployment("data/11.in", 5);
	}
	
	@Test
	public void test_12() throws IOException {
		assertDeployment("data/12.in", 5);
	}
	
	@Test
	public void test_13() throws IOException {
		assertDeployment("data/13.in", 5);
	}
	
	@Test
	public void test_14() throws IOException {
		assertDeployment("data/14.in", 5);
	}
	
	@Test
	public void test_15() throws IOException {
		assertDeployment("data/15.in", 5);
	}
	
	@Test
	public void test_16() throws IOException {
		assertDeployment("data/16.in", 5);
	}
	
	@Test
	public void test_17() throws IOException {
		assertDeployment("data/17.in", 5);
	}
	
	@Test
	public void test_18() throws IOException {
		assertDeployment("data/18.in", 5);
	}
	
	@Test
	public void test_19() throws IOException {
		assertDeployment("data/19.in", 5);
	}
	
	@Test
	public void test_20() throws IOException {
		assertDeployment("data/20.in", 5);
	}

	private void assertDeployment(String dataFile, int baseSize) throws IOException {
//		solution1(dataFile, baseSize);
		solution2(dataFile, baseSize);
//		solution3(dataFile, baseSize);
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
		String fileContent = "	\n" + 
				"	@Test\n" + 
				"	public void test_#() throws IOException {\n" + 
				"		assertDeployment(\"data/#.in\", 5);\n" + 
				"	}\n";
		StringBuilder stringBuilder = new StringBuilder();
		
		for (int i=1; i<=20; i++) {
			String temp = new String(fileContent);
			String newString = temp.replaceAll("#", Integer.toString(i));
			stringBuilder.append(newString);
		}
		
		System.out.println(stringBuilder.toString());
	}
}
