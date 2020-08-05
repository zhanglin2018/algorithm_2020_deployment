#include <stdio.h>
#define true 1
#define false 0
#define boolean _Bool
#define MAX_ROW 2022
#define MAX_COLUMN 2022

typedef struct Point{
int x;
int y;
} Point;

typedef struct Node {
	int x;
	int y;
	int fValue;
	int gValue;
	int hValue;
	boolean reachable;
	Node* parent;
	Node* nextNode;
	Point* point;
	int openIteratorIndex;
	int closeInteratorIndex;
	int matchedIndex;
}Node;


typedef struct Matrix
{
	int maxRow;
	int maxColumn;
	Node** node_map;
}Matrix;
	


	void initMap(Matrix* matrix, Point* points, int pointsSize, Point** solutions, int* rowAndColumn) {

		matrix->maxRow = rowAndColumn[0];
		matrix->maxColumn = rowAndColumn[1];

		
		matrix->node_map = (Node**)malloc(matrix->maxRow * sizeof(Node));

		for (int i = 0; i < matrix->maxRow; i++) {
			matrix->node_map[i] = (Node*)malloc(matrix->maxColumn * sizeof(Node));
		}



		for (int i = 0; i < matrix->maxRow; i++) {
			for (int j = 0; j < matrix->maxColumn; j++) {
				Node node;
				node.x = i;
				node.y = j;
				matrix->node_map[i][j] = node;
			}
		}

		for (int i = 0; i < pointsSize; i++) {
			int x = points[i].x;
			int y = points[i].y;
			matrix->node_map[x][y].reachable = true;
			matrix->node_map[x][y].point = &points[i];
		}

		for (List<Point> solution : solutions) {
			if (solution.size() == 1) {
				Point point = solution.get(0);
				node_map[point.x][point.y].nextNode = null;
			}
			else {
				Point point1 = solution.get(0);
				Point point2 = solution.get(1);
				Node node1 = node_map[point1.x][point1.y];
				Node node2 = node_map[point2.x][point2.y];

				node1.nextNode = node2;
				node2.nextNode = node1;
			}
		}

		System.out.println("consume time: " + (System.currentTimeMillis() - consumeTime));
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
					}
					else {
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



/*
@param places - the array of points e.g [(1,2), (2,2), (2,3)]
@param size   - size of input array
@param returnSize   - size of output 2 dimension array
@return Point** - 2 dimension array of points , fixed size of each row is 2.
Keep the second point = (0,0) if the base station only use first point.
e.g [[(1,2),(2,2)], [(2,3),(0,0)]
*/


Point** getMinStations(Point* places, int size, int* returnSize) {
	// Please fill this blank
	return NULL;
}


int main() {
	while (true)
	{

	printf("hello world");
	}

}