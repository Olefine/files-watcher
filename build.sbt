import org.scalatra.sbt._
import com.mojolly.scalate.ScalatePlugin._
import ScalateKeys._

val ScalatraVersion = "2.5.0"

ScalatraPlugin.scalatraSettings

scalateSettings

organization := "ru.egorodov"

name := "files-watcher"

version := "0.0.1-SNAPSHOT"

scalaVersion := "2.11.8"

resolvers += Classpaths.typesafeReleases

libraryDependencies ++= Seq(
  "org.scalatra" %% "scalatra" % ScalatraVersion,
  "org.scalatra" %% "scalatra-scalate" % ScalatraVersion,
  "org.scalatra" %% "scalatra-specs2" % ScalatraVersion % "test",
  "ch.qos.logback" % "logback-classic" % "1.1.5" % "runtime",
  "org.eclipse.jetty" % "jetty-webapp" % "9.2.15.v20160210" % "container",
  "javax.servlet" % "javax.servlet-api" % "3.1.0" % "provided",
  "org.scalatra" %% "scalatra-json" % ScalatraVersion,
  "org.json4s"   %% "json4s-jackson" % "3.3.0",
  "com.typesafe.akka" %% "akka-actor" % "2.4.16",
  "net.databinder.dispatch" %% "dispatch-core" % "0.11.1",
  "com.lihaoyi" %%% "scalatags" % "0.6.1",
  "org.scala-js" %%% "scalajs-dom" % "0.9.1",
  "be.doeraene" %%% "scalajs-jquery" % "0.9.1",
  "org.mongodb.scala" %% "mongo-scala-driver" % "1.0.1",
  "com.github.seratch" %% "awscala" % "0.5.+",
  "com.typesafe.akka" %% "akka-cluster" % "2.4.16",
  "com.decodified" %% "scala-ssh" % "0.7.0",
  "org.scalactic" %% "scalactic" % "3.0.1",
  "org.scalatest" %% "scalatest" % "3.0.1" % "test",
  "org.scalamock" %% "scalamock-scalatest-support" % "3.4.2" % "test"
)

mergeStrategy in assembly <<= (mergeStrategy in assembly) { (old) =>
{
  case m if m.toLowerCase.endsWith("manifest.mf") => MergeStrategy.discard
  case m if m.startsWith("META-INF") => MergeStrategy.discard
  case PathList("javax", "servlet", xs @ _*) => MergeStrategy.first
  case PathList("org", "apache", xs @ _*) => MergeStrategy.first
  case PathList("org", "jboss", xs @ _*) => MergeStrategy.first
  case "about.html"  => MergeStrategy.rename
  case "reference.conf" => MergeStrategy.concat
  case PathList("org", "datanucleus", xs @ _*) => MergeStrategy.discard
  case _ => MergeStrategy.first
}
}

test in assembly := {}

skip in packageJSDependencies := false
jsDependencies +=
  "org.webjars" % "jquery" % "2.1.4" / "2.1.4/jquery.js"

scalateTemplateConfig in Compile := {
  val base = (sourceDirectory in Compile).value
  Seq(
    TemplateConfig(
      base / "webapp" / "WEB-INF" / "templates",
      Seq.empty,  /* default imports should be added here */
      Seq(
        Binding("context", "_root_.org.scalatra.scalate.ScalatraRenderContext", importMembers = true, isImplicit = true)
      ),  /* add extra bindings here */
      Some("templates")
    )
  )
}

lazy val copyjs = TaskKey[Unit]("copyjs", "Copy javascript files to target directory")
copyjs := {
  val base = (sourceDirectory in Compile).value
  val outDir = baseDirectory.value / "target/webapp/scripts"
  val inDir = baseDirectory.value / "target/scala-2.11"
  val files = Seq("files-watcher-fastopt.js", "files-watcher-fastopt.js.map", "files-watcher-jsdeps.js") map { p =>   (inDir / p, outDir / p) }
  IO.copy(files, true)
}

addCommandAlias("server", ";jetty:stop ;jetty:start")
addCommandAlias("build", ";fastOptJS ;copyjs")

enablePlugins(JettyPlugin)
enablePlugins(ScalaJSPlugin)
