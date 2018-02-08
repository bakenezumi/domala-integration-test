package domala.it.postgres

import domala.it.SelectBenchMarkTestSuite
import domala.jdbc.Config

class PostgresSelectBenchMarkTestSuite extends SelectBenchMarkTestSuite {
  override val config: Config = PostgresTestConfig.get

}
