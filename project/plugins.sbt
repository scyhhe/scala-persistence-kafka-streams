resolvers += "Artifactory Realm" at "https://artifactory.firstbird.com/sbt-plugins-local"
credentials += Credentials(Path.userHome / ".sbt" / "credentials")

addSbtPlugin("com.firstbird"          % "sbt-firstbird" % "1.16.0")
addSbtPlugin("com.firstbird.underpin" % "sbt-underpin"  % "1.7.0-RC4+65-dda8d56f")
