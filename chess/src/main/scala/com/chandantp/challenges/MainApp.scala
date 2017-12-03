package com.chandantp.challenges

import ChessService.{Silent, Verbose}

object MainApp {

  def main(args: Array[String]) {
    try {
      if (args.size == 0) showUsage else {
        val (rows, columns, pieces, printMode) = parse(args)
        val chessSvc = new ChessService(rows, columns, pieces, printMode)

        val startTime = System.currentTimeMillis()
        val solutions = chessSvc.computeSolutions
        val timeElapsedInMillis = System.currentTimeMillis() - startTime

        println("Total Solutions = %d".format(solutions.size))
        println("Time elapsed = %.3f seconds (%d ms)".format(timeElapsedInMillis / 1000.0, timeElapsedInMillis))
      }
    } catch {
      case e: Exception => {
        e.printStackTrace
        showUsage
      }
    }
  }

  def parse(args: Array[String]) = args.size match {
    case 3 => (args(0).toInt, args(1).toInt, args(2), None)
    case 4 => {
      if (args(3) != Silent && args(3) != Verbose) {
        throw new IllegalArgumentException("Invalid print mode: " + args(3))
      }
      (args(0).toInt, args(1).toInt, args(2), Option(args(3)))
    }
    case _ => throw new IllegalArgumentException("Invalid number of arguments")
  }

  def showUsage {
    println
    println("USAGE: run <rows> <columns> <pawns> [-v|-s]")
    println("   rows : number of rows on the chess board")
    println("columns : number of columns on the chess board")
    println("  pawns : a string comprising of 'K', 'Q', 'R', 'B', 'N' which denote pawns")
    println("          repeat the characters if the pawn occurs multiple times.")
    println("     -s : silent mode, solution count & solution are not printed (optional)")
    println("     -v : print solution count & solution (optional)")
    println
    println("Note that in the absence of -v|-s, only the solution count is printed")
    println
    println("Examples:")
    println(" sbt run 4 4 QQQQ")
    println(" sbt run 5 5 KKBBN -s")
    println(" sbt run 6 6 KKQQBB -v")
    println
  }
}
