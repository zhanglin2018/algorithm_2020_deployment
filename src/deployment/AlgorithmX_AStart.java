package deployment;

import static org.junit.Assert.*;

import java.time.Year;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.CountDownLatch;

/*
 * 1. find the minimum number from all the PointEnhance. ->
 * 2. 
 */

public class AlgorithmX_AStart {

	class Node implements Comparator<Node> {
		int x;
		int y;
		int fValue;
		int gValue;
		int hValue;
		boolean reachable;
		boolean used;
		Node parent;
		Node nextNode;
		Point point;
		int openIteratorIndex;
		int closeInteratorIndex;
		int matchedIndex;
		List<Node> adjacent;

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
			used = false;
			parent = null;
			nextNode = null;
			fValue = 0;
			gValue = 0;
			hValue = 0;
			point = null;
			openIteratorIndex = 0;
			closeInteratorIndex = 0;
			adjacent = new ArrayList<>();
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

		public List<List<Point>> getAllSolution(List<Point> list) {
			List<List<Point>> results = new ArrayList<>();
			for (Point point : list) {
				int i = point.x;
				int j = point.y;
				
				if (node_map[i][j] == null) {
					continue;
				}
				
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
			
			return results;
		}
	}

	static class AStar {
		private static int count = 0;
		private static int openIteratorIndex = 0;
		private static int closeIteratorIndex = 0;
		private static int matchedIndex = 0;

		PriorityQueue<Node> openQueue = new PriorityQueue<Node>(new Comparator<Node>() {

			@Override
			public int compare(Node o1, Node o2) {
				return o1.fValue - o2.fValue;
			}
		});

		public int[][] next = new int[][] { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } }; // right, down, left, top

