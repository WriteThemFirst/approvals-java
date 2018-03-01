# Frequently Asked Questions

## IntelliJ merge and newlines

When using the default `Windows.IDEA` Reporter, it adds a line at the end of the *approved* file
and my test never passes.

**Solution**: 
Add this block to your `.editorconfig` file. 

    [*.approved]
    insert_final_newline = false


## *.approved files and Git line endings

The `*.approved` files must be checked into source your source control, they are part of your tests. 

This can be an issue with git as it will change the line endings.

**Solution**:
Add this block to your `.gitattributes`

    *.approved.* binary
    *.approved binary

With this, git will treat these files are *binary* instead of *text* 
and will respect their line endings.


## Using approvals-java with a unit test framework in another JVM language

Approvals tries to name your *approved* files by looking at the stack 
which called `approvals.verify()` to detect the calling class and method name.

When you use it from a Kotlin or Scala unit test framework, you can have issue with the naming.

### Example solution with ScalaTest:

When you use `approvals.verify()` from a spec, you need to specify the filename for *approved* and *received* files 
because it is not inferred from the stack like in JUnit tests 
(the classes and methods are not expressive in this context).

```scala
 "Parser" should "parse example" in {
    val problem = myParser.parse(data)
    approvals.verify(problem, "parsedExample")
  }
```

You can define a trait `Approbation` like this:

```scala
import com.github.writethemfirst.approvals.Approvals

trait Approbation {
  val approvals = new Approvals(getClass)
}
```
