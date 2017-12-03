package com.chandantp.challenges

import org.scalatest.FunSuite

import ChessBoard._

class ChessBoardTest extends FunSuite {

  test("chessBoard.encoded() returns empty list for empty chessboard") {
    val chessBoard = new ChessBoard(4, 4)
    assert(chessBoard.encoded == Nil)
  }

  test("placePawn(King,1,1) is successful and encoded() returns List('5K')") {
    val chessBoard = new ChessBoard(4, 4).placePawn(ChessBoard.King, 1, 1)
    assert(chessBoard.encoded == List("5K"))
  }

  test("placePawn(King,1,1) & placePawn(Bishop,1,3) is successful and encoded() returns List('5K','7B')") {
    val chessBoard = new ChessBoard(4, 4).placePawn(King, 1, 1).placePawn(Bishop, 1, 3)
    assert(chessBoard.encoded == List("5K", "7B"))
  }

  test("placePawn(Queen,10,10) for some invalid position fails with no effect") {
    val chessBoard = new ChessBoard(4, 4).placePawn(Queen, 10, 10)
    assert(chessBoard.encoded == Nil)
  }

  test("canPlacePawn() returns false when trying to place pawn over an occupied position") {
    val chessBoard = new ChessBoard(5, 5).placePawn(Queen, 2, 2)
    for(pawn <- List(King, Queen, Rook, Bishop, Knight)) {
      assert(!chessBoard.canPlacePawn(pawn, 2, 2))
    }
  }

  test("canPlacePawn(King) returns true for unsafe positions w.r.t the Queen") {
    val chessBoard = new ChessBoard(5, 5).placePawn(Queen, 2, 2)
    for(row <- 0 to 4 if row != 2) { // unsafe
      assert(!chessBoard.canPlacePawn(King, row, 2))
    }
    for(col <- 0 to 4 if col != 2) {
      assert(!chessBoard.canPlacePawn(King, 2, col)) // unsafe
    }
    for((r,c) <- List((0,0),(1,1),(3,3),(0,4),(1,3),(3,1),(4,0))) { // unsafe
      assert(!chessBoard.canPlacePawn(King, r, c))
    }
  }

  test("canPlacePawn(King) returns true for safe positions w.r.t the Queen") {
    val chessBoard = new ChessBoard(5, 5).placePawn(Queen, 2, 2)
    for((r,c) <- List((0,1),(0,3),(1,0),(1,4),(3,0),(3,4),(4,1),(4,3))) { // safe
      assert(chessBoard.canPlacePawn(King, r, c))
    }
  }

  test("canPlacePawn(King) returns true for unsafe positions w.r.t the King") {
    val chessBoard = new ChessBoard(4, 3).placePawn(King, 1, 1)
    for((r,c) <- List((0,0),(0,1),(0,2),(1,0),(1,2),(2,0),(2,1),(2,2))) { // unsafe
      assert(!chessBoard.canPlacePawn(King, r, c))
    }
  }

  test("canPlacePawn(King) returns true for safe positions w.r.t the King") {
    val chessBoard = new ChessBoard(4, 3).placePawn(King, 1, 1)
    for(col <- 0 to 2) {   // safe
      assert(chessBoard.canPlacePawn(King, 3, col))
    }
  }

  test("canPlacePawn(Rook) returns true for unsafe positions w.r.t the Rook") {
    val chessBoard = new ChessBoard(4, 3).placePawn(Rook, 1, 1)
    for((r,c) <- List((0,1),(2,1),(1,0),(1,2))) { // unsafe
      assert(!chessBoard.canPlacePawn(Rook, r, c))
    }
  }

  test("canPlacePawn(Rook) returns true for safe positions w.r.t the Rook") {
    val chessBoard = new ChessBoard(4, 3).placePawn(Rook, 1, 1)
    for((r,c) <- List((0,0),(0,2),(2,0),(2,2))) { // safe
      assert(chessBoard.canPlacePawn(Rook, r, c))
    }
  }

  test("canPlacePawn(Rook) returns true for unsafe positions w.r.t the Bishop") {
    val chessBoard = new ChessBoard(3, 3).placePawn(Bishop, 0, 0)
    for((r,c) <- List((1,1),(2,2),(0,1),(0,2),(1,0),(2,0))) { // unsafe
      assert(!chessBoard.canPlacePawn(Rook, r, c))
    }
  }

  test("canPlacePawn(Rook) returns true for safe positions w.r.t the Bishop") {
    val chessBoard = new ChessBoard(3, 3).placePawn(Bishop, 0, 0)
    for((r,c) <- List((1,2),(2,1))) { // safe
      assert(chessBoard.canPlacePawn(Rook, r, c))
    }
  }

  test("canPlacePawn(Bishop) returns true for unsafe positions w.r.t the Knight") {
    val chessBoard = new ChessBoard(3, 3).placePawn(Knight, 0, 0)
    for((r,c) <- List((1,1),(2,2),(2,1),(1,2))) { // unsafe
      assert(!chessBoard.canPlacePawn(Bishop, r, c))
    }
  }

  test("canPlacePawn(Bishop) returns true for safe positions w.r.t the Knight") {
    val chessBoard = new ChessBoard(3, 3).placePawn(Knight, 0, 0)
    for((r,c) <- List((0,1),(0,2),(1,0),(2,0))) { // safe
      assert(chessBoard.canPlacePawn(Bishop, r, c))
    }
  }
}