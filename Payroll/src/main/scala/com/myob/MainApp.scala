package com.myob

import scala.util.{Try, Success, Failure}

object MainApp extends App {

  val Separator = ","

  println("Enter Payroll Records one per line in CSV format specified below (Ctrl-D for EOI):")
  println(Employee.Header)

  val payslips = io.Source.stdin.getLines.toList.flatMap(parse).flatMap(Payslip(_))
  println("\n#### Payslips ####:\n" + Payslip.Header)
  payslips.foreach(println)

  def parse(employeeRecord: String): Option[Employee] = {
    employeeRecord.split(Separator) match {
      case Array(firstName, lastName, annualSalary, superRate, duration) => {
        Try(Employee(firstName, lastName, annualSalary, superRate, duration)) match {
          case Success(employee) => Option(employee)
          case Failure(ex)      => {
            ex.printStackTrace()
            None
          }
        }
      }
      case _ => {
        println("Invalid or incorrect field count in input = '%s'".format(employeeRecord))
        None
      }
    }
  }

}
