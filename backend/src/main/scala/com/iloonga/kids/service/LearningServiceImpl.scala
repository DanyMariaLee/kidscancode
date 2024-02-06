package com.iloonga.kids.service

import sharedstuff.domain.Problem

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Try, Success, Failure}

class LearningServiceImpl(problems: Future[Seq[Problem]], implicit val ec: ExecutionContext) extends LearningService {

  def checkSolution(inputCodeString: String, id: Int): Future[String] = problems.map { p =>
    val problem = p.find(_.id == id).getOrElse(
      throw new RuntimeException(s"problem with id $id doesn't exist"))

    Try(scala.tools.nsc.interpreter.shell.Scripted()
      .eval(s"""${problem.incompleteImplementation} $inputCodeString""")) match {
      case Success(s) =>
        Try(scala.tools.nsc.interpreter.shell.Scripted()
          .eval(
            s"""${problem.incompleteImplementation} $inputCodeString
               |${problem.testingStatement} == ${problem.testingResult}""".stripMargin)) match {
          case Success(s) if s.toString.contains("true") => "Success!"
          case Success(_) => "Looks like test didn't pass! Hint " + problem.hint
          case Failure(th) => "Looks like test didn't pass! " + th.getMessage
        }
      case Failure(th) =>
        if (th.getMessage.contains("not found: value")) "Incorrect. You need to use parameter names provided in the description"
        else th.getMessage
    }

  }.recoverWith {
    case th => Future.failed(new RuntimeException("Failed to run code: " + th.getMessage))
  }

}
