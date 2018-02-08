package domala.it.h2

import domala.it.IntegrationTestSuite
import domala.jdbc.Config
import org.scalatest._

class H2IntegrationTestSuite extends IntegrationTestSuite with BeforeAndAfter {
  override val config: Config = H2TestConfig.get

}
