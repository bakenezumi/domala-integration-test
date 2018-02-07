package domala.it.mysql

import com.typesafe.config.ConfigFactory
import domala.jdbc.LocalTransactionConfig
import org.seasar.doma.jdbc.Naming
import org.seasar.doma.jdbc.dialect.MysqlDialect
import org.seasar.doma.jdbc.tx.LocalTransactionDataSource

class MysqlTestConfig(url: String, user: String, password: String)
    extends LocalTransactionConfig(
      dataSource = new LocalTransactionDataSource(url, user, password),
      dialect = new MysqlDialect,
      naming = Naming.SNAKE_LOWER_CASE
    ) {
  Class.forName("com.mysql.jdbc.Driver")
}

object MysqlTestConfig {
  private[this] val conf = ConfigFactory.load()
  def get: MysqlTestConfig = {
    new MysqlTestConfig(conf.getString("db.mysql.url"),
                        conf.getString("db.mysql.username"),
                        conf.getString("db.mysql.password"))
  }

  def getForBenchMark: MysqlTestConfig = {
    new MysqlTestConfig(conf.getString("db.mysql.url") + "_bench",
                        conf.getString("db.mysql.username"),
                        conf.getString("db.mysql.password")) {
      override def getBatchSize: Int = 1000
    }
  }

}
