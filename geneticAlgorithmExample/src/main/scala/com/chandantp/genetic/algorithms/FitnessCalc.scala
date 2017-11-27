package com.chandantp.genetic.algorithms

import scala.collection.mutable.ArrayBuffer

object FitnessCalc {

  var solution = {
    val tmp = new ArrayBuffer[Byte]()
    (0 until Individual.GeneLength).foreach(_ => tmp.append(0))
    tmp.toArray
  }

  // Method to set our candidate solution with string of 0s and 1s
  def setSolution(newSolution: String): Unit = {
    for(i <- 0 until Individual.GeneLength) {
      val character = newSolution.charAt(i)
      solution(i) = if (character == '1')  1 else 0
    }
  }

  // Calculate inidividuals fitness by comparing it to the candidate solution
  def fitness(individual: Individual): Int = {
    var fitness = 0
    for(i <- 0 until Individual.GeneLength) {
      if (individual.genes(i) == solution(i)) fitness += 1
    }
    fitness
  }

  def maxFitness: Int = solution.length

}
