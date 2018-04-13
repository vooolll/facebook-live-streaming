name := "facebook-live-streaming"

version := "0.0.1"

scalaVersion := "2.11.9"

libraryDependencies ++= {
  val scalaLoggingV = "3.7.2"
  val typesafeV = "1.3.1"
  Seq(
    "com.typesafe.scala-logging"     %% "scala-logging"       % scalaLoggingV,
    "com.typesafe"                   %  "config"              % typesafeV
  )
}