pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
        jcenter() // needed by some older libraries of the member side


    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        jcenter() // needed by some older libraries of the member side
        maven(url = "https://www.jitpack.io")
        maven(url = "https://jitpack.io")

    }
}

rootProject.name = "Agence"
include(":app", ":wave_record_util" , ":awesomeDialog")
 