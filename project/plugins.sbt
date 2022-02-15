resolvers += "Artifactory Realm" at "https://artifactory.firstbird.com/sbt-plugins-local"
credentials += Credentials(Path.userHome / ".sbt" / "credentials")

addSbtPlugin("com.firstbird"          % "sbt-firstbird" % "1.15.2")
addSbtPlugin("com.firstbird.underpin" % "sbt-underpin"  % "1.6.0")
