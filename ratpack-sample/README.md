Ratpack Sample Project
-----------------------------

This is the sample project which tryed to implement a tiny web application using Ratpack 1.0.0.

This project generated by Lazybones.
I made [this article](http://grimrose.bitbucket.org/blog/html/2014/12/07/g_advent_calendar_2014_ratpack_02.html) as a reference.

You have just created a basic Groovy Ratpack application. It doesn't do much
at this point, but we have set you up with a standard project structure, a 
Guice back Registry, simple home page, and Spock for writing tests (because 
you'd be mad not to use it).

In this project you get:

* A Gradle build file with pre-built Gradle wrapper
* A tiny home page at src/ratpack/templates/index.html (it's a template)
* A routing file at src/ratpack/Ratpack.groovy
* Reloading enabled in build.gradle
* A standard project structure:

```
    <proj>
      |
      +- src
          |
          +- ratpack
          |     |
          |     +- Ratpack.groovy
          |     +- ratpack.properties
          |     +- public // Static assets in here
          |          |
          |          +- images
          |          +- lib
          |          +- scripts
          |          +- styles
          |
          +- main
          |   |
          |   +- groovy
                   |
                   +- // App classes in here!
          |
          +- test
              |
              +- groovy
                   |
                   +- // Spock tests in here!
```

That's it! You can start the basic app with

    ./gradlew run

but it's up to you to add the bells, whistles, and meat of the application.