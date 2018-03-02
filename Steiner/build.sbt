//yaourt openjfx

 unmanagedJars in Compile += file("lib/jfxrt.jar")
// unmanagedJars in Compile += Attributed.blank(
//  file("/home/ishkhan/Programs/jdk1.8.0_111/jre/lib/ext/jfxrt.jar"))
//   file(System.getenv("JAVA_HOME") + "/jre/lib/jfxrt.jar"))
fork in run := true
