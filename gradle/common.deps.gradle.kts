dependencies {
  "implementation"(platform("io.knotx:knotx-dependencies:${project.version}"))
  "implementation"(group = "io.vertx", name = "vertx-core")
  "implementation"(group = "io.vertx", name = "vertx-service-proxy")
  "implementation"(group = "io.vertx", name = "vertx-rx-java2")
  "implementation"(group = "io.vertx", name = "vertx-codegen")

  "testImplementation"(group = "io.knotx", name = "knotx-junit5")
  "testImplementation"(group = "io.vertx", name = "vertx-junit5")
  "testImplementation"(group = "io.vertx", name = "vertx-unit")
}
