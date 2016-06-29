package io.github.rlazoti.tictactoe

import akka.http.scaladsl.Http
import io.github.rlazoti.tictactoe.api.GameAPI
import io.github.rlazoti.tictactoe.utils.AppContextProvider
import scala.io.StdIn

object WebServer extends App with AppContextProvider {

  private val api = new GameAPI()
  private val server = Http().bindAndHandle(api.routes, "0.0.0.0", 8080)

  println(s"Server online at port 8080.\nPress 'exit' to stop...")

  waitExitMessage()

  private def waitExitMessage(): Unit = {
    val exitMessage = StdIn.readLine()

    exitMessage match {
      case "exit" => server.flatMap(_.unbind()).onComplete(_ => actorSystem.terminate())
      case _ => waitExitMessage()
    }
  }

}
