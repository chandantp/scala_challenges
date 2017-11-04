package com.zdesk.search.services

object SearchService {

  val EmptyKey = "#null#"

  def showUsage(message: Option[String] = None) {
    if (message.isDefined) {
      println("\n" + message.get + "\n\nRefer to the usage below for more information...\n")
    }

    println("""scala ./search.jar <collection> <field> <key>
      |  <collection> : collection to be searched (Mandatory)
      |                    collection = organizations | users | tickets
      |       <field> : field to be searched which should be one of organization, user or ticket fields (Mandatory)
      |                    Org fields = id,name,details,domains,tags,sharedTickets,createdAt,externalId,url
      |                   User fields = id,name,alias,email,phone,organizationId,locale,timezone,signature,role
      |                                 tags,active,verified,shared,suspended,createdAt,lastLoginAt,externalId,url
      |                 Ticket fields = id,subject,type,priority,status,submitterId,assigneeId,organizationId
      |                                 hasIncidents,via,tags,createdAt,dueAt,externalId,url,description
      |         <key> : search key (Mandatory)
      |                    key = #null# is used to match empty fields"""
      .stripMargin + "\n")
  }

  def parse(args: Array[String]) = args.size match {
    case 3 => (args(0), args(1), args(2))
    case _ => throw new IllegalArgumentException("Invalid argument size '%d' detected !!".format(args.size))
  }

  def loadData = {
    OrganizationService.init()
    UserService.init()
    TicketService.init()
  }

  def search(collection: String, field: String, key: String): List[AnyRef] = collection match {
    case "organizations" => OrganizationService.search(field, key)
    case "users" => UserService.search(field, key)
    case "tickets" => TicketService.search(field, key)
    case _ => throw new IllegalArgumentException("Invalid collection '%s' detected !!".format(collection))
  }

  def isMatching[T](key: String, fieldValue: T): Boolean = fieldValue match {
    case i: Int => if (key.equalsIgnoreCase(EmptyKey)) false else key.equals(i.toString)
    case b: Boolean => if (key.equalsIgnoreCase(EmptyKey)) false else key.equalsIgnoreCase(b.toString)
    case str: String => if (key.equalsIgnoreCase(EmptyKey)) false else key.equalsIgnoreCase(str)
    case list: List[_] => key match {
      case EmptyKey => list.isEmpty // Match depends of whether list is empty or not as search is for empty field
      case _ => if (list.isEmpty) false else list.exists(s => isMatching(key, s)) // No match as list is empty and search key is not empty
    }
    case opt: Option[_] => opt match {
      case None => key.equalsIgnoreCase(EmptyKey)
      case Some(optValue) => isMatching(key, optValue)
    }
    case _ => throw new Exception("Unexpected field type %s".format(fieldValue))
  }

}
