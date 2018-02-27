# Contributing

Please follow this simple rules:

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
