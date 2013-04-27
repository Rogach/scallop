
organization := "org.rogach"

name := "scallop"

scalaVersion := "2.10.1"

scalacOptions ++= Seq(
  "-deprecation", 
  "-unchecked", 
  "-feature",
  "-language:postfixOps",
  "-language:reflectiveCalls",
  "-language:existentials",
  "-language:implicitConversions",
  "-Xlint",
  "-Ywarn-all"
)

Revolver.settings

resolvers += "Scala Tools Snapshots" at "http://scala-tools.org/repo-snapshots/"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "1.9.1" % "test"
)

crossScalaVersions := Seq("2.10.1")

unmanagedClasspath in Compile += file("dummy")

libraryDependencies <+= scalaVersion(sv => "org.scala-lang" % "scala-reflect" % sv)

publishTo <<= version { v: String =>
  val nexus = "https://oss.sonatype.org/"
  if (v.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

licenses := Seq(
  "MIT License" -> url("http://www.opensource.org/licenses/mit-license.php")
)

homepage := Some(url("https://github.com/Rogach/scallop"))

scmInfo := Some(
  ScmInfo(
    browseUrl = url("http://github.com/Rogach/scallop"),
    connection = "scm:git:git@github.com:Rogach/scallop.git"
  )
)


publishMavenStyle := true

publishArtifact in Test := false

pomIncludeRepository := { x => false }

pomExtra := (
  <developers>
    <developer>
      <id>rogach</id>
      <name>Platon Pronko</name>
      <url>http://rogach.org</url>
    </developer>
  </developers>
)

scalacOptions in (Compile, doc) ++= Opts.doc.sourceUrl("https://github.com/Rogach/scallop/tree/master/€{FILE_PATH}.scala")

parallelExecution in Test := false

site.settings

site.includeScaladoc("")

ghpages.settings

git.remoteRepo := "git@github.com:Rogach/scallop.git"

// fix for paths to source files in scaladoc
doc in Compile <<= (doc in Compile) map { in =>
  Seq("bash","-c",""" for x in $(find target/scala-2.10/api/ -type f); do sed -i "s_`pwd`/__" $x; done """).!
  in
}
