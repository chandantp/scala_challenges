package com.desk.search

import com.zdesk.search.services.SearchService._

import org.scalatest.{BeforeAndAfter, FunSuite}

class SearchServiceTest extends FunSuite with BeforeAndAfter {

  test("parsing arguments of invalid length = 1,2 & 4 fails") {
    val input =
      List(Array("1"), Array("1", "2"), Array("1", "2", "3", "4"))

    input.foreach {args =>
      val thrown = intercept[IllegalArgumentException] { parse(args) }
      assert(thrown.getMessage === "Invalid argument size '%d' detected !!".format(args.size))
    }
  }

  test("parsing arguments of valid length '3' succeeds") {
    val result = parse(Array("users", "name", "Alice"))
    assert(result === ("users", "name", "Alice"))
  }

}
