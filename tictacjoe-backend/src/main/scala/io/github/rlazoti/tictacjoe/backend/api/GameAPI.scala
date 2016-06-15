package io.github.rlazoti.tictacjoe.backend.api

import io.github.rlazoti.tictacjoe.backend.models.NewGame
import io.github.rlazoti.tictacjoe.backend.services.GameService
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import scala.concurrent.ExecutionContext

class GameAPI(implicit val executionContext: ExecutionContext) {

  private val service = new GameService()

  val routes =
    (path("game" / "new") & post & entity(as[NewGame])) { newGame =>
      service.createNewGame(newGame).map { board => board.toJson }
    }

}
