package deployment;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/*
 * 1. find the minimum number from all the PointEnhance. ->
 * 2. 
 */

public class AlgorithmX_refactor_before {

	static class PointEnhance {
		int x;
		int y;
		List<Solution> solutions;
		int solutionSize;
		boolean hasUsed_1;
		int isExist;

		public PointEnhance(int x2, int y2) {
			this.x = x2;
			this.y = y2;
			isExist = 1;
			hasUsed_1 = true;
		}

		public void addSolutions(Solution solution) {
			if (solutions == null) {
				solutions = new ArrayList<>();
			}
			solutions.add(solution);
			assertTrue(solutions.size() >= 1 && solutions.size() <= 5);

			solutionSize = solutions.size();
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			PointEnhance other = (PointEnhance) obj;
			if (x != other.x)
				return false;
			if (y != other.y)
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "(" + x + ", " + y + ") solutionSize=" + solutionSize + ", hasUsed=" + hasUsed_1 + "\n";
		}

		@Override
		public int hashCode() {
			return super.hashCode();
		}

		public int getValidSolutionSize() {
			int sum = 0;
			for (Solution solution : solutions) {
				if (solution.isSelected) {
					sum++;
				}
			}

			// consider the orphan point.
			if (sum == 0) {
				return 1;
			}
			return sum;
		}
	}

	static class Solution {
		private List<PointEnhance> pointEnhances;

		private boolean isSelected;

		private int rowId;

		private static int allRowId;

		public Solution() {
			pointEnhances = new ArrayList<>();
			isSelected = true;
			allRowId++;
			rowId = allRowId;
		}

		public void addPointsEnhance(PointEnhance enhance) {
			assertNotNull(enhance);
			pointEnhances.add(enhance);
			assertTrue(pointEnhances.size() >= 1 && pointEnhances.size() <= 2);
		}

		public void addPointsEnhance(PointEnhance enhance1, PointEnhance enhance2) {
			pointEnhances.add(enhance1);
			pointEnhances.add(enhance2);
			assertTrue(pointEnhances.size() >= 1 && pointEnhances.size() <= 2);
		}

		public int getValidPointCount() {
			int validPointCount = 0;
			for (PointEnhance pointEnhance : pointEnhances) {
				if (pointEnhance.hasUsed_1) {
					validPointCount++;
				}
			}
			return validPointCount;
		}

		public List<PointEnhance> getValidPoints() {
			List<PointEnhance> validPointsEnhances = new ArrayList<>();

			for (PointEnhance pointEnhance : pointEnhances) {
				if (pointEnhance.hasUsed_1) {
					validPointsEnhances.add(pointEnhance);
				}
			}

			return validPointsEnhances;
		}

		@Override
		public String toString() {
			StringBuilder stringBuilder = new StringBuilder();
			pointEnhances.forEach((ele) -> stringBuilder.append("(" + ele.x + "," + ele.y + ")"));
			stringBuilder.append(", isSelected= " + isSelected + "\n");
			return stringBuilder.toString();
		}

		public int getAllValidPathCount() {
			int allValidPathCount = 0;

			for (PointEnhance pointEnhance : pointEnhances) {
				if (pointEnhance.hasUsed_1) {
					allValidPathCount += pointEnhance.getValidSolutionSize();
				}
			}

			return allValidPathCount;
		}

		public boolean isValidSolution(PointEnhance targetPoint) {
			if (!isSelected) {
				return false;
			}

			if (pointEnhances.size() == 1) {
				return true;
			}

//			for (PointEnhance pointEnhance : pointEnhances) {
//				if (pointEnhance.equals(targetPoint)) {
//					continue;
//				}
//
//				if (targetPoint.x < pointEnhance.x || targetPoint.y < pointEnhance.y) {
//					return true;
//				}
//			}

			return true;
		}
	}

	static class ResultCover {
		List<Solution> solutions;
		List<ResultCover> nextResultCovers;
		int allCoveredPointCounts;
		int allCoveredSolutionCounts;
		ResultCover parentCover;

