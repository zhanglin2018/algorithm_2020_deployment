//============================================================================
// Name        : AlgorithmX.cpp
// Author      : linzhang
// Version     :
// Copyright   : Your copyright notice
// Description : Hello World in C++, Ansi-style
//============================================================================

#include <fstream>
#include <stdlib.h> /* atoi */
#include <dirent.h>  /* */
#include <sstream>
#include <sys/time.h>

//
#include <iostream>
#include <vector>
#include <set>
#include <cmath>
#include <algorithm>
#include <map>
using namespace std;

struct Point{
  int x;
  int y;
  Point(){
    x=0;
    y=0;
  }
};

struct mycomp {
  bool operator()(const Point &p1, const Point &p2) {
    if (p1.x  ==  p2.x)
        return p1.y < p2.y;
    else
        return p1.x < p2.x;
  }
};

bool CompareLess(Point lhs, Point rhs)
{
  if (lhs.x == rhs.x)
    return lhs.y < rhs.y;
  else
    return lhs.x < rhs.x;
}

class Solution_Item;

class Point_Item {
public:

  int x;
  int y;
  vector<Solution_Item> solutions;
  bool noUsed;
  bool exist;

  Point_Item()
  {
    x = 0;
    y = 0;
    noUsed = true;
    exist = true;
  }

  Point_Item(int x2, int y2)
  {
    x = x2;
    y = y2;
    noUsed = true;
    exist = true;
  }

  int solutionSize();
};

class Cell {
public:
  bool exist;
  int value;
  bool marked;
  int nextX;
  int nextY;
  Point point;

  Cell() {
    value = 0;
    marked = false;
    nextX = 0;
    nextY = 0;
    exist = false;
  }

};

class Solution_Item {
public:
  vector<Point_Item> points;
  bool noUsed;

  Solution_Item()
  {
     noUsed = true;
  }

  vector<Point_Item> noUsedPoints();
  int pointCount();
};


  int Solution_Item::pointCount()
  {
    int pointCount = 0;

    for (size_t i=0; i< points.size(); i++){
      if (points[i].noUsed) {
        pointCount++;
      }
    }
    return pointCount;
  }

  vector<Point_Item> Solution_Item::noUsedPoints()
  {
    vector <Point_Item > noUsedPoints;

    for (size_t i=0; i< points.size() ; i++){

      if (points[i].noUsed) {
        noUsedPoints.push_back(points[i]);
      }
    }

    return noUsedPoints;
  }

int Point_Item::solutionSize()
{
  int sum = 0;
  for (size_t i=0; i<solutions.size(); i++){
    if(solutions[i].noUsed) {
      sum++;
    }
  }

  // consider the orphan point.
  if (sum == 0) {
    return 1;
  }
  return sum;
}

class Result {
public:
  vector<Solution_Item> solutions;
  Result* parent;
  vector<Result*> children;
  int pointCount;
  int solutionCount;
  static vector<Result*> results;

  Result() {
    pointCount = 0;
    solutionCount = 0;
    parent = NULL;
  }

  void addResult(const Solution_Item & solution) {
    solutions.push_back(solution);
    solutionCount++;
    pointCount += solution.points.size();
  }

  void addResult(const vector<Solution_Item> & otherSolutions) {
    for (Solution_Item solution : otherSolutions) {
      solutions.push_back(solution);
      solutionCount++;
      pointCount += solution.points.size();
    }
  }

  void addResult(const Result & result) {
    pointCount += result.pointCount;
    solutionCount += result.solutionCount;
  }

  Result* cloneResult() {
    Result* result = new Result;
    result->pointCount = pointCount;
    result->solutionCount = solutionCount;
    children.push_back(result);
    result->parent = this;
    return result;
  }

  static bool ResultCompare(const Result *lhs, const Result *rhs)
  {
      return lhs->solutionCount < rhs->solutionCount;
  }


