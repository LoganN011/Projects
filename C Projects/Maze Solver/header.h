/***************************************************************************
 * Logan Nunno
 *
 * This file has all of the required include statments
 * it also has all of the required method headers that are used to slove
 * a maze and free a 2d array 
 **************************************************************************/
#ifndef HEADERFILE_H
#define HEADERFILE_H

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

void sloveMaze(char**,int);

void printMaze(char**,int);

int findPath(char**,int,int, char**,int);

int isValidMove(char**,int,int,int);

void freeArray(char**,int);
#endif
