package io.github.rlazoti.tictacjoe.backend

import akka.http.scaladsl.Http
import io.github.rlazoti.tictacjoe.backend.api.GameAPI
import io.github.rlazoti.tictacjoe.backend.utils.AppContextProvider

object WebServer extends App with AppContextProvider {

  private val api = new GameAPI()
  Http().bindAndHandle(api.routes, "localhost", 8080)

}
