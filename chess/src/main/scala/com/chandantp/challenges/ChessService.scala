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

class ChessService(rows: Int, columns: Int, pieces: String) {

  private val BoardSize = rows * columns

  private val EmptyBoard = EMPTY.toString * BoardSize

  private val pawns = pieces.split("").toList.map(_(0))

  private val solutionTree = Tree(ROOT, Map("dummy" -> Tree("dummy", Map())))

  val solutions = collection.mutable.ListBuffer[String]()

  private def computePosition(row: Int, col: Int): Int = row * columns + col

  private def isValidPosition(row: Int, col: Int): Boolean = {
    row >= 0 && row < rows && col >= 0 && col < columns
  }

  /*
   * Find all possible combinations of non-threatening
   * positions the pawns can be placed on the chess board
   */
  def findSolutions: List[String] = {

    def placePawn(pawn: Char, usedPawns: List[Char], board: String): Unit = {
      val tmpBoard = new StringBuilder(board)
      val usedPawnsUpdated = pawn :: usedPawns
      val remainingPawns = pawns.diff(usedPawnsUpdated).distinct

      for (row <- 0 until rows; col <- 0 until columns) {
        if (canPlacePawn(pawn, row, col)) {
          placeChessPawn(pawn, row, col)
          if (!solutionTree.isExplored(branch)) {
            remainingPawns.foreach(pawn => placePawn(pawn, usedPawnsUpdated, tmpBoard.toString))
            solutionTree.add(branch)
            if (branch.size == pawns.size) {
              solutions.append(tmpBoard.toString)
            }
          }
          tmpBoard.replace(0, BoardSize, board) // Restore board
        }
      }

      def branch = {
        tmpBoard.zipWithIndex.filter{case (c,_) => !isEmpty(c)}.map{case (pawn,i) => i+":"+pawn}.toList
      }

      def canPlacePawn(pawn: Char, row: Int, col: Int): Boolean = {

        def isOccupied(row: Int, col: Int) = {
          val position = computePosition(row, col)
          isValidPosition(row, col) && tmpBoard(position) != EMPTY && tmpBoard(position) != EMPTY_BUT_UNSAFE
        }

        def isRowOccupied(row: Int): Boolean = {
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
          (isDiagonalOccupied(-1, -1) || isDiagonalOccupied(-1, 1) ||
            isDiagonalOccupied(1, 1) || isDiagonalOccupied(1, -1))
        }

        pawn match {
          case KING   => {
            tmpBoard(computePosition(row, col)) == EMPTY &&
              !(isOccupied(row - 1, col - 1) || isOccupied(row - 1, col) ||
                isOccupied(row - 1, col + 1) || isOccupied(row, col + 1) ||
                isOccupied(row + 1, col + 1) || isOccupied(row + 1, col) ||
                isOccupied(row + 1, col - 1) || isOccupied(row, col - 1))
          }
          case QUEEN  => {
            tmpBoard(computePosition(row, col)) == EMPTY &&
              !(isRowOccupied(row) || isColumnOccupied(col) || areDiagonalsOccupied(row, col))
          }
          case ROOK   => {
            tmpBoard(computePosition(row, col)) == EMPTY &&
              !(isRowOccupied(row) || isColumnOccupied(col))

          }
          case BISHOP => {
            tmpBoard(computePosition(row, col)) == EMPTY &&
              !(areDiagonalsOccupied(row, col))
          }
          case KNIGHT => {
            tmpBoard(computePosition(row, col)) == EMPTY &&
              !(isOccupied(row - 2, col - 1) || isOccupied(row - 2, col + 1) ||
                isOccupied(row + 2, col - 1) || isOccupied(row + 2, col + 1) ||
                isOccupied(row - 1, col - 2) || isOccupied(row + 1, col - 2) ||
                isOccupied(row - 1, col + 2) || isOccupied(row + 1, col + 2))
          }
          case _ => {
            throw new IllegalArgumentException("Unknown pawn '%c'".format(pawn))
          }
        }
      }

      def placeChessPawn(pawn: Char, row: Int, col: Int): Unit = {
        val position = computePosition(row, col)
        pawn match {
          case KING   => {
            markUnsafe(row - 1, col - 1); // Top-Left
            markUnsafe(row - 1, col); // Top
            markUnsafe(row - 1, col + 1); // Top-Right
            markUnsafe(row, col + 1); // Right
            markUnsafe(row + 1, col + 1); // Bottom-Right
            markUnsafe(row + 1, col); // Bottom
            markUnsafe(row + 1, col - 1); // Bottom-Left
            markUnsafe(row, col - 1); // Left
            tmpBoard(position) = KING
          }
          case QUEEN  => {
            markRowUnsafe(row)
            markColumnUnsafe(col)
            markDiagonalsUnsafe(row, col)
            tmpBoard(position) = QUEEN
          }
          case ROOK   => {
            markRowUnsafe(row)
            markColumnUnsafe(col)
            tmpBoard(position) = ROOK
          }
          case BISHOP => {
            markDiagonalsUnsafe(row, col)
            tmpBoard(position) = BISHOP
          }
          case KNIGHT => {
            markUnsafe(row - 2, col - 1) // Top-Left
            markUnsafe(row - 2, col + 1) // Top-Right
            markUnsafe(row + 2, col - 1) // Bottom-Left
            markUnsafe(row + 2, col + 1) // Bottom-Right
            markUnsafe(row - 1, col - 2) // Left-Top
            markUnsafe(row + 1, col - 2) // Left-Bottom
            markUnsafe(row - 1, col + 2) // Right-Top
            markUnsafe(row + 1, col + 2) // Right-Bottom
            tmpBoard(position) = KNIGHT
          }
          case _ => {
            throw new IllegalArgumentException("Unknown pawn '%c'".format(pawn))
          }
        }

        def markUnsafe(row: Int, col: Int) {
          val position = computePosition(row, col)
          if (isValidPosition(row, col)) tmpBoard(position) = EMPTY_BUT_UNSAFE
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
      }

    }

    pawns.distinct.foreach(pawn => placePawn(pawn, Nil, EmptyBoard))
    solutions.toList
  }

  def prettyPrintSolutions: Unit = {
    solutions.zipWithIndex.foreach {
      case (solution, i) => {
        println("Solution %d:".format(i+1))
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
  }

}