///*** Installation 
0.Install JDK8 and set JAVA_HOME, then check 
$ java -version    //Make sure you have version 1.8.
1.Download https://github.com/sbt/sbt/releases/download/v1.1.1/sbt-1.1.1.zip
  Unzip and put into some dir and update PATH to include THAT_DIR\bin;
2.cd to an empty folder.
$ cd firstProject 
3.Run the following command
//List of templates: https://github.com/foundweekends/giter8/wiki/giter8-templates
$ sbt new scala/scalatest-example.g8   //or scala/hello-world.g8
4.Enter 'first' when prompted 
5. Project structure 
- first
    - project (sbt uses this to install manage plugins and dependencies)
        - build.properties
    - src
        - main
            - scala (All of your scala code goes here)
                -Main.scala (Entry point of program) <-- this is all we need for now
    build.sbt (sbt build definition file)

6.Note sbt can compile src/main/scala, src/main/java and src/test/scala and src/test/java 
7.IntelliJ IDEA already supports sbt projects. 
    a. download from https://www.jetbrains.com/idea/download/#section=windows
    b.For Eclipse users, use sbt-eclipse plugin.
8.Quick IntellJ:  
    a.Open up IntelliJ and click File => New => Project
      To install a Scala SDK. To the right of the Scala SDK field, click the Create button.(select 2.11.12 and download)
      With SBT, On the left panel, select Scala and on the right panel, select SBT
    OR
    a.open up IntelliJ, select Import Project and open the build.sbt file for your project
    b.Make sure the JDK Version is 1.8 and the SBT Version is at least 0.13.13
    c.Select Use auto-import so dependencies are automatically downloaded when available
    d.Select Finish
    //Writing Scala code
    1.On the Project panel on the left, expand SBTExampleProject => src => main
    2.Right-click scala and select New => Package
    3.Name the package example and click OK.
    4.Right-click the package example and select New => Scala class.
    5.Name the class Main and change the Kind to object.
    6.Change the code in the class to the following:
    object Main extends App {
      val ages = Seq(42, 75, 29, 64)
      println(s"The oldest person is ${ages.max}")
    }
    //Creating a test
    1.On the project pane on the left, expand src => test.
    2.Right-click on scala and select New => Scala class.
    3.Name the class CubeCalculatorTest and click OK.
    4.Replace the code with the following:

    import org.scalatest.FunSuite

    class CubeCalculatorTest extends FunSuite {
      test("CubeCalculator.cube") {
        assert(CubeCalculator.cube(3) === 27)
      }
    }
    //Running the test 
    1.In the source code, right-click CubeCalculatorTest and select Run ‘CubeCalculatorTest’.
    //Running the project
    1.From the Run menu, select Edit configurations
    2.Click the + button and select SBT Task.
    3.Name it 'Run the program'.
    4.In the Tasks field, type ~run. 
      The ~ causes SBT to rebuild and rerun the project when you save changes to a file in the project.
    5.Click OK.
    6.On the Run menu. Click Run 'Run the program'.





///*** Update 
//build.sbt 
lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.example",
      scalaVersion := "2.11.11"
    )),
    name := "first"
  )
  
//groupid %% artifactId % version % phase
libraryDependencies ++= Seq(
  "org.specs2" %% "specs2-core" % "4.0.2" % "test",
  "org.scalatest" %% "scalatest" % "3.0.0" % "test"
)
//Can add assembly plugin 


//OR can be written as (note one empty line after each attribute)
//As of sbt 0.13.7 blank lines are no longer used to delimit build.sbt files. 
name := "style4"

organization := "support"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "org.specs2" %% "specs2-core" % "4.0.2" % "test",
  "org.scalatest" %% "scalatest" % "3.0.0" % "test"
)


//project/build.properties 
sbt.version=1.1.1

//src/main/scala/CubeCalculator.scala 
package support 

object CubeCalculator extends App {
  def cube(x: Int) = {
    x * x * x
  }
  //write here main code 
  args foreach println
}
/* OR 
object Stub {
  def main(args: Array[String]) {
   // do something
   args foreach println
  }
}
*/

class Stub {
  // do something
}

//src/test/scala/CubeCalculatorTest.scala 
//'===' is same as '==' , but gives better error messages 
package support 

class CubeCalculatorTest extends org.scalatest.FunSuite {
  test("CubeCalculator.cube") {
    assert(CubeCalculator.cube(3) === 27)
  }
}


//src/test/scala/StubSpec.scala 
//For BDD specifications 
package support

import org.specs2.mutable._

class StubSpec extends Specification {

    "The 'Hello world' string" should {
      "contain 11 characters" in {
        "Hello world" must have size(11)
      }
      "start with 'Hello'" in {
        "Hello world" must startWith("Hello")
      }
      "end with 'world'" in {
        "Hello world" must endWith("world")
      }
    }
    
    "Cube a number " should {
      "cubes given a number " in {
        CubeCalculator.cube(3) must beEqualTo(27)
      }
    }    
  }

//src/test/scala/StubTest.scala 
//for Scalatest BDD specifications 
package support

import org.scalatest._

class StubTest extends FunSpec with Matchers {

  describe("you description here") {
    it("should do something") {
      "Hello world" should equal("Hello world")
    }
  }
  
  describe("Cube tester") {
    it("cubes when given number ") {
      CubeCalculator.cube(3) should equal(27)
    }
  }
}


//src/test/scala/StubFlatSpec.scala 
//for unitest
import org.scalatest.FlatSpec

class SetSpec extends FlatSpec {

  behavior of "An empty Set"

  it should "have size 0" in {
    assert(Set.empty.size === 0)
  }

  it should "produce NoSuchElementException when head is invoked" in {
    intercept[NoSuchElementException] {
      Set.empty.head
    }
  }
}

class SetSpec2 extends FlatSpec {

  "An empty Set" should "have size 0" in {
    assert(Set.empty.size === 0)
  }

  it should "produce NoSuchElementException when head is invoked" in {
    intercept[NoSuchElementException] {
      Set.empty.head
    }
  }
}


//compile 
$ sbt compile
$ sbt testCompile 
$ sbt test          // in windows, gives correct display!!!
$ sbt "run arg1 arg2"    //for running main project
$ sbt package   //packaging jar
$ sbt assembly //creating Fat jar if assembly plugin is included  
$ sbt console   //scala REPL
$ sbt clean clean-files  //clean everything 
#then 
find . -name target -type d -exec rm -rf {} \;
#or in windows 
find . -name target -type d -exec rm -rf '{}' ';'
#or in windows 
rmdir /s /q target project/target project/project/target

$ scala -cp target/scala-2.11/<<name>>.jar  <<main>>  <<command line args>>

//OR with java include scala/lib all jars
$  java -cp "c:/scala/lib/*;target/scala-2.10/equal_2.10-0.1-SNAPSHOT.jar;target/scala-2.10/test-classes/" org.scalatest.run <<name>>
$ java -cp "c:/scala/lib/*;target/scala-2.10/equal_2.10-0.1-SNAPSHOT.jar;target/scala-2.10/test-classes/" org.scalatest.tools.Runner -R . -oDW -s <<name>>

///*** SBT - more configurations 
//https://www.scala-sbt.org/1.0/docs/Basic-Def-Examples.html
//https://www.scala-sbt.org/1.0/docs/Scala-Files-Example.html
//https://www.scala-sbt.org/1.0/docs/Advanced-Configurations-Example.html
// Enables publishing to maven repo
publishMavenStyle := true

// Do not append Scala versions to the generated artifacts
crossPaths := false

///*** SBT - overriding a version 
 //For example, the following dependency definitions conflict 
 //because spark uses log4j 1.2.16 and scalaxb uses log4j 1.2.17:
libraryDependencies ++= Seq(
  "org.spark-project" %% "spark-core" % "0.5.1",    
  "org.scalaxb" %% "scalaxb" % "1.0.0" ) 


//The default conflict manager chooses the latest revision of log4j, 1.2.17:
> show update 
[info] compile: 
[info]    log4j:log4j:1.2.17: ... ... 
[info]    (EVICTED) log4j:log4j:1.2.16 ... 


//To change the version selected, add an override:
dependencyOverrides += "log4j" % "log4j" % "1.2.16"



///*** SBT- For command line 
//https://www.scala-sbt.org/release/docs/Command-Line-Reference.html

> compile       # Compile the source codes
> test:compile  # Compile the source and test codes
> test          # Run tests
> test-quick    # Run previously failed tests only

