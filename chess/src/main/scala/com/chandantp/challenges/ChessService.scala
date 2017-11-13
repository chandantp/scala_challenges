package com.chandantp.challenges

import ChessService._

object ChessService {
  val KING = 'K'
  val QUEEN = 'Q'
  val ROOK = 'R'
  val BISHOP = 'B'
  val KNIGHT = 'N'
  val ROOT = "root"

  val EMPTY = ' '
  val EMPTY_BUT_UNSAFE = 'x'

  def isEmpty(board: String, position: Int):Boolean = isEmpty(board(position))

  def isEmpty(ch: Char):Boolean = (ch == EMPTY_BUT_UNSAFE || ch == EMPTY)

}

class ChessService(rows: Int, columns: Int, iPawns: String) {

  val BoardSize = rows * columns

  val BlankBoard = EMPTY.toString * BoardSize

  val pawns = iPawns.split("").toList.map(_(0))

  val solutionTree = Tree(ROOT, Map("dummy" -> Tree("dummy", Map())))

  def findAllNonThreateningCombinations = {
    //
    // Place chess pieces on the board in mutually non-threatening positions
    //
    def placePawn(pawn: Char, usedPawns: List[Char], iBoard: String): Unit = {

      val tmpBoard = new StringBuilder(iBoard)
      val usedPawnsUpdated = pawn :: usedPawns
      val remainingPawns = pawns.diff(usedPawnsUpdated).distinct

      for (row <- 0 until rows; col <- 0 until columns) {
        val position = calculatePosition(row, col)
        if (tmpBoard(position) == EMPTY && placeChessPawn(pawn, row, col, position)) {
          if (!solutionTree.isExplored(branch)) {
            remainingPawns.foreach(pawn => placePawn(pawn, usedPawnsUpdated, tmpBoard.toString))
            solutionTree.include(branch, tmpBoard.toString, pawns.size)
          }
          tmpBoard.replace(0, BoardSize, iBoard) // Restore board
        }
      }

      def branch = {
        tmpBoard.zipWithIndex.filter{case (c,_) => !isEmpty(c)}.map{case (pawn,i) => i+":"+pawn}.toList
      }

      def calculatePosition(row: Int, col: Int) = row * columns + col

      def isValidPosition(row: Int, col: Int) = row >= 0 && row < rows && col >= 0 && col < columns

      def isOccupied(row: Int, col: Int) = {
        val position = calculatePosition(row, col)
        isValidPosition(row, col) && tmpBoard(position) != EMPTY && tmpBoard(position) != EMPTY_BUT_UNSAFE
      }

      def markUnsafe(row: Int, col: Int) {
        val position = calculatePosition(row, col)
        if (isValidPosition(row, col)) tmpBoard(position) = EMPTY_BUT_UNSAFE
      }

      def isRowOccupied(row: Int) = {
        def _isRowOccupied(c: Int): Boolean = {
          if (c == columns) false else (if (isOccupied(row, c)) true else _isRowOccupied(c+1))
        }
        _isRowOccupied(0)
      }

      def isColumnOccupied(col: Int) = {
        def _isColumnOccupied(r: Int): Boolean = {
          if (r == rows) false else (if (isOccupied(r, col)) true else _isColumnOccupied(r+1))
        }
        _isColumnOccupied(0)
      }

      def areDiagonalsOccupied(row: Int, col: Int) = {
        def isDiagonalOccupied(rdelta: Int, cdelta: Int): Boolean = {
          def _isDiagonalOccupied(r: Int, c: Int): Boolean = {
            if (!isValidPosition(r, c)) false
            else if (isOccupied(r, c)) true else _isDiagonalOccupied(r+rdelta, c+cdelta)
          }
          _isDiagonalOccupied(row, col)
        }
        isDiagonalOccupied(-1, -1) || isDiagonalOccupied(-1, 1) || isDiagonalOccupied(1, 1) || isDiagonalOccupied(1, -1)
      }

      def markRowUnsafe(row: Int) = for (c <- 0 until columns) markUnsafe(row, c)

      def markColumnUnsafe(col: Int) = for (r <- 0 until rows) markUnsafe(r, col)

      def markDiagonalsUnsafe(row: Int, col: Int) {
        def markDiagonalUnsafe(r: Int, c: Int, rdelta: Int, cdelta: Int) {
          if (isValidPosition(r + rdelta, c + cdelta)) {
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

      def placeKing(row: Int, col: Int, position: Int): Boolean = {
        if (isOccupied(row - 1, col - 1) || isOccupied(row - 1, col) ||
          isOccupied(row - 1, col + 1) || isOccupied(row, col + 1) ||
          isOccupied(row + 1, col + 1) || isOccupied(row + 1, col) ||
          isOccupied(row + 1, col - 1) || isOccupied(row, col - 1)) {
          false
        } else {
          markUnsafe(row - 1, col - 1); // Top-Left
          markUnsafe(row - 1, col); // Top
          markUnsafe(row - 1, col + 1); // Top-Right
          markUnsafe(row, col + 1); // Right
          markUnsafe(row + 1, col + 1); // Bottom-Right
          markUnsafe(row + 1, col); // Bottom
          markUnsafe(row + 1, col - 1); // Bottom-Left
          markUnsafe(row, col - 1); // Left
          tmpBoard(position) = KING
          true
        }
      }

      def placeQueen(row: Int, col: Int, position: Int): Boolean = {
        if (isRowOccupied(row) || isColumnOccupied(col) || areDiagonalsOccupied(row, col)) false else {
          markRowUnsafe(row)
          markColumnUnsafe(col)
          markDiagonalsUnsafe(row, col)
          tmpBoard(position) = QUEEN
          true
        }
      }

      def placeRook(row: Int, col: Int, position: Int): Boolean = {
        if (isRowOccupied(row) || isColumnOccupied(col)) false else {
          markRowUnsafe(row)
          markColumnUnsafe(col)
          tmpBoard(position) = ROOK
          true
        }
      }

      def placeBishop(row: Int, col: Int, position: Int): Boolean = {
        if (areDiagonalsOccupied(row, col)) false else {
          markDiagonalsUnsafe(row, col)
          tmpBoard(position) = BISHOP
          true
        }
      }

      def placeKnight(row: Int, col: Int, position: Int): Boolean = {
        if (isOccupied(row - 2, col - 1) || isOccupied(row - 2, col + 1) ||
          isOccupied(row + 2, col - 1) || isOccupied(row + 2, col + 1) ||
          isOccupied(row - 1, col - 2) || isOccupied(row + 1, col - 2) ||
          isOccupied(row - 1, col + 2) || isOccupied(row + 1, col + 2)) {
          false
        } else {
          markUnsafe(row - 2, col - 1) // Top-Left
          markUnsafe(row - 2, col + 1) // Top-Right
          markUnsafe(row + 2, col - 1) // Bottom-Left
          markUnsafe(row + 2, col + 1) // Bottom-Right
          markUnsafe(row - 1, col - 2) // Left-Top
          markUnsafe(row + 1, col - 2) // Left-Bottom
          markUnsafe(row - 1, col + 2) // Right-Top
          markUnsafe(row + 1, col + 2) // Right-Bottom
          tmpBoard(position) = KNIGHT
          true
        }
      }

      def placeChessPawn(pawn: Char, row: Int, col: Int, position: Int): Boolean = pawn match {
        case KING   => placeKing(row, col, position)
        case QUEEN  => placeQueen(row, col, position)
        case ROOK   => placeRook(row, col, position)
        case BISHOP => placeBishop(row, col, position)
        case KNIGHT => placeKnight(row, col, position)
        case _ => throw new IllegalArgumentException("Unknown pawn '%c'".format(pawn))
      }
    }

    pawns.distinct.foreach(piece => placePawn(piece, Nil, BlankBoard))
    solutionTree.solutions
  }

  def displaySolution(solution: String) {
    for (row <- 0 until rows) {
      for (col <- 0 until columns) {
        val i = row * columns + col
        val piece = if (isEmpty(solution, i)) EMPTY else solution(i)
        print("|" + piece + (if (col < columns - 1) "" else "|"))
      }
      println
    }
    println
  }

}