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
        jcenter() // إذا كان JCenter ضروريًا لبعض المكتبات القديمة


    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        jcenter() // إذا كان JCenter ضروريًا لبعض المكتبات القديمة

        maven(url = "https://www.jitpack.io")

    }
}

rootProject.name = "AgenceCompany"
include(":app", ":wave_record_util" , ":awesomeDialog")
 