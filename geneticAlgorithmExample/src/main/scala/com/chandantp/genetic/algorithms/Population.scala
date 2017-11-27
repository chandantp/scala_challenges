package com.chandantp.genetic.algorithms

import scala.collection.mutable.ArrayBuffer

case class Population(val size: Int, initialize: Boolean) {

  private val individuals = {
    val tmp = new ArrayBuffer[Individual]()
    (0 until size).foreach(_ => tmp.append(Individual(initialize)))
    tmp.toArray
  }

  def individual(index: Int) = individuals(index)

  def save(index: Int, individual: Individual): Unit = {
    individuals(index) = individual
  }

  def fittest: Individual = individuals.max

}
