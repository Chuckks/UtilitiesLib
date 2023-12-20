buildscript {
    repositories {
        google()
        mavenCentral()
        mavenLocal()
        gradlePluginPortal()
    }
    dependencies {

        classpath("com.github.dcendents:android-maven-gradle-plugin:2.1")
        classpath("com.android.tools.build:gradle:8.1.4")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.0")
    }
}