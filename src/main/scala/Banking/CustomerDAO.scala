package Banking

import scala.util.matching.Regex
import scala.io.StdIn

import org.mongodb.scala.{MongoClient, MongoCollection, Observable}
import org.mongodb.scala.bson.codecs.Macros._
import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}

import scala.concurrent.Await
import scala.concurrent.duration.{Duration, SECONDS}
import org.mongodb.scala.model.Filters._

/*
    This class will check a given username and password and see if there is a match in the user database.
 */
object CustomerDAO {

  //Establishes a connection to the database
  val codecRegistry = fromRegistries(fromProviders(classOf[User]), MongoClient.DEFAULT_CODEC_REGISTRY)

  val client = MongoClient()

  val db = client.getDatabase("Banking").withCodecRegistry(codecRegistry)

  val collection : MongoCollection[User] = db.getCollection("users")

  def getResults[T](obs: Observable[T]) : Seq[T] = {
    Await.result(obs.toFuture(), Duration(10, SECONDS))
  }

  //This will display user and their current account balance
  def displayInfo(commandArgPattern : Regex) : Unit = {

    //Reads the user and password that was inputted by the user
    StdIn.readLine() match {
      case commandArgPattern(cmd, arg) => {

        val username = s"${cmd}@gmail.com"

        val user = getResults(collection.find(and(equal("email", username), equal("password", arg))))

        //If the username and password are a match the name of the user along with their current balance will be
        //printed to the console.
        if (user.length > 0) {
          println(s"Hello ${user(0).first_name} ${user(0).last_name}, your current balance is ${user(0).balance}.")
        }
        else {
          println("The username and password you entered is incorrect")
        }
      }
    }
  }
}
