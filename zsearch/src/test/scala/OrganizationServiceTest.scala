package com.desk.search

import com.zdesk.search.services.OrganizationService._
import com.zdesk.search.services._

import org.scalatest.{BeforeAndAfter, FunSuite}

class OrganizationServiceTest extends FunSuite with BeforeAndAfter {

  before {
    UserService.init("src/test/resources/users.json")
    TicketService.init("src/test/resources/tickets.json")
    OrganizationService.init("src/test/resources/organizations.json")
  }

  test("search() throws exception when an invalid field name is passed") {
    val thrown = intercept[IllegalArgumentException] {
      search("blah", "dontcare")
    }
    assert(thrown.getMessage === "Invalid field 'blah' detected !!")
  }

  test("search() accepts valid field names without throwing exception") {
    val validFields = "id,name,details,domains,tags,sharedTickets,createdAt,externalId,url"
    validFields.split(",").foreach(field => search(field, "dontcare"))
  }

  //
  // Id field tests
  //
  test("search for id = 1 in mandatory 'id' integer field is successful") {
    val orgs = search("id", "1")
    assert(orgs.size === 1 && orgs(0).id === 1)
  }

  test("search for id = 999 in mandatory 'id' integer field is unsuccessful") {
    val orgs = search("id", "999")
    assert(orgs.size === 0)
  }

  //
  // Name field tests
  //
  test("search for empty 'name' fields is successful") {
    val orgs = search("name", "#null#")
    assert(orgs.size === 1)
  }

  test("search for name = 'not-found' in optional 'name' string field is unsuccessful") {
    val orgs = search("name", "not-found")
    assert(orgs.size === 0)
  }

  test("search for name = 'name1' in optional 'name' string field is successful") {
    val orgs = search("name", "name1")
    assert(orgs.size === 1 && orgs(0).name === Some("name1"))
  }

  //
  // Details field tests
  //
  test("search for empty 'details' fields is successful") {
    val orgs = search("details", "#null#")
    assert(orgs.size === 1)
  }

  test("search for details = 'not-found' in optional 'details' string field is unsuccessful") {
    val orgs = search("details", "not-found")
    assert(orgs.size === 0)
  }

  test("search for details = 'details1' in optional 'details' string field is successful") {
    val orgs = search("details", "details1")
    assert(orgs.size === 1 && orgs(0).details === Some("details1"))
  }

  //
  // Domains field tests
  //
  test("search for empty 'domains' fields is successful") {
    val orgs = search("domains", "#null#")
    assert(orgs.size === 2)
  }

  test("search for domains = 'not-found' in optional 'details' List[string] field is unsuccessful") {
    val orgs = search("domains", "not-found")
    assert(orgs.size === 0)
  }

  test("search for domains = 'domain11.com' in optional 'details' List[string] field is successful") {
    val orgs = search("domains", "domain11.com")
    assert(orgs.size == 1 && orgs(0).domainNames === Some(List("domain11.com", "domain12.com")))
  }

  //
  // Tags field tests
  //
  test("search for empty 'tags' fields is successful") {
    val orgs = search("tags", "#null#")
    assert(orgs.size === 2)
  }

  test("search for tags = 'not-found' in optional 'tags' List[string] field is unsuccessful") {
    val orgs = search("tags", "not-found")
    assert(orgs.size === 0)
  }

  test("search for tags = 'tags1' in optional 'tags' List[string] field is successful") {
    val orgs = search("tags", "tags1")
    assert(orgs.size == 1 && orgs(0).tags === Some(List("tags1", "tags2")))
  }

  //
  // SharedTickets field tests
  //
  test("search for empty 'sharedTickets' fields is successful") {
    val orgs = search("sharedTickets", "#null#")
    assert(orgs.size === 1)
  }

  test("search for sharedTickets = 'invalid-value' in optional 'sharedTickets' Boolean field is unsuccessful") {
    val orgs = search("sharedTickets", "invalid-value")
    assert(orgs.size === 0)
  }

  test("search for sharedTickets = 'true' in optional 'sharedTickets' Boolean field is successful") {
    val orgs = search("sharedTickets", "true")
    assert(orgs.size === 1)
  }

  test("search for sharedTickets = 'false' in optional 'sharedTickets' Boolean field is successful") {
    val orgs = search("sharedTickets", "false")
    assert(orgs.size == 2)
  }

  //
  // CreatedAt field tests
  //
  test("search for empty 'createdAt' fields is successful") {
    val orgs = search("createdAt", "#null#")
    assert(orgs.size === 1)
  }

  test("search for createdAt = 'not-found' in optional 'createdAt' string field is unsuccessful") {
    val orgs = search("createdAt", "not-found")
    assert(orgs.size === 0)
  }

  test("search for createdAt = 'createdAt3' in optional 'createdAt' string field is successful") {
    val orgs = search("createdAt", "createdAt3")
    assert(orgs.size === 1 && orgs(0).createdAt === Some("createdAt3"))
  }

  //
  // ExternalId field tests
  //
  test("search for empty 'externalId' fields is successful") {
    val orgs = search("externalId", "#null#")
    assert(orgs.size === 1)
  }

  test("search for externalId = 'not-found' in optional 'externalId' string field is unsuccessful") {
    val orgs = search("externalId", "not-found")
    assert(orgs.size === 0)
  }

  test("search for externalId = 'externalId2' in optional 'externalId' string field is successful") {
    val orgs = search("externalId", "externalId2")
    assert(orgs.size === 1 && orgs(0).externalId === Some("externalId2"))
  }

  //
  // Url field tests
  //
  test("search for empty 'url' fields is successful") {
    val orgs = search("url", "#null#")
    assert(orgs.size === 1)
  }

  test("search for url = 'not-found' in optional 'url' string field is unsuccessful") {
    val orgs = search("url", "not-found")
    assert(orgs.size === 0)
  }

  test("search for url = 'url2' in optional 'url' string field is successful") {
    val orgs = search("url", "url2")
    assert(orgs.size === 1 && orgs(0).url === Some("url2"))
  }
}
