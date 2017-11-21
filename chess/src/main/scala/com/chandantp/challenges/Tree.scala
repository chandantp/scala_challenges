package com.chandantp.challenges

//
// Trie for tracking the already traversed parts of the solution tree
//
case class Tree(branch: String, var branches: Map[String, Tree]) {

  override def hashCode = branch.hashCode
  
  override def equals(that: Any): Boolean = that match {
    case that: Tree => this.branch.equals(that.branch)
    case _ => false
  }

  def isExplored(branch: List[String]): Boolean = {
    if (branches.isEmpty) true
    else if (branch.isEmpty) false //Branch not explored fully, only child branch explored
    else if (branches.contains(branch.head)) branches(branch.head).isExplored(branch.tail)
    else false
  }

  def add(branch: List[String]) {
    if (branch.isEmpty) branches = Map()
    else {
      if (!branches.contains(branch.head)) branches += (branch.head -> Tree(branch.head, Map()))
      branches(branch.head).add(branch.tail)
    }
  }

}