package com.myob

case class PayRecord(firstName: String,
                     lastName: String,
                     annualSalary: Float,
                     superRate: Float,
                     payPeriod: PayPeriod) {

  require(firstName.size > 0)
  require(lastName.size > 0)
  require(annualSalary > 0)
  require(superRate >= 0 && superRate <= 50)

}

object PayRecord {

  val Percent = "%"
  val Empty = ""

  def apply(firstName: String,
            lastName: String,
            annualSalary: String,
            superRate: String,
            payPeriod: String): PayRecord = {

    require(
      firstName != null && lastName != null
        && annualSalary != null && superRate != null && payPeriod != null)

    PayRecord(firstName.trim,
              lastName.trim,
              annualSalary.trim.toFloat,
              superRate.replace(Percent, Empty).trim.toFloat,
              PayPeriod(payPeriod))
  }

}
