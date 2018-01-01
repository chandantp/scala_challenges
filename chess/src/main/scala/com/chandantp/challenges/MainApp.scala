package com.chandantp.challenges

import concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object MainApp {

  val Silent = "-s"
  val Verbose = "-v"
  val PrintModes = Set(None, Some(Silent), Some(Verbose))

  def main(args: Array[String]): Unit = try {
    if (args.size == 0) showUsage
    else {
      val (rows, columns, chessPieces, printMode) = parse(args)
      val service = new ChessService(rows, columns, chessPieces)

      val task = Future {
        val startTime = System.currentTimeMillis
        val solutions = service.computeSolutions
        val timeElapsedInMillis = System.currentTimeMillis - startTime
        println("Total Solutions = %d".format(solutions.size))
        println("Time elapsed = %.3f seconds (%d ms)".format(timeElapsedInMillis / 1000.0, timeElapsedInMillis))
      }

      // Progress indicator - indicate progress every 5 seconds
      while(!task.isCompleted) {
        if (service.computedSolutionsCount > 0) {
          val solutionSize = service.computedSolutionsCount
          printSolution(solutionSize, service.lastComputedSolution, printMode)
        }
        Thread.sleep(5000)
      }
    }
  } catch {
    case e: Exception => {
      e.printStackTrace
      showUsage
    }
  }

  def parse(args: Array[String]) = {
    val (rows, columns, pieces, printMode) = args.size match {
      case 3 => (args(0).toInt, args(1).toInt, args(2), None)
      case 4 => (args(0).toInt, args(1).toInt, args(2), Option(args(3)))
      case _ => throw new IllegalArgumentException("Invalid number of arguments")
    }

    val chessPieces = pieces.split("").toList.map(ChessPiece(_))

    if (!PrintModes.contains(printMode)) {
      throw new IllegalArgumentException("Invalid print mode: " + printMode)
    }

    (rows, columns, chessPieces, printMode)
  }

  def printSolution(count: Int, board: ChessBoard, printMode: Option[String]): Unit = printMode match {
    case None => println("Found solution %d".format(count))
    case Some(Silent) => // do nothing
    case Some(Verbose) => println("Solution %d:\n%s".format(count, board.toPrettyPrintString))
    case _ => throw new IllegalArgumentException("Unknown print mode!")
  }

  def showUsage {
    println
    println("USAGE: run <rows> <columns> <pawns> [-v|-s]")
    println("   rows : number of rows on the chess board")
    println("columns : number of columns on the chess board")
    println(" pieces : a string comprising of 'K', 'Q', 'R', 'B', 'N' which denote chess pieces")
    println("          repeat the characters if the chess piece occurs multiple times.")
    println("     -s : silent mode, no progress information shown, only final solution count is printed (optional)")
    println("     -v : solution count & pretty print solution is printed every 5 seconds (optional)")
    println
    println("Note that in the absence of -s|-v, only solution count progress info is printed every 5 seconds")
    println
    println("Examples:")
    println(" sbt run 4 4 QQQQ")
    println(" sbt run 5 5 KKBBN -s")
    println(" sbt run 6 6 KKQQBB -v")
    println
  }

}
