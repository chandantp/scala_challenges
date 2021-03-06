package com.zdesk.search

import com.zdesk.search.services.UserService
import com.zdesk.search.services.Utils.EmptyKey

import org.scalatest.{BeforeAndAfter, FunSuite}

class UserServiceTest extends FunSuite with BeforeAndAfter {

  val userSvc = new UserService("src/test/resources/users.json")

  test("search() throws exception when an invalid field name is passed") {
    val thrown = intercept[IllegalArgumentException] {
      userSvc.search("blah", "dontcare")
    }
    assert(thrown.getMessage === "Invalid field 'blah' detected !!")
  }

  test("search() accepts valid field names without throwing exception") {
    val validFields = "id,name,alias,email,phone,organizationId,locale,timezone,signature,role," +
                      "tags,active,verified,shared,suspended,createdAt,lastLoginAt,externalId,url"
    validFields.split(",").foreach(field => userSvc.search(field, "dontcare"))
  }

  //
  // Id field tests
  //
  test("search for id = 1 in mandatory 'id' integer field is successful") {
    val users = userSvc.search("id", "1")
    assert(users.size === 1 && users(0).id === 1)
  }

  test("search for id = 999 in mandatory 'id' integer field is unsuccessful") {
    val users = userSvc.search("id", "999")
    assert(users.size === 0)
  }

  //
  // Name field tests
  //
  test("search for empty 'name' fields is unsuccessful") {
    val users = userSvc.search("name", EmptyKey)
    assert(users.size === 0)
  }

  test("search for name = 'not-found' in optional 'name' string field is unsuccessful") {
    val users = userSvc.search("name", "not-found")
    assert(users.size === 0)
  }

  test("search for name = 'name1' in optional 'name' string field is successful") {
    val users = userSvc.search("name", "name2 lname2")
    assert(users.size === 1 && users(0).name === Some("name2 lname2"))
  }

  //
  // Alias field tests
  //
  test("search for empty 'alias' fields is successful") {
    val users = userSvc.search("alias", EmptyKey)
    assert(users.size === 1)
  }

  test("search for name = 'not-found' in optional 'alias' string field is unsuccessful") {
    val users = userSvc.search("alias", "not-found")
    assert(users.size === 0)
  }

  test("search for alias = 'alias2' in optional 'alias' string field is successful") {
    val users = userSvc.search("alias", "alias2")
    assert(users.size === 1 && users(0).alias === Some("alias2"))
  }

  //
  // Email field tests
  //
  test("search for empty 'email' fields is successful") {
    val users = userSvc.search("email", EmptyKey)
    assert(users.size === 1)
  }

  test("search for email = 'not-found' in optional 'email' string field is unsuccessful") {
    val users = userSvc.search("email", "not-found")
    assert(users.size === 0)
  }

  test("search for email = 'name2@gmail.com' in optional 'email' string field is successful") {
    val users = userSvc.search("email", "name2@gmail.com")
    assert(users.size === 1 && users(0).email === Some("name2@gmail.com"))
  }

  //
  // Phone field tests
  //
  test("search for empty 'phone' fields is successful") {
    val users = userSvc.search("phone", EmptyKey)
    assert(users.size === 1)
  }

  test("search for phone = 'not-found' in optional 'phone' string field is unsuccessful") {
    val users = userSvc.search("phone", "not-found")
    assert(users.size === 0)
  }

  test("search for phone = '2222-222-222' in optional 'phone' string field is successful") {
    val users = userSvc.search("phone", "2222-222-222")
    assert(users.size === 1 && users(0).phone === Some("2222-222-222"))
  }

  //
  // OrganizationId field tests
  //
  test("search for empty 'organizationId' fields is successful") {
    val users = userSvc.search("organizationId", EmptyKey)
    assert(users.size === 1)
  }

  test("search for organizationId = 'not-found' in optional 'organizationId' string field is unsuccessful") {
    val users = userSvc.search("organizationId", "not-found")
    assert(users.size === 0)
  }

  test("search for organizationId = '1' in optional 'organizationId' string field is successful") {
    val users = userSvc.search("organizationId", "1")
    assert(users.size === 2 && users(0).organizationId === Some(1))
  }

