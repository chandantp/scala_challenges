package com.myob

case class Employee(firstName: String,
                    lastName: String,
                    annualSalary: Double,
                    superRate: Double,
                    payMonths: List[PayMonth]) {

  if (firstName == null || firstName.trim.length == 0 ||
    lastName == null || lastName.trim.length == 0 ||
    payMonths == null || payMonths == Nil) {
    throw new IllegalArgumentException("No employee field can be empty or null!!")
  }

  if (annualSalary <= 0) {
    throw new IllegalArgumentException(
      "AnnualSalary should be positive : %f".format(annualSalary))
  }

  if (superRate < 0 || superRate > 0.5) {
    throw new IllegalArgumentException(
      "SuperRate should be between 0%% to 50%% inclusive : %.2f".format(superRate))
  }

}

object Employee {

  val Empty = ""
  val Percent = "%"
  val Separator = ","
  val Header = "FirstName,LastName,AnnualSalary,SuperRate,PayPeriod"

  def apply(employee: String): Employee = employee.split(Separator) match {

    case Array(firstName, lastName, annualSalary, superRate, payPeriod) => {
      if (annualSalary == null || annualSalary.trim.length == 0 ||
        superRate == null || superRate.trim.length == 0) {
        throw new IllegalArgumentException("No employee field can be empty or null!!")
      }

      Employee(firstName.trim,
        lastName.trim,
        annualSalary.trim.toDouble,
        superRate.replace(Percent, Empty).trim.toDouble / 100.0,
        PayMonth(payPeriod))
    }

    case _ => {
      throw new IllegalArgumentException(
        "Missing/extra fields in employee record: '%s'".format(employee))
    }
  }

}
