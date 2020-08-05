package deployment;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/*
 * 1. find the minimum number from all the PointEnhance. ->
 * 2. 
 */

public class AlgorithmX {

	static class Point_Item {
		public int x;
		public int y;
		public List<Solution_Item> solutions;
		public boolean noUsed;

		public Point_Item(int x2, int y2) {
			this.x = x2;
			this.y = y2;
			noUsed = true;
			solutions = new ArrayList<>();
		}
		
		public int solutionSize() {
			int sum = 0;
			for (Solution_Item solution : solutions) {
				if (solution.noUsed) {
					sum++;
				}
			}

			// consider the orphan point.
			if (sum == 0) {
				return 1;
			}
			return sum;
		}
		
		@Override
		public String toString() {
			return "(" + x + ", " + y + ") solutionSize=" + solutions.size() + ", noUsed =" + noUsed +  "\n";
		}
	}

	static class Solution_Item {
		public List<Point_Item> points;
		public boolean noUsed;

		public Solution_Item() {
			points = new ArrayList<>();
			noUsed = true;
		}

		public int pointCount() {
			int pointCount = 0;
			
			for (Point_Item point : points) {
				if (point.noUsed) {
					pointCount++;
				}
			}
			return pointCount;
		}

		public List<Point_Item> noUsedPoints() {
			List<Point_Item> noUsedPoints = new ArrayList<>();

			for (Point_Item point : points) {
				if (point.noUsed) {
					noUsedPoints.add(point);
				}
			}

			return noUsedPoints;
		}

		@Override
		public String toString() {
			StringBuilder stringBuilder = new StringBuilder();
			points.forEach((ele) -> stringBuilder.append("(" + ele.x + "," + ele.y + ")"));
			stringBuilder.append(", noUsed = " + noUsed + "\n");
			return stringBuilder.toString();
		}
	}

	static class Result {
		public List<Solution_Item> solutions;
		public Result parent;
		public List<Result> children;
		public int pointCount;
		public int solutionCount;
		public static List<Result> results = new ArrayList<>();

		@Override
		public String toString() {
			return "Result [solutions=" + solutions + ", pointCount=" + pointCount
					+ ", solutionCount=" + solutionCount + "]";
		}

		public Result() {
			super();
			solutions = new ArrayList<>();
			children = new ArrayList<>();
			pointCount = 0;
			solutionCount = 0;
			parent = null;
		}

		public void addResult(Solution_Item solution) {
			solutions.add(solution);
			solutionCount++;
			pointCount += solution.points.size();
		}

		public void addResult(List<Solution_Item> otherSolutions) {
			for (Solution_Item solution : otherSolutions) {
				solutions.add(solution);
				solutionCount++;
				pointCount += solution.points.size();
			}
		}

		public void addResult(Result result) {
			pointCount += result.pointCount;
			solutionCount += result.solutionCount;
		}

		public Result cloneResult() {
			Result result = new Result();
			result.pointCount = this.pointCount;
			result.solutionCount = this.solutionCount;
			children.add(result);
			result.parent = this;
			return result;
		}

		public List<Solution_Item> printSolution(Result result, int pointSize) {
			if (!results.isEmpty()) {
				results.clear();
			}

			List<Result> allResults = findAllResult(result, pointSize);
			allResults.sort((o1, o2) -> o1.solutionCount - o2.solutionCount);

			Result optimalResult = allResults.get(0);

			List<Solution_Item> optimalSolutions = new ArrayList<>();
			while (optimalResult != null) {
				optimalSolutions.addAll(optimalResult.solutions);
				optimalResult = optimalResult.parent;
			}

			return optimalSolutions;
		}

		public List<Result> findAllResult(Result rootResult, int pointSize) {
			if (rootResult == null) {
				return results;
			}

			if (rootResult.children.isEmpty()) {
				if (rootResult.pointCount == pointSize) {
					results.add(rootResult);
				}
				return results;
			}

			for (Result result : rootResult.children) {
				findAllResult(result, pointSize);
			}

			return results;
		}
	}

