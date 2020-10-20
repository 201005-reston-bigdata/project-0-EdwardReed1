package Banking

import java.io.FileNotFoundException

import scala.util.matching.Regex
import scala.io.StdIn

/*
    This class will run a method from the ManagerDAO class that will be an appropriate response to the input
    that is given by a successfully logged in manager.
 */
object ManagerOptions {

  //This class will check a given string that was inputted by the user and run a method from the ManagerDAO
  // in response to the input.

  def run(commandArgPattern : Regex) : Int = {
    //retrieves user input
    StdIn.readLine() match {
      //ran if the logOut command is used by the manager.
      //will be used to log them out of the system.
      case commandArgPattern(cmd, arg) if cmd.equalsIgnoreCase("logOut") => {
        return 1
      }

      //ran if the createAccount command is used by the user
      //will read a csv file and insert a document into the users document with information from the file
      case commandArgPattern(cmd, arg) if cmd.equalsIgnoreCase("createAccount") => {
        try {
          println("")
          println("createAccount was selected")
          ManagerDAO.createAccounts(arg)
          println("")
          return 0
        }
        catch {
          case fnf: FileNotFoundException => {
            println("")
            println(s"Failed to find file $arg")
            println("")
            return 0
          }
          case arr: ArrayIndexOutOfBoundsException => {
            println("")
            println(s"The file $arg had to many arguments passed into it.")
            println("")
            return 0
          }
        }

      }

      //ran if the search command is used by the user
      //will search the users collection to find a document that contains information from a given csv file.
      case commandArgPattern(cmd, arg) if cmd.equalsIgnoreCase("search") => {
        try {
          println("")
          println("search was selected")
          ManagerDAO.findAccount(arg)
          println("")
          return 0
        }
        catch {
          case fnf: FileNotFoundException => {
            println("")
            println(s"Failed to find file $arg")
            println("")
            return 0
          }
          case arr: ArrayIndexOutOfBoundsException => {
            println("")
            println(s"The file $arg had to many arguments passed into it.")
            println("")
            return 0
          }
          case idx: IndexOutOfBoundsException => {
            println("")
            println(s"The file $arg had to many arguments passed into it")
            println("")
            return 0
          }
        }

      }

      //ran if the updateBalance command is used by the user
      //will find a document in the users collection that contains the information from the csv file and will
      //add or subtract a given amount from that documents balance field.
      case commandArgPattern(cmd, arg) if cmd.equalsIgnoreCase("updateBalance") => {
        try {
          println("")
          println("update balance was selected")
          ManagerDAO.updateBalance(arg)
          println("")
          return 0
        }
        catch {
          case fnf: FileNotFoundException => {
            println("")
            println(s"Failed to find file $arg")
            println("")
            return 0
          }
          case arr: ArrayIndexOutOfBoundsException => {
            println("")
            println(s"The file $arg had to many arguments passed into it.")
            println("")
            return 0
          }
          case idx: IndexOutOfBoundsException => {
            println("")
            println(s"The file $arg had to many arguments passed into it")
            println("")
            return 0
          }
        }
      }

      //ran if the delete command is used by the user
      //will delete a document from the users collection that contains information from a given
      //csv file.
      case commandArgPattern(cmd, arg) if cmd.equalsIgnoreCase("delete") => {
        try {
          println("")
          println("delete was selected")
          ManagerDAO.deleteAccount(arg)
          println("")
          return 0
        }
        catch {
          case fnf: FileNotFoundException => {
            println("")
            println(s"Failed to find file $arg")
            println("")
            return 0
          }
          case arr: ArrayIndexOutOfBoundsException => {
            println("")
            println(s"The file $arg had to many arguments passed into it.")
            println("")
            return 0
          }
          case idx: IndexOutOfBoundsException => {
            println("")
            println(s"The file $arg had to many arguments passed into it")
            println("")
            return 0
          }
        }
      }

      //ran if the searchByAmount command is used by the user
      //will read a csv file given by the manager and will search and list documents from the users collection
      //that contain have a balance that is greater than or equal to the given amount.
      case commandArgPattern(cmd, arg) if cmd.equalsIgnoreCase("searchByAmount") => {
        try {
          println("")
          println("search by amount was selected")
          ManagerDAO.searchByAmount(arg)
          println("")
          return 0
        }
        catch {
          case fnf: FileNotFoundException => {
            println("")
            println(s"Failed to find file $arg")
            println("")
            return 0
          }
          case arr: ArrayIndexOutOfBoundsException => {
            println("")
            println(s"The file $arg had to many arguments passed into it.")
            println("")
            return 0
          }
          case idx: IndexOutOfBoundsException => {
            println("")
            println(s"The file $arg had to many arguments passed into it")
            println("")
            return 0
          }
        }
      }

      //ran if the user typed in a command that was not listed on the options menu
      case notRecognized => {
        println("")
        println(s"$notRecognized is not a recognized command")
        println("")
        return 0
      }
    }
  }
}