  //
  // Locale field tests
  //
  test("search for empty 'locale' fields is successful") {
    val users = userSvc.search("locale", EmptyKey)
    assert(users.size === 1)
  }

  test("search for locale = 'not-found' in optional 'locale' string field is unsuccessful") {
    val users = userSvc.search("locale", "not-found")
    assert(users.size === 0)
  }

  test("search for locale = 'en-AU' in optional 'locale' string field is successful") {
    val users = userSvc.search("locale", "en-AU")
    assert(users.size === 2 && users(0).locale === Some("en-AU"))
  }

  //
  // Timezone field tests
  //
  test("search for empty 'timezone' fields is successful") {
    val users = userSvc.search("timezone", EmptyKey)
    assert(users.size === 1)
  }

  test("search for timezone = 'not-found' in optional 'timezone' string field is unsuccessful") {
    val users = userSvc.search("timezone", "not-found")
    assert(users.size === 0)
  }

  test("search for timezone = 'Australia' in optional 'timezone' string field is successful") {
    val users = userSvc.search("timezone", "Australia")
    assert(users.size === 2 && users(0).timezone === Some("Australia"))
  }

  //
  // Role field tests
  //
  test("search for empty 'role' fields is successful") {
    val users = userSvc.search("role", EmptyKey)
    assert(users.size === 1)
  }

  test("search for role = 'not-found' in optional 'role' string field is unsuccessful") {
    val users = userSvc.search("role", "not-found")
    assert(users.size === 0)
  }

  test("search for role = 'admin' in optional 'role' string field is successful") {
    val users = userSvc.search("role", "admin")
    assert(users.size === 2 && users(0).role === Some("admin"))
  }

  //
  // Signature field tests
  //
  test("search for empty 'signature' fields is successful") {
    val users = userSvc.search("signature", EmptyKey)
    assert(users.size === 1)
  }

  test("search for signature = 'not-found' in optional 'signature' string field is unsuccessful") {
    val users = userSvc.search("signature", "not-found")
    assert(users.size === 0)
  }

  test("search for signature = 'signature1' in optional 'signature' string field is successful") {
    val users = userSvc.search("signature", "signature1")
    assert(users.size === 1 && users(0).signature === Some("signature1"))
  }

  //
  // Tags field tests
  //
  test("search for empty 'tags' fields is successful") {
    val users = userSvc.search("tags", EmptyKey)
    assert(users.size === 1)
  }

  test("search for tags = 'not-found' in optional 'tags' List[string] field is unsuccessful") {
    val users = userSvc.search("tags", "not-found")
    assert(users.size === 0)
  }

  test("search for tags = 'tags1' in optional 'tags' List[string] field is successful") {
    val users = userSvc.search("tags", "tags1")
    assert(users.size == 1 && users(0).tags === Some(List("tags1", "tags2")))
  }

  //
  // Active field tests
  //
  test("search for empty 'active' fields is successful") {
    val orgs = userSvc.search("active", EmptyKey)
    assert(orgs.size === 1)
  }

  test("search for active = 'invalid-value' in optional 'active' Boolean field is unsuccessful") {
    val orgs = userSvc.search("active", "invalid-value")
    assert(orgs.size === 0)
  }

  test("search for active = 'true' in optional 'active' Boolean field is successful") {
    val orgs = userSvc.search("active", "true")
    assert(orgs.size === 1)
  }

  test("search for active = 'false' in optional 'active' Boolean field is successful") {
    val orgs = userSvc.search("active", "false")
    assert(orgs.size == 2)
  }

  //
  // Verified,shared,suspended field tests
  //
  test("search for empty 'verified' fields is successful") {
    val orgs = userSvc.search("verified", EmptyKey)
    assert(orgs.size === 1)
  }

  test("search for verified = 'invalid-value' in optional 'verified' Boolean field is unsuccessful") {
    val orgs = userSvc.search("verified", "invalid-value")
    assert(orgs.size === 0)
  }

  test("search for verified = 'true' in optional 'verified' Boolean field is successful") {
    val orgs = userSvc.search("verified", "true")
    assert(orgs.size === 1)
  }

  test("search for verified = 'false' in optional 'verified' Boolean field is successful") {
    val orgs = userSvc.search("verified", "false")
    assert(orgs.size == 2)
  }

