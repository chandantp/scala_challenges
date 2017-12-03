package com.chandantp.challenges

import com.chandantp.challenges.ChessBoard._
import ChessService._

import collection.mutable

object ChessService {
  val Verbose = "-v"
  val Silent = "-s"
  val ValidPawns = Set(King, Queen, Rook, Bishop, Knight)
}

class ChessService(rows: Int, columns: Int, pieces: String, printMode: Option[String]) {

  private val pawns = {
    if (!pieces.forall(ValidPawns.contains(_))) {
      throw new IllegalArgumentException("Invalid pawn!, valid pawns are: " + ValidPawns.mkString(","))
    }
    pieces.split("").toList.map(_ (0))
  }

  private val solutionTree = TrackerTree(mutable.Map())

  private val solutions = collection.mutable.ListBuffer[ChessBoard]()

  /*
   * Find all possible combinations of non-threatening positions
   * that the pawns can occupy on the chess board
   */
  private def placePawn(pawn: Char,
                        usedPawns: List[Char],
                        board: ChessBoard): Unit = {
    val usedPawnsUpdated = pawn :: usedPawns
    val remainingPawns = pawns.diff(usedPawnsUpdated).distinct

    for (row <- 0 until rows; col <- 0 until columns) {
      if (board.canPlacePawn(pawn, row, col)) {
        val newBoard = board.placePawn(pawn, row, col)
        val branch = newBoard.encoded
        if (!solutionTree.isExplored(branch)) {
          remainingPawns.foreach(pawn => placePawn(pawn, usedPawnsUpdated, newBoard))
          solutionTree.markAsExplored(branch)
          if (branch.size == pawns.size) {
            solutions.append(newBoard)
            printSolution(solutions.size, newBoard)
          }
        }
      }
    }
  }

  def computeSolutions: List[ChessBoard] = {
    pawns.distinct.foreach(pawn => placePawn(pawn, Nil, new ChessBoard(rows, columns)))
    solutions.toList
  }

  def printSolution(count: Int, board: ChessBoard): Unit = printMode match {
    case None => println("Found solution %d".format(count))
    case Some(mode) => mode match {
      case Silent => // print nothing
      case Verbose => {
        println("Solution %d:".format(count))
        board.prettyPrint
        println
      }
    }
    case _ => throw new IllegalArgumentException("Unknown print mode!")
  }

}
