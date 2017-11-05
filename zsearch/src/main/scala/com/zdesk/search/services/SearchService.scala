package com.zdesk.search.services

import com.zdesk.search.model._
import com.zdesk.search.services.OrganizationService._
import com.zdesk.search.services.TicketService._
import com.zdesk.search.services.UserService._


object SearchService {

  val EmptyKey = "#null#"
  val NotAvailable = "-NA-"
  val Line = "-" * 100

  def loadData = {
    OrganizationService.init()
    UserService.init()
    TicketService.init()
  }

  def search(collection: String, field: String, key: String): Unit = collection match {
    case "organizations" => displayOrgSearchResults(OrganizationService.search(field, key))
    case "users" => displayUserSearchResults(UserService.search(field, key))
    case "tickets" => displayTicketSearchResults(TicketService.search(field, key))
    case _ => throw new IllegalArgumentException("Invalid collection '%s' detected !!".format(collection))
  }

  def displayOrgSearchResults(orgs: List[Organization]): Unit = {
    println(Line + "\nOrganizations found = %s\n".format(orgs.size) + Line)

    orgs.zipWithIndex.foreach{ case(org, index) => {
      println("Result: %d".format(index+1))
      println(org.toStringDetailed)

      val orgUsers = getOrgUsers(org.id)
      println("Users linked to this organization: " + orgUsers.map(_.size).getOrElse(NotAvailable))
      orgUsers.foreach(users => users.foreach(user => println("=> " + user)))

      val orgTickets = getOrgTickets(org.id)
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
      println("=> " + user.organizationId.flatMap(getOrg(_)).getOrElse(NotAvailable))

      val submittedTickets = getSubmittedTickets(user.id)
      println("User submitted tickets: " + submittedTickets.map(_.size).getOrElse(NotAvailable))
      submittedTickets.foreach(tickets => tickets.foreach(ticket => println("=> " + ticket)))

      val assignedTickets = getAssignedTickets(user.id)
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
      println("=> " + ticket.organizationId.flatMap(getOrg(_)).getOrElse(NotAvailable))

      println("Ticket submitter")
      println("=> " + ticket.submitterId.flatMap(getUser(_)).getOrElse(NotAvailable))

      println("Ticket assignee")
      println("=> " + ticket.assigneeId.flatMap(getUser(_)).getOrElse(NotAvailable))

      println(Line)
    }}
  }

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
