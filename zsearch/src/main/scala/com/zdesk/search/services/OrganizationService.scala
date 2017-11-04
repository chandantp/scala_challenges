package com.zdesk.search.services

import com.zdesk.search.model.Organization
import net.liftweb.json.{DefaultFormats, JField, parse}

import scala.collection.mutable

object OrganizationService {

  private val DefaultOrganizationsFile = "src/main/resources/organizations.json"

  private implicit val formats = DefaultFormats

  private var organizations: List[Organization] = _

  val id2org = new mutable.HashMap[Int, Organization]()

  def init(file: String = DefaultOrganizationsFile) = {
    // load data
    organizations = parse(io.Source.fromFile(file).mkString)
      .transformField {
        case JField("_id", x)            => JField("id", x)
        case JField("external_id", x)    => JField("externalId", x)
        case JField("created_at", x)     => JField("createdAt", x)
        case JField("domain_names", x)   => JField("domainNames", x)
        case JField("shared_tickets", x) => JField("sharedTickets", x)
      }
      .extract[List[Organization]]

    // build indexes
    for (org <- organizations) {
      id2org.put(org.id, org)
    }
  }

  def getOrganization(orgId: Int): Option[Organization] = id2org.get(orgId)

}
