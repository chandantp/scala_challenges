package com.chandantp.challenges

import org.scalatest.FunSuite

class ChessServiceTest extends FunSuite {

  test("Passing rows='a' throws NumberFormatException") {
    val thrown = intercept[NumberFormatException] {
      MainApp.parse(Array("a", "5", "KKKQQ"))
    }
    assert(thrown.getMessage == "For input string: \"a\"")
  }

  test("Passing columns='a' throws NumberFormatException") {
    val thrown = intercept[NumberFormatException] {
      MainApp.parse(Array("5", "a", "KKKQQ"))
    }
    assert(thrown.getMessage == "For input string: \"a\"")
  }

  test("Passing printMode='-a' throws IllegalArgumentException") {
    val thrown = intercept[IllegalArgumentException] {
      MainApp.parse(Array("3", "3", "KKR", "-a"))
    }
    assert(thrown.getMessage == "Invalid print mode: Some(-a)")
  }

  test("Passing rows='0' throws IllegalArgumentException") {
    intercept[IllegalArgumentException] {
      val (rows, columns, pieces, _) = MainApp.parse(Array("0", "5", "KKKQQ"))
      new ChessService(rows, columns, pieces)
    }
  }

  test("Passing columns='-1' throws IllegalArgumentException") {
    intercept[IllegalArgumentException] {
      val (rows, columns, pieces, _) = MainApp.parse(Array("5", "-1", "KKKQQ"))
      new ChessService(rows, columns, pieces)
    }
  }

  test("Passing invalid pawns='AKK' throws IllegalArgumentException") {
    val thrown = intercept[IllegalArgumentException] {
      MainApp.parse(Array("3", "3", "AKK"))
    }
    assert(thrown.getMessage == "Unknown chess piece 'A'")
  }

  test("Passing valid rows=3, cols=3, printMode=None|-s|-v is successful") {
    MainApp.parse(Array("3", "3", "KKR"))
    MainApp.parse(Array("3", "3", "KKR", "-v"))
    MainApp.parse(Array("3", "3", "KKR", "-s"))
  }

  test("3x3 chessboard : KKR has 4 combinations") {
    val (rows, columns, pieces, _) = MainApp.parse(Array("3", "3", "KKR"))
    val chessSvc = new ChessService(rows, columns, pieces)
    val solutions = chessSvc.computeSolutions
    assert(solutions.size === 4)
    assert(solutions.map(_.toString).forall(
      x => Set(
        "0K:2K:7R", "0K:5R:6K", "2K:3R:8K", "1R:6K:8K")
        .contains(x)))
  }
  
  test("5x5 chessboard : QQQQQ has 10 combinations") {
    val (rows, columns, pieces, _) = MainApp.parse(Array("5", "5", "QQQQQ"))
    val chessSvc = new ChessService(rows, columns, pieces)
    val solutions = chessSvc.computeSolutions
    assert(solutions.size === 10)
    assert(solutions.map(_.toString).forall(
      x => Set(
        "0Q:7Q:14Q:16Q:23Q", "0Q:8Q:11Q:19Q:22Q", "1Q:8Q:10Q:17Q:24Q",
        "1Q:9Q:12Q:15Q:23Q", "2Q:5Q:13Q:16Q:24Q", "2Q:9Q:11Q:18Q:20Q",
        "3Q:5Q:12Q:19Q:21Q", "3Q:6Q:14Q:17Q:20Q", "4Q:6Q:13Q:15Q:22Q",
        "4Q:7Q:10Q:18Q:21Q")
        .contains(x)))
  }

  test("5x5 chessboard : KKQQR has 460 combinations") {
    val (rows, columns, pieces, _) = MainApp.parse(Array("5", "5", "KKQQR"))
    val chessSvc = new ChessService(rows, columns, pieces)
    assert(chessSvc.computeSolutions.size === 460)
  }

  test("7x7 chessboard : QQQQQQQ has 40 combinations") {
    val (rows, columns, pieces, _) = MainApp.parse(Array("7", "7", "QQQQQQQ"))
    val chessSvc = new ChessService(rows, columns, pieces)
    assert(chessSvc.computeSolutions.size === 40)
  }
  
  test("8x8 chessboard : QQQQQQQQ has 92 combinations") {
    val (rows, columns, pieces, _) = MainApp.parse(Array("8", "8", "QQQQQQQQ"))
    val chessSvc = new ChessService(rows, columns, pieces)
    assert(chessSvc.computeSolutions.size === 92)
  }
  
  test("6x6 chessboard : KKQBN has 4696 combinations") {
    val (rows, columns, pieces, _) = MainApp.parse(Array("5", "5", "KKQBN"))
    val chessSvc = new ChessService(rows, columns, pieces)
    assert(chessSvc.computeSolutions.size === 4696)
  }
  
}