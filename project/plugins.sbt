
// Compiles Scala to JavaScript: https://www.scala-js.org
addSbtPlugin("org.scala-js" % "sbt-scalajs" % "1.5.0")

// Creates a jar from the source code: https://github.com/sbt/sbt-native-packager
addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.7.6")

// Make information about our build, such as the application name, available in the application.
// We use this, for example, to get the name of the JavaScript file that contains the transpiled Scala of our app.
// https://github.com/sbt/sbt-buildinfo
addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.7.0")

addSbtPlugin("org.portable-scala" % "sbt-scalajs-crossproject" % "1.0.0")

addSbtPlugin("org.scala-js" % "sbt-jsdependencies" % "1.0.2")

addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.10")

addSbtPlugin("io.spray" % "sbt-revolver" % "0.9.1")
