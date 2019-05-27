<p align='center'>
    <img alt='Write Them First!' src='https://raw.githubusercontent.com/WriteThemFirst/approvals-java/master/etc/logo.png' />
</p>
<p align='center'>
    <a href='https://travis-ci.org/WriteThemFirst/approvals-java'>
        <img src='https://travis-ci.org/WriteThemFirst/approvals-java.svg?branch=master' />
    </a>
    <a href='http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.github.writethemfirst%22%20AND%20a%3A%22approvals-java%22'>
        <img src='https://img.shields.io/maven-central/v/com.github.writethemfirst/approvals-java.svg' />
    </a>
    <a href='https://www.javadoc.io/doc/com.github.writethemfirst/approvals-java'>
        <img src='https://www.javadoc.io/badge/com.github.writethemfirst/approvals-java.svg' />
    </a>
</p>
<p align='center'>
    <a href='https://bintray.com/writethemfirst/maven/approvals-java/_latestVersion'>
        <img src='https://api.bintray.com/packages/writethemfirst/maven/approvals-java/images/download.svg' />
    </a>
    <a href='https://codecov.io/gh/WriteThemFirst/approvals-java'>
        <img src='https://codecov.io/gh/WriteThemFirst/approvals-java/branch/master/graph/badge.svg' />
    </a>
    <a href='https://codeclimate.com/github/WriteThemFirst/approvals-java/maintainability'>
        <img src='https://api.codeclimate.com/v1/badges/b5d49999f3d09bae95ce/maintainability' />
    </a>
    <a href='https://codeclimate.com/github/WriteThemFirst/approvals-java/test_coverage'>
        <img src='https://api.codeclimate.com/v1/badges/b5d49999f3d09bae95ce/test_coverage' />
    </a>
</p>
<p align='center'>
    <a href='http://hits.dwyl.io/WriteThemFirst/approvals-java'>
        <img src='http://hits.dwyl.io/WriteThemFirst/approvals-java.svg' />
    </a>
    <a href='https://github.com/WriteThemFirst/approvals-java/tree/v0.9.0'>
        <img src='https://img.shields.io/github/commits-since/WriteThemFirst/approvals-java/v0.9.0.svg' />
    </a>
    <a href='https://github.com/WriteThemFirst/approvals-java/issues/'>
        <img src='https://img.shields.io/github/issues/WriteThemFirst/approvals-java.svg' />
    </a>
    <a href='https://github.com/WriteThemFirst/approvals-java/issues?q=is%3Aissue+is%3Aclosed'>
        <img src='https://img.shields.io/github/issues-closed/WriteThemFirst/approvals-java.svg' />
    </a>
    <a href='https://github.com/WriteThemFirst/approvals-java'>
        <img src='https://img.shields.io/github/languages/code-size/WriteThemFirst/approvals-java.svg' />
    </a>
</p>
<p align='center'>
    <a href='https://www.gnu.org/licenses/gpl-3.0'>
        <img src='https://img.shields.io/badge/License-GPL%20v3-blue.svg' />
    </a>
    <a href='http://semver.org/spec/v2.0.0.html'>
        <img src='https://img.shields.io/SemVer/2.0.0.png' />
    </a>
    <a href='https://github.com/WriteThemFirst/approvals-java/pulls'>
        <img src='https://img.shields.io/badge/made%20with-%E2%99%A5-pink.svg' />
    </a>
</p>

---

# Approvals-Java

Approvals-Java is a lightweight open source assertion/verification library to facilitate unit testing. It alleviates the burden of hand-writing assertions.

<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->

