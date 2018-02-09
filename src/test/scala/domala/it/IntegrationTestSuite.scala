package domala.it

import domala.Required
import domala.it.dao._
import domala.it.entity._
import domala.it.holder._
import domala.jdbc.{BatchResult, Config, SelectOptions}
import org.scalatest._

trait IntegrationTestSuite extends FunSuiteLike with BeforeAndAfter {
  implicit val config: Config

  lazy private[this] val dao: PersonDao = PersonDao.impl

  before {
    Required {
      dao.create()
    }
  }

  after {
    Required {
      dao.drop()
    }
  }

  test("select by 1 basic parameter to return optional entity") {
    Required {
      assert(
        dao.selectById(ID(1)) == Some(
          Person(ID(1),
                 Some(PersonName("SMITH")),
                 Some(Age(10)),
                 Address(CityName("Tokyo"), StreetName("Yaesu")),
                 Some(ID(2)),
                 0)))
      assert(dao.selectById(ID(5)) == None)
    }
  }

  test("select to return Int") {
    Required {
      assert(dao.selectCount == 2)
    }
  }

  test("select to return Seq") {
    Required {
      assert(
        dao.selectAll == Seq(
          Person(ID(1),
                 Some(PersonName("SMITH")),
                 Some(Age(10)),
                 Address(CityName("Tokyo"), StreetName("Yaesu")),
                 Some(ID(2)),
                 0),
          Person(ID(2),
                 Some(PersonName("ALLEN")),
                 Some(Age(20)),
                 Address(CityName("Kyoto"), StreetName("Karasuma")),
                 Some(ID(1)),
                 0)
        ))
    }
  }

  test("select to return nullable entity") {
    Required {
      assert(
        dao.selectByIdNullable(ID(1)) ==
          Person(ID(1),
                 Some(PersonName("SMITH")),
                 Some(Age(10)),
                 Address(CityName("Tokyo"), StreetName("Yaesu")),
                 Some(ID(2)),
                 0))
      assert(dao.selectByIdNullable(ID(5)) == null)
    }
  }

  test("join select") {
    Required {
      assert(
        dao.selectWithDepartmentById(ID(1)) ==
          Some(
            PersonDepartment(ID(1),
                             PersonName("SMITH"),
                             Some(ID(2)),
                             Some(PersonName("SALES")))))
    }
  }

  test("join select to embedded entity") {
    Required {
      assert(
        dao.selectWithDepartmentEmbeddedById(ID(1)) ==
          Some(
            PersonDepartmentEmbedded(ID(1),
                                     PersonName("SMITH"),
                                     Department(ID(2),
                                                DepartmentName("SALES")))))
    }
  }

  test("select stream no param") {
    Required {
      assert(dao.selectAllStream { stream =>
        stream.size
      } == 2)
    }
  }

  test("select iterator no param") {
    Required {
      assert(dao.selectAllIterator { it =>
        it.size
      } == 2)
    }
  }

  test("select stream with one param") {
    Required {
      assert(dao.selectByIdStream(ID(1)) { stream =>
        stream.headOption.map(_.address)
      } == Some(Address(CityName("Tokyo"), StreetName("Yaesu"))))
      assert(
        dao
          .selectByIdStream(ID(5)) { stream =>
            stream.headOption.map(_.address)
          }
          .isEmpty)
    }
  }

  test("select iterator with one param") {
    Required {
      assert(dao.selectByIdIterator(ID(1)) { it =>
        it.toStream.headOption.map(_.address)
      } == Some(Address(CityName("Tokyo"), StreetName("Yaesu"))))
      assert(
        dao
          .selectByIdIterator(ID(5)) { it =>
            it.toStream.headOption.map(_.address)
          }
          .isEmpty)
    }
  }

