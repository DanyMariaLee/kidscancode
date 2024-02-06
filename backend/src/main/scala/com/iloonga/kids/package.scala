package com.iloonga

import com.typesafe.scalalogging.LazyLogging
import pureconfig.ConfigSource
import pureconfig._
import pureconfig.generic.auto._

import scala.util.{Failure, Success, Try}

package object kids extends LazyLogging {

  val iloongaConfig: IloongaConfig =
    Try(ConfigSource.default.loadOrThrow[IloongaConfig]) match {
      case Success(cfg) =>
        Try(System.getenv("PORT").toInt).toOption match {
          case Some(p) => cfg.copy(apiPort = p)
          case None => logger.warn(
            "Failed to read PORT env, will proceed with value from reference.conf")
            cfg
        }

      case Failure(th) => logger.error(s"Failed to read config:\n$th")
        throw new RuntimeException(s"Error due config reading: ${th.getCause}")
    }
}