		public static List<ResultCover> allPossibleSolutions = new ArrayList<>();

		@Override
		public String toString() {
			return "ResultCover [solutions=" + solutions + ", allCoveredPointCounts=" + allCoveredPointCounts
					+ ", allCoveredSolutionCounts=" + allCoveredSolutionCounts + "]";
		}

		public ResultCover() {
			super();
			this.solutions = new ArrayList<>();
			this.nextResultCovers = new ArrayList<>();
			allCoveredPointCounts = 0;
			allCoveredSolutionCounts = 0;
			parentCover = null;
		}

		public void addResult(Solution solution) {
			solutions.add(solution);
			allCoveredSolutionCounts++;
			allCoveredPointCounts += solution.getValidPointCount();
		}

		public void addResult(List<Solution> otherSolutions) {
			for (Solution solution : otherSolutions) {
				this.solutions.add(solution);
				allCoveredSolutionCounts++;
				allCoveredPointCounts += solution.pointEnhances.size();
			}
		}

		public void addResult(ResultCover resultCover) {
			allCoveredPointCounts += resultCover.allCoveredPointCounts;
			allCoveredSolutionCounts += resultCover.allCoveredSolutionCounts;
		}

		public void addChild(ResultCover child) {
			assertNotNull(child);
			nextResultCovers.add(child);
			child.parentCover = this;
		}

		@Override
		protected Object clone() {
			ResultCover newResultCover = new ResultCover();
			newResultCover.allCoveredPointCounts = this.allCoveredPointCounts;
			newResultCover.allCoveredSolutionCounts = this.allCoveredSolutionCounts;
			addChild(newResultCover);
			return newResultCover;
		}

		public List<Solution> findTheOptimalSolutions(ResultCover rootResultCover, int pointSize) {
			if (!allPossibleSolutions.isEmpty()) {
				allPossibleSolutions.clear();
			}

			List<ResultCover> possibleSolutions = findAllSolutions(rootResultCover, pointSize);
			possibleSolutions.sort((o1, o2) -> o1.allCoveredSolutionCounts - o2.allCoveredSolutionCounts);

			if (possibleSolutions.isEmpty()) {
				//system.out.println("possibleSolutions's size: " + possibleSolutions.size());
			}
			ResultCover optimalResultCover = possibleSolutions.get(0);

			List<Solution> optimalSolutions = new ArrayList<>();
			while (optimalResultCover != null) {
				optimalSolutions.addAll(optimalResultCover.solutions);
				optimalResultCover = optimalResultCover.parentCover;
			}

			return optimalSolutions;
		}

		private List<ResultCover> findAllSolutions(ResultCover resultCover, int pointSize) {
			if (resultCover == null) {
				return allPossibleSolutions;
			}

			if (resultCover.nextResultCovers.isEmpty()) {
				if (resultCover.allCoveredPointCounts == pointSize) {
					allPossibleSolutions.add(resultCover);
				}
				return allPossibleSolutions;
			}

			for (ResultCover result : resultCover.nextResultCovers) {
				findAllSolutions(result, pointSize);
			}

			return allPossibleSolutions;
		}
	}

	public static class Cell {
		int value;
		boolean marked;
		int nextX;
		int nextY;
		Point point;

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
		private int MAX_ROW = 2022;
		private int MAX_COlUMN = 2022;
		private int actualMaxRow;
		private int actualMaxColumn;
		private Cell[][] maze = new Cell[MAX_ROW][MAX_COlUMN];
		private int[][] next = new int[][] { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } }; // right, down, left, top
		private List<Point> allSinglePoints;
		private List<Point> allOriginalPoints;
		private List<List<Point>> originalSolutions;

		LinkedList<Integer> paths = new LinkedList<>();

		public DfsMazeSearch(List<Point> allOriginalPoints, List<Point> allSinglePoints,
				List<List<Point>> multipleParsingResult) {
			super();
			this.allOriginalPoints = allOriginalPoints;
			this.allSinglePoints = allSinglePoints;
			this.originalSolutions = multipleParsingResult;
		}

