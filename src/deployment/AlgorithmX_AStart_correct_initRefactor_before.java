package deployment;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

/*
 * 1. find the minimum number from all the PointEnhance. ->
 * 2. 
 */

public class AlgorithmX_AStart_correct_initRefactor_before {

	static class Point_Item {
		public int x;
		public int y;
		public List<Solution_Item> solutions;
		public boolean noUsed;
		public Point point;

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
			return "(" + x + ", " + y + ") solutionSize=" + solutions.size() + ", noUsed =" + noUsed + "\n";
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
			return "Result [solutions=" + solutions + ", pointCount=" + pointCount + ", solutionCount=" + solutionCount
					+ "]";
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

	class Node implements Comparator<Node> {
		int x;
		int y;
		int fValue;
		int gValue;
		int hValue;
		boolean reachable;
		Node parent;
		Node nextNode;
		Point point;
		int openIteratorIndex;
		int closeInteratorIndex;


		@Override
		public String toString() {
			return "Node [x=" + x + ", y=" + y + ", fValue=" + fValue + ", gValue=" + gValue + ", hValue=" + hValue
					+ ", reachable=" + reachable + ", openIteratorIndex=" + openIteratorIndex + ", closeInteratorIndex="
					+ closeInteratorIndex + "]";
		}

		public Node(int x, int y) {
			this.x = x;
			this.y = y;
			reachable = false;
			parent = null;
			nextNode = null;
			fValue = 0;
			gValue = 0;
			hValue = 0;
			point = null;
			openIteratorIndex = 0;
			closeInteratorIndex = 0;
		}

		String getXY() {
			return new String(x + "_" + y);
		}

		@Override
		public int compare(Node o1, Node o2) {
			return o1.fValue - o2.fValue;
		}
	}

	class Matrix {
		int maxRow;
		int maxColumn;
		Node[][] node_map;

		void initMap(List<Point> points, List<List<Point>> solutions) {

			for (int i = 0; i < points.size(); i++) {
				int x = points.get(i).x;
				int y = points.get(i).y;

				if (x > maxRow) {
					maxRow = x;
				}
				if (y > maxColumn) {
					maxColumn = y;
				}
			}

			maxRow += 2;
			maxColumn += 2;

			node_map = new Node[maxRow][maxColumn];

			for (int i = 0; i < maxRow; i++) {
				for (int j = 0; j < maxColumn; j++) {
					node_map[i][j] = new Node(i, j);
				}
			}

			for (int i = 0; i < points.size(); i++) {
				int x = points.get(i).x;
				int y = points.get(i).y;
				node_map[x][y].reachable = true;
				node_map[x][y].point = points.get(i);
			}

			for (List<Point> solution : solutions) {
				if (solution.size() == 1) {
					Point point = solution.get(0);
					node_map[point.x][point.y].nextNode = null;
				} else {
					Point point1 = solution.get(0);
					Point point2 = solution.get(1);
					Node node1 = node_map[point1.x][point1.y];
					Node node2 = node_map[point2.x][point2.y];

					node1.nextNode = node2;
					node2.nextNode = node1;
				}
			}
		}

		public List<List<Point>> getAllSolution() {
			List<List<Point>> results = new ArrayList<>();
			for (int i = 0; i < maxRow; i++) {
				for (int j = 0; j < maxColumn; j++) {
					List<Point> result = new ArrayList<>();
					if (node_map[i][j].reachable) {
						Node node1 = node_map[i][j];
						Node node2 = node1.nextNode;
						if (node2 == null) {
							node1.reachable = false;
							result.add(node1.point);
						} else {
							node1.reachable = false;
							node2.reachable = false;
							result.add(node1.point);
							result.add(node2.point);
						}
						results.add(result);
					}
				}
			}

			return results;
		}
	}

	static class AStar {
		private static int openIteratorIndex = 0;
		private static int closeIteratorIndex = 0;
		
		PriorityQueue<Node> openQueue = new PriorityQueue<Node>(new Comparator<Node>() {

			@Override
			public int compare(Node o1, Node o2) {
				return o1.fValue - o2.fValue;
			}
		});

		public int[][] next = new int[][] { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } }; // right, down, left, top

		public int getHValue(Node currentNode, Node targetNode) {
			return (Math.abs(currentNode.x - targetNode.x) + Math.abs(currentNode.y - targetNode.y));
		}

		public int getGValue(Node currentNode) {
			//assertNotNull(currentNode.parent);
			return currentNode.parent.gValue + 1;
		}

		public int getFValue(Node currentNode) {
			return currentNode.gValue + currentNode.hValue;
		}

