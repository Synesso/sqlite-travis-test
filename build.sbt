name := "bug-test"

organization := "com.github.synesso"

version := sys.env.getOrElse("TRAVIS_BRANCH", "dev")

scalaVersion := "2.11.12"

fork in test := true

scalacOptions ++= Seq(
  "-unchecked",
  "-deprecation",
  "-feature",
  "-language:postfixOps",
  "-encoding",
  "UTF-8",
  "-target:jvm-1.8"
)

libraryDependencies ++= {
  val scalikejdbcJDBCV = "3.1.0"
  List(
    "org.xerial" % "sqlite-jdbc" % "3.19.3",
    "org.scalikejdbc" %% "scalikejdbc" % scalikejdbcJDBCV,
    "org.scalikejdbc" %% "scalikejdbc-config" % scalikejdbcJDBCV,
    "org.scalikejdbc" %% "scalikejdbc-test" % scalikejdbcJDBCV % "test",
    "org.specs2" %% "specs2" % "3.7" % "test"
  )
}

assemblyOutputPath in assembly := file(s"target/assembly/${name.value}-${version.value}.jar")

test in assembly := {}

assemblyMergeStrategy in assembly := {
  case PathList("scala", "xml", xs @ _*)            => MergeStrategy.first
  case PathList("osmosis-plugins.conf")             => MergeStrategy.first
  case PathList("com", "esotericsoftware", xs @ _*) => MergeStrategy.first
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}

// -- project
lazy val root = (project in file("."))
  .enablePlugins(BuildInfoPlugin)
  .settings(
    buildInfoKeys := Seq[BuildInfoKey](git.gitHeadCommit, scalaVersion),
    buildInfoPackage := "testing"
  )

