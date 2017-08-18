package com.trn.jsch

case class RemoteExecutionResponse(val exitStatus: Int, outStream: List[String])
