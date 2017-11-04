package com.zdesk.search.model

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
    "%d,%s,%s".format(id, name, details.getOrElse(""))
  }
}
