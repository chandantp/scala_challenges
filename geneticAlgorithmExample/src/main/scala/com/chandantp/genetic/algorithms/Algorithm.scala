package com.chandantp.genetic.algorithms

object Algorithm {

  private val uniformRate = 0.5
  private val mutationRate = 0.015
  private val tournamentSize = 5

  def evolve(pop: Population): Population = {
    val newPopulation = new Population(pop.size, false)
    newPopulation.save(0, pop.fittest) // Keep our best individual

    for(i <- 1 until pop.size) {
      val indiv1 = tournamentSelection(pop)
      val indiv2 = tournamentSelection(pop)
      val newIndiv = crossover(indiv1, indiv2)
      newPopulation.save(i, newIndiv)
      mutate(newPopulation.individual(i))
    }

    newPopulation
  }

  // Crossover individuals
  private def crossover(indiv1: Individual, indiv2: Individual) = {
    val newIndiv = new Individual(false)
    for(i <- 0 until newIndiv.size) { // Crossover
      newIndiv.genes(i) = if (Math.random <= uniformRate) indiv1.genes(i) else indiv2.genes(i)
    }
    newIndiv
  }

  // Mutate an individual
  private def mutate(indiv: Individual): Unit = {
    for(i <- 0 until indiv.size) {
      if (Math.random <= mutationRate) {
        indiv.genes(i) = Math.random.round.toByte
      }
    }
  }

  // Select individuals for crossover
  private def tournamentSelection(pop: Population) = {
    val tournament = new Population(tournamentSize, false)
    for(i <- 0 until tournamentSize) {
      val randomId = (Math.random * pop.size).toInt
      tournament.save(i, pop.individual(randomId))
    }
    tournament.fittest
  }
}