		public List<List<Point>> clusteringAlgorithm() {
//			return test_correct();
			return test_recfactor();
		}


		private List<List<Point>> test_recfactor() {
			if (allSinglePoints.isEmpty() || allSinglePoints.size() == 1 || originalSolutions.isEmpty()) {
				return originalSolutions;
			}
			
			initMaze();
			int allSinglePointsSize = allSinglePoints.size();
			List<Point> matchedPoints = new ArrayList<>();
			
			System.out.println(":=single point size's " + allSinglePointsSize);
			
			printMaze();
			
			for (int i = 0; i < allSinglePointsSize - 1; i++) {
				if (matchedPoints.contains(allSinglePoints.get(i))) {
					continue;
				}
				List<Point> otherPoints = new ArrayList<>();
				for (int j = i + 1; j < allSinglePointsSize; j++) {
					if (!matchedPoints.contains(allSinglePoints.get(j))) {
						otherPoints.add(allSinglePoints.get(j));
					}
				}
				Point startPoint = allSinglePoints.get(i);
				
				boolean isSucceed = dfs(startPoint.x, startPoint.y, otherPoints, matchedPoints);
				if (isSucceed) {
					matchedPoints.add(startPoint);
					//system.out.println("success-------print");
					printMaze();
					
				}
				
				clearMark();
			}
			
			return processDfsResult();
		}
		private void clearMark() {
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

		private List<List<Point>> processDfsResult() {
			Set<Point> matchedSet = new HashSet<>();
			List<List<Point>> allPointsSet = new ArrayList<>();

			printMaze();

			for (Point point : allOriginalPoints) {
				if (matchedSet.contains(point)) {
					continue;
				}

				int x = point.x;
				int y = point.y;
				List<Point> singlePointSet = new ArrayList<>();
				Cell currentCell = maze[x][y];
				int nextX = currentCell.nextX;
				int nextY = currentCell.nextY;
				if (nextX == 0 && nextY == 0) {
					singlePointSet.add(point);
					allPointsSet.add(singlePointSet);
					matchedSet.add(point);
				} else {
					Cell nextCell = maze[nextX][nextY];
					Point otherPoint = nextCell.point;
					assertNotNull(otherPoint);
					singlePointSet.add(point);
					singlePointSet.add(otherPoint);
					allPointsSet.add(singlePointSet);
					matchedSet.add(point);
					matchedSet.add(otherPoint);
				}
			}

			return allPointsSet;
		}

		private void printMaze() {
			//system.out.println("process before =================");
			for (int i = 0; i < actualMaxRow; i++) {
				for (int j = 0; j < actualMaxColumn; j++) {
					Cell currentCell = maze[i][j];
					if (currentCell == null) {
						continue;
					}
					//system.out.println(currentCell.toString());
				}
			}
			//system.out.println("process after =================");
		}

		private void initMaze() {
			actualMaxColumn = 0;
			actualMaxRow = 0;
			List<Point> checkList = new ArrayList<>();
			originalSolutions.forEach((ele) -> checkList.addAll(ele));
			//system.out.println("checkList.size() : " + checkList.size());
			//system.out.println("allOriginalPoints: " + allOriginalPoints.size());
			assertEquals(checkList.size(), allOriginalPoints.size());

			for (Point point : allOriginalPoints) {
				int x = point.x;
				int y = point.y;
				maze[x][y] = new Cell();
				maze[x][y].value = 1;
				maze[x][y].point = point;
				if (x > actualMaxRow) {
					actualMaxRow = x + 2;
				}
				if (y > actualMaxColumn) {
					actualMaxColumn = y + 2;
				}
			}

			for (List<Point> points : originalSolutions) {
				//system.out.println(points.toString());
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
				//system.out.println(cell1.toString());
				//system.out.println(cell2.toString());
			}
		}

		private boolean dfs(int startX, int startY, List<Point> otherPoints, List<Point> matchedPoints) {
			if (otherPoints.isEmpty()) {
				return true;
			}

			for (Point matchPoint : otherPoints) {
				int match_x = matchPoint.x;
				int match_y = matchPoint.y;
				int x_diff = startX - match_x;
				int y_diff = startY - match_y;
				if ((startX == match_x && Math.abs(y_diff) == 1) || (startY == match_y && Math.abs(x_diff) == 1)) {
					matchedPoints.add(matchPoint);
					paths.push(Integer.valueOf(startX));
					paths.push(Integer.valueOf(startY));
					paths.push(Integer.valueOf(matchPoint.x));
					paths.push(Integer.valueOf(matchPoint.y));
					Cell startCell = maze[startX][startY];
					Cell matchedCell = maze[match_x][match_y];
					startCell.nextX = match_x;
					startCell.nextY = match_y;
					matchedCell.nextX = startX;
					matchedCell.nextY = startY;

					//system.out.println("map.size: " + paths.size());
					for (int i = paths.size() - 1; i >= 0; i -= 2) {
						int nextX = paths.get(i).intValue();
						int nextY = paths.get(i - 1).intValue();
						//system.out.println("" + nextX + "," + nextY + "");
					}
					//system.out.println();

					paths.clear();
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
					//system.out.println("before -- startCell: " + startCell.toString());
					//system.out.println("before -- nextCell: " + nextCell.toString());
					//system.out.println();

					paths.push(Integer.valueOf(startX));
					paths.push(Integer.valueOf(startY));
					paths.push(Integer.valueOf(nextX));
					paths.push(Integer.valueOf(nextY));
					nextCell.marked = true;

					int oldStartX = startCell.nextX;
					int oldStartY = startCell.nextY;
					int next_startX = nextCell.nextX;
					int next_startY = nextCell.nextY;

					startCell.nextX = nextX;
					startCell.nextY = nextY;
					nextCell.nextX = startX;
					nextCell.nextY = startY;

					//system.out.println("after -- startCell: " + startCell.toString());
					//system.out.println("after -- nextCell: " + nextCell.toString());
					//system.out.println();

					maze[next_startX][next_startY].nextX = 0;
					maze[next_startX][next_startY].nextY = 0;

					boolean result = dfs(next_startX, next_startY, otherPoints, matchedPoints);
					if (result) {
						return true;
					}

					//system.out.println("restore before -- startCell: " + startCell.toString());
					//system.out.println("restore before -- nextCell: " + nextCell.toString());
					//system.out.println();

					maze[next_startX][next_startY].nextX = nextX;
					maze[next_startX][next_startY].nextY = nextY;
					nextCell.nextX = next_startX;
					nextCell.nextY = next_startY;

					startCell.nextX = oldStartX;
					startCell.nextY = oldStartY;

					//system.out.println("restore after -- startCell: " + startCell.toString());
					//system.out.println("restore after -- nextCell: " + nextCell.toString());
					//system.out.println();

					paths.pop();
					paths.pop();
					paths.pop();
					paths.pop();
				}
			}
			return false;
		}
	}

