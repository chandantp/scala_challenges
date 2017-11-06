Problem
=======
Using the provided data (tickets.json  and  users.json  and  organization.json) write a simple command line application 
to search the data and return the results in a human readable format. Feel free to use libraries or roll your own code 
as you see fit. Where the data exists, values from any related entities should be included in the results. The user 
should be able to search on any field, full value matching is fine (e.g. “mar” won’t return “mary”). The user should 
also be able to search for empty values, e.g. where description is empty.

Readme
======
- This application is written using Scala
- The app has a menu based interface
- To search, using menus choose 'collection' and 'field' and enter search key
- To match empty fields, use empty string as search key
- Full matches only (no partial matches)

Assumptions
-----------
- Only '_id' fields are mandatory
- All other fields are optional
- Unique fields = '_id', 'external_id', 'url', 'phone' & 'email'
- Search is NOT case sensitive
- Dates are considered as strings

Dependencies
------------
- SBT (Scala build tool)
- Scala compiler (taken care of by SBT)
- ScalaTest (taken care of by SBT)
- lift-json (taken care of by SBT)
- JDK 1.8

Test execution
--------------
From project root directory, run following command to run test cases

    $ sbt test

Running the App
---------------
From project root directory, run one of the following commands:

    $ sbt run
    $ sbt "-Djline.terminal=none" run    (in case the input is not echoing - jline library issue)
    $ sbt "-Djline.terminal=auto" run    (in case the input is not echoing - jline library issue)

Sample screens
--------------

#### Screen 1

    ### Zendesk Search: Main (search is NOT case-sensitive) ###
      1. organizations
      2. users
      3. tickets
      4. quit
    Choose [1..4]

#### Screen 2

    ### Zendesk Search: Organizations (search is NOT case-sensitive) ###
      1. id
      2. name
      3. details
      4. domains
      5. tags
      6. sharedTickets
      7. createdAt
      8. externalId
      9. url
      10. backToPreviousMenu
    Choose [1..10]
    2
    Enter search key (use empty string for matching empty fields): 
    enthaze
    ----------------------------------------------------------------------------------------------------
    Organizations found = 1
    ----------------------------------------------------------------------------------------------------
    Result: 1
    OrgId: 101, Name: Enthaze
    Details: MegaCorp, SharedTickets: false
    Domains: kage.com:ecratic.com:endipin.com:zentix.com
    Tags: Fulton:West:Rodriguez:Farley
    CreatedAt: 2016-05-21T11:10:28 -10:00
    ExternalId: 9270ed79-35eb-4a38-a46f-35725197ea8d
    Url: http://initech.zendesk.com/api/v2/organizations/101.json
    Users linked to this organization: 4
    => UserId: 5, Name: Loraine Pittman
    => UserId: 27, Name: Haley Farmer
    => UserId: 29, Name: Herrera Norman
    => UserId: 23, Name: Francis Bailey
    Tickets linked to this organization: 4
    => TicketId: 27c447d9-cfda-4415-9a72-d5aa12942cf1, Subject: A Problem in Guyana
    => TicketId: c22aaced-7faa-4b5c-99e5-1a209500ff16, Subject: A Problem in Ethiopia
    => TicketId: b07a8c20-2ee5-493b-9ebf-f6321b95966e, Subject: A Drama in Portugal
    => TicketId: 89255552-e9a2-433b-970a-af194b3a39dd, Subject: A Problem in Turks and Caicos Islands
    ----------------------------------------------------------------------------------------------------

#### Screen 3

    ### Zendesk Search: Users (search is NOT case-sensitive) ###
      1. id
      2. name
      3. alias
      4. email
      5. phone
      6. organizationId
      7. locale
      8. timezone
      9. signature
      10. role
      11. tags
      12. active
      13. verified
      14. shared
      15. suspended
      16. createdAt
      17. lastLoginAt
      18. externalId
      19. url
      20. backToPreviousMenu
    Choose [1..20]
    2
    Enter search key (use empty string for matching empty fields): 
    Catalina simpson
    ----------------------------------------------------------------------------------------------------
    Users found = 1
    ----------------------------------------------------------------------------------------------------
    Result: 1
    UserId: 75, Name: Catalina Simpson
    Alias: Miss Rosanna, Email: rosannasimpson@flotonic.com, Phone: 8615-883-099
    Role: agent, Locale: zh-CN, Timezone: US Minor Outlying Islands, Signature: Don't Worry Be Happy!
    Active: false, Verified: true, Shared: true, Suspended: true
    Tags: Veguita:Navarre:Elizaville:Beaulieu
    CreatedAt: 2016-06-07T09:18:00 -10:00
    LastLoginAt: 2012-10-15T12:36:41 -11:00
    ExternalId: 0db0c1da-8901-4dc3-a469-fe4b500d0fca
    Url: http://initech.zendesk.com/api/v2/users/75.json
    User organization:
    => OrgId: 119, Name: Multron
    User submitted tickets: 2
    => TicketId: dcb9143e-cb17-49ea-a9be-abf6989bd2d4, Subject: A Problem in Svalbard and Jan Mayen Islands
    => TicketId: 5c66cef0-7abc-46df-b487-5f8eb6208422, Subject: A Problem in Switzerland
    User assigned tickets: 1
    => TicketId: 25d9edca-7756-4d28-8fdd-f16f1532f6ab, Subject: A Problem in Cyprus
    ----------------------------------------------------------------------------------------------------

#### Screen 4

    ### Zendesk Search: Tickets (search is NOT case-sensitive) ###
      1. id
      2. subject
      3. type
      4. priority
      5. status
      6. submitterId
      7. assigneeId
      8. organizationId
      9. hasIncidents
      10. via
      11. tags
      12. createdAt
      13. dueAt
      14. externalId
      15. url
      16. description
      17. backToPreviousMenu
    Choose [1..17]
    2
    Enter search key (use empty string for matching empty fields): 
    A Catastrophe in Micronesia
    ----------------------------------------------------------------------------------------------------
    Tickets found = 1
    ----------------------------------------------------------------------------------------------------
    Result: 1
    TicketId: 1a227508-9f39-427c-8f57-1b72f3fab87c
    Subject: A Catastrophe in Micronesia
    Type: incident, Priority: low, Status: hold
    HasIncidents: false, Via: chat
    Tags: Puerto Rico:Idaho:Oklahoma:Louisiana
    CreatedAt: 2016-04-14T08:32:31 -10:00
    DueAt: 2016-08-15T05:37:32 -10:00
    ExternalId: 3e5ca820-cd1f-4a02-a18f-11b18e7bb49a
    Url: http://initech.zendesk.com/api/v2/tickets/1a227508-9f39-427c-8f57-1b72f3fab87c.json
    Description: Aliquip excepteur fugiat ex minim ea aute eu labore. Sunt eiusmod esse eu non commodo est veniam consequat.
    Ticket organization
    => OrgId: 112, Name: Quilk
    Ticket submitter
    => UserId: 71, Name: Prince Hinton
    Ticket assignee
    => UserId: 38, Name: Elma Castro
    ----------------------------------------------------------------------------------------------------
