# Distributed Auction

## Authors
Cameron Fox, Logan Nunno, Tanisha Patel, and Lawrence Rybarcyk.

# Description
The Distributed Auction is a simulation of a system of multiple auction houses 
selling items, multiple agents buying items, and a bank to keep track of 
everyone’s funds. The bank will exist on one machine at a static known address, 
the agents and auction houses will be dynamically created on other machines.

## The Bank
The Bank is the component of the simulation that establishes and holds 
accounts for the Auction Houses and Agents and handles their requests to 
deposit, hold, release or transfer funds. Only a single instance of the bank 
is used in the simulation. It acts as a server to both the auction houses 
and the agents. Each auction house registers with the bank, obtains an account 
with a $0 initial balance. Each agent deposit some amount of funds into the 
bank upon establishing an account with it. These funds are used to bid on 
auction house items for sale. While an agent has the highest bid on an item, 
those funds are held by the bank and either released if the agent is outbid 
or transferred to the auction house from the winning agents account. Upon 
registering with the bank, or when subsequent auction houses are add, an 
agent receives a list of auction houses so they are aware of them and can 
visit them.

## The Auction House
The Auction House (AH) is one piece of the simulation. Multiple instances can 
reside on one or more computers, but those on the same computer must have 
different port numbers. Each auction house is responsible for first, 
connecting to the bank to establish a $0 balance account and providing the 
bank with its(AH)host and port number so that the bank can relay this to 
agent(s). The agents in turn use this information to then connect to the 
auction houses and start the bidding. An auction house is responsible for 
provide a listing(min 3 items) to an inquiring agent. The AH also keeps track 
of bid made by agents on it's various items up for auction. Each time a bid 
is made on an item, a 30 sec timer counts down. If someone else bids on the 
same item, the timer reset to 30 sec and resumes countdown. If no one else bids 
within the 30 sec time. Then that bid is declared the winning bid, and the 
winner is notified. 

## The Agent
The Agents in this simulation are tasked with bidding on the various items 
available at the auction houses. Initially each agent registers and deposits 
funds in the bank. These funds are used to bid on items. Each bid must meet 
the minimum bid and the agent must have enough funds available, i.e., 
not held, for the bid to be accepted by the auction house. Otherwise the bid
is rejected. Upon winning a bid, the agent is notified by the auction house 
to tell the bank to transfer funds to the auction house. 

## Running the Distributed Auction House

# How to use the Auction House
To set up an Auction House, follow these steps:
1. **Enter the Port Number:** Provide the port number on which the Auction House
will run in the first text field.
2. **Enter the Bank Host Name:** Type the name of the bank host in the second 
text field.
3. **Enter the Bank Port Number:**  Enter the port number of the bank in the 
third text field.

## Notes 
* If any port number is incorrect, a warning will appear in a pop-up window.
Please follow the instructions in the pop-up to correct your input.
* The Welcome screen will guide you through this setup process.

# How to use the Agent
To set up an Agent, follow these steps:
1. **Enter the Account Name:** Type the name that you wish to use for the Account. 
2. **Enter the Bank Balance:** Enter the amount of money you wish to deposit into
the bank. 
3. **Enter the Bank Host Name:** Type the name of the bank host in the second
   text field.
4. **Enter the Bank Port Number:**  Enter the port number of the bank in the
   third text field.
5. **Enable Auto-Bidder (Optional):**
   * Check the "Enable Auto-Bidder" checkbox if you want to start an auto-bidder.
   * Once enabled, a new window will open to allow you to configure and monitor 
the auto-bidder.
## Auto Bidder Set Up
How to set up the Auto Bidder, follow these steps
1. **Enter the Auto Bidder Names:** Type the name of the Auto Bidder in the first
second text field.
2. **Enter the Bank Balance:** Type the bank balance of the Auto Bidder in the 
second text field. 
## Notes
* If any input is entered incorrectly, a warning will appear in a pop-up window.
Please follow the instructions in the pop-up to correct your input.
* The Welcome Screen will guide you through this setup process.

## Bank Set Up

The bank can be set up either with 1 or no command line arguments. The command
line argument that is passed to the bank represents the port number of the bank.
If no command line is added when running the bank the default port will be "
1234"
for the bank.

## Starting from the command line

**NOTE**: For both the Agent, Auto Bidder, and Auction house you are able to
start
the program using command line

### Agent

There 3 required command line arguments for starting the normal agent GUI from
the command line.

1. The name of the agent as a string
2. The amount of money that the agent will start with
3. The host name of the Bank that is being connected
4. The port number of the Bank

To start and Auto Bidder there needs to be an additional 5th command line
argument.
This fifth entry can be anything.

### Auction House

There are 3 required command line arguments when starting the Auction house from
the command line

1. The port number of Auction house
2. The host name of the Bank that is being connected
3. The port number of the Bank

## Bugs List
* When a connection ends over the network there is an java.io.EOFException that
  is caused but just ignored so nothing will show
* When stopping an AH in the Bid screen the screen will freeze until a new AH is
  added
* Sometimes the buttons do not show up on the screen. There is an add auction
  house
  button to handle these cases
* Auto bidder not ending the last thread when starting from the console.
  The method will stop bidding and be removed from all AHs and the bank but the
  program does not end till killed again
* When running the auto bidder in the GUI with another agent and exiting there
  will be errors when trying to close 
* The timer will flash when the GUI is updated

## Future Features

* A list of items won by the agent on the screen
* A list of active bids on the agent screen
* Images for each of the items
* 