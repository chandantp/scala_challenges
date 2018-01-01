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
	
	$ JAVA_OPTS="-Xms2g -Xmx4g" scala target/scala-2.12/chess_2.12-1.0.jar 7 7 KKQQBBN
    Found solution 538682
    Found solution 1205125
    Found solution 1734623
    Found solution 2340682
    Found solution 3018291

    Total Solutions = 3063828
    Time elapsed = 25.472 seconds (25472 ms)
     
(2) Board size: 7x7, Pawns: 2 Kings, 2 Queens, 2 Bishops & 1 Knight, PrintMode: Verbose
	
	$ JAVA_OPTS="-Xms2g -Xmx4g" scala target/scala-2.12/chess_2.12-1.0.jar 7 7 KKQQBBN -v
    Solution 526188:
    | | | |K| | |K|
    | |Q| | | | | |
    | | | | | | | |
    | | | | | | |N|
    | | | |B| | | |
    | | | | | | |B|
    | | |Q| | | | |

    Solution 1213526:
    | | | |K| | |K|
    | |Q| | | | | |
    | | | | | | | |
    | | | | | | |N|
    | | | |B| | | |
    | | | | | | |B|
    | | |Q| | | | |

    Solution 1748635:
    | | | |K| | |K|
    | |Q| | | | | |
    | | | | | | | |
    | | | | | | |N|
    | | | |B| | | |
    | | | | | | |B|
    | | |Q| | | | |

    Solution 2373123:
    | | | |K| | |K|
    | |Q| | | | | |
    | | | | | | | |
    | | | | | | |N|
    | | | |B| | | |
    | | | | | | |B|
    | | |Q| | | | |

    Solution 3031855:
    | | | |K| | |K|
    | |Q| | | | | |
    | | | | | | | |
    | | | | | | |N|
    | | | |B| | | |
    | | | | | | |B|
    | | |Q| | | | |

    Total Solutions = 3063828
    Time elapsed = 25.312 seconds (25312 ms)

(3) Board size: 7x7, Pawns: 2 Kings, 2 Queens, 2 Bishops & 1 Knight, PrintMode: Silent

	$ JAVA_OPTS="-Xms2g -Xmx4g" scala target/scala-2.12/chess_2.12-1.0.jar 7 7 KKQQBBN -s
    Total Solutions = 3063828
    Time elapsed = 24.597 seconds (24597 ms)