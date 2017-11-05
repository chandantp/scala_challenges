package com.zdesk.search.services

import com.zdesk.search.model._
import Utils._

class SearchService(orgsFile: String, usersFile: String, ticketsFile: String) {

  val Line = "-" * 100

  val orgSvc = new OrganizationService(orgsFile)
  val userSvc = new UserService(usersFile)
  val ticketSvc = new TicketService(ticketsFile)

  def search(collection: String, field: String, key: String): Unit = collection match {
    case "organizations" => displayOrgSearchResults(orgSvc.search(field, key))
    case "users" => displayUserSearchResults(userSvc.search(field, key))
    case "tickets" => displayTicketSearchResults(ticketSvc.search(field, key))
    case _ => throw new IllegalArgumentException("Invalid collection '%s' detected !!".format(collection))
  }

  def displayOrgSearchResults(orgs: List[Organization]): Unit = {
    println(Line + "\nOrganizations found = %s\n".format(orgs.size) + Line)

    orgs.zipWithIndex.foreach{ case(org, index) => {
      println("Result: %d".format(index+1))
      println(org.toStringDetailed)

      val orgUsers = userSvc.getOrgUsers(org.id)
      println("Users linked to this organization: " + orgUsers.map(_.size).getOrElse(NotAvailable))
      orgUsers.foreach(users => users.foreach(user => println("=> " + user)))

      val orgTickets = ticketSvc.getOrgTickets(org.id)
      println("Tickets linked to this organization: " + orgTickets.map(_.size).getOrElse(NotAvailable))
      orgTickets.foreach(tickets => tickets.foreach(ticket => println("=> " + ticket)))

      println(Line)
    }}
  }

  def displayUserSearchResults(users: List[User]): Unit = {
    println(Line + "\nUsers found = %d\n".format(users.size) + Line)

    users.zipWithIndex.foreach{ case(user, index) => {
      println("Result: %d".format(index+1))
      println(user.toStringDetailed)

      println("User organization:")
      println("=> " + user.organizationId.flatMap(orgSvc.getOrg(_)).getOrElse(NotAvailable))

      val submittedTickets = ticketSvc.getSubmittedTickets(user.id)
      println("User submitted tickets: " + submittedTickets.map(_.size).getOrElse(NotAvailable))
      submittedTickets.foreach(tickets => tickets.foreach(ticket => println("=> " + ticket)))

      val assignedTickets = ticketSvc.getAssignedTickets(user.id)
      println("User assigned tickets: " + assignedTickets.map(_.size).getOrElse(NotAvailable))
      assignedTickets.foreach(tickets => tickets.foreach(ticket => println("=> " + ticket)))

      println(Line)
    }}
  }

  def displayTicketSearchResults(tickets: List[Ticket]): Unit = {
    println(Line + "\nTickets found = %s\n".format(tickets.size) + Line)

    tickets.zipWithIndex.foreach{ case(ticket, index) => {
      println("Result: %d".format(index+1))
      println(ticket.toStringDetailed)

      println("Ticket organization")
      println("=> " + ticket.organizationId.flatMap(orgSvc.getOrg(_)).getOrElse(NotAvailable))

      println("Ticket submitter")
      println("=> " + ticket.submitterId.flatMap(userSvc.getUser(_)).getOrElse(NotAvailable))

      println("Ticket assignee")
      println("=> " + ticket.assigneeId.flatMap(userSvc.getUser(_)).getOrElse(NotAvailable))

      println(Line)
    }}
  }

}
