//build.sbt 
libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.5"

//Custom equality

Scalactics Equality typeclass enables you to define alternate notions of equality for specified types that can be used with Scalactics === and !== syntax (and ScalaTests matcher) syntax. 

For example, say you have a case class that includes a Double value: 
scala> case class Person(name: String, age: Double)
defined class Person


Imagine you are calculating the age values in such as way that occasionally tests are failing because of rounding differences that you actually dont care about. For example, you expect an age of 29.0, but youre sometimes seeing 29.0001: 
scala> import org.scalactic._
import org.scalactic._

scala> import TripleEquals._
import TripleEquals._

scala> Person("Joe", 29.0001) === Person("Joe", 29.0)
res0: Boolean = false


Scalactics === operator looks for an implicit Equality[L], where L is the left-hand type: in this case, Person. Because you didnt specifically provide an implicit Equality[Person], === will fall back on default equality, which will call Persons equals method. That equals method, provided by the Scala compiler because Person is a case class, will declare these two objects unequal because 29.001 does not exactly equal 29.0. 

To make the equality check more forgiving, you could define an implicit Equality[Person] that compares the age Doubles with a tolerance, like this: 
scala> import Tolerance._
import Tolerance._

scala> implicit val personEq =
     |   new Equality[Person] {
     |     def areEqual(a: Person, b: Any): Boolean =
     |       b match {
     |         case p: Person => a.name == p.name && a.age === p.age +- 0.0002
     |         case _ => false
     |       }
     |   }
personEq: org.scalactic.Equality[Person] = $anon$1@2b29f6e7


Now the === operator will use your more forgiving Equality[Person] for the equality check instead of default equality: 
scala> Person("Joe", 29.0001) === Person("Joe", 29.0)
res1: Boolean = true


Default equality

Scalactic defines a default Equality that treats arrays specially. Its areEqual method works by first calling .deep on any passed array, then calling == on the resulting left-hand object, passing in the resulting right-hand object. If a more specific implicit Equality is not available for the left-hand type in an === or !== expression, default equality will be used. 

Here are some examples showing the special treatment of arrays: 
scala> import org.scalactic._
import org.scalactic._

scala> import TripleEquals._
import TripleEquals._

scala> Array(1, 2, 3) == Array(1, 2, 3)
res0: Boolean = false

scala> Array(1, 2, 3) === Array(1, 2, 3)
res1: Boolean = true

