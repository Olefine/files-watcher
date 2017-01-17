package ru.egorodov.server.utils

import com.typesafe.config.ConfigFactory

object DeploySettings {
  private val amazonConf = ConfigFactory.load("application.amazon")

  def keyPath: String = {
    val keyobj = amazonConf.getObject("key")

    keyobj.get("key-location").render.concat(keyobj.get("key").render)
  }

  def ami: String = amazonConf.getObject("deployment").get("ami").render

  def user: String = amazonConf.getObject("deployment").get("user").render

  def workerPath: String = amazonConf.getString("worker")
}