  test("select Sequential Map") {
    Required {
      assert(
        dao.selectAllSeqMap() == Seq(
          Map("id" -> 1,
              "name" -> "SMITH",
              "age" -> 10,
              "city" -> "Tokyo",
              "street" -> "Yaesu",
              "department_id" -> 2,
              "version" -> 0),
          Map("id" -> 2,
              "name" -> "ALLEN",
              "age" -> 20,
              "city" -> "Kyoto",
              "street" -> "Karasuma",
              "department_id" -> 1,
              "version" -> 0)
        ))
    }
  }

  test("select Single Map") {
    Required {
      assert(
        dao.selectByIdMap(ID(1)) ==
          Map("id" -> 1,
              "name" -> "SMITH",
              "age" -> 10,
              "city" -> "Tokyo",
              "street" -> "Yaesu",
              "department_id" -> 2,
              "version" -> 0))
      assert(dao.selectByIdMap(ID(5)) == Map.empty)
    }
  }

  test("select Option Map") {
    Required {
      assert(
        dao.selectByIdOptionMap(ID(1)) == Some(
          Map("id" -> 1,
              "name" -> "SMITH",
              "age" -> 10,
              "city" -> "Tokyo",
              "street" -> "Yaesu",
              "department_id" -> 2,
              "version" -> 0)))
      assert(dao.selectByIdOptionMap(ID(5)).isEmpty)
    }
  }

  test("select option domain") {
    Required {
      assert(dao.selectNameById(ID(1)) == Some(PersonName("SMITH")))
      assert(dao.selectNameById(ID(99)).isEmpty)
    }
  }

  test("select nullable domain") {
    Required {
      assert(dao.selectNameByIdNullable(ID(1)) == PersonName("SMITH"))
      assert(dao.selectNameByIdNullable(ID(99)) == PersonName(null))
    }
  }

  test("select domain list") {
    Required {
      assert(dao.selectNames == Seq(PersonName("SMITH"), PersonName("ALLEN")))
    }
  }

  test("select map stream") {
    Required {
      assert(dao.selectAllStreamMap { stream =>
        stream.size
      } == 2)
    }
  }

  test("select map iterator") {
    Required {
      assert(dao.selectAllIteratorMap { it =>
        it.size
      } == 2)
    }
  }

  test("select domain stream") {
    Required {
      assert(dao.selectNameStream { stream =>
        assert(stream.toList == List(PersonName("SMITH"), PersonName("ALLEN")))
        stream.size
      } == 2)
    }
  }

  test("select domain iterator") {
    Required {
      assert(dao.selectNameIterator { it =>
        val list = it.toList
        assert(list == List(PersonName("SMITH"), PersonName("ALLEN")))
        list.size
      } == 2)
    }
  }

  test("select by builder") {
    Required {
      assert(dao.selectByIDBuilder(ID(1)) == "SMITH")
    }
  }

  test("insert from entity") {
    Required {
      dao.insert(
        Person(name = Some(PersonName("aaa")),
               age = Some(Age(5)),
               address = Address(CityName("bbb"), StreetName("ccc")),
               departmentId = Some(ID(1))))
      assert(dao.selectCount == 3)
      assert(
        dao.selectById(ID(3)) == Some(
          Person(ID(3),
                 Some(PersonName("aaa")),
                 Some(Age(5)),
                 Address(CityName("bbb"), StreetName("ccc")),
                 Some(ID(1)),
                 1)))
    }
  }

  test("update by entity") {
    Required {
      dao.update(
        Person(id = ID(1),
               name = Some(PersonName("aaa")),
               age = Some(Age(5)),
               address = Address(CityName("bbb"), StreetName("ccc")),
               departmentId = Some(ID(2)),
               version = 0))
      assert(
        dao.selectById(ID(1)) == Some(
          Person(ID(1),
                 Some(PersonName("aaa")),
                 Some(Age(5)),
                 Address(CityName("bbb"), StreetName("ccc")),
                 Some(ID(2)),
                 1))) // @Version
    }
  }