	private int MAX_ROW = 2022;
	private int MAX_COLUMN = 2022;

	private void initAllPointsAndSolutions(List<Point> list, List<PointEnhance> pointEnhances,
			List<Solution> allSolutions) {
		PointEnhance[][] pointEnhanceMatrix = new PointEnhance[MAX_ROW][MAX_COLUMN];

		// init base matrix
		for (Point point : list) {
			int x = point.x;
			int y = point.y;
			pointEnhanceMatrix[x][y] = new PointEnhance(x, y);
			pointEnhances.add(pointEnhanceMatrix[x][y]);
		}

		for (PointEnhance pointEnhance : pointEnhances) {
			int x = pointEnhance.x;
			int y = pointEnhance.y;

			PointEnhance top_point = pointEnhanceMatrix[x - 1][y];
			PointEnhance down_point = pointEnhanceMatrix[x + 1][y];
			PointEnhance left_point = pointEnhanceMatrix[x][y - 1];
			PointEnhance right_point = pointEnhanceMatrix[x][y + 1];

			int possibleSize = 0;
			if (top_point != null) {
				possibleSize += top_point.isExist;
			}
			if (down_point != null) {
				possibleSize += down_point.isExist;
			}
			if (left_point != null) {
				possibleSize += left_point.isExist;
			}
			if (right_point != null) {
				possibleSize += right_point.isExist;
			}

			if (possibleSize == 0) {
				Solution solution = new Solution();
				solution.addPointsEnhance(pointEnhance);
				pointEnhance.addSolutions(solution);
				allSolutions.add(solution);
				continue;
			}

			if (right_point != null && right_point.isExist > 0) {
				createSolution(allSolutions, pointEnhances, pointEnhance, right_point);
			} // right -> better case.
			if (down_point != null && down_point.isExist > 0) {
				createSolution(allSolutions, pointEnhances, pointEnhance, down_point);
			}
		}
	}

