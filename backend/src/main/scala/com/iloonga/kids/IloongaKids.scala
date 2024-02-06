package com.iloonga.kids

import slogging._

object IloongaKids extends App with LazyLogging {

  /**
   * Prints runtime information such as the amount of memory and processors available to the JVM.
   */
  private def printRuntimeInfo() = {
    val runtime = Runtime.getRuntime
    val mb = 1024 * 1024
    val maxMemoryInMb = runtime.maxMemory() / mb
    logger.info("JVM max heap memory: {} MB. Available processors: {}", maxMemoryInMb, runtime.availableProcessors)
  }

  // Without this, we wouldn't have log messages
  LoggerConfig.factory = SLF4JLoggerFactory()

  logger.info("Let's start the {} server", "iloonga")
  printRuntimeInfo()

  AppServer.up()
}