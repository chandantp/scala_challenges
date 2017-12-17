package com.myob

import java.time.format.TextStyle
import java.time.{LocalDate, Month, Period}
import java.util.Locale

case class PayMonth(startDate: LocalDate, endDate: LocalDate) {

  // In java.time.Period, the endDate is not included
  val period = Period.between(startDate, endDate.plusDays(1))

  if (period.isNegative || period.isZero) {
    throw new IllegalArgumentException(
      "Month period cannot be negative or zero : '%s'".format(period))
  }

  if (period.getMonths > 1 || (period.getMonths == 1 && period.getDays > 0)) {
    throw new IllegalArgumentException(
      "Month period cannot exceed 1 month duration : '%s'".format(period))
  }

  val workingMonthRatio: Double = {
    if (period.getMonths == 0)
      period.getDays.toDouble / startDate.getMonth.length(startDate.isLeapYear).toDouble
    else 1.0
  }

  override def toString: String = {
    "%02d:%s:%d - %02d:%s:%d".format(
      startDate.getDayOfMonth,
      startDate.getMonth.getDisplayName(TextStyle.SHORT, Locale.getDefault),
      startDate.getYear,
      endDate.getDayOfMonth,
      endDate.getMonth.getDisplayName(TextStyle.SHORT, Locale.getDefault),
      endDate.getYear
    )
  }

}

object PayMonth {

  val Separator1 = "-"
  val Separator2 = " "

  def apply(period: String): List[PayMonth] = {

    if (period == null || period.trim.length == 0) {
      throw new IllegalArgumentException("Period cannot be empty or null")
    }

    period.trim.split(Separator1) match {
      case Array(startDate, endDate) =>
        computePayMonths(parse(startDate), parse(endDate))
      case _ =>
        throw new IllegalArgumentException(
          "Invalid period '%s', should be of the form 'Day Month - Day Month'"
            .format(period))
    }
  }

  def parse(date: String): LocalDate = {
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
            "Invalid date '%s', should be of the form 'Day Month'".format(date))
      }
    } catch {
      case e: Exception =>
        throw new IllegalArgumentException(
          "Invalid date '%s' : %s".format(date, e))
    }
  }

  def computePayMonths(startDate: LocalDate, endDate: LocalDate): List[PayMonth] = {

    def isOnOrBeforeEndDate(date: LocalDate) = {
      date.getYear < endDate.getYear || date.getMonth.getValue <= endDate.getMonth.getValue
    }

    def computeMonths(currDate: LocalDate): List[PayMonth] = {
      if (!isOnOrBeforeEndDate(currDate)) Nil
      else {
        val startDay =
          if (currDate.getMonth == startDate.getMonth) startDate.getDayOfMonth
          else 1
        val endDay =
          if (currDate.getMonth == endDate.getMonth) endDate.getDayOfMonth
          else currDate.getMonth.length(currDate.isLeapYear)

        PayMonth(
          currDate.withDayOfMonth(startDay),
          currDate.withDayOfMonth(endDay)) :: computeMonths(
          currDate.plusMonths(1))
      }
    }

    val period = Period.between(startDate, endDate.plusDays(1))
    if (period.isNegative || period.isZero) {
      throw new IllegalArgumentException("Period cannot be negative or zero : '%s'".format(period))
    }

    computeMonths(startDate)
  }

}
