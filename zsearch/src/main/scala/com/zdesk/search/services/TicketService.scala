package com.zdesk.search.services

import com.zdesk.search.model.Ticket
import com.zdesk.search.services.SearchService._

import net.liftweb.json.{DefaultFormats, JField, parse}

import scala.collection.mutable

object TicketService {

  private val Id = "id"
  private val Subject = "subject"
  private val Type = "type"
  private val Priority = "priority"
  private val Status = "status"
  private val SubmitterId = "submitterId"
  private val AssigneeId = "assigneeId"
  private val OrganizationId = "organizationId"
  private val HasIncidents = "hasIncidents"
  private val Via = "via"
  private val Tags = "tags"
  private val CreatedAt = "createdAt"
  private val DueAt = "dueAt"
  private val ExternalId = "externalId"
  private val Url = "url"
  private val Description = "description"

  private val DefaultTicketsFile = "src/main/resources/tickets.json"

  private implicit val formats = DefaultFormats // Used by JSON library for loading JSON files

  private var tickets: List[Ticket] = _

  private val id2ticket = new mutable.HashMap[String, Ticket]()

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

  def search(field: String, key: String): List[Ticket] = field match {
    case Id => tickets.filter(ticket => isMatching(key, ticket.id))
    case Subject => tickets.filter(ticket => isMatching(key, ticket.subject))
    case Type => tickets.filter(ticket => isMatching(key, ticket.ticketType))
    case Priority => tickets.filter(ticket => isMatching(key, ticket.priority))
    case Status => tickets.filter(ticket => isMatching(key, ticket.status))
    case SubmitterId => tickets.filter(ticket => isMatching(key, ticket.submitterId))
    case AssigneeId => tickets.filter(ticket => isMatching(key, ticket.assigneeId))
    case OrganizationId => tickets.filter(ticket => isMatching(key, ticket.organizationId))
    case HasIncidents => tickets.filter(ticket => isMatching(key, ticket.hasIncidents))
    case Via => tickets.filter(ticket => isMatching(key, ticket.via))
    case Tags => tickets.filter(ticket => isMatching(key, ticket.tags))
    case CreatedAt => tickets.filter(ticket => isMatching(key, ticket.createdAt))
    case DueAt => tickets.filter(ticket => isMatching(key, ticket.dueAt))
    case Description => tickets.filter(ticket => isMatching(key, ticket.description))
    case ExternalId => tickets.filter(ticket => isMatching(key, ticket.externalId))
    case Url => tickets.filter(ticket => isMatching(key, ticket.url))
    case _ => throw new IllegalArgumentException("Invalid field '%s' detected !!".format(field))
  }

}