		public void inOpen(Node currentNode, Node targetNode, Matrix matrix) {
			int x = currentNode.x;
			int y = currentNode.y;

			for (int possible = 0; possible < next.length; possible++) {
				int nextX = x + next[possible][0];
				int nextY = y + next[possible][1];
				Node nextNode = matrix.node_map[nextX][nextY];

				if (!nextNode.reachable || nextNode.closeInteratorIndex == closeIteratorIndex) {//xxxxxxxxxxxxxxxxxxxxxxxx
					continue;
				}

				if (nextNode.openIteratorIndex != openIteratorIndex) {
					nextNode.parent = currentNode;
					nextNode.gValue = getGValue(nextNode);
					nextNode.hValue = getHValue(nextNode, targetNode);
					nextNode.fValue = getFValue(nextNode);
					openQueue.add(nextNode);
					nextNode.openIteratorIndex = openIteratorIndex;
				}
			}
		}

		private Node getMinimumNode() {
			Node firstNode = openQueue.poll();
			firstNode.openIteratorIndex = openIteratorIndex - 1;
			firstNode.closeInteratorIndex = closeIteratorIndex;
			return firstNode;
		}

		public List<Node> search(Matrix matrix, Point startPoint, Point targetPoint, Set<Point> matchedPoints,
				List<Point> allPoints) {
			openIteratorIndex++;
			closeIteratorIndex++;
			Node startNode = matrix.node_map[startPoint.x][startPoint.y];
			Node targetNode = matrix.node_map[targetPoint.x][targetPoint.y];
			startNode.parent = null;
			targetNode.parent = null;
			Node possibleTargetNode = null;

			inOpen(startNode, targetNode, matrix);
			startNode.openIteratorIndex = openIteratorIndex-1;
			startNode.closeInteratorIndex = closeIteratorIndex;

			do {
				if (openQueue.isEmpty()) {
					break;
				}
				Node minumNode = getMinimumNode();
//				System.out.println(minumNode);
				Node nextNode = minumNode.nextNode;
//				System.out.println(nextNode);
				if (nextNode == null) { // find the other nodes. success
					possibleTargetNode = minumNode;
					break;
				}

				nextNode.parent = minumNode;
				nextNode.gValue = getGValue(nextNode);
				nextNode.hValue = getHValue(nextNode, targetNode);
				nextNode.fValue = getFValue(nextNode);

				nextNode.openIteratorIndex = openIteratorIndex-1;
				inOpen(nextNode, targetNode, matrix);
				nextNode.closeInteratorIndex = closeIteratorIndex;

				if (targetNode.openIteratorIndex == openIteratorIndex) {
					break;
				}
			} while (true);
			// 知道开启列表中包含终点时，循环退出

			openQueue.clear();
			
			assertTrue(openQueue.isEmpty());
			
			if (possibleTargetNode != null) { // another success exit
				Point possibleTargetPoint = possibleTargetNode.point;

				//assertNotNull(possibleTargetPoint);
				matchedPoints.add(startPoint);
				matchedPoints.add(possibleTargetPoint);

//				System.out.println("yes success......another........");
//				System.out.println(startNode + "-->" + possibleTargetNode);
				List<Node> searchPath = new ArrayList<>();
				Node lastNode = possibleTargetNode;
				while (lastNode != null) {
//					System.out.println(lastNode.x + ", " + lastNode.y);
					searchPath.add(lastNode);
					lastNode = lastNode.parent;
				}

				return searchPath;
			}

//			System.out.println(startNode);
//			System.out.println(targetNode);
			if (targetNode.parent == null) { // fail exit
//				System.out.println("has not fount the path.");
				return Collections.emptyList();
			}

			matchedPoints.add(startPoint);
			matchedPoints.add(targetPoint);

//			System.out.println("yes success..............");
//			System.out.println(startNode + "-->" + targetNode);
			List<Node> searchPath = new ArrayList<>();
			Node lastNode = targetNode;
			while (lastNode != null) {
//				System.out.println(lastNode.x + ", " + lastNode.y);
				searchPath.add(lastNode);
				lastNode = lastNode.parent;
			}

			return searchPath;
		}

		public List<List<Point>> clusteringAlgorithm(Matrix matrix, List<Point> multiplePoints,
				List<Point> singlePoints, List<List<Point>> multipleSolutions) {
			if (singlePoints.isEmpty()) {
				return multipleSolutions;
			}

			if (singlePoints.size() == 1) {
				return multipleSolutions;
			}
			int singlePointSize = singlePoints.size();
			Set<Point> matchedPoints = new HashSet<Point>();

			matrix.initMap(multiplePoints, multipleSolutions);

			System.out.println("==========================all single point's : " + singlePointSize);
			
			for (int i = 0; i < singlePointSize - 1; i++) {
				if (matchedPoints.contains(singlePoints.get(i))) {
					continue;
				}
				
				Point startPoint = singlePoints.get(i);
				Point targetPoint = null;
				for (int j=i+1; j<singlePointSize; j++) {
					if (matchedPoints.contains(singlePoints.get(j))) {
						continue;
					}
					targetPoint = singlePoints.get(j);
					break;
				}
				
				if (targetPoint == null) {
					continue;
				}

				List<Node> singleSearchPath = search(matrix, startPoint, targetPoint, matchedPoints, singlePoints);
				modifySolution(singleSearchPath);
			}

			List<List<Point>> result = matrix.getAllSolution();
			return result;
		}

