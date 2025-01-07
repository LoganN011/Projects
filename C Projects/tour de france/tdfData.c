/***************************************************************************
 * Logan Nunno
 *
 * This file converts data that is ented from a file based of tour de France
 * finishing postion and prints it in a better formated version with spacing 
 *
 * There are 4 ways that the file can return the data.
 * 1. formatted: this makes the data better spaced and prints all avilable
 * info from the input file 
 * 2. Teams: Prints all of the riders sorted by team and printed based on
 * the rider number of 
 * 3. Countries: Will sort the data by both the countries and the name of 
 * the rider. Each country will be grouped then the names sorted in 
 * alphabetical order so A-Z
 * 4. Extra: This Prints the average time of each team based on the average 
 * Time for the top 3 riders per team. Sorts the list of teams based on this
 * time and prints that sorted time list with the team name and average time
 *
 * These options are selected by using command line agruments and if none
 * are slected nothing will print 
 *************************************************************************/

#include <stdio.h>
#include <string.h>
#include <stdlib.h>


#define NUMRIDERS 150

typedef struct Rider
{
  int class;
  char name[30];
  int num;
  char country[30];
  char team[30];
  char time[30];
  double hours;
}rider;

void formatted(rider[]);
void teams(rider[]);
void country(rider[]);
void extra(rider[]);

void convertHours(rider[]);

int swap(rider *xp,rider *yp);

/***************************************************************************
 * Main method that will always return a zero
 * It takes in two paramters that are called argc and argv
 * argc is the number as an int of command line arguments 
 * argv is an array of string litterals of command line arguments
 *
 * This method takes both input and output
 * This method takes in command line arguments 
 * This method outputs printed data based on the data that is passed in 
 * from the user by calling other methods 
 *
 * The function will read in the a file of data and sort the data into 
 * an array that is the length of the of the number of riders. The data is
 * read by using fgets and sscanf. and saving the data in an array called 
 * listOfRiders. It will then sort and print the list based on what the 
 * command line arguments are by calling an other method
 *************************************************************************/
int main(int argc, char *argv[])
{
  rider listOfRiders[NUMRIDERS];
  char line[300];
  int i=0;

  while (fgets(line,300,stdin) != NULL)
  {
    sscanf(line,"%d %s %d %s %s %s",&listOfRiders[i].class,
	   listOfRiders[i].name,&listOfRiders[i].num,listOfRiders[i].country
	   ,listOfRiders[i].team,listOfRiders[i].time);
      i++;
  }

  if(!(strcmp(argv[1],"format")))
  {
    formatted(listOfRiders);
  }
  else if(!(strcmp(argv[1],"team")))
  {
    teams(listOfRiders);
  }
  else if(!strcmp(argv[1],"country"))
  {
    country(listOfRiders);
  }
  else if(!strcmp(argv[1],"extra"))
  {
    convertHours(listOfRiders);
    extra(listOfRiders);
  }
  return 0;
}
/***************************************************************************
 * This method takes in an array of the struck rider called listOfRiders
 * This method does not return anything only prints 
 *
 * This method outputs the data from listOfRiders and prints it with a 
 * header and all of the data with a field width of 20
 * 
 * The method wil; frist print header for the table of data with 6 columns
 * that represent: Classification, Rider(Last Name), Rider Number, Country,
 * team, Time(hh:mm:ss)
 * Under each column will be the data for each rider in the listOfRiders 
 * array spaced with a field width of 20
 *
 *************************************************************************/
void formatted(rider listOfRiders[])
{
  int i=0;
  printf("%-20s%-20s%-20s%-20s%-20s%-20s\n","Classification", "Rider",
	 "Rider No.","Country","Team","Time (hh:mm:ss)"); 
  for(i=0;i<NUMRIDERS;i++)
  {
    printf("%-20d%-20s%-20d%-20s%-20s%s\n",listOfRiders[i].class,
	   listOfRiders[i].name,listOfRiders[i].num,listOfRiders[i].country
	   ,listOfRiders[i].team,listOfRiders[i].time);
  }
}
/***************************************************************************
 * This function takes in two Parameters
 * 1. rider1 is a adress to a rider 
 * 2. rider2 is a adress to a rider 
 * this method will always return 1 
 * 
 * This method is a basic swap function. It will saved rider1 value to a temp
 * rider and then replace the value of rider1 the value of rider2. Then 
 * will replace the value of rider2 with the temp rider that is holding 
 * the old value of rider1 so swaping the data of the two riders
 *
 *************************************************************************/
int swap(rider *rider1,rider *rider2)
{
  rider temp= *rider1;
  *rider1 = *rider2;
  *rider2 =temp;
  return 1;
}
/***************************************************************************
 * This method takes in a unsorted list of Riders 
 * this method will not return anything 
 *
 * This method will sort the provided array of riders by using bubble sort
 * The algorithm ofr bubble sort is:
 *
 * Given a list of unsorted riders, each pass will consists of:
 * 1. Starting with the first rider in the list
 * 2. Walking trough the list and comparing each rider wit the rider that is
 * one position father down the list 
 * 3. In each comparison, if the first rider has a greater rider number than 
 * than the second, then swap the two riders with the swap function 
 * Continue making passes until a full pass is completed without making any
 * swaps 
 *
 *************************************************************************/
