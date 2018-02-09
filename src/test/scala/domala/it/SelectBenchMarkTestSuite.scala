package domala.it

import domala._
import domala.it.dao._
import domala.it.entity._
import domala.it.holder._
import domala.jdbc.{Config, EntityManager}
import org.scalatest.{BeforeAndAfterAll, FunSuiteLike}

trait SelectBenchMarkTestSuite extends FunSuiteLike with BeforeAndAfterAll {
  implicit val config: Config

  lazy private[this] val dao: PersonDao = PersonDao.impl

  private[this] val SIZE = 10000
  private[this] val initialEntities = (1 to SIZE).map(
    i =>
      Person(ID(i),
             Some(PersonName("name" + i)),
             Some(Age(i * 2)),
             Address(CityName("city" + i), StreetName("street" + i)),
             Some(ID(1)),
             0))

  override def beforeAll(): Unit = {
    Required {
      dao.create()
      EntityManager.batchDelete(dao.selectAll())
      EntityManager.batchInsert(initialEntities)
    }
  }

  override def afterAll(): Unit = {
    Required {
      dao.drop()
    }
  }

  test("warm-up") {
    Required {
      dao.selectAllSeqMap()
      dao.selectAll()
    }
  }

  test("map select by interpolation") {
    Required {
      val result = select"select * from person order by id".getMapList
      assert(result.lengthCompare(SIZE) == 0)
    }
  }

  test("entity select by interpolation") {
    Required {
      val result = select"select * from person order by id".getList[Person]
      assert(result.lengthCompare(SIZE) == 0)
    }
  }

  test("entity iterator select by interpolation") {
    Required {
      val result = select"select * from person order by id"
        .apply[Person, Int](_.foldLeft(0)((x, _) => x + 1))
      assert(result == SIZE)
    }
  }

  test("map select by generated dao") {
    Required {
      val result = dao.selectAll()
      assert(result.lengthCompare(SIZE) == 0)
    }
  }

  test("entity select by generated dao") {
    Required {
      val result = dao.selectAll()
      assert(result.lengthCompare(SIZE) == 0)
    }
  }

  test("entity iterator select by generated dao") {
    Required {
      val result = dao.selectAllIterator(_.foldLeft(0)((x, _) => x + 1))
      assert(result == SIZE)
    }
  }

}