	public static class Cell {
		public int value;
		public boolean marked;
		public int nextX;
		public int nextY;
		public Point point;

		public Cell() {
			super();
			this.value = 0;
			this.marked = false;
			nextX = 0;
			nextY = 0;
		}

		@Override
		public String toString() {
			return "Cell [value=" + value + ", " + "(" + point.x + ", " + point.y + ") -> " + "(" + nextX + ", " + nextY
					+ ")";
		}
	}

	public static class DfsMazeSearch {
		public int MAX_ROW = 2022;
		public int MAX_COlUMN = 2022;
		public int actualMaxRow;
		public int actualMaxColumn;
		public Cell[][] maze = new Cell[MAX_ROW][MAX_COlUMN];
		public int[][] next = new int[][] { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } }; // right, down, left, top
		public List<Point> multiplePoints;
		public List<Point> singlePoints;
		public List<List<Point>> solutions;

		public DfsMazeSearch(List<Point> multiplePoints, List<Point> singlePoints,
				List<List<Point>> solutions) {
			super();
			this.multiplePoints = multiplePoints;
			this.singlePoints = singlePoints;
			this.solutions = solutions;
		}
		
		public void initMazeMatrix() {
			actualMaxColumn = 0;
			actualMaxRow = 0;
			List<Point> checkList = new ArrayList<>();
			solutions.forEach((ele) -> checkList.addAll(ele));

			for (Point point : multiplePoints) {
				int x = point.x;
				int y = point.y;
				maze[x][y] = new Cell();
				maze[x][y].value = 1;
				maze[x][y].point = point;
				if (x > actualMaxRow) {
					actualMaxRow = x;
				}
				if (y > actualMaxColumn) {
					actualMaxColumn = y;
				}
			}

			actualMaxRow += 2;
			actualMaxColumn += 2;
			
			for (List<Point> points : solutions) {
				if (points.size() == 1) {
					continue;
				}

				Point point1 = points.get(0);
				Point point2 = points.get(1);
				Cell cell1 = maze[point1.x][point1.y];
				Cell cell2 = maze[point2.x][point2.y];
				cell1.nextX = point2.x;
				cell1.nextY = point2.y;
				cell2.nextX = point1.x;
				cell2.nextY = point1.y;
			}
		}
		
		public void clearMark() {
			for (int i = 0; i < actualMaxRow; i++) {
				for (int j = 0; j < actualMaxColumn; j++) {
					Cell currentCell = maze[i][j];
					if (currentCell == null) {
						continue;
					}
					currentCell.marked = false;
				}
			}
		}
		
		public List<List<Point>> processDfsResult() {
			Set<Point> matchedPoint = new HashSet<>();
			List<List<Point>> result = new ArrayList<>();

			for (Point point : multiplePoints) {
				if (matchedPoint.contains(point)) {
					continue;
				}

				int x = point.x;
				int y = point.y;
				List<Point> points = new ArrayList<>();
				Cell currentCell = maze[x][y];
				int nextX = currentCell.nextX;
				int nextY = currentCell.nextY;
				if (nextX == 0 && nextY == 0) {
					points.add(point);
					result.add(points);
					matchedPoint.add(point);
				} else {
					Cell nextCell = maze[nextX][nextY];
					Point otherPoint = nextCell.point;
					points.add(point);
					points.add(otherPoint);
					result.add(points);
					matchedPoint.add(point);
					matchedPoint.add(otherPoint);
				}
			}

			return result;
		}

