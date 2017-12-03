package com.chandantp.challenges

import collection.mutable
//
// Tree for tracking already traversed parts of the solution space
//
case class TrackerTree(branches: mutable.Map[String, TrackerTree]) {

  val Explored = "X"

  def isExplored(path: List[String]): Boolean = {
    if (branches.contains(Explored)) true
    else if (path.isEmpty) false //Branch not explored fully
    else if (branches.contains(path.head)) branches(path.head).isExplored(path.tail)
    else false
  }

  def markAsExplored(path: List[String]): Unit = path match {
    case Nil => {
      branches.clear
      branches(Explored) = TrackerTree(mutable.Map())
    }
    case head :: tail => {
      if (!branches.contains(head)) branches(head) = TrackerTree(mutable.Map())
      branches(head).markAsExplored(tail)
    }
  }

}
