package deployment;

import java.util.ArrayList;
import java.util.List;

public class Deployment {
	
	public List<List<Point>> solution(List<Point> list) {
		int maxX = 2022;
		int maxY = 2022;

		int[][] baseMatrix = new int[maxX][maxY];
		int[][] possibleDeployment = new int[maxX][maxY];
		int[][] selectedDeployment = new int[maxX][maxY];

		List<Point> sameOddAndEven = new ArrayList<Point>(); // same to odd and even.
		List<Point> notSameOddAndEven = new ArrayList<Point>(); // not same to odd and even.

		for (Point point : list) {
			baseMatrix[point.x][point.y] = 1;
			if ((point.x + point.y) % 2 == 0) {
				sameOddAndEven.add(point);
			} else {
				notSameOddAndEven.add(point);
			}
		}

		for (Point point : list) {
			int x = point.x;
			int y = point.y;
			int top = baseMatrix[x - 1][y];
			int down = baseMatrix[x + 1][y];
			int left = baseMatrix[x][y - 1];
			int right = baseMatrix[x][y + 1];
			possibleDeployment[x][y] = top + down + left + right;
		}

		List<Point> blackPoints = null;
		List<Point> whitePoints = null;

		if (sameOddAndEven.size() <= notSameOddAndEven.size()) {
			blackPoints = sameOddAndEven;
			whitePoints = notSameOddAndEven;
		} else {
			blackPoints = notSameOddAndEven;
			whitePoints = sameOddAndEven;
		}

		List<Point> singleList = new ArrayList<>();
		List<Point> doubleList = new ArrayList<>();
		List<Point> tripleList = new ArrayList<>();
		List<Point> quateList = new ArrayList<>();
		List<List<Point>> deployResult = new ArrayList<>();

		for (Point point : blackPoints) {
			if (possibleDeployment[point.x][point.y] == 0) {
				List<Point> orphanList = new ArrayList<>();
				orphanList.add(point);
				deployResult.add(orphanList);
			} else if (possibleDeployment[point.x][point.y] == 1) {
				singleList.add(point);
			} else if (possibleDeployment[point.x][point.y] == 2) {
				doubleList.add(point);
			} else if (possibleDeployment[point.x][point.y] == 3) {
				tripleList.add(point);
			} else if (possibleDeployment[point.x][point.y] == 4) {
				quateList.add(point);
			}
		}


		for (Point blackPoint : singleList) {
			List<Point> tmPoints = getPossiblePoints(blackPoint, baseMatrix, selectedDeployment, possibleDeployment);
			if (tmPoints.isEmpty()) {
				continue;
			}
			deployResult.add(tmPoints);
		}
		
		for (Point blackPoint : doubleList) {
			List<Point> tmPoints = getPossiblePoints(blackPoint, baseMatrix, selectedDeployment, possibleDeployment);
			if (tmPoints.isEmpty()) {
				continue;
			}
			deployResult.add(tmPoints);		}
		
		for (Point blackPoint : tripleList) {
			List<Point> tmPoints = getPossiblePoints(blackPoint, baseMatrix, selectedDeployment, possibleDeployment);
			if (tmPoints.isEmpty()) {
				continue;
			}
			deployResult.add(tmPoints);		}
		
		for (Point blackPoint : quateList) {
			List<Point> tmPoints = getPossiblePoints(blackPoint, baseMatrix, selectedDeployment, possibleDeployment);
			if (tmPoints.isEmpty()) {
				continue;
			}
			deployResult.add(tmPoints);		}

		// process the last white list
		for (Point whitePoint : whitePoints) {
			if (selectedDeployment[whitePoint.x][whitePoint.y] == 0) {
				List<Point> selectedPoints = new ArrayList<>();
				selectedPoints.add(whitePoint);
				deployResult.add(selectedPoints);
			}
		}

		return deployResult;
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
			selectedDeployment[point.x][point.y] = 1;
			return possiblePoints;
		}

		List<Point> selectedPoints = new ArrayList<>();
		selectedPoints.add(point);
		Point possiblePoint = possiblePoints.get(0);
		selectedPoints.add(possiblePoint);
		selectedDeployment[point.x][point.y] = 1;
		selectedDeployment[possiblePoint.x][possiblePoint.y] = 1;
		return selectedPoints;
	}
}
