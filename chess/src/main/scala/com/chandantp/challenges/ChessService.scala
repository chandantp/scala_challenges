package com.chandantp.challenges

class ChessService(rows: Int, columns: Int, chessPieces: List[ChessPiece]) {

  require(rows > 0 && columns > 0)

  private var solutions = List[ChessBoard]()

  //
  // Below methods (computedSolutionsCount, lastComputedSolution) provide the necessary
  // tooling to track the progress of the computation to find all possible solutions
  //
  def computedSolutionsCount = solutions.size

  def lastComputedSolution: ChessBoard = solutions.head

  //
  // Find all possible combinations of non-threatening positions
  // that the chess pieces can occupy on the chess board
  //
  def computeSolutions: Set[ChessBoard] = {

    def placeChessPiece(pieceToBePlaced: ChessPiece, piecesRemaining: List[ChessPiece], board: ChessBoard): Unit = {
      val minimumStartingPosition = board.maxLinearPosition(pieceToBePlaced)
      for(currentPosition <- 0 until rows * columns) {
        val (row, col) = (currentPosition / rows, currentPosition % rows)

        if (currentPosition >= minimumStartingPosition && board.isSafeToPlace(pieceToBePlaced, row, col)) {
          val updatedBoard = board.place(pieceToBePlaced, row, col)
          if (updatedBoard.chessPiecesCount == chessPieces.size) {
            solutions ::= updatedBoard
          }
          else if (piecesRemaining.size > 0) {
            placeChessPiece(piecesRemaining.head, piecesRemaining.tail, updatedBoard)
          }
        }
      }
    }

    placeChessPiece(chessPieces.head, chessPieces.tail, ChessBoard.create(rows, columns))
    solutions.toSet
  }

}
