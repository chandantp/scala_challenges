package com.chandantp.challenges

class ChessService(rows: Int, columns: Int, pieces: String) {

  private val pawns = pieces.split("").toList.map(_(0))

  private val solutionTree = Tree("root", Map())

  private val solutions = collection.mutable.ListBuffer[ChessBoard]()

  /*
   * Find all possible combinations of non-threatening positions
   * that the pawns can occupy on the chess board
   */
  private def placePawn(pawn: Char, usedPawns: List[Char], board: ChessBoard): Unit = {
    val usedPawnsUpdated = pawn :: usedPawns
    val remainingPawns = pawns.diff(usedPawnsUpdated).distinct

    for (row <- 0 until rows; col <- 0 until columns) {
      if (board.canPlacePawn(pawn, row, col)) {
        val newBoard = board.placePawn(pawn, row, col)
        val branch = newBoard.toStringReadable
        if (!solutionTree.isExplored(branch)) {
          remainingPawns.foreach(pawn => placePawn(pawn, usedPawnsUpdated, newBoard))
          solutionTree.track(branch)
          if (branch.size == pawns.size) {
            solutions.append(newBoard)
          }
        }
      }
    }
  }

  def computeSolutions: List[ChessBoard] = {
    pawns.distinct.foreach(pawn => placePawn(pawn, Nil, new ChessBoard(rows, columns)))
    solutions.toList
  }

  def displaySolutions: Unit = solutions.zipWithIndex.foreach {
    case (chessBoard, i) => {
      println("Solution %d:".format(i+1))
      chessBoard.prettyPrint
      println
    }
  }

}
