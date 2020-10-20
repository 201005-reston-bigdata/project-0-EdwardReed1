package Banking

import org.bson.types.ObjectId

/*
    This object will represent documents in the manager table in the database
 */

case class Manager(_id: ObjectId, first_name: String, last_name: String, email: String, password: String) {}

object Manager {
  def apply(first_name: String, last_name: String, email: String, password: String): Manager = Manager(new
      ObjectId(), last_name, first_name, email, password)
}
