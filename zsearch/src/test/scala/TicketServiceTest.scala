package com.zdesk.search

import com.zdesk.search.services.TicketService
import com.zdesk.search.services.Utils.EmptyKey

import org.scalatest.FunSuite

class TicketServiceTest extends FunSuite {

  val ticketSvc = new TicketService("src/test/resources/tickets.json")

  test("search() throws exception when an invalid field name is passed") {
    val thrown = intercept[IllegalArgumentException] {
      ticketSvc.search("blah", "dontcare")
    }
    assert(thrown.getMessage === "Invalid field 'blah' detected !!")
  }

  test("search() accepts valid field names without throwing exception") {
    val validFields = "id,subject,type,priority,status,submitterId,assigneeId,organizationId," +
      "hasIncidents,via,tags,createdAt,dueAt,externalId,url,description"
    validFields.split(",").foreach(field => ticketSvc.search(field, "dontcare"))
  }

  //
  // Id field tests
  //
  test("search for id = 1 in mandatory 'id' string field is successful") {
    val tickets = ticketSvc.search("id", "id1")
    assert(tickets.size === 1 && tickets(0).id === "id1")
  }

  test("search for id = 'id999' in mandatory 'id' string field is unsuccessful") {
    val tickets = ticketSvc.search("id", "id999")
    assert(tickets.size === 0)
  }

  //
  // Subject field tests
  //
  test("search for empty 'subject' fields is unsuccessful") {
    val tickets = ticketSvc.search("subject", EmptyKey)
    assert(tickets.size === 0)
  }

  test("search for subject = 'not-found' in optional 'subject' string field is unsuccessful") {
    val tickets = ticketSvc.search("subject", "not-found")
    assert(tickets.size === 0)
  }

  test("search for subject = 'subject2' in optional 'subject' string field is successful") {
    val tickets = ticketSvc.search("subject", "subject2")
    assert(tickets.size === 1 && tickets(0).subject === Some("subject2"))
  }

  //
  // Type field tests
  //
  test("search for empty 'type' fields is successful") {
    val tickets = ticketSvc.search("type", EmptyKey)
    assert(tickets.size === 1)
  }

  test("search for type = 'not-found' in optional 'type' string field is unsuccessful") {
    val tickets = ticketSvc.search("type", "not-found")
    assert(tickets.size === 0)
  }

  test("search for type = 'question' in optional 'type' string field is successful") {
    val tickets = ticketSvc.search("type", "question")
    assert(tickets.size === 1 && tickets(0).ticketType === Some("question"))
  }

  //
  // Priority field tests
  //
  test("search for empty 'priority' fields is successful") {
    val tickets = ticketSvc.search("priority", EmptyKey)
    assert(tickets.size === 1)
  }

  test("search for priority = 'not-found' in optional 'priority' string field is unsuccessful") {
    val tickets = ticketSvc.search("priority", "not-found")
    assert(tickets.size === 0)
  }

  test("search for priority = 'low' in optional 'priority' string field is successful") {
    val tickets = ticketSvc.search("priority", "low")
    assert(tickets.size === 1 && tickets(0).priority === Some("low"))
  }

  //
  // Status field tests
  //
  test("search for empty 'status' fields is successful") {
    val tickets = ticketSvc.search("status", EmptyKey)
    assert(tickets.size === 1)
  }

  test("search for status = 'not-found' in optional 'status' string field is unsuccessful") {
    val tickets = ticketSvc.search("status", "not-found")
    assert(tickets.size === 0)
  }

  test("search for status = 'low' in optional 'status' string field is successful") {
    val tickets = ticketSvc.search("status", "pending")
    assert(tickets.size === 1 && tickets(0).status === Some("pending"))
  }

  //
  // SubmitterId field tests
  //
  test("search for empty 'submitterId' fields is successful") {
    val tickets = ticketSvc.search("submitterId", EmptyKey)
    assert(tickets.size === 1)
  }