> ~compile      # Incremental compilation, triggered by source code change
> ~test:compile # Incremental compilation including test codes
> ~test-quick   # Run previously failed tests as you modify the code
# Run the test cases in a given test class. 
# You can use wildcard (\*) in the class name
> ~test-only (test class name)

> clean         # Clean up the target folder

> reload        # Reload the sbt configuration files

> project (sub project name)   # Move to a sub project

> package       # Create a package (target/(project-name).jar) including pom.xml

> publishLocal  # Publish to local repository ~/.ivy2/local
> publishM2     # Publish to local maven repository ~/.m2/repository


///*** SBT- for compiling java sources (default)
//https://www.scala-sbt.org/1.0/docs/Java-Sources.html

///*** SBT - Few Plugins
//List : https://www.scala-sbt.org/1.x/docs/Community-Plugins.html

//create project/plugins.sbt with all of the desired sbt plugins, any general dependencies, and any necessary repositories: 
addSbtPlugin("org.example" % "plugin" % "1.0")

addSbtPlugin("org.example" % "another-plugin" % "2.0")

// plain library (not an sbt plugin) for use in the build definition
libraryDependencies += "org.example" % "utilities" % "1.3"

resolvers += "Example Plugin Repository" at "https://example.org/repo/"

//Many of the auto plugins automatically add settings into projects, 
//however, some may require explicit enablement. Here’s an example: 
lazy val util = (project in file("util"))
  .enablePlugins(FooPlugin, BarPlugin)
  .disablePlugins(plugins.IvyPlugin)
  .settings(
    name := "hello-util"
  )





///sbt-assembly :creating FAT jar 
//https://github.com/sbt/sbt-assembly
//project/plugins.sbt
addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.6")
//build.sbt 

fork := true

retrieveManaged := true

assemblyJarName in assembly := "learning-assembly.jar"

assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = false)

assemblyMergeStrategy in assembly := {
  case PathList("javax", "servlet", xs @ _*)         => MergeStrategy.first
  case PathList(ps @ _*) if ps.last endsWith ".html" => MergeStrategy.first
  case "application.conf"                            => MergeStrategy.concat
  case "unwanted.txt"                                => MergeStrategy.discard
  case m if m.toLowerCase.endsWith("manifest.mf")    => MergeStrategy.discard
  case m if m.toLowerCase.endsWith(".rsa") 			 => MergeStrategy.discard
  case m if m.toLowerCase.endsWith(".dsa") 			 => MergeStrategy.discard
  case m if m.toLowerCase.endsWith(".sf") 			 => MergeStrategy.discard
  case _ 											 => MergeStrategy.first
}
//command 
$ sbt assembly 


///sbt-pack: Collecting dependent jars into a folder
//to collect dependent jars including scala-library.jar are collected in target/pack/lib folder
//and generate launch scripts of  program
//https://github.com/xerial/sbt-pack

//project/plugins.sbt:
resolvers += "sbt-pack" at "http://repo1.maven.org/maven2/org/xerial/sbt/"
addSbtPlugin("org.xerial.sbt" % "sbt-pack" % "0.10.1")

 
//build.sbt 
// [Required] Enable plugin and automatically find def main(args:Array[String]) methods from the classpath
enablePlugins(PackPlugin)

// [Optional] Specify main classes manually
// This example creates `hello` command (target/pack/bin/hello) that calls org.mydomain.Hello#main(Array[String]) 
packMain := Map("hello" -> "org.mydomain.Hello")


 
//commands 
# Collect all dependent jars into target/pack/lib  folder and program launch scripts target/pack/bin/{program name} 
> pack

# Create .tar.gz archive of your project 
> packArchive

 

///sbt-junit-interface: Running JUnit tests with sbt
//https://github.com/sbt/junit-interface
//build.sbt 
libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % "test"

//set default options in your build.sbt file:
testOptions += Tests.Argument(TestFrameworks.JUnit, "-q", "-v")

//commands 
 # Run test cases matching a <regex> within the specified test class
> ~test-only (test class name) -- --tests=<regex>

 



 
 

///*** Scalatest - FlatSpec 
//FlatSpec is a good first step for teams wishing to move from xUnit to BDD. 
//Its structure is flat like xUnit, 
//but the test names must be written in a specification style: "X should Y," "A must B," etc. 


import org.scalatest._

class SetSpec extends FlatSpec {
  override def withFixture(test: NoArgTest) = { // Define a shared fixture
    // Shared setup (run at beginning of each test)
    try test()
    finally {
      // Shared cleanup (run at end of each test)
    }
  }
  // Define the first test for a subject, in this case: "An empty Set"
  "An empty Set" should "have size 0" in { // Name the subject, write 'should', then the rest of the test name
    assert(Set.empty.size == 0)            // (Can use 'must' or 'can' instead of 'should')
  }
  it should "produce NoSuchElementException when head is invoked" in { // Define another test for the same
    intercept[NoSuchElementException] {                                // subject with 'it'
      Set.empty.head
    }
  }
  ignore should "be empty" in { // To ignore a test, change 'it' to 'ignore'...
    assert(Set.empty.isEmpty)
  }
  it should "not be non-empty" ignore { // ...or change 'in' to 'ignore'
    assert(!Set.empty.nonEmpty)
  }
  "A non-empty Set" should "have the correct size" in { // Describe another subject
    assert(Set(1, 2, 3).size == 3)
  }
  // 'it' now refers to 'A non-empty Set'
  it should "return a contained value when head is invoked" is (pending) // Define a pending test
  import tagobjects.Slow
  it should "be non-empty" taggedAs(Slow) in { // Tag a test
    assert(Set(1, 2, 3).nonEmpty)
  }
}

// Can also pass fixtures into tests with fixture.FlatSpec
class StringSpec extends fixture.FlatSpec {
  type FixtureParam = String // Define the type of the passed fixture object
  override def withFixture(test: OneArgTest) = {
    // Shared setup (run before each test), including...
    val fixture = "a fixture object" // ...creating a fixture object
    try test(fixture) // Pass the fixture into the test
    finally {
      // Shared cleanup (run at end of each test)
    }
  }
  "The passed fixture" can "be used in the test" in { s => // Fixture passed in as s
    assert(s == "a fixture object")
  }
}

@DoNotDiscover // Disable discovery of a test class
class InvisibleSpec extends FlatSpec { /*code omitted*/ }

@Ignore // Ignore all tests in a test class
class IgnoredSpec extends FlatSpec { /*code omitted*/ }

import tags.Slow
@Slow // Mark all tests in a test class with a tag
class SlowSpec extends FlatSpec { /*code omitted*/ }


///*** ScalaTest- FunSuite
//For teams coming from xUnit,  to BDD 

import org.scalatest._

class SetSpec extends FunSuite {
  override def withFixture(test: NoArgTest) = { // Define a shared fixture
    // Shared setup (run at beginning of each test)
    try test()
    finally {
      // Shared cleanup (run at end of each test)
    }
  }
  // Define tests with 'test', a test name string in parentheses,
  // and test body in curly braces
  test("An empty Set should have size 0") {
    assert(Set.empty.size == 0)
  }
  // To ignore a test, change 'test' to 'ignore'
  ignore("Invoking head on an empty Set should produce NoSuchElementException") {
    intercept[NoSuchElementException] {
      Set.empty.head
    }
  }
  // Define a pending test by using (pending) for the body
  test("An empty Set's isEmpty method should return false") (pending)
  // Tag a test by placing a tag object after the test name
  import tagobjects.Slow
  test("An empty Set's nonEmpty method should return true", Slow) { 
    assert(!Set.empty.nonEmpty)
  }
}

// Can also pass fixtures into tests with fixture.FunSuite
class StringSpec extends fixture.FunSuite {
  type FixtureParam = String // Define the type of the passed fixture object
  override def withFixture(test: OneArgTest) = {
    // Shared setup (run before each test), including...
    val fixture = "a fixture object" // ...creating a fixture object
    try test(fixture) // Pass the fixture into the test
    finally {
      // Shared cleanup (run at end of each test)
    }
  }
  test("The passed fixture can be used in the test") { s => // Fixture passed in as s
    assert(s == "a fixture object")
  }
}

@DoNotDiscover // Disable discovery of a test class
class InvisibleSpec extends FunSuite { /*code omitted*/ }

@Ignore // Ignore all tests in a test class
class IgnoredSpec extends FunSuite { /*code omitted*/ }

import tags.Slow
@Slow // Mark all tests in a test class with a tag
class SlowSpec extends FunSuite { /*code omitted*/ }


///*** ScalaTest - FunSpec
//For teams coming from Ruby's RSpec tool

