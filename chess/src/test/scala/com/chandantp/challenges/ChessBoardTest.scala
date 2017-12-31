package com.chandantp.challenges

import org.scalatest.FunSuite

import ChessBoard._

class ChessBoardTest extends FunSuite {

  test("chessBoard.encoded() returns empty list for empty chessboard") {
    val chessBoard = ChessBoard.create(4, 4)
    assert(chessBoard.encoded == Nil)
  }

  test("place(King,1,1) is successful and encoded() returns List('5K')") {
    val chessBoard = ChessBoard.create(4, 4).place(ChessBoard.King, 1, 1)
    assert(chessBoard.encoded == List("5K"))
  }

  test("place(King,1,1) & place(Bishop,1,3) is successful and encoded() returns List('5K','7B')") {
    val chessBoard = ChessBoard.create(4, 4).place(King, 1, 1).place(Bishop, 1, 3)
    assert(chessBoard.encoded == List("5K", "7B"))
  }

  test("place(Queen,10,10) for some invalid position throws IllegalArgumentExcetion") {
    intercept[IllegalArgumentException] {
      ChessBoard.create(4, 4).place(Queen, 10, 10)
    }
  }

  test("isSafeToPlace() returns false when trying to place piece over an occupied position") {
    val chessBoard = ChessBoard.create(5, 5).place(Queen, 2, 2)
    for(piece <- List(King, Queen, Rook, Bishop, Knight)) {
      assert(!chessBoard.isSafeToPlace(piece, 2, 2))
    }
  }

  test("isSafeToPlace(King) returns true for unsafe positions w.r.t the Queen") {
    val chessBoard = ChessBoard.create(5, 5).place(Queen, 2, 2)
    for(row <- 0 to 4 if row != 2) { // unsafe
      assert(!chessBoard.isSafeToPlace(King, row, 2))
    }
    for(col <- 0 to 4 if col != 2) {
      assert(!chessBoard.isSafeToPlace(King, 2, col)) // unsafe
    }
    for((r,c) <- List((0,0),(1,1),(3,3),(0,4),(1,3),(3,1),(4,0))) { // unsafe
      assert(!chessBoard.isSafeToPlace(King, r, c))
    }
  }

  test("isSafeToPlace(King) returns true for safe positions w.r.t the Queen") {
    val chessBoard = ChessBoard.create(5, 5).place(Queen, 2, 2)
    for((r,c) <- List((0,1),(0,3),(1,0),(1,4),(3,0),(3,4),(4,1),(4,3))) { // safe
      assert(chessBoard.isSafeToPlace(King, r, c))
    }
  }

  test("isSafeToPlace(King) returns true for unsafe positions w.r.t the King") {
    val chessBoard = ChessBoard.create(4, 3).place(King, 1, 1)
    for((r,c) <- List((0,0),(0,1),(0,2),(1,0),(1,2),(2,0),(2,1),(2,2))) { // unsafe
      assert(!chessBoard.isSafeToPlace(King, r, c))
    }
  }

  test("isSafeToPlace(King) returns true for safe positions w.r.t the King") {
    val chessBoard = ChessBoard.create(4, 3).place(King, 1, 1)
    for(col <- 0 to 2) {   // safe
      assert(chessBoard.isSafeToPlace(King, 3, col))
    }
  }

  test("isSafeToPlace(Rook) returns true for unsafe positions w.r.t the Rook") {
    val chessBoard = ChessBoard.create(4, 3).place(Rook, 1, 1)
    for((r,c) <- List((0,1),(2,1),(1,0),(1,2))) { // unsafe
      assert(!chessBoard.isSafeToPlace(Rook, r, c))
    }
  }

  test("isSafeToPlace(Rook) returns true for safe positions w.r.t the Rook") {
    val chessBoard = ChessBoard.create(4, 3).place(Rook, 1, 1)
    for((r,c) <- List((0,0),(0,2),(2,0),(2,2))) { // safe
      assert(chessBoard.isSafeToPlace(Rook, r, c))
    }
  }

  test("isSafeToPlace(Rook) returns true for unsafe positions w.r.t the Bishop") {
    val chessBoard = ChessBoard.create(3, 3).place(Bishop, 0, 0)
    for((r,c) <- List((1,1),(2,2),(0,1),(0,2),(1,0),(2,0))) { // unsafe
      assert(!chessBoard.isSafeToPlace(Rook, r, c))
    }
  }

  test("isSafeToPlace(Rook) returns true for safe positions w.r.t the Bishop") {
    val chessBoard = ChessBoard.create(3, 3).place(Bishop, 0, 0)
    for((r,c) <- List((1,2),(2,1))) { // safe
      assert(chessBoard.isSafeToPlace(Rook, r, c))
    }
  }

  test("isSafeToPlace(Bishop) returns true for unsafe positions w.r.t the Knight") {
    val chessBoard = ChessBoard.create(3, 3).place(Knight, 0, 0)
    for((r,c) <- List((1,1),(2,2),(2,1),(1,2))) { // unsafe
      assert(!chessBoard.isSafeToPlace(Bishop, r, c))
    }
  }

  test("isSafeToPlace(Bishop) returns true for safe positions w.r.t the Knight") {
    val chessBoard = ChessBoard.create(3, 3).place(Knight, 0, 0)
    for((r,c) <- List((0,1),(0,2),(1,0),(2,0))) { // safe
      assert(chessBoard.isSafeToPlace(Bishop, r, c))
    }
  }
}