void sortByTeam(rider listOfRiders[])
{
  int i, s =1;
  while(s)
  {
    s=0;
    for(i=0;i<NUMRIDERS-1;i++)
    {
      if(listOfRiders[i].num>listOfRiders[i+1].num)
      {
	s=swap(&listOfRiders[i],&listOfRiders[i+1]);
      }
    }
  }
}
/***************************************************************************
 * This mathod takes in array of riders called listOfRiders
 * This method does not return anythg but does print 
 *
 *It will output the Data of every rider sorted by team.
 *
 * The method will first print a header that has the team, Rider No., Rider,
 * Time (hh:mm:ss)
 *
 * It will then print every rider in the now sorted list under each of
 * the headers and print the info of each rider with a spacing of 20
 *
 *************************************************************************/
void teams(rider listOfRiders[])
{
  int i;
  sortByTeam(listOfRiders);
  
  printf("%-20s%-20s%-20s%-20s\n","Team","Rider No.", "Rider",
	 "Time (hh:mm:ss)");
  for(i=0;i<NUMRIDERS;i++)
  {
    printf("%-20s%-20d%-20s%s\n",listOfRiders[i].team,listOfRiders[i].num,
	   listOfRiders[i].name,listOfRiders[i].time);
  }
}
/***************************************************************************
 * This method takes in a unsorted list of riders
 * This method does not return anything 
 *
 * This method will sort a array of riders by the country that they are from
 * by using bubble sort that algorthim is discibed as: 
 *
 * Given a list of unsorted riders, each pass will consists of:
 * 1. Starting with the first rider in the list
 * 2. Walking trough the list and comparing each rider wit the rider that is
 * one position father down the list 
 * 3. In each comparison, if the first rider has a greater country than 
 * than the second, by using strcmp then swap the two riders with the swap 
 * function 
 * Continue making passes until a full pass is completed without making any
 * swaps 
 *
 *
 *************************************************************************/
void sortCountry(rider listOfRiders[])
{
  int i, s =1;
  while(s)
  {
    s=0;
    for(i=0;i<NUMRIDERS-1;i++)
    {
      if(strcmp(listOfRiders[i].country,listOfRiders[i+1].country)>0)
      {
	s=swap(&listOfRiders[i],&listOfRiders[i+1]);
      }
    }
  }
}
/***************************************************************************
 * This method takes in an array of riders called listOfRiders 
 * This method does not return anything only prints 
 * The method will sort the list twice by using bubble sort by:
 * 
 * Sort by country using the sort by country method then sort that list by:
 * 1. Starting with the first rider in the list
 * 2. Walking trough the list and comparing each rider wit the rider that is
 * one position father down the list 
 * 3. In each comparison, if the first rider has a greater name than by using 
 * strcmp and the country is the same  
 * than the second, then swap the two riders with the swap function 
 * Continue making passes until a full pass is completed without making any
 * swaps
 *************************************************************************/
void country(rider listOfRiders[])
{
  int i, s =1;
  sortCountry(listOfRiders);
  while(s)
  {
    s=0;
    for(i=0;i<NUMRIDERS-1;i++)
    {
      if(strcmp(listOfRiders[i].country,listOfRiders[i+1].country)==0 &&
	 strcmp(listOfRiders[i].name,listOfRiders[i+1].name)>0)
      {
	s=swap(&listOfRiders[i],&listOfRiders[i+1]);
      }
    }
  }

  printf("%-20s%-20s%-20s%-20s\n","Country","Rider", "Classification",
	 "Time (hh:mm:ss)");
  for(i=0;i<NUMRIDERS;i++)
  {
    printf("%-20s%-20s%-20d%s\n",listOfRiders[i].country,listOfRiders[i].name,
	   listOfRiders[i].class,listOfRiders[i].time);
  }
}

typedef struct teamTime
{
  double time;
  char team[3];
} teamTime;

/***************************************************************************
 * This progam takes in the following:
 * 1. list of riders called team that has count number of riders and the rest
 * filled with blanks 
 * 2. int count is the number of riders in the team array 
 * 3. int idex is the current index of the in the teamList
 * 4. a array of called teamList of type teamTime that has a time as a double
 * and a string that is the team name 
 *
 * This method takes no input from the user and return nothing to the user
 *
 * This method creats the average time for each of the teams based on the 
 * best 3 times from each team and saves it in the array teamList for each
 * team in the whole ListOfRider team that is in other methods 
 *************************************************************************/
