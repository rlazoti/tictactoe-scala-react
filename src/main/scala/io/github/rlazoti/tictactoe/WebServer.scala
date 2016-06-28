package io.github.rlazoti.tictactoe

import akka.http.scaladsl.Http
import io.github.rlazoti.tictactoe.api.GameAPI
import io.github.rlazoti.tictactoe.utils.AppContextProvider
import scala.io.StdIn

object WebServer extends App with AppContextProvider {

  private val api = new GameAPI()
  private val server = Http().bindAndHandle(api.routes, "127.0.0.1", 8080)

  println(s"Server online at port 8080.\nPress RETURN to stop...")
  StdIn.readLine()
  server
    .flatMap(_.unbind())
    .onComplete(_ => actorSystem.terminate())

}