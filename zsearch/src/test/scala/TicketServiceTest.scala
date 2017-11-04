package com.zdesk.search

import com.zdesk.search.services.TicketService._
import com.zdesk.search.services._

import org.scalatest.{BeforeAndAfter, FunSuite}

class TicketServiceTest extends FunSuite with BeforeAndAfter {

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
    val validFields = "id,subject,type,priority,status,submitterId,assigneeId,organizationId," +
      "hasIncidents,via,tags,createdAt,dueAt,externalId,url,description"
    validFields.split(",").foreach(field => search(field, "dontcare"))
  }

  //
  // Id field tests
  //
  test("search for id = 1 in mandatory 'id' string field is successful") {
    val tickets = search("id", "id1")
    assert(tickets.size === 1 && tickets(0).id === "id1")
  }

  test("search for id = 'id999' in mandatory 'id' string field is unsuccessful") {
    val tickets = search("id", "id999")
    assert(tickets.size === 0)
  }

  //
  // Subject field tests
  //
  test("search for empty 'subject' fields is unsuccessful") {
    val tickets = search("subject", "#null#")
    assert(tickets.size === 0)
  }

  test("search for subject = 'not-found' in optional 'subject' string field is unsuccessful") {
    val tickets = search("subject", "not-found")
    assert(tickets.size === 0)
  }

  test("search for subject = 'subject2' in optional 'subject' string field is successful") {
    val tickets = search("subject", "subject2")
    assert(tickets.size === 1 && tickets(0).subject === Some("subject2"))
  }

  //
  // Type field tests
  //
  test("search for empty 'type' fields is successful") {
    val tickets = search("type", "#null#")
    assert(tickets.size === 1)
  }

  test("search for type = 'not-found' in optional 'type' string field is unsuccessful") {
    val tickets = search("type", "not-found")
    assert(tickets.size === 0)
  }

  test("search for type = 'question' in optional 'type' string field is successful") {
    val tickets = search("type", "question")
    assert(tickets.size === 1 && tickets(0).ticketType === Some("question"))
  }

  //
  // Priority field tests
  //
  test("search for empty 'priority' fields is successful") {
    val tickets = search("priority", "#null#")
    assert(tickets.size === 1)
  }

  test("search for priority = 'not-found' in optional 'priority' string field is unsuccessful") {
    val tickets = search("priority", "not-found")
    assert(tickets.size === 0)
  }

  test("search for priority = 'low' in optional 'priority' string field is successful") {
    val tickets = search("priority", "low")
    assert(tickets.size === 1 && tickets(0).priority === Some("low"))
  }

  //
  // Status field tests
  //
  test("search for empty 'status' fields is successful") {
    val tickets = search("status", "#null#")
    assert(tickets.size === 1)
  }

  test("search for status = 'not-found' in optional 'status' string field is unsuccessful") {
    val tickets = search("status", "not-found")
    assert(tickets.size === 0)
  }

  test("search for status = 'low' in optional 'status' string field is successful") {
    val tickets = search("status", "pending")
    assert(tickets.size === 1 && tickets(0).status === Some("pending"))
  }

  //
  // SubmitterId field tests
  //
  test("search for empty 'submitterId' fields is successful") {
    val tickets = search("submitterId", "#null#")
    assert(tickets.size === 1)
  }

  test("search for submitterId = 'not-found' in optional 'submitterId' string field is unsuccessful") {
    val tickets = search("submitterId", "not-found")
    assert(tickets.size === 0)
  }

  test("search for submitterId = '2' in optional 'submitterId' string field is successful") {
    val tickets = search("submitterId", "2")
    assert(tickets.size === 2)
  }

  //
  // AssigneeId field tests
  //
  test("search for empty 'assigneeId' fields is successful") {
    val tickets = search("assigneeId", "#null#")
    assert(tickets.size === 1)
  }

  test("search for assigneeId = 'not-found' in optional 'assigneeId' string field is unsuccessful") {
    val tickets = search("assigneeId", "not-found")
    assert(tickets.size === 0)
  }

  test("search for assigneeId = '4' in optional 'assigneeId' string field is successful") {
    val tickets = search("assigneeId", "4")
    assert(tickets.size === 1)
  }

  //
  // OrganizationId field tests
  //
  test("search for empty 'organizationId' fields is successful") {
    val tickets = search("organizationId", "#null#")
    assert(tickets.size === 1)
  }

  test("search for organizationId = 'not-found' in optional 'organizationId' string field is unsuccessful") {
    val tickets = search("organizationId", "not-found")
    assert(tickets.size === 0)
  }

  test("search for organizationId = '1' in optional 'organizationId' string field is successful") {
    val tickets = search("organizationId", "1")
    assert(tickets.size === 1 && tickets(0).organizationId === Some(1))
  }

  //
  // Tags field tests
  //
  test("search for empty 'tags' fields is successful") {
    val tickets = search("tags", "#null#")
    assert(tickets.size === 2)
  }

  test("search for tags = 'not-found' in optional 'tags' List[string] field is unsuccessful") {
    val tickets = search("tags", "not-found")
    assert(tickets.size === 0)
  }

  test("search for tags = 'tag2' in optional 'tags' List[string] field is successful") {
    val tickets = search("tags", "tag2")
    assert(tickets.size == 2)
  }

  //
  // HasIncidents field tests
  //
  test("search for empty 'hasIncidents' fields is successful") {
    val tickets = search("hasIncidents", "#null#")
    assert(tickets.size === 1)
  }

  test("search for hasIncidents = 'invalid-value' in optional 'hasIncidents' Boolean field is unsuccessful") {
    val tickets = search("hasIncidents", "invalid-value")
    assert(tickets.size === 0)
  }

  test("search for hasIncidents = 'true' in optional 'hasIncidents' Boolean field is successful") {
    val tickets = search("hasIncidents", "true")
    assert(tickets.size === 2)
  }

  test("search for hasIncidents = 'false' in optional 'hasIncidents' Boolean field is successful") {
    val tickets = search("hasIncidents", "false")
    assert(tickets.size == 1)
  }

  //
  // Via field tests
  //
  test("search for empty 'via' fields is successful") {
    val tickets = search("via", "#null#")
    assert(tickets.size === 1)
  }

  test("search for via = 'not-found' in optional 'via' string field is unsuccessful") {
    val tickets = search("via", "not-found")
    assert(tickets.size === 0)
  }

  test("search for via = 'chat' in optional 'description' string field is successful") {
    val tickets = search("via", "chat")
    assert(tickets.size === 1 && tickets(0).via === Some("chat"))
  }

  //
  // Description field tests
  //
  test("search for empty 'description' fields is successful") {
    val tickets = search("description", "#null#")
    assert(tickets.size === 1)
  }

  test("search for description = 'not-found' in optional 'description' string field is unsuccessful") {
    val tickets = search("description", "not-found")
    assert(tickets.size === 0)
  }

  test("search for description = 'description3' in optional 'description' string field is successful") {
    val tickets = search("description", "description3")
    assert(tickets.size === 1 && tickets(0).description === Some("description3"))
  }

  //
  // CreatedAt field tests
  //
  test("search for empty 'createdAt' fields is successful") {
    val tickets = search("createdAt", "#null#")
    assert(tickets.size === 1)
  }

  test("search for createdAt = 'not-found' in optional 'createdAt' string field is unsuccessful") {
    val tickets = search("createdAt", "not-found")
    assert(tickets.size === 0)
  }

  test("search for createdAt = 'createdAt3' in optional 'createdAt' string field is successful") {
    val tickets = search("createdAt", "createdAt3")
    assert(tickets.size === 1 && tickets(0).createdAt === Some("createdAt3"))
  }

  //
  // DueAt field tests
  //
  test("search for empty 'dueAt' fields is successful") {
    val tickets = search("dueAt", "#null#")
    assert(tickets.size === 1)
  }

  test("search for dueAt = 'not-found' in optional 'dueAt' string field is unsuccessful") {
    val tickets = search("dueAt", "not-found")
    assert(tickets.size === 0)
  }

  test("search for dueAt = 'dueAt1' in optional 'dueAt' string field is successful") {
    val tickets = search("dueAt", "dueAt1")
    assert(tickets.size === 1 && tickets(0).dueAt === Some("dueAt1"))
  }

  //
  // ExternalId field tests
  //
  test("search for empty 'externalId' fields is successful") {
    val tickets = search("externalId", "#null#")
    assert(tickets.size === 1)
  }

  test("search for externalId = 'not-found' in optional 'externalId' string field is unsuccessful") {
    val tickets = search("externalId", "not-found")
    assert(tickets.size === 0)
  }

  test("search for externalId = 'externalId2' in optional 'externalId' string field is successful") {
    val tickets = search("externalId", "externalId2")
    assert(tickets.size === 1 && tickets(0).externalId === Some("externalId2"))
  }

  //
  // Url field tests
  //
  test("search for empty 'url' fields is successful") {
    val tickets = search("url", "#null#")
    assert(tickets.size === 1)
  }

  test("search for url = 'not-found' in optional 'url' string field is unsuccessful") {
    val tickets = search("url", "not-found")
    assert(tickets.size === 0)
  }

  test("search for url = 'url2' in optional 'url' string field is successful") {
    val tickets = search("url", "url2")
    assert(tickets.size === 1 && tickets(0).url === Some("url2"))
  }

}
