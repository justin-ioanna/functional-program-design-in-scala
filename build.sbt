scalaVersion := "2.13.1"

lazy val root = (project in file("."))
  .settings(
    name := "functional-program-design-in-scala",
    libraryDependencies ++= Seq(
      "com.novocode" % "junit-interface" % "0.11" % Test
    ),
    scalacOptions ++= Seq("-language:implicitConversions", "-deprecation"),
    testOptions in Test += Tests
      .Argument(TestFrameworks.JUnit, "-a", "-v", "-s")
  )
