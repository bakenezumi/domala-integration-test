package domala.it.entity

import domala._
import domala.it.holder.{Age, ID, PersonName}

case class Person(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    id: ID[Person] = ID.notAssigned,
    name: Option[PersonName],
    age: Option[Age],
    address: Address,
    departmentId: Option[ID[Department]],
    @Version
    version: Int = -1
)
