package com.chandantp.challenges

import concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

import com.chandantp.challenges.ChessBoard.{Bishop, Knight, King, Queen, Rook}

object MainApp {

  val Silent = "-s"
  val Verbose = "-v"
  val PrintModes = Set(None, Some(Silent), Some(Verbose))
  val ChessPieces = Set(King, Queen, Rook, Bishop, Knight)

  def main(args: Array[String]) {
    try {
      if (args.size == 0) showUsage
      else {
        val (rows, columns, pieces, printMode) = parse(args)
        val service = new ChessService(rows, columns, pieces)

        val task = Future {
          val startTime = System.currentTimeMillis
          val solutions = service.computeSolutions
          val timeElapsedInMillis = System.currentTimeMillis - startTime
          println("Total Solutions = %d".format(solutions.size))
          println("Time elapsed = %.3f seconds (%d ms)".format(timeElapsedInMillis / 1000.0, timeElapsedInMillis))
        }

        // Progress indicator - indicate progress every 5 seconds
        while(!task.isCompleted) {
          if (service.solutionSize > 0) {
            val solutionSize = service.solutionSize
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
  }

  def parse(args: Array[String]) = {

    val (rows, columns, pieces, printMode) = args.size match {
      case 3 => (args(0).toInt, args(1).toInt, args(2), None)
      case 4 => (args(0).toInt, args(1).toInt, args(2), Option(args(3)))
      case _ => throw new IllegalArgumentException("Invalid number of arguments")
    }

    if (rows <= 0 || columns <= 0) {
      throw new IllegalArgumentException("Invalid chess board dimensions, rows & columns should be > 0")
    }

    if (!PrintModes.contains(printMode)) {
      throw new IllegalArgumentException("Invalid print mode: " + printMode)
    }

    if (!pieces.forall(ChessPieces.contains(_))) {
      throw new IllegalArgumentException("Invalid chess piece!, valid pieces are: " + ChessPieces.mkString(","))
    }

    (rows, columns, pieces, printMode)
  }

  def printSolution(count: Int, board: ChessBoard, printMode: Option[String]): Unit = printMode match {
    case None => println("Found solution %d".format(count))
    case Some(Silent) => // print nothing
    case Some(Verbose) => println("Solution %d:\n%s".format(count, board.toPrettyPrintString))
    case _ => throw new IllegalArgumentException("Unknown print mode!")
  }

  def showUsage {
    println
    println("USAGE: run <rows> <columns> <pawns> [-v|-s]")
    println("   rows : number of rows on the chess board")
    println("columns : number of columns on the chess board")
    println("  pawns : a string comprising of 'K', 'Q', 'R', 'B', 'N' which denote pawns")
    println("          repeat the characters if the chess piece occurs multiple times.")
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
