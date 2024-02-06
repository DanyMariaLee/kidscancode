package com.iloonga.kids.service

import sharedstuff.domain.Problem

import scala.concurrent.{ExecutionContext, Future}
import scala.io.Source

class ProblemServiceImpl(implicit val ec: ExecutionContext) extends ProblemService {

  val allProblems: Future[Seq[Problem]] = readAllProblems

  def readAllProblems: Future[Seq[Problem]] = Future {
    val resource = Source.fromResource("problems.csv")
    val lines: Seq[String] = resource.getLines.toSeq.tail

    lines.map {
      line =>
        val all = line.split(";")
        Problem(id = all.head.toInt,
          statement = all(1),
          incompleteImplementation = all(2),
          correctImplementation = all(3),
          testingStatement = all(4),
          testingResult = all(5),
          hint = all(6))
    }

  }


}
