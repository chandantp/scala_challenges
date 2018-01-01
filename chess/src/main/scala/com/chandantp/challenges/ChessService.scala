package com.chandantp.challenges

class ChessService(rows: Int, columns: Int, pieces: String) {

  private val chessPieces = pieces.split("").toList.map(_ (0))
  private var solutions = Set[ChessBoard]()

  def computedSolutionsCount = solutions.size

  def lastComputedSolution: ChessBoard = solutions.head

  /*
   * Find all possible combinations of non-threatening positions
   * that the chess pieces can occupy on the chess board
   */
  def computeSolutions: Set[ChessBoard] = {

    def placeChessPiece(pieceToBePlaced: Char, piecesRemaining: List[Char], board: ChessBoard): Unit = {
      val minimumStartingPosition = board.minLinearPosition(pieceToBePlaced)
      for(currentPosition <- 0 until rows * columns) {
        val (row, col) = (currentPosition / rows, currentPosition % rows)

        if (currentPosition >= minimumStartingPosition && board.isSafeToPlace(pieceToBePlaced, row, col)) {
          val updatedBoard = board.place(pieceToBePlaced, row, col)
          if (updatedBoard.piecesCount == chessPieces.size) {
            solutions += updatedBoard
          }
          else if (piecesRemaining.size > 0) {
            placeChessPiece(piecesRemaining.head, piecesRemaining.tail, updatedBoard)
          }
        }
      }
    }

    placeChessPiece(chessPieces.head, chessPieces.tail, ChessBoard.create(rows, columns))
    solutions
  }

}
