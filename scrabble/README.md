# Scrabble

Scrabble is a word game in which two to four players score points by placing
tiles
bearing a single letter onto a board divided into a 15 by 15 grid of squares.
The tiles must form words that, in crossword fashion, read left to right in rows
or downward in columns, and be included in a standard dictionary

## Rules

There are 100 tiles a standard Scrabble game. They are placed in bag
to serve as a pool of tiles. Initially, each player draws seven tiles and places
them on their tray.
The first player combines two or more of his or her letters to form a word and
places
it on the board to read either across or down with one letter on the center
square.
Diagonal words are not allowed. The Human player always takes the first turn.
Complete your turn by clicking the take turn button that is in the upper right
conner of
the display. Then draw as many new letters as you played; always keep seven
letters on your rack, as long as
there are enough tiles left in the bag. The second player, and then each in
turn, adds one or more
letters to those already played to form new words. All letters played on a turn
must
be placed in one row across or down the board, to form at least one complete
word.
If, at the same time, they touch others letters in adjacent rows, those must
also form
complete words, crossword fashion, with all such letters. The player gets full
credit for
all words formed or modified on his or her turn. No tile may be shifted or
replaced
after it has been played and scored. Blanks: The two blank tiles may be used as
any letters. When playing a blank, you must state which letter it represents.
It remains that letter for the rest of the game. The game ends when all letters
have been drawn and one player uses his or her last letter; or when all
possible plays have been made.

## Scoring

The score for each letter is based on the standard rules of scrabble.
The score for each turn is the sum of the letter values in each word(s) formed
or
modified on that turn, plus the additional points obtained from placing letters
on
Premium Squares. Premium Letter Squares: A light blue square doubles the score
of a letter placed on it; a dark blue square triples the letter score. Premium
Word Squares:
The score for an entire word is doubled when one of its letters
is placed on a pink square: it is tripled when one of its letters is placed on a
red square.
Letter and word premiums count only on the turn in which they are played. On
later
turns, letters already played on premium squares count at face value.
When a blank tile is played on a pink or red square, the value of the word is
doubled
or tripled, even though the blank itself has no score value. When two or more
words are formed in the same play, each is scored. The common
letter is counted (with full premium value, if any) for each word.
BINGO! If you play seven tiles on a turn, it’s a Bingo. You score a premium of
50
points after totaling your score for the turn. The player with the highest final
score wins the game.
In the case of a tie the computer player wins.

## How to Play

When playing the full game with the GUI you will see a board in the center of
screen
and your tray at the bottom of the screen. On the right hand side of the screen
you
will see a couple of labels that show the Humans score and the computers score.
There is also a button that says take turn on it. This will be used to play your
move.
To make a move click on the tile and the space on the board where you want the
tile
to be played in any order. Once you have selected both the tile will be removed
from your
tray and be placed on a board once you have placed all the tiles that you wish
to make a move click the take turn button. this will update your tray and update
your
score if it is a valid move. If the move that was made was not valid then all
played
tiles will return to your tray and be removed from the board for you to tray
again.
Once you make a move that is valid and it is score the computer player will take
a turn and place its move on the board. You will then be able to keep making
moves
until the game ends. If you play a blank tile then a pop up will ask you to
enter a letter
into a text box enter the letter you wish the blank to represent.

## Future Features

* Unplayed Letters: When the game ends, each player’s score is reduced by the
  sum of
  his or her unplayed letters. In addition, if a player has used all of his or
  her letters,
  the sum of the other players’ unplayed letters is added to that player’s
  score.
* In case of a tie, the player with the highest score before adding or deducting
  unplayed letters wins.
* Add handling for when the user does not enter a valid input for a blank tile
* Shuffle button for the tray
* Ability to undo a tile that was placed on the board
* Better looking GUI
* Drag and drop to place tiles

## Known Bugs

* There are definitely tests for the scoring where it might not score it right
  or might say its valid when it's not. I check a lot of cases but some were
  never tested
* Computer player takes a second to find a move when having blank tiles worse
  with more than 1
* When the game ends if you keep accepting the pop ups then it will keep going
  and make the score lower and cause bugs

## Score Checker

The score check will take in any two boards based on the requirements from the
project. They will be compared and the move that was made will be score and
the move that was made and the score will be printed. It can take any number of
boards and score them. Most if not all of the cases were tested and produce the
required output.

## Solver

The solver finds the best move possible based on the board and
the current tray. It does this by using a recursive back tracking algorithm
described the worlds fastest scrabble solver found [here](
https://www.cs.cmu.edu/afs/cs/academic/class/15451-s06/www/lectures/scrabble.pdf).
I also use the data structure that the paper talks about called a Trie or a
prefix
tree. This is used to optimize the amount of searching that is required, so we
do
not try to find words based on letters that could never make part of a word. 
