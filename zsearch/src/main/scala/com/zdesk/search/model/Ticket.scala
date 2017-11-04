package com.zdesk.search.model

import com.zdesk.search.services.SearchService.NotAvailable

case class Ticket(id: String,
                  url: Option[String],
                  externalId: Option[String],
                  createdAt: Option[String],
                  ticketType: Option[String],
                  subject: Option[String],
                  description: Option[String],
                  priority: Option[String],
                  status: Option[String],
                  submitterId: Option[Int],
                  assigneeId: Option[Int],
                  organizationId: Option[Int],
                  tags: Option[List[String]],
                  hasIncidents: Option[Boolean],
                  dueAt: Option[String],
                  via: Option[String]) {

  override def toString: String = {
    "TicketId: %s, Subject: %s".format(id, subject.getOrElse(NotAvailable))
  }

  def toStringDetailed: String = {
    "TicketId: %s\nSubject: %s\nType: %s, Priority: %s, Status: %s\nHasIncidents: %s, Via: %s\nTags: %s\nCreatedAt: %s\nDueAt: %s\nExternalId: %s\nUrl: %s\nDescription: %s"
      .format(
        id,
        subject.getOrElse(NotAvailable),
        ticketType.getOrElse(NotAvailable),
        priority.getOrElse(NotAvailable),
        status.getOrElse(NotAvailable),
        hasIncidents.getOrElse(NotAvailable),
        via.getOrElse(NotAvailable),
        tags.map(_.mkString(":")).getOrElse(NotAvailable),
        createdAt.getOrElse(NotAvailable),
        dueAt.getOrElse(NotAvailable),
        externalId.getOrElse(NotAvailable),
        url.getOrElse(NotAvailable),
        description.getOrElse(NotAvailable)
      )
  }
}
