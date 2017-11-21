package com.chandantp.challenges

//
// Tree for tracking already traversed parts of the solution space
//
case class Tree(branch: String, var branches: Map[String, Tree]) {

  val Explored = "Explored"

  def isExplored(path: List[String]): Boolean = {
    if (branches.contains(Explored)) true
    else if (path.isEmpty) false //Branch not explored fully
    else if (branches.contains(path.head)) branches(path.head).isExplored(path.tail)
    else false
  }

  def track(path: List[String]): Unit = path match {
    case Nil => branches = Map(Explored -> Tree(Explored, Map()))
    case first :: rest => {
      if (!branches.contains(first)) {
        branches += (first -> Tree(first, Map()))
      }
      branches(first).track(rest)
    }
  }

}