void avgTimeMaker(rider team[], int count, int index, teamTime teamList[])
{
  int i=0;
  double time=0;

  for(i=0;i<count;i++)
  {
    time+=team[i].hours;
  }
  time/=count;
  strcpy(teamList[index].team,team[0].team);
  teamList[index].time=time;
}
/***************************************************************************
 * This method takes in two things:
 * 1. a array called teamList that is of type teamTime that has only the
 * the average time and the team neam for each team in the TDF
 * 2. int index is the index of the next blank spot of the array 
 *
 * This method sorts the list of team time based on the the average time 
 * for each team in hours. It sorts this by:
 *
 * 1. Starting with the first team in the list
 * 2. Walking trough the list and comparing each teamTime with the team that is
 * one position father down the list 
 * 3. In each comparison, if the first team  has a greater time 
 * than the second, then swap the two teams 
 * Continue making passes until a full pass is completed without making any
 * swaps
 * 
 * Then we will print the header of the table with the team name and 
 * the time(hh:mm:ss) with an offest of 20 char
 * It will then loop through the sorted teamList and print each team and
 * convert its time from the double hours to a String that is in the format 
 * of the (hh:mm:ss) and each colum is an offest of 20 for the data as well
 *************************************************************************/
void printAvgTime(teamTime teamList[],int index)
{
  int i, s =1;
  while(s)
  {
    s=0;
    for(i=0;i<index-1;i++)
    {
      if(teamList[i].time>teamList[i+1].time)
      {
	 teamTime temp= teamList[i];
	 teamList[i] = teamList[i+1];
	 teamList[i+1] =temp;
	 s=1;
      }
    }
  }

  printf("%-20s%-20s\n","Team", "Time (hh:mm:ss)");

  for(i=0;i<index;i++)
  {
    float time=teamList[i].time;
    int hours=(int)time;
    int min=(int)((time-hours)* 60);
    int sec=(int)((((time-hours)*60)-min)*60);
    printf("%-20s%02d:%02d:%02d\n",teamList[i].team,hours,min,sec);
  }
  
}
    
/***************************************************************************
 * This method takes in one paramater of the an array of riders 
 * called listOfRider
 * This method will not return anything 
 * This method takes in no input from the user but calls other methods to 
 * print info to the user 
 *
 * The method will first:
 *
 * Sort by team first by using the sort by team method then sort that list by:
 * 1. Starting with the first rider in the list
 * 2. Walking trough the list and comparing each rider with the rider that is
 * one position father down the list 
 * 3. In each comparison, if the first rider has a greater classification 
 * and the teams are the same than the second, then swap the two riders 
 * with the swap function 
 * Continue making passes until a full pass is completed without making any
 * swaps
 *
 * With that sorted list it will loop through the list of Riders 
 * and get the first 3 for each each based on the sorted list and pass 
 * them to the avgTimeMaker method it create the whole teamList it will
 * then print the list by giving it to the printAvgTime method
 *************************************************************************/
void extra(rider listOfRiders[])
{
  int i,s =1;
  int index=0;
  teamTime teamList[NUMRIDERS];
  int count=0;
  rider team[3];
  char curTeam[3];

  sortByTeam(listOfRiders);
  while(s)
  {
    s=0;
    for(i=0;i<NUMRIDERS-1;i++)
    {
      if(strcmp(listOfRiders[i].team,listOfRiders[i+1].team)==0 &&
	 listOfRiders[i].class >listOfRiders[i+1].class)
      {
	s=swap(&listOfRiders[i],&listOfRiders[i+1]);
      }
    }
  }
  
  memcpy(curTeam,listOfRiders[0].team,strlen(listOfRiders[0].team)+1);

  for(i=0;i<NUMRIDERS;i++)
  {
    if(strcmp(curTeam,listOfRiders[i].team)== 0 && count<3)
    {
      team[count]=listOfRiders[i];
      count++;
    }
    else if(strcmp(curTeam,listOfRiders[i].team)!=0)
    {
      avgTimeMaker(team,count,index,teamList);
      index++;
      count=0;
      memcpy(curTeam,listOfRiders[i].team,strlen(listOfRiders[i].team)+1);
      team[count]=listOfRiders[i];
      count++;
    }
  }
  avgTimeMaker(team,count,index,teamList);
  printAvgTime(teamList,index);
}

/***************************************************************************
 * This Method takes in a array of riders called listOfRiders
 *
 * This method takes no input from the users and does not ouput anything 
 *
 * This functions converts all of the time of each rider in the list 
 * of riders from a string to a single double that is in hours
 * It will then save that info to the rider in the list in its variable
 * called hours to be used in other methods
 *************************************************************************/
void convertHours(rider listOfRiders[])
{
  int i=0;
  char time[30];
  double hours=0;
  char *ptr;
  for(i=0;i<NUMRIDERS;i++)
  {
   
    
    memcpy(time,listOfRiders[i].time,strlen(listOfRiders[i].time)+1);

    hours += strtol(time,&ptr,10);
    memcpy(time,ptr+1,strlen(ptr)+1);
    hours += ((double)(strtol(time,&ptr,10)))/60;
    memcpy(time,ptr+1,strlen(ptr)+1);
    hours += (((double)strtol(time,&ptr,10))/60)/60;
    listOfRiders[i].hours=hours;
    hours=0;
  }
}
