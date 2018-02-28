<p align='center'>
    <img alt='Write Them First!' src='https://raw.githubusercontent.com/WriteThemFirst/approvals-java/master/etc/logo.png' />
</p>
<p align='center'>
    <a href='https://travis-ci.org/WriteThemFirst/approvals-java'>
        <img src='https://travis-ci.org/WriteThemFirst/approvals-java.svg?branch=master' />
    </a>
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
    <a href='https://github.com/WriteThemFirst/approvals-java/tree/v0.1'>
        <img src='https://img.shields.io/github/commits-since/WriteThemFirst/approvals-java/v0.1.svg' />
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

**This is a WIP**, we are trying the [RDD](http://tom.preston-werner.com/2010/08/23/readme-driven-development.html) approach !


<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**Table of Contents**  *generated with [DocToc](https://github.com/thlorenz/doctoc)*

<!-- END doctoc generated TOC please keep comment here to allow auto update -->


# Approvals

Approvals is an lightweight open source assertion/verification library to aid unit testing. 
It alleviates the burden of hand-writing assertions.
Instead you call a `verify` method.

1. The first time `verify` is called, a file is generated with a representation of its argument
2. You review the content and *approve* it by renaming the file
3. You commit the file, it is now part of the unit test and contains part of the specification for your code
4. Now each time `verify` is called, the argument is compared with the *approved* file. 
This replaces the calls to traditional `assert` methods.

Approval is compatible with most unit test frameworks like JUnit.


## What can it be used for?

Approvals can be used to verify objects which would usually require several hand-written assertions, such as:

- HashMaps & Collections
- Long Strings
- Files and folders
- Anything with a proper toString method

## How to get it

### Binary Releases

It is written in pure java and has no dependency on other frameworks.

Version 1.2.1 and earlier were built for Java 6, while newer
versions (1.3.0 and above) will be built for Java 8.

You can find published releases on Maven Central.

    <dependency>
        <groupId>com.typesafe</groupId>
        <artifactId>config</artifactId>
        <version>1.3.1</version>
    </dependency>


Link for direct download if you don't use a dependency manager:

 - http://central.maven.org/maven2/com/typesafe/config/
 
### How to use it

#### Verify a simple object

```java
package com.examples;

import org.approvalsj.Approvals;

public class GildedRoseApprovalTest {
    Approvals approvals = new Approvals(getClass());

    @Test
    void approvalSwordShouldDeteriorate() {
        Item sword = new Item("basic sword", 10, 8);
        approvals.verify(GildedRose.nextDay(sword));
    }
}
```

The `toString()` of sword is used.

#### Verify the output of a method when called with combinations of arguments

```java
@Test
void approvalBrieShouldImprove() throws Exception {
    Approvals approvals = new Approvals(getClass());

    Integer[] sellInDays = {-5, 0, 1, 20};
    Integer[] qualities = {0, 10};

    approvals.verifyAllCombinations((sellIn, quality) ->
            GildedRose.nextDay(new Item(BRIE, sellIn, quality)), sellInDays, qualities);
}
```

`GildedRose.nextDay` is called 8 times, each time with an instance of `Item` constructed with a possible combination of `sellIn` and `quality`.

#### Verify each file in a folder

```java
@Test
void approvalCopySrcFolder() throws Exception {
    Approvals approvals = new Approvals(getClass());
    
    Path outDir = Files.createTempDirectory("src");
    FolderCopy.copyFrom(Paths.get("."), outDir);
    approvals.verifyEachFileInDirectory(outDir.toFile(), f -> f.getName().endsWith(".xml"));
}
```

Each file in `outDir` is checked against the master directory.

## More Examples

Approvals eats it own dogfood, so the best examples are in the source code itself.

None the less,  Here's a quick look at some
[Sample Code](https://github.com/approvals/ApprovalTests.Java/blob/master/java/org/approvaltests/tests/demos/SampleArrayTest.java)

```java
public class SampleArrayTest extends TestCase {
    public void testList() {
        String[] names = {"Llewellyn", "James", "Dan", "Jason", "Katrina"};
        Arrays.sort(names);
        Approvals.verifyAll(names);
    }
}
```

Will Produce a File

    SampleTest.TestList.received
    [0] = Dan
    [1] = James
    [2] = Jason
    [3] = Katrina
    [4] = Llewellyn

Simply rename this to `SampleTest.testList.approved` and the test will now pass.

## More Info

### ApprovalTests initial project

Approvals is inspired by [ApprovalTests](http://approvaltests.sourceforge.net/)

We liked the idea of approval testing but not so much the Java implementation ([Github](https://github.com/approvals/ApprovalTests.Java))
- not built with maven
- not published on a repository like Maven Central (you have to download the zip and add the jar manually to your project)
- not actively maintained (Pull Requests are not actively merged)
- code style not up to Java standards (developer is mainly working with .Net)

So we decided to implement quickly a subset of the initial features and deploy the dependency on Maven Central 
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.vavr/vavr/badge.png)](https://maven-badges.herokuapp.com/maven-central/io.vavr/vavr)


### Documentation

[Getting Started Doc](https://github.com/approvals/ApprovalTests.Java/blob/master/build/resources/approval_tests/documentation/ApprovalTests%20-%20GettingStarted.md)

[Javadoc](https://projects.raffael.ch/markdown-doclet/)

### Approved Files and Git

The `*.approved` files must be checked into source your source control. This can be an issue with git as it will change the line endings.
The suggested fix is to add `*.approved binary` to your `.gitattributes`


### LICENSE
[Apache 2.0 License](https://github.com/SignalR/SignalR/blob/master/LICENSE.md)


### Contributing

See [CONTRIBUTING](CONTRIBUTING.md)