  //
  // Shared field tests
  //
  test("search for empty 'shared' fields is successful") {
    val orgs = userSvc.search("shared", EmptyKey)
    assert(orgs.size === 1)
  }

  test("search for shared = 'invalid-value' in optional 'shared' Boolean field is unsuccessful") {
    val orgs = userSvc.search("shared", "invalid-value")
    assert(orgs.size === 0)
  }

  test("search for shared = 'true' in optional 'shared' Boolean field is successful") {
    val orgs = userSvc.search("shared", "true")
    assert(orgs.size === 2)
  }

  test("search for shared = 'false' in optional 'shared' Boolean field is successful") {
    val orgs = userSvc.search("shared", "false")
    assert(orgs.size == 1)
  }

  //
  // Suspended field tests
  //
  test("search for empty 'suspended' fields is successful") {
    val orgs = userSvc.search("suspended", EmptyKey)
    assert(orgs.size === 1)
  }

  test("search for suspended = 'invalid-value' in optional 'suspended' Boolean field is unsuccessful") {
    val orgs = userSvc.search("suspended", "invalid-value")
    assert(orgs.size === 0)
  }

  test("search for suspended = 'true' in optional 'suspended' Boolean field is successful") {
    val orgs = userSvc.search("suspended", "true")
    assert(orgs.size === 2)
  }

  test("search for suspended = 'false' in optional 'suspended' Boolean field is successful") {
    val orgs = userSvc.search("suspended", "false")
    assert(orgs.size == 1)
  }

  //
  // CreatedAt field tests
  //
  test("search for empty 'createdAt' fields is successful") {
    val users = userSvc.search("createdAt", EmptyKey)
    assert(users.size === 1)
  }

  test("search for createdAt = 'not-found' in optional 'createdAt' string field is unsuccessful") {
    val users = userSvc.search("createdAt", "not-found")
    assert(users.size === 0)
  }

  test("search for createdAt = 'createdAt3' in optional 'createdAt' string field is successful") {
    val users = userSvc.search("createdAt", "createdAt3")
    assert(users.size === 1 && users(0).createdAt === Some("createdAt3"))
  }

  //
  // LastLoginAt field tests
  //
  test("search for empty 'lastLoginAt' fields is successful") {
    val users = userSvc.search("lastLoginAt", EmptyKey)
    assert(users.size === 1)
  }

  test("search for lastLoginAt = 'not-found' in optional 'lastLoginAt' string field is unsuccessful") {
    val users = userSvc.search("lastLoginAt", "not-found")
    assert(users.size === 0)
  }

  test("search for lastLoginAt = 'lastLoginAt3' in optional 'lastLoginAt' string field is successful") {
    val users = userSvc.search("lastLoginAt", "lastLoginAt3")
    assert(users.size === 1 && users(0).lastLoginAt === Some("lastLoginAt3"))
  }

  //
  // ExternalId field tests
  //
  test("search for empty 'externalId' fields is successful") {
    val users = userSvc.search("externalId", EmptyKey)
    assert(users.size === 1)
  }

  test("search for externalId = 'not-found' in optional 'externalId' string field is unsuccessful") {
    val users = userSvc.search("externalId", "not-found")
    assert(users.size === 0)
  }

  test("search for externalId = 'externalId2' in optional 'externalId' string field is successful") {
    val users = userSvc.search("externalId", "externalId2")
    assert(users.size === 1 && users(0).externalId === Some("externalId2"))
  }

  //
  // Url field tests
  //
  test("search for empty 'url' fields is successful") {
    val users = userSvc.search("url", EmptyKey)
    assert(users.size === 1)
  }

  test("search for url = 'not-found' in optional 'url' string field is unsuccessful") {
    val users = userSvc.search("url", "not-found")
    assert(users.size === 0)
  }

  test("search for url = 'url2' in optional 'url' string field is successful") {
    val users = userSvc.search("url", "url2")
    assert(users.size === 1 && users(0).url === Some("url2"))
  }

  test("getOrgUsers returns None for org with no users") {
    assert(userSvc.getOrgUsers(4) == None)
  }

  test("getOrgUsers return users for org containing users") {
    val users = userSvc.getOrgUsers(3).get
    assert(users.size == 1 && users.toList(0).id == 1)
  }
}
