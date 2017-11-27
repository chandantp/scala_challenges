package com.chandantp.genetic.algorithms

object MainApp extends App {

  // Set a candidate solution
  FitnessCalc.setSolution("1111000000000000000000000000000000000000000000000000000000001111")
  println("Solution = " + FitnessCalc.solution.mkString)

  // Create an initial population
  var myPop = new Population(50, true)

  // Evolve our population until we reach an optimum solution
  var generationCount = 1


  while (myPop.fittest.fitness < FitnessCalc.maxFitness) {
    System.out.println("Generation: %3d, Fittest: %s, Fitness: %d".format(
      generationCount, myPop.fittest, myPop.fittest.fitness))
    myPop = Algorithm.evolve(myPop)
    generationCount += 1
  }

  System.out.println("Solution found!")
  System.out.println("Generation: %3d, Fittest: %s, Fitness: %d".format(
    generationCount, myPop.fittest, myPop.fittest.fitness))
}