		private void modifySolution(List<Node> singleSearchPath) {
			if (singleSearchPath.isEmpty()) {
				return;
			}

			assertTrue(singleSearchPath.size() % 2 == 0);

			for (int i = 0; i < singleSearchPath.size(); i += 2) {
				Node node1 = singleSearchPath.get(i);
				Node node2 = singleSearchPath.get(i + 1);
				node1.nextNode = node2;
				node2.nextNode = node1;
//				System.out.println(node1 + "-->" + node1.nextNode);
//				System.out.println(node2 + "-->" + node2.nextNode);
			}
		}
	}

	public void initAllPointsAndSolutions(List<Point> list, List<Point_Item> points, List<Solution_Item> allSolutions) {
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
			pointMatrix[x][y].point = point;
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

	public void createSolution(List<Solution_Item> solutions, List<Point_Item> points, Point_Item point1,
			Point_Item point2) {
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

	public void uncover(List<Point_Item> allcolumnItems, List<Solution_Item> allrowItems, Set<Point_Item> usedPoints,
			Set<Solution_Item> usedSolution) {
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
				resultPoints.add(point.point);
			}
			finalResult.add(resultPoints);
		}

		return finalResult;
	}

	public void processSinglePoints(List<Point_Item> points, List<Solution_Item> solutions, Set<Point_Item> usedPoints,
			Set<Solution_Item> usedSolution, Result result) {
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
		for (int i = 0; i < 2; i++) {
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
			uncover(points, solutions, usedPoints, usedSolutions);
		}
	}

	public List<List<Point>> solution(List<Point> list) {
		//1. init
		long init = System.currentTimeMillis();
		
		List<Point_Item> points = new ArrayList<Point_Item>();

		List<Solution_Item> solutions = new ArrayList<Solution_Item>();

		initAllPointsAndSolutions(list, points, solutions);

		Set<Point_Item> usedPoints = new LinkedHashSet<>();
		Set<Solution_Item> usedSolution = new LinkedHashSet<>();
		System.out.println("======================================init process: " + (System.currentTimeMillis() - init));

		// 1. process the orphan and single points.
		long singleProcessTime = System.currentTimeMillis();
		Result singleResult = new Result();
		processSinglePoints(points, solutions, usedPoints, usedSolution, singleResult);
		int singlePointSize = list.size() - points.size();
		List<List<Point>> singleSolutions = generateResult(singleResult, singlePointSize);
		System.out.println("======================================single process: " + (System.currentTimeMillis() - singleProcessTime));

		// 2. process the multiple points.
		long multipleTime = System.currentTimeMillis();
		
		Result multipleResult = new Result();
		int multiplePointCount = points.size();
		List<Point> multiplePoints = generateAllMultipleNodes(points);

		usedPoints.clear();
		usedSolution.clear();
		processMultiplePoints(points, solutions, usedPoints, usedSolution, multipleResult);
		List<List<Point>> multipleSolutions = generateResult(multipleResult, multiplePointCount);

		System.out.println("======================================multiple process: " + (System.currentTimeMillis() - multipleTime));
		
		// 4. Astar algorithm
		
		
		long searchTime = System.currentTimeMillis();

		List<Point> singlePoints = findAllSinglePoints(multipleSolutions);
		Matrix matrix = new Matrix();
		AStar astarAlgorithm = new AStar();
		List<List<Point>> finalResult = new ArrayList<>();
		List<List<Point>> matchedResult = astarAlgorithm.clusteringAlgorithm(matrix, multiplePoints, singlePoints,
				multipleSolutions);
		finalResult.addAll(singleSolutions);
		finalResult.addAll(matchedResult);

		System.out.println("======================================search process: " + (System.currentTimeMillis() - searchTime));
		return finalResult;
	}

	public List<Point> generateAllMultipleNodes(List<Point_Item> points) {
		if (points.isEmpty()) {
			return Collections.emptyList();
		}

		List<Point> newPoints = new ArrayList<>();
		points.forEach((ele) -> {
			newPoints.add(ele.point);
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

		expectedResult.sort((o1, o2) -> (o1.x + o1.y) - (o2.x + o2.y));
		return expectedResult;
	}
}
