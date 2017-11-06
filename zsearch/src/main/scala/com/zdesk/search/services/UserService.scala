package com.zdesk.search.services

import com.zdesk.search.model.User
import com.zdesk.search.services.Utils._

import net.liftweb.json.{DefaultFormats, JField, parse}

import scala.collection.mutable

class UserService(file: String) {

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

  private implicit val formats = DefaultFormats // Used by JSON library for loading JSON files

  // load data
  private var users: List[User] = parse(io.Source.fromFile(file).mkString)
    .transformField {
      case JField("_id", x)             => JField("id", x)
      case JField("external_id", x)     => JField("externalId", x)
      case JField("created_at", x)      => JField("createdAt", x)
      case JField("last_login_at", x)   => JField("lastLoginAt", x)
      case JField("organization_id", x) => JField("organizationId", x)
    }
    .extract[List[User]]

  /*
   * DESIGN DECISION: Indexes for fast lookups
   *
   * All indexes are String -> String(s) mappings.
   * This leads to cleaner code as the 'SearchKey' string does not require messy
   * type conversion and related exception handling in case of invalid values
   * The performance hit is negligible as this affects only Int/Boolean fields
   *
   * No indexes are built for 'url', 'createdAt' & 'lastLoginAt' fields as this
   * would be memory intensive. These fields will use conventional iterative search.
   */
  private val id2user = new mutable.HashMap[String, User] // Unique
  private val name2users = new mutable.HashMap[String, mutable.Set[User]] with mutable.MultiMap[String, User]
  private val alias2users = new mutable.HashMap[String, mutable.Set[User]] with mutable.MultiMap[String, User]
  private val email2user = new mutable.HashMap[String, User] // Unique
  private val phone2user = new mutable.HashMap[String, User] // Unique
  private val orgId2users = new mutable.HashMap[String, mutable.Set[User]] with mutable.MultiMap[String, User]
  private val locale2users = new mutable.HashMap[String, mutable.Set[User]] with mutable.MultiMap[String, User]
  private val timezone2users = new mutable.HashMap[String, mutable.Set[User]] with mutable.MultiMap[String, User]
  private val signature2users = new mutable.HashMap[String, mutable.Set[User]] with mutable.MultiMap[String, User]
  private val role2users = new mutable.HashMap[String, mutable.Set[User]] with mutable.MultiMap[String, User]
  private val tag2user = new mutable.HashMap[String, mutable.Set[User]] with mutable.MultiMap[String, User]
  private val active2users = new mutable.HashMap[String, mutable.Set[User]] with mutable.MultiMap[String, User]
  private val verified2users = new mutable.HashMap[String, mutable.Set[User]] with mutable.MultiMap[String, User]
  private val shared2users = new mutable.HashMap[String, mutable.Set[User]] with mutable.MultiMap[String, User]
  private val suspended2users = new mutable.HashMap[String, mutable.Set[User]] with mutable.MultiMap[String, User]
  private val externalId2user = new mutable.HashMap[String, User] // Unique

  // build indexes for all except 'url', 'createdAt' & 'lastLoginAt' fields
  for (user <- users) {
    id2user.put(user.id.toString, user)
    name2users.addBinding(user.name.getOrElse(EmptyKey).toLowerCase, user)
    alias2users.addBinding(user.alias.getOrElse(EmptyKey).toLowerCase, user)
    email2user.put(user.email.getOrElse(EmptyKey).toLowerCase, user)
    phone2user.put(user.phone.getOrElse(EmptyKey).toLowerCase, user)
    orgId2users.addBinding(user.organizationId.map(_.toString).getOrElse(EmptyKey), user)
    locale2users.addBinding(user.locale.getOrElse(EmptyKey).toLowerCase, user)
    timezone2users.addBinding(user.timezone.getOrElse(EmptyKey).toLowerCase, user)
    signature2users.addBinding(user.signature.getOrElse(EmptyKey).toLowerCase, user)
    role2users.addBinding(user.role.getOrElse(EmptyKey).toLowerCase, user)
    bind2map(user.tags, user, tag2user)
    active2users.addBinding(user.active.map(_.toString).getOrElse(EmptyKey), user)
    verified2users.addBinding(user.verified.map(_.toString).getOrElse(EmptyKey), user)
    shared2users.addBinding(user.shared.map(_.toString).getOrElse(EmptyKey), user)
    suspended2users.addBinding(user.suspended.map(_.toString).getOrElse(EmptyKey), user)
    externalId2user.put(user.externalId.getOrElse(EmptyKey).toLowerCase, user)
  }

  def getUser(userId: Int): Option[User] = id2user.get(userId.toString)

  def getOrgUsers(orgId: Int): Option[mutable.Set[User]] = orgId2users.get(orgId.toString)

  def search(field: String, key: String): List[User] = field match {
    case Id => id2user.get(key).map(List(_)).getOrElse(Nil)
    case Name => name2users.get(key.toLowerCase).map(_.toList).getOrElse(Nil)
    case Alias => alias2users.get(key.toLowerCase).map(_.toList).getOrElse(Nil)
    case Email => email2user.get(key.toLowerCase).map(List(_)).getOrElse(Nil)
    case Phone => phone2user.get(key.toLowerCase).map(List(_)).getOrElse(Nil)
    case OrganizationId => orgId2users.get(key.toLowerCase).map(_.toList).getOrElse(Nil)
    case Locale => locale2users.get(key.toLowerCase).map(_.toList).getOrElse(Nil)
    case Timezone => timezone2users.get(key.toLowerCase).map(_.toList).getOrElse(Nil)
    case Signature => signature2users.get(key.toLowerCase).map(_.toList).getOrElse(Nil)
    case Role => role2users.get(key.toLowerCase).map(_.toList).getOrElse(Nil)
    case Tags => tag2user.get(key.toLowerCase).map(_.toList).getOrElse(Nil)
    case Active => active2users.get(key.toLowerCase).map(_.toList).getOrElse(Nil)
    case Verified => verified2users.get(key.toLowerCase).map(_.toList).getOrElse(Nil)
    case Shared => shared2users.get(key.toLowerCase).map(_.toList).getOrElse(Nil)
    case Suspended => suspended2users.get(key.toLowerCase).map(_.toList).getOrElse(Nil)
    case CreatedAt => users.filter(user => isMatching(key, user.createdAt))
    case LastLoginAt => users.filter(user => isMatching(key, user.lastLoginAt))
    case ExternalId => externalId2user.get(key.toLowerCase).map(List(_)).getOrElse(Nil)
    case Url => users.filter(user => isMatching(key, user.url))
    case _ => throw new IllegalArgumentException("Invalid field '%s' detected !!".format(field))
  }

}
