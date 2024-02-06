import sbt._
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import sbtcrossproject.CrossPlugin.autoImport.{CrossType, crossProject}
import org.scalajs.jsdependencies.sbtplugin.JSDependenciesPlugin.autoImport._
import org.portablescala.sbtplatformdeps.PlatformDepsPlugin.autoImport._
import sbt.Keys.scalaOrganization

// I am keeping alphabetical order. Should anyone eve get into this codebase - please do so too
object Dependencies {

  lazy val versions = new {
    val akkaActorVersion = "2.6.10"
    val akkaHttp         = "10.1.12"
    val akkaStream       = "2.6.10"
    val auth0JavaVersion  = "1.24.0"
    val autowireVersion  = "0.3.2"
    val circeVersion     = "0.14.1"
    val corsAkka         = "1.0.0"
    val doobie           = "0.9.0"
    val groovyAll        = "2.4.7"
    val javaxMail        = "1.4.7"
    val jwtAkka          = "1.1.6"
    val jQuery           = "3.1.1"
    val logbackClassic   = "1.2.3"
    val pureConfig       = "0.12.3"
    val pushy            = "0.15.0"
    val scalaLogging     = "3.9.2"
    val scalaTagsVersion = "0.9.2"
    val scalaTest        = "3.1.1"
    val scalaJsDomV      = "1.0.0"
    val sloggingVersion  = "0.6.2"
    val swaggerAkka      = "2.0.5"
    val javaxWsRs        = "2.1.1" // shady shit, Java 9 for some reason is lacking
    val uPickleVersion   = "1.2.2"
  }

  lazy val dependencies = new {
    val akkaHttp          = "com.typesafe.akka"            %% "akka-http"            % versions.akkaHttp
    val akkaHttpCache     = "com.typesafe.akka"            %% "akka-http-caching"    % versions.akkaHttp
    val akkaHttpSprayJson = "com.typesafe.akka"            %% "akka-http-spray-json" % versions.akkaHttp
    // Akka, which provides our HTTP server, logs using SLF4J: https://mvnrepository.com/artifact/com.typesafe.akka/akka-slf4j_2.12
    val akkaSl4j          = "com.typesafe.akka"            %% "akka-slf4j"           % versions.akkaActorVersion
    val akkaStream        = "com.typesafe.akka"            %% "akka-stream"          % versions.akkaStream
    // Type-safe Ajax calls between client and server: https://github.com/lihaoyi/autowire
    val akkaAutowire      = "com.lihaoyi"                  %% "autowire"             % versions.autowireVersion
    val auth0             = "com.auth0"                    % "auth0"                 % versions.auth0JavaVersion
    val corsAkka          = "ch.megard"                    %% "akka-http-cors"       % versions.corsAkka
    val doobiePostgres    = "org.tpolecat"                 %% "doobie-postgres"      % versions.doobie
    // Needed for reading the logback.groovy configuration file
    val groovyAll         = "org.codehaus.groovy"          % "groovy-all"            % versions.groovyAll
    val jwtAkka           = "com.emarsys"                  %% "jwt-akka-http"        % versions.jwtAkka
    val javaxMail         = "javax.mail"                   %  "mail"                 % versions.javaxMail
    val logbackClassic    = "ch.qos.logback"               %  "logback-classic"      % versions.logbackClassic
    val pureConfig        = "com.github.pureconfig"        %% "pureconfig"           % versions.pureConfig
    val pushy             = "com.eatthepath"               % "pushy"                 % versions.pushy
    val scalaLogging      = "com.typesafe.scala-logging"   %% "scala-logging"        % versions.scalaLogging
    val scalaSlogging     = "biz.enef"                     %% "slogging-slf4j"       % versions.sloggingVersion
    // The server creates HTML pages and CSS that it sends to the client
    val scalaTags         = "com.lihaoyi"                  %% "scalatags"            % versions.scalaTagsVersion
    val scalaTest         = "org.scalatest"                %  "scalatest_2.13"       % versions.scalaTest % "test"
    val swaggerAkka       = "com.github.swagger-akka-http" %% "swagger-akka-http"    % versions.swaggerAkka
    val javaxWsRs         = "javax.ws.rs"                  %  "javax.ws.rs-api"      % versions.javaxWsRs
    // Serializes data between client and server: https://github.com/lihaoyi/upickle-pprint
    val upickle           = "com.lihaoyi"                  %% "upickle"              % versions.uPickleVersion
  }

  val server  = Seq(
      dependencies.akkaHttp,
      dependencies.akkaHttpCache,
      dependencies.akkaHttpSprayJson,
      dependencies.akkaSl4j,
      dependencies.akkaStream,
      dependencies.akkaAutowire,
      dependencies.auth0,
      dependencies.corsAkka,
      dependencies.doobiePostgres,
      dependencies.groovyAll,
      dependencies.javaxMail,
      dependencies.jwtAkka,
      dependencies.logbackClassic,
      dependencies.pureConfig,
      dependencies.pushy,
      dependencies.scalaLogging,
      dependencies.scalaSlogging,
      dependencies.scalaTags,
      dependencies.scalaTest,
      dependencies.swaggerAkka,
      dependencies.javaxWsRs,
      dependencies.upickle

/*
       QUestionable deps
      // Hikari Connection Pool has a dependency on Slick
      // https://mvnrepository.com/artifact/com.typesafe.slick/slick-hikaricp_2.12
      "com.typesafe.slick" %% "slick-hikaricp" % "3.3.3",
      // https://mvnrepository.com/artifact/org.postgresql/postgresql
      "org.postgresql" % "postgresql" % "42.1.4"*/
    )

  object client {
    // JavaScript libraries the client uses
    val jsClientDependencies = Def.setting(Seq(
      // Used to add classes to HTML elements and also remove them: https://mvnrepository.com/artifact/org.webjars/jquery
      "org.webjars" % "jquery" % "3.1.1" / "3.1.1/jquery.min.js"
    ))

    // The triple % gets the library in two versions: One for running on the JVM and one for running on a JavaScript engine like V8
    val scalaJsDependencies = Def.setting(Seq(
      // Type-safe Ajax calls between client and server: https://github.com/lihaoyi/autowire
      "com.lihaoyi"       %%% "autowire"                    % versions.autowireVersion,
      "io.circe"          %%% "circe-core"                  % versions.circeVersion,
      "io.circe"          %%% "circe-generic"               % versions.circeVersion,
      "io.circe"          %%% "circe-parser"                % versions.circeVersion,
      "org.scala-js"      %%% "scalajs-dom"                 % versions.scalaJsDomV,
      "io.github.cquiroz" %%% "scala-java-time"             % "2.0.0",
      // A type facade for jQuery so we can use the JavaScript library in a type-safe manner: https://github.com/scala-js/scala-js-jquery
      "be.doeraene"       %%% "scalajs-jquery"              % "1.0.0",
      // Used to produce HTML and CSS with Scala on the client side: https://github.com/lihaoyi/scalatags
      "com.lihaoyi"       %%% "scalatags"                   % versions.scalaTagsVersion,
      // Logging: https://github.com/jokade/slogging
      "biz.enef"         %%% "slogging"                    % versions.sloggingVersion,
      // Serializes data between client and server: https://github.com/lihaoyi/upickle-pprint
      "com.lihaoyi"      %%% "upickle"                     % versions.uPickleVersion
    ))
  }
}