writing specification-style tests. 
import org.scalatest._

class SetSpec extends FunSpec {
  override def withFixture(test: NoArgTest) = { // Define a shared fixture
    // Shared setup (run at beginning of each test)
    try test()
    finally {
      // Shared cleanup (run at end of each test)
    }
  }

  // Describe a scope for a subject, in this case: "A Set"
  describe("A Set") { // All tests within these curly braces are about "A Set"

    // Can describe nested scopes that "narrow" its outer scopes
    describe("(when empty)") { // All tests within these curly braces are about "A Set (when empty)"

      it("should have size 0") {    // Here, 'it' refers to "A Set (when empty)". The full name
        assert(Set.empty.size == 0) // of this test is: "A Set (when empty) should have size 0"
      }
      it("should produce NoSuchElementException when head is invoked") { // Define another test
        intercept[NoSuchElementException] {
          Set.empty.head
        }
      }
      ignore("should be empty") { // To ignore a test, change 'it' to 'ignore'...
        assert(Set.empty.isEmpty)
      }
    }

    // Describe a second nested scope that narrows "A Set" in a different way
    describe("(when non-empty)") { // All tests within these curly braces are about "A Set (when non-empty)"

      it("should have the correct size") { // Here, 'it' refers to "A Set (when non-empty)". This test's full
        assert(Set(1, 2, 3).size == 3)     // name is: "A Set (when non-empty) should have the correct size"
      }
      // Define a pending test by using (pending) for the body
      it("should return a contained value when head is invoked") (pending)
      import tagobjects.Slow
      it("should be non-empty", Slow) { // Tag a test by placing a tag object after the test name
        assert(Set(1, 2, 3).nonEmpty)
      }
    }
  }
}

// Can also pass fixtures into tests with fixture.FunSpec
class StringSpec extends fixture.FunSpec {
  type FixtureParam = String // Define the type of the passed fixture object
  override def withFixture(test: OneArgTest) = {
    // Shared setup (run before each test), including...
    val fixture = "a fixture object" // ...creating a fixture object
    try test(fixture) // Pass the fixture into the test
    finally {
      // Shared cleanup (run at end of each test)
    }
  }
  describe("The passed fixture") {
    it("can be used in the test") { s => // Fixture passed in as s
      assert(s == "a fixture object")
    }
  }
}

@DoNotDiscover // Disable discovery of a test class
class InvisibleSpec extends FunSpec { /*code omitted*/ }

@Ignore // Ignore all tests in a test class
class IgnoredSpec extends FunSpec { /*code omitted*/ }

import tags.Slow
@Slow // Mark all tests in a test class with a tag
class SlowSpec extends FunSpec { /*code omitted*/ }


///*** ScalaTest - WordSpec
//For teams coming from specs or specs2

import org.scalatest._

class SetSpec extends WordSpec {
  override def withFixture(test: NoArgTest) = { // Define a shared fixture
    // Shared setup (run at beginning of each test)
    try test()
    finally {
      // Shared cleanup (run at end of each test)
    }
  }

  // Describe a scope for a subject, in this case: "A Set"
  "A Set" can { // All tests within these curly braces are about "A Set"

    // Can describe nested scopes that "narrow" its outer scopes
    "empty" should { // All tests within these curly braces are about "A Set (when empty)"

      "have size 0" in {    // Here, 'it' refers to "A Set (when empty)". The full name
        assert(Set.empty.size == 0) // of this test is: "A Set (when empty) should have size 0"
      }
      "produce NoSuchElementException when head is invoked" in { // Define another test
        intercept[NoSuchElementException] {
          Set.empty.head
        }
      }
      "should be empty" ignore { // To ignore a test, change 'it' to 'ignore'...
        assert(Set.empty.isEmpty)
      }
    }

    // Describe a second nested scope that narrows "A Set" in a different way
    "non-empty" should { // All tests within these curly braces are about "A Set (when non-empty)"

      "have the correct size" in { // Here, 'it' refers to "A Set (when non-empty)". This test's full
        assert(Set(1, 2, 3).size == 3)     // name is: "A Set (when non-empty) should have the correct size"
      }
      // Define a pending test by using (pending) for the body
      "return a contained value when head is invoked" is (pending)
      import tagobjects.Slow
      "be non-empty" taggedAs (Slow) in { // Tag a test by placing a tag object after the test name
        assert(Set(1, 2, 3).nonEmpty)
      }
    }
  }
}

// Can also pass fixtures into tests with fixture.WordSpec
class StringSpec extends fixture.WordSpec {
  type FixtureParam = String // Define the type of the passed fixture object
  override def withFixture(test: OneArgTest) = {
    // Shared setup (run before each test), including...
    val fixture = "a fixture object" // ...creating a fixture object
    try test(fixture) // Pass the fixture into the test
    finally {
      // Shared cleanup (run at end of each test)
    }
  }
  "The passed fixture" can {
    "be used in the test" in { s => // Fixture passed in as s
      assert(s == "a fixture object")
    }
  }
}

@DoNotDiscover // Disable discovery of a test class
class InvisibleSpec extends WordSpec { /*code omitted*/ }

@Ignore // Ignore all tests in a test class
class IgnoredSpec extends WordSpec { /*code omitted*/ }

import tags.Slow
@Slow // Mark all tests in a test class with a tag
class SlowSpec extends WordSpec { /*code omitted*/ }


///*** ScalaTest - FreeSpec
//For teams experienced with BDD  and no structure 

import org.scalatest._

class SetSpec extends FreeSpec {
  override def withFixture(test: NoArgTest) = { // Define a shared fixture
    // Shared setup (run at beginning of each test)
    try test()
    finally {
      // Shared cleanup (run at end of each test)
    }
  }

  // Describe a scope for a subject, in this case: "A Set"
  "A Set" - { // All tests within these curly braces are about "A Set"

    // Can describe nested scopes that "narrow" its outer scopes
    "(when empty)" - { // All tests within these curly braces are about "A Set (when empty)"

      "should have size 0" in {    // Here, 'it' refers to "A Set (when empty)". The full name
        assert(Set.empty.size == 0) // of this test is: "A Set (when empty) should have size 0"
      }
      "should produce NoSuchElementException when head is invoked" in { // Define another test
        intercept[NoSuchElementException] {
          Set.empty.head
        }
      }
      "should should be empty" ignore { // To ignore a test, change 'it' to 'ignore'...
        assert(Set.empty.isEmpty)
      }
    }

    // Describe a second nested scope that narrows "A Set" in a different way
    "(when non-empty)" - { // All tests within these curly braces are about "A Set (when non-empty)"

      "should have the correct size" in { // Here, 'it' refers to "A Set (when non-empty)". This test's full
        assert(Set(1, 2, 3).size == 3)     // name is: "A Set (when non-empty) should have the correct size"
      }
      // Define a pending test by using (pending) for the body
      "return a contained value when head is invoked" is (pending)
      import tagobjects.Slow
      "should be non-empty" taggedAs (Slow) in { // Tag a test by placing a tag object after the test name
        assert(Set(1, 2, 3).nonEmpty)
      }
    }
  }
}

// Can also pass fixtures into tests with fixture.FreeSpec
class StringSpec extends fixture.FreeSpec {
  type FixtureParam = String // Define the type of the passed fixture object
  override def withFixture(test: OneArgTest) = {
    // Shared setup (run before each test), including...
    val fixture = "a fixture object" // ...creating a fixture object
    try test(fixture) // Pass the fixture into the test
    finally {
      // Shared cleanup (run at end of each test)
    }
  }
  "The passed fixture" - {
    "can be used in the test" in { s => // Fixture passed in as s
      assert(s == "a fixture object")
    }
  }
}

@DoNotDiscover // Disable discovery of a test class
class InvisibleSpec extends FreeSpec { /*code omitted*/ }

@Ignore // Ignore all tests in a test class
class IgnoredSpec extends FreeSpec { /*code omitted*/ }

import tags.Slow
@Slow // Mark all tests in a test class with a tag
class SlowSpec extends FreeSpec { /*code omitted*/ }


///*** ScalaTest - Spec 
//Spec allows  to define tests as methods, which saves one function literal per test compared Other 
//translates into faster compile times 

import org.scalatest._

class SetSpec extends Spec {
  override def withFixture(test: NoArgTest) = { // Define a shared fixture
    // Shared setup (run at beginning of each test)
    try test()
    finally {
      // Shared cleanup (run at end of each test)
    }
  }

  // Describe a scope for a subject, in this case: "A Set"
  object `A Set` { // All tests within these curly braces are about "A Set"