  test("search for submitterId = 'not-found' in optional 'submitterId' string field is unsuccessful") {
    val tickets = ticketSvc.search("submitterId", "not-found")
    assert(tickets.size === 0)
  }

  test("search for submitterId = '2' in optional 'submitterId' string field is successful") {
    val tickets = ticketSvc.search("submitterId", "2")
    assert(tickets.size === 2)
  }

  //
  // AssigneeId field tests
  //
  test("search for empty 'assigneeId' fields is successful") {
    val tickets = ticketSvc.search("assigneeId", EmptyKey)
    assert(tickets.size === 1)
  }

  test("search for assigneeId = 'not-found' in optional 'assigneeId' string field is unsuccessful") {
    val tickets = ticketSvc.search("assigneeId", "not-found")
    assert(tickets.size === 0)
  }

  test("search for assigneeId = '4' in optional 'assigneeId' string field is successful") {
    val tickets = ticketSvc.search("assigneeId", "4")
    assert(tickets.size === 1)
  }

  //
  // OrganizationId field tests
  //
  test("search for empty 'organizationId' fields is successful") {
    val tickets = ticketSvc.search("organizationId", EmptyKey)
    assert(tickets.size === 1)
  }

  test("search for organizationId = 'not-found' in optional 'organizationId' string field is unsuccessful") {
    val tickets = ticketSvc.search("organizationId", "not-found")
    assert(tickets.size === 0)
  }

  test("search for organizationId = '1' in optional 'organizationId' string field is successful") {
    val tickets = ticketSvc.search("organizationId", "1")
    assert(tickets.size === 1 && tickets(0).organizationId === Some(1))
  }

  //
  // Tags field tests
  //
  test("search for empty 'tags' fields is successful") {
    val tickets = ticketSvc.search("tags", EmptyKey)
    assert(tickets.size === 2)
  }

  test("search for tags = 'not-found' in optional 'tags' List[string] field is unsuccessful") {
    val tickets = ticketSvc.search("tags", "not-found")
    assert(tickets.size === 0)
  }

  test("search for tags = 'tag2' in optional 'tags' List[string] field is successful") {
    val tickets = ticketSvc.search("tags", "tag2")
    assert(tickets.size == 2)
  }

  //
  // HasIncidents field tests
  //
  test("search for empty 'hasIncidents' fields is successful") {
    val tickets = ticketSvc.search("hasIncidents", EmptyKey)
    assert(tickets.size === 1)
  }

  test("search for hasIncidents = 'invalid-value' in optional 'hasIncidents' Boolean field is unsuccessful") {
    val tickets = ticketSvc.search("hasIncidents", "invalid-value")
    assert(tickets.size === 0)
  }

  test("search for hasIncidents = 'true' in optional 'hasIncidents' Boolean field is successful") {
    val tickets = ticketSvc.search("hasIncidents", "true")
    assert(tickets.size === 2)
  }

  test("search for hasIncidents = 'false' in optional 'hasIncidents' Boolean field is successful") {
    val tickets = ticketSvc.search("hasIncidents", "false")
    assert(tickets.size == 1)
  }

  //
  // Via field tests
  //
  test("search for empty 'via' fields is successful") {
    val tickets = ticketSvc.search("via", EmptyKey)
    assert(tickets.size === 1)
  }

  test("search for via = 'not-found' in optional 'via' string field is unsuccessful") {
    val tickets = ticketSvc.search("via", "not-found")
    assert(tickets.size === 0)
  }

  test("search for via = 'chat' in optional 'description' string field is successful") {
    val tickets = ticketSvc.search("via", "chat")
    assert(tickets.size === 1 && tickets(0).via === Some("chat"))
  }

  //
  // Description field tests
  //
  test("search for empty 'description' fields is successful") {
    val tickets = ticketSvc.search("description", EmptyKey)
    assert(tickets.size === 1)
  }

