package com.myob

import org.scalatest.FunSuite

class PayslipTest extends FunSuite {

  test("computeTax(18200,1|0.1) == 0") {
    assert(Payslip.computeTax(18200,1) === 0)
    assert(Payslip.computeTax(18200,1) === 0)
  }

  test("computeTax(28200,1|0.1) == 0") {
    assert(Payslip.computeTax(28200,1) === 158)
    assert(Payslip.computeTax(28200,0.1) === 16)
  }

  test("computeTax(47000,1|0.1) == 0") {
    assert(Payslip.computeTax(47000,1) === 569)
    assert(Payslip.computeTax(47000,0.1) === 57)
  }

  test("computeTax(97000,1|0.1) == 0") {
    assert(Payslip.computeTax(97000,1) === 1960)
    assert(Payslip.computeTax(97000,0.1) === 196)
  }

  test("computeTax(190000,1|0.1) == 0") {
    assert(Payslip.computeTax(190000,1) === 4894)
    assert(Payslip.computeTax(190000,0.1) === 489)
  }

  test("check gross,net,tax,super when annualSalary=100000 and super=10%") {
    val payslip = Payslip(Employee("Abc,Xyz,100000,10%,01 February - 28 February"))(0)
    assert(payslip.grossIncome === 8333)
    assert(payslip.incomeTax === 2053)
    assert(payslip.netIncome === 6280)
    assert(payslip.superAmount === 833)
  }

  test("1 Payslip generated for Employee(David,Rudd,60050,9%,01 March - 31 March)") {
    val payslips = Payslip(Employee("David,Rudd,60050,9%,01 March - 31 March"))
    assert(payslips.size === 1)
    assert(payslips(0).toString === "David Rudd,01:Mar:2018 - 31:Mar:2018,5004,922,4082,450")
  }

  test("1 Payslip generated for Employee(Ryan,Chen,120000,10%,01 March - 31 March)") {
    val payslips = Payslip(Employee("Ryan,Chen,120000,10%,01 March - 31 March"))
    assert(payslips.size === 1)
    assert(payslips(0).toString === "Ryan Chen,01:Mar:2018 - 31:Mar:2018,10000,2669,7331,1000")
  }

  test("8 Payslip generated for Employee(Alice,360000,10%,16 September-1 April)") {
    val payslips = Payslip(Employee("Alice,Mars,360000,10%,16 september-1 april"))
    assert(payslips.size === 8)
    assert(payslips(0).toString === "Alice Mars,16:Sep:2017 - 30:Sep:2017,15000,5635,9365,1500")
    assert(payslips(1).toString === "Alice Mars,01:Oct:2017 - 31:Oct:2017,30000,11269,18731,3000")
    assert(payslips(2).toString === "Alice Mars,01:Nov:2017 - 30:Nov:2017,30000,11269,18731,3000")
    assert(payslips(3).toString === "Alice Mars,01:Dec:2017 - 31:Dec:2017,30000,11269,18731,3000")
    assert(payslips(4).toString === "Alice Mars,01:Jan:2018 - 31:Jan:2018,30000,11269,18731,3000")
    assert(payslips(5).toString === "Alice Mars,01:Feb:2018 - 28:Feb:2018,30000,11269,18731,3000")
    assert(payslips(6).toString === "Alice Mars,01:Mar:2018 - 31:Mar:2018,30000,11269,18731,3000")
    assert(payslips(7).toString === "Alice Mars,01:Apr:2018 - 01:Apr:2018,1000,376,624,100")
  }

}
