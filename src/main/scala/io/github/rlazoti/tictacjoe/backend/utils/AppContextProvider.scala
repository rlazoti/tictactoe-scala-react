package io.github.rlazoti.tictacjoe.backend.utils

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import scala.concurrent.ExecutionContext

trait AppContextProvider {
  protected implicit lazy val actorSystem: ActorSystem = ActorSystem("tictacjoe")
  protected implicit lazy val materializer: ActorMaterializer = ActorMaterializer()
  protected implicit lazy val executionContext: ExecutionContext = actorSystem.dispatcher
}
