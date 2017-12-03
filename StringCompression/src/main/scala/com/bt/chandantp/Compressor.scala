package com.bt.chandantp

object Compressor {

  def compressStr(input: String, minOccurrence: Int): String = {

    def compressSeq(seq: List[Char]): String = seq match {
      case Nil => ""
      case _ => if (seq.size >= minOccurrence) "%d%c".format(seq.size, seq(0)) else seq.mkString
    }

    def compress(remaining: List[Char], currSeq: List[Char], result: String): String = remaining match {
      case Nil => result + compressSeq(currSeq)
      case currChar :: remaining => currSeq match {
        case Nil => compress(remaining, currChar :: currSeq, result)
        case seqChar :: _ => seqChar match {
          case seqChar: Char if currChar == seqChar => compress(remaining, currChar :: currSeq, result)
          case _ => compress(remaining, currChar :: Nil, result + compressSeq(currSeq))
        }
      }
    }

    if (input == null) {
      throw new IllegalArgumentException("Invalid input string, cannot be 'null'")
    }

    if (minOccurrence <= 0) {
      throw new IllegalArgumentException(
        "Invalid minOccurrence '%d' value, should be greater than 0".format(minOccurrence))
    }
    compress(input.toList, Nil, "")
  }

}