  test("delete by entity") {
    Required {
      dao.selectById(ID(1)).foreach(dao.delete)
      assert(dao.selectCount() == 1)
    }
  }

  test("batch insert") {
    Required {
      val BatchResult(counts, entities) = dao.batchInsert(
        List(
          Person(name = Some(PersonName("aaa")),
                 age = Some(Age(5)),
                 address = Address(CityName("bbb"), StreetName("ccc")),
                 departmentId = Some(ID(1))),
          Person(name = Some(PersonName("ddd")),
                 age = Some(Age(10)),
                 address = Address(CityName("eee"), StreetName("fff")),
                 departmentId = Some(ID(2)))
        ))
      assert(counts sameElements Array(1, 1))
      assert(
        entities == Seq(
          Person(ID(3),
                 Some(PersonName("aaa")),
                 Some(Age(5)),
                 Address(CityName("bbb"), StreetName("ccc")),
                 Some(ID(1)),
                 1),
          Person(ID(4),
                 Some(PersonName("ddd")),
                 Some(Age(10)),
                 Address(CityName("eee"), StreetName("fff")),
                 Some(ID(2)),
                 1)
        ))
      assert(dao.selectCount == 4)
      assert(
        dao.selectById(ID(3)) == Some(
          Person(ID(3),
                 Some(PersonName("aaa")),
                 Some(Age(5)),
                 Address(CityName("bbb"), StreetName("ccc")),
                 Some(ID(1)),
                 1)))
      assert(
        dao.selectById(ID(4)) == Some(
          Person(ID(4),
                 Some(PersonName("ddd")),
                 Some(Age(10)),
                 Address(CityName("eee"), StreetName("fff")),
                 Some(ID(2)),
                 1)))
    }
  }

  test("batch update") {
    Required {
      val BatchResult(counts, entities) = dao.batchUpdate(
        dao
          .selectAll()
          .map(entity => entity.copy(age = entity.age.map(_.grow))))
      assert(counts sameElements Array(1, 1))
      assert(
        entities == Seq(
          Person(ID(1),
                 Some(PersonName("SMITH")),
                 Some(Age(11)),
                 Address(CityName("Tokyo"), StreetName("Yaesu")),
                 Some(ID(2)),
                 1),
          Person(ID(2),
                 Some(PersonName("ALLEN")),
                 Some(Age(21)),
                 Address(CityName("Kyoto"), StreetName("Karasuma")),
                 Some(ID(1)),
                 1)
        ))
      assert(dao.selectAll == entities)
    }
  }

  test("batch delete") {
    Required {
      dao.batchDelete(dao.selectAll())
      assert(dao.selectCount() == 0)
    }
  }

  test("insert by Sql") {
    Required {
      dao.insertSql(
        Person(ID(3),
               Some(PersonName("aaa")),
               Some(Age(5)),
               Address(CityName("bbb"), StreetName("ccc")),
               Some(ID(1)),
               1),
        Person(ID(3),
               Some(PersonName("ddd")),
               Some(Age(10)),
               Address(CityName("eee"), StreetName("fff")),
               Some(ID(1)),
               2),
        3
      )
      assert(dao.selectCount == 3)
      assert(
        dao.selectById(ID(3)) == Some(
          Person(ID(3),
                 Some(PersonName("aaa")),
                 Some(Age(5)),
                 Address(CityName("eee"), StreetName("fff")),
                 Some(ID(2)),
                 3)))
    }
  }

  test("update by Sql") {
    Required {
      dao.updateSql(
        Person(ID(1),
               Some(PersonName("aaa")),
               Some(Age(5)),
               Address(CityName("bbb"), StreetName("ccc")),
               Some(ID(1)),
               1),
        Person(ID(3),
               Some(PersonName("ddd")),
               Some(Age(10)),
               Address(CityName("eee"), StreetName("fff")),
               Some(ID(1)),
               2),
        0
      )
      assert(
        dao.selectById(ID(1)) == Some(
          Person(ID(1),
                 Some(PersonName("aaa")),
                 Some(Age(5)),
                 Address(CityName("eee"), StreetName("fff")),
                 Some(ID(2)),
                 1)))
    }
  }

