package com.zdesk.search

import com.zdesk.search.services.Utils._

import scala.collection.mutable

import org.scalatest.{BeforeAndAfter, FunSuite}

class UtilsTest extends FunSuite with BeforeAndAfter {

  val Key = "somekey"
  val Value = "somevalue"
  val map = new mutable.HashMap[String, mutable.Set[String]] with mutable.MultiMap[String, String]

  before {
    map.clear
  }

  test("bind2Map() binds 'somevalue' to empty string when passed keys = None") {
    bind2map(None, Value, map)
    assert(map.get(EmptyKey) === Some(Set(Value)))
  }

  test("bind2Map() binds 'somevalue' to empty string when passed keys = Option(Nil)") {
    bind2map(Some(Nil), Value, map)
    assert(map.get(EmptyKey) === Some(Set(Value)))
  }

  test("bind2Map() binds 'somevalue' to 'somekey' when passed keys = Some(List('somekey'))") {
    bind2map(Some(List(Key)), Value, map)
    assert(map.get(Key) === Some(Set(Value)))
  }

  test("empty search key matches with empty field or list") {
    assert(isMatching(EmptyKey, None))
    assert(isMatching(EmptyKey, Nil))
  }

  test("empty search key != non-empty Int/Option(Int) field") {
    assert(!isMatching(EmptyKey, 1))
    assert(!isMatching(EmptyKey, Some(1)))
  }

  test("empty search key != non-empty Boolean/Option(Boolean) field") {
    assert(!isMatching(EmptyKey, true))
    assert(!isMatching(EmptyKey, Some(true)))
  }

  test("empty search key != non-empty String/Option(String) field") {
    assert(!isMatching(EmptyKey, "Alice"))
    assert(!isMatching(EmptyKey, Some("Alice")))
  }

  test("empty search key != non-empty List[String]/Option(List[String]) field") {
    assert(!isMatching(EmptyKey, List("Alice")))
    assert(!isMatching(EmptyKey, Some(List("Alice"))))
  }

  test("search key matches Int/Option(Int) field value") {
    assert(isMatching("1", 1))
    assert(isMatching("1", Some(1)))
  }

  test("search key matches Boolean/Option(Boolean) field value") {
    assert(isMatching("true", true))
    assert(isMatching("true", Some(true)))
  }

  test("search key matches String/Option(String) field value") {
    assert(isMatching("alice", "Alice"))
    assert(isMatching("alice", Some("Alice")))
  }

  test("search key matches a value in List[String]/Option(List[String]) field") {
    assert(isMatching("alice", List("Alice")))
    assert(isMatching("alice", Some(List("Alice"))))
  }

}
