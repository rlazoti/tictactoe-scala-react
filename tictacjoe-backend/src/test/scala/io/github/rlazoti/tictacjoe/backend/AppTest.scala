package io.github.rlazoti.tictacjoe.backend

import org.scalatest._

class AppTest extends FunSpec {

  describe("My first spec") {
    it("should pass") {
      assert(true)
    }
  }

  describe("My second spec") {
    it("should have a pending test") (pending)
  }

}
