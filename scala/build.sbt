name := """ssh-port-forward-study"""

version := "1.0-SNAPSHOT"

organization := "org.grimrose.sandbox"

scalaVersion := "2.12.2"

libraryDependencies ++= Seq(
  "mysql" % "mysql-connector-java" % "6.0.6",
  "org.scalikejdbc" %% "scalikejdbc" % "2.5.1",

  "fr.janalyse" %% "janalyse-ssh" % "0.10.1",
  "ch.qos.logback"  %  "logback-classic"   % "1.2.3",

  "org.pegdown" % "pegdown" % "1.6.0" % Test,
  "org.scalatest" %% "scalatest" % "3.0.1" % Test
)

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-unchecked",
  "-Xfatal-warnings",
  "-Xlint",
  "-Yno-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard",
  "-Xfuture"
)

wartremoverErrors in(Compile, compile) ++= Warts.unsafe

testOptions in Test ++= Seq(
  Tests.Argument(TestFrameworks.ScalaTest, "-h", "target/test-reports")
)