		public boolean dfs(int startX, int startY, List<Point> otherPoints, List<Point> results) {
			if (otherPoints.isEmpty()) {
				return true;
			}
			
			for (Point matchPoint : otherPoints) {
				int match_x = matchPoint.x;
				int match_y = matchPoint.y;
				int x_diff = startX - match_x;
				int y_diff = startY - match_y;
				
				if ((startX == match_x && Math.abs(y_diff) == 1) || (startY == match_y && Math.abs(x_diff) == 1)) {
					results.add(matchPoint);
					Cell startCell = maze[startX][startY];
					Cell matchedCell = maze[match_x][match_y];
					startCell.nextX = match_x;
					startCell.nextY = match_y;
					matchedCell.nextX = startX;
					matchedCell.nextY = startY;
					return true;
				}
			}
			
			for (int possible = 0; possible < next.length; possible++) {
				int nextX;
				int nextY;
				nextX = startX + next[possible][0];
				nextY = startY + next[possible][1];
				
				if (nextX < 0 || nextX >= MAX_ROW || nextY < 0 || nextY >= MAX_COlUMN || maze[nextX][nextY] == null) {
					continue;
				}
				
				Cell startCell = maze[startX][startY];
				Cell nextCell = maze[nextX][nextY];
				
				if (nextCell.value == 1 && (!nextCell.marked)) {
					nextCell.marked = true;
					
					int oldStartX = startCell.nextX;
					int oldStartY = startCell.nextY;
					int next_startX = nextCell.nextX;
					int next_startY = nextCell.nextY;
					
					startCell.nextX = nextX;
					startCell.nextY = nextY;
					nextCell.nextX = startX;
					nextCell.nextY = startY;
					
					maze[next_startX][next_startY].nextX = 0;
					maze[next_startX][next_startY].nextY = 0;
					
					boolean result = dfs(next_startX, next_startY, otherPoints, results);
					if (result) {
						return true;
					}
					
					maze[next_startX][next_startY].nextX = nextX;
					maze[next_startX][next_startY].nextY = nextY;
					nextCell.nextX = next_startX;
					nextCell.nextY = next_startY;
					
					startCell.nextX = oldStartX;
					startCell.nextY = oldStartY;
				}
			}
			return false;
		}
		
