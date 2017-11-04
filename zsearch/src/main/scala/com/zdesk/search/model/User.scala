package com.zdesk.search.model

import com.zdesk.search.services.OrganizationService

case class User(id: Int,
                url: Option[String],
                externalId: Option[String],
                name: Option[String],
                alias: Option[String],
                createdAt: Option[String],
                active: Option[Boolean],
                verified: Option[Boolean],
                shared: Option[Boolean],
                locale: Option[String],
                timezone: Option[String],
                lastLoginAt: Option[String],
                email: Option[String],
                phone: Option[String],
                signature: Option[String],
                organizationId: Option[Int],
                tags: Option[List[String]],
                suspended: Option[Boolean],
                role: Option[String]) {

  override def toString: String = {
    "%d,%s,%s,%s".format(
      id,
      name.getOrElse("--NoName--"),
      alias.getOrElse("--NoAlias--"),
      organizationId.flatMap(OrganizationService.getOrganization(_)).flatMap(_.name).getOrElse("--NoOrg--")
    )
  }
}
