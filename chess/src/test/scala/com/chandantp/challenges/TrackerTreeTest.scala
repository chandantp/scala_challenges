package com.chandantp.challenges

import org.scalatest.{BeforeAndAfter, FunSuite}

import scala.collection.mutable.Map

class TrackerTreeTest extends FunSuite with BeforeAndAfter {

  test("markAsExplored() works as expected") {
    val tree = TrackerTree(Map())

    tree.markAsExplored(List("1K", "5Q", "12R"))
    tree.markAsExplored(List("1K", "5Q", "14N"))
    assert(tree ==
      TrackerTree(Map("1K" ->
        TrackerTree(Map("5Q" ->
          TrackerTree(Map(
            "12R" -> TrackerTree(Map("X" -> TrackerTree(Map()))),
            "14N" -> TrackerTree(Map("X" -> TrackerTree(Map()))))
          ))))))


    tree.markAsExplored(List("1K", "5Q"))
    assert(tree ==
      TrackerTree(Map("1K" ->
        TrackerTree(Map("5Q" ->
          TrackerTree(Map("X" -> TrackerTree(Map()))))))))

    tree.markAsExplored(List("1K"))
    assert(tree ==
      TrackerTree(Map("1K" ->
        TrackerTree(Map("X" -> TrackerTree(Map()))))))

    tree.markAsExplored(Nil)
    assert(tree ==
      TrackerTree(Map("X" -> TrackerTree(Map()))))
  }


  test("isExplored() works as expected when searching explored paths") {
    val tree = TrackerTree(Map())

    tree.markAsExplored(List("1K", "5Q", "12R"))
    assert(!tree.isExplored(List("1K")))
    assert(!tree.isExplored(List("1K", "5Q")))
    assert(tree.isExplored(List("1K", "5Q", "12R")))

    tree.markAsExplored(List("1K", "5Q"))
    assert(!tree.isExplored(List("1K")))
    assert(tree.isExplored(List("1K", "5Q")))

    tree.markAsExplored(List("1K"))
    assert(tree.isExplored(List("1K")))

    tree.markAsExplored(Nil)
    assert(tree.isExplored(Nil))
  }

  test("isExplored() works as expected when searching for unexplored paths") {
    val tree = TrackerTree(Map())

    tree.markAsExplored(List("1K", "5Q", "12R"))
    assert(!tree.isExplored(List("2K")))
    assert(!tree.isExplored(List("1K", "10Q")))
    assert(!tree.isExplored(List("1K", "5Q", "22N")))
  }

}