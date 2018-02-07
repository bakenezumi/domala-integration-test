package domala.it.mysql

import domala._
import domala.it.SelectBenchMarkTestSuite
import domala.jdbc.Config
import org.scalatest.FunSuite

class MysqlSelectBenchMarkTestSuite extends SelectBenchMarkTestSuite {
  override val config: Config = MysqlTestConfig.getForBenchMark
  private[this] val baseConfig = MysqlTestConfig.get

  override def beforeAll(): Unit = {
    Required {
      script"create database mysql_bench" (baseConfig).execute()
    }(baseConfig)
    super.beforeAll()
  }

  override def afterAll(): Unit = {
    super.afterAll()
    Required {
      script"drop database mysql_bench" (baseConfig).execute()
    }(baseConfig)
  }
}
