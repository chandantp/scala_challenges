package com.zdesk.search.services

import com.zdesk.search.model.Ticket
import net.liftweb.json.{DefaultFormats, JField, parse}

import scala.collection.mutable

object TicketService {

  private val DefaultTicketsFile = "src/main/resources/tickets.json"

  private implicit val formats = DefaultFormats

  private var tickets: List[Ticket] = _

  val id2ticket = new mutable.HashMap[String, Ticket]()

  def init(file: String = DefaultTicketsFile) = {
    // load data
    tickets = parse(io.Source.fromFile(file).mkString)
      .transformField {
        case JField("_id", x)             => JField("id", x)
        case JField("external_id", x)     => JField("externalId", x)
        case JField("created_at", x)      => JField("createdAt", x)
        case JField("type", x)            => JField("ticketType", x)
        case JField("submitter_id", x)    => JField("submitterId", x)
        case JField("assignee_id", x)     => JField("assigneeId", x)
        case JField("organization_id", x) => JField("organizationId", x)
        case JField("has_incidents", x)   => JField("hasIncidents", x)
        case JField("due_at", x)          => JField("dueAt", x)
      }
      .extract[List[Ticket]]

    // build indexes
    for (ticket <- tickets) {
      id2ticket.put(ticket.id, ticket)
    }
  }
  
}
