//yaourt openjfx

//umanagedJars in Compile += file("lib/jfxrt.jar")
unmanagedJars in Compile += Attributed.blank(
  file(System.getenv("JAVA_HOME") + "/jre/lib/jfxrt.jar"))

fork in run := true
