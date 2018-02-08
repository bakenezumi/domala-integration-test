package domala.it.h2

import domala.it.SelectBenchMarkTestSuite
import domala.jdbc.Config

class H2SelectBenchMarkTestSuite extends SelectBenchMarkTestSuite {
  override val config: Config = H2TestConfig.get

}
