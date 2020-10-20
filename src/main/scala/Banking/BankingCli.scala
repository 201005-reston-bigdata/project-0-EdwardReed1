package Banking

import scala.util.matching.Regex
import scala.io.StdIn


/*
     This class will read user input and will call the appropriate method in response to what was entered.
     It will also display the possible options to the user and let them know what commands are available and
     what they do.
 */
class BankingCli {

  //default welcome when the cli starts up
  def printWelcome() : Unit = {
    println("Welcome to the Banking Management System")
  }

  //displays the list of options before the user has logged into the system.
  def printDefaultOptions() : Unit = {
    println(" ")
    println("manager: Sends you to the login command for Managers.")
    println("customer: Sends you to the login command for customers.")
    println("exit: Exits the user out of the system.")
    println(" ")
  }

  //displays how the user(when a customer) needs to sign into their account
  def printLogInOptions() : Unit = {
    println(" ")
    println("Please enter your username(email before the @ sign) and password separated by a space.")
  }


  //displays how the user(when a manager) needs to sign into their account
  def printMangerOptions() : Unit = {
      println(" ")
      println("logOut: Will log you out of your account and send you back to the default command.")
      println("createAccount: Reads through a csv file and adds an account(s) to the system with the given values")
      println("search: reads a csv file and print out information with an account that matches the one specified" +
        "in the csv file.")
      println("updateBalance: Reads a csv file will add the amount specified to the user's account.")
      println("delete: Reads through a csv and will delete the account(s) with the information specified in the csv file.")
      println("searchByAmount: Will find and print out the information of account(s) that have either an amount greater than " +
        "or equal to amount given by the user")
      println(" ")

  }

  //the regex that will check user input while using the app
  val commandArgPattern : Regex = "(\\w+)\\s*(.*)".r

  //this boolean is used to check if the user has decided to exit out of the system
  var continueLoop = true

  //this boolean is used to check if the user is a manager of the bank
  var isManager = false

  //this boolean is used to check if the user is customer of the bank
  var isCustomer = false

  //this boolean is used to check if the user is currently logging in as a manager
  var isLoggingIn = false

  //checks the user input and run the appropriate code related
  //to the input.

  def menu() : Unit = {
    printWelcome()

    //loop that checks input and runs code
    while (continueLoop) {

      //will run when the user first enters the system and it is not defined if the user
      //is a customer or manager
      if (!isManager && !isCustomer && !isLoggingIn) {
        printDefaultOptions()

        //Checks to see if the user entered one of the three apporpriate commands or if they made a typo.
        StdIn.readLine() match {
          case commandArgPattern(cmd, arg) if cmd.equalsIgnoreCase("manager") => isLoggingIn = true
          case commandArgPattern(cmd, arg) if cmd.equalsIgnoreCase("customer") => isCustomer = true
          case commandArgPattern(cmd, arg) if cmd.equalsIgnoreCase("exit") => continueLoop = false
          case notRecognized => {
            println("")
            println(s"$notRecognized is not a valid command, please try again.")
            println("")
          }
        }

      }

      //will run when the user is confirmed to be a customer(User)
      if (isCustomer && continueLoop) {
          //this will show the user how they must enter their username and password.
          printLogInOptions()

          //this will check if the username and password are correct and then either display the users info or
          //tell them that it was incorrect.
          CustomerDAO.displayInfo(commandArgPattern)

          //this will cause the application to go back to the default screen.
          isCustomer = false
      }

      if (isLoggingIn && continueLoop) {
        //this will show the user how they must enter their username and password.
        printLogInOptions()
          if (Login.loginManager(commandArgPattern) == 1) {
            isLoggingIn = false
            isManager = true
          }
      }


      //will run if the user is confirmed to be a manager
      if (isManager && continueLoop) {
        printMangerOptions()
        if (ManagerOptions.run(commandArgPattern) == 1) {
          isManager = false
        }
      }
    }

  }
}
