package com.desk.search

import com.zdesk.search.services.SearchService

import org.scalatest.FunSuite

class SearchServiceTest extends FunSuite {

  test("search() throws exception when an invalid collection name is passed") {
    val searchSvc = new SearchService(
      "src/test/resources/organizations.json",
      "src/test/resources/users.json",
      "src/test/resources/tickets.json")

    val thrown = intercept[IllegalArgumentException] {
      searchSvc.search("blah", "name", "Alice")
    }
    assert(thrown.getMessage === "Invalid collection 'blah' detected !!")
  }

}
