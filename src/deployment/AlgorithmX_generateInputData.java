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

public class AlgorithmX_generateInputData {

	static class PointEnhance {
		int x;
		int y;
		List<Solution> solutions;
		int solutionSize;
		boolean isSelected;
		int isExist;
		int columnId;
		private static int allColumnId = 0;

		public PointEnhance(int x2, int y2) {
			this.x = x2;
			this.y = y2;
			isExist = 1;
			isSelected = true;
			allColumnId++;
			columnId = allColumnId;
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
			return "(" + x + ", " + y + ") solutionSize=" + solutionSize + ", isSelected=" + isSelected + ", columnId="
					+ columnId + "\n";
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
				if (pointEnhance.isSelected) {
					validPointCount++;
				}
			}
			return validPointCount;
		}

		public List<PointEnhance> getValidPoints() {
			List<PointEnhance> validPointsEnhances = new ArrayList<>();

			for (PointEnhance pointEnhance : pointEnhances) {
				if (pointEnhance.isSelected) {
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
				if (pointEnhance.isSelected) {
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

			for (PointEnhance pointEnhance : pointEnhances) {
				if (pointEnhance.equals(targetPoint)) {
					continue;
				}

				if (targetPoint.x < pointEnhance.x || targetPoint.y < pointEnhance.y) {
					return true;
				}
			}

			return false;
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

	private long TIME_OUT = 6000;

	private void initAllPointsAndSolutions(List<Point> list, List<PointEnhance> pointEnhances,
			List<Solution> allSolutions) {
		int maxX = 2022;
		int maxY = 2022;
		PointEnhance[][] pointEnhanceMatrix = new PointEnhance[maxX][maxY];

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
			if (columnItem.isSelected) {
				columnItem.isSelected = false;
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
		deletedColumns.forEach((ele) -> ele.isSelected = true);
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
			if (!point.isSelected) {
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
			ResultCover currentResultCover) {
		if (allcolumnItems.isEmpty()) {
			return;
		}
		
		Set<PointEnhance> deletedColumns = new LinkedHashSet<PointEnhance>();
		Set<Solution> deletedRows = new LinkedHashSet<Solution>();

		List<Solution> rowItems = selectProperSolutions(allcolumnItems, allrowItems);
		trace("select the row Items: \n" + rowItems.toString());

		for (Solution rowItem : rowItems) {
			if (System.currentTimeMillis() - timeOut > TIME_OUT) {
				return;
			}
			
			currentResultCover.addResult(rowItem);
			cover(rowItem, allcolumnItems, allrowItems, deletedColumns, deletedRows);
			allcolumnItems.removeAll(deletedColumns);
			allrowItems.removeAll(deletedRows);
			boolean finished = performAlgorithmX(allcolumnItems, allrowItems, deletedColumns, deletedRows,
					currentResultCover);
			if (finished) {
				return;
			}

//			ResultCover childResult = (ResultCover) currentResultCover.clone();
//			childResult.addResult(rowItem);
//			cover(rowItem, allcolumnItems, allrowItems,  deletedColumns, deletedRows);
//			allcolumnItems.removeAll(deletedColumns);
//			allrowItems.removeAll(deletedRows);
//			performAlgorithmX(allcolumnItems, allrowItems,deletedColumns, deletedRows, childResult);
//
//			// recover the previous environment.
//			
//			uncover(allcolumnItems, allrowItems ,deletedColumns, deletedRows);
		}
	}

	private List<Solution> findMaxPossibleSolution1(List<Solution> allrowItems) {
		allrowItems.sort((o1, o2) -> o2.getAllValidPathCount() - o1.getAllValidPathCount());

		Solution maxPossibleSolution = allrowItems.get(0);

		// 1. select the point from the solution.
		List<PointEnhance> pointEnhances = maxPossibleSolution.getValidPoints();

		pointEnhances.sort((o1, o2) -> o2.getValidSolutionSize() - o1.getValidSolutionSize());
		return pointEnhances.get(0).solutions;
	}

	private List<Solution> selectProperSolutions(List<PointEnhance> allcolumnItems, List<Solution> allrowItems) {
		if (false) {
			PointEnhance expectedPoint = null;

			for (PointEnhance pointEnhance : allcolumnItems) {
				if (pointEnhance.getValidSolutionSize() == 2) {
					expectedPoint = pointEnhance;
					break;
				}
			}

			List<Solution> expectedSolutions = getValidSolutionsForMultiple(expectedPoint);
			assertTrue(expectedSolutions.size() == 2);

			expectedSolutions.sort((o1, o2) -> o1.getAllValidPathCount() - o2.getAllValidPathCount());

			Solution maxPossibleSolution = expectedSolutions.get(0);

			// 1. select the point from the solution.
			List<PointEnhance> pointEnhances = maxPossibleSolution.getValidPoints();
			assertTrue(pointEnhances.size() == 2);

			PointEnhance selectedPointEnhance = expectedPoint;
			for (PointEnhance ponitEnhance : pointEnhances) {
				if (ponitEnhance != expectedPoint) {
					selectedPointEnhance = ponitEnhance;
				}
			}

			return selectedPointEnhance.solutions;
		}

		if (allcolumnItems.isEmpty()) {
			return Collections.EMPTY_LIST;
		}

		return getValidSolutionsForMultiple(allcolumnItems.get(0));
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
		return true;
	}

	private static int count = 0;

	public List<List<Point>> solution(List<Point> list, List<PointEnhance> pointEnhances) {
		count = 0;

		List<Solution> allSolutions = new ArrayList<Solution>();

		initAllPointsAndSolutions(list, pointEnhances, allSolutions);

		int size = getTheoryMinSolutionCount(pointEnhances);

		ResultCover resultCover = new ResultCover();

		Set<PointEnhance> deletedColumnItems = new LinkedHashSet<>();
		Set<Solution> deletedRowItems = new LinkedHashSet<>();
		timeOut = System.currentTimeMillis();
		performAlgorithmX(pointEnhances, allSolutions, deletedColumnItems, deletedRowItems, resultCover);

		return transformDataFormat(resultCover, list.size()-pointEnhances.size());
	}
	
	private static long timeOut;
	

	public void trace(String content) {
		if (true) {
			System.out.println(content);
		}
	}

	public void assertUtil(boolean flag) {
		assertTrue(flag);
	}
}
