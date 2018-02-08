package domala.it

import java.lang.reflect.Method
import javax.sql.DataSource

import domala.jdbc.LocalTransactionConfig
import domala.jdbc.dialect.Dialect
import org.seasar.doma.MapKeyNamingType
import org.seasar.doma.jdbc.{MapKeyNaming, Naming}

class IntegrationTestConfig(dataSource: DataSource,
                            dialect: Dialect,
                            driverName: String)
    extends LocalTransactionConfig(
      dataSource,
      dialect,
      naming = Naming.SNAKE_LOWER_CASE
    ) {
  Class.forName(driverName)

  override def getMapKeyNaming: MapKeyNaming = new MapKeyNaming {
    override def apply(method: Method,
                       mapKeyNamingType: MapKeyNamingType,
                       text: String): String =
      super.apply(method,
                  if (mapKeyNamingType == MapKeyNamingType.NONE)
                    MapKeyNamingType.LOWER_CASE
                  else mapKeyNamingType,
                  text)
  }
}
