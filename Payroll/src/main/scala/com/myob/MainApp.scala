package com.myob

import scala.util.{Try, Success, Failure}

object MainApp {

  val Separator = ","

  def main(args: Array[String]): Unit = {

    val payrollRecords = io.Source.stdin.getLines.toList.map(parse)

    println(payrollRecords)

  }

  def parse(record: String): Option[PayRecord] = {
    record.split(Separator) match {
      case Array(firstName, lastName, annualSalary, superRate, duration) => {
        Try(PayRecord(firstName, lastName, annualSalary, superRate, duration)) match {
          case Success(record) => Option(record)
          case Failure(ex)     => println(ex); None
        }
      }
      case _ => println("Invalid or missing fields in input record '%s'".format(record)); None
    }
  }

}
