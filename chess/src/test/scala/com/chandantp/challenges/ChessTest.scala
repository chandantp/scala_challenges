package com.chandantp.challenges

import org.scalatest.FunSuite

class ChessTest extends FunSuite {

  test("3x3 chessboard : KKR has 4 combinations") {
    val chessSvc = new ChessService(3, 3, "KKR")
    assert(chessSvc.computeSolutions.size === 4)
  }
  
  test("5x5 chessboard : QQQQQ has 10 combinations") {
    val chessSvc = new ChessService(5, 5, "QQQQQ")
    assert(chessSvc.computeSolutions.size === 10)
  }

  test("5x5 chessboard : KKQQR has 460 combinations") {
    val chessSvc = new ChessService(5, 5, "KKQQR")
    assert(chessSvc.computeSolutions.size === 460)
  }

  test("7x7 chessboard : QQQQQQQ has 40 combinations") {
    val chessSvc = new ChessService(7, 7, "QQQQQQQ")
    assert(chessSvc.computeSolutions.size === 40)
  }
  
  test("8x8 chessboard : QQQQQQQQ has 92 combinations") {
    val chessSvc = new ChessService(8, 8, "QQQQQQQQ")
    assert(chessSvc.computeSolutions.size === 92)
  }
  
  test("6x6 chessboard : KKQBN has 4696 combinations") {
    val chessSvc = new ChessService(5, 5, "KKQBN")
    assert(chessSvc.computeSolutions.size === 4696)
  }
  
}