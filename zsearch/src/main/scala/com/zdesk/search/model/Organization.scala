package com.zdesk.search.model

import com.zdesk.search.services.SearchService.NotAvailable

case class Organization(id: Int,
                        url: Option[String],
                        externalId: Option[String],
                        name: Option[String],
                        domainNames: Option[List[String]],
                        createdAt: Option[String],
                        details: Option[String],
                        sharedTickets: Option[Boolean],
                        tags: Option[List[String]]) {

  override def toString: String = {
    "OrgId: %d, Name: %s".format(id, name.getOrElse(NotAvailable))
  }

  def toStringDetailed: String = {
    "OrgId: %d, Name: %s\nDetails: %s, SharedTickets: %s\nDomains: %s\nTags: %s\nCreatedAt: %s\nExternalId: %s\nUrl: %s"
      .format(
        id,
        name.getOrElse(NotAvailable),
        details.getOrElse(NotAvailable),
        sharedTickets.getOrElse(NotAvailable),
        domainNames.map(_.mkString(":")).getOrElse(NotAvailable),
        tags.map(_.mkString(":")).getOrElse(NotAvailable),
        createdAt.getOrElse(NotAvailable),
        externalId.getOrElse(NotAvailable),
        url.getOrElse(NotAvailable)
      )
  }
}
