package com.owlike.genson

import com.owlike.genson.reflect.RenamingPropertyNameResolver
import org.scalatest.{FunSuite, Matchers}

case class TestEntity(name: String, date: Long, amount: Int)
case class TestEntity2(name: String, date: Long, amount: Int)

class ScalaPropertyFilteringTest extends FunSuite with Matchers {

  test("test filtering out a property across all classes by name") {
    val genson = new ScalaGenson(new GensonBuilder().withBundle(ScalaBundle()).exclude("amount").create())
    val expected = """{"date":100,"name":"Hi"}"""

    genson.toJson( TestEntity("Hi",100L,10) ) should equal(expected)
    genson.toJson( TestEntity2("Hi",100L,10) ) should equal(expected)
  }

  test("test filtering out a property for specific class by name") {
    val genson = new ScalaGenson(new GensonBuilder().withBundle(ScalaBundle()).exclude("amount",classOf[TestEntity]).create())

    genson.toJson( TestEntity("Hi",100L,10) ) should equal("""{"date":100,"name":"Hi"}""")
    genson.toJson( TestEntity2("Hi",100L,10) ) should equal("""{"amount":10,"date":100,"name":"Hi"}""")
  }

  test("test filtering out a property across all classes by type") {
    val genson = new ScalaGenson(new GensonBuilder().withBundle(ScalaBundle()).exclude(classOf[String]).create())

    genson.toJson( TestEntity("Hi",100L,10) ) should equal("""{"amount":10,"date":100}""")
  }


  /* !!!FAILS!!!!

  test("test renaming a property across all classes") {
    val genson = new ScalaGenson(new GensonBuilder().withBundle(ScalaBundle()).`with`(new RenamingPropertyNameResolver("name", null, classOf[String], "yourName")).create())
    val expected = """{"amount":10,"date":100,"yourName":"Hi"}"""

    genson.toJson( TestEntity("Hi",100L,10) ) should equal(expected)
    genson.toJson( TestEntity2("Hi",100L,10) ) should equal(expected)
  }

  test("test renaming a property for specific class") {
    val genson = new ScalaGenson(new GensonBuilder().withBundle(ScalaBundle()).rename("amount",classOf[TestEntity],"quantity").create())

    genson.toJson( TestEntity("Hi",100L,10) ) should equal("""{"quantity":10,"date":100,"name":"Hi"}""")
    genson.toJson( TestEntity2("Hi",100L,10) ) should equal("""{"amount":10,"date":100,"name":"Hi"}""")
  }*/
}
