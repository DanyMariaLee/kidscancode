package com.iloonga.kids.service

import scala.concurrent.Future
import sharedstuff.domain.Problem

trait ProblemService {

  def readAllProblems: Future[Seq[Problem]]
  def allProblems: Future[Seq[Problem]]

}
