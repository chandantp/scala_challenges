package com.myob

case class Payslip(name: String,
                   payPeriod: PayMonth,
                   grossIncome: Long,
                   incomeTax: Long,
                   netIncome: Long,
                   superAmount: Long) {

  override def toString: String = {
    "%s,%s,%d,%d,%d,%d".format(name,
      payPeriod,
      grossIncome,
      incomeTax,
      netIncome,
      superAmount)
  }

}

object Payslip {

  val Header = "Name,PayPeriod,GrossIncome,IncomeTax,NetIncome,Super"

  def apply(emp: Employee): List[Payslip] = emp.payMonths.map(payMonth => {
    val name = "%s %s".format(emp.firstName, emp.lastName)
    val grossIncome = Math.round((emp.annualSalary / 12.0) * payMonth.workingMonthRatio)
    val incomeTax = computeTax(emp.annualSalary, payMonth.workingMonthRatio)
    val netIncome = Math.round(grossIncome - incomeTax)
    val superAmount = Math.round(grossIncome * emp.superRate)

    Payslip(name, payMonth, grossIncome, incomeTax, netIncome, superAmount)
  })

  def computeTax(annualSalary: Double, workingMonthRatio: Double): Long = {
    def compute(baseTax: Double, deduction: Double, rate: Double): Double = {
      ((baseTax + ((annualSalary - deduction) * rate)) / 12.0) * workingMonthRatio
    }

    Math.round(annualSalary match {
      case gi if gi <= 18200  => 0
      case gi if gi <= 37000  => compute(0, 18200, 0.19)
      case gi if gi <= 87000  => compute(3572, 37000, 0.325)
      case gi if gi <= 180000 => compute(19822, 87000, 0.37)
      case _                  => compute(54232, 180000, 0.45)
    })
  }

}