- [What's new?](#whats-new)
- [Get Approvals-Java](#get-approvals-java)
  - [Maven](#maven)
  - [Gradle](#gradle)
  - [Nightly Builds](#nightly-builds)
  - [Requirements](#requirements)
- [Why using Approvals-Java?](#why-using-approvals-java)
  - [Approval testing basics](#approval-testing-basics)
  - [Approvals-Java basics](#approvals-java-basics)
  - [What about other testing libraries?](#what-about-other-testing-libraries)
  - [Is it Java only?](#is-it-java-only)
  - [Usage examples?](#usage-examples)
- [How to use Approvals-Java?](#how-to-use-approvals-java)
  - [Sample project](#sample-project)
  - [Verify a simple object](#verify-a-simple-object)
  - [Verify each file in a folder](#verify-each-file-in-a-folder)
- [Advanced documentation](#advanced-documentation)
- [Frequently Asked Questions](#frequently-asked-questions)
- [Help/Contribute](#helpcontribute)
- [Thanks/Inspiration](#thanksinspiration)
- [The team?](#the-team)
- [License](#license)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

# What's new?

Just have a look at our [Releases Notes](https://github.com/WriteThemFirst/approvals-java/releases)!

# Get Approvals-Java

Approvals-Java is released on <a href='http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.github.writethemfirst%22%20AND%20a%3A%22approvals-java%22'><img src='https://img.shields.io/maven-central/v/com.github.writethemfirst/approvals-java.svg' /></a> which means you don't need any particular Maven/Gradle configuration to retrieve it.

Also, it is written in pure Java and has no additional dependencies.

## Maven

In your `pom.xml`, add this dependency:

```xml
<dependency>
    <groupId>com.github.writethemfirst</groupId>
    <artifactId>approvals-java</artifactId>
    <version>0.9.0</version>
    <scope>test</scope>
</dependency>
```

## Gradle

In your `build.gradle`, add this dependency:

```groovy
testCompile 'com.github.writethemfirst:approvals-java:0.9.0'
```

## Sbt (Scala users)

In your `build.sbt`, add this dependency:

```scala
libraryDependencies += "com.github.writethemfirst" % "approvals-java" % "0.9.0"
```


## Nightly Builds

Our `SNAPSHOT` versions are released on [oss.jfrog.org](https://oss.jfrog.org/artifactory/oss-snapshot-local). To use them, simply add this repository to your `pom.xml` or to your `settings.xml`:

```xml
<repositories>
    <repository>
        <snapshots>
            <enabled>true</enabled>
        </snapshots>
        <id>oss-jfrog-snapshot</id>
        <name>oss-jfrog-snapshot</name>
        <url>https://oss.jfrog.org/artifactory/oss-snapshot-local</url>
    </repository>
</repositories>
```

And then you can simply rely on the latest `SNAPSHOT`:

```xml
<dependency>
    <groupId>com.github.writethemfirst</groupId>
    <artifactId>approvals-java</artifactId>
    <version>0.8.1-SNAPSHOT</version>
    <scope>test</scope>
</dependency>
```

## Requirements

Approvals-Java has been tested to work efficiently with:

- Windows 7+,
- Linux,
- Java 8,
- JUnit 5.

If you use it in other contexts, do not hesitate to let us know!

# Why using Approvals-Java?

## Approval testing basics

Traditional unit testing is based on hand-writing assertions on the output of your method. This might sound boring for some people, or even sometimes really hard in case of working on some legacy source code.

*Approval Testing* is a way of approching assertions with the following principle:

1. You first execute the source code you'd like to test and let it produce its usual output,
2. You review it manually, and say if it's producing the results you expect,
3. All future test executions will actually compare the produced results with what has been previsouly approved.

Which means you no longer write assertions... You just approve the data which will be used by assertions computer by the framework.

## Approvals-Java basics

Approvals-Java is a simple Java framework allowing you to compute verifications of what your source code is doing, relying on *Approval Testing* principles.

Instead of writing tons of assertions, you simply call `approvals.verify(result);`.

1. The first time `verify` is called, a *received* file is generated with a representation of its argument,
2. You review the content and *approve* it by renaming the file, *(this step is usually facilitated by a merge tool detected and launched by Approvals-Java)*
3. You commit the *approved* file, it is now part of the unit test and specifies the behaviour of your code,
4. Now each time `verify` is called, the argument is compared with the *approved* file.

This replaces the calls to traditional `assert` methods.

## What about other testing libraries?

Approvals-Java is compatible with most unit test frameworks and libraries such as JUnit, AssertJ, Mockito, etc. Since it's actually doing another job.

## Is it Java only?

Approvals-Java should be able to work fine while being called from Scala or Kotlin, at least we're working on that topic. There might be a few things to take in consideration while calling the framework though. Refer to our [wiki](https://github.com/WriteThemFirst/approvals-java/wiki) to get some details.

## Usage examples?

Approvals-Java can be used to verify objects which would usually require several hand-written assertions, such as:

- HashMaps & Collections,
- Long Strings,
- Files and folders,
- Anything with a proper toString method...

And for sure lots of other usages you will find out!

# How to use Approvals-Java?

Please note that most of our code samples are based on the [Gilded Rose Kata](https://github.com/emilybache/GildedRose-Refactoring-Kata). Do not hesitate to check it out ;)

## Sample project

First, if you'd just want a sample project to see it in action, [we have one for you](https://github.com/WriteThemFirst/GildedRoseApprovalDemo)!

## Verify a simple object

```java
package com.examples;

import com.github.writethemfirst.approvals.Approvals;

public class GildedRoseApprovalTest {
    private Approvals approvals = new Approvals();

    @Test
    void approvalSwordShouldDeteriorate() {
        final Item sword = new Item("basic sword", 10, 8);
        approvals.verify(GildedRose.nextDay(sword));
    }
}
```

The `toString()` of `sword` is used for representing the data to be stored in the *approved* file.

## Verify each file in a folder

```java
package com.examples;

import com.github.writethemfirst.approvals.Approvals;

public class GildedRoseApprovalTests {
    @Test
    void approvalCopySrcFolder() {
        final Approvals approvals = new Approvals();

        final Path output = Files.createTempDirectory("src");
        FolderCopy.copyFrom(Paths.get("."), output);
        approvals.verifyAgainstMasterFolder(output);
    }
}
```

Each file in `output` is checked against the master directory.


## Verify a method with combinations of arguments

This can save you a lot of time instead of manual assertions, and still cover for limit cases
like those which [mutation testing](http://pitest.org/) detected.

```java
package com.examples;

import com.github.writethemfirst.approvals.Approvals;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

class GildedRoseApprovalTest {

    private Approvals approvals = new Approvals();

    @Test
    void updateQuality_pass_shouldEvolve() {
        approvals.verifyAll(
            singletonList("Backstage passes"),
            asList(-1, 0, 1, 5, 6, 10, 11),
            asList(-1, 0, 1, 10),
            this::doTest);
    }

    private Item doTest(final String name, final int sellIn, final int quality) {
        final Item[] items = new Item[]{new Item(name, sellIn, quality)};
        final GildedRose app = new GildedRose(items);
        app.updateQuality();
        return app.items[0];
    }
}
```

Each of the 28 (1x7x4) combinations of `name`, `sellIn`, `quality` is used to call `doTest(name, sellIn, quality)`.

The 28 results are stored in the *received* text file and compared with the *approved* text file, which should look like:

    (Backstage passes, -1, -1) => Backstage passes, -2, 0
    (Backstage passes, -1, 0) => Backstage passes, -2, 0
    (Backstage passes, -1, 1) => Backstage passes, -2, 0
    (Backstage passes, -1, 10) => Backstage passes, -2, 0
    (Backstage passes, 0, -1) => Backstage passes, -1, 0
    (Backstage passes, 0, 0) => Backstage passes, -1, 0
    (Backstage passes, 0, 1) => Backstage passes, -1, 0
    (Backstage passes, 0, 10) => Backstage passes, -1, 0
    (Backstage passes, 1, -1) => Backstage passes, 0, 2
    (Backstage passes, 1, 0) => Backstage passes, 0, 3
    (Backstage passes, 1, 1) => Backstage passes, 0, 4
    (Backstage passes, 1, 10) => Backstage passes, 0, 13
    (Backstage passes, 5, -1) => Backstage passes, 4, 2
    (Backstage passes, 5, 0) => Backstage passes, 4, 3
    (Backstage passes, 5, 1) => Backstage passes, 4, 4
    (Backstage passes, 5, 10) => Backstage passes, 4, 13
    (Backstage passes, 6, -1) => Backstage passes, 5, 1
    (Backstage passes, 6, 0) => Backstage passes, 5, 2
    (Backstage passes, 6, 1) => Backstage passes, 5, 3
    (Backstage passes, 6, 10) => Backstage passes, 5, 12
    (Backstage passes, 10, -1) => Backstage passes, 9, 1
    (Backstage passes, 10, 0) => Backstage passes, 9, 2
    (Backstage passes, 10, 1) => Backstage passes, 9, 3
    (Backstage passes, 10, 10) => Backstage passes, 9, 12
    (Backstage passes, 11, -1) => Backstage passes, 10, 0
    (Backstage passes, 11, 0) => Backstage passes, 10, 1
    (Backstage passes, 11, 1) => Backstage passes, 10, 2
    (Backstage passes, 11, 10) => Backstage passes, 10, 11


# Advanced documentation

If you can't find the information you're searching for in [our documentation](README.md)
or in our [code sample](https://github.com/WriteThemFirst/GildedRoseApprovalDemo),
then don't hesitate to have a look at our [FAQ](FAQ.md) or
[Javadoc](https://www.javadoc.io/doc/com.github.writethemfirst/approvals-java/).

# Frequently Asked Questions

Don't hesitate to have a quick look at our [Frequently Asked Questions](FAQ.md) before submitting an issue.

# Help/Contribute

This project is completely open to any contributions!
*(and remember: feedback is a valuable contribution!)*

Do not hesitate to:

1. [Submit issues](https://github.com/WriteThemFirst/approvals-java/issues/new)
  about any feedbacks you may have about the library,
2. [Send us a Pull Request](https://github.com/WriteThemFirst/approvals-java/pulls)
  with any contribution you think about,
3. [Have a look at open issues](https://github.com/WriteThemFirst/approvals-java/issues)
  if you want to find a topic to work on,
4. Do not hesitate to have a look at
  [good first issues](https://github.com/WriteThemFirst/approvals-java/issues?q=is%3Aopen+is%3Aissue+label%3A%22%3A%2B1%3A+good+first+issue%22)
  or [help wanted issues](https://github.com/WriteThemFirst/approvals-java/issues?q=is%3Aopen+is%3Aissue+label%3A%22%3Asos%3A+help+wanted%22)
  if you search for something to start with!
5. Get in touch with us to discuss about what you'd like to contribute if you don't feel like starting alone ;)

Before contributing though, please have a look
at our [Code of Conduct](CODE_OF_CONDUCT.md) *(because we value humans and their differences)*
and to our [Contribution Guide](CONTRIBUTING.md) *(because we think that a few rules allow to work faster and safer)*.

Do not hesitate to discuss anything from those documents if you feel they need any modification though.

# Thanks/Inspiration

Approvals-Java is inspired by [ApprovalTests](http://approvaltests.sourceforge.net/).

We really liked the idea of approval testing but not so much the Java implementation
([Github](https://github.com/approvals/ApprovalTests.Java)).

Our main concerns were that:

- it is not published on Maven Central, so you need to add the jar manually to your project,
- it is not actively maintained (Pull Requests are not actively merged),
- the code style is not up to Java standards (developer is mainly working with .Net).

So we decided to implement quickly a subset of the initial features and deploy the dependency on Maven Central!

Thanks a lot to [all the people behind Approvals](https://github.com/orgs/approvals/people),
because we got the inspiration from their work!

Thanks also to all people who created those tools we love:

- [Gitmoji](https://gitmoji.carloscuesta.me/)
- [Gitmoji for IDEA](https://plugins.jetbrains.com/plugin/10315-gitmoji)
- [doctoc](https://github.com/thlorenz/doctoc)

# The team?

[Write Them First!](https://github.com/WriteThemFirst) is just a bunch of french developers
who strongly believe that automated tests are extremely important in software development.

Since they also value [TDD](https://en.wikipedia.org/wiki/Test-driven_development)
or [BDD](https://en.wikipedia.org/wiki/Behavior-driven_development),
they decided to create a few *(at least one)* tools to make those activities easier!

# License

Our code is released under [GNU General Public License v3.0](LICENSE).
