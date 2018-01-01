package com.chandantp.challenges

object ChessBoard {
  def create(rows: Int, columns: Int) = {
    val positions = for (row <- 0 until rows; col <- 0 until columns) yield (row, col)
    new ChessBoard(rows, columns, Map.empty, positions.toSet, Map.empty)
  }
}

case class ChessBoard private (rows: Int,
                               columns: Int,
                               position2chessPiece: Map[(Int, Int), ChessPiece],
                               unoccupiedSafePositions: Set[(Int, Int)],
                               piece2MaxLinearPosition: Map[ChessPiece, Int]) {
  //
  // Check if piece can be safely placed without threatening any
  // existing pawns already present on the chess board
  //
  def isSafeToPlace(piece: ChessPiece, row: Int, col: Int): Boolean = {

    def isOccupied(position: (Int, Int)) = {
      val (row, col) = (position._1, position._2)
      isValid(row, col) && position2chessPiece.contains((row, col))
    }

    piece match {
      case King => unoccupiedSafePositions.contains(row, col) &&
        King.attackPositions(row, col, this).forall(!isOccupied(_))

      case Queen => unoccupiedSafePositions.contains(row, col) &&
        Queen.attackPositions(row, col, this).forall(!isOccupied(_))

      case Rook => unoccupiedSafePositions.contains(row, col) &&
        Rook.attackPositions(row, col, this).forall(!isOccupied(_))

      case Bishop => unoccupiedSafePositions.contains(row, col) &&
        Bishop.attackPositions(row, col, this).forall(!isOccupied(_))

      case Knight => unoccupiedSafePositions.contains(row, col) &&
        Knight.attackPositions(row, col, this).forall(!isOccupied(_))

      case _ => throw new IllegalArgumentException("Unknown chess piece '%s'".format(piece))
    }
  }

  //
  // Place piece at the specified location on the chess board
  // and mark locations that can be attacked by the piece as unsafe
  //
  def place(piece: ChessPiece, row: Int, col: Int): ChessBoard = {

    def updatedMaxLinearPosition(piece: ChessPiece, row: Int, col: Int): Int = {
      val maxPos = piece2MaxLinearPosition.getOrElse(piece, -1)
      val currPos = row * rows + col
      if (currPos > maxPos) currPos else maxPos
    }

    if (!isValid(row, col)) {
      throw new IllegalArgumentException("Invalid location (row, column) = (%d, %d) !!".format(row, col))
    }

    piece match {
      case King => this.copy(
        position2chessPiece = position2chessPiece + ((row, col) -> King),
        unoccupiedSafePositions = unoccupiedSafePositions -- King.attackPositions(row, col, this),
        piece2MaxLinearPosition = piece2MaxLinearPosition + (King -> updatedMaxLinearPosition(King, row, col))
      )

      case Queen => this.copy(
        position2chessPiece = position2chessPiece + ((row, col) -> Queen),
        unoccupiedSafePositions = unoccupiedSafePositions -- Queen.attackPositions(row, col, this),
        piece2MaxLinearPosition = piece2MaxLinearPosition + (Queen -> updatedMaxLinearPosition(Queen, row, col))
      )

      case Rook => this.copy(
        position2chessPiece = position2chessPiece + ((row, col) -> Rook),
        unoccupiedSafePositions = unoccupiedSafePositions -- Rook.attackPositions(row, col, this),
        piece2MaxLinearPosition = piece2MaxLinearPosition + (Rook -> updatedMaxLinearPosition(Rook, row, col))
      )

      case Bishop => this.copy(
        position2chessPiece = position2chessPiece + ((row, col) -> Bishop),
        unoccupiedSafePositions = unoccupiedSafePositions -- Bishop.attackPositions(row, col, this),
        piece2MaxLinearPosition = piece2MaxLinearPosition + (Bishop -> updatedMaxLinearPosition(Bishop, row, col))
      )

      case Knight => this.copy(
        position2chessPiece = position2chessPiece + ((row, col) -> Knight),
        unoccupiedSafePositions = unoccupiedSafePositions -- Knight.attackPositions(row, col, this),
        piece2MaxLinearPosition = piece2MaxLinearPosition + (Knight -> updatedMaxLinearPosition(Knight, row, col))
      )

      case _ => throw new IllegalArgumentException("Unknown chess piece '%s'".format(piece))
    }
  }

  override def toString: String = {
    position2chessPiece.toList.sortBy(_._1).map{ case((x,y),piece) => x * rows + y + "" + piece }.mkString(":")
  }

  def toPrettyPrintString: String = {
    val buf = new StringBuffer
    for (row <- 0 until rows; col <- 0 until columns) {
      buf.append("|")
      buf.append(position2chessPiece.getOrElse((row, col), " "))
      buf.append(if (col < columns - 1) "" else "|\n")
    }
    buf.toString
  }

  def isValid(row: Int, col: Int): Boolean = row >= 0 && row < rows && col >= 0 && col < columns

  def chessPiecesCount: Int = position2chessPiece.size

  def maxLinearPosition(piece: ChessPiece): Int = piece2MaxLinearPosition.getOrElse(piece, -1) + 1

  def allRowPositions(row: Int) = {
    val positions = for(c <- 0 until columns) yield (row, c)
    positions.toList
  }

  def allColumnPositions(col: Int) = {
    val positions = for(r <- 0 until rows) yield (r, col)
    positions.toList
  }

  def allDiagonalPositions(row: Int, col: Int) = {
    def recurse(r: Int, c: Int, rdelta: Int, cdelta: Int): List[(Int, Int)] = {
      if (!isValid(r, c)) Nil else {
        (r, c) :: recurse(r + rdelta, c + cdelta, rdelta, cdelta)
      }
    }
    recurse(row, col, -1, -1) :::  // Top-Left
      recurse(row, col, -1, 1) ::: // Top-Right
      recurse(row, col, 1, 1) :::  // Bottom-Right
      recurse(row, col, 1, -1)     // Bottom-Left
  }
}
