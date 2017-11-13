package com.chandantp.challenges

//
// Trie for tracking the already traversed parts of the solution tree
//
case class Tree(branch: String, var branches: Map[String, Tree]) {

  var solutions = List[String]()
  
  override def hashCode = branch.hashCode()
  
  override def equals(that: Any): Boolean = that match {
    case that: Tree => this.branch.equals(that.branch)
    case _ => false
  }

  def include(ibranch: List[String], board: String, chessPieces: Int) {
    incl(ibranch)
    if (ibranch.size == chessPieces) {
      solutions = solutions :+ board
      //println("solution: " + ibranch)
    }
  }

  def isExplored(ibranch: List[String]): Boolean = {
    if (branches.isEmpty) true
    else if (ibranch.isEmpty) false //Branch not explored fully, only child branch explored
    else if (branches.contains(ibranch.head)) branches(ibranch.head).isExplored(ibranch.tail)
    else false
  }

  private def incl(ibranch: List[String]) {
    if (ibranch.isEmpty) branches = Map()
    else {
      if (!branches.contains(ibranch.head)) branches += (ibranch.head -> Tree(ibranch.head, Map()))
      branches(ibranch.head).incl(ibranch.tail)
    }
  }

}