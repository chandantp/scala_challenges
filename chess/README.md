Problem
-------
The problem is to find all unique configurations of a set of normal chess pieces on a chess board with 
dimensions M×N where none of the pieces is in a position to take any of the others. Providing the number 
of results is useful, but not enough to complete the assignment. Assume the colour of the piece does not 
matter, and that there are no pawns among the pieces.

Write a program which takes as input:
- The dimensions of the board: M, N
- The number of pieces of each type (King, Queen, Bishop, Rook and Knight) to try and place on the board

As output, the program should list all the unique configurations to the console for which all of the 
pieces can be placed on the board without threatening each other.

When returning your solution, please provide with your answer the total number of unique configurations 
for a 7 ×7 board with 2 Kings, 2 Queens, 2 Bishops and 1 Knight. Also provide the time it took to get the 
final score. Needless to say, the lower the time, the better.

Readme
------
- This application is written using Scala
- The app accepts the input (board size, chess pieces, print mode) as command line arguments

Caveats
-------
- If program is executed in silent mode, NO PROGRESS INFORMATION IS SHOWN!!
  This is not advisable for large board sizes (7x7 or more) where the program can run for several
  minutes or even hours before displaying the final output.
    
- For large board sizes >= 7x7, set bigger heap size as shown below. Alternatively, you set it for all 
  input regardless of the board size.

    JAVA_OPTS = "-Xms4g -Xmx8g"

  Refer to the examples below for more information.

Dependencies
------------
- SBT (Scala build tool)
- Scala compiler (taken care of by SBT)
- ScalaTest (taken care of by SBT)
- JDK 1.8

Test execution
--------------
From project root directory, run following command to run test cases

    $ sbt test

Usage
-----
    USAGE: run <rows> <columns> <pawns> [-v|-s]
        rows : number of rows on the chess board
     columns : number of columns on the chess board
       pawns : a string comprising of 'K', 'Q', 'R', 'B', 'N' which denote pawns
               repeat the characters if the pawn occurs multiple times.
          -s : silent mode, solution count & solution are not printed (optional)
          -v : print solution count & solution (optional)

    Note that in the absence of -v|-s, only the solution count is printed

Running the App
---------------
From project root directory, run one of the following commands:

    $ sbt run <rows> <columns> <pawns> [-v|-s]

    $ scala chess_2.12-1.0.jar <rows> <columns> <pawns> [-v|-s]

Examples
--------

(1) Board size: 7x7, Pawns: 2 Kings, 2 Queens, 2 Bishops & 1 Knight, PrintMode: Normal
	
	$ JAVA_OPTS="-Xms4g -Xmx8g" scala chess_2.12-1.0.jar 7 7 KKQQBBN
	Found solution 1
    Found solution 2
    Found solution 3
        .
        .
        .
    Found solution 3063827
    Found solution 3063828
    Total Solutions = 3063828
    Time elapsed = 598.139 seconds (598139 ms)
     
(2) Board size: 3x3, Pawns: 2 Kings & 1 Rook, PrintMode: Verbose
	
	$ export JAVA_OPTS="-Xms4g -Xmx8g" 
	$ scala chess_2.12-1.0.jar 3 3 KKR -v
    Solution 1:
    |K| |K|
    | | | |
    | |R| |
    
    Solution 2:
    |K| | |
    | | |R|
    |K| | |
    
    Solution 3:
    | | |K|
    |R| | |
    | | |K|
    
    Solution 4:
    | |R| |
    | | | |
    |K| |K|
    
    Total Solutions = 4
    Time elapsed = 0.039 seconds (39 ms)

(3) Board size: 5x5, Pawns: 5 Queens, PrintMode: Silent

	$ JAVA_OPTS="-Xms4g -Xmx8g"
	$ scala chess_2.12-1.0.jar 5 5 QQQQQ -s
    Total Solutions = 10
    Time elapsed = 0.098 seconds (98 ms)