  test("delete by Sql") {
    Required {
      dao.deleteSql(
        Person(ID(1),
               Some(PersonName("aaa")),
               Some(Age(5)),
               Address(CityName("bbb"), StreetName("ccc")),
               Some(ID(1)),
               1),
        0
      )
      assert(dao.selectById(ID(1)).isEmpty)
    }
  }

  test("select by options") {
    val options = SelectOptions.get
    Required {
      assert(
        dao.selectAllByOption(options) == Seq(
          Person(ID[Person](1),
                 Some(PersonName("SMITH")),
                 Some(Age(10)),
                 Address(CityName("Tokyo"), StreetName("Yaesu")),
                 Some(ID(2)),
                 0),
          Person(ID(2),
                 Some(PersonName("ALLEN")),
                 Some(Age(20)),
                 Address(CityName("Kyoto"), StreetName("Karasuma")),
                 Some(ID(1)),
                 0)
        ))
      assert(options.getCount == -1)
      options.limit(1).count()
      assert(
        dao.selectAllByOption(options) == Seq(
          Person(ID(1),
                 Some(PersonName("SMITH")),
                 Some(Age(10)),
                 Address(CityName("Tokyo"), StreetName("Yaesu")),
                 Some(ID(2)),
                 0)
        ))
      assert(options.getCount == 2)
    }
  }

  test("batch insert by Sql") {
    Required {
      dao.batchInsertSql(
        Seq(
          Person(ID(3),
                 Some(PersonName("aaa")),
                 Some(Age(5)),
                 Address(CityName("bbb"), StreetName("ccc")),
                 Some(ID(1)),
                 1),
          Person(ID(4),
                 Some(PersonName("ddd")),
                 Some(Age(10)),
                 Address(CityName("eee"), StreetName("fff")),
                 Some(ID(1)),
                 1),
          Person(ID(5),
                 Some(PersonName("ggg")),
                 Some(Age(15)),
                 Address(CityName("hhh"), StreetName("iii")),
                 Some(ID(1)),
                 1)
        ))
      assert(dao.selectCount == 5)
      assert(
        dao.selectById(ID(3)) == Some(
          Person(ID(3),
                 Some(PersonName("aaa")),
                 Some(Age(5)),
                 Address(CityName("bbb"), StreetName("ccc")),
                 Some(ID(2)),
                 1)))
      assert(
        dao.selectById(ID(4)) == Some(
          Person(ID(4),
                 Some(PersonName("ddd")),
                 Some(Age(10)),
                 Address(CityName("eee"), StreetName("fff")),
                 Some(ID(2)),
                 1)))
      assert(
        dao.selectById(ID(5)) == Some(
          Person(ID(5),
                 Some(PersonName("ggg")),
                 Some(Age(15)),
                 Address(CityName("hhh"), StreetName("iii")),
                 Some(ID(2)),
                 1)))
    }
  }

  test("batch update by Sql") {
    Required {
      val entities = for (e <- dao.selectAll())
        yield e.copy(age = e.age.map(_.grow(10)))
      dao.batchUpdateSql(entities)
      assert(
        dao.selectAll == Seq(
          Person(ID(1),
                 Some(PersonName("SMITH")),
                 Some(Age(20)),
                 Address(CityName("Tokyo"), StreetName("Yaesu")),
                 Some(ID(2)),
                 1),
          Person(ID(2),
                 Some(PersonName("ALLEN")),
                 Some(Age(30)),
                 Address(CityName("Kyoto"), StreetName("Karasuma")),
                 Some(ID(2)),
                 1)
        )
      )
    }
  }

  test("batch delete by Sql") {
    Required {
      dao.batchDeleteSql(dao.selectAll())
      assert(dao.selectCount() == 0)
    }
  }
}
