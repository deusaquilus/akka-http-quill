lazy val `akka-http-quill` = (project in file("."))
  .settings(
  	organization := "io.getquill",
  	scalaVersion := "2.11.12",
  	libraryDependencies ++= Seq(
  	  "io.getquill" %% "quill-jdbc" % "3.1.0",
  	  "org.postgresql" % "postgresql" % "42.2.5",
			"com.typesafe.akka" %% "akka-http"   % "10.1.8",
			"com.typesafe.akka" %% "akka-stream" % "2.5.19"
  	)
  )
