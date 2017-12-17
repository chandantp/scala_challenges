package com.myob

import org.scalatest.FunSuite

class PayMonthTest extends FunSuite {

  test("PayMonth(null) throws IllegalArgumentException") {
    val thrown = intercept[IllegalArgumentException](PayMonth(null))
    assert(thrown.getMessage === "Period cannot be empty or null!!")
  }

  test("PayMonth('') throws IllegalArgumentException") {
    val thrown = intercept[IllegalArgumentException](PayMonth(""))
    assert(thrown.getMessage === "Period cannot be empty or null!!")
  }

  test("PayMonth('  ') throws IllegalArgumentException") {
    val thrown = intercept[IllegalArgumentException](PayMonth("  "))
    assert(thrown.getMessage === "Period cannot be empty or null!!")
  }

  test("PayMonth.parse('01') throws IllegalArgumentException") {
    val thrown = intercept[IllegalArgumentException] {
      PayMonth.parse("01")
    }
    assert(thrown.getMessage.endsWith("should be of the form 'Day Month'"))
  }

  test("PayMonth.parse('01march') throws IllegalArgumentException") {
    val thrown = intercept[IllegalArgumentException] {
      PayMonth.parse("01march")
    }
    assert(thrown.getMessage.endsWith("should be of the form 'Day Month'"))
  }

  test("PayMonth.parse('01 abc') throws IllegalArgumentException") {
    val thrown = intercept[IllegalArgumentException] {
      PayMonth.parse("01 abc")
    }
    assert(thrown.getMessage.startsWith("Invalid date '01 abc'"))
  }

  test("PayMonth.parse('01 mar') throws IllegalArgumentException") {
    val thrown = intercept[IllegalArgumentException] {
      PayMonth.parse("01 mar")
    }
    assert(thrown.getMessage.startsWith("Invalid date '01 mar'"))
  }

  test("PayMonth.parse('0 march') throws IllegalArgumentException") {
    val thrown = intercept[IllegalArgumentException] {
      PayMonth.parse("0 march")
    }
    assert(thrown.getMessage.startsWith("Invalid date '0 march'"))
  }

  test("PayMonth.parse('32 march') throws IllegalArgumentException") {
    val thrown = intercept[IllegalArgumentException] {
      PayMonth.parse("32 march")
    }
    assert(thrown.getMessage.startsWith("Invalid date '32 march'"))
  }

  test("PayMonth.parse('30 february') throws IllegalArgumentException") {
    val thrown = intercept[IllegalArgumentException] {
      PayMonth.parse("30 february")
    }
    assert(thrown.getMessage.startsWith("Invalid date '30 february'"))
  }

  test("PayMonth.parse(January 2018 - June 2018) succeeds") {
    assert(PayMonth.parse("01 january").toString === "2018-01-01")
    assert(PayMonth.parse("1 february").toString === "2018-02-01")
    assert(PayMonth.parse("28 february").toString === "2018-02-28")
    assert(PayMonth.parse("15 march").toString === "2018-03-15")
    assert(PayMonth.parse("30 june").toString === "2018-06-30")
  }

  test("PayMonth.parse(July 2017 - Dec 2017) succeeds") {
    assert(PayMonth.parse("1 july").toString === "2017-07-01")
    assert(PayMonth.parse("15 august").toString === "2017-08-15")
    assert(PayMonth.parse("31 december").toString === "2017-12-31")
  }

  test("PayMonth('01 august') throws IllegalArgumentException") {
    val thrown = intercept[IllegalArgumentException] {
      PayMonth("01 august")
    }
    assert(thrown.getMessage.endsWith("should be of the form 'Day Month - Day Month'"))
  }

  test("PayMonth('01 august-') throws IllegalArgumentException") {
    val thrown = intercept[IllegalArgumentException] {
      PayMonth("01 august-")
    }
    assert(thrown.getMessage.endsWith("should be of the form 'Day Month - Day Month'"))
  }

  test("PayMonth('01 august 02 september') throws IllegalArgumentException") {
    val thrown = intercept[IllegalArgumentException] {
      PayMonth("01 august 02 september")
    }
    assert(thrown.getMessage.endsWith("should be of the form 'Day Month - Day Month'"))
  }

  test("PayMonth('01 august - 02 september') succeeds") {
    val months = PayMonth("01 august - 02 september")
    assert(months.size === 2)
    assert(months(0).toString === "01:Aug:2017 - 31:Aug:2017")
    assert(months(1).toString === "01:Sep:2017 - 02:Sep:2017")
  }

  test("PayMonth('15 august - 01 august' is invalid as it denotes negative period") {
    val thrown = intercept[IllegalArgumentException] {
      PayMonth("15 august - 01 august")
    }
    assert(thrown.getMessage.startsWith("Period cannot be negative or zero"))
  }

  test("PayMonth('15 february - 31 january' is invalid as it denotes negative period") {
    val thrown = intercept[IllegalArgumentException] {
      PayMonth("15 february - 31 january")
    }
    assert(thrown.getMessage.startsWith("Period cannot be negative or zero"))
  }

  test("PayMonth('01 february - 31 july' is invalid as it denotes negative period") {
    val thrown = intercept[IllegalArgumentException] {
      PayMonth("01 february - 31 july") // 2018 to 2017
    }
    assert(thrown.getMessage.startsWith("Period cannot be negative or zero"))
  }
}