	private void createSolution(List<Solution> allSolutions, List<PointEnhance> pointEnhances,
			PointEnhance pointEnhance1, PointEnhance pointEnhance2) {
		Solution solution = new Solution();
		solution.addPointsEnhance(pointEnhance1);
		solution.addPointsEnhance(pointEnhance2);
		pointEnhance1.addSolutions(solution);
		pointEnhance2.addSolutions(solution);
		allSolutions.add(solution);
	}

	private int getTheoryMinSolutionCount(List<PointEnhance> pointEnhances) {
		int p0 = 0;
		int p1 = 0;
		int p2 = 0;
		int p3 = 0;
		int p4 = 0;

		for (PointEnhance pointEnhance : pointEnhances) {
			int solutionSize = pointEnhance.solutionSize;
			if (solutionSize == 1) {
				if (pointEnhance.solutions.get(0).pointEnhances.size() == 1) {
					p0++;
				} else {
					p1++;
				}
			} else if (solutionSize == 2) {
				p2++;
			} else if (solutionSize == 3) {
				p3++;
			} else if (solutionSize == 4) {
				p4++;
			}
		}
		int theoryMimSolutionSize = p0 + p1 + (p2 + p3 + p4 - p1) / 2 + 1;
		trace("p0: " + p0 + ", p1: " + p1 + ", p2: " + p2 + ", p3: " + p3 + ", p4: " + p4 + ", therory: "
				+ theoryMimSolutionSize);
		return theoryMimSolutionSize;
	}

	private List<Solution> getValidSolutionsForSingle(PointEnhance columnItem) {
		List<Solution> validSolution = new ArrayList<>();

		for (Solution solution : columnItem.solutions) {
			if (solution.isSelected) {
				validSolution.add(solution);
			}
		}

		if (validSolution.isEmpty()) {
			Solution solution = new Solution();
			solution.addPointsEnhance(columnItem);
			validSolution.add(solution);
		}

		return validSolution;
	}

	private List<Solution> getValidSolutionsForMultiple(PointEnhance columnItem) {
		List<Solution> validSolution = new ArrayList<>();

		for (Solution solution : columnItem.solutions) {
//			if (solution.isSelected) {
			if (solution.isValidSolution(columnItem)) {
				validSolution.add(solution);
			}
		}

		if (validSolution.isEmpty()) {
			Solution solution = new Solution();
			solution.addPointsEnhance(columnItem);
			validSolution.add(solution);
		}

		return validSolution;
	}

	private void cover(Solution rowItem, List<PointEnhance> allcolumnItems, List<Solution> allrowItems,
			Set<PointEnhance> deletedColumns, Set<Solution> deletedRows) {
		List<PointEnhance> columnItems = rowItem.pointEnhances;

		for (PointEnhance columnItem : columnItems) {
			if (columnItem.hasUsed_1) {
				columnItem.hasUsed_1 = false;
				deletedColumns.add(columnItem);
			}

			List<Solution> otherItems = columnItem.solutions;
			for (Solution otherRowItem : otherItems) {
				if (otherRowItem.isSelected) {
					otherRowItem.isSelected = false;
					deletedRows.add(otherRowItem);
				}
			}
		}
	}

