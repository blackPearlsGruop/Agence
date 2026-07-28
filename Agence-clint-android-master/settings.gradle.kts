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


    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        google()
        mavenCentral()
        maven(url = "https://www.jitpack.io")
        maven(url = "https://jitpack.io")

    }
}

rootProject.name = "Agence"
include(":app", ":wave_record_util" , ":awesomeDialog")
 