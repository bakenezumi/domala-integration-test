package domala.it.mysql

import domala.it.SelectBenchMarkTestSuite
import domala.jdbc.Config

class MysqlSelectBenchMarkTestSuite extends SelectBenchMarkTestSuite {
  override val config: Config = MysqlTestConfig.get

}