  vector<Solution_Item> printSolution(Result* result, int pointSize) {
    if (!results.empty()) {
      results.clear();
    }

    vector<Result*> allResults = findAllResult(result, pointSize);
    sort(allResults.begin(), allResults.end(), ResultCompare);

    Result* optimalResult = allResults[0];

    vector<Solution_Item> optimalSolutions;
    while (optimalResult != NULL) {
      for (size_t i=0; i < optimalResult->solutions.size(); i++){

      optimalSolutions.push_back(optimalResult->solutions[i]);
      optimalResult = optimalResult->parent;
      }
    }

    return optimalSolutions;
  }

  vector<Result*> findAllResult(Result* rootResult, int pointSize) {
    if (rootResult == NULL) {
      return results;
    }

    if (rootResult->children.empty()) {
      if (rootResult->pointCount == pointSize) {
        results.push_back(rootResult);
      }
      return results;
    }

    for (Result* result : rootResult->children) {
      findAllResult(result, pointSize);
    }

    return results;
  }
};

vector<Result*> Result::results = {NULL};


class Solution {
public:
  Solution(){}
  ~ Solution(){}
  vector<vector<Point>> getMinStations(vector<Point> places);
  void initAllPointsAndSolutions(const vector<Point> &vector, std::vector<Point_Item>& points, std::vector<Solution_Item> &allSolutions);
  void createSolution(vector<Solution_Item> & solutions,
      vector<Point_Item> &points, Point_Item& point1, Point_Item &point2);
  void cover(Solution_Item &solution, vector<Point_Item> &usedPoints,
      vector<Solution_Item> &usedSolution);
  void uncover(vector<Point_Item> &allcolumnItems,
        vector<Solution_Item> &allrowItems, vector<Point_Item> &usedPoints,
        vector<Solution_Item>& usedSolution);
  vector<vector<Point>> generateResult(Result* result, int pointSize);
  void pocessSinglePoints(vector<Point_Item> &points,
        vector<Solution_Item> &solutions,  vector<Point_Item>& usedPoints,
        vector<Solution_Item> &usedSolution, Result* result);
  void processMultiplePoints(vector<Point_Item> &points,
      vector<Solution_Item> &solutions, vector<Point_Item> &usedPoints,
      vector<Solution_Item> &usedSolutions, Result* result);
  vector<vector<Point>> solution_1(vector<Point> &list);
  vector<Point> generateAllMultipleNodes(vector<Point_Item>& points);
  vector<Point> findAllSinglePoints(vector<vector<Point>>& multipleParsingResult);
  vector<Solution_Item> validSolution(const Point_Item& point);

};

class DfsMazeSearch {
public:
  int actualMaxRow;
  int actualMaxColumn;
  Cell maze[2022][2022];
  int next[4][2] = { {0, 1}, {1, 0}, {0, -1}, {-1, 0}}; // right, down, left, top
  vector<Point> multiplePoints;
  vector<Point> singlePoints;
  vector<vector<Point>> solutions;

  DfsMazeSearch(const vector<Point> & multiplePoints, const vector<Point> & singlePoints,
      const vector<vector<Point>> & solutions)
  {
    this->multiplePoints = multiplePoints;
    this->singlePoints = singlePoints;
    this->solutions = solutions;
    actualMaxColumn = 0;
    actualMaxRow = 0;
  }

  void initMazeMatrix()
  {
    actualMaxColumn = 0;
    actualMaxRow = 0;
    vector<Point> checkvector;
    for (size_t i=0; i<solutions.size(); i++){
      for (size_t j=0; j<solutions[i].size(); j++){
        checkvector.push_back(solutions[i][j]);
      }
    }

    for (Point point : multiplePoints) {
      int x = point.x;
      int y = point.y;
      Cell cell;
      maze[x][y] = cell;
      maze[x][y].value = 1;
      maze[x][y].point = point;
      maze[x][y].exist = true;
      if (x > actualMaxRow) {
        actualMaxRow = x;
      }
      if (y > actualMaxColumn) {
        actualMaxColumn = y;
      }
    }

    actualMaxRow += 2;
    actualMaxColumn += 2;

    for (vector<Point> points : solutions) {
      if (points.size() == 1) {
        continue;
      }

      Point point1 = points[0];
      Point point2 = points[1];
      maze[point1.x][point1.y].nextX = point2.x;
      maze[point1.x][point1.y].nextY = point2.y;
      maze[point2.x][point2.y].nextX = point1.x;
      maze[point2.x][point2.y].nextY = point1.y;
    }
  }

