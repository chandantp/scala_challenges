package com.zdesk.search.services

import com.zdesk.search.model.Organization
import com.zdesk.search.services.Utils.isMatching

import net.liftweb.json.{DefaultFormats, JField, parse}

import scala.collection.mutable

class OrganizationService(file: String) {

  private val Id = "id"
  private val Name = "name"
  private val Details = "details"
  private val Domains = "domains"
  private val Tags = "tags"
  private val SharedTickets = "sharedTickets"
  private val CreatedAt = "createdAt"
  private val ExternalId = "externalId"
  private val Url = "url"

  private implicit val formats = DefaultFormats // Used by JSON library for loading JSON files

  // load data
  private var organizations: List[Organization] = parse(io.Source.fromFile(file).mkString)
    .transformField {
      case JField("_id", x)            => JField("id", x)
      case JField("external_id", x)    => JField("externalId", x)
      case JField("created_at", x)     => JField("createdAt", x)
      case JField("domain_names", x)   => JField("domainNames", x)
      case JField("shared_tickets", x) => JField("sharedTickets", x)
    }
    .extract[List[Organization]]

  private val id2org = new mutable.HashMap[Int, Organization]()

  // build indexes
  for (org <- organizations) {
    id2org.put(org.id, org)
  }

  def getOrg(orgId: Int): Option[Organization] = id2org.get(orgId)

  def search(field: String, key: String): List[Organization] = field match {
    case Id => organizations.filter(org => isMatching(key, org.id))
    case Name => organizations.filter(org => isMatching(key, org.name))
    case Details => organizations.filter(org => isMatching(key, org.details))
    case Domains => organizations.filter(org => isMatching(key, org.domainNames))
    case Tags => organizations.filter(org => isMatching(key, org.tags))
    case SharedTickets => organizations.filter(org => isMatching(key, org.sharedTickets))
    case CreatedAt => organizations.filter(org => isMatching(key, org.createdAt))
    case ExternalId => organizations.filter(org => isMatching(key, org.externalId))
    case Url => organizations.filter(org => isMatching(key, org.url))
    case _ => throw new IllegalArgumentException("Invalid field '%s' detected !!".format(field))
  }

}
