/*
#include <vector>
#include <queue>
#include <math.h>
using namespace std;

struct Point
{
	int x;
	int y;
};

class Node {
public:
	int x;
	int y;
	int fValue;
	int gValue;
	int hValue;
	bool reachable;
	bool used;
	Node* parent;
	Node* nextNode;
	const Point* point;
	int openIteratorIndex;
	int closeInteratorIndex;
	int matchedIndex;

	Node(int x, int y) {
		this->x = x;
		this->y = y;
		reachable = false;
		used = false;
		parent = NULL;
		nextNode = NULL;
		fValue = 0;
		gValue = 0;
		hValue = 0;
		point = NULL;
		openIteratorIndex = 0;
		closeInteratorIndex = 0;
	}

	int operator()(const Node* lhs, const Node* rhs) const
	{
		return lhs->fValue - rhs->fValue;
	}
};

class Matrix {
public:
	int maxRow;
	int maxColumn;
	Node*** node_map;

	Matrix() {
		maxRow = 0;
		maxColumn = 0;
		node_map = NULL;
	}

	void getAllSolution(vector<vector<Point>>& results) {
		for (int i = 0; i < maxRow; i++) {
			for (int j = 0; j < maxColumn; j++) {
				if (node_map[i][j] == NULL) {
					continue;
				}
				vector<Point> result;
				if (node_map[i][j]->reachable) {
					Node* node1 = node_map[i][j];
					Node* node2 = node1->nextNode;
					if (node2 == NULL) {
						node1->reachable = false;
						result.push_back(*(node1->point));
					}
					else {
						node1->reachable = false;
						node2->reachable = false;
						result.push_back(*(node1->point));
						result.push_back(*(node2->point));
					}
					results.push_back(result);
				}
			}
		}
	}
};

class AStar {
public:
	static int openIteratorIndex;
	static int closeIteratorIndex;
	static int matchedIndex;

	std::priority_queue<Node*, vector<Node*>, less<Node*> > openQueue;

	int next[4][2]{ { 0, 1 },{ 1, 0 },{ 0, -1 },{ -1, 0 } }; // right, down, left, top

	void inOpen(Node* currentNode, Node* targetNode, const Matrix& matrix) {
		int x = currentNode->x;
		int y = currentNode->y;

		for (int possible = 0; possible < 4; possible++) {
			int nextX = x + next[possible][0];
			int nextY = y + next[possible][1];
			Node* nextNode = matrix.node_map[nextX][nextY];

			if (nextNode == NULL || !nextNode->reachable || nextNode->closeInteratorIndex == closeIteratorIndex) {
				continue;
			}

			if (nextNode->openIteratorIndex != openIteratorIndex) {
				nextNode->parent = currentNode;
				nextNode->gValue = currentNode->gValue + 1;
				nextNode->hValue = abs(nextX - targetNode->x) + abs(nextY - targetNode->y);
				nextNode->fValue = nextNode->gValue + nextNode->hValue;
				openQueue.push(nextNode);
				nextNode->openIteratorIndex = openIteratorIndex;
			}
		}
	}

	Node* getMinimumNode() {
		Node* firstNode = openQueue.top();
		openQueue.pop();
		firstNode->openIteratorIndex = openIteratorIndex - 1;
		firstNode->closeInteratorIndex = closeIteratorIndex;
		return firstNode;
	}

	void search(const Matrix& matrix, Point* startPoint, Point* targetPoint, vector<Node*>& searchPath) {
		openIteratorIndex++;
		closeIteratorIndex++;
		Node* startNode = matrix.node_map[startPoint->x][startPoint->y];
		Node* targetNode = matrix.node_map[targetPoint->x][targetPoint->y];
		startNode->parent = NULL;
		targetNode->parent = NULL;
		Node* possibleTargetNode = NULL;

		inOpen(startNode, targetNode, matrix);
		startNode->openIteratorIndex = openIteratorIndex - 1;
		startNode->closeInteratorIndex = closeIteratorIndex;

		do {
			if (openQueue.empty()) {
				break;
			}
			Node* minumNode = getMinimumNode();

			Node* nextNode = minumNode->nextNode;

			if (nextNode == NULL) { // find the other nodes. success
				possibleTargetNode = minumNode;
				break;
			}

			nextNode->parent = minumNode;
			nextNode->gValue = nextNode->parent->gValue + 1;
			nextNode->hValue = abs(nextNode->x - targetNode->x) + abs(nextNode->y - targetNode->y);
			nextNode->fValue = nextNode->gValue + nextNode->hValue;

			nextNode->openIteratorIndex = openIteratorIndex - 1;
			inOpen(nextNode, targetNode, matrix);
			nextNode->closeInteratorIndex = closeIteratorIndex;

			if (targetNode->openIteratorIndex == openIteratorIndex) {
				break;
			}
		} while (true);

		priority_queue<Node*> null_queue;
		openQueue.swap(null_queue);  // clear the openQueue.

		if (possibleTargetNode != NULL) { // another success exit
			startNode->matchedIndex = matchedIndex;
			possibleTargetNode->matchedIndex = matchedIndex;

			Node* lastNode = possibleTargetNode;
			while (lastNode != NULL) {
				searchPath.push_back(lastNode);
				lastNode = lastNode->parent;
			}
			return;
		}


		if (targetNode->parent == NULL) { // fail exit
			return;
		}

		startNode->matchedIndex = matchedIndex;
		targetNode->matchedIndex = matchedIndex;

		Node* lastNode = targetNode;
		while (lastNode != NULL) {
			searchPath.push_back(lastNode);
			lastNode = lastNode->parent;
		}
		return;
	}

	void searchOptimalResults(Matrix& matrix, const vector<Point>& multiplePoints,
		vector<Point>& singlePoints, const vector<vector<Point>>& multipleSolutions, vector<vector<Point>>& finalResults) {
		if (singlePoints.empty()) {
			return;
		}
		if (singlePoints.size() == 1) {
			return;
		}

		size_t singlePointSize = singlePoints.size();
		matchedIndex++;

		for (int i = 0; i < singlePointSize - 1; i++) {
			if (matrix.node_map[singlePoints[i].x][singlePoints[i].y]->matchedIndex == matchedIndex) {
				continue;
			}

			Point* startPoint = &singlePoints[i];
			Point* targetPoint = NULL;
			for (int j = i + 1; j < singlePointSize; j++) {
				if (matrix.node_map[singlePoints[j].x][singlePoints[j].y]->matchedIndex == matchedIndex) {
					continue;
				}
				targetPoint = &singlePoints[j];
				break;
			}

			if (targetPoint == NULL) {
				continue;
			}

			vector<Node*> singleSearchPath;
			search(matrix, startPoint, targetPoint, singleSearchPath);
			modifySolution(singleSearchPath);
		}

		matrix.getAllSolution(finalResults);
		return;
	}

	void modifySolution(const vector<Node*>& singleSearchPath) {
		if (singleSearchPath.empty()) {
			return;
		}

		size_t singleSearchPathSize = singleSearchPath.size();
		for (int i = 0; i < singleSearchPathSize; i += 2) {
			Node* node1 = singleSearchPath[i];
			Node* node2 = singleSearchPath[i + 1];
			node1->nextNode = node2;
			node2->nextNode = node1;
		}
	}
};

int AStar::openIteratorIndex = 0;
int AStar::closeIteratorIndex = 0;
int AStar::matchedIndex = 0;

class Solution {
public:
	vector<vector<Point>> getMinStations(vector<Point> places) {
		vector<Point> singlePoints;
		Matrix matrix;
		AStar astarAlgorithm;
		vector<vector<Point>> result;
		findApproximateResults(matrix, places, singlePoints, result);

		vector<vector<Point>> finalResult;
		astarAlgorithm.searchOptimalResults(matrix, places, singlePoints, result, finalResult);
		return finalResult;
	}

	void findApproximateResults(Matrix& matrix2, const vector<Point>& points, vector<Point>& singlePoints, vector<vector<Point>> &results) {
		if (points.empty()) {
			return;
		}

		int maxRow = 0;
		int maxColumn = 0;
		int pointSize = points.size();
		for (int i = 0; i<pointSize; i++) {
			if (points[i].x > maxRow) {
				maxRow = points[i].x;
			}
			if (points[i].y > maxColumn) {
				maxColumn = points[i].y;
			}
		}

		maxRow += 2;
		maxColumn += 2;

		matrix2.maxRow = maxRow;
		matrix2.maxColumn = maxColumn;
		matrix2.node_map = new Node**[maxColumn];
		for (int i = 0; i < maxRow; i++) {
			matrix2.node_map[i] = new Node*[maxColumn];
			for (int j = 0; j < maxColumn; j++) {
				matrix2.node_map[i][j] = NULL;
			}
		}

		for (int i = 0; i < pointSize; i++) {
			Point point = points[i];
			matrix2.node_map[point.x][point.y] = new Node(point.x, point.y);
			matrix2.node_map[point.x][point.y]->point = &points[i];
			matrix2.node_map[point.x][point.y]->parent = NULL;
			matrix2.node_map[point.x][point.y]->reachable = true;
			matrix2.node_map[point.x][point.y]->used = false;
		}

		for (int i = 0; i < maxRow; i++) {
			for (int j = 0; j < maxColumn; j++) {
				Node* node = matrix2.node_map[i][j];
				if (node == NULL || node->used) {
					continue;
				}

				vector<Point> result;
				result.push_back(*(node->point));

				if (matrix2.node_map[i][j + 1] != NULL && (!matrix2.node_map[i][j + 1]->used)) {
					result.push_back(*(matrix2.node_map[i][j + 1]->point));
					matrix2.node_map[i][j + 1]->used = true;
					matrix2.node_map[i][j + 1]->nextNode = matrix2.node_map[i][j];
					matrix2.node_map[i][j]->nextNode = matrix2.node_map[i][j + 1];
				}
				else if (matrix2.node_map[i + 1][j] != NULL && (!matrix2.node_map[i + 1][j]->used)) {
					result.push_back(*(matrix2.node_map[i + 1][j]->point));
					matrix2.node_map[i + 1][j]->used = true;
					matrix2.node_map[i + 1][j]->nextNode = matrix2.node_map[i][j];
					matrix2.node_map[i][j]->nextNode = matrix2.node_map[i + 1][j];
				}
				else {
					singlePoints.push_back(*(node->point));
					matrix2.node_map[i][j]->used = true;
					matrix2.node_map[i][j]->nextNode = NULL;
				}
				results.push_back(result);
			}
		}
		return;
	}
};

*/