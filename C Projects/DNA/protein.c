/*************************************************************************
 * Logan Nunno
 *
 * This file is used to modify a spike protein with diffrent Amino acids
 * Based on the file that is passed in we will modify the protein with
 * deletions or modify the given amino acids wit the use of command line 
 * arguments
 * The needed commond line arguments are the name of the protein chain 
 * and any deletions or modifications that will be done to it.
 * To delete in the command use a lower case 'd' and the postions you want
 * to delete from
 * To modify an amino acid first Give the amion acid you wish to replace 
 * then the postion of that amion acid and finally the one you wish to have 
 * now placed within the chain of amino acids 
 *
 ************************************************************************/
#include <stdio.h>
#include <string.h>
#include <stdlib.h>

int indexCount=1;

typedef struct ListNode
{
  struct ListNode* next;
  char data;
  int index;
}ListNode;

/*************************************************************************
 * Method has one parameter:
 * char called data and that is an amion acid stored as a chartater 
 *
 * This method takes in no input and puts out nothing 
 * functiom will return a new ListNode pointer that is created within
 *
 * Function with make a new node with the use of malloc 
 * and assign the data to the node and give it an index value with the global
 * count of indexs. It will also assign the the next node to null
 * it will finally return the whole new pointer 
 ************************************************************************/
ListNode* createNode(char data)
{
  ListNode* node = malloc(sizeof(ListNode));
  node->data = data;
  node->index=indexCount++;
  node->next= NULL;
  return node;
}

/*************************************************************************
 * Function has two parameters:
 * char called data that is the new data that will be added to the end 
 * of the linked list
 *
 * ListNode pointer called head that is the head of the list 
 *
 * This function has no input from the user and gives no output
 *
 * This fuction will return nothing 
 *
 * This fuction loops thourgh the whole linked this until it reaches the end
 * of the list. Once it does it will added a new node to the end of the list
 * by using the creat new node method.
 ************************************************************************/
void append(char data, ListNode* head)
{
  ListNode* current = head;
  while(current->next != NULL)
  {
    current = current->next;
  }
  current->next= createNode(data);
}

/*************************************************************************
 * This functions has two parametes:
 * int called index that is the index in the list that will be deleted 
 * And a ListNode pointer called head that is the head of the linked list
 * 
 * This fucntion has no user input  and gives no output to the user 
 *
 * This function has no return value 
 *
 * This function will loop thorugh the list until it find ths provied index
 * once it find the the index it will set the tmp node to the one that 
 * is going to be deleted. then set the previous nodes next to the current
 * nodes next (Skipping the current node). Jump to the next spot in the list
 * then finally deleted the tmp node with the use of free (No memory leaking)
 ************************************************************************/
void deleteIndex(int index, ListNode* head)
{
  ListNode* tmp=head;
  ListNode* current=head;
  ListNode* pre=head;
  while(current!=NULL)
  {
    if(head->index==index)
    {
      head=head->next;
      free(pre);
    }
    else if(current->index==index)
    {
      tmp=current;
      pre->next=current->next;
      current=current->next;
      free(tmp);
    }
    else
    {
      pre=current;
      current=current->next;
    }
  }
  
}

/*************************************************************************
 * This function takes in three parameters:
 * 1. int called index that is the postion that will be modified 
 * 2. char called data that is the new data the will be placed into the list
 * 3. ListNode pointer called head that is the start of the linked list 
 *
 * This fuction takes no input from the user and gives not output
 *
 * This fuction will not return anything
 *
 * This function will loop thourgh the linked list and once it finds the 
 * index that is provided it will change that current data to the new data 
 * that was passed in from the user. it is changing the list with the use
 * of pass by refrance 
 ************************************************************************/
void modify(int index, char data, ListNode* head)
{
  ListNode* current = head;
  while(current !=NULL)
  {
    if(current->index== index)
    {
      current->data=data;
    }
    current=current->next;
  }

}

/*************************************************************************
 * This function has one parameter:
 * ListNode pointer called head that is the start of the linked list
 *
 * This function has no input from the user
 * This function will output a formatted version of the linked list 
 * to the user showing the content of the linked list 
 *
 * This fuction will print the linked list in a formatted version for the 
 * user. It will print 50 amion acids per line with 10 in one section.
 * the total index will be printed above each section. This is done by: 
 *
 * while looping through the whole linked list. first print the index that
 * justifed 10 spaces to the right. then if we are not at the end of each
 * line add a space. once we are at the end of the line we print the last
 * index of each line that is divisable by 50 then go to a new line and start
 * printing the amino acids in the linked list. print 10 per section 
 * once whole section is complete add a space and contuine. once 5 sections
 * are complete add a new line and start the index counting again.
 * This will loop until the last whole line is complete and any reaming 
 * sections will be printed. once this happes we fill any unused sections
 * with spaces and then print the remaining amion acids to the user.  
 ************************************************************************/
