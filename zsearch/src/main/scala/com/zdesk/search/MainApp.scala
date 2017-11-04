package com.zdesk.search

import java.io.FileNotFoundException

import com.zdesk.search.services.SearchService._

object MainApp {

  def main(args: Array[String]) {
    if (args.size == 0) showUsage()
    else {
      try {
        val (collection, field, key) = parse(args)
        loadData
        search(collection, field, key).foreach(println)
      } catch {
        case e: IllegalArgumentException => showUsage(Option(e.getMessage))
        case e: FileNotFoundException => println(e.getMessage)
        case e: Exception => println(e.getMessage)
      }
    }
  }
}
