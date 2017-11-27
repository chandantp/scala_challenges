package com.chandantp.genetic.algorithms

import scala.collection.mutable.ArrayBuffer

case class Individual(initialize: Boolean = true) extends Ordered[Individual] {

  val genes = {
    val tmp = new ArrayBuffer[Byte]()
    (0 until Individual.GeneLength).foreach(_ =>
      tmp.append(if (initialize) Math.random.round.toByte else 0))
    tmp.toArray
  }

  def size = genes.size

  def fitness: Int = FitnessCalc.fitness(this)

  override def compare(that: Individual): Int = this.fitness - that.fitness

  override def toString: String = genes.mkString
}

object Individual {
  val GeneLength = 64
}
