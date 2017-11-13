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
- The app accepts the input (board size & chess pieces) as command line arguments

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

Running the App
---------------
From project root directory, run one of the following commands:

    $ sbt run

Examples
--------

(1) Board size: 3x3, Pawns: 2 Kings & 1 Rook
	
	$ sbt run 3 3 KKR
    [info] Running com.chandantp.challenges.MainApp 3 3 KKR
    
    Solution 1 :
    |K| |K|
    | | | |
    | |R| |
    
    Solution 2 :
    |K| | |
    | | |R|
    |K| | |
    
    Solution 3 :
    | | |K|
    |R| | |
    | | |K|
    
    Solution 4 :
    | |R| |
    | | | |
    |K| |K|
    
    Solutions Count = 4
    Time elapsed = 0.0040 seconds (4 ms)

(2) Board size: 4x4, Pawns: 2 Kings & 2 Rooks
	
	$ sbt run 4 4 QQQQ
    [info] Running com.chandantp.challenges.MainApp 4 4 QQQQ
    
    Solution 1 :
    | |Q| | |
    | | | |Q|
    |Q| | | |
    | | |Q| |
    
    Solution 2 :
    | | |Q| |
    |Q| | | |
    | | | |Q|
    | |Q| | |
    
    Solutions Count = 2
    Time elapsed = 0.0080 seconds (8 ms)
