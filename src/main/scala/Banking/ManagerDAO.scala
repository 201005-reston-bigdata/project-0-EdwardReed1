package Banking

import scala.io.{BufferedSource, Source}

import org.mongodb.scala.{MongoClient, MongoCollection, Observable}
import org.mongodb.scala.bson.codecs.Macros._
import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}

import scala.concurrent.Await
import scala.concurrent.duration.{Duration, SECONDS}
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.model.Updates._

/*
    This class will run methods that will insert, update, and delete records in the user database
    that will be ran by a manager that has logged into the system.
 */

object ManagerDAO {

  //Establishes a database connection

  val codecRegistry = fromRegistries(fromProviders(classOf[User]), MongoClient.DEFAULT_CODEC_REGISTRY)

  val client = MongoClient()

  val db = client.getDatabase("Banking").withCodecRegistry(codecRegistry)

  val collection : MongoCollection[User] = db.getCollection("users")

  def getResults[T](obs: Observable[T]) : Seq[T] = {
    Await.result(obs.toFuture(), Duration(10, SECONDS))
  }

  def printResults[T](obs: Observable[T]) : Unit = {
    getResults(obs).foreach(println(_))
  }

  def closeClient() : Unit = {
    client.close()
  }

  //This method is used to create account(s) that will be added into the database.
  def createAccounts(filename : String) : BufferedSource = {
    // both of these are just to declare outside of the try block
    var openedFile: BufferedSource = null
    //var textContent : Option[String] = None

    try {
      openedFile = Source.fromFile(filename)
      //textContent = Some(openedFile.mkString(" "))
      //returns this
      //Some(openedFile.getLines().mkString(" "))
      try {
        val openedFile = Source.fromFile(filename)



        for (line <- openedFile.getLines()) {

          val cols = line.split(",").map(_.trim)

          //printResults(collection.insertOne(User(cols(0), cols(1), cols(2), cols(3), cols(4))))

          var lName = cols(0)
          var fName = cols(1)
          var email = cols(2)
          var balance = cols(3).toDouble
          var address = cols(4)

          if (getResults(collection.find(equal("email", cols(2) ))).length == 0) {
            printResults(collection.insertOne(User(lName, fName, email, balance, address)))
          }
          else {
            println("There already exists a user with that email.")
          }
        }
      }
      return openedFile
    } finally {
      if (openedFile != null) openedFile.close()
    }
  }

  //This method is used to return an account given the email address used to create it.
  def findAccount(filename : String) : BufferedSource = {
    // both of these are just to declare outside of the try block
    var openedFile: BufferedSource = null
    //var textContent : Option[String] = None

    try {
      openedFile = Source.fromFile(filename)
      //textContent = Some(openedFile.mkString(" "))
      //returns this
      //Some(openedFile.getLines().mkString(" "))
      try {
        val openedFile = Source.fromFile(filename)

        for (line <- openedFile.getLines()) {

          val cols = line.split(",").map(_.trim)

          var email = cols(0)

          val searched = getResults(collection.find(equal("email", cols(0))))(0)
          println(s"The user you have searched for is ${searched.first_name} ${searched.last_name}. Their email address" +
            s" is ${email} and they have a balance of ${searched.balance}.")
        }
        return openedFile

    } finally {
        if (openedFile != null) openedFile.close()
      }
    }
  }

  //This method is used to update a user's account when the user deposits an amount of
  //money into their account.
  def updateBalance(filename : String) : BufferedSource = {
    // both of these are just to declare outside of the try block
    var openedFile: BufferedSource = null

    try {
      openedFile = Source.fromFile(filename)

      try {
        val openedFile = Source.fromFile(filename)

        for (line <- openedFile.getLines()) {

          val cols = line.split(",").map(_.trim)

          var email = cols(0)
          var change = cols(1).toDouble

          val changed = getResults(collection.find(equal("email", cols(0))))(0)

          val currentBalance = changed.balance

          printResults(collection.updateOne(equal("email", email),
            set("balance", (currentBalance + change))
          ))

        }
        return openedFile

      } finally {
        if (openedFile != null) openedFile.close()
      }
    }
  }

  //This method is used to delete a users account when they decide to stop being a member of
  //our bank.
  def deleteAccount(filename: String) : BufferedSource = {
    // both of these are just to declare outside of the try block
    var openedFile: BufferedSource = null
    //var textContent : Option[String] = None

    try {
      openedFile = Source.fromFile(filename)

      try {
        val openedFile = Source.fromFile(filename)
        for (line <- openedFile.getLines()) {

          val cols = line.split(",").map(_.trim)

          val emailToDelete = cols(0)

          printResults(collection.deleteOne(equal("email", emailToDelete)))

        }
      }
      return openedFile
    } finally {
      if (openedFile != null) openedFile.close()
    }
  }

  //This method will print a list of all users that have more than or do have
  // a user given value in their account
  def searchByAmount(filename: String) : BufferedSource = {
    var openedFile: BufferedSource = null
    //var textContent : Option[String] = None

    try {
      openedFile = Source.fromFile(filename)

      try {
        val openedFile = Source.fromFile(filename)
        for (line <- openedFile.getLines()) {

          val cols = line.split(",").map(_.trim)

          val searchAmount = cols(0).toDouble

          for(item <- (collection.find(or(gt("balance", searchAmount), equal("balance", searchAmount))))) {
            println(s"${item.email}")
          }
        }
      }
      return openedFile
    } finally {
      if (openedFile != null) openedFile.close()
    }
  }
}
