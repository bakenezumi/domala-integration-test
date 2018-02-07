package domala.it.entity

import domala.it.holder.{ID, Name}

case class PersonDepartment(
  id: ID[Person],
  name: Name,
  departmentId: Option[ID[Department]],
  departmentName:Option[Name]
)
