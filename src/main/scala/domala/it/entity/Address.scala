package domala.it.entity

case class Address(city: CityName, street: StreetName)

case class CityName(value: String) extends AnyVal
case class StreetName(value: String) extends AnyVal
