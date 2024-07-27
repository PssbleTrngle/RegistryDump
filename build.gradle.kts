plugins {
    idea
    id("com.possible-triangle.gradle") version ("0.1.5")
}

subprojects {
    enablePublishing {
        githubPackages()
    }
}

