package com.zdesk.search.services

import com.zdesk.search.model.Ticket
import com.zdesk.search.services.Utils._

import net.liftweb.json.{DefaultFormats, JField, parse}

import scala.collection.mutable

class TicketService(file: String) {

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

  private implicit val formats = DefaultFormats // Used by JSON library for loading JSON files

  // load data
  private var tickets: List[Ticket] = parse(io.Source.fromFile(file).mkString)
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

  /*
   * DESIGN DECISION: Indexes for fast lookups
   *
   * All indexes are String -> String(s) mappings.
   * This leads to cleaner code as the 'SearchKey' string does not require messy
   * type conversion and related exception handling in case of invalid values
   * The performance hit is negligible as this affects only Int/Boolean fields
   *
   * No indexes are built for 'url', 'description', 'createdAt' & 'dueAt' fields as this
   * would be memory intensive. These fields will use conventional iterative search.
   */
  private val id2ticket = new mutable.HashMap[String, Ticket] // Unique
  private val subject2tickets = new mutable.HashMap[String, mutable.Set[Ticket]] with mutable.MultiMap[String, Ticket]
  private val type2tickets = new mutable.HashMap[String, mutable.Set[Ticket]] with mutable.MultiMap[String, Ticket]
  private val priority2tickets = new mutable.HashMap[String, mutable.Set[Ticket]] with mutable.MultiMap[String, Ticket]
  private val status2tickets = new mutable.HashMap[String, mutable.Set[Ticket]] with mutable.MultiMap[String, Ticket]
  private val submitterId2tickets = new mutable.HashMap[String, mutable.Set[Ticket]] with mutable.MultiMap[String, Ticket]
  private val assigneeId2tickets = new mutable.HashMap[String, mutable.Set[Ticket]] with mutable.MultiMap[String, Ticket]
  private val orgId2tickets = new mutable.HashMap[String, mutable.Set[Ticket]] with mutable.MultiMap[String, Ticket]
  private val hasIncident2tickets = new mutable.HashMap[String, mutable.Set[Ticket]] with mutable.MultiMap[String, Ticket]
  private val via2tickets = new mutable.HashMap[String, mutable.Set[Ticket]] with mutable.MultiMap[String, Ticket]
  private val tag2tickets = new mutable.HashMap[String, mutable.Set[Ticket]] with mutable.MultiMap[String, Ticket]
  private val externalId2tickets = new mutable.HashMap[String, mutable.Set[Ticket]] with mutable.MultiMap[String, Ticket]

  // build indexes for all except 'url', 'description', 'createdAt' & 'dueAt' fields
  for (ticket <- tickets) {
    id2ticket.put(ticket.id, ticket)
    subject2tickets.addBinding(ticket.subject.getOrElse(EmptyKey).toLowerCase, ticket)
    type2tickets.addBinding(ticket.ticketType.getOrElse(EmptyKey).toLowerCase, ticket)
    priority2tickets.addBinding(ticket.priority.getOrElse(EmptyKey).toLowerCase, ticket)
    status2tickets.addBinding(ticket.status.getOrElse(EmptyKey).toLowerCase, ticket)
    submitterId2tickets.addBinding(ticket.submitterId.map(_.toString).getOrElse(EmptyKey), ticket)
    assigneeId2tickets.addBinding(ticket.assigneeId.map(_.toString).getOrElse(EmptyKey), ticket)
    orgId2tickets.addBinding(ticket.organizationId.map(_.toString).getOrElse(EmptyKey), ticket)
    hasIncident2tickets.addBinding(ticket.hasIncidents.map(_.toString).getOrElse(EmptyKey), ticket)
    via2tickets.addBinding(ticket.via.getOrElse(EmptyKey).toLowerCase, ticket)
    bind2map(ticket.tags, ticket, tag2tickets)
    externalId2tickets.addBinding(ticket.externalId.getOrElse(EmptyKey).toLowerCase, ticket)
  }

  def getOrgTickets(orgId: Int) = orgId2tickets.get(orgId.toString)

  def getSubmittedTickets(submitterId: Int) = submitterId2tickets.get(submitterId.toString)

  def getAssignedTickets(assigneeId: Int)= assigneeId2tickets.get(assigneeId.toString)

  def search(field: String, key: String): List[Ticket] = field match {
    case Id => id2ticket.get(key).map(List(_)).getOrElse(Nil)
    case Subject => subject2tickets.get(key.toLowerCase).map(_.toList).getOrElse(Nil)
    case Type => type2tickets.get(key.toLowerCase).map(_.toList).getOrElse(Nil)
    case Priority => priority2tickets.get(key.toLowerCase).map(_.toList).getOrElse(Nil)
    case Status => status2tickets.get(key.toLowerCase).map(_.toList).getOrElse(Nil)
    case SubmitterId => submitterId2tickets.get(key.toLowerCase).map(_.toList).getOrElse(Nil)
    case AssigneeId => assigneeId2tickets.get(key.toLowerCase).map(_.toList).getOrElse(Nil)
    case OrganizationId => orgId2tickets.get(key.toLowerCase).map(_.toList).getOrElse(Nil)
    case HasIncidents => hasIncident2tickets.get(key.toLowerCase).map(_.toList).getOrElse(Nil)
    case Via => via2tickets.get(key.toLowerCase).map(_.toList).getOrElse(Nil)
    case Tags => tag2tickets.get(key.toLowerCase).map(_.toList).getOrElse(Nil)
    case CreatedAt => tickets.filter(ticket => isMatching(key, ticket.createdAt))
    case DueAt => tickets.filter(ticket => isMatching(key, ticket.dueAt))
    case ExternalId => externalId2tickets.get(key.toLowerCase).map(_.toList).getOrElse(Nil)
    case Url => tickets.filter(ticket => isMatching(key, ticket.url))
    case Description => tickets.filter(ticket => isMatching(key, ticket.description))
    case _ => throw new IllegalArgumentException("Invalid field '%s' detected !!".format(field))
  }

}
