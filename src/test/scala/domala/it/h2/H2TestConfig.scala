package domala.it.h2

import com.typesafe.config.ConfigFactory
import domala.it.IntegrationTestConfig
import domala.it.mysql.MysqlTestConfig
import domala.jdbc.Config
import org.seasar.doma.jdbc.dialect.H2Dialect
import org.seasar.doma.jdbc.tx.LocalTransactionDataSource

class H2TestConfig private (url: String, user: String)
    extends IntegrationTestConfig(
      new LocalTransactionDataSource(url, user, null),
      new H2Dialect(),
      "org.h2.Driver"
    )

object H2TestConfig {
  private[this] val conf = ConfigFactory.load()
  private[this] val forIntegration = new H2TestConfig(
    conf.getString("db.h2.url"),
    conf.getString("db.h2.username"))

  private[this] val forBenchMark = new H2TestConfig(
    conf.getString("db.h2.url") + "_bench",
    conf.getString("db.h2.username")) {
    override def getBatchSize: Int = 1000
  }

  def get: Config = forIntegration

  def getForBenchMark: Config = forBenchMark

}
