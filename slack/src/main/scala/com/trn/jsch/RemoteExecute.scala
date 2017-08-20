package com.trn.jsch

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.util
import com.jcraft.jsch.ChannelExec
import com.jcraft.jsch.JSch
import com.jcraft.jsch.Session


object RemoteExecute /*extends App*/ {


/*
  val username = "fidato"
  val password = "hadoop"
  val host = "192.168.50.39"
  val port = 22

  val path = "/home/fidato/ruf/script1.sh"
*/


  /**
    *
    * @param config absolute path of the script to be executed
    */
  def remoteExecuteScript(config: RemoteConfig): RemoteExecutionResponse = {

    try {

      /**
        * Create a new Jsch object
        * This object will execute shell commands or scripts on server
        */
      val jsch = new JSch

      /*
         * Open a new session, with your username, host and port
         * Set the password and call connect.
         * session.connect() opens a new connection to remote SSH server.
         * Once the connection is established, you can initiate a new channel.
         * this channel is needed to connect to remotely execution program
         */
      val session = jsch.getSession(config.username, config.host, config.port)
      session.setConfig("StrictHostKeyChecking", "no")
      //session.setPassword(config.password)
      jsch.addIdentity(config.key)
      session.connect

      //create the excution channel over the session
      val channelExec: ChannelExec = session.openChannel("exec").asInstanceOf[ChannelExec]

      // Gets an InputStream for this channel. All data arriving in as messages from the remote side can be read from this stream.
      val in = channelExec.getInputStream

      // Set the command that you want to execute
      // In our case its the remote shell script
      channelExec.setCommand("sh "+config.path)


      // Execute the command
      channelExec.connect

      // Read the output from the input stream we set above
      val reader = new BufferedReader(new InputStreamReader(in))

      //Read each line from the buffered reader and add it to result list
      // You can also simple print the result here
      val lines: List[String] = Iterator.continually(reader.readLine()).takeWhile(_ != null).toList

      //retrieve the exit status of the remote command corresponding to this channel
      val exitStatus: Int = channelExec.getExitStatus

      //Safely disconnect channel and disconnect session. If not done then it may cause resource leak
      channelExec.disconnect
      session.disconnect

      exitStatus match {
        case x if x < 0 => println("Exit status less than zero!")
        case x if x > 0 => println("Exit status greater than zero!")
        case _ => println("Successfully completed!")
      }

      RemoteExecutionResponse(exitStatus, lines)


    } catch {
      case ex: Exception => println(s"Some exception occured ${ex.printStackTrace}"); RemoteExecutionResponse(99,List.empty[String])
    }

  }


  //println("Execute script on sandbox /root/ruf-trn/script1.sh")

  //create RemoteConfig object
  //val remoteConfig = RemoteConfig(username, password, host, port, path)

  //val result: RemoteExecutionResponse = remoteExecuteScript(remoteConfig)

  //println(s"The result from script execution is  => ${result.outStream}")

}
