package com.chandantp.challenges

object ChessBoard {
  val King = 'K'
  val Queen = 'Q'
  val Rook = 'R'
  val Bishop = 'B'
  val Knight = 'N'

  def create(rows: Int, columns: Int) = {
    val emptyAndSafePositions =
      for (row <- 0 until rows; col <- 0 until columns) yield (row, col)

    new ChessBoard(rows, columns, Map.empty, emptyAndSafePositions.toSet)
  }
}

class ChessBoard private (rows: Int,
                          columns: Int,
                          pieces: Map[(Int, Int), Char],
                          emptyAndSafePositions: Set[(Int, Int)]) {

  import ChessBoard._

  private def isValid(row: Int, col: Int) = {
    row >= 0 && row < rows && col >= 0 && col < columns
  }

  /*
   * Check if piece can be safely placed without threatening any
   * existing pawns already present on the chess board
   */
  def isSafeToPlace(piece: Char, row: Int, col: Int): Boolean = {

    def isOccupied(row: Int, col: Int) = {
      isValid(row, col) && pieces.contains((row, col))
    }

    def isRowOccupied(row: Int) = {
      def _isRowOccupied(c: Int): Boolean = {
        if (c == columns) false else if (isOccupied(row, c)) true else _isRowOccupied(c+1)
      }
      _isRowOccupied(0)
    }

    def isColumnOccupied(col: Int) = {
      def _isColumnOccupied(r: Int): Boolean = {
        if (r == rows) false else if (isOccupied(r, col)) true else _isColumnOccupied(r+1)
      }
      _isColumnOccupied(0)
    }

    def areDiagonalsOccupied(row: Int, col: Int) = {
      def isDiagonalOccupied(rdelta: Int, cdelta: Int): Boolean = {
        def _isDiagonalOccupied(r: Int, c: Int): Boolean = {
          if (!isValid(r, c)) false
          else if (isOccupied(r, c)) true else _isDiagonalOccupied(r+rdelta, c+cdelta)
        }
        _isDiagonalOccupied(row, col)
      }

      isDiagonalOccupied(-1, -1) || isDiagonalOccupied(-1, 1) ||
        isDiagonalOccupied(1, 1) || isDiagonalOccupied(1, -1)
    }

    piece match {
      case King   => emptyAndSafePositions.contains(row, col) &&
        !(isOccupied(row - 1, col - 1) || isOccupied(row - 1, col) ||
          isOccupied(row - 1, col + 1) || isOccupied(row, col + 1) ||
          isOccupied(row + 1, col + 1) || isOccupied(row + 1, col) ||
          isOccupied(row + 1, col - 1) || isOccupied(row, col - 1))

      case Queen  => emptyAndSafePositions.contains(row, col) &&
        !(isRowOccupied(row) || isColumnOccupied(col) || areDiagonalsOccupied(row, col))

      case Rook   => emptyAndSafePositions.contains(row, col) &&
        !(isRowOccupied(row) || isColumnOccupied(col))

      case Bishop => emptyAndSafePositions.contains(row, col) &&
        !(areDiagonalsOccupied(row, col))

      case Knight => emptyAndSafePositions.contains(row, col) &&
        !(isOccupied(row - 2, col - 1) || isOccupied(row - 2, col + 1) ||
          isOccupied(row + 2, col - 1) || isOccupied(row + 2, col + 1) ||
          isOccupied(row - 1, col - 2) || isOccupied(row + 1, col - 2) ||
          isOccupied(row - 1, col + 2) || isOccupied(row + 1, col + 2))

      case _ => {
        throw new IllegalArgumentException("Unknown chess piece '%c'".format(piece))
      }
    }
  }

  /*
   * Place piece at the specified location on the chess board
   * and mark locations that can be attacked by the piece as unsafe
   */
  def place(piece: Char, row: Int, col: Int): ChessBoard = {

    var unsafePositions = collection.mutable.Buffer.empty[(Int,Int)]

    def unsafeRowPositions(row: Int) = for (c <- 0 until columns) unsafePositions.append((row, c))

    def unsafeColumnPositions(col: Int) = for (r <- 0 until rows) unsafePositions.append((r, col))

    def unsafeDiagonalsPositions(row: Int, col: Int): Unit = {
      def unsafeDiagonalPositions(r: Int, c: Int, rdelta: Int, cdelta: Int): Unit = {
        if (!isValid(r + rdelta, c + cdelta)) Set.empty[(Int, Int)] else {
          unsafePositions.append((r + rdelta, c + cdelta))
          unsafeDiagonalPositions(r + rdelta, c + cdelta, rdelta, cdelta)
        }
      }
      unsafeDiagonalPositions(row, col, -1, -1) // Top-Left
      unsafeDiagonalPositions(row, col, -1, 1)  // Top-Right
      unsafeDiagonalPositions(row, col, 1, 1)   // Bottom-Right
      unsafeDiagonalPositions(row, col, 1, -1)  // Bottom-Left
    }

    if (!isValid(row, col)) {
      throw new IllegalArgumentException("Invalid location (row, column) = (%d, %d) !!".format(row, col))
    }

    piece match {
      case King   => {
        unsafePositions.append((row, col))
        unsafePositions.append((row - 1, col - 1)) // Top-Left
        unsafePositions.append((row - 1, col))     // Top
        unsafePositions.append((row - 1, col + 1)) // Top-Right
        unsafePositions.append((row, col + 1))     // Right
        unsafePositions.append((row + 1, col + 1)) // Bottom-Right
        unsafePositions.append((row + 1, col))     // Bottom
        unsafePositions.append((row + 1, col - 1)) // Bottom-Left
        unsafePositions.append((row, col - 1))     // Left
        new ChessBoard(rows, columns, pieces + ((row, col) -> King), emptyAndSafePositions -- unsafePositions)
      }

      case Queen  => {
        unsafePositions.append((row, col))
        unsafeRowPositions(row)
        unsafeColumnPositions(col)
        unsafeDiagonalsPositions(row, col)
        new ChessBoard(rows, columns, pieces + ((row, col) -> Queen), emptyAndSafePositions -- unsafePositions)
      }

      case Rook   => {
        unsafePositions.append((row, col))
        unsafeRowPositions(row)
        unsafeColumnPositions(col)
        new ChessBoard(rows, columns, pieces + ((row, col) -> Rook), emptyAndSafePositions -- unsafePositions)
      }

      case Bishop => {
        unsafePositions.append((row, col))
        unsafeDiagonalsPositions(row, col)
        new ChessBoard(rows, columns, pieces + ((row, col) -> Bishop), emptyAndSafePositions -- unsafePositions)
      }

      case Knight => {
        unsafePositions.append((row, col))
        unsafePositions.append((row - 2, col - 1)) // Top-Left
        unsafePositions.append((row - 2, col + 1)) // Top-Right
        unsafePositions.append((row + 2, col - 1)) // Bottom-Left
        unsafePositions.append((row + 2, col + 1)) // Bottom-Right
        unsafePositions.append((row - 1, col - 2)) // Left-Top
        unsafePositions.append((row + 1, col - 2)) // Left-Bottom
        unsafePositions.append((row - 1, col + 2)) // Right-Top
        unsafePositions.append((row + 1, col + 2))  // Right-Bottom
        new ChessBoard(rows, columns, pieces + ((row, col) -> Knight), emptyAndSafePositions -- unsafePositions)
      }

      case _ => {
        throw new IllegalArgumentException("Unknown chess piece '%c'".format(piece))
      }
    }
  }

  // return chess board as list of "PositionPiece" stringx
  def encoded: List[String] = pieces.toList.sorted.map{ case((x,y),piece) => x * rows + y + "" + piece }

  override def toString: String = encoded.mkString(":")

  def toPrettyPrintString: String = {
    val buf = new StringBuffer
    for (row <- 0 until rows; col <- 0 until columns) {
      buf.append("|")
      buf.append(pieces.getOrElse((row, col), " "))
      buf.append(if (col < columns - 1) "" else "|\n")
    }
    buf.toString
  }
}
