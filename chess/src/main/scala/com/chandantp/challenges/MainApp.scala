package com.chandantp.challenges

object MainApp {

  def main(args: Array[String]) {
    try {
      val (rows, columns, pieces, verbose) = parse(args)
      val chessSvc = new ChessService(rows, columns, pieces.toUpperCase)

      val startTime = System.currentTimeMillis()
      val solutions = chessSvc.computeSolutions
      val timeElapsedInMillis = System.currentTimeMillis() - startTime

      if (verbose) chessSvc.prettyPrintSolutions
      println("Solutions Count = %d".format(solutions.size))
      println("Time elapsed = %.4f seconds (%d ms)".format(timeElapsedInMillis / 1000.0, timeElapsedInMillis))

    } catch {
      case e: Exception => {
        e.printStackTrace
        showUsage
      }
    }
  }

  def parse(args: Array[String]) = args.size match {
    case 3 => (args(0).toInt, args(1).toInt, args(2).toUpperCase, false)
    case 4 => {
      if (args(3).toLowerCase != "-v") {
        throw new IllegalArgumentException("Invalid argument: " + args(3))
      }
      (args(0).toInt, args(1).toInt, args(2), true)
    }
    case _ => throw new IllegalArgumentException("Invalid number of arguments")
  }

  def showUsage {
    println("USAGE: <rows> <columns> <pawns> [-v]")
    println("  -v : show all chess board solutions (optional)")
    println("Examples:")
    println(" sbt run 4 4 QQQQ")
    println(" sbt run 6 6 KKQQBB -v")
    System.exit(0)
  }
}
