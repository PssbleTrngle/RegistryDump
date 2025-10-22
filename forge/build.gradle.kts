plugins {
    id("com.possible-triangle.forge")
}

forge {
    dependOn(project(":common"))
}

upload {
    modrinth {
        syncBodyFromReadme()
    }
}