  void clearMark()
  {
    for (int i = 0; i < actualMaxRow; i++) {
      for (int j = 0; j < actualMaxColumn; j++) {
        Cell& currentCell = maze[i][j];
        if (currentCell.exist) {
          continue;
        }
        currentCell.marked = false;
      }
    }
  }

  vector<vector<Point>> processDfsResult()
  {
    std::map<Point, int, mycomp> matchedPoint;
    vector<vector<Point>> result;

    for (size_t i=0; i< multiplePoints.size(); i++){

      Point point = multiplePoints[i];

      if (matchedPoint.find(point) != matchedPoint.end()) {
        continue;
      }

      int x = point.x;
      int y = point.y;
      vector < Point > points;
      Cell currentCell = maze[x][y];
      int nextX = currentCell.nextX;
      int nextY = currentCell.nextY;
      if (nextX == 0 && nextY == 0) {
        points.push_back(point);
        result.push_back(points);
        matchedPoint.insert(std::pair<Point,int>(point, 1));
      } else {
        Cell nextCell = maze[nextX][nextY];
        Point otherPoint = nextCell.point;
        points.push_back(point);
        points.push_back(otherPoint);
        result.push_back(points);
        matchedPoint.insert(std::pair<Point,int>(point, 1));
        matchedPoint.insert(std::pair<Point,int>(otherPoint, 1));
      }
    }

    return result;
  }

