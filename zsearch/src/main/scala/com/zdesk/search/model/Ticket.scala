package com.zdesk.search.model

import com.zdesk.search.services.{OrganizationService, UserService}

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
    "%s,%s,%s,%s,%s".format(
      id,
      subject.getOrElse("--NoSubject--"),
      submitterId.flatMap(UserService.getUser(_)).flatMap(_.name).getOrElse("--NoSubmitter--"),
      assigneeId.flatMap(UserService.getUser(_)).flatMap(_.name).getOrElse("--NoAssignee--"),
      organizationId.flatMap(OrganizationService.getOrganization(_)).flatMap(_.name).getOrElse("--NoOrg--")
    )
  }
}