    // Can describe nested scopes that "narrow" its outer scopes
    object `(when empty)` { // All tests within these curly braces are about "A Set (when empty)"

      def `should have size 0` {    // Here, 'it' refers to "A Set (when empty)". The full name
        assert(Set.empty.size == 0) // of this test is: "A Set (when empty) should have size 0"
      }
      def `should produce NoSuchElementException when head is invoked` { // Define another test
        intercept[NoSuchElementException] {
          Set.empty.head
        }
      }
      @Ignore def `should should be empty` { // To ignore a test, change 'it' to 'ignore'...
        assert(Set.empty.isEmpty)
      }
    }

    // Describe a second nested scope that narrows "A Set" in a different way
    object `(when non-empty)` { // All tests within these curly braces are about "A Set (when non-empty)"

      def `should have the correct size` { // Here, 'it' refers to "A Set (when non-empty)". This test's full
        assert(Set(1, 2, 3).size == 3)     // name is: "A Set (when non-empty) should have the correct size"
      }
      // Define a pending test by using { pending } for the body
      def `return a contained value when head is invoked` { pending }
      import org.scalatest.tags.Slow
      @Slow def `should be non-empty` { // Tag a test by placing a tag object after the test name
        assert(Set(1, 2, 3).nonEmpty)
      }
    }
  }
}

// Can also pass fixtures into tests with fixture.Spec
class StringSpec extends fixture.Spec {
  type FixtureParam = String // Define the type of the passed fixture object
  override def withFixture(test: OneArgTest) = {
    // Shared setup (run before each test), including...
    val fixture = "a fixture object" // ...creating a fixture object
    try test(fixture) // Pass the fixture into the test
    finally {
      // Shared cleanup (run at end of each test)
    }
  }
  object `The passed fixture` {
   def `can be used in the test` in { s => // Fixture passed in as s
      assert(s == "a fixture object")
    }
  }
}

@DoNotDiscover // Disable discovery of a test class
class InvisibleSpec extends Spec { /*code omitted*/ }

@Ignore // Ignore all tests in a test class
class IgnoredSpec extends Spec { /*code omitted*/ }

import tags.Slow
@Slow // Mark all tests in a test class with a tag
class SlowSpec extends Spec { /*code omitted*/ }


///*** ScalaTest - PropSpec
//in terms of property checks; 
//also a good choice for writing the occasional test matrix when a different style trait is chosen as the main unit testing style. 

import org.scalatest._

class SetSpec extends PropSpec {
  override def withFixture(test: NoArgTest) = { // Define a shared fixture
    // Shared setup (run at beginning of each test)
    try test()
    finally {
      // Shared cleanup (run at end of each test)
    }
  }
  // Define tests with 'property', a test name string in parentheses,
  // and test body in curly braces
  property("An empty Set should have size 0") {
    assert(Set.empty.size == 0)
  }
  // To ignore a test, change 'property' to 'ignore'
  ignore("Invoking head on an empty Set should produce NoSuchElementException") {
    intercept[NoSuchElementException] {
      Set.empty.head
    }
  }
  // Define a pending test by using (pending) for the body
  property("An empty Set's isEmpty method should return false") (pending)
  // Tag a test by placing a tag object after the test name
  import tagobjects.Slow
  property("An empty Set's nonEmpty method should return true", Slow) { 
    assert(!Set.empty.nonEmpty)
  }
}

// Can also pass fixtures into tests with fixture.PropSpec
class StringSpec extends fixture.PropSpec {
  type FixtureParam = String // Define the type of the passed fixture object
  override def withFixture(test: OneArgTest) = {
    // Shared setup (run before each test), including...
    val fixture = "a fixture object" // ...creating a fixture object
    try test(fixture) // Pass the fixture into the test
    finally {
      // Shared cleanup (run at end of each test)
    }
  }
  property("The passed fixture can be used in the test") { s => // Fixture passed in as s
    assert(s == "a fixture object")
  }
}

@DoNotDiscover // Disable discovery of a test class
class InvisibleSpec extends PropSpec { /*code omitted*/ }

@Ignore // Ignore all tests in a test class
class IgnoredSpec extends PropSpec { /*code omitted*/ }

import tags.Slow
@Slow // Mark all tests in a test class with a tag
class SlowSpec extends PropSpec { /*code omitted*/ }

///*** ScalaTest - FeatureSpec 
//for acceptance testing, 

import org.scalatest._
class TVSet {
  private var on: Boolean = false
  def isOn: Boolean = on
  def pressPowerButton() { on = !on }
}

class TVSetSpec extends FeatureSpec with GivenWhenThen {
  info("As a TV set owner")
  info("I want to be able to turn the TV on and off")
  info("So I can watch TV when I want")
  info("And save energy when I'm not watching TV")

  feature("TV power button") {
    scenario("User presses power button when TV is off") {
      Given("a TV set that is switched off")
      val tv = new TVSet
      assert(!tv.isOn)
      When("the power button is pressed")
      tv.pressPowerButton()
      Then("the TV should switch on")
      assert(tv.isOn)
    }
    scenario("User presses power button when TV is on") {
      Given("a TV set that is switched on")
      val tv = new TVSet
      tv.pressPowerButton()
      assert(tv.isOn)
      When("the power button is pressed")
      tv.pressPowerButton()
      Then("the TV should switch off")
      assert(!tv.isOn)
    }
  }
} 





///*** Scalatest - BeforeAndAfter , trait, hence use with 'with' - must be last trait
//Trait that can be mixed into suites that need code executed before and after running each test.
// Note === is similar to ==, but logs more messages 

//The advantage this trait has over BeforeAndAfterEach is that its syntax is more concise. 
//The main disadvantage is that it is not stackable, 
//whereas BeforeAndAfterEach is. I.e., you can write several traits that extend BeforeAndAfterEach 
//and provide beforeEach methods that include a call to super.beforeEach, 
//and mix them together in various combinations. 
//By contrast, only one call to the before registration function is allowed in a suite 
//or spec that mixes in BeforeAndAfter. 



import org.scalatest._
import scala.collection.mutable.ListBuffer

//Note - ERROR - class MySuite extends BeforeAndAfter with FunSuite
class MySuite extends FunSuite with BeforeAndAfter {

  // Fixtures as reassignable variables and mutable objects
  var sb: StringBuilder = _
  val lb = new ListBuffer[String]

  before {
    sb = new StringBuilder("ScalaTest is ")
    lb.clear()
  }

  def testEasy() {
    sb.append("easy!")
    assert(sb.toString === "ScalaTest is easy!")
    assert(lb.isEmpty)
    lb += "sweet"
  }

  def testFun() {
    sb.append("fun!")
    assert(sb.toString === "ScalaTest is fun!")
    assert(lb.isEmpty)
  }
}

///*** Scalatest - trait BeforeAndAfterEach - must be last trait
// can be mixed into suites that need methods invoked before and after running each test. 
import org.scalatest._

import scala.collection.mutable.ListBuffer

//WRONG - class MySuite extends BeforeAndAfterEach with FunSuite 
class MySuite extends BeforeAndAfterEach {

    // Fixtures as reassignable variables and mutable objects
    var sb: StringBuilder = _
    val lb = new ListBuffer[String]

    override def beforeEach() {
     sb = new StringBuilder("ScalaTest is ")
     lb.clear()
    }

    def testEasy() {
     sb.append("easy!")
     assert(sb.toString === "ScalaTest is easy!")
     assert(lb.isEmpty)
     lb += "sweet"
    }

    def testFun() {
     sb.append("fun!")
     assert(sb.toString === "ScalaTest is fun!")
     assert(lb.isEmpty)
    }
}
 
///*** Scalatest - trait BeforeAndAfterAll  - must be last trait
//This trait allows code to be executed before and/or after all the tests
protected def 	beforeAll : Unit
    Defines a method to be run before any of this suite tests or nested suites are run.
protected def 	afterAll : Unit
    Defines a method to be run after all of this suite tests and nested suites have been run.


    
    

///*** Scalatest - Matchers - use with 'with Matchers'
//http://www.scalatest.org/user_guide/using_matchers

//Usage :
import org.scalatest._

class ExampleSpec extends FlatSpec with Matchers { ...

}
//OR 
import org.scalatest._
import Matchers._

class ExampleSpec extends FlatSpec { 
    // Can use matchers here ...

}


///* Scalatest - Matchers - Assertions

import org.scalatest._
import Assertions._

val (a, b, c, d) = (1, 2, 3, 4)