scala> Array(1, 2, 3) == List(1, 2, 3)
<console>:14: warning: comparing values of types Array[Int] and List[Int] using `== will always yield false
              Array(1, 2, 3) == List(1, 2, 3)
                             ^
res2: Boolean = false

scala> Array(1, 2, 3) === List(1, 2, 3)
res3: Boolean = true

scala> List(1, 2, 3) == Array(1, 2, 3)
res4: Boolean = false

scala> List(1, 2, 3) === Array(1, 2, 3)
res5: Boolean = true


You can obtain a default equality via the default method of the  Equality companion object, or from the defaultEquality method defined in TripleEqualsSupport. 
Constrained equality

Scalactic provides a way to get a compile-time type error if two types being compared with === violate a tunable type Constraint. The basic TripleEquals trait allows you to compare any type for equality with any other type: 
scala> import org.scalactic._
import org.scalactic._

scala> import TripleEquals._
import TripleEquals._

scala> Some(1) === 2
res0: Boolean = false

scala> 1 === 1L
res1: Boolean = true


TypeCheckedTripleEquals will compile only if the left and right sides are in a subtype/supertype relationship (including when the left and right sides are exactly the same type): 
scala> import TypeCheckedTripleEquals._
import TypeCheckedTripleEquals._

scala> Some(1) === 2
<console>:17: error: types Some[Int] and Int do not adhere to the type constraint selected for the === and !== operators;
    the missing implicit parameter is of type org.scalactic.Constraint[Some[Int],Int]
              Some(1) === 2

scala> 1 === 1L
<console>:17: error: types Int and Long do not adhere to the type constraint selected for the === and !== operators;
    the missing implicit parameter is of type org.scalactic.Constraint[Int,Long]
              1 === 1L
                ^


Note that the latter case is an example of an over-protective type error. If this expression were allowed to compile and run, it would yield true, because the Int would be implicitly converted to Long. One way to solve such errors is with a type ascription. In this case, you can case either side to AnyVal, a supertype of both Int and Long: 
scala> 1 === (1L: AnyVal)
res7: Boolean = true

scala> 1L === (1: AnyVal)
res8: Boolean = true


Since every type is a subtype of Any, you can always solve an over-protective type error with a type ascription of Any. 
Tolerance

The Tolerance trait contains an implicit conversion that adds a +- method to Numeric types, which, combined with one of the TripleEquals traits, enables you to determine whether the numeric value is with a tolerance. 

For example, the TripleEquals trait (and its type-checking siblings TypeCheckedTripleEquals and ConversionCheckedTripleEquals) enable you to write: 
result === 10.0 +- 0.5


Here are some examples in the Scala REPL: 
scala> import org.scalactic._
import org.scalactic._

scala> import TripleEquals._
import TripleEquals._

scala> import Tolerance._
import Tolerance._

scala> val result = 2.000001
result: Double = 2.000001

scala> result === 2.0 +- .001
res0: Boolean = true

scala> result === 2.0 +- .000000001
res1: Boolean = false

Normalization

The Scalactic Normalization trait allows you to define a custom ways normalize objects based on their type. 

For example, to normalize Doubles by truncating off any decimal part, you might write: 
import org.scalactic._

val truncated =
  new Normalization[Double] {
    def normalized(d: Double) = d.floor
  }


Given this definition you could use it with the explicitly DSL like this: 
import org.scalatest._
import Matchers._
import TypeCheckedTripleEquals._

(2.1 should === (2.0)) (after being truncated)

Uniformity

An important subtype of Normalization is Uniformity, which defines a custom way to normalize instances of a type that can also handle normalization of that type when passed as Any. 

For example, to normalize Doubles by truncating off any decimal part, you might write: 
import org.scalactic._

val truncated =
  new Uniformity[Double] {
   def normalized(d: Double) = d.floor
   def normalizedCanHandle(o: Any) = o.isInstanceOf[Double]
   def normalizedOrSame(o: Any): Any =
     o match {
       case d: Double => normalized(d)
       case _ => o
     }
 }


Given this definition you could use it with the Explicitly DSL like this: 
import org.scalatest._
import Matchers._

2.1 should equal (2.0) (after being truncated)


If you make the truncated val implicit and import or mix in the members of NormMethods, you can access the behavior by invoking .norm on Doubles. 
implicit val doubleUniformity = truncated
import NormMethods._

val d = 2.1
d.norm // returns 2.0
The Explicitly DSL

Scalactics Explicitly trait provides the “explicitly DSL,” which facilitates the explicit specification of an Equality[T] or a Uniformity[T] where Equality[T] is taken implicitly. 

The Explicitly DSL can be used with the === and !== operators of Scalactic as well as the should equal, be, contain, and === syntax of ScalaTest matchers. 

If you want to customize equality for a type in general, you would likely want to place an implicit Equality[T] for that type in scope (or in Ts companion object). That implicit equality definition will then be picked up and used when that type is compared for equality with the equal, be, and contain matchers in ScalaTest tests and with === in both tests and production code. If you just want to use a custom equality for a single comparison, however, you may prefer to pass it explicitly. For example, if you have an implicit Equality[String] in scope, you can force a comparison to use the default equality with this syntax: 
// In production code:
if ((result === "hello")(decided by defaultEquality)) true else false

// In tests:
result should equal ("hello") (decided by defaultEquality)


The explicitly DSL also provides support for specifying a one-off equality that is based on a normalization. For example, Scalactic offers a StringNormalizations trait that provides methods such as trimmed and lowerCased that return Uniformity[String] instances that normalize by trimming and lower-casing, respectively. If you bring those into scope by mixing in or importing the members of StringNormalizations, you could use the explicitly DSL like this: 
// In production code:
if ((result === "hello")(after being lowerCased)) true else false

// In tests:
result should equal ("hello") (after being lowerCased and trimmed)

Or and Every

The Or and Every types of Scalactic allow you to represent errors as an “alternate return value” (like Either) and to optionally accumulate errors. Or represents a value that is one of two possible types, with one type being “good” (a value wrapped in an instance of Good) and the other “bad” (a value wrapped in an instance of Bad). 

The motivation for Or

Or differs from Scalas Either type in that Either treats both its Left and Right alternatives in an identical manner, whereas Or treats its two alternatives differently: it favors Good over Bad. Because of this, it is more convenient to work with Ors when you prefer one alternative over the other; for example, if one alternative represents a valid result and another represents an error. 

To illustrate, imagine you want to create instances this Person class from user input strings: 
case class Person(name: String, age: Int)


You might write a method that parses the name from user input string and returns an Option[String]: None if the string is empty or blank, else the trimmed string wrapped in a Some: 
def parseName(input: String): Option[String] = {
  val trimmed = input.trim
  if (!trimmed.isEmpty) Some(trimmed) else None
}


You might also write a method that parses the age from user input string and returns an Option[Int]: None if either the string is not a valid integer or it is a negative integer, else the string converted to an integer wrapped in a Some: 
def parseAge(input: String): Option[Int] = {
  try {
    val age = input.trim.toInt
    if (age >= 0) Some(age) else None
  }
  catch {
    case _: NumberFormatException => None
  }
}


With these building blocks you could write a method that parses name and age input strings and returns either a Person, wrapped in a Some, or None if either the name or age, or both, was invalid: 
  def parsePerson(inputName: String, inputAge: String): Option[Person] =
  for {
    name <- parseName(inputName)
    age <- parseAge(inputAge)
  } yield Person(name, age)


Here are some examples of invoking parsePerson: 
parsePerson("Bridget Jones", "29")
// Result: Some(Person(Bridget Jones,29))

parsePerson("Bridget Jones", "")
// Result: None

parsePerson("Bridget Jones", "-29")
// Result: None

parsePerson("", "")
// Result: None


Now imagine you want to give an error message back if the users input is invalid. You might rewrite the parsing methods to return an Either instead. In this case, the desired result is a valid name or age, which by convention should be placed on the right of the Either. The left will be a String error message. Heres the new parseName function, which returns an Either[String, String]: 
def parseName(input: String): Either[String, String] = {
  val trimmed = input.trim
  if (!trimmed.isEmpty) Right(trimmed) else Left(s""""${input}" is not a valid name""")
}


And heres the new parseAge function, which returns an Either[String, Int]: 
def parseAge(input: String): Either[String, Int] = {
  try {
    val age = input.trim.toInt
    if (age >= 0) Right(age) else Left(s""""${age}" is not a valid age""")
  }
  catch {
    case _: NumberFormatException => Left(s""""${input}" is not a valid integer""")
  }
}


The new parsePerson method will return an Either[String, Person]: 
def parsePerson(inputName: String, inputAge: String): Either[String, Person] =
  for {
    name <- parseName(inputName).right
    age <- parseAge(inputAge).right
  } yield Person(name, age)


Note that Either requires you to add .right at the end of each generator in the for expression. Although the convention is to place the valid result on the right, you must explicitly (and repetitively) indicate that youve done so by transforming the Either to a RightProjection by invoking .right at each step. Given this implementation, the parsePerson method will now short-circuit at the first sign of trouble (as it did when we used an Option), but you now get the first error message returned in a Left. Here are some examples: 
parsePerson("Bridget Jones", "29")
// Result: Right(Person(Bridget Jones,29))

parsePerson("Bridget Jones", "")
// Result: Left("" is not a valid integer)

parsePerson("Bridget Jones", "-29")
// Result: Left("-29" is not a valid age)

parsePerson("", "")
// Result: Left("" is not a valid name)


An Either with “attitude”

Because Or declares one alternative to be “good” and the other “bad,” it is more convenient than Either in this kind of situation. One difference to note with Or is that the Good alternative is on the left, Bad on the right. The reason is that Or is designed to be written using infix notation, and placing the “happy path” first is more readable. For example, instead of writing: 
Or[Int, ErrorMessage]


You can write: 
Int Or ErrorMessage


Heres how the parseName method might be written using an Or, where ErrorMessage is a type alias for String declared in the org.scalactic package object: 
import org.scalactic._

def parseName(input: String): String Or ErrorMessage = {
  val trimmed = input.trim
  if (!trimmed.isEmpty) Good(trimmed) else Bad(s""""${input}" is not a valid name""")
}


You can think of the String Or ErrorMessage result type like this: 

The parseName method will return a name String or, if the input string is not a valid name, an ErrorMessage. 

Heres how the parseAge method might be written: 
def parseAge(input: String): Int Or ErrorMessage = {
  try {
    val age = input.trim.toInt
    if (age >= 0) Good(age) else Bad(s""""${age}" is not a valid age""")
  }
  catch {
    case _: NumberFormatException => Bad(s"""${input}" is not a valid integer""")
  }
}

//"""

Given these implementations, heres how youd write the parsePerson method: 
def parsePerson(inputName: String, inputAge: String): Person Or ErrorMessage =
  for {
    name <- parseName(inputName)
    age <- parseAge(inputAge)
  } yield Person(name, age)


Because of Ors attitude, you need not write .good at the end of each generator. Or will keep going so long as each step produces a Good, short circuiting at the first sign of a Bad. Here are a few invocations of this parsePerson method: 
parsePerson("Bridget Jones", "29")
// Result: Good(Person(Bridget Jones,29))

parsePerson("Bridget Jones", "")
// Result: Bad("" is not a valid integer)

parsePerson("Bridget Jones", "-29")
// Result: Bad("-29" is not a valid age)

parsePerson("", "")
// Result: Bad("" is not a valid name)

 
Accumulating errors with Or

Another difference between Or and Either is that Or enables you to accumulate errors if the Bad type is an Every. An Every is similar to a Seq in that it contains ordered elements, but different from Seq in that it cannot be empty. An Every is either a One, which contains one and only one element, or a Many, which contains two or more elements. 

Note: an Or whose Bad type is an Every, or one of its subtypes, is called an “accumulating Or.” 

To rewrite the previous example so that errors can be accumulated, you need first to return an Every as the Bad type. Heres how youd change the parseName method: 
def parseName(input: String): String Or One[ErrorMessage] = {
  val trimmed = input.trim
  if (!trimmed.isEmpty) Good(trimmed) else Bad(One(s""""${input}" is not a valid name"""))
}


Because parseName will either return a valid name String wrapped in a Good, or one error message, wrapped in a Bad, you would write the Bad type as One[ErrorMessage]. The same is true for parseAge: 
def parseAge(input: String): Int Or One[ErrorMessage] = {
  try {
    val age = input.trim.toInt
    if (age >= 0) Good(age) else Bad(One(s""""${age}" is not a valid age"""))
  }
  catch {
    case _: NumberFormatException => Bad(One(s""""${input}" is not a valid integer"""))
  }
}


Because a for expression short-circuits on the first Bad encountered, youll need to use a different approach to write the parsePerson method. In this example, the withGood method from trait Accumulation will do the trick: 
import Accumulation._

def parsePerson(inputName: String, inputAge: String): Person Or Every[ErrorMessage] = {
  val name = parseName(inputName)
  val age = parseAge(inputAge)
  withGood(name, age) { Person(_, _) }
}


Trait Accumulation offers overloaded withGood methods that take 1 to 22 accumulating Ors, plus a function taking the same number of corresponding Good values. In this example, if both name and age are Goods, the withGood method will pass the good name String and age Int to the Person(_, _) function, and return the resulting Person object wrapped in a Good. If either name and age, or both, are Bad, withGood will return the accumulated errors in a Bad. 

The result of parsePerson, if Bad, will therefore contain either one or two error messages, i.e., the result will either be a One or a Many. As a result, the result type of parsePerson must be Person Or Every[ErrorMessage]. Regardless of whether a Bad result contains one or two error messages, it will contain every error message. Heres some invocations of this accumulating version of parsePerson: 
parsePerson("Bridget Jones", "29")
// Result: Good(Person(Bridget Jones,29))

parsePerson("Bridget Jones", "")
// Result: Bad(One("" is not a valid integer))

parsePerson("Bridget Jones", "-29")
// Result: Bad(One("-29" is not a valid age))

parsePerson("", "")
// Result: Bad(Many("" is not a valid name, "" is not a valid integer))


Note that in the last example, the Bad contains an error message for both name and age. 

Working with Everys

The previous examples demonstrate constructing a one-element Every with a factory method in the One companion object. You can similarly create an Every that contains more than one using a Many factory method. Here are some examples: 
One(1)
Many(1, 3)
Many(1, 2, 3)


You can also construct an Every by passing one or more elements to the Every.apply factory method: 
Every(1)
Every(1, 2)
Every(1, 2, 3)


Every does not extend Scalas Seq or Traversable traits because these require that implementations may be empty. For example, if you invoke tail on a Seq that contains just one element, youll get an empty Seq: 
scala> List(1).tail
res6: List[Int] = List()


On the other hand, many useful methods exist on Seq that when invoked on a non-empty Seq are guaranteed to not result in an empty Seq. For convenience, Every defines a method corresponding to every such Seq method. Here are some examples: 
Many(1, 2, 3).map(_ + 1)                  // Result: Many(2, 3, 4)
One(1).map(_ + 1)                         // Result: One(2)
Every(1, 2, 3).containsSlice(Every(2, 3)) // Result: true
Every(1, 2, 3).containsSlice(Every(3, 4)) // Result: false
Every(-1, -2, 3, 4, 5).minBy(_.abs)       // Result: -1


Every does not currently define any methods corresponding to Seq methods that could result in an empty Seq. However, an implicit converison from Every to collection.immutable.IndexedSeq is defined in the Every companion object that will be applied if you attempt to call one of the missing methods. As a result, you can invoke filter on an Every, even though filter could result in an empty sequence—but the result type will be collection.immutable.IndexedSeq instead of Every: 
Every(1, 2, 3).filter(_ < 10) // Result: Vector(1, 2, 3)
Every(1, 2, 3).filter(_ > 10) // Result: Vector()


You can use Everys in for expressions. The result will be an Every unless you use a filter (an if clause). Because filters are desugared to invocations of filter, the result type will switch to a collection.immutable.IndexedSeq at that point. Here are some examples: 
scala> import org.scalactic._
import org.scalactic._

scala> for (i <- Every(1, 2, 3)) yield i + 1
res0: org.scalactic.Every[Int] = Many(2, 3, 4)

scala> for (i <- Every(1, 2, 3) if i < 10) yield i + 1
res1: scala.collection.immutable.IndexedSeq[Int] = Vector(2, 3, 4)

scala> for {
     |   i <- Every(1, 2, 3)
     |   j <- Every(a, b, c)
     | } yield (i, j)
res3: org.scalactic.Every[(Int, Char)] =
        Many((1,a), (1,b), (1,c), (2,a), (2,b), (2,c), (3,a), (3,b), (3,c))

scala> for {
     |   i <- Every(1, 2, 3) if i < 10
     |   j <- Every(a, b, c)
     | } yield (i, j)
res6: scala.collection.immutable.IndexedSeq[(Int, Char)] =
        Vector((1,a), (1,b), (1,c), (2,a), (2,b), (2,c), (3,a), (3,b), (3,c))


Other ways to accumulate errors

The Accumlation trait also enables other ways of accumulating errors. 
 
Using combined

If you have a collection of accumulating Ors, for example, you can combine them into one Or using combined, like this: 
List(parseAge("29"), parseAge("30"), parseAge("31")).combined
// Result: Good(List(29, 30, 31))

List(parseAge("29"), parseAge("-30"), parseAge("31")).combined
// Result: Bad(One("-30" is not a valid age))

List(parseAge("29"), parseAge("-30"), parseAge("-31")).combined
// Result: Bad(Many("-30" is not a valid age, "-31" is not a valid age))

 
Using validatedBy

Or if you have a collection of values and a function that transforms that type of value into an accumulating Ors, you can validate the values using the function using validatedBy, like this: 
List("29", "30", "31").validatedBy(parseAge)
// Result: Good(List(29, 30, 31))

List("29", "-30", "31").validatedBy(parseAge)
// Result: Bad(One("-30" is not a valid age))

List("29", "-30", "-31").validatedBy(parseAge)
// Result: Bad(Many("-30" is not a valid age, "-31" is not a valid age))

 
Using zip

You can also zip two accumulating Ors together. If both are Good, youll get a Good tuple containin both original Good values. Otherwise, youll get a Bad containing every error message. Here are some examples: 
parseName("Dude") zip parseAge("21")
// Result: Good((Dude,21))

parseName("Dude") zip parseAge("-21")
// Result: Bad(One("-21" is not a valid age))

parseName("") zip parseAge("-21")
// Result: Bad(Many("" is not a valid name, "-21" is not a valid age))

 
Using when

In addition, given an accumlating Or, you can pass one or more validation functions to when on the Or to submit that Or to further scrutiny. A validation function accepts a Good type and returns a Validation[E], where E is the type in the Every in the Bad type. For an Int Or One[ErrorMessage], for example the validation function type would be Int => Validation[ErrorMessage]. Here are a few examples: 
def isRound(i: Int): Validation[ErrorMessage] =
    if (i % 10 == 0) Pass else Fail(i + " was not a round number")

def isDivBy3(i: Int): Validation[ErrorMessage] =
  if (i % 3 == 0) Pass else Fail(i + " was not divisible by 3")


If the Or on which you call when is already Bad, you get the same (Bad) Or back, because no Good value exists to pass to the valiation functions: 
parseAge("-30").when(isRound, isDivBy3)
// Result: Bad(One("-30" is not a valid age))


If the Or on which you call when is Good, and also passes all the validation functions (i.e., the all return None), you again get the same Or back, but this time, a Good one: 
parseAge("30").when(isRound, isDivBy3)
// Result: Good(30)


If one or more of the validation functions fails, however, youll get a Bad back contining every error. Here are some examples: 
parseAge("33").when(isRound, isDivBy3)
// Result: Bad(One(33 was not a round number))

parseAge("20").when(isRound, isDivBy3)
// Result: Bad(One(20 was not divisible by 3))

parseAge("31").when(isRound, isDivBy3)
// Result: Bad(Many(31 was not a round number, 31 was not divisible by 3))


Note that you can use when to accumulate errors in a for expression involving an accumulating Or, like this: 
for (age <- parseAge("-30") when (isRound, isDivBy3)) yield age
// Result: Bad(One("-30" is not a valid age))

for (age <- parseAge("30") when (isRound, isDivBy3)) yield age
// Result: Good(30)

for (age <- parseAge("33") when (isRound, isDivBy3)) yield age
// Result: Bad(One(33 was not a round number))

for (age <- parseAge("20") when (isRound, isDivBy3)) yield age
// Result: Bad(One(20 was not divisible by 3))

for (age <- parseAge("31") when (isRound, isDivBy3)) yield age
// Result: Bad(Many(31 was not a round number, 31 was not divisible by 3))

 
Much ado about Nothing

Because Or has two types, but each of its two subtypes only takes a value of one or the other type, the Scala compiler will infer Nothing for the unspecified type: 
scala> Good(3)
res0: org.scalactic.Good[Int,Nothing] = Good(3)

scala> Bad("oops")
res1: org.scalactic.Bad[Nothing,String] = Bad(oops)


Often Nothing will work fine, as it will be widened as soon as the compiler encounters a more specific type. Sometimes, however, you may need to specify it. In such situations you can use this syntax: 
scala> Good(3).orBad[String]
res2: org.scalactic.Good[Int,String] = Good(3)

scala> Good[Int].orBad("oops")
res3: org.scalactic.Bad[Int,String] = Bad(oops)


If you want to specify both types, because you dont like the inferred type, you can do so like this: 
scala> Good[AnyVal, String](3)
res4: org.scalactic.Good[AnyVal,String] = Good(3)

scala> Bad[Int, ErrorMessage]("oops")
res5: org.scalactic.Bad[Int,org.scalactic.ErrorMessage] = Bad(oops)


But you may find the code is clearer if you instead use a type ascription, like this: 
scala> Good(3): AnyVal Or String
res6: org.scalactic.Or[AnyVal,String] = Good(3)

scala> Bad("oops"): Int Or ErrorMessage
res7: org.scalactic.Or[Int,org.scalactic.ErrorMessage] = Bad(oops)

Requirements

The Requirements trait that contains require, and requireState, and requireNonNull methods for checking pre-conditions that give descriptive error messages extracted via a macro.

These methods of trait Requirements aim to improve error messages provided when a pre-condition check fails at runtime in production code. Although it is recommended practice to supply helpful error messages when doing pre-condition checks, often people dont. Instead of this:
scala> val length = 5
length: Int = 5

scala> val idx = 6
idx: Int = 6

scala> require(idx >= 0 && idx <= length, "index, " + idx + ", was less than zero or greater than or equal to length, " + length)
java.lang.IllegalArgumentException: requirement failed: index, 6, was less than zero or greater than or equal to length, 5
	at scala.Predef$.require(Predef.scala:233)
	...


People write simply:

scala> require(idx >= 0 && idx <= length)
java.lang.IllegalArgumentException: requirement failed
	at scala.Predef$.require(Predef.scala:221)
	...

Snapshots

The Snapshots trait provides a snap method that takes one or more arguments and results in a SnapshotSeq, whose toString lists the names and values of each argument.

The intended use case of this trait is to help you write debug and log messages that give a "snapshot" of program state. Heres an example:

scala> import Snapshots._
import Snapshots._

scala> snap(a, b, c, d, e, f)
res3: org.scalactic.SnapshotSeq = a was 1, b was 2, c was 3, d was 4, e was null, f was null





SnapshotSeq offers a lines method that places each variable name/value pair on its own line:

scala> snap(a, b, c, d, e, f).lines
res4: String =
a was 1
b was 2
c was 3
d was 4
e was null
f was null



Or, because a SnapshotSeq is a IndexedSeq[Snapshot], you can process it just like any other Seq, for example:

scala> snap(a, b, c, d, e, f).mkString("Wow! ", ", and ", ". Thats so awesome!")
res6: String = Wow! a was 1, and b was 2, and c was 3, and d was 4, and e was null, and f was null. Thats so awesome!

TimesOnInt

The TimesOnInt trait providing an implicit conversion that adds a times method to Ints that will repeat a given side-effecting operation multiple times. 

Heres an example in which a friendly greeting is printed three times: 
3 times println("Hello again, world!")


Running the above code would yield this output: 
Hello again, world!
Hello again, world!
Hello again, world!


If you need to repeat a block of statements multiple times, just enclose them in parentheses, like this: 
2 times {
  print("Hello ")
  print("again, ")
  println("world!")
}


Running the above code would yield: 
Hello again, world!
Hello again, world!


This trait enables times to be invoked on 0 and any positive integer, but attempting to invoke times on a negative integer will result in an IllegalArgumentException. 
