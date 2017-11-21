package com.chandantp.challenges

object MainApp {

  def main(args: Array[String]) {

    try {
      if (args.size != 3) showUsage
      val (rows, columns, pieces) = (args(0).toInt, args(1).toInt, args(2))

      val chessSvc = new ChessService(rows, columns, pieces.toUpperCase)
      val startTime = System.currentTimeMillis()
      val solutions = chessSvc.computeSolutions
      val timeElapsedInMillis = System.currentTimeMillis() - startTime

      chessSvc.displaySolutions
      println("Solutions Count = %d".format(solutions.size))
      println("Time elapsed = %.4f seconds (%d ms)".format(timeElapsedInMillis / 1000.0, timeElapsedInMillis))

    } catch {
      case e: Exception => e.printStackTrace
    }
  }

  def showUsage {
    println("USAGE: <rows> <columns> <pawns>")
    println("Example:")
    println("sbt run 5 5 KKQQR")
    System.exit(0)
  }
}
