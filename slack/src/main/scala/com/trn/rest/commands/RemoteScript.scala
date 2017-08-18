package com.trn.rest.commands

import com.trn.jsch.{RemoteConfig, RemoteExecutionResponse}
import com.typesafe.config.ConfigFactory
import com.trn.jsch.RemoteExecute._

class RemoteScript(text: String) extends Command {
  override val command: String = text

  /**
    * Application configuration object.
    */
  final val config = ConfigFactory.load()

  /**
    * This will come from config files.. the name of the properties on the config file must match the keywords of the commands
    */
  val path =  config.getString(s"app.remote.$command.path")
  val host =  config.getString(s"app.remote.$command.host")
  val port = config.getInt(s"app.remote.$command.port")
  val username =  config.getString(s"app.remote.$command.username")
  val password =  config.getString(s"app.remote.$command.password")


  override def action: String = {
    //create remote Config object
    val remoteConfig = RemoteConfig(username, password, host, port, path)
    //send the execution response
    remoteExecuteScript(remoteConfig).outStream.mkString("\n")
  }

  override def response: String = action
}
