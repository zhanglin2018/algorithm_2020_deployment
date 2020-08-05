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

import deployment.AlgorithmX_generateInputData.PointEnhance;

public class TestAlgorithmX_generateInputData {
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
		assertDeployment("data/10.in", 167-54);
	}

	@Test
	public void test_12() throws IOException {
		assertDeployment("data/12.in", 2877);
	}

	@Test
	public void test_17() throws IOException {
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
		assertDeployment("data/21.in", 182386);
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
	public void test_34() throws IOException {
		assertDeployment("data/34.in", 2036);
	}
	
	@Test
	public void test_35() throws IOException {
		assertDeployment("data/35.in", 2951);
	}
	
	@Test
	public void test_37() throws IOException {
		assertDeployment("data/37.in", 8173);
	}
	
	@Test
	public void test_40() throws IOException {
		assertDeployment("data/40.in", 50844);
	}
	@Test
	public void test_42() throws IOException {
		assertDeployment("data/42.in", 50844);
	}
	
	@Test
	public void test_44() throws IOException {
		assertDeployment("data/44.in", 160);
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
	public void test_50() throws IOException {
		assertDeployment("data/50.in", 174);
	}


	private void assertDeployment(String dataFile, int baseSize) throws IOException {
		testAlgorithm(dataFile, baseSize);
	}

	private void testAlgorithm(String dataFile, int baseSize) throws IOException {
		List<Point> points = readFile(dataFile);
		System.out.println("list's size : " + points.size() + ", test " + dataFile);
		List<PointEnhance> solution1 = new ArrayList<>();
		List<List<Point>> pointResult = new AlgorithmX_generateInputData().solution(points, solution1);
		
		System.out.println(dataFile + " =========data file ending: " + pointResult.size());

		StringBuilder out = new StringBuilder();
		out.append(solution1.size() + "\n");
		solution1.forEach((ele) -> {
			out.append(ele.x + "," + ele.y + "\n");
		});

		
		writeToFile(dataFile, out.toString());
		
//		assertEquals(baseSize, solution1.size());
	}

	private void writeToFile(String dataFile, String resultData) {
		int index = dataFile.indexOf(".");
		String fileName = dataFile.substring(0,index);
		String filePathString =fileName + "_multiple.in";
		
		File file = new File(filePathString);
        FileWriter fr = null;
        try {
            fr = new FileWriter(file);
            fr.write(resultData);
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
}
