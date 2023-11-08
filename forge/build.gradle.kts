forge {
    dependOn(project(":common"))
}

uploadToCurseforge()
uploadToModrinth {
    syncBodyFromReadme()
}