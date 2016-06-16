package io.github.rlazoti.tictacjoe.backend.api

import io.github.rlazoti.tictacjoe.backend.models.{ BoardData, NewGame }
import io.github.rlazoti.tictacjoe.backend.services.GameService
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import scala.concurrent.ExecutionContext
import spray.json._

class GameAPI(implicit val executionContext: ExecutionContext) extends DefaultJsonProtocol {

  protected implicit val NewGameFormat = jsonFormat3(NewGame)
  protected implicit val BoardDataFormat = jsonFormat2(BoardData)
  private val service = new GameService()

  val routes =
    (path("game" / "new") & post & entity(as[NewGame])) { newGame =>
      complete(service.createNewGame(newGame).map { board => board.toData.toJson })
    }

}
