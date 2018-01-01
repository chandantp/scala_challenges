package com.chandantp.challenges

object ChessPiece {
  def apply(piece: String): ChessPiece = piece.trim.toUpperCase match {
    case "K" => King
    case "Q" => Queen
    case "R" => Rook
    case "B" => Bishop
    case "N" => Knight
    case _ => throw new IllegalArgumentException("Unknown chess piece '%s'".format(piece))
  }
}

sealed class ChessPiece(name: Char) {
  def attackPositions(row: Int, col: Int, board: ChessBoard): List[(Int, Int)] = Nil
  override def toString: String = name.toString
}

case object King extends ChessPiece('K') {
  override def attackPositions(row: Int, col: Int, board: ChessBoard): List[(Int, Int)] = {
    List((row, col), (row - 1, col - 1), (row - 1, col), (row - 1, col + 1), (row, col + 1),
      (row + 1, col + 1), (row + 1, col), (row + 1, col - 1), (row, col - 1))
  }
}

case object Queen extends ChessPiece('Q') {
  override def attackPositions(row: Int, col: Int, board: ChessBoard): List[(Int, Int)] = {
    board.allRowPositions(row) ::: board.allColumnPositions(col) ::: board.allDiagonalPositions(row, col)
  }
}

case object Rook extends ChessPiece('R') {
  override def attackPositions(row: Int, col: Int, board: ChessBoard): List[(Int, Int)] = {
    board.allRowPositions(row) ::: board.allColumnPositions(col)
  }
}

case object Bishop extends ChessPiece('B') {
  override def attackPositions(row: Int, col: Int, board: ChessBoard): List[(Int, Int)] = {
    board.allDiagonalPositions(row, col)
  }
}

case object Knight extends ChessPiece('N') {
  override def attackPositions(row: Int, col: Int, board: ChessBoard): List[(Int, Int)] = {
    List((row, col), (row - 2, col - 1), (row - 2, col + 1), (row + 2, col - 1), (row + 2, col + 1),
      (row - 1, col - 2), (row + 1, col - 2), (row - 1, col + 2), (row + 1, col + 2))
  }
}
