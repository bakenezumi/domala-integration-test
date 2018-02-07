package domala.it.mysql

import domala.it.IntegrationTestSuite
import domala.jdbc.Config
import org.scalatest._

class MysqlIntegrationTestSuite
    extends IntegrationTestSuite
    with BeforeAndAfter {
  override val config: Config = MysqlTestConfig.get

}
