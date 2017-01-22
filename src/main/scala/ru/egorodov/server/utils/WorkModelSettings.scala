package ru.egorodov.server.utils

import com.typesafe.config.ConfigFactory

object WorkModelSettings {
  def isStandalone: Boolean = ConfigFactory.load().getObject("application").get("execution_mode").unwrapped.toString == "standalone"

  def isCluster: Boolean = !isStandalone
}
