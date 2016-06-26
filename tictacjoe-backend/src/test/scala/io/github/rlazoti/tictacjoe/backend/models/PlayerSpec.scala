package io.github.rlazoti.tictacjoe.backend.models

import org.scalatest._

class PlayerSpec extends FunSuite with Matchers {

  test("X Mark should accept 'x' as Input") {
    val mark = Mark.getByType("x")
    mark shouldBe MarkX()
  }

  test("X Mark should accept 'X' as Input") {
    val mark = Mark.getByType("X")
    mark shouldBe MarkX()
  }

  test("Any olther input different from 'x' and 'X' should be transformed in O Mark") {
    Mark.getByType("a") shouldBe MarkO()
    Mark.getByType("B") shouldBe MarkO()
    Mark.getByType("123") shouldBe MarkO()
    Mark.getByType(" ") shouldBe MarkO()
    Mark.getByType("") shouldBe MarkO()
  }

  test("An opponent's mark should always be an opposite mark to the player's mark") {
    Mark.getOpponentMark(Mark.getByType("x")) shouldBe MarkO()
    Mark.getOpponentMark(Mark.getByType("o")) shouldBe MarkX()
  }

}
