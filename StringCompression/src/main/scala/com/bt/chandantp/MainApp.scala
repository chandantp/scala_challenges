package com.bt.chandantp

object MainApp {

  def main(args: Array[String]) {
    val input = "QQQFAAABEEEDFFC"
    println("Output: compress(%s,%d) = ".format(input, 2) + Compressor.compressStr(input, 2))
  }

}
