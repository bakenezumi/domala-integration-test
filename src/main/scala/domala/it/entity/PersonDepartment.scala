package domala.it.entity

import domala.it.holder.{ID, PersonName}

case class PersonDepartment(
    id: ID[Person],
    name: PersonName,
    departmentId: Option[ID[Department]],
    departmentName: Option[PersonName]
)