	private void uncover(List<PointEnhance> allcolumnItems, List<Solution> allrowItems,
			Set<PointEnhance> deletedColumns, Set<Solution> deletedRows) {
		deletedColumns.forEach((ele) -> ele.hasUsed_1 = true);
		deletedRows.forEach((ele) -> ele.isSelected = true);
		allcolumnItems.addAll(0, deletedColumns);
		allrowItems.addAll(0, deletedRows);
		deletedColumns.clear();
		deletedRows.clear();
	}

	private List<List<Point>> transformDataFormat(ResultCover resultCover, int pointSize) {
		List<List<Point>> finalResult = new ArrayList<>();

		List<Solution> optimalSolution = resultCover.findTheOptimalSolutions(resultCover, pointSize);

		for (Solution solution : optimalSolution) {
			List<PointEnhance> pointEnhances = solution.pointEnhances;
			List<Point> points = new ArrayList<Point>();
			for (PointEnhance pointEnhance : pointEnhances) {
				Point point = new Point();
				point.x = pointEnhance.x;
				point.y = pointEnhance.y;
				points.add(point);
			}
			finalResult.add(points);
		}

		return finalResult;
	}

	private void processOrpanAndSingle(List<PointEnhance> allcolumnItems, List<Solution> allrowItems,
			Set<PointEnhance> deletedColumnItems, Set<Solution> deletedRowItems, ResultCover resultCover) {
		if (allcolumnItems.isEmpty()) {
			return;
		}

		boolean hasOrphanAndSinglePoint = false;

		for (PointEnhance point : allcolumnItems) {
			if (!point.hasUsed_1) {
				continue;
			}

			if (point.getValidSolutionSize() == 1) {
				hasOrphanAndSinglePoint = true;
				List<Solution> validSolutions = getValidSolutionsForSingle(point);
				Solution validSolution = validSolutions.get(0);
				resultCover.addResult(validSolution);
				cover(validSolution, allcolumnItems, allrowItems, deletedColumnItems, deletedRowItems);
			}
		}

		allcolumnItems.removeAll(deletedColumnItems);
		allrowItems.removeAll(deletedRowItems);
		if (!hasOrphanAndSinglePoint) {
			return;
		}

		processOrpanAndSingle(allcolumnItems, allrowItems, deletedColumnItems, deletedRowItems, resultCover);
	}

	private void processMultiplePoint(List<PointEnhance> allcolumnItems, List<Solution> allrowItems,
			Set<PointEnhance> deletedColumnItems, Set<Solution> deletedRowItems, ResultCover currentResultCover) {
		if (allcolumnItems.isEmpty()) {
			return;
		}
		
		long start = System.currentTimeMillis();
		System.out.println("sort start========" );
		allrowItems.sort((o1, o2) -> o1.getAllValidPathCount() - o2.getAllValidPathCount());
		long end = System.currentTimeMillis();
		System.out.println("sort end :" + (end - start));
		
		// preprocess
		
		// iterator two possibles: all down and all right
		for (int i=0; i<2; i++) {
			ResultCover childResult = (ResultCover) currentResultCover.clone();
			
			for (PointEnhance point : allcolumnItems) {
				if (!point.hasUsed_1) {
					continue;
				}
				
				int validSolutionSize = point.getValidSolutionSize();
				
				if (validSolutionSize == 1) {
					List<Solution> validSolutions = getValidSolutionsForSingle(point);
					Solution validSolution = validSolutions.get(0);
					childResult.addResult(validSolution);
					cover(validSolution, allcolumnItems, allrowItems, deletedColumnItems, deletedRowItems);
				} else if (validSolutionSize >= 2) {
					List<Solution> validSolutions = getValidSolutionsForMultiple(point);
					Solution validSolution = validSolutions.get(i);
					childResult.addResult(validSolution);
					cover(validSolution, allcolumnItems, allrowItems, deletedColumnItems, deletedRowItems);
				}
			}
			
			allcolumnItems.removeAll(deletedColumnItems);
			allrowItems.removeAll(deletedRowItems);
			
			processMultiplePoint(allcolumnItems, allrowItems, deletedColumnItems, deletedRowItems, currentResultCover);
			
			// recover the previous environment.
			uncover(allcolumnItems, allrowItems ,deletedColumnItems, deletedRowItems);
		}
	}
	
