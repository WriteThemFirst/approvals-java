# Contributing to approvals-java

Thanks for contributing on [approvals-java](https://github.com/WriteThemFirst/approvals-java). Before implementing new features and changes, feel free to [submit an issue](https://github.com/WriteThemFirst/approvals-java/issues/new). We're going to talk here :stuck_out_tongue_winking_eye:.

## How to submit a pull request?

1. Fork [this repository](https://github.com/WriteThemFirst/approvals-java/fork).
2. Make your changes.
3. Commit your changes. 
5. Push your changes.
6. Submit your pull request.

## How to add a Reporter

TODO

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
