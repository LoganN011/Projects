# Dominos

The game of dominos has its origins in China and made its way into Europe
sometime in
the 18th century. There are many versions of the game and some use different
number of
pieces. A domino, the piece being played, has two numbers separated by a line.
The most
common version of the game uses 28 pieces. The rules of the game also vary a
great deal.

## Rules

The game starts with all dominos face down and mixed this is called the
boneyard.
Each player draws 7 dominos. One player starts the game by placing a domino face
up on the table. The players each take turns. on each turn the player plays a
single
domino matching the configuration on the table. The dominos will form two
parallel
rows shifted by a half domino. Two adjacent dominos located on different rows
must
have the same value in the overlapping halves. A blank is used as a wild card.
If a player has a domino that can extend the line of play in either direction,
they put it down and their turn ends. if the player does not have a domino able
to extend the line of play, they must draw from the boneyard and must continue
until
a matching domino is found or the boneyard is empty. If the player has a domino
able
to extend the line of play, they cannot draw from the boneyard. If the boneyard
is empty and the player cannot extend the line of play, they end their turn.
The game ends when the boneyard is empty and either the current player places
their
last domino or both players have taken a turn without placing a domino. At the
end of the game the dots on each player's dominos are counted and the player
with
the least number of dots wins. If both players have the same number of dots then
the player that played the last domino wins.

## Console Version

To play the console version of game once you run the jar file or run the
ConsoleGame
file. It will be you versus a computer player. The human always make the first
move.
In the console you will see that the current state of the board and your tray
has
been printed. You will select what domino you want to play by entering a number
based
on the position of the domino on the screen. So for the first move pick a number
1-7.
The computer will then take it move, and you will see the new state of the
board,
and it will ask you to pick another domino. It will the ask you to pick a side
either
left or right. Will accept input of either the word left or right and l or r
capitalization does not matter. Then it will play the domino if it is valid.
If you play a domino that is invalid it will ask you to try again and reselect
a new domino to play. You a domino can be played flipped or not flipped it will
ask you
if you want to flip the domino. To flip say yes or y, to not flip say no or n
and
capitalization does not matter. This will keep going until the game ends when it
will tell
you in the console who has won the game.

## GUI Version

To play the GUI version run the jar file or the GUIGame file. Once the GUI shows
up,
to play the game you will select a domino from the tray by clicking on the
domino
that you wish to play. You will then click in the center area of the screen to
play the domino.
To play on the left side of the board you must click on the left side of the
screen.
To play on the right side of the board you must click on the right side of the
screen.
Once a domino is played the computer player will play directly after you and the
screen will refresh. At the top of the screen you will see the two labels that
tell
you the number of dominos that are in the boneyard and the number of dominos
that
the computer player has. When playing a domino that is invalid a popup will
appear saying
that the move you made is invalid. You will then have to reselect a new domino
to play.
If a domino is played, and it can be played either flipped or not flipped a
popup will
show up asking if you want to flip the domino. If you click OK then the domino
will be
played flipped. If you click either cancel or close the window then it will not
be played flipped.
When the game end all elements on the screen will disappear and a message will
appear
saying the game is over and will either say the human won or the computer won.

## Known Bugs

* The visual representation of the board does not work as intended. There is two
  lines that are and the number that is able to be played off of are on the ends
  shifted by a half domino. The problem is that it does not keep track of where
  the
  dominos were played, so it will flip every move
* In the GUI version the ending with a tie might be broken. When testing the
  computer
  always seems to win. I think it is now fixed, but it could still be broken
* Other random edge cases might not have been accounted for. During testing
  sometimes
  it would not play the domino that was selected and it would skip your turn.
  This hasn't happened
  more than a couple of times so im not sure what is causing it.
* When Drawing a card in the GUI the only notification that this is happening is
  a
  print-out in the console. That is fine for the console version but not good in
  the GUI

## Future Features

* Better Computer player
* Adding a board around selected dominos in the GUI
* Better handling of drawing cards and sending the info to the play
* Better representation of the domino on the screen 


