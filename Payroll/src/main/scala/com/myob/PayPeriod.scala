package com.myob

import java.time.format.TextStyle
import java.time.{LocalDate, Month, Period}
import java.util.Locale

/*
 * Represents InputPayPeriod inclusive of start & end dates
 * for financial year 2017-18 only!
 *
 * Note: java.time.Period cannot be used directly as endDate is not included
 */
case class PayPeriod(startDate: LocalDate, endDate: LocalDate) {

  private val period = Period.between(startDate, endDate.plusDays(1))

  require(!period.isNegative && !period.isZero)

  override def toString: String = {
    "%s %s - %s %s".format(
      startDate.getDayOfMonth,
      startDate.getMonth.getDisplayName(TextStyle.FULL, Locale.getDefault),
      endDate.getDayOfMonth,
      endDate.getMonth.getDisplayName(TextStyle.FULL, Locale.getDefault)
    )
  }

  // Returns PayPeriods by month for payslip calculation
  def getPayPeriodsByMonth: List[PayPeriod] = {

    def isOnOrBeforeEndDate(date: LocalDate) = {
      date.getYear < endDate.getYear || date.getMonth.getValue <= endDate.getMonth.getValue
    }

    def getPayPeriods(currDate: LocalDate): List[PayPeriod] = {
      if (!isOnOrBeforeEndDate(currDate)) Nil
      else {
        val startDay =
          if (currDate.getMonth == startDate.getMonth) startDate.getDayOfMonth
          else 1
        val endDay =
          if (currDate.getMonth == endDate.getMonth) endDate.getDayOfMonth
          else currDate.getMonth.length(currDate.isLeapYear)
        PayPeriod(currDate.withDayOfMonth(startDay),
                  currDate.withDayOfMonth(endDay)) :: getPayPeriods(
          currDate.plusMonths(1))
      }
    }

    getPayPeriods(startDate)
  }

}

object PayPeriod {

  val Separator1 = "-"
  val Separator2 = " "

  def apply(period: String): PayPeriod = {
    require(period != null)

    period.trim.split(Separator1) match {
      case Array(startDate, endDate) =>
        PayPeriod(parse(startDate), parse(endDate))
      case _ =>
        throw new IllegalArgumentException(
          "Invalid payment period '%s', should be of the form 'DD MONTH - DD MONTH'"
            .format(period))
    }
  }

  def parse(date: String): LocalDate =
    try {
      date.trim.split(Separator2) match {
        case Array(day, month) => {
          val monthEnum = Month.valueOf(month.trim.toUpperCase)
          val year =
            if (monthEnum.getValue <= Month.JUNE.getValue) 2018 else 2017
          LocalDate.of(year, monthEnum, day.trim.toInt)
        }
        case _ =>
          throw new IllegalArgumentException(
            "Invalid date '%s', should be of the form 'DD MONTH'".format(date))
      }
    } catch {
      case e: Exception =>
        throw new IllegalArgumentException(
          "Invalid date '%s' : %s".format(date, e))
    }

}
