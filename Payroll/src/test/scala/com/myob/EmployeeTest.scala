package com.myob

import org.scalatest.FunSuite

class EmployeeTest extends  FunSuite {

  test("Employee(null) throws IllegalArgumentException") {
    val thrown = intercept[IllegalArgumentException](Employee(null))
    assert(thrown.getMessage === "Employee record cannot be null!!")
  }

  test("Employee record missing required number of fields throws IllegalArgumentException") {
    val thrown = intercept[IllegalArgumentException] {
      Employee("invalidrecord")
    }
    assert(thrown.getMessage.startsWith("Missing/extra fields in employee record"))
  }

  test("Employee record containing extra fields throws IllegalArgumentException") {
    val thrown = intercept[IllegalArgumentException] {
      Employee("f1,f2,f3,f4,f5,f6")
    }
    assert(thrown.getMessage.startsWith("Missing/extra fields in employee record"))
  }

  test("Employee record with empty 'firstName' throws IllegalArgumentException") {
    val thrown = intercept[IllegalArgumentException] {
      Employee(" ,lname,10000,10%,01 March - 31 March")
    }
    assert(thrown.getMessage === "No employee field can be empty or null!!")
  }

  test("Employee record with empty 'lastName' throws IllegalArgumentException") {
    val thrown = intercept[IllegalArgumentException] {
      Employee("fname,,10000,10%,01 March - 31 March")
    }
    assert(thrown.getMessage === "No employee field can be empty or null!!")
  }

  test("Employee record with empty 'annualSalary' throws IllegalArgumentException") {
    val thrown = intercept[IllegalArgumentException] {
      Employee("fname,lname,,10%,01 March - 31 March")
    }
    assert(thrown.getMessage === "No employee field can be empty or null!!")
  }

  test("Employee record with empty 'superRate' throws IllegalArgumentException") {
    val thrown = intercept[IllegalArgumentException] {
      Employee("fname,lname,10000,  ,01 March - 31 March")
    }
    assert(thrown.getMessage === "No employee field can be empty or null!!")
  }

  test("Employee record with empty 'payPeriod' throws IllegalArgumentException") {
    val thrown = intercept[IllegalArgumentException] {
      Employee("fname,lname,10000,10%,  ")
    }
    assert(thrown.getMessage === "No employee field can be empty or null!!")
  }

  test("Employee(fname=null) throws IllegalArgumentException") {
    val thrown = intercept[IllegalArgumentException] {
      Employee(null, "lname", 10000, 10, PayMonth("01 March - 31 March"))
    }
    assert(thrown.getMessage === "No employee field can be empty or null!!")
  }

  test("Employee(fname='') throws IllegalArgumentException") {
    val thrown = intercept[IllegalArgumentException] {
      Employee("", "lname", 10000, 10, PayMonth("01 March - 31 March"))
    }
    assert(thrown.getMessage === "No employee field can be empty or null!!")
  }

  test("Employee(lname=null) throws IllegalArgumentException") {
    val thrown = intercept[IllegalArgumentException] {
      Employee("fname", null, 10000, 10, PayMonth("01 March - 31 March"))
    }
    assert(thrown.getMessage === "No employee field can be empty or null!!")
  }

  test("Employee(lname=' ') throws IllegalArgumentException") {
    val thrown = intercept[IllegalArgumentException] {
      Employee("fname", " ", 10000, 10, PayMonth("01 March - 31 March"))
    }
    assert(thrown.getMessage === "No employee field can be empty or null!!")
  }

  test("Employee(annualSalary=0) throws IllegalArgumentException") {
    val thrown = intercept[IllegalArgumentException] {
      new Employee("fname", "lname", 0, 10, PayMonth("01 March - 31 March"))
    }
    assert(thrown.getMessage.startsWith("AnnualSalary should be positive"))
  }

  test("Employee(annualSalary=-5000) throws IllegalArgumentException") {
    val thrown = intercept[IllegalArgumentException] {
      new Employee("fname", "lname", -5000, 10, PayMonth("01 March - 31 March"))
    }
    assert(thrown.getMessage.startsWith("AnnualSalary should be positive"))
  }

  test("Employee(superRate=51) throws IllegalArgumentException") {
    val thrown = intercept[IllegalArgumentException] {
      new Employee("fname", "lname", 10000, 51, PayMonth("01 March - 31 March"))
    }
    assert(thrown.getMessage.startsWith("SuperRate should be between 0% to 50% inclusive"))
  }

  test("Employee(superRate=-1) throws IllegalArgumentException") {
    val thrown = intercept[IllegalArgumentException] {
      new Employee("fname", "lname", 10000, -1, PayMonth("01 March - 31 March"))
    }
    assert(thrown.getMessage.startsWith("SuperRate should be between 0% to 50% inclusive"))
  }

  test("Employee(payPeriod=null) throws IllegalArgumentException") {
    val thrown = intercept[IllegalArgumentException] {
      Employee("fname", "lname", 10000, 10, null)
    }
    assert(thrown.getMessage === "No employee field can be empty or null!!")
  }

  test("Employee(payPeriod=Nil) throws IllegalArgumentException") {
    val thrown = intercept[IllegalArgumentException] {
      Employee("fname", "lname", 10000, 10, Nil)
    }
    assert(thrown.getMessage === "No employee field can be empty or null!!")
  }

}
