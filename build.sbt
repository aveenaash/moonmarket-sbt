//import sbtprotobuf.{ProtobufPlugin=>PB}
//import scalabuff.ScalaBuffPlugin._

//seq(PB.protobufSettings: _*)

name := "Lift 2.5 starter template"

version := "0.0.1"

organization := "net.liftweb"

scalaVersion := "2.10.0"

resolvers ++= Seq("snapshots"     at "http://oss.sonatype.org/content/repositories/snapshots",
                  "staging"       at "http://oss.sonatype.org/content/repositories/staging",
                  "releases"      at "http://oss.sonatype.org/content/repositories/releases"
                 )

seq(webSettings :_*)

unmanagedResourceDirectories in Test <+= (baseDirectory) { _ / "src/main/webapp" }

scalacOptions ++= Seq("-deprecation", "-unchecked")

libraryDependencies ++= {
  val liftVersion = "2.5.1"
  Seq(
    "net.liftweb"             %% "lift-webkit"            % liftVersion           % "compile",
    "net.liftweb"             %% "lift-mapper"            % liftVersion           % "compile",
    "net.liftmodules"         %% "lift-jquery-module_2.5" % "2.4",
    "org.eclipse.jetty"       % "jetty-webapp"            % "8.1.7.v20120910"     % "container,test",
    "org.eclipse.jetty.orbit" % "javax.servlet"           % "3.0.0.v201112011016" % "container,test" artifacts Artifact("javax.servlet", "jar", "jar"),
    "ch.qos.logback"          % "logback-classic"         % "1.0.6",
    "org.specs2"              %% "specs2"                 % "1.14"                % "test",
    "mysql"                   %  "mysql-connector-java"   % "5.1.12",
    "com.h2database"          % "h2"                      % "1.3.167", 
    "org.elasticsearch"       % "elasticsearch"           % "1.0.0",
    "com.fasterxml.jackson.core" % "jackson-databind"     % "2.1.3",
    //"com.google.protobuf"     % "protobuf-java"           % "2.5.0",
    "net.sandrogrzicic"       %% "scalabuff-runtime"      % "1.3.7",
    "com.rabbitmq"            %  "amqp-client"            % "2.8.1",
    "net.liftmodules"         %% "amqp_2.5"               % "1.3", 
    "org.scalanlp" % "breeze_2.10" % "0.7",
    // native libraries are not included by default. add this if you want them (as of 0.7)
    // native libraries greatly improve performance, but increase jar sizes.
    "org.scalanlp" % "breeze-natives_2.10" % "0.7",
    "net.databinder.dispatch" %% "dispatch-core" % "0.11.0"
  )
}


//object build extends Build {
//	  lazy val root = Project("main", file("."), settings = Defaults.defaultSettings ++ scalabuffSettings).configs(ScalaBuff)
//}
port in container.Configuration := 8082
