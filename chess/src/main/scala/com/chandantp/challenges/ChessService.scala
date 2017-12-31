package com.chandantp.challenges

class ChessService(rows: Int, columns: Int, pieces: String) {

  private val chessPieces = pieces.split("").toList.map(_ (0))
  private val solutionSpace = TrackerTree(collection.mutable.Map())
  private var solutions = List[ChessBoard]()

  def solutionSize = solutions.size

  def lastComputedSolution: ChessBoard = solutions.head

  def computeSolutions: List[ChessBoard] = {
    /*
     * Find all possible combinations of non-threatening positions
     * that the chess pieces can occupy on the chess board
     */
    def placeChessPiece(chessPiece: Char, usedPieces: List[Char], board: ChessBoard): Unit = {
      val usedPiecesUpdated = chessPiece :: usedPieces
      val remainingPieces = chessPieces.diff(usedPiecesUpdated).distinct

      for (row <- 0 until rows; col <- 0 until columns) {
        if (board.isSafeToPlace(chessPiece, row, col)) {
          val newBoard = board.place(chessPiece, row, col)
          val newBoardBranch = newBoard.encoded
          if (!solutionSpace.isExplored(newBoardBranch)) {
            remainingPieces.foreach(piece => placeChessPiece(piece, usedPiecesUpdated, newBoard))
            solutionSpace.markAsExplored(newBoardBranch)
            if (newBoardBranch.size == chessPieces.size) {
              solutions = newBoard :: solutions
            }
          }
        }
      }
    }

    chessPieces.distinct.foreach(piece => placeChessPiece(piece, Nil, ChessBoard.create(rows, columns)))
    solutions
  }

}
