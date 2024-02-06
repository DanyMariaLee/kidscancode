package com.iloonga.kids.service

import scala.concurrent.{ExecutionContext, Future}

trait LearningService {

  def checkSolution(inputCodeString: String, id: Int): Future[String]

}
