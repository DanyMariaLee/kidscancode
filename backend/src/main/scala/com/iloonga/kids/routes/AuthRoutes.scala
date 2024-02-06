package com.iloonga.kids.routes

import akka.actor.ActorSystem
import com.iloonga.kids.JsonProtocols
import com.iloonga.kids.JsonProtocols
import com.typesafe.scalalogging.LazyLogging
import sharedstuff.api.AuthRouteAjaxApi
import sharedstuff.serverresponses.ServerResponse

import scala.concurrent.{ExecutionContext, Future}

trait AuthRoutes extends AuthRouteAjaxApi with JsonProtocols with LazyLogging {

  implicit def ec: ExecutionContext

  implicit def actorSystem: ActorSystem

  def register: Future[ServerResponse] = Future(ServerResponse(0, ""))

}
