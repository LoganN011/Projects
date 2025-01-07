# SmartRail

This Project is made by Cameron Fox and Logan Nunno

## Description

SmartRail is a visionary system concept that allows trains to interact directly
with the
tracks, switches, and lights that a part of a light rail network. Each train is
an independent
agent, which selects a destination (normally based on a schedule, policy, or
conductor input),
discovers the route to the destination, and secures the route against possible
collisions (by
repositioning switches and setting lights). Trains are designed not to be able
to run red
lights. For a train to move, it needs to secure a route or a portion of it by
locking lights
and switches appropriately, both along the route itself and in a protection zone
around the
route. The latter is designed to prevent other trains from entering or crossing
the route.
Trains function independently of each other and cannot communicate with other
trains.
A train is aware of the track on which it is located and, similarly, the track
is aware of the
identity of the train present at that location. Two trains cannot be located on
the same
track–a route consists of multiple tracks that connect with each other.
For modularity and flexibility reasons, each component of the light rail system
is aware
only of its immediate neighbors in the system.

## Simplifying Assumptions

* Each station can only hold 1 train
* There are no railroad crossing
* Trains can only be added to a station
* A train can only travel to a single station
* A train can be heading either to the left or to the right. A train will
  continue heading
  in its current direction left/right until it reaches a station
* Trains change direction only when at a station
* All trains move at the same speed

## GUI Description

Stations are black squares that have labels next to them telling the use what
station
is what. They are connected by brown rectangles that represent the track
segments.
The yellow squares are the switches. When a train is added it will be added next
to the station that it was added to. The color of the train represent diffrent
things.
When the train is Grey it is idle and not doing anything. When the train is
yellow it is
looking for a path. When the train turns red it is actively locking the path it
is going to take.
When the train turns green it is moving. When a path is locked by a train red
circles will be added on the
component that is locked and will be removed when the train passes the location.

## How to Use
### 1. Select a Station and Add a Train
* hen the application opens, you’ll see a dropdown menu on the right side of 
the screen. Click on it using the mouse to view a list of all available stations.
* Choose a station from the list, then click the "Add Train" button to the 
right of the dropdown. This will add a train to the selected station.
* Repeat this process to add more trains, but note that each station can only 
hold one train at a time. You can add trains at any point while the program 
is running, so there’s no need to add them all at once.

### 2. Configure Train Details
* After adding a train, three dropdown menus will appear for each train:
  * Train Number: This dropdown displays the train’s number. There is no need 
  to change it, as the correct number will already be selected by default.
  * Select Station: Use the mouse to select the destination station for this
  train. Trains can only move one direction at a time, so ensure the selected 
  station is in line with the train’s current position.
  * Direction: Use the mouse to select the direction the train should 
  travel—either "Left" or "Right." This determines the train's search direction 
  for the destination station.
* If you don’t want a train to move, select "Stay" in the "Select Station" 
dropdown, or leave it at the default option. This will keep the train at its 
current station. The "Direction" box can be ignored for stationary trains.

### 3. Start Train Movement
* Once your train configurations are set, click the "Start" button with the 
mouse. Trains that are ready to move will turn Yellow, indicating they are 
searching for their designated station.
* When a train locks onto its route, it will turn Red. This locking process 
ensures that all passengers and cargo will reach their destination safely.
You’ll see a red circle appear on rails, switches, and stations, showing that 
the path has been secured.
* Finally, the train will turn Green and start moving along the rails to its
destination.

### 4. Repeat as Needed
* Once the trains reach their destinations, you can configure their next moves. 
Simply select the next destination and direction for each train, 
then start the process again. 

## Future Features

* Better looking GUI
* Adding checking for train configuration
* Better logging for debugging
* Able to Remove a Train

## Bugs

* When two train are trying to go the same station. The train that gets there
  first
  will be deleted from the station but will still exist on the GUI and in the
  program and
  if a new message is passed to it will infinity be looking for a new path
* Not all configuration files are check to be valid. There could be bugs when
  having
  two of any component in the same place
* One some computers the larger configuration will not run. There is something
  about
  running out of memory when making the canvas, but it works on the cs lab
  computers.
  This only happens when running either uneven or sample but might not happen to
  you
* Some Bugs about the train number and it dispersing in places and not
  appearing on the screen sometimes 
