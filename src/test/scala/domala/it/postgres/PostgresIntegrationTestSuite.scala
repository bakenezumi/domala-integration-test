package domala.it.postgres

import domala.it.IntegrationTestSuite
import domala.jdbc.Config
import org.scalatest._

class PostgresIntegrationTestSuite
    extends IntegrationTestSuite
    with BeforeAndAfter {
  override implicit val config: Config = PostgresTestConfig.get
}
