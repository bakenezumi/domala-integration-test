package domala.it.postgres

import domala._
import domala.it.SelectBenchMarkTestSuite
import domala.jdbc.Config
import org.scalatest.FunSuite

class PostgresSelectBenchMarkTestSuite extends SelectBenchMarkTestSuite {
  override implicit val config: Config = PostgresTestConfig.getForBenchMark
  private[this] val baseConfig = PostgresTestConfig.get

  override def beforeAll(): Unit = {
    Required {
      script"create schema bench" (baseConfig).execute()
    }(baseConfig)
    super.beforeAll()
  }

  override def afterAll(): Unit = {
    super.afterAll()
    Required {
      script"drop schema bench" (baseConfig).execute()
    }(baseConfig)
  }

}
