package com.trn.jsch

import java.io.{DataInputStream, DataOutputStream, PrintStream}

import com.jcraft.jsch.{ChannelShell, JSch}

object RemoteShell /*extends App*/ {

  val username = "fidato"
  val password = "hadoop"
  val host = "192.168.50.39"
  val port = 22

  val path = "/home/fidato/ruf/xyz.sh"

  def remoteShell(config: RemoteConfig)/*: RemoteExecutionResponse*/ = {

    try {

      val jsch = new JSch

      val endLineStr = " # "

      //0-$ bash  1*$ bash
      //screen -x ${sessionname} -X select newwin
      //screen -x ${sessionname} -X at "newwin" stuff "echo hello world^M"
      //13418.tempscreen

      //0-$ bash  1*$ bash
      //screen -x 13418.tempscreen -X select bash0
      //screen -x 13418.tempscreen -X at "bash0" stuff "echo hello world^M"
      //screen -x 13418.tempscreen -p 0
      //13418.tempscreen

      val command1 = "touch test1.txt"
      val command2 = "touch test2.txt"
      val command3 = "touch test3.txt"
      val command4 = "ls -lart"
      val command5 = "screen -x 13418.tempscreen -p 0"

      val commands = List[String](/*command1,command2,command3,command4,*/command5)



      val session = jsch.getSession(config.username, config.host, config.port)
      session.setConfig("StrictHostKeyChecking", "no")
      //val privateKey = "~/.ssh/id_rsa"
      //jsch.addIdentity(privateKey)
      session.setPassword(config.password)
      session.connect

      //create the excution channel over the session
      val channel = session.openChannel("shell").asInstanceOf[ChannelShell]

      channel.setOutputStream(System.out)

      val shellStream = new PrintStream(channel.getOutputStream())

      channel.connect()

      /*commands.foreach{x =>
        shellStream.println(x)
        shellStream.flush
      }*/

      val dataIn = new DataInputStream(channel.getInputStream)

      val dataOut = new DataOutputStream(channel.getOutputStream)

      // send ls command to the server
      dataOut.writeBytes(s"$command5\r\n")
      dataOut.flush()


      // and print the response
      //var line = dataIn.readLine()
      //println("The line of response => "+line)

      val lines: List[String] = Iterator.continually(dataIn.readLine()).take(10).toList

      lines.foreach(println(_))

      /*while(!line.endsWith(endLineStr)) {
        println(line)
        line = dataIn.readLine()
        //println("In the while loop...")
      }*/
      dataIn.close
      dataOut.close

      //println("Calling wait thread")
      Thread.sleep(1000)

      //println("Disconnecting...")
      channel.disconnect
      session.disconnect


    } catch {
      case ex: Exception => println(s"Some exception ${ex.printStackTrace}")
    }

  }

  //create RemoteConfig object
  val remoteConfig = RemoteConfig(username, password, host, port, path, "")

  val output = remoteShell(remoteConfig)

  //println(s"The value from shell => $output")

}
