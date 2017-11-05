package com.zdesk.search.model

import com.zdesk.search.services.SearchService.NotAvailable

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
    "UserId: %d, Name: %s".format(id, name.getOrElse(NotAvailable))
  }

  def toStringDetailed: String = {
    ("UserId: %d, Name: %s\nAlias: %s, Email: %s, Phone: %s\nRole: %s, Locale: %s, Timezone: %s, Signature: %s\n"+
      "Active: %s, Verified: %s, Shared: %s, Suspended: %s\nTags: %s\nCreatedAt: %s\nLastLoginAt: %s\nExternalId: %s\nUrl: %s")
      .format(
        id,
        name.getOrElse(NotAvailable),
        alias.getOrElse(NotAvailable),
        email.getOrElse(NotAvailable),
        phone.getOrElse(NotAvailable),
        role.getOrElse(NotAvailable),
        locale.getOrElse(NotAvailable),
        timezone.getOrElse(NotAvailable),
        signature.getOrElse(NotAvailable),
        active.getOrElse(NotAvailable),
        verified.getOrElse(NotAvailable),
        shared.getOrElse(NotAvailable),
        suspended.getOrElse(NotAvailable),
        tags.map(_.mkString(":")).getOrElse(NotAvailable),
        createdAt.getOrElse(NotAvailable),
        lastLoginAt.getOrElse(NotAvailable),
        externalId.getOrElse(NotAvailable),
        url.getOrElse(NotAvailable)
      )
  }
}