void printFormatted(ListNode* head)
{
  int i=0;
  int j=0;
  int index=10;
  ListNode* current= head;

  while(i<indexCount/10)
  {   
    printf("%10d",index);
    if(index%50!=0)
    {
      printf(" ");
    }
    i++;
    if(index%50==0){
      printf("\n");
      for(j=1;j<51;j++)
      {
	printf("%c",current->data);
	current=current->next;
	if(current==NULL)
	{
	  break;
	}
	if(j%10==0&&j!=0&&j!=50)
	{
	  printf(" ");
	}
      }
      printf("\n");
    }
    index+=10;
  }
  if((indexCount/10)%50!=0)
  {

    for(i=0;i<50-((indexCount%50)/10)*10;i++)
    {
      printf(" ");
      if(i%10==0&&i!=0&&i!=50)
      {
	printf(" ");
      }
    }
  }
    
  printf("\n");
  i=0;
  while(current!=NULL)
  {
    printf("%c",current->data);
    current=current->next;
    i++;
    if(i%10==0&&current!=NULL)
    {
      printf(" ");
    }
  }
  printf("\n");
    
  
}
    
    
/*************************************************************************
 * This fuction takes in one parameter:
 * ListNode pointer called head that is the start of the linked list
 * 
 * This fuction takes in no input from the user and gives no output
 *
 * This fuction will loop thourgh the whole linked list and free each of the 
 * nodes within the linked list. This is done because each of the node were
 * created with the use a malloc and there for must be freed inorder to not
 * have memory leaks
 ************************************************************************/
void freeList(ListNode* head)
{
  ListNode* tmp;
  while(head!=NULL)
  { 
    tmp=head;
    head=head->next;
    free(tmp);
  }

}

/*************************************************************************
 * This method takes in two parameters:
 * int called argc that is the number of command line arguments
 * An array of string litterals called argv that is the command line arguments
 *  
 * This function has input of commond line arguments to delete and modify
 * amion acids. it also takes in a file of amion acids
 * This fuctions out puts the header for the out put showing the user
 * the name of the amion acid chain thein inputed and calls other methods 
 * to output more
 *
 * This fuctions will read a file a amion acids represented by letters and 
 * loop thourgh the file and store them in a linked list. It will then sort 
 * the user input by if they want to delete a amion acid in the chain or if
 * they want to modify one. It will store these requests in an array of 
 * strings one for deleting and one for modifing. theses this are dynamically
 * allocated using malloc and realloc. it will then loop thourgh each of these
 * lists and delete or modify the linked list respectivly. it will then print 
 * the whole linked list by calling print formatted, and free the whole 
 * linekd list by calling free list and also free the delete and modify
 * list so there is no memory leaks. 
 ************************************************************************/
int main(int argc, char* argv[])
{

  int i=0;
  int index=0;
  char *endPtr;
  char** deleteList=NULL;
  char** modList=NULL;
  int delCont=1;
  int modCont=1;
  char c=getchar();
  ListNode* head = createNode(c);
  c=getchar();
  while(c!=EOF)
  {
    append(c,head);
    c=getchar();
    if(c=='\n')
    {
      c=getchar();
    }
  }

  printf("Spike protein sequence for %s:\n",argv[1]);

  i=2;
  while(i<argc)
  {
    if(*argv[i]=='d')
    {
      argv[i]++;
      if(delCont==1)
      {
	deleteList = (char**) malloc(delCont*sizeof(deleteList));
      }
      else
      {
	deleteList = (char**) realloc(deleteList,delCont*sizeof(deleteList));
      }
      deleteList[delCont-1]=argv[i];
      delCont++;
     
    }
    else
    {
      argv[i]++;
      if(modCont==1)
      {
	modList = (char**) malloc(modCont*sizeof(modList));
      }
      else
      {
	modList = (char**) realloc(modList,modCont*sizeof(modList));
      }
      modList[modCont-1]=argv[i];
      modCont++;
    }
    i++;
  }

  for(i=0;i<delCont-1;i++)
  {
    index = strtol(deleteList[i],&endPtr,10);
    deleteIndex((int)index,head);
  }
  
  for(i=0;i<modCont-1;i++)
  {
    index = strtol(modList[i],&endPtr,10);
    modify(index,*endPtr,head);
  }

 
  printFormatted(head);
  free(deleteList);
  free(modList);
  freeList(head);
  return 0;
}
