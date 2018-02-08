package domala.it.entity

import domala._
import domala.it.holder.{ID, Name}

case class Person(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    id: ID[Person] = ID.notAssigned,
    name: Option[Name],
    age: Option[Int],
    address: Address,
    departmentId: Option[Int],
    @Version
    version: Option[Int] = Option(-1)
)
