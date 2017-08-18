package com.trn.rest.commands

trait Command {
 val command: String
  def action: String // do all the processing .. called by response method
  def response: String // the response back to user
}



/**
  * Currency companion object.
  */
object Command {



}