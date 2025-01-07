/***************************************************************************
 * Logan Nunno
 *
 * This file is used to slove a maze 
 * to use it you must pass in a file of the maze that is N by N spaces wide
 * 1 means that there is a valid space in the maze 
 * 0 means that there is not a valid space 
 *
 * This file will try and slove the maze if there is a vaild path
 * if there is a vaild path the maze will show the path it takes to slove 
 * the maze 
 * if there is not a valid path the file will tell you this
 **************************************************************************/
#include "header.h"

/***************************************************************************
 * This function takes in no parameter's
 *
 * This method takes in a file of 1's and 0's that is N by N wide 
 * this method will output the path of the sloved maze if there is a 
 * valid path
 *
 * This method will read in the maze and store it within a 2d by using 
 * dynamic memory allocation 
 * Once the maze is in a 2d array it will call the slove maze method 
 * that method will slove the maze and print the result 
 * Then the 2d array will be freed in order to not cause memory leaks
 **************************************************************************/
int main(void)
{
  char **maze2d;
  char c = getchar();
  int index=0;
  int line=0;
  int size=0;

  maze2d = (char**) malloc(sizeof(char*));
  maze2d[line]= (char*) malloc(sizeof(char));
			   
  while(c!=EOF)
  {
    if(c=='\n'&&(size==0||(size*size>(line+1)*(index+1))))
    {
      size=index;
      line++;
      maze2d = (char**) realloc(maze2d,(line+1)*sizeof(char*));
      maze2d[line]= (char*) malloc(sizeof(char));
      index=0;
    }
    else
    {
      maze2d[line][index]=c;
      index++;
      maze2d[line] = realloc(maze2d[line],(index+1)*sizeof(char));
    }
    c=getchar();
  }


  sloveMaze(maze2d,size);
  freeArray(maze2d,size);
  return 0;
}
