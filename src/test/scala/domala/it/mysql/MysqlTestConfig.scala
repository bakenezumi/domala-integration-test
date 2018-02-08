package domala.it.mysql

import com.typesafe.config.ConfigFactory
import domala.it.IntegrationTestConfig
import domala.jdbc.Config
import org.seasar.doma.jdbc.dialect.MysqlDialect
import org.seasar.doma.jdbc.tx.LocalTransactionDataSource

class MysqlTestConfig private (url: String, user: String, password: String)
    extends IntegrationTestConfig(
      new LocalTransactionDataSource(url, user, password),
      new MysqlDialect(),
      "com.mysql.jdbc.Driver"
    )

object MysqlTestConfig {
  private[this] val conf = ConfigFactory.load()
  private[this] val forIntegration = new MysqlTestConfig(
    conf.getString("db.mysql.url"),
    conf.getString("db.mysql.username"),
    conf.getString("db.mysql.password"))

  private[this] val forBenchMark = new MysqlTestConfig(
    conf.getString("db.mysql.url") + "_bench",
    conf.getString("db.mysql.username"),
    conf.getString("db.mysql.password")) {
    override def getBatchSize: Int = 1000
  }

  def get: Config = forIntegration

  def getForBenchMark: Config = forBenchMark

}