  test("search for description = 'not-found' in optional 'description' string field is unsuccessful") {
    val tickets = ticketSvc.search("description", "not-found")
    assert(tickets.size === 0)
  }

  test("search for description = 'description3' in optional 'description' string field is successful") {
    val tickets = ticketSvc.search("description", "description3")
    assert(tickets.size === 1 && tickets(0).description === Some("description3"))
  }

  //
  // CreatedAt field tests
  //
  test("search for empty 'createdAt' fields is successful") {
    val tickets = ticketSvc.search("createdAt", EmptyKey)
    assert(tickets.size === 1)
  }

  test("search for createdAt = 'not-found' in optional 'createdAt' string field is unsuccessful") {
    val tickets = ticketSvc.search("createdAt", "not-found")
    assert(tickets.size === 0)
  }

  test("search for createdAt = 'createdAt3' in optional 'createdAt' string field is successful") {
    val tickets = ticketSvc.search("createdAt", "createdAt3")
    assert(tickets.size === 1 && tickets(0).createdAt === Some("createdAt3"))
  }

  //
  // DueAt field tests
  //
  test("search for empty 'dueAt' fields is successful") {
    val tickets = ticketSvc.search("dueAt", EmptyKey)
    assert(tickets.size === 1)
  }

  test("search for dueAt = 'not-found' in optional 'dueAt' string field is unsuccessful") {
    val tickets = ticketSvc.search("dueAt", "not-found")
    assert(tickets.size === 0)
  }

  test("search for dueAt = 'dueAt1' in optional 'dueAt' string field is successful") {
    val tickets = ticketSvc.search("dueAt", "dueAt1")
    assert(tickets.size === 1 && tickets(0).dueAt === Some("dueAt1"))
  }

  //
  // ExternalId field tests
  //
  test("search for empty 'externalId' fields is successful") {
    val tickets = ticketSvc.search("externalId", EmptyKey)
    assert(tickets.size === 1)
  }

  test("search for externalId = 'not-found' in optional 'externalId' string field is unsuccessful") {
    val tickets = ticketSvc.search("externalId", "not-found")
    assert(tickets.size === 0)
  }

  test("search for externalId = 'externalId2' in optional 'externalId' string field is successful") {
    val tickets = ticketSvc.search("externalId", "externalId2")
    assert(tickets.size === 1 && tickets(0).externalId === Some("externalId2"))
  }

  //
  // Url field tests
  //
  test("search for empty 'url' fields is successful") {
    val tickets = ticketSvc.search("url", EmptyKey)
    assert(tickets.size === 1)
  }

  test("search for url = 'not-found' in optional 'url' string field is unsuccessful") {
    val tickets = ticketSvc.search("url", "not-found")
    assert(tickets.size === 0)
  }

  test("search for url = 'url2' in optional 'url' string field is successful") {
    val tickets = ticketSvc.search("url", "url2")
    assert(tickets.size === 1 && tickets(0).url === Some("url2"))
  }

  test("getOrgTickets returns None for org with no tickets") {
    assert(ticketSvc.getOrgTickets(4) == None)
  }

  test("getOrgTickets return users for org containing tickets") {
    val tickets = ticketSvc.getOrgTickets(3).get
    assert(tickets.size == 1 && tickets.toList(0).id == "id2")
  }

  test("getSubmittedTickets returns None for user with no submitted tickets") {
    assert(ticketSvc.getSubmittedTickets(1) == None)
  }

  test("getSubmittedTickets returns tickets for user with submitted tickets") {
    assert(ticketSvc.getSubmittedTickets(2).get.size == 2)
  }

  test("getAssignedTickets returns None for user with no assigned tickets") {
    assert(ticketSvc.getAssignedTickets(2) == None)

  }

  test("getAssignedTickets returns tickets for user with assigned tickets") {
    assert(ticketSvc.getAssignedTickets(1).get.size == 1)
  }
}
