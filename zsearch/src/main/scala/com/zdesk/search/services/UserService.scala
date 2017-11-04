package com.zdesk.search.services

import com.zdesk.search.model.User
import net.liftweb.json.{DefaultFormats, JField, parse}

import scala.collection.mutable

object UserService {

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

}