assert(a == b || c >= d) // Error message: 1 did not equal 2, and 3 was not greater than or equal to 4

assertResult(2) { c + d } // Error message: Expected 2, but got 7

intercept[IllegalArgumentException] { // Error message: Expected exception java.lang.IllegalArgumentException
  c / 0                               // to be thrown, but java.lang.ArithmeticException was thrown.
}

fail("I've got a bad feeling about this") // Error message: I've got a bad feeling about this

// Succeeds if doesn't compile because of type or syntax error
assertDoesNotCompile("val a: Int = 1") // Error message: Expected a compiler error, but got none for code:
                                       // val a: Int = 1

// Succeeds only if a type error
assertTypeError("val a: Int = 1") // Error message: Expected a type error, but got none for code:
                                  // val a: Int = 1

// Succeeds only if no type or syntax error
assertCompiles("_") // Error message: Expected no compiler error, but got the following parse error:
                    // "unbound placeholder parameter", for code: _

// Tests that can't run because of missing preconditions should be canceled
cancel("Network was down") // Error message: Network was down

// Tests can also be canceled with assume
case class Database(available: Boolean)
val db = Database(false)
assume(db.available) // Error message: db.available was false

// You can add clues to error messages
assert(a == b, ", but you already knew that") // Error message: 1 did not equal 2, but you already knew that

assertResult(2, ", what a bummer!") { a + b } // Error message: Expected 2, but got 3, what a bummer!

assume(db.available, "yet again") // Error message: db.available was false yet again

withClue("prepended clue") { // Error message: prepended clue 1 did not equal 2
  assert(a == b)
}

import AppendedClues._
assert(a == b) withClue "appended clue" // Error message: 1 did not equal 2 appended clue

 

///* Scalatest - Matchers - Equality and identity

import org.scalatest.Matchers._
def incr(i: Int) = i + 1
val result = incr(2)
result should equal (3) // By default, calls left == right, except for arrays
result should be (3)    // Calls left == right, except for arrays
result should === (3)   // By default, calls left == right, except for arrays

Array(1, 2) should equal (Array(1, 2)) // Arrays are compared structurally,
Array(1, 2) should be (Array(1, 2))    // by calling left.deep == right.deep
Array(1, 2) should === (Array(1, 2))

result shouldEqual 3 // Alternate forms for equal and be
result shouldBe 3    // that don't require parentheses

// The === syntax can give you a type error for suspicious equality comparisons
import org.scalactic._
import TypeCheckedTripleEquals._
result should === ("three") // Fails to compile with error message: types String and Int
                            // do not adhere to the type constraint selected for the === and
                            // !== operators; the missing implicit parameter is of type
                            // org.scalactic.Constraint[String,Int]
                            //      greeting should === (3)
                            //               ^

// The equal matcher and === syntax compare with an implicit Equality[L],
// where L is the left hand type (i.e., the type of greeting below)
val greeting = "Hi"
greeting should equal ("hi") // Both fail with error message: "[H]i" did not equal "[h]i"
greeting should === ("hi")

import org.scalactic._ // Can provide a custom Equality[L] explicitly
import Explicitly._
import StringNormalizations._
greeting should equal ("hi") (after being lowerCased) // Both succeed, because 'after being lowercased'
(greeting should === ("hi")) (after being lowerCased) // defines an Equality[String] that ignores case

implicit val strEq = lowerCased.toEquality // Can provide a custom Equality[L] implicitly
greeting should equal ("hi")               // Succeeds because equal the implicit Equality[String]
greeting should === ("hi")                 // (defined as strEq) that ignores case

greeting should not equal "bye"            // equal and be are negated with not
greeting should not be "such sweet sorrow"
greeting should !== ("ho")                 // === is negated as !==

val list = List(1, 2, 3)              // Ensure two objects have the same identity
val ref1 = list
val ref2 = list
ref1 should be theSameInstanceAs ref2 // Calls left eq right

 

///* Scalatest - Matchers - Object's class

import org.scalatest.Matchers._
class Tiger
val tiger = new Tiger
tiger shouldBe a [Tiger]       // Ensure an object is an instance of a specified class or trait

val tigerList = List(tiger)    // Because type parameters are erased on the JVM, you must use
tigerList shouldBe a [List[_]] // an underscore for any type parameters when using this syntax


 

///* Scalatest - Matchers - Expected exceptions

import org.scalatest.Matchers._
val s = "hai"
an [IndexOutOfBoundsException] should be thrownBy {     // Ensure a particular exception type is thrown
  s.charAt(-1)
}
val caught = the [IndexOutOfBoundsException] thrownBy { // Capturing an expected exception in a variable
  s.charAt(-1)
}
the [IndexOutOfBoundsException] thrownBy {              // Inspecting an expected exception
  s.charAt(-1)
} should have message ("String index out of range: -1")

 

///* Scalatest - Matchers - Length and size

import org.scalatest.Matchers._
val result = "hai"

result should have length 3 // Works for any type T for which an implicit Length[T] is available, including String,
                            // Array, scala.collection.GenSeq, java.util.List, and any object with length field or
                            // method or getLength method; the length value can be Int or Long.

result should have size 3   // Works for any type T for which an implicit Size[T] is available, including String, Array,
                            // scala.collection.GenTraversable, java.util.Collection, java.util.Map, and any object with
                            // size field or method or getSize method; the size value can be Int or Long.


 

///* Scalatest - Matchers - Strings and regular expressions

import org.scalatest.Matchers._
val string = "Hello World"
string should startWith ("Hello")               // starts with the given substring

string should endWith ("world")                 // ends with the given substring

string should include ("seven")                 // includes the given substring

string should startWith regex "Hel*o"           // starts with a substring matching the regular expression
string should startWith regex "Hel*o".r         // (works with String or Regex)

string should endWith regex "wo.ld".r           // ends with a substring matching the regular expression

string should include regex "wo.ld".r           // includes substring matching regular expression

val re = """(-)?(\d+)(\.\d*)?""".r
string should fullyMatch regex re               // includes substring matching regular expression


 

///* Scalatest - Matchers - Order and ranges


import org.scalatest.Matchers._
val one = 1
one should be < 7                               // works for any T when an implicit Ordered[T] exists
one should be <= 7
one should be >= 0

val num = 8
num should (be >= 1 and be <= 10)               // one way to ensure a value is within a range or tolerance.

val seven = 7
seven should be (6 +- 2)                        // another way to verify a range
7.0 should be (6.9 +- 0.2)                      // works for both integral and floating point types


 

///* Scalatest - Matchers - Checking boolean properties with be

import org.scalatest.Matchers._                 // access a boolean property dynamically (via reflection)

