package com.zdesk.search.services

import com.zdesk.search.model.Organization
import com.zdesk.search.services.Utils._

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

  /*
   * DESIGN DECISION: Indexes for fast lookups
   *
   * All indexes are String -> String(s) mappings.
   * This leads to cleaner code as the 'SearchKey' string does not require messy
   * type conversion and related exception handling in case of invalid values
   * The performance hit is negligible as this affects only Int/Boolean fields
   *
   * No indexes are built for 'url' & 'createdAt' fields as this would be
   * memory intensive. These fields will use conventional iterative search.
   */
  private val id2org = new mutable.HashMap[String, Organization] // Unique
  private val name2orgs = new mutable.HashMap[String, mutable.Set[Organization]] with mutable.MultiMap[String, Organization]
  private val detail2orgs = new mutable.HashMap[String, mutable.Set[Organization]] with mutable.MultiMap[String, Organization]
  private val domain2orgs = new mutable.HashMap[String, mutable.Set[Organization]] with mutable.MultiMap[String, Organization]
  private val tag2orgs = new mutable.HashMap[String, mutable.Set[Organization]] with mutable.MultiMap[String, Organization]
  private val sharedTicket2orgs = new mutable.HashMap[String, mutable.Set[Organization]] with mutable.MultiMap[String, Organization]
  private val externalId2orgs = new mutable.HashMap[String, Organization] // Unique

  // build indexes for all except 'url' & 'createdAt' fields
  for (org <- organizations) {
    id2org.put(org.id.toString, org)
    name2orgs.addBinding(org.name.getOrElse(EmptyKey).toLowerCase, org)
    detail2orgs.addBinding(org.details.getOrElse(EmptyKey).toLowerCase, org)
    bind2map(org.tags, org, tag2orgs)
    bind2map(org.domainNames, org, domain2orgs)
    sharedTicket2orgs.addBinding(org.sharedTickets.map(_.toString).getOrElse(EmptyKey), org)
    externalId2orgs.put(org.externalId.getOrElse(EmptyKey).toLowerCase, org)
  }

  def getOrg(orgId: Int): Option[Organization] = id2org.get(orgId.toString)

  def search(field: String, key: String): List[Organization] = field match {
    case Id => id2org.get(key).map(List(_)).getOrElse(Nil)
    case Name => name2orgs.get(key.toLowerCase).map(_.toList).getOrElse(Nil)
    case Details => detail2orgs.get(key.toLowerCase).map(_.toList).getOrElse(Nil)
    case Domains => domain2orgs.get(key.toLowerCase).map(_.toList).getOrElse(Nil)
    case Tags => tag2orgs.get(key.toLowerCase).map(_.toList).getOrElse(Nil)
    case SharedTickets => sharedTicket2orgs.get(key.toLowerCase).map(_.toList).getOrElse(Nil)
    case CreatedAt => organizations.filter(org => isMatching(key, org.createdAt))
    case ExternalId => externalId2orgs.get(key.toLowerCase).map(List(_)).getOrElse(Nil)
    case Url => organizations.filter(org => isMatching(key, org.url))
    case _ => throw new IllegalArgumentException("Invalid field '%s' detected !!".format(field))
  }

}
