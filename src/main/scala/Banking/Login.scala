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
    This class will check two inputs that are part of a regex and check if there is match
    of a username and password combination in the Manager table.
 */
object Login{

  //These four declarations set up a connection to the database

  val codecRegistry = fromRegistries(fromProviders(classOf[Manager]), MongoClient.DEFAULT_CODEC_REGISTRY)

  val client = MongoClient()

  val db = client.getDatabase("Banking").withCodecRegistry(codecRegistry)

  val collection : MongoCollection[Manager] = db.getCollection("managers")

  def getResults[T](obs: Observable[T]) : Seq[T] = {
    Await.result(obs.toFuture(), Duration(10, SECONDS))
  }

  //This method will check a given regex and compare it to usernames and passwords that are in the
  //manager database.
  def loginManager(commandArgPattern : Regex): Int = {

    //Reads the user and password that was inputted by the user
    StdIn.readLine() match {

      case commandArgPattern(cmd, arg) =>{

        val username = s"${cmd}@gmail.com"

        val user = getResults(collection.find(and(equal("email", username), equal("password", arg))))

        //If the username and password was successfully entered by the user their credentials will be printed
        if (user.length > 0) {
          println(s"Hello ${user(0).first_name} ${user(0).last_name}.")
          return 1
        }
        else {
          println("The username and password you entered is incorrect")
          return 2
        }
      }
    }
  }

}
