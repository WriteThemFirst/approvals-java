# Contributing to Approvals-Java

First of all, thanks a lot for at least considering a contribution to [approvals-java](https://github.com/WriteThemFirst/approvals-java)!

Please keep in mind that all contributions are valuable, and that **feedbacks are contributions**, so do not hesitate to let us know about your remarks or feelings about the library if you don't feel like doing other things! It's always nice to get feedbacks!

<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->

- [Getting in touch with the team](#getting-in-touch-with-the-team)
- [About Approvals-Java](#about-approvals-java)
- [Environment & Tooling](#environment--tooling)
- [Contribution Conventions](#contribution-conventions)
  - [Source Code](#source-code)
  - [Formatting](#formatting)
  - [Javadoc](#javadoc)
  - [Documentation](#documentation)
  - [Commits](#commits)
  - [Versions](#versions)
- [Processes and Operations](#processes-and-operations)
  - [Releasing the library](#releasing-the-library)
- [Useful commands](#useful-commands)
  - [Licenses in source headers](#licenses-in-source-headers)
  - [Table of Contents in Markdown files](#table-of-contents-in-markdown-files)
  - [Validate your tests coverage with mutations](#validate-your-tests-coverage-with-mutations)
  - [Generate the changelog to put in the release notes on GitHub](#generate-the-changelog-to-put-in-the-release-notes-on-github)
- [Development Tips'n'Tricks](#development-tipsntricks)
  - [Adding a new Reporter](#adding-a-new-reporter)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

Oh, and also, remember our [Code of Conduct](CODE_OF_CONDUCT.md) ;)

# Getting in touch with the team

There are plently of ways for you to get in touch with us:

1. Direct contact with [one of us](https://github.com/orgs/WriteThemFirst/people) (Twitter, Mail, etc.),
2. [Creating an issue](https://github.com/WriteThemFirst/approvals-java/issues/new) on Approvals-Java,
3. [Submitting a Pull Request](https://github.com/WriteThemFirst/approvals-java/pulls) with changes (even some Work In Progress).

If you'd just like to contribute but don't have much ideas on what to do, feel free to check [good first issues](https://github.com/WriteThemFirst/approvals-java/issues?q=is%3Aopen+is%3Aissue+label%3A%22%3A%2B1%3A+good+first+issue%22) or [help wanted issues](https://github.com/WriteThemFirst/approvals-java/issues?q=is%3Aopen+is%3Aissue+label%3A%22%3Asos%3A+help+wanted%22).

# About Approvals-Java

Approvals-Java is a pure Java framework with no additional dependencies. That choice has been made to ensure that the library is the easiest possible to integrate in a project and won't lead to conflicts or anything like this.

The framework is dealing a lot with the filesystem and will be used both from developers' workstations and CI servers. Everything should work fine both on Windows & Linux.

It is relying on some Java 8 features, so won't be compliant with earlier Java versions. Further Java versions are to be supported though.

We'd like Approvals-Java to be usable from Scala or Kotlin source code as well, so we're keeping that aspect in mind.

Finally, the library's main goal is to be simple and easy to use, so we want to ensure the documentation and source code are clear and readable.

# Environment & Tooling

You can use whatever you want to develop, but here's a list of what we use so you can get some ideas out of it:

- [archlinux](https://www.archlinux.org/) or [windows](https://www.microsoft.com/en-us/windows)
- [IntelliJ IDEA](https://www.jetbrains.com/idea/)
- [OpenJDK 8](http://openjdk.java.net/projects/jdk8/)
- [Git](https://git-scm.com/)
- [Maven 3.5+](https://maven.apache.org/)

Also, we're using those additional tools:

- [Gitmoji](http://gitmoji.carloscuesta.me/)
    - And its [IDEA Plugin](https://plugins.jetbrains.com/plugin/10315-gitmoji)
    - Also [this one helps](https://plugins.jetbrains.com/plugin/9174-emoji-support-plugin)
- [editorconfig](http://editorconfig.org/)
    - And its [IDEA Plugin](https://plugins.jetbrains.com/plugin/7294-editorconfig)
- [doctoc](https://github.com/thlorenz/doctoc)
- [gnupg](https://gnupg.org/)
- [pegdown doclet](https://github.com/jamesots/pegdown-doclet)
    - And its [IDEA Plugin](https://plugins.jetbrains.com/plugin/7253-pegdown-doclet-for-idea)
- [pitest](http://pitest.org/)
- [Maven License Plugin](http://code.mycila.com/license-maven-plugin/)

And those online services:

- [GitHub](https://github.com/WriteThemFirst/approvals-java)
- [Travis](https://travis-ci.org/WriteThemFirst/approvals-java)
- [Bintray](https://bintray.com/writethemfirst/maven/approvals-java)
- [Maven Central](https://search.maven.org/#search|ga|1|g%3A%22com.github.writethemfirst%22)
- [CodeCov](https://codecov.io/gh/WriteThemFirst/approvals-java)
- [CodeClimate](https://codeclimate.com/github/WriteThemFirst/approvals-java/)
- [Javadoc](https://www.javadoc.io/doc/com.github.writethemfirst/approvals-java)

# Contribution Conventions

## Source Code

- CI jobs should always be green *(which means unit tests are ok)*,
- Shouldn't lower the code coverage,
- Shouldn't lower the marks from [CodeClimate](https://codeclimate.com/github/WriteThemFirst/approvals-java/),

## Formatting

We're using the default IntelliJ IDEA Java Formatter with those modifications:

- No `<p>` is added in Javadoc's empty lines,
- Javadoc is wrapped at right margin,

## Javadoc

- Written for all packages (`package-info.java`), classes, and public functions (at least),
- Written in Markdown, *(it'll be processed by pegdown doclet)*

## Documentation

- Should be updated along with the contributions,
- Should contain examples and code snippets when required,
- Should contain references to latest versions of the library,
- Should have an up-to-date table of contents,

## Commits

- Make small and independent commits including [gitmojis](https://gitmoji.carloscuesta.me/),
- Rebase is preferred to Merge,

## Versions

- Should follow [Semantic Versioning](https://semver.org/spec/v2.0.0.html) [![Semver](http://img.shields.io/SemVer/2.0.0.png)](http://semver.org/spec/v2.0.0.html)


# Processes and Operations

Most of the actual things to be done from the source code are automated by our Continuous Integration on [Travis](https://travis-ci.org/WriteThemFirst/approvals-java).

The CI will:

- Build the source code,
- Run the Unit Tests,
- Calculate the code coverage and report it to [CodeCov](https://codecov.io/gh/WriteThemFirst/approvals-java),
- Run quality analysis and report it to [CodeClimate](https://codeclimate.com/github/WriteThemFirst/approvals-java/)
- Create jars for:
    - Binaries,
    - Source code,
    - Javadoc,
- Deploy the jars:
    - On [oss.jfrog.org](https://oss.jfrog.org/artifactory/oss-snapshot-local) for SNAPSHOTs,
    - On [Bintray](https://bintray.com/writethemfirst/maven/approvals-java) for releases.
    
## Releasing the library

Releasing a new version of the library is the only action requiring manual operations. To trigger a new release, you have to:

- Push a commit including a final version in the `pom.xml`,

That will trigger the delivery of all necessary jars on [Bintray](https://bintray.com/writethemfirst/maven/approvals-java).

Then, you'll have to:

- Create a [new release on GitHub](https://github.com/WriteThemFirst/approvals-java/releases),
- Update it with the [changelog](#generate-the-changelog-to-put-in-the-release-notes-on-github),
- Synchronize the binaries to Maven Central from [Bintray](https://bintray.com/writethemfirst/maven/approvals-java),
- Push a new commit updating the library's version to the next SNAPSHOT.

Please make sure while releasing that you updated properly all necessary documentation and links (everything is located in [our README](README.md)).

# Useful commands

## Licenses in source headers

We're using the [Maven License Plugin](http://code.mycila.com/license-maven-plugin/) to ensure that all of our source code files have a proper license header. The validation will be made in the `check` phase of the Maven build and will fail it if some files do not contain the proper header.

To update the header in all files, you can run this command:

```
mvn license:format
```

From the root of the repository.

## Table of Contents in Markdown files

We're using [doctoc](https://github.com/thlorenz/doctoc) to automagically manage our Markdown documents' table of contents. Do not hesitate to have a look at [doctoc](https://github.com/thlorenz/doctoc)'s documentation for a complete understanding, but long story short:

Install it with:

```
npm install -g doctoc
```

And then update the table of contents of Markdown documents with:

``` 
doctoc *.md
```

Or update a single document with:

``` 
doctoc README.md
```

Ensure that the table of content will be generated in the convenient location by marking that specific place with:

```html
<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->

<!-- END doctoc generated TOC please keep comment here to allow auto update -->
```

## Validate your tests coverage with mutations

Our test coverage is calculated by [CodeCov](https://codecov.io/gh/WriteThemFirst/approvals-java), but it doesn't mean all the tests are perfect. To ensure that tests are actually validating properly what the code is supposed to do, you can run mutations on it:

```
mvn clean test pitest:mutationCoverage
```

Then have a look at the generated reports.

## Generate the changelog to put in the release notes on GitHub

Once you created a [new release on GitHub](https://github.com/WriteThemFirst/approvals-java/releases), you will need to complete that release with the changelog of the current release.

You can get that by executing the provided script at the root of the repository:

``` 
./changelog.sh
```

It will compute the changelog and display it in your terminal. You can then copy/paste it on GitHub.

# Development Tips'n'Tricks

## Adding a new Reporter

You should probably instantiate a new `CommandReporter` in the `Windows` or `Linux` (TBC) interface.
 
Here is a sample syntax:

```java
Reporter IDEA = new CommandReporter(new Command(
    "%programFiles%\\JetBrains", "idea64.exe"), 
    "merge %approved% %received% %approved%");
```

`Command` will replace `%programFiles%` with one of the "Program Files" folders on your computer until it finds the executable.

`%approved%` and `%received%` are replaced by the file names of the *approved* and *received* files.