  bool dfs(int startX, int startY, const vector<Point> & matchedPoints,
     map<Point, int, mycomp> & results)
  {
    if (matchedPoints.empty()) {
      return true;
    }

    for (Point matchPoint : matchedPoints) {
      int match_x = matchPoint.x;
      int match_y = matchPoint.y;
      int x_diff = startX - match_x;
      int y_diff = startY - match_y;

      if ((startX == match_x && abs(y_diff) == 1)
          || (startY == match_y && abs(x_diff) == 1)) {
        results[matchPoint] = 1;
        maze[startX][startY].nextX = match_x;
        maze[startX][startY].nextY = match_y;
        maze[match_x][match_y].nextX = startX;
        maze[match_x][match_y].nextY = startY;
        return true;
      }
    }

    for (int possible = 0; possible < 4; possible++) {
      int nextX;
      int nextY;
      nextX = startX + next[possible][0];
      nextY = startY + next[possible][1];

      if (nextX < 0 || nextX >= 2020 || nextY < 0 || nextY >= 2020
          || maze[nextX][nextY].exist) {
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

        bool result = dfs(next_startX, next_startY, matchedPoints, results);
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

  vector<vector<Point>> clusteringAlgorithm()
  {
    if (singlePoints.empty() || singlePoints.size() == 1
        || solutions.empty()) {
      return solutions;
    }

    initMazeMatrix();
    int singlePointSize = singlePoints.size();
    map<Point, int,  mycomp> results;
    cout<< "all single point's : " <<  singlePointSize;

    for (int i = 0; i < singlePointSize - 1; i++) {
      if (results.find(singlePoints[i]) != results.end()){
        continue;
      }

      vector<Point> matchedPoints;
      for (int j = i + 1; j < singlePointSize;j++) {
            if (results.find(singlePoints[j]) != results.end()) {
              matchedPoints.push_back(singlePoints[j]);
            }
          }

          Point startPoint = singlePoints[i];

          bool isSucceed = dfs(startPoint.x, startPoint.y, matchedPoints, results);
          if (isSucceed) {
            results[startPoint] = 1;
          }

          clearMark();
        }

        return processDfsResult();
      }
  };


  std::vector<std::vector<Point>> Solution::getMinStations(std::vector<Point> places)
  {
    return solution_1(places);
  }

  void Solution::initAllPointsAndSolutions(const vector<Point> &vec, std::vector<Point_Item>& points, std::vector<Solution_Item> &allSolutions)
  {
    int max_row = 0;
    int max_column = 0;

    // init base matrix
    for (Point point : vec) {
      if (point.x > max_row) {
        max_row = point.x;
      }

      if (point.y > max_column) {
        max_column = point.y;
      }
    }
    max_row += 2;
    max_column += 2;

    Point_Item pointMatrix[max_row][max_column];

    for (Point point : vec) {
      int x = point.x;
      int y = point.y;
      Point_Item point_Item;
      pointMatrix[x][y] = point_Item;
      points.push_back(pointMatrix[x][y]);
    }

    for (Point_Item point : points) {
      int x = point.x;
      int y = point.y;

      Point_Item top_point = pointMatrix[x - 1][y];
      Point_Item down_point = pointMatrix[x + 1][y];
      Point_Item left_point = pointMatrix[x][y - 1];
      Point_Item right_point = pointMatrix[x][y + 1];

      int possibleSize = 0;
      if (top_point.exist) {
        possibleSize += 1;
      }
      if (down_point.exist) {
        possibleSize += 1;
      }
      if (left_point.exist) {
        possibleSize += 1;
      }
      if (right_point.exist) {
        possibleSize += 1;
      }

      if (possibleSize == 0) {
        Solution_Item solution;
        solution.points.push_back(point);
        point.solutions.push_back(solution);
        allSolutions.push_back(solution);
        continue;
      }

      if (right_point.exist) {
        createSolution(allSolutions, points, point, right_point);
      } // right -> better case.
      if (down_point.exist) {
        createSolution(allSolutions, points, point, down_point);
      }
    }
  }

  void Solution::createSolution(vector<Solution_Item> & solutions,
      vector<Point_Item> &points, Point_Item& point1, Point_Item &point2)
  {
    Solution_Item solution;
    solution.points.push_back(point1);
    solution.points.push_back(point2);
    point1.solutions.push_back(solution);
    point2.solutions.push_back(solution);
    solutions.push_back(solution);
  }

  vector<Solution_Item> Solution::validSolution(const Point_Item& point)
  {
    vector<Solution_Item> validSolution;

    for (Solution_Item solution : point.solutions) {
      if (solution.noUsed) {
        validSolution.push_back(solution);
      }
    }

    if (validSolution.empty()) {
      Solution_Item solution;
      solution.points.push_back(point);
      validSolution.push_back(solution);
    }

    return validSolution;
  }

  void Solution::cover(Solution_Item &solution, vector<Point_Item> &usedPoints,
      vector<Solution_Item> &usedSolution)
  {
    vector<Point_Item> points = solution.points;

    for (size_t i=0; i<points.size(); i++) {
      Point_Item point = points[i];
      if (point.noUsed) {
        point.noUsed = false;
        usedPoints.push_back(point);
      }

      vector<Solution_Item> solutions = point.solutions;
      for (size_t j=0; j<solutions.size(); j++){
        Solution_Item otherSolution = solutions[j];
        if (otherSolution.noUsed) {
          otherSolution.noUsed = false;
          usedSolution.push_back(otherSolution);
        }
      }
    }
  }

  void Solution::uncover(vector<Point_Item> &allcolumnItems,
        vector<Solution_Item> &allrowItems, vector<Point_Item> &usedPoints,
        vector<Solution_Item>& usedSolution)
  {
    vector<Point_Item>::iterator usedPointIterator = usedPoints.begin();

    for (; usedPointIterator != usedPoints.end(); usedPointIterator++){
        usedPointIterator->noUsed = true;
        allcolumnItems.push_back(*usedPointIterator);
    }

    vector<Solution_Item>::iterator usedSolutionIterator = usedSolution.begin();

    for (; usedSolutionIterator != usedSolution.end(); usedSolutionIterator++){
      usedSolutionIterator->noUsed = true;
      allrowItems.push_back(*usedSolutionIterator);
    }

    usedPoints.clear();
    usedSolution.clear();
  }

  vector<vector<Point>> Solution::generateResult(Result* result, int pointSize)
  {
    vector<vector<Point>> finalResult;

    vector<Solution_Item> optimalSolution = result->printSolution(result, pointSize);

    for (Solution_Item solution : optimalSolution) {
      vector<Point_Item> points = solution.points;
      vector<Point> resultPoints;

      for (Point_Item point : points) {
        Point newPoint;
        newPoint.x = point.x;
        newPoint.y = point.y;
        resultPoints.push_back(newPoint);
      }
      finalResult.push_back(resultPoints);
    }

    return finalResult;
  }

  void Solution::pocessSinglePoints(vector<Point_Item> &points,
        vector<Solution_Item> &solutions,  vector<Point_Item>& usedPoints,
        vector<Solution_Item> &usedSolution, Result* result)
  {
    if (points.empty()) {
      return;
    }

    bool containSinglePoint = false;

    for (Point_Item point : points) {
      if (!point.noUsed) {
        continue;
      }

      if (point.solutionSize() == 1) {
        containSinglePoint = true;
        vector<Solution_Item> validSolutions = validSolution(point);
        Solution_Item validSolution = validSolutions[0];
        result->addResult(validSolution);
        cover(validSolution, usedPoints, usedSolution);
      }
    }

    points.erase(usedPoints.begin(), usedPoints.end()); // xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
    solutions.erase(usedSolution.begin(), usedSolution.end()); // *********************************

    if (!containSinglePoint) {
      return;
    }

    pocessSinglePoints(points, solutions, usedPoints, usedSolution, result);
  }

  void Solution::processMultiplePoints(vector<Point_Item> &points,
      vector<Solution_Item> &solutions, vector<Point_Item> &usedPoints,
      vector<Solution_Item> &usedSolutions, Result* result)
  {
    if (points.empty()) {
      return;
    }

    // iterator two possibles: all down and all right
    for (int i = 0; i < 2; i++) {
      Result* childResult = result->cloneResult();

      for (Point_Item point : points) {
        if (!point.noUsed) {
          continue;
        }

        int solutionSize = point.solutionSize();

        if (solutionSize == 1) {
          vector<Solution_Item> validSolutions = validSolution(point);
          Solution_Item validSolution = validSolutions[0];
          childResult->addResult(validSolution);
          cover(validSolution, usedPoints, usedSolutions);
        } else if (solutionSize >= 2) {
          vector<Solution_Item> validSolutions = validSolution(point);
          Solution_Item validSolution = validSolutions[i];
          childResult->addResult(validSolution);
          cover(validSolution, usedPoints, usedSolutions);
        }
      }

      points.erase(usedPoints.begin(), usedPoints.end());
      solutions.erase(usedSolutions.begin(), usedSolutions.end());

      processMultiplePoints(points, solutions, usedPoints, usedSolutions, result);

      // recover the previous environment.
      uncover(points, solutions, usedPoints, usedSolutions);
    }
  }

  std::vector<std::vector<Point>> Solution::solution_1(std::vector<Point> &list){
    cout << "hello world" << endl;
    std::vector<Point_Item> points;

    std::vector<Solution_Item> solutions;

    initAllPointsAndSolutions(list, points, solutions);

    std::vector <Point_Item> usedPoints;
    std::vector <Solution_Item> usedSolution;

    // 1. process the orphan and single points.
    Result* singleResult = new Result;
    pocessSinglePoints(points, solutions, usedPoints, usedSolution,
        singleResult);
    int singlePointSize = list.size() - points.size();
    std::vector<std::vector<Point>> singleSolutions = generateResult(singleResult,
        singlePointSize);

    // 2. process the multiple points.
    Result* multipleResult = new Result;
    int multiplePointCount = points.size();
    std::vector < Point > multiplePoints = generateAllMultipleNodes(points);

    usedPoints.clear();
    usedSolution.clear();
    processMultiplePoints(points, solutions, usedPoints, usedSolution,
        multipleResult);
    std::vector < std::vector < Point >> multipleSolutions = generateResult(
        multipleResult, multiplePointCount);

    // 3. DfsMazeSearch
    std::vector < Point > singlePoints = findAllSinglePoints(multipleSolutions);
    DfsMazeSearch dfsMazeSearch(multiplePoints, singlePoints, multipleSolutions);
    std::vector<std::vector< Point>> finalResult;
    std::vector<std::vector< Point>> matchedResults = dfsMazeSearch.clusteringAlgorithm();


    for (size_t i=0; i<singleSolutions.size(); i++){
      finalResult.push_back(singleSolutions[i]);
    }

    for (size_t i=0; i<matchedResults.size(); i++){
      finalResult.push_back(matchedResults[i]);
    }

    return finalResult;
  }

  vector<Point> Solution::generateAllMultipleNodes(vector<Point_Item>& points)
  {
    if (points.empty()) {
      vector<Point> vector;
      return vector;
    }

    vector < Point > newPoints;

    for (size_t i=0; i<points.size(); i++){
      Point point;
      point.x = points[i].x;
      point.y = points[i].y;
      newPoints.push_back(point);
    }

    return newPoints;
  }

  vector<Point> Solution::findAllSinglePoints(vector<vector<Point>>& multipleParsingResult)
  {
    vector<Point> expectedResult;

    for (vector<Point> points : multipleParsingResult) {
      if (points.size() == 1) {
        for (size_t i=0; i<points.size(); i++){
         expectedResult.push_back(points[i]);
        }
      }
    }

    sort(expectedResult.begin(), expectedResult.end(), CompareLess);
    return expectedResult;
  }


  void preparePlaces(vector<Point> & places, int & size, std::string & filename)
  {
        places.clear();
        // create input file stream
        std::ifstream ifs;
        ifs.open(filename.c_str(), std::ios::in);

        if (!ifs.is_open())
        {

            return;
        }

        std::string buf;
        std::stringstream ss;
        while (std::getline(ifs, buf))
        {
            ss.str("");
             std::size_t found = buf.find(",");
             if (found == std::string::npos)
             {
                 if (!buf.empty())
                 {
                     size = atoi(buf.c_str());
                     ss << "size: " << size;
                 }
             }
             else {
                 int x = atoi(buf.substr( 0, found).c_str());
                 int y = atoi(buf.substr(found+1).c_str());
                 Point point;
                 point.x = x;
                 point.y = y;
                 places.push_back(point);
                 ss << "x: "<< places.back().x << " y: " <<  places.back().y;
             }

        }
        ss.str("");
        ss << "Vector size: " << places.size();

  }


  int fileNameFilter(const struct dirent * cur)
  {
     std::string str(cur->d_name);
     if (str.find(".in") != std::string::npos)
     {
         return 1;
     }
      return 0;
  }

  // read filename list from path
  void readFileList(std::vector<std::string> & fileNameList, std::string & path)
  {

      struct dirent **namelist;
      int n = scandir(path.c_str(),  &namelist, fileNameFilter, alphasort);
      if (n < 0)
      {

          return;
      }

      for (int i = 0; i < n; i++)
      {
          std::string filePath(namelist[i]->d_name);
          fileNameList.push_back(path+filePath);
          free(namelist[i]);
      }
      free(namelist);
  }

  long getCurrentTime()
  {
      struct timeval tv;
      gettimeofday(&tv, NULL);
      return tv.tv_sec * 1000 + tv.tv_usec / 1000;
  }

  int main(){
       std::vector<std::string> fileNameList;
      std::string path = "/home/linzhang/deployment/data/";
      readFileList(fileNameList, path);
      vector<Point> places;
      int size = 0;
      stringstream ss;

      std::vector<std::string>::iterator it = fileNameList.begin();
      for (; it != fileNameList.end(); it++)
      {
  //        long startTime = getCurrentTime();
          preparePlaces(places, size, *it);
          ss.str("");
          long startTime = getCurrentTime();

          for(auto uit = places.begin(); uit != places.end(); uit++)
          {
            cout << uit->x << "," << uit->y << endl;
          }

          Solution solution;
          vector<vector<Point>> result = solution.getMinStations(places);

          for(size_t i=0; i<result.size(); i++)
          {
            for(size_t j=0; j<result[i].size(); j++)
            {
              cout << result[i][j].x << "," << result[i][j].y <<";";
            }
            cout << endl;
          }

          long endTime = getCurrentTime();
          ss << "Execution Time:  " << (double(endTime - startTime) / 1000) << "s";

          break;
      }


  }