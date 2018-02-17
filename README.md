**This is a WIP**, we are trying the [RDD](http://tom.preston-werner.com/2010/08/23/readme-driven-development.html) approach !


[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.vavr/vavr/badge.png)](https://maven-badges.herokuapp.com/maven-central/io.vavr/vavr)

[![codecov.io](http://codecov.io/github/ReactiveX/RxJava/coverage.svg?branch=2.x)](https://codecov.io/gh/ReactiveX/RxJava/branch/2.x)


[![Build Status](https://travis-ci.org/vavr-io/vavr.png)](https://travis-ci.org/vavr-io/vavr)

<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**Table of Contents**  *generated with [DocToc](https://github.com/thlorenz/doctoc)*

<!-- END doctoc generated TOC please keep comment here to allow auto update -->


# Approvals

Approvals is an lightweight open source assertion/verification library to aid unit testing.

It is compatible with JUnit.

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


```java
package com.examples;

import static org.approvals.*;

public class HelloTest {
    @Test
    public void approveHello() {
       Approvals.verify("hello");
    }
}
```

Examples
---
Approvals eats it own dogfood, so the best examples are in the source code itself.

None the less,  Here's a quick look at some
[Sample Code](https://github.com/approvals/ApprovalTests.Java/blob/master/java/org/approvaltests/tests/demos/SampleArrayTest.java)

	public class SampleArrayTest extends TestCase
	{
		public void testList() throws Exception
		{
			String[] names = {"Llewellyn", "James", "Dan", "Jason", "Katrina"};
			Arrays.sort(names);
			Approvals.verifyAll("", names);
		}
	}

Will Produce a File

    SampleTest.TestList.received.txt
    [0] = Dan
    [1] = James
    [2] = Jason
    [3] = Katrina
    [4] = Llewellyn

Simply rename this to SampleTest.testList.approved.txt and the test will now pass.

Approved File Artifacts
---

The `*.approved.*` files must be checked into source your source control. This can be an issue with git as it will change the line endings.
The suggested fix is to add
`*.approved.* binary` to your `.gitattributes`

## More Info

- Approvals is inspired by [ApprovalTests](http://approvaltests.sourceforge.net/)
- [Getting Started Doc](https://github.com/approvals/ApprovalTests.Java/blob/master/build/resources/approval_tests/documentation/ApprovalTests%20-%20GettingStarted.md)


## LICENSE
[Apache 2.0 License](https://github.com/SignalR/SignalR/blob/master/LICENSE.md)


## Questions?


