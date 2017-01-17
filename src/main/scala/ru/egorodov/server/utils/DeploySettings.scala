package ru.egorodov.server.utils

import com.typesafe.config.ConfigFactory

object DeploySettings {
  def keyPath: String = {
    val conf = ConfigFactory.load().getObject("application.amazon.key")

    conf.get("key-location").unwrapped.toString + conf.get("key-filename").unwrapped.toString
  }

  def ami: String = ConfigFactory.load().getObject("application.amazon.deployment").get("ami").unwrapped.toString

  def user: String = ConfigFactory.load().getObject("application.amazon.deployment").get("user").unwrapped.toString

  def workerPath: String = ConfigFactory.load().getObject("application.amazon").get("workerFilePath").unwrapped.toString
}
