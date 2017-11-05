package com.zdesk.search

import com.zdesk.search.services.SearchService.{loadData, search}

import io.StdIn.readLine

//
// Menu based interface for search
//
object MainApp {

  val MainMenu = "Main"
  val OrgsMenu = "Organizations"
  val UsersMenu = "Users"
  val TicketsMenu = "Tickets"

  val OrgFields = "id,name,details,domains,tags,sharedTickets,createdAt,externalId,url".split(",")
  val UserFields = ("id,name,alias,email,phone,organizationId,locale,timezone,signature,role," +
    "tags,active,verified,shared,suspended,createdAt,lastLoginAt,externalId,url").split(",")
  val TicketFields = ("id,subject,type,priority,status,submitterId,assigneeId,organizationId," +
    "hasIncidents,via,tags,createdAt,dueAt,externalId,url,description").split(",")

  val Menus = Array(OrgsMenu, UsersMenu, TicketsMenu)
  val MainMenuOptions = "organizations,users,tickets,quit"
  val OrgsMenuOptions = OrgFields.mkString(",") + ",backToPreviousMenu"
  val UsersMenuOptions = UserFields.mkString(",") + ",backToPreviousMenu"
  val TicketsMenuOptions = TicketFields.mkString(",") + ",backToPreviousMenu"

  val SearchKeyPrompt = "Enter search key (use '#null#' for matching empty fields): "
  val UnknownOptionPrompt = "Unknown option, try again..."

  def main(args: Array[String]): Unit = {
    var currentMenu = MainMenu
    var done = false
    loadData

    while(!done) {
      try {
        currentMenu match {
          case MainMenu => {
            displayMenu(MainMenu, MainMenuOptions)
            readLine.toInt match {
              case i if i >= 1 && i <= 3 => currentMenu = Menus(i-1)
              case 4 => done = true
              case _ => println(UnknownOptionPrompt)
            }
          }
          case OrgsMenu => {
            displayMenu(OrgsMenu, OrgsMenuOptions)
            readLine.toInt match {
              case i if i >= 1 && i <= 9 => {
                println(SearchKeyPrompt)
                search("organizations", OrgFields(i-1), readLine.trim)
              }
              case 10 => currentMenu = MainMenu
              case _ => println(UnknownOptionPrompt)
            }
          }
          case UsersMenu => {
            displayMenu(UsersMenu, UsersMenuOptions)
            readLine.toInt match {
              case i if i >= 1 && i <= 19 => {
                println(SearchKeyPrompt)
                search("users", UserFields(i-1), readLine.trim)
              }
              case 20 => currentMenu = MainMenu
              case _ => println(UnknownOptionPrompt)
            }
          }
          case TicketsMenu => {
            displayMenu(TicketsMenu, TicketsMenuOptions)
            readLine.toInt match {
              case i if i >= 1 && i <= 16 => {
                println(SearchKeyPrompt)
                search("tickets", TicketFields(i-1), readLine.trim)
              }
              case 17 => currentMenu = MainMenu
              case _ => println(UnknownOptionPrompt)
            }
          }
          case _ => println(UnknownOptionPrompt)
        }
      } catch {
        case e: Exception => e.printStackTrace
      }
    }
  }

  def displayMenu(level: String, menuOptions: String) = {
    println("\n### Zendesk Search: %s (search is NOT case-sensitive) ###".format(level))
    val menuItems = menuOptions.split(",")
    menuItems.zipWithIndex.foreach { case (item, index) => {
        println("  %d. %s".format(index + 1, item))
    }}
    println("Choose [1..%d]".format(menuItems.size))
  }

}
