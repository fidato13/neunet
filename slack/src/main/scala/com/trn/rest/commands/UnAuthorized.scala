package com.trn.rest.commands

class UnAuthorized extends Command {
  override val command: String = ""

  override def action: String = "You are not Authorized here!"

  override def response: String = "You are not Authorized here!"
}
