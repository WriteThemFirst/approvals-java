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
    <a href='https://github.com/WriteThemFirst/approvals-java/tree/v0.2'>
        <img src='https://img.shields.io/github/commits-since/WriteThemFirst/approvals-java/v0.2.svg' />
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

Approvals is an lightweight open source assertion/verification library to facilitate unit testing. It alleviates the burden of hand-writing assertions.

<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

# Approvals

Traditional unit testing is based on hand-writing assertions on the output of your method.

With approvals-java you call instead `Approvals.verify(result)`.

1. The first time `verify` is called, a *received* file is generated with a representation of its argument
2. You review the content and *approve* it by renaming the file 
(this step is usually facilitated by a merge tool detected and launched by approvals)
3. You commit the *approved* file, it is now part of the unit test and specifies the behaviour of your code
4. Now each time `verify` is called, the argument is compared with the *approved* file. 
This replaces the calls to traditional `assert` methods.

Approval is compatible with most unit test frameworks like JUnit, AssertJ ... 
For Kotlin or Scala test frameworks a different way to invoke the verification can be needed, see the Wiki (TODO) for the details.


## What can it be used for?

Approvals can be used to verify objects which would usually require several hand-written assertions, such as:

- HashMaps & Collections
- Long Strings
- Files and folders
- Anything with a proper toString method

## How to get it

### Binary Releases

It is written in pure java and has no dependency on other frameworks.

You can find published releases on bintray (Maven Central to come soon).

<a href='https://bintray.com/writethemfirst/maven/approvals-java/_latestVersion'>
  <img src='https://api.bintray.com/packages/writethemfirst/maven/approvals-java/images/download.svg' />
</a>

        <dependency>
            <groupId>com.github.writethemfirst</groupId>
            <artifactId>approvals-java</artifactId>
            <version>0.2</version>
        </dependency>

For the moment you need to add the repository to your `pom.xml` or `settings.xml`:

        <repository>
            <snapshots>
                <enabled>false</enabled>
            </snapshots>
            <id>bintray-writethemfirst-maven</id>
            <name>bintray</name>
            <url>https://dl.bintray.com/writethemfirst/maven</url>
        </repository>

 
### How to use it

Have a look at our sample project [GildedRoseApprovalDemo](https://github.com/WriteThemFirst/GildedRoseApprovalDemo) 

#### Verify a simple object

```java
package com.examples;

import org.approvalsj.Approvals;

public class GildedRoseApprovalTest {
    Approvals approvals = new Approvals();

    @Test
    void approvalSwordShouldDeteriorate() {
        Item sword = new Item("basic sword", 10, 8);
        approvals.verify(GildedRose.nextDay(sword));
    }
}
```

The `toString()` of sword is used.

#### Verify each file in a folder

Coming soon !

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


#### Verify the output of a method when called with combinations of arguments

To be implemented.

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


## More Info

### ApprovalTests initial project

Approvals is inspired by [ApprovalTests](http://approvaltests.sourceforge.net/)

We liked the idea of approval testing but not so much the Java implementation ([Github](https://github.com/approvals/ApprovalTests.Java))
- not built with maven
- not published on a repository like Maven Central (you have to download the zip and add the jar manually to your project)
- not actively maintained (Pull Requests are not actively merged)
- code style not up to Java standards (developer is mainly working with .Net)

So we decided to implement quickly a subset of the initial features and deploy the dependency on Maven Central (coming soon!)

### Documentation

[Getting Started Doc](https://github.com/approvals/ApprovalTests.Java/blob/master/build/resources/approval_tests/documentation/ApprovalTests%20-%20GettingStarted.md)

[Javadoc](https://projects.raffael.ch/markdown-doclet/)

### Approved Files and Git

The `*.approved` files must be checked into source your source control. This can be an issue with git as it will change the line endings.
The suggested fix is to add `*.approved binary` to your `.gitattributes`


### LICENSE
[GNU General Public License v3.0](LICENSE.md)


### Contributing

See [CONTRIBUTING](CONTRIBUTING.md)
