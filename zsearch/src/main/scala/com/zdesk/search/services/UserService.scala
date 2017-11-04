package com.zdesk.search.services

import com.zdesk.search.model.User
import com.zdesk.search.services.SearchService._

import net.liftweb.json.{DefaultFormats, JField, parse}

import scala.collection.mutable

object UserService {

  private val Id = "id"
  private val Name = "name"
  private val Alias = "alias"
  private val Email = "email"
  private val Phone = "phone"
  private val OrganizationId = "organizationId"
  private val Locale = "locale"
  private val Timezone = "timezone"
  private val Signature = "signature"
  private val Role = "role"
  private val Tags = "tags"
  private val Active = "active"
  private val Verified = "verified"
  private val Shared = "shared"
  private val Suspended = "suspended"
  private val CreatedAt = "createdAt"
  private val LastLoginAt = "lastLoginAt"
  private val ExternalId = "externalId"
  private val Url = "url"

  private val DefaultUsersFile = "src/main/resources/users.json"

  private implicit val formats = DefaultFormats

  private var users: List[User] = _

  val id2user = new mutable.HashMap[Int, User]()

  def init(file: String = DefaultUsersFile) = {
    // load data
    users = parse(io.Source.fromFile(file).mkString)
      .transformField {
        case JField("_id", x)             => JField("id", x)
        case JField("external_id", x)     => JField("externalId", x)
        case JField("created_at", x)      => JField("createdAt", x)
        case JField("last_login_at", x)   => JField("lastLoginAt", x)
        case JField("organization_id", x) => JField("organizationId", x)
      }
      .extract[List[User]]

    // build indexes
    for (user <- users) {
      id2user.put(user.id, user)
    }
  }

  def getUser(userId: Int): Option[User] = id2user.get(userId)

  def search(field: String, key: String): List[User] = field match {
    case Id => users.filter(user => isMatching(key, user.id))
    case Name => users.filter(user => isMatching(key, user.name))
    case Alias => users.filter(user => isMatching(key, user.alias))
    case Email => users.filter(user => isMatching(key, user.email))
    case Phone => users.filter(user => isMatching(key, user.phone))
    case OrganizationId => users.filter(user => isMatching(key, user.organizationId))
    case Locale => users.filter(user => isMatching(key, user.locale))
    case Timezone => users.filter(user => isMatching(key, user.timezone))
    case Signature => users.filter(user => isMatching(key, user.signature))
    case Role => users.filter(user => isMatching(key, user.role))
    case Tags => users.filter(user => isMatching(key, user.tags))
    case Active => users.filter(user => isMatching(key, user.active))
    case Verified => users.filter(user => isMatching(key, user.verified))
    case Shared => users.filter(user => isMatching(key, user.shared))
    case Suspended => users.filter(user => isMatching(key, user.suspended))
    case CreatedAt => users.filter(user => isMatching(key, user.createdAt))
    case LastLoginAt => users.filter(user => isMatching(key, user.lastLoginAt))
    case ExternalId => users.filter(user => isMatching(key, user.externalId))
    case Url => users.filter(user => isMatching(key, user.url))
    case _ => throw new IllegalArgumentException("Invalid field '%s' detected !!".format(field))
  }

}
