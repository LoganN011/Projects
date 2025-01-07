/***************************************************************************
 * Logan Nunno
 *
 * This file contains all of the methods that are required to slove a maze
 * To slove a maze all that must be done is to call the slove maze method
 * There os also a method to free a 2d array if required based on the 
 * allocation of the 2d array 
 *
 **************************************************************************/
#include "header.h"

/***************************************************************************
 * This method takes in 2 parameter's:
 * 1. a char pointer to a pointer called maze (acts like a 2d array)
 * 2. a int called size that is the size of the array 
 *
 * This method takes in no input from the user 
 * this method will output if the maze was slovable or not 
 *
 * This method will make an emprty array that is the same size as the maze
 * that is being passed in and it is filled with 0's by using calloc 
 * It will then call a diffrent method to find the path of the maze
 * if the maze is sloveable it will print the path of the maze
 * if not it will say it is not solveable 
 * it will then free the path array 
 **************************************************************************/
void sloveMaze(char** maze,int size){
  int i,j=0;

  char** path = (char**) calloc(size,sizeof(char*));
  for(i=0;i < size; i++)
  {
    path[i]= (char*) calloc(size,sizeof(char));
    for(j=0;j<size;j++)
    {
      path[i][j]='0';
    }
  }

  if(findPath(maze,0,0,path,size))
  {
    printf("PATH FOUND!");
    printMaze(path,size);
  }
  else
  {
    printf("no path found.\n");
  }
  freeArray(path,size);
  
  
}


/***************************************************************************
 * This method takes in 5 parameter's:
 * 1. a char pointer to a pointer called maze (acts like a 2d array)
 * 2. a int called row that is the starting row 
 * 3. a int called col that is the starting col 
 * 4. a char pointer to a pointer called path (acts like a 2d array)
 * 5. a int called size that is the size of the array
 *
 * This method takes in no input from the user
 * this method produces no output to the user 
 * 
 * this method will return either a 1 or 0 based on if the maze is solveable
 *
 * The way that this method works is while there is a path it keeps looping
 * if we can move right move right and save that we moved right in a array
 * if we cant move right move down if able 
 * if you can not move right or left use the direction that we last went and 
 * go the oppisite direction. So if we last went right go left
 * if we last went down go up
 * if we reach the last postion in the array return that the array a 1
 * meaning that we found a path
 * if we can not find a path the will go back to the start and return a o
 *
 **************************************************************************/
int findPath(char** maze,int row, int col, char** path, int size)
{
  int* directions = (int*) malloc(sizeof(int));
  int count=1;
  while(1)
  {
    if(isValidMove(maze,row,col+1,size))
    {
     
      path[row][col]='1';
      directions= (int*) realloc(directions,count*sizeof(int));
      directions[count-1]=1;
      count++;
      col++;
    }
    else if(isValidMove(maze,row+1,col,size))
    {
    
      path[row][col]='1';
      directions= (int*) realloc(directions,count*sizeof(int));
      directions[count-1]=2;
      count++;
      row++;
    }
    else if(row==size-1&&col==size-1)
    {
      path[row][col]='1';
      free(directions);
      return 1;
    }
    else if(count>1)
    {
      if(directions[count-2]==1)
      {
	path[row][col]='0';
	maze[row][col]='0';
	count--;
	directions= (int*) realloc(directions,count*sizeof(int));
	col--;
      }
      else if(directions[count-2]==2)
      {
	path[row][col]='0';
	maze[row][col]='0';
	count--;
	directions=(int*) realloc(directions,count*sizeof(int));
	row--;
      }
    }
    else
    {
      free(directions);
      return 0;
    }
	
  }
}

/***************************************************************************
 * This method takes 4 parameter's:
 * 1. a char pointer to a pointer called maze (acts like a 2d array)
 * 2. a int called row that is the current row
 * 3. a int called col that is the current col
 * 4. a int called size that is the size of the array
 *
 * This method takes in no input from the user 
 * this method produces no output to the user 
 *
 * This mehtod will return either a 1 or a 0 based on weather the row and
 * the col are valid and within the array
 *
 **************************************************************************/
int isValidMove(char** maze,int row,int col, int size)
{
  if(row>=0&&row<size&&col>=0&&col<size&&maze[row][col]=='1')
  {
    return 1;
  }
  return 0;
}

/***************************************************************************
 * This method takes in 2 parameter's:
 * 1. a char pointer to a pointer called maze (acts like a 2d array)
 * 2. a int called size that is the size of the array 
 *
 * This method takes in no input from the user 
 * This method will output the maze to the user 
 *
 * This method will loop thourgh the 2d with nested for loops
 * it will print all value inside of the 2d array with new lines after
 * each row is finished and add a at the end 
 **************************************************************************/
void printMaze(char** maze, int size)
{
  int i,j=0;
  for(i=0;i<size;i++)
  {
    printf("\n");
    for(j=0;j<size;j++)
    {
      printf("%c",maze[i][j]);
    }
  }
  printf("\n");
}

/***************************************************************************
 * This method takes in 2 parameter's:
 * 1. a char pointer to a pointer called maze (acts like a 2d array)
 * 2. a int called size that is the size of the array 
 *
 * This method does not take any input from the user 
 * This mehtod does mot produce any output for the user 
 *
 * In order to free the whole 2d array the method loops thoughr each row of 
 * the 2d array and frees the inner array. It then frees the whole maze in
 * in order to have no memory leaks
 **************************************************************************/
void freeArray(char** maze,int size)
{
  int i=0;
  
  for(i=0;i<size;i++)
  {
    free(maze[i]);
  }
  free(maze);
}
