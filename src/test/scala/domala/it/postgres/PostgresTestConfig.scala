package domala.it.postgres

import com.typesafe.config.ConfigFactory
import domala.jdbc.LocalTransactionConfig
import org.seasar.doma.jdbc.Naming
import org.seasar.doma.jdbc.dialect.PostgresDialect
import org.seasar.doma.jdbc.tx.LocalTransactionDataSource

class PostgresTestConfig(url: String, user: String, password: String)
    extends LocalTransactionConfig(
      dataSource = new LocalTransactionDataSource(url, user, password),
      dialect = new PostgresDialect,
      naming = Naming.SNAKE_LOWER_CASE
    ) {
  Class.forName("org.postgresql.Driver")
}

object PostgresTestConfig {
  private[this] val conf = ConfigFactory.load()
  def get: PostgresTestConfig = {
    new PostgresTestConfig(conf.getString("db.postgres.url"),
                           conf.getString("db.postgres.username"),
                           conf.getString("db.postgres.password"))
  }

  def getForBenchMark: PostgresTestConfig = {
    new PostgresTestConfig(
      conf.getString("db.postgres.url") + "?currentSchema=bench",
      conf.getString("db.postgres.username"),
      conf.getString("db.postgres.password")) {
      override def getBatchSize: Int = 1000
    }
  }

}
