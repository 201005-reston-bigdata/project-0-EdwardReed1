package Banking

import org.bson.types.ObjectId

/*
    This class will represent documents from the users table in the database
 */
case class User(_id: ObjectId, last_name: String, first_name: String, email: String, balance: Double, password: String) {}

object User {
  def apply(last_name: String, first_name: String, email: String, balance: Double, password: String) : User = User(new
  ObjectId(), last_name, first_name, email, balance, password)


}
