package com.myob

import scala.util.{Try, Success, Failure}

object MainApp extends App {

  println("Enter Payroll Records one per line in CSV format specified below (Ctrl-D for EOI):")
  println(Employee.Header)

  val payslips = io.Source.stdin.getLines.toList.flatMap(parse).flatMap(Payslip(_))
  println("\n#### Payslips ####:\n" + Payslip.Header)
  payslips.foreach(println)

  def parse(employee: String): Option[Employee] = Try(Employee(employee)) match {
    case Success(employee) => Some(employee)
    case Failure(ex)      => {
      ex.printStackTrace()
      None
    }
  }

}