		public List<List<Point>> clusteringAlgorithm() {
			if (singlePoints.isEmpty() || singlePoints.size() == 1 || solutions.isEmpty()) {
				return solutions;
			}
			
			initMazeMatrix();
			int singlePointSize = singlePoints.size();
			List<Point> matchedPoints = new ArrayList<>();
			System.out.println("all single point's : " + singlePointSize);
			
			for (int i = 0; i < singlePointSize - 1; i++) {
				if (matchedPoints.contains(singlePoints.get(i))) {
					continue;
				}
				
				List<Point> otherPoints = new ArrayList<>();
				for (int j = i + 1; j < singlePointSize; j++) {
					if (!matchedPoints.contains(singlePoints.get(j))) {
						otherPoints.add(singlePoints.get(j));
					}
				}
				
				Point startPoint = singlePoints.get(i);
				
				boolean isSucceed = dfs(startPoint.x, startPoint.y, otherPoints, matchedPoints);
				if (isSucceed) {
					matchedPoints.add(startPoint);
				}
				
				clearMark();
			}
			
			return processDfsResult();
		}

	}

	public void initAllPointsAndSolutions(List<Point> list, List<Point_Item> points,
			List<Solution_Item> allSolutions) {
		int max_row = 0;
		int max_column = 0;
		
		// init base matrix
		for (Point point : list) {
			if (point.x > max_row) {
				max_row = point.x;
			}
			
			if (point.y > max_column) {
				max_column = point.y;
			}
		}
		max_row += 2;
		max_column += 2;
		
		
		Point_Item[][] pointMatrix = new Point_Item[max_row][max_column];
		
		for (Point point : list) {
			int x = point.x;
			int y = point.y;
			pointMatrix[x][y] = new Point_Item(x, y);
			points.add(pointMatrix[x][y]);
		}

		for (Point_Item point : points) {
			int x = point.x;
			int y = point.y;

			Point_Item top_point = pointMatrix[x - 1][y];
			Point_Item down_point = pointMatrix[x + 1][y];
			Point_Item left_point = pointMatrix[x][y - 1];
			Point_Item right_point = pointMatrix[x][y + 1];

			int possibleSize = 0;
			if (top_point != null) {
				possibleSize += 1;
			}
			if (down_point != null) {
				possibleSize += 1;
			}
			if (left_point != null) {
				possibleSize += 1;
			}
			if (right_point != null) {
				possibleSize += 1;
			}

			if (possibleSize == 0) {
				Solution_Item solution = new Solution_Item();
				solution.points.add(point);
				point.solutions.add(solution);
				allSolutions.add(solution);
				continue;
			}

			if (right_point != null) {
				createSolution(allSolutions, points, point, right_point);
			} // right -> better case.
			if (down_point != null) {
				createSolution(allSolutions, points, point, down_point);
			}
		}
	}

	public void createSolution(List<Solution_Item> solutions, List<Point_Item> points,
			Point_Item point1, Point_Item point2) {
		Solution_Item solution = new Solution_Item();
		solution.points.add(point1);
		solution.points.add(point2);
		point1.solutions.add(solution);
		point2.solutions.add(solution);
		solutions.add(solution);
	}

	public List<Solution_Item> validSolution(Point_Item point) {
		List<Solution_Item> validSolution = new ArrayList<>();

		for (Solution_Item solution : point.solutions) {
			if (solution.noUsed) {
				validSolution.add(solution);
			}
		}

		if (validSolution.isEmpty()) {
			Solution_Item solution = new Solution_Item();
			solution.points.add(point);
			validSolution.add(solution);
		}

		return validSolution;
	}

	public void cover(Solution_Item solution, Set<Point_Item> usedPoints, Set<Solution_Item> usedSolution) {
		List<Point_Item> points = solution.points;

		for (Point_Item point : points) {
			if (point.noUsed) {
				point.noUsed = false;
				usedPoints.add(point);
			}

			List<Solution_Item> solutions = point.solutions;
			for (Solution_Item otherSolution : solutions) {
				if (otherSolution.noUsed) {
					otherSolution.noUsed = false;
					usedSolution.add(otherSolution);
				}
			}
		}
	}

	public void uncover(List<Point_Item> allcolumnItems, List<Solution_Item> allrowItems,
			Set<Point_Item> usedPoints, Set<Solution_Item> usedSolution) {
		usedPoints.forEach((ele) -> ele.noUsed = true);
		usedSolution.forEach((ele) -> ele.noUsed = true);
		allcolumnItems.addAll(0, usedPoints);
		allrowItems.addAll(0, usedSolution);
		usedPoints.clear();
		usedSolution.clear();
	}

	public List<List<Point>> generateResult(Result result, int pointSize) {
		List<List<Point>> finalResult = new ArrayList<>();

		List<Solution_Item> optimalSolution = result.printSolution(result, pointSize);

		for (Solution_Item solution : optimalSolution) {
			List<Point_Item> points = solution.points;
			List<Point> resultPoints = new ArrayList<Point>();
			
			for (Point_Item point : points) {
				Point newPoint = new Point();
				newPoint.x = point.x;
				newPoint.y = point.y;
				resultPoints.add(newPoint);
			}
			finalResult.add(resultPoints);
		}

		return finalResult;
	}

	public void processSinglePoints(List<Point_Item> points, List<Solution_Item> solutions,
			Set<Point_Item> usedPoints, Set<Solution_Item> usedSolution, Result result) {
		if (points.isEmpty()) {
			return;
		}

		boolean containSinglePoint = false;

		for (Point_Item point : points) {
			if (!point.noUsed) {
				continue;
			}

			if (point.solutionSize() == 1) {
				containSinglePoint = true;
				List<Solution_Item> validSolutions = validSolution(point);
				Solution_Item validSolution = validSolutions.get(0);
				result.addResult(validSolution);
				cover(validSolution, usedPoints, usedSolution);
			}
		}

		points.removeAll(usedPoints);
		solutions.removeAll(usedSolution);
		if (!containSinglePoint) {
			return;
		}

		processSinglePoints(points, solutions, usedPoints, usedSolution, result);
	}

	public void processMultiplePoints(List<Point_Item> points, List<Solution_Item> solutions,
			Set<Point_Item> usedPoints, Set<Solution_Item> usedSolutions, Result result) {
		if (points.isEmpty()) {
			return;
		}
		
		// iterator two possibles: all down and all right
		for (int i=0; i<2; i++) {
			Result childResult = result.cloneResult();
			
			for (Point_Item point : points) {
				if (!point.noUsed) {
					continue;
				}
				
				int solutionSize = point.solutionSize();
				
				if (solutionSize == 1) {
					List<Solution_Item> validSolutions = validSolution(point);
					Solution_Item validSolution = validSolutions.get(0);
					childResult.addResult(validSolution);
					cover(validSolution, usedPoints, usedSolutions);
				} else if (solutionSize >= 2) {
					List<Solution_Item> validSolutions = validSolution(point);
					Solution_Item validSolution = validSolutions.get(i);
					childResult.addResult(validSolution);
					cover(validSolution, usedPoints, usedSolutions);
				}
			}
			
			points.removeAll(usedPoints);
			solutions.removeAll(usedSolutions);
			
			processMultiplePoints(points, solutions, usedPoints, usedSolutions, result);
			
			// recover the previous environment.
			uncover(points, solutions ,usedPoints, usedSolutions);
		}
	}
	
	public List<List<Point>> solution(List<Point> list) {
		List<Point_Item> points = new ArrayList<Point_Item>();

		List<Solution_Item> solutions = new ArrayList<Solution_Item>();

		initAllPointsAndSolutions(list, points, solutions);

		Set<Point_Item> usedPoints = new LinkedHashSet<>();
		Set<Solution_Item> usedSolution = new LinkedHashSet<>();

		// 1. process the orphan and single points.
		Result singleResult = new Result();
		processSinglePoints(points, solutions, usedPoints, usedSolution, singleResult);
		int singlePointSize = list.size() - points.size();
		List<List<Point>> singleSolutions = generateResult(singleResult, singlePointSize);

		// 2. process the multiple points.
		Result multipleResult = new Result();
		int multiplePointCount = points.size();
		List<Point> multiplePoints = generateAllMultipleNodes(points);
		
		usedPoints.clear();
		usedSolution.clear();
		processMultiplePoints(points, solutions, usedPoints, usedSolution, multipleResult);
		List<List<Point>> multipleSolutions = generateResult(multipleResult, multiplePointCount);

		// 3. DfsMazeSearch
		List<Point> singlePoints = findAllSinglePoints(multipleSolutions);
		DfsMazeSearch dfsMazeSearch = new DfsMazeSearch(multiplePoints, singlePoints, multipleSolutions);
		List<List<Point>> finalResult = new ArrayList<>();
		List<List<Point>> matchedResults = dfsMazeSearch.clusteringAlgorithm();
		finalResult.addAll(singleSolutions);
		finalResult.addAll(matchedResults);

		return finalResult;
	}

	public List<Point> generateAllMultipleNodes(List<Point_Item> points) {
		if (points.isEmpty()) {
			return Collections.emptyList();
		}

		List<Point> newPoints = new ArrayList<>();
		points.forEach((ele) -> {
			Point point = new Point();
			point.x = ele.x;
			point.y = ele.y;
			newPoints.add(point);
		});
		return newPoints;
	}

	public List<Point> findAllSinglePoints(List<List<Point>> multipleParsingResult) {
		List<Point> expectedResult = new ArrayList<>();

		for (List<Point> points : multipleParsingResult) {
			if (points.size() == 1) {
				expectedResult.addAll(points);
			}
		}

		expectedResult.sort((o1, o2) -> o1.x - o2.x);
		expectedResult.sort((o1, o2) -> o1.y - o2.y);
		return expectedResult;
	}
}