	private boolean performAlgorithmX(List<PointEnhance> allcolumnItems, List<Solution> allrowItems,
			Set<PointEnhance> deletedColumnItems, Set<Solution> deletedRowItems, ResultCover resultCover) {
		if (allcolumnItems.isEmpty()) {
			trace("process end...");
			return true;
		}

		processOrpanAndSingle(allcolumnItems, allrowItems, deletedColumnItems, deletedRowItems, resultCover);

		trace("process count " + (count++));
//			for (PointEnhance pointEnhance : allcolumnItems) {
//				if (pointEnhance.isSelected) {
//					System.out.println(pointEnhance.x + "," + pointEnhance.y);
//				}
//			}
		processMultiplePoint(allcolumnItems, allrowItems, deletedColumnItems, deletedRowItems, resultCover);

		return true;
	}

	private static int count = 0;

	public List<List<Point>> solution(List<Point> list) {
		count = 0;
		List<PointEnhance> pointEnhances = new ArrayList<PointEnhance>();

		List<Solution> allSolutions = new ArrayList<Solution>();

		initAllPointsAndSolutions(list, pointEnhances, allSolutions);

		Set<PointEnhance> deletedColumnItems = new LinkedHashSet<>();
		Set<Solution> deletedRowItems = new LinkedHashSet<>();

		// 1. process the orphan and single points.
		ResultCover orphanAndSingleResultCover = new ResultCover();
		processOrpanAndSingle(pointEnhances, allSolutions, deletedColumnItems, deletedRowItems,
				orphanAndSingleResultCover);
		int orphanAndSingleCoverPoints = list.size() - pointEnhances.size();
		List<List<Point>> orphanAndSingleParsingResult = transformDataFormat(orphanAndSingleResultCover,
				orphanAndSingleCoverPoints);

		// 2. process the multiple points.
		ResultCover multipleResultCover = new ResultCover();
		int multiplePointCount = pointEnhances.size();
		List<Point> allMultipleNodes = generateAllMultipleNodes(pointEnhances);
		
		deletedColumnItems.clear();
		deletedRowItems.clear();
		performAlgorithmX(pointEnhances, allSolutions, deletedColumnItems, deletedRowItems, multipleResultCover);
		List<List<Point>> multipleParsingResult = transformDataFormat(multipleResultCover, multiplePointCount);

		// 3. DfsMazeSearch
		List<Point> allSinglePoints = findAllSingleSet(multipleParsingResult);
		//system.out.println("========================: " + allSinglePoints);
		//system.out.println("====================size: " + allSinglePoints.size());

		DfsMazeSearch dfsMazeSearch = new DfsMazeSearch(allMultipleNodes, allSinglePoints, multipleParsingResult);
		List<List<Point>> finalResult = new ArrayList<>();
		List<List<Point>> searchResultList = dfsMazeSearch.clusteringAlgorithm();
		finalResult.addAll(orphanAndSingleParsingResult);
		finalResult.addAll(searchResultList);

		return finalResult;
	}

	private List<Point> generateAllMultipleNodes(List<PointEnhance> pointEnhances) {
		// TODO Auto-generated method stub
		if (pointEnhances.isEmpty()) {
			return Collections.emptyList();
		}

		List<Point> points = new ArrayList<>();
		pointEnhances.forEach((ele) -> {
			Point point = new Point();
			point.x = ele.x;
			point.y = ele.y;
			points.add(point);
		});
		return points;
	}

	private List<Point> findAllSingleSet(List<List<Point>> multipleParsingResult) {
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

	public void trace(String content) {
		if (true) {
			//system.out.println(content);
		}
	}

	public void assertUtil(boolean flag) {
		assertTrue(flag);
	}
}
