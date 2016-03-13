atam4j
======

[![Join the chat at https://gitter.im/atam4j/atam4j](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/atam4j/atam4j?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

Acceptance Tests As Monitors

* A simple library that allows you to run junit tests as monitoring checks.
* Runs tests on a schedule and exposes results via a restful api that can be called from your monitoring setup (e.g. nagios, 
zabbix, icinga, pingdom etc etc)

## Latest Build Status

[![Build Status](https://travis-ci.org/atam4j/atam4j.svg?branch=master)](https://travis-ci.org/atam4j/atam4j)

## Maven Dependency
    <dependency>    
       <groupId>me.atam</groupId>    
       <artifactId>atam4j</artifactId>    
       <version>0.51</version>    
    </dependency>
    
[http://mvnrepository.com/artifact/me.atam/atam4j](http://mvnrepository.com/artifact/me.atam/atam4j)

## Using atam4j

1. Include the atam4j maven dependency.

2. Write Junit based tests in the usual manner, with the exception of including them in the main application classpath 
instead of the tests classpath. This can be done by including them in the `src/main/java directory` instead of the 
`src/test/java` directory. You can choose to either add a `@Monitor` annotation to your test classes and let atam4j 
detect them or simply supply an array of classes to the builder.

3. Instantiate `Atam4j` in your application.    

    If specifying an explicit array of Test classes:

        Atam4j atam4j = new Atam4j.Atam4jBuilder(jerseyEnvironment)     
            .withTestClasses(HelloWorldTest.class) 
            .build()      
            .initialise();
            
    If using `@Monitor` annotations to auto-detect test classes:            
            
        Atam4j atam4j = new Atam4j.Atam4jBuilder(jerseyEnvironment)      
            .build()      
            .initialise();     
                   
    where `jerseyEnvironment` is an object of type `io.dropwizard.jersey.setup.JerseyEnvironment`
    
    Use the `stop()` method to halt the test runs:
    
        atam4j.stop()

4. Run your app and observe the status of the acceptance tests reported by the `/tests` endpoint.

Refer to [atam4j-sample-app](acceptance-tests/src/main/java) for a complete working example.

### Available Versions
#### Snapshots
[https://oss.sonatype.org/content/repositories/snapshots/me/atam/atam4j/](https://oss.sonatype.org/content/repositories/snapshots/me/atam/atam4j/)

#### Releases
Look for non-snapshot version
[https://oss.sonatype.org/content/groups/public/me/atam/atam4j/](https://oss.sonatype.org/content/groups/public/me/atam/atam4j/)

### Additional usage documentation
Refer to our [Wiki](https://github.com/atam4j/atam4j/wiki)

## Developing atam4j
    
### Build and Release

#### Building Locally
> mvn clean install

#### Running Tests using Maven CLI
> mvn clean test

#### Release - Manual
##### Prerequisite
Only [core committers](Core-Committers.md) can release atam4j to maven central. You need Sonatype Nexus OSS account info
for atam.me.

##### Steps

1. Set version of the release    
> mvn versions:set -DnewVersion=${versionNumber}

2. Commit the version back to git and push to remote 
> git commit -a -m "Prepare release v${versionNumber}"    
git push

3. Tag code 
> git tag -a v${versionNumber} -m 'version ${versionNumber}'     
git push origin v${versionNumber}
    
4. Create release in Github - [https://github.com/atam4j/atam4j/releases](https://github.com/atam4j/atam4j/releases)    

5. Deploy to maven central    
> mvn clean deploy

### Overview of modules

#### acceptance-tests
A module which tests atam4j from end to end by starting an application that imports the core-library and runs dummy-tests.

#### atam4j
The core library that runs tests and exposes the results via an api.

#### atam4j-annotations
Module containing the atam4j annotations only.  If you wish to annotate your tests, this is the only dependency required.

#### dummy-tests
Contains dummy tests to verify that atam4j reports correctly on the status of tests that we know in advance will pass or fail.  These are not run during the build.

#### test-utils
Contains some common code shared by tests.








