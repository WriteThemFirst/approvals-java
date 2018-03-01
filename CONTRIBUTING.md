# Contributing to approvals-java

Thanks for contributing on [approvals-java](https://github.com/WriteThemFirst/approvals-java). Before implementing new features and changes, feel free to [submit an issue](https://github.com/WriteThemFirst/approvals-java/issues/new). We're going to talk here :stuck_out_tongue_winking_eye:.

## How to submit a pull request?

1. Fork [this repository](https://github.com/WriteThemFirst/approvals-java/fork).
2. Make your changes.
3. Commit your changes. 
5. Push your changes.
6. Submit your pull request.

## How to add a Reporter

You should probably instantiate a new `CommandReporter` in the `Windows` or `Linux` (TBC) interface. 
Here is a sample syntax:

```java
Reporter IDEA = new CommandReporter(new Command(
    "%programFiles%\\JetBrains", "idea64.exe"), 
    "merge %approved% %received% %approved%");
```

`Command` will replace `%programFiles%` with one of the "Program Files" folders on your computer
until it finds the executable.

`%approved%` and `%received%` are replaced by the file names of the *approved* and *received* files.

## Small commits

:sparkles: small commits with messages using [gitmojis](https://gitmoji.carloscuesta.me/)
 
This [IDEA plugin](https://plugins.jetbrains.com/plugin/9174-emoji-support-plugin) helps!

## Javadoc

:memo: Javadoc is written in Markdown, and processed with [Pegdown Doclet](https://github.com/jamesots/pegdown-doclet)

The [Pegdown plugin](https://plugins.jetbrains.com/plugin/7253-pegdown-doclet-for-idea) 
will make it look nicer in IDEA. 
Activate it in the settings after restart of IDEA. 

## Mutation tests

You can check the test coverage with PIT by running: 

    mvn clean test pitest:mutationCoverage