		public void inOpen(Node currentNode, Node targetNode, Matrix matrix) {
			
			int x = currentNode.x;
			int y = currentNode.y;

			for (int possible = 0; possible < next.length; possible++) {
				int nextX = x + next[possible][0];
				int nextY = y + next[possible][1];
				Node nextNode = matrix.node_map[nextX][nextY];

				if (nextNode == null || !nextNode.reachable || nextNode.closeInteratorIndex == closeIteratorIndex) {// xxxxxxxxxxxxxxxxxxxxxxxx
					continue;
				}

				if (nextNode.openIteratorIndex != openIteratorIndex) {
					count++;
					nextNode.parent = currentNode;
					nextNode.gValue = nextNode.parent.gValue + 1;
					nextNode.hValue = Math.abs(nextNode.x - targetNode.x) + Math.abs(nextNode.y - targetNode.y);
					nextNode.fValue = nextNode.gValue + nextNode.hValue;
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

		public List<Node> search(Matrix matrix, Point startPoint, Point targetPoint) {
			openIteratorIndex++;
			closeIteratorIndex++;
			if (startPoint.x == targetPoint.x && Math.abs(startPoint.y-targetPoint.y) == 1
					|| startPoint.y == targetPoint.y && Math.abs(startPoint.x-targetPoint.x) == 1) {
				matrix.node_map[startPoint.x][startPoint.y].nextNode = matrix.node_map[targetPoint.x][targetPoint.y];
				matrix.node_map[targetPoint.x][targetPoint.y].nextNode = matrix.node_map[startPoint.x][startPoint.y];
				
				matrix.node_map[startPoint.x][startPoint.y].matchedIndex = matchedIndex;
				matrix.node_map[targetPoint.x][targetPoint.y].matchedIndex = matchedIndex;
				
				return Collections.emptyList();
			}
			
			Node startNode = matrix.node_map[startPoint.x][startPoint.y];
			Node targetNode = matrix.node_map[targetPoint.x][targetPoint.y];
			
			startNode.parent = null;
			targetNode.parent = null;
			inOpen(startNode, targetNode, matrix);
			Node possibleTargetNode = null;

			startNode.openIteratorIndex = openIteratorIndex - 1;
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
				nextNode.gValue = nextNode.parent.gValue + 1;
				nextNode.hValue = Math.abs(nextNode.x - targetNode.x) + Math.abs(nextNode.y - targetNode.y);
				nextNode.fValue = nextNode.gValue + nextNode.hValue;
				
				nextNode.openIteratorIndex = openIteratorIndex - 1;
				inOpen(nextNode, targetNode, matrix);
				nextNode.closeInteratorIndex = closeIteratorIndex;

				if (targetNode.openIteratorIndex == openIteratorIndex) {
					break;
				}
			} while (true);

			openQueue.clear();  // clear the openQueue.

//			assertTrue(openQueue.isEmpty());

			if (possibleTargetNode != null) { // another success exit
				// assertNotNull(possibleTargetPoint);
				startNode.matchedIndex = matchedIndex;
				possibleTargetNode.matchedIndex = matchedIndex;

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
			
			startNode.matchedIndex = matchedIndex;
			targetNode.matchedIndex = matchedIndex;

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

		public void searchOptimalResults(Matrix matrix, List<Point> points) {
			if (points.isEmpty()) {
				return;
			}

			int pointsSize = points.size();
			matchedIndex++;

			//////// System.out.println("==========================all single point's : " +
			//////// pointsSize);

			for (int i = 0; i < pointsSize - 1; i++) {
				Point point_i = points.get(i);
				if (matrix.node_map[point_i.x][point_i.y].matchedIndex == matchedIndex) {
					continue;
				}

				Point startPoint = points.get(i);
				Point targetPoint = startPoint;
				for (int j = i + 1; j < pointsSize; j++) {
					Point point_j = points.get(j);
					if (matrix.node_map[point_j.x][point_j.y].matchedIndex == matchedIndex) {
						continue;
					}
					targetPoint = point_j;
					break;
				}

				if (startPoint == targetPoint) {
					continue;
				}

				List<Node> singleSearchPath = search(matrix, startPoint, targetPoint);
				modifySolution(singleSearchPath);
			}

			return;
		}

		public void searchOptimalResults1(Matrix matrix, List<Point> singlePoints) {
			if (singlePoints.isEmpty()) {
				return;
			}

			int singlePointSize = singlePoints.size();
			matchedIndex++;

			////// System.out.println("==========================all single point's : " +
			////// singlePointSize);

			for (int i = 0; i < singlePointSize - 1; i++) {
				Point point_i = singlePoints.get(i);
				if (matrix.node_map[point_i.x][point_i.y].matchedIndex == matchedIndex) {
					continue;
				}

				Point startPoint = singlePoints.get(i);
				Point targetPoint = null;
				for (int j = i + 1; j < singlePointSize; j++) {
					Point point_j = singlePoints.get(j);
					if (matrix.node_map[point_j.x][point_j.y].matchedIndex == matchedIndex) {
						continue;
					}
					targetPoint = point_j;
					break;
				}


				List<Node> singleSearchPath = search(matrix, startPoint, targetPoint);
				modifySolution(singleSearchPath);
			}

			return;
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
			}
		}
	}

	public void findApproximateResults(Matrix matrix2, List<Point> points) {
		if (points.isEmpty()) {
			return;
		}

		int maxRow = 0;
		int maxColumn = 0;

		for (Point point : points) {
			if (point.x > maxRow) {
				maxRow = point.x;
			}
			if (point.y > maxColumn) {
				maxColumn = point.y;
			}
		}

		maxRow += 2;
		maxColumn += 2;

		matrix2.maxRow = maxRow;
		matrix2.maxColumn = maxColumn;
		matrix2.node_map = new Node[maxRow][maxColumn];

		for (Point point : points) {
			matrix2.node_map[point.x][point.y] = new Node(point.x, point.y);
			matrix2.node_map[point.x][point.y].point = point;
			matrix2.node_map[point.x][point.y].parent = null;
			matrix2.node_map[point.x][point.y].reachable = true;
			matrix2.node_map[point.x][point.y].nextNode = null;
		}
		
		for (Point point : points) {
			int x = point.x;
			int y = point.y;
		}

	}

	public List<List<Point>> findApproximateResults1(Matrix matrix2, List<Point> points, List<Point> singlePoints) {
		if (points.isEmpty()) {
			return Collections.emptyList();
		}

		int maxRow = 0;
		int maxColumn = 0;

		for (Point point : points) {
			if (point.x > maxRow) {
				maxRow = point.x;
			}
			if (point.y > maxColumn) {
				maxColumn = point.y;
			}
		}

		maxRow += 2;
		maxColumn += 2;

		matrix2.maxRow = maxRow;
		matrix2.maxColumn = maxColumn;
		matrix2.node_map = new Node[maxRow][maxColumn];

		for (Point point : points) {
			matrix2.node_map[point.x][point.y] = new Node(point.x, point.y);
			matrix2.node_map[point.x][point.y].point = point;
			matrix2.node_map[point.x][point.y].parent = null;
			matrix2.node_map[point.x][point.y].reachable = true;
			matrix2.node_map[point.x][point.y].used = false;
		}

		List<List<Point>> results = new ArrayList<>();

		for (int i = 0; i < maxRow; i++) {
			for (int j = 0; j < maxColumn; j++) {
				Node node = matrix2.node_map[i][j];
				if (node == null || node.used) {
					continue;
				}

				List<Point> result = new ArrayList<>();
				result.add(node.point);

				if (matrix2.node_map[i][j + 1] != null && (!matrix2.node_map[i][j + 1].used)) {
					result.add(matrix2.node_map[i][j + 1].point);
					matrix2.node_map[i][j + 1].used = true;
					matrix2.node_map[i][j + 1].nextNode = matrix2.node_map[i][j];
					matrix2.node_map[i][j].nextNode = matrix2.node_map[i][j + 1];
				} else if (matrix2.node_map[i + 1][j] != null && (!matrix2.node_map[i + 1][j].used)) {
					result.add(matrix2.node_map[i + 1][j].point);
					matrix2.node_map[i + 1][j].used = true;
					matrix2.node_map[i + 1][j].nextNode = matrix2.node_map[i][j];
					matrix2.node_map[i][j].nextNode = matrix2.node_map[i + 1][j];
				} else {
					singlePoints.add(node.point);
					matrix2.node_map[i][j].used = true;
					matrix2.node_map[i][j].nextNode = null;
				}
				results.add(result);
			}
		}

		return results;
	}

	public List<List<Point>> solution(List<Point> list) {
		Matrix matrix = new Matrix();
		AStar astarAlgorithm = new AStar();
		astarAlgorithm.count = 0;

		findApproximateResults(matrix, list);
		astarAlgorithm.searchOptimalResults(matrix, list);
		System.out.println("=====================================static int: " + astarAlgorithm.count);
		return matrix.getAllSolution(list);
	}
}