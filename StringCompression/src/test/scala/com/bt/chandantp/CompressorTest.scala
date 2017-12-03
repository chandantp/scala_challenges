package com.bt.chandantp

import org.scalatest.FunSuite

import Compressor.compressStr

class CompressorTest extends FunSuite {

  test("compressStr(null, 1) throws IllegalArgumentException") {
    val thrown = intercept[IllegalArgumentException] {
      compressStr(null, 1)
    }
    assert(thrown.getMessage === "Invalid input string, cannot be 'null'")
  }

  test("compressStr('AABBCCC', 0) throws IllegalArgumentException") {
    val thrown = intercept[IllegalArgumentException] {
      compressStr("ABC", 0)
    }
    assert(thrown.getMessage === "Invalid minOccurrence '0' value, should be greater than 0")
  }

  test("compressStr('AABBCCC', -1) throws IllegalArgumentException") {
    val thrown = intercept[IllegalArgumentException] {
      compressStr("ABC", -1)
    }
    assert(thrown.getMessage === "Invalid minOccurrence '-1' value, should be greater than 0")
  }

  test("compressStr('', 1) is ''") {
    assert(compressStr("", 1) === "")
  }

  test("compressStr('', 2) is ''") {
    assert(compressStr("", 2) === "")
  }

  test("compressStr('A', 1) is '1A'") {
    assert(compressStr("A", 1) === "1A")
  }

  test("compressStr('A', 2) is 'A'") {
    assert(compressStr("A", 2) === "A")
  }

  test("compressStr('AAA', 1) is '3A'") {
    assert(compressStr("AAA", 1) === "3A")
  }

  test("compressStr('AAA', 2) is '3A'") {
    assert(compressStr("AAA", 2) === "3A")
  }

  test("compressStr('ABBCDD', 1) is '1A2B1C2D'") {
    assert(compressStr("ABBCDD", 1) === "1A2B1C2D")
  }

  test("compressStr('ABBCDD', 2) is 'A2BC2D'") {
    assert(compressStr("ABBCDD", 2) === "A2BC2D")
  }

  test("compressStr('ABBBCCDDD', 3) is 'A3BCC3D'") {
    assert(compressStr("ABBBCCDDD", 3) === "A3BCC3D")
  }

  test("compressStr('ABBBCCDDD', 4) is 'ABBBCCDDD'") {
    assert(compressStr("ABBBCCDDD", 4) === "ABBBCCDDD")
  }

  test("compressStr('QQQFAAABEEEDFFC', 2) is '3QF3AB3ED2FC'") {
    assert(compressStr("QQQFAAABEEEDFFC", 2) === "3QF3AB3ED2FC")
  }

}
