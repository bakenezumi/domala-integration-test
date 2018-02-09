package domala.it.entity

import domala.it.holder.{DepartmentName, ID}

case class Department(
    departmentId: ID[Department],
    departmentName: DepartmentName
)
