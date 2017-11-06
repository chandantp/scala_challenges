package com.zdesk.search.services

import scala.collection.mutable

object Utils {

  val EmptyKey = ""
  val NotAvailable = "-NA-"

  def bind2map[T](keys: Option[List[String]], value: T, map: mutable.MultiMap[String, T]) = keys match {
    case None => map.addBinding(EmptyKey, value)
    case Some(Nil) => map.addBinding(EmptyKey, value)
    case Some(keys) => keys.foreach(key => map.addBinding(key.toLowerCase, value))
  }

  //
  // This method is used only for matching 'Url', 'Description' & 'date' fields
  // For all other fields, indexes are used for fast lookups
  //
  def isMatching[T](key: String, fieldValue: T): Boolean = fieldValue match {
    case i: Int => if (key.equalsIgnoreCase(EmptyKey)) false else key.equals(i.toString)
    case b: Boolean => if (key.equalsIgnoreCase(EmptyKey)) false else key.equalsIgnoreCase(b.toString)
    case str: String => if (key.equalsIgnoreCase(EmptyKey)) false else key.equalsIgnoreCase(str)
    case list: List[_] => key match {
      case EmptyKey => list.isEmpty // Match depends of whether list is empty or not as search is for empty field
      case _ => if (list.isEmpty) false else list.exists(s => isMatching(key, s)) // No match as list is empty and search key is not empty
    }
    case opt: Option[_] => opt match {
      case None => key.equalsIgnoreCase(EmptyKey)
      case Some(optValue) => isMatching(key, optValue)
    }
    case _ => throw new Exception("Unexpected field type %s".format(fieldValue))
  }

}