Set(1, 2, 3) should be ('nonEmpty)              // will also match isNonEmpty  '

import java.io.File
val temp = new File("aFile.txt")
temp should be a ('file)                        // can use an optional 'a' or 'an'  '

import java.awt._
import event.KeyEvent
val canvas = new Canvas
val keyEvent = new KeyEvent(canvas, 0, 0, 0, 0, '0')
keyEvent should be an ('actionKey)  //'

import org.scalatest._

class FileBePropertyMatcher extends             // For static checking, a BePropertyMatcher can be defined.
    BePropertyMatcher[java.io.File] {
  def apply(left: File) =
    BePropertyMatchResult(left.isFile, "file")
}
val file = new FileBePropertyMatcher
val temp = new File("aFile.txt")
temp should be a file


 

///* Scalatest - Matchers - Using arbitrary be matchers

import org.scalatest._                          // To place an arbitrary token after be, use a BeMatcher.
import Matchers._
import matchers._
class OddMatcher extends BeMatcher[Int] {
  def apply(left: Int) =
    MatchResult(
      left % 2 == 1,
      left.toString + " was even",
      left.toString + " was odd"
    )
}
val odd = new OddMatcher
7 should be (odd)
 

///* Scalatest - Matchers - Checking arbitrary properties with have

import org.scalatest.Matchers._
case class Book(title: String, author: String, pubYear: Int)
val book = Book("A Book", "Sally", 2008)

book should have (                                 // check arbitrary properties dynamically (via reflection)
  'title ("A Book"),                               // see HavePropertyMatcherGenerator
  'author ("Sally"),
  'pubYear (2008)
)
//'
import org.scalatest._                             // check arbitrary properties statically with HavePropertyMatchers
import Matchers._
import matchers._
case class Book(title: String, author: String)
def title(expectedValue: String) = new HavePropertyMatcher[Book, String] {
  def apply(book: Book) = HavePropertyMatchResult(
    book.title == expectedValue,
    "title",
    expectedValue,
    book.title
  )
}
def author(expectedValue: String) = new HavePropertyMatcher[Book, String] {
  def apply(book: Book) = HavePropertyMatchResult(
    book.author == expectedValue,
    "author",
    expectedValue,
    book.author
  )
}
val book = Book("A Book", "Sally")
book should have (
  title ("A Book"),
  author ("Sally")
)



 

///* Scalatest - Matchers - Working with Scala and Java collections

import org.scalatest.Matchers._
List.empty[Int] should be (empty)                         // various ways of checking whether a collection is empty 
List.empty[Int] should be (Nil)
Map.empty[Int, String] should be (Map())
Set.empty[Int] should be (Set.empty)

Set(1, 2, 3) should have size (3)                         // check the size of a collection 
Set(1, 2, 3) should have size (3)
List(1, 2, 3) should have length (3)

Set("three", "five", "seven") should contain ("five")     // check whether a traversable contains an element

val map = Map(1 -> "one", 2 -> "two", 3 -> "three")
map should contain key (1)                                // check whether a map contains a particular key or value
should contain value ("two")

import PartialFunctionValues._                                      // check both that a map is defined at a key as
Map("I" -> 1, "II" -> 2, "III" -> 3).valueAt("II") should equal (2) // well as something about its value

import java.util._
val javaCol = new ArrayList[Int]
javaCol should be (empty)                                 // Check if a java.util.Collection is empty


val javaMap = new java.util.HashMap[Int, String]
javaMap.put(1, "one")
javaMap.put(2, "two")
javaMap.put(3, "three")
javaMap should have size (3)                              // check the size of a java.util.Map

val javaList = new ArrayList[Int]
javaList.add(1)
javaList.add(2)
javaList.add(3)
javaList should have length (3)                           // check the length of a java.util.List

val javaCol = new HashSet[String]
javaCol.add("three")
javaCol.add("five")
javaCol.add("seven")                                      // Check whether a java.util.Collection contains a
javaCol should contain ("five")                           // particular element

val javaMap = new HashMap[Int, String]
javaMap.put(1, "one")
javaMap.put(2, "two")
javaMap.put(3, "three")
javaMap should contain key (1)                            // Check whether a java.util.Map contains a particular key

val javaMap = new HashMap[Int, String]
javaMap.put(1, "Hi")
javaMap.put(2, "Howdy")
javaMap.put(3, "Hai")
javaMap should contain value ("Howdy")                    // Check whether a java.util.Map contains a particular value




 

///* Scalatest - Matchers - Working with Option and Either

import org.scalatest.Matchers._

val option = Some(1)
option should be (Some(1))                                // Check whether an Option contains a certain value

val option = Some(1)
option shouldBe defined                                   // Check whether an Option is defined

val option = Some(1)
option shouldBe empty                                     // Check whether an Option is empty

val option = None
option should be (None)                                   // Another way to check whether an Option is empty

val either = Left(1)
either should be ('left)                                  // Check whether an Either is a Left

val either = Left(1)
either should be (Left(1))                                // Check whether an Either is Left with a certain value

val either = Right(1)
either should be ('right)                                 // Check whether an Either is a Right

val either = Right(1)
either should be (Right(1))                               // Check whether an Either is Right with a certain value

import org.scalatest.EitherValues._
val either = Right(3)                                     // Check both that an Either is defined as well as something
either.right.value should be < 7                          // about its value


 

///* Scalatest - Checking for emptiness

import org.scalatest.Matchers._

List.empty shouldBe empty                                 // Check if a List (and any GenTraversable) is empty

None shouldBe empty                                       // Check if an Option is empty

"" shouldBe empty                                         // Check if a String is empty

new java.util.HashMap[Int, Int] shouldBe empty            // Check if a java.util.Map is empty

new { def isEmpty = true } shouldBe empty                 // Check if a structural type is empty

Array.empty shouldBe empty                                // Check if an Array is empty


 

///* Scalatest - Matchers - Working with "containers"

import org.scalatest.Matchers._

List(1, 2, 3) should contain (2)                            // Check if a List (or any GenTraversable) contains the
                                                            // specified element
Map('a' -> 1, 'b' -> 2, 'c' -> 3) should contain ('b' -> 2) // Check if a Map contains the specified mapping

Set(1, 2, 3) should contain (2)                             // Check if various collections contain a specified element

Array(1, 2, 3) should contain (2)

"123" should contain ('2')

Some(2) should contain (2)

import org.scalactic.Explicitly._                          // Using StringNormalizations explicitly
import org.scalactic.StringNormalizations._
(List("Hi", "Di", "Ho") should contain ("ho")) (after being lowerCased)


 

///* Scalatest - Matchers - Working with "aggregations"

import org.scalatest.Matchers._
List(1, 2, 3) should contain atLeastOneOf (2, 3, 4)  // Check if a List (or any GenTraversable) contains at least one of
                                                     // the specified elements

Array(1, 2, 3) should contain atLeastOneOf (3, 4, 5) // Check if an Array contains at least one of the specified
                                                     // elements

"abc" should contain atLeastOneOf ('c', 'a', 't')    // Check if a String contains at least one of the specified
                                                     // characters

import org.scalactic.Explicitly._                       // Using StringNormalizations explicitly
import org.scalactic.StringNormalizations._
(Vector(" A", "B ") should contain atLeastOneOf ("a ", "b", "c")) (after being lowerCased and trimmed)

List(1, 2, 3, 4, 5) should contain atMostOneOf (5, 6, 7) // Check if a List (or any GenTraversable) contains at most
                                                         // one of the specified element

List(1, 2, 3, 4, 5) should contain allOf (2, 3, 5)       // Check if a List (or any GenTraversable) contains all of
                                                         // the specified elements

List(1, 2, 3, 2, 1) should contain only (1, 2, 3)        // Check if a List (or any GenTraversable) contains only the
                                                         // specified elements

val expected = Vector(3, 2, 3, 1, 2, 3)                          // Check if a List (or any GenTraversable) contains
List(1, 2, 2, 3, 3, 3) should contain theSameElementsAs expected // only the same elements as the specified Vector
                                                                 // (or any GenTraversable)


 

///* Scalatest - Matchers - Working with "sequences"

import org.scalatest.Matchers._                                       // Check if a List (or any GenTraversable)
List(1, 2, 2, 3, 3, 3) should contain inOrderOnly (1, 2, 3)           // contains only the specified elements in same
                                                                      // order
                                                                      
Array(1, 2, 2, 3, 3, 3) should contain inOrderOnly (1, 2, 3)          // Check if an Array contains only the specified
                                                                      // elements in same order

"122333" should contain inOrderOnly ('1', '2', '3')                   // Check if a String contains only the specified
                                                                      // characters in same order

List(0, 1, 2, 2, 99, 3, 3, 3, 5) should contain inOrder (1, 2, 3)     // Check if a List (or any GenTraversable)
                                                                      // contains the specified elements in same order

List(1, 2, 3) should contain theSameElementsInOrderAs Vector(1, 2, 3) // Check if a List (or any GenTraversable)
                                                                      // contains the same elements in same order as the
                                                                      // specified Vector (or any GenTraversable)

 

///* Scalatest - Matchers - Working with iterators

import org.scalatest.Matchers._// Use toStream to convert Iterator to Stream to work with contain
val it = List(1, 2, 3).iterator
it.toStream should contain (2)

 

Matchers - Working with File

import org.scalatest.Matchers._
import java.io.File
val javaHomeDir = new File(System.getProperty("java.home"))

javaHomeDir should exist                                     // Check if a directory exists 

val javaHomeLicenseFile = javaHomeDir +
  System.getProperty("file.separator") + "LICENSE")

javaHomeLicenseFile should exist                             // Check if a file exists 

javaHomeLicenseFile shouldBe readable                        // Check if a file is readable 

javaHomeLicenseFile shouldBe writable                        // Check if a file is writable 


 

///* Scalatest - Matchers - Inspector Shorthands

import org.scalatest.Matchers._
val xs = List(1, 2, 3, 4, 5)

all (xs) should be < 10                                  // check that all elements in xs are less than 10

atMost (2, xs) should be >= 4                            // check that at most 2 elements in xs are greater than or
                                                         // equal to 4

atLeast (3, xs) should be < 5                            // check that at least 3 elements in xs are lesser than 5

between (2, 3, xs) should (be > 1 and be < 5)            // check that 2 to 3 elements in xs are greater than 5 and less
                                                         // than 5

exactly (2, xs) should be <= 2                           // check that 2 elements in xs are less than or equal to 2

every (xs) should be < 10                                // check that all elements in xs are less than 10


 

///* Scalatest - Matchers - Logical expressions with and, or, and not

import org.scalatest.Matchers._
val result = 8
result should (be > 0 and be < 10)                         // You can and matcher expressions together

val map = Map("hi" -> "HI", "hei" -> "HEI", "he" -> "HE")
map should (contain key ("hi") or contain key ("ho"))      // You can or matcher expressions together

val result = "Hello Word"                                  // You can negate a matcher expression
result should not be (null)

val map = Map("one" -> 1, "two" -> 2, "three" -> 3)
map should (contain key ("two") and not contain value (7)) // Another example of negation


 

///* Scalatest - Matchers - When you need a different matcher

import org.scalatest.Matchers._
import java.awt._
import event.KeyEvent

val canvas = new Canvas                                      // You can check boolean properties dynamically with be and
val keyEvent = new KeyEvent(canvas, 0, 0, 0, 0, '0')         // a Symbol (the tick mark syntax). For statically typed
keyEvent should be an ('actionKey)                           // approach, use a BePropertyMatcher or BeMatcher.

case class Book(title: String, author: String)               // You can check arbitrary properties dynamically with have
val book = Book("A Tale of Two Cities", "Sally")             // and a Symbol (the tick mark syntax). For a statically
book should have ('title("A Tale of Two Cities"))            // typed approach, use a HavePropertyMatchers.

val defined = 'defined                                       // One way to get rid of the tick mark for a dynamic
Some("hi") should be (defined)                               // property check


val beDefined = be ('defined)                                // One way to get rid of the tick mark and a pair of
Some("hi") should beDefined                                  // parentheses

val beWithinTolerance = be >= 0 and be <= 10                 // You can combine matchers with and, or, and not
8 should beWithinTolerance

import matchers._                                            // You can compose a matcher with a function that
val beOdd = Matcher((i: Int) =>                              // transforms the input type
  MatchResult(
    i % 2 != 0,
    i + " is not odd",
    i + " is odd"))

val beOddAsInt = beOdd compose { (s: String) => s.toInt }
"3" should beOddAsInt

import java.io.File                                           // You can also use matcher composition to create a new
def endWithExtension(ext: String) =                           // matcher given a parameter
endWith(ext) compose { (f: File) => f.getPath }
new File("output.txt") should endWithExtension("txt")

val beOdd =  Matcher { (left: Int) =>                         // You can use a factory method to define a custom matcher
  MatchResult(                                                // (such factory methods also exist for BeMatcher,
    left % 2 == 1,                                            // BePropertyMatcher, and HavePropertyMatcher
    left + " was not odd",
    left + " was odd")
  }
3 should beOdd

val beOdd = new Matcher[Int] {                                // Or you can write a full-blown matcher.
  def apply(left: Int) = MatchResult(
    left % 2 == 1,
    left + " was not odd",
    left + " was odd")
}
3 should beOdd


 

///* Scalatest - Matchers - Selenium DSL

import org.scalatest._                        // Example of using WebBrowser and HtmlUnit for selenium test.  You could use other DSL listed in this section. 
import selenium._                             // Note: You'll need to setup your project to use Selenium and HtmlUnit driver for the example to work correctly.

class BlogSpec extends FeatureSpec with WebBrowser with HtmlUnit {
  val host = "http://localhost:9000/"
  scenario("The blog app home page should have the correct title") {
    go to (host + "index.html")
    pageTitle should be ("Awesome Blog")
  }
}

go to "http://www.scalatest.org"                              // Go to ScalaTest website

pageTitle should be ("ScalaTest")                             // Check page title

click on "q"                                                  // Click on element which has attribute id or name = "q"

click on id("q")                                              // Click on element which has attribute id = "q"

click on name("name")                                         // Click on element which has attribute name = "q"

click on tagName("input")                                     // Click on element which is an 'input' tag

click on className("quickref")                                // Click on element which has the CSS class 'quickref' tag

cssSelector("a[id='aLink']").element.tagName should be ("a")  // Check a element's tag name, selected by using CSS
                                                              // selector

linkText("Click Me!").element.tagName should be ("a")         // Check a element's tag name, selected by using the link
                                                              // text

partialLinkText("Click").element.tagName should be ("a")      // Check a element's tag name, selected by using the
                                                              // partial link text

enter("Cheese!")                                              // Enter "Cheese!" into currently selected text element

submit()                                                      // Submit form

textField("q").value = "Cheese!"                              // Set text field (which has attribute id or name = "q")
                                                              // to "Cheese!"

textField("q").value should be ("Cheese!")                    // Read and check a text field (which has attribute id or
                                                              // name = "q") value

radioButtonGroup("group1").value = "Option 2"                 // Set radio button group (which has group name = "group1")
// or                                                         // to choose "Option 2"
radioButtonGroup("group1").selection = Some("Option 2")


radioButtonGroup("group1").value should be ("Option 2")       // Read and check radio button group (which has group
// or                                                         // name = "group1") chosen value
radioButtonGroup("group1").selection should be (Some("Option 2"))

click on radioButton("opt1")                                  // Click on a radio button (which has attribute id or
                                                              // name = "opt1")

radioButton("opt1").isSelected should be (true)               // Check if a radio button (which has attribute id or
                                                              // name = "opt1") is selected

checkbox("cbx1").select()                                     // Select a check box (which has attribute id or name =
                                                              // "cbx1")

checkbox("cbx1").clear()                                      // Clear a check box (which has attribute id or name =
                                                              // "cbx1")

checkbox("cbx1").isSelected should be (true)                  // Check if a check box (which has attribute id or name =
                                                              // "cbx1") is selected

singleSel("select1").value = "option2"                        // Set a single-selection dropdown list (which has
// or                                                         // attribute id or name = "select1") to choose "option2"
singleSel("select1").selection = Some("option2")


singleSel("select1").clear()                                  // Clear the selection of a single-selection dropdown list
// or                                                         // (which has attribute id or name = "select1")
singleSel("select1").selection = None

singleSel("select1").value should be ("option2")              // Read and check currently selected value of a
// or                                                         // single-selection dropdown list (which has attribute id
singleSel("select1").selection should be (Some("option2"))    // or name = "select1")

multiSel("select2").values = Seq("option5", "option6")        // Set the selection of a multi-selection list (which has
                                                              // attribute id or name = "select2")

multiSel("select2").values += "option3"                       // Select "option3" in addition to current selection of a
                                                              // multi-selection list (which has attribute id or name =
                                                              // "select2")

multiSel("select2").clear("option5")                          // Clear "option5" from current selection of a
                                                              // multi-selection list (which has attribute id or name =
                                                              // "select2")

multiSel("select2").clearAll()                                // Clear all selection of a multi-selection list (which
                                                              // has attribute id or name = "select2")

multiSel("select2").values should have size 2                 // Read and check currently selected values of a
multiSel("select2").values(0) should be ("option5")           // multi-selection list (which has attribute id or name =
multiSel("select2").values(1) should be ("option6")           // "select2")

goBack()                                                      // Go back to previous page in history

goForward()                                                   // Go forward to next page in history

reloadPage()                                                  // Reload the current page

add cookie ("name1", "value1")                                // Add new cookie with name = "name1" and value = "value1"

cookie("name1").value should be ("value1")                    // Read and check a cookie's value

delete cookie "name1"                                         // Delete cookie "name1

delete all cookies                                            // Delete all cookies under the same domain.


capture to "MyScreenShot"                                     // Capture the screen and save as "MyScreenShot.png"

setCaptureDir("/home/your_name/screenshots")                  // Set the directory to save captured pictures.

withScreenshot {                                              // Auto capture screen when something goes wrong (e.g.
assert("Gold" == "Silver", "Expected gold, but got silver")   // test failed)
}

close()                                                       // Close current browser window

quit()                                                        // Close all windows and exit the driver


 

///* Scalatest - Matchers - Concurrent Support

import org.scalatest._                                                        // eventually retries the block until it
import concurrent.Eventually._                                                // no longer throws an exception, using a
import Matchers._                                                             // timeout and interval taken from an
                                                                              // implicit PatienceConfig. The default
                                                                              // PatienceConfig is 150 milliseconds
                                                                              // timeout and 15 milliseconds interval.
                                                                              // If you import IntegrationPatience,
                                                                              // you'll get a 15 second timeout and 150
                                                                              // milliseconds interval. If you want
                                                                              // something else, you can define your own
                                                                              // implicit PatienceConfig like the next
                                                                              // example.

val it = Vector("sad", "happy").iterator
eventually { it.next shouldBe "happy" }

import org.scalatest.time.SpanSugar._                                         // Define custom implicit PatienceConfig
implicit val patienceConfig =
PatienceConfig(timeout = scaled(2 seconds), interval = scaled(5 millis))

import org.scalatest.time._                                                   // Use eventually with explicit timeout
eventually (timeout(Span(5, Seconds))) { /*code omitted*/ }

eventually (timeout(5 seconds), interval(5 millis)) { /*code omitted*/ }      // Use eventually with explicit timeout
                                                                              // and interval


 

///* Scalatest - Matchers - Inspectors

import org.scalatest._
import Matchers._
import Inspectors._

forAll(List(1, 2, 3)) { _ should be > 0 }                     // Check that every element passes the assertion block

forAtLeast(1, List(1, 2, 3)) { _ should be > 2 }              // Check that at least the specified number of elements
                                                              // pass the assertion block

forAtMost(2, List(1, 2, 3)) { _ should be > 1 }               // Check that at most the specified number of elements pass
                                                              // the assertion block

forBetween(2, 4, List(1, 2, 3, 4, 5)) { _ should be > 2 }     // Check that the specified minimum and maximum number of
                                                              // elements (inclusive) pass the assertion block

forEvery(List(1, 2, 3)) { _ should be > 0 }                   // Check that every element passes the assertion block,
                                                              // listing all failing elements on failure (whereas forAll
                                                              // just reports the first failing element)

forExactly(2, List(1, 2, 3)) { _ should be > 1 }              // Check that the exact specified number of elements pass
                                                              // the assertion block


 

///* Scalatest - Matchers - Single-element collections

import org.scalatest._                                        // Use loneElement to check that the Set (or any
import Matchers._                                             // GenTraversable) contains single element, and that
import LoneElement._                                          // element is greater than or equal to 10.  If the
                                                              // GenTraversable does not contain single element,
Set(18).loneElement should be >= 10                           // TestFailedException will be thrown.


 

///* Scalatest - Matchers - Inside

import org.scalatest._
import Matchers._
import Inside._                                               // Checking nested object graph using Inside

case class Name(first: String, middle: String, last: String)

case class Record(name: Name, age: Int)
val rec =
  Record(
    Name("Sally", "Anna", "Jones"),
    38
  )

inside (rec) { case Record(name, age) =>
  inside (name) { case Name(first, middle, last) =>
    first should be ("Sally")
  }
}


 

///* Scalatest - Matchers - OptionValues

import org.scalatest._
import OptionValues._
val anOption = Some(18)                        // Use OptionValues to check Option's value, using assert
assert(anOption.value > 9)

import Matchers._
val anOption = Some(18)                        // Use OptionValues to check Option's value, using Matchers syntax
anOption.value should be > 9


 

///* Scalatest - Matchers - EitherValues

import org.scalatest._
import EitherValues._

val either1: Either[String, Int] = Right(16)                         // Use EitherValues to check left and right value
assert(either1.right.value > 9)                                      // of an Either, using assert
val either2: Either[String, Int] = Left("Muchas problemas")
assert(either2.left.value == "Muchas problemas")

import Matchers._
val either1: Either[String, Int] = Right(16)                         // Use EitherValues to check left and right value
either1.right.value should be > 9                                    // of an Either, using Matchers syntax
val either2: Either[String, Int] = Left("Muchas problemas")
either2.left.value should be ("Muchas problemas")


 

///* Scalatest - Matchers - PartialFunctionValues

import org.scalatest._
import PartialFunctionValues._
val map = Map("one" -> 1, "two" -> 2, "three" -> 3)                  // Use PartialFunctionValues to check value of a
assert(map.valueAt("two") == 2)                                      // PartialFunction, using assert

import Matchers._
val map = Map("one" -> 1, "two" -> 2, "three" -> 3)                  // Use PartialFunctionValues to check value of a
map.valueAt("two") should equal (2)                                  // PartialFunction, using Matchers syntax


 

///* Scalatest - Matchers - PrivateMethodTester

import org.scalatest.PrivateMethodTester._

class TaxCalculator {                                                // Use PrivateMethodTester to invoke a private
  private def calc(amount: Double, percentage: Double): Double =     // method for testing purpose
    amount * percentage / 100.00
  def calculateTax(amount: Double): Double =
    calc(amount, 5.00)
}
val calculator = new TaxCalculator
val calcMethod = PrivateMethod[Double]('calc)               //'
calculator invokePrivate calcMethod(1000.00, 8.88)
 










///*** Sepcs2 Specification 
//https://etorreborre.github.io/specs2/guide/SPECS2-4.0.2/org.specs2.guide.Installation.html
//build.sbt 
libraryDependencies += "org.specs2" %% "specs2-core" % "4.0.2" % "test"

scalacOptions in Test ++= Seq("-Yrangepos")


///*** Sepcs2 Specification  - Styles

//Acceptance specification
//An acceptance specification extends org.specs2.Specification and defines the is method. 

class MySpecification extends org.specs2.Specification { def is = s2"""

 this is my specification
   where example 1 must be true           $e1
   where example 2 must be true           $e2
                                          """

  def e1 = 1 must_== 1
  def e2 = 2 must_== 2
}


//Unit specification
//A unit specification extends org.specs2.mutable.Specification 
//and uses the >> operator to create “blocks” containing Texts and Examples:
class MySpecification extends org.specs2.mutable.Specification {
  "this is my specification" >> {
    "where example 1 must be true" >> {
      1 must_== 1
    }
    "where example 2 must be true" >> {
      2 must_== 2
    }
  }
}


///*** Sepcs2 Specification -Matchers 
//https://etorreborre.github.io/specs2/guide/SPECS2-4.0.2/org.specs2.guide.Matchers.html

//Equality
1 must beEqualTo(1) 
1 must be_==(1) 
1 must_== 1 
1 mustEqual 1 
1 should_== 1 
1 === 1 
1 must be equalTo(1) 

//the beEqualTo matcher is using the regular == Scala equality. 
//However in the case of Arrays, Scala == is just using reference equality, eq. 
//However beEqualTo matcher has been adapted to use the .deep method on Arrays, 
//so that Array(1, 2, 3) === Array(1, 2, 3) (despite the fact that Array(1, 2, 3) != Array(1, 2, 3)).


//Other types of equality 
beTypedEqualTo          typed equality. a must beTypedEqualTo(b) will not work if a and b don’t have compatible types 
be_===                  synonym for beTypedEqualTo 
a ==== b                synonym for a must beTypedEqualTo(b) 
a must_=== b            similar to a must_== b but will not typecheck if a and b don’t have the same type 
be_==~                  check if (a: A) == conv(b: B) when there is an implicit conversion conv from B to A 
beTheSameAs             reference equality: check if a eq b (a must be(b) also works) 
be                      a must be(b): synonym for beTheSameAs 
beTrue, beFalse         shortcuts for Boolean equality 
beLike                  partial equality, using a PartialFunction[T, MatchResult[_]]: (1, 2) must beLike { case (1, _) => ok } 

//Check Other type of matchers for 
String
Traversable
Numeric
Option/Either
Try
Future
Exception
Map
Any

//With optional extra 
libraryDependencies += "org.specs2" %% "specs2-matcher-extra" % "4.0.2" % "test"
//has functionality of 
//https://etorreborre.github.io/specs2/guide/SPECS2-4.0.2/org.specs2.guide.Matchers.html
Disjunction
Disjunction
Validation
Scalaz Task
Case class
Result
Termination
Xml
Json
File
Content
Parser
Typecheck
Scala Interpreter
Dependency Matchers



///*** Logging 
libraryDependencies ++= Seq("org.slf4j" % "slf4j-api" % "1.7.5",
                            "org.slf4j" % "slf4j-log4j12" % "1.7.25",
                            "log4j" % "log4j" % "1.2.17")

//src/main/resources/lg4j.properties
# Root logger option
log4j.rootLogger=INFO, stdout

# Direct log messages to stdout
log4j.appender.stdout=org.apache.log4j.ConsoleAppender
log4j.appender.stdout.Target=System.out
log4j.appender.stdout.layout=org.apache.log4j.PatternLayout
log4j.appender.stdout.layout.ConversionPattern=%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1} - %m%n


//code 
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Pizza {
  val logger = LoggerFactory.getLogger(classOf[Pizza])
  logger.info("Hello from the Pizza class")
}

object Main extends App {
  val p = new Pizza
}