package com.chandantp.challenges

object ChessBoard {
  val King = 'K'
  val Queen = 'Q'
  val Rook = 'R'
  val Bishop = 'B'
  val Knight = 'N'

  val Empty = ' '
  val Empty_But_Unsafe = 'x'
}

class ChessBoard(rows: Int, columns: Int, board: String) {

  import ChessBoard._
  private val BoardSize = rows * columns

  if (board.size != BoardSize) {
    throw new IllegalArgumentException("Invalid board size = %d, should be %d !!".format(board.size, BoardSize))
  }

  def this(rows: Int, cols: Int) {
    this(rows, cols, ChessBoard.Empty.toString * rows * cols)
  }

  private def position(row: Int, col: Int) = row * columns + col

  private def isValid(row: Int, col: Int) = {
    row >= 0 && row < rows && col >= 0 && col < columns
  }

  private def isEmpty(ch: Char): Boolean = (ch == Empty_But_Unsafe || ch == Empty)

  private def isEmpty(board: String, position: Int): Boolean = isEmpty(board(position))

  private def isOccupied(row: Int, col: Int) = {
    val pos = position(row, col)
    isValid(row, col) && board(pos) != Empty && board(pos) != Empty_But_Unsafe
  }

  private def isRowOccupied(row: Int) = {
    def _isRowOccupied(c: Int): Boolean = {
      if (c == columns) false else (if (isOccupied(row, c)) true else _isRowOccupied(c+1))
    }
    _isRowOccupied(0)
  }

  private def isColumnOccupied(col: Int) = {
    def _isColumnOccupied(r: Int): Boolean = {
      if (r == rows) false else (if (isOccupied(r, col)) true else _isColumnOccupied(r+1))
    }
    _isColumnOccupied(0)
  }

  private def areDiagonalsOccupied(row: Int, col: Int) = {
    def isDiagonalOccupied(rdelta: Int, cdelta: Int): Boolean = {
      def _isDiagonalOccupied(r: Int, c: Int): Boolean = {
        if (!isValid(r, c)) false
        else if (isOccupied(r, c)) true else _isDiagonalOccupied(r+rdelta, c+cdelta)
      }
      _isDiagonalOccupied(row, col)
    }
    (isDiagonalOccupied(-1, -1) || isDiagonalOccupied(-1, 1) ||
      isDiagonalOccupied(1, 1) || isDiagonalOccupied(1, -1))
  }

  /*
   * Check if pawn can be safely placed without threatening any
   * existing pawns already present on the chess board
   */
  def canPlacePawn(pawn: Char, row: Int, col: Int): Boolean = pawn match {
    case King   => {
      board(position(row, col)) == Empty &&
        !(isOccupied(row - 1, col - 1) || isOccupied(row - 1, col) ||
          isOccupied(row - 1, col + 1) || isOccupied(row, col + 1) ||
          isOccupied(row + 1, col + 1) || isOccupied(row + 1, col) ||
          isOccupied(row + 1, col - 1) || isOccupied(row, col - 1))
    }
    case Queen  => {
      board(position(row, col)) == Empty &&
        !(isRowOccupied(row) || isColumnOccupied(col) || areDiagonalsOccupied(row, col))
    }
    case Rook   => {
      board(position(row, col)) == Empty &&
        !(isRowOccupied(row) || isColumnOccupied(col))

    }
    case Bishop => {
      board(position(row, col)) == Empty &&
        !(areDiagonalsOccupied(row, col))
    }
    case Knight => {
      board(position(row, col)) == Empty &&
        !(isOccupied(row - 2, col - 1) || isOccupied(row - 2, col + 1) ||
          isOccupied(row + 2, col - 1) || isOccupied(row + 2, col + 1) ||
          isOccupied(row - 1, col - 2) || isOccupied(row + 1, col - 2) ||
          isOccupied(row - 1, col + 2) || isOccupied(row + 1, col + 2))
    }
    case _ => {
      throw new IllegalArgumentException("Unknown pawn '%c'".format(pawn))
    }
  }

  /*
   * Place pawn at the specified location on the chess board
   * and mark locations that can be attacked by the pawn as unsafe
   */
  def placePawn(pawn: Char, row: Int, col: Int): ChessBoard = {

    val buf = new StringBuilder(board)

    def markUnsafe(row: Int, col: Int) {
      val pos = position(row, col)
      if (isValid(row, col)) buf(pos) = Empty_But_Unsafe
    }

    def markRowUnsafe(row: Int) = for (c <- 0 until columns) markUnsafe(row, c)

    def markColumnUnsafe(col: Int) = for (r <- 0 until rows) markUnsafe(r, col)

    def markDiagonalsUnsafe(row: Int, col: Int) {
      def markDiagonalUnsafe(r: Int, c: Int, rdelta: Int, cdelta: Int) {
        if (isValid(r + rdelta, c + cdelta)) {
          markUnsafe(r + rdelta, c + cdelta)
          markDiagonalUnsafe(r + rdelta, c + cdelta, rdelta, cdelta)
        }
      }
      markUnsafe(row, col)
      markDiagonalUnsafe(row, col, -1, -1) // Top-Left
      markDiagonalUnsafe(row, col, -1, 1) // Top-Right
      markDiagonalUnsafe(row, col, 1, 1) // Bottom-Right
      markDiagonalUnsafe(row, col, 1, -1) // Bottom-Left
    }

    if (isValid(row, col)) pawn match {
      case King   => {
        markUnsafe(row - 1, col - 1); // Top-Left
        markUnsafe(row - 1, col); // Top
        markUnsafe(row - 1, col + 1); // Top-Right
        markUnsafe(row, col + 1); // Right
        markUnsafe(row + 1, col + 1); // Bottom-Right
        markUnsafe(row + 1, col); // Bottom
        markUnsafe(row + 1, col - 1); // Bottom-Left
        markUnsafe(row, col - 1); // Left
        buf(position(row, col)) = King
      }
      case Queen  => {
        markRowUnsafe(row)
        markColumnUnsafe(col)
        markDiagonalsUnsafe(row, col)
        buf(position(row, col)) = Queen
      }
      case Rook   => {
        markRowUnsafe(row)
        markColumnUnsafe(col)
        buf(position(row, col)) = Rook
      }
      case Bishop => {
        markDiagonalsUnsafe(row, col)
        buf(position(row, col)) = Bishop
      }
      case Knight => {
        markUnsafe(row - 2, col - 1) // Top-Left
        markUnsafe(row - 2, col + 1) // Top-Right
        markUnsafe(row + 2, col - 1) // Bottom-Left
        markUnsafe(row + 2, col + 1) // Bottom-Right
        markUnsafe(row - 1, col - 2) // Left-Top
        markUnsafe(row + 1, col - 2) // Left-Bottom
        markUnsafe(row - 1, col + 2) // Right-Top
        markUnsafe(row + 1, col + 2) // Right-Bottom
        buf(position(row, col)) = Knight
      }
      case _ => {
        throw new IllegalArgumentException("Unknown pawn '%c'".format(pawn))
      }
    }

    // return new chess board after placing the pawn
    new ChessBoard(rows, columns, buf.toString)

  }

  // return chess board as list of "position:pawn" pairs
  def encoded: List[String] = {
    board.zipWithIndex.filter{case (c,_) => !isEmpty(c)}.map{case (pawn,i) => i+""+pawn}.toList
  }

  override def toString: String = encoded.mkString(":")

  def prettyPrint: Unit = {
    for (row <- 0 until rows; col <- 0 until columns) {
      val pos = row * columns + col
      val pawn = if (isEmpty(board, pos)) Empty else board(pos)
      print("|" + pawn + (if (col < columns - 1) "" else "|\n"))
    }
  }
}
