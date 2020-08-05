package deployment;

import java.util.ArrayList;
import java.util.List;

public class DeploymentForTurorials2 {

	public List<List<Point>> solution(List<Point> list) {
		int maxX = 2022;
		int maxY = 2022;

		int[][] baseMatrix = new int[maxX][maxY];
		int[][] possibleDeployment = new int[maxX][maxY];
		int[][] selectedDeployment = new int[maxX][maxY];

		initBaseMatrix(list, baseMatrix, possibleDeployment);

		List<List<Point>> deployResult = new ArrayList<>();

		recurseProcess(list, baseMatrix, possibleDeployment, selectedDeployment, deployResult);

		return deployResult;
	}

	private void recurseProcess(List<Point> list, int[][] baseMatrix, int[][] possibleDeployment,
			int[][] selectedDeployment, List<List<Point>> deployResult) {
		if (list.isEmpty()) {
			return;
		}

		List<Point> newPoints = new ArrayList<>(list);

		for (Point point : list) {
			updatePossibleDeployment(point, baseMatrix, possibleDeployment);
			if (selectedDeployment[point.x][point.y] == 1) {
				continue;
			}

			if (possibleDeployment[point.x][point.y] == 0) {
				List<Point> orphanList = new ArrayList<>();
				orphanList.add(point);
				deployResult.add(orphanList);
				selectedDeployment[point.x][point.y] = 1;
				baseMatrix[point.x][point.y] = 0;
				newPoints.removeIf((ele) -> ele.x == point.x && ele.y == point.y);
			} else if (possibleDeployment[point.x][point.y] == 1) {
				List<Point> tmPoints = getPossiblePoints(point, baseMatrix, selectedDeployment, possibleDeployment);
				if (!tmPoints.isEmpty()) {
					deployResult.add(tmPoints);
					tmPoints.forEach(
							(tmpoint) -> newPoints.removeIf((ele) -> ele.x == tmpoint.x && ele.y == tmpoint.y));
				}
			}
		}
		recurseProcess(newPoints, baseMatrix, possibleDeployment, selectedDeployment, deployResult);
	}

	private void initBaseMatrix(List<Point> list, int[][] baseMatrix, int[][] possibleDeployment) {
		int maxRow = 0;
		int maxColumn = 0;
		// init base matrix
		for (Point point : list) {
			baseMatrix[point.x][point.y] = 1;
			if (point.x > maxRow) {
				maxRow = point.x;
			}
			if (point.y > maxColumn) {
				maxColumn = point.y;
			}
		}

		for (int row = 0; row < maxRow + 2; row++) {
			for (int column = 0; column < maxColumn + 2; column++) {
				if (baseMatrix[row][column] == 0) {
					System.out.printf("%-10s", "~");
				} else {
					System.out.printf("%-10s", "(" + row + "," + column + ")");
				}
			}
			System.out.println();
		}

		initPossibleDeployment(list, baseMatrix, possibleDeployment);
	}

	private void initPossibleDeployment(List<Point> list, int[][] baseMatrix, int[][] possibleDeployment) {
		// init possible deployment matrix
		for (Point point : list) {
			updatePossibleDeployment(point, baseMatrix, possibleDeployment);
		}
	}

	private void updatePossibleDeployment(Point point, int[][] baseMatrix, int[][] possibleDeployment) {
		// init possible deployment matrix
		int x = point.x;
		int y = point.y;
		int top = baseMatrix[x - 1][y];
		int down = baseMatrix[x + 1][y];
		int left = baseMatrix[x][y - 1];
		int right = baseMatrix[x][y + 1];
		possibleDeployment[x][y] = top + down + left + right;
	}

	private List<Point> getPossiblePoints(Point point, int[][] baseMatrix, int[][] selectedDeployment,
			int[][] possibleDeployment) {
		int x = point.x;
		int y = point.y;

		int top_x = x - 1;
		int down_x = x + 1;
		int left_y = y - 1;
		int right_y = y + 1;

		List<Point> possiblePoints = new ArrayList<>();

		if (selectedDeployment[x][y] != 0) {
			return possiblePoints;
		}

		if (baseMatrix[top_x][y] > 0 && selectedDeployment[top_x][y] == 0) {
			Point possiblePoint = new Point();
			possiblePoint.x = top_x;
			possiblePoint.y = y;
			possiblePoints.add(possiblePoint);
		}

		if (baseMatrix[down_x][y] > 0 && selectedDeployment[down_x][y] == 0) {
			Point possiblePoint = new Point();
			possiblePoint.x = down_x;
			possiblePoint.y = y;
			possiblePoints.add(possiblePoint);
		}
		if (baseMatrix[x][left_y] > 0 && selectedDeployment[x][left_y] == 0) {
			Point possiblePoint = new Point();
			possiblePoint.x = x;
			possiblePoint.y = left_y;
			possiblePoints.add(possiblePoint);
		}
		if (baseMatrix[x][right_y] > 0 && selectedDeployment[x][right_y] == 0) {
			Point possiblePoint = new Point();
			possiblePoint.x = x;
			possiblePoint.y = right_y;
			possiblePoints.add(possiblePoint);
		}

		possiblePoints.sort((o1, o2) -> possibleDeployment[o1.x][o1.y] - possibleDeployment[o2.x][o2.y]);
		
		if (possiblePoints.isEmpty()) {
			possiblePoints.add(point);
			baseMatrix[point.x][point.y] = 0;
			selectedDeployment[point.x][point.y] = 1;
			return possiblePoints;
		}

		List<Point> selectedPoints = new ArrayList<>();
		selectedPoints.add(point);
		Point possiblePoint = possiblePoints.get(0);
		selectedPoints.add(possiblePoint);
		selectedDeployment[point.x][point.y] = 1;
		selectedDeployment[possiblePoint.x][possiblePoint.y] = 1;
		baseMatrix[point.x][point.y] = 0;
		baseMatrix[possiblePoint.x][possiblePoint.y] = 0;
		return selectedPoints;
	}

}
