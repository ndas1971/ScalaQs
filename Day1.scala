"""
DAY-1
    Quick SBT 
    Scala Basic (mutability, immutability, operator)
    Scala Imperative 
    Scala collection intoduction 
    Scala  Object oriented with scalatest an sbt 
    Case Class and Pattern Matching
"""

///***Quick SBT 
0.Install JDK8 and set JAVA_HOME, then check 
$ java -version    //Make sure you have version 1.8.
1.Download https://github.com/sbt/sbt/releases/download/v1.1.1/sbt-1.1.1.zip
  Unzip and put into some dir and update PATH to include that <<install_dir>>\bin;
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




//File: build.sbt 
name := "Experiment"

version := "1.0"

scalaVersion := "2.11.12"

val akkaVersion = "2.5.9"

libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-actor" % akkaVersion,
     "com.typesafe.akka" %% "akka-remote" % akkaVersion
  )

libraryDependencies ++= Seq(
      "org.apache.avro" % "avro" % "1.7.7",
      "org.apache.kafka" % "kafka_2.11" % "0.10.0.0",
     // "io.confluent" % "kafka-avro-serializer" % "3.0.0",
     // "ch.qos.logback" %  "logback-classic" % "1.1.7"    //multiple slf4j binding 
)

libraryDependencies ++= Seq(
  "org.specs2" %% "specs2-core" % "4.0.2" ,
  "org.scalatest" %% "scalatest" % "3.0.0" 
)

//compile 
$ sbt compile
$ sbt testCompile 
$ sbt test          // in windows, gives correct display!!!
$ sbt "run arg1 arg2"    //for running main class (only one)
$ sbt package   //packaging jar
$ sbt console   //scala REPL
$ sbt "runMain examples.Foo arg1 arg2" //when many classes are present 

//Note files under src/main/resources are put inside Jar on root dir, hence goes to application classpath 
//Note sbt can compile src/main/scala, src/main/java and src/test/scala and src/test/java 


//in build.sbt, when many main classes are present, 
//to make one class as in 'sbt run' or in main class of Jar 
mainClass in (Compile, run) := Some("examples.Foo")
mainClass in (Compile, packageBin) := Some("examples.Foo")

//To include few unmanaged Jar in compile and to include few files during Runtime 
//Add different dirs for different "sbt command", Note command is Capitalized 
//check from sbt shell, > show <<project>>:unmanagedClasspath
unmanagedClasspath in Compile += baseDirectory.value / "lib"
unmanagedClasspath in Test += baseDirectory.value / "special-resources"
unmanagedClasspath in Runtime += baseDirectory.value / "config"
unmanagedClasspath in (Compile, runMain) += baseDirectory.value / "special-resources"

//In scala REPL to execute external scala file 
:paste /path/to/file



//*** Execution of code - Various styles

// script way- Scala installation is required - https://www.scala-lang.org/download/2.11.12.html
$ scala style1.scala I am Good
//file:style1.scala
args foreach println


//file:style2.scala - can be via sbt, for scalac, scala installation is required 
//scalac -d ./classes style2.scala
//scala -cp "./classes" basic.Main I am good

package basic  

object Main extends App{
	args foreach println
}

//file:style3.scala
//scalac -d ./classes style3.scala
//scala -cp "./classes" basic.Main2 I am good
//OR
//java -cp "c:/scala/lib/*;./classes" basic.Main2 I am good

package basic  

object Main2  {         			 //object is must else methods would not be static
	def main(args: Array[String]) {  //Note that args has to be Array[String], not String* which produces Seq<String>
		args foreach println
	}
 }


///*** Scala Basic (mutability, immutability, operator)
//Collections have two varieties - immutable and mutable

scala> val a = 1
a: Int = 1

scala> var b = 1
b: Int = 1

//final modifier
//In class , final val or final def can not be overridden
//final var is just like var
//final val is treated as compile time constant expression, hence inlined

//class can be final , means class can not be inherited
//class can be 'sealed' in a file, means only class inside that file can inherit 'sealed' class but not from outside

//val means that variable may not change, but not content
class Foo(s:String) { var thisIsMutable=s }
// you can then do this
val x = new Foo("hello")
x.thisIsMutable="goodbye"
// note that val guarantees that x is still the same instance of Foo
// reassigning x = new Foo("goodbye") would be illegal




///*** Quick reflections  

//runtime class of a type(JVM compatible)- reifiable type and  type erased class
//example- java - String.class - reifiable type ie runtime has type info 

scala> classOf[Array[Int]]
res0: Class[Array[Int]] = class [I

scala> classOf[Int]
res1: Class[Int] = int

//For all container class, ie C[T](except Array[T]) ,type is erased in runtime 
scala> classOf[List[Int]]
res2: Class[List[Int]] = class scala.collection.immutable.List
//for user defined Complex 
scala> classOf[Complex[Int]]
res6: Class[Complex[Int]] = class Complex

scala> classOf[List[Int]].getMethods

//To get class from instance - use getClass
scala> val a = 2
a: Int = 2

scala> a.getClass
res5: Class[Int] = int

///* scala type system(Used for compilation)
//to get type from type Name
scala> scala.reflect.runtime.universe.typeOf[List[Int]]
res4: reflect.runtime.universe.Type = scala.List[Int]

scala.reflect.runtime.universe.typeOf[List[Int]].baseClasses
scala.reflect.runtime.universe.typeOf[List[Int]].members 
//Scala Type 
scala.Any , abstract  class  Any ( != , ==, toString, ##, asInstanceOf[], isInstanceOf[], equals, hashCode)
	Nothing  (type represents nothing, no instance)
	scala.AnyRef (java.lang.Object) (overrides Any methods as final, eq , ne , notify, notifyAll, synchronized, wait)
		java.lang.String
		Array
		java.lang.Number
			java.lang.Integer
			....
		Other java classes
		scala.Seq
			...Collections
		scala.Option
		... other scala classes
		Null (with instance null )
	scala.AnyVal , only method is getClass()
		scala.Int
		scala.Double
		scala.Float
		scala.Unit
		scala.Char
		scala.Long
		scala.Byte
		scala.Boolean
		



//To get type from instance - use TypeTag
import scala.reflect.runtime.universe._
def getType[T:TypeTag](obj: T) = typeTag[T].tpe

scala> getType(a)
res6: reflect.runtime.universe.Type = Int

//In scala casting is done by instance.asInstanceOf[T]
//Checking is done by instance.isInstanceOf[T]
//In java generic casting (T) not possible, 
//in scala generic casting asInstanceOf[T] is possible, but it may fail at runtime 
scala> val a = 2.0
a: Double = 2.0

scala> a.isInstanceOf[Double]
res9: Boolean = true

//How to use type info at runtime for reifiable type(type system available at runtime)
// use ClassTag (Manifest is deprecated)

//Note new List[T]() would fail as List is abstract class 
def f[T] = List[T]()   //f: [T]=> List[T], ok as runtime has type erasure for List

def f[T] = Array[T]()  //But not possible for Array as runtime includes Array's T

//To fix use, ClassTag
def f[T:scala.reflect.ClassTag] = Array[T]()  //f: [T](implicit evidence$1: scala.reflect.ClassTag[T])Array[T]


//Another example for creating - new T()
def create[T] = new T() //error
                           ^
def create[T:scala.reflect.ClassTag] = scala.reflect.classTag[T].runtimeClass.newInstance
//create: [T](implicit evidence$1: scala.reflect.ClassTag[T])Any
def create[T:scala.reflect.ClassTag]:T = scala.reflect.classTag[T].runtimeClass.newInstance.asInstanceOf[T]
//create: [T](implicit evidence$1: scala.reflect.ClassTag[T])T

//But T must have default constructors else use of getDeclaredConstructors - complex
scala> class A
defined class A

scala> create[A]
res9: Any = A@70331432

//Alternate way
def make(x:Class[_]) = x.newInstance()  //make: (x: Class[_])Any
make(classOf[Integer])  // error as no default constructor

scala> make(classOf[A])
res8: Any = A@3cae7b8b


///*** Scala comparison 
1. ==, != is used for content checking
   == calls def equals(other:Any):Boolean method internally. 
  Overload this if required
2. eq, ne is used for object identity

//Note bydefault == is implemented by all scala class(comes from Any, default is compare addres, like eq)
//but not <, > etc
scala> List(1,2) == List(1,2)
res2: Boolean = true



		
///*** Operators
//Assignment operator (=) is a reserved word, not a method
//reserved word are   _   :   =   =>   <-   <:   <%   >:   #   @


//A prefix operation
op e   	Translates to e.unary_op
        op can be +, -, !, ~ 

//A postfix operator 
import   scala.language.postfixOps 
e op ;	//Translates to e.op    //; is required 
        //op can be any identifier

//Infix Operators 
e op a  Translates to e.op(a)
        op can be any combinations of special characters

//prefix operator is highest priority
//then infix operators
//last postfix 
//E.g. e1 op1 e2 op2 is always equivalent to (e1 op1 e2) op2

//e op (e1, . . . ,en). 
//This expression is then interpreted as e.op(e1, . . . ,en).


//Associativity - If last character is :, it is right associative, else left associative
a + b   is equivalent to a.+(b)
a +: b  is equivalent to b.+:(a)

//In a statement, all infix operators with SAME PRECEDENCE must have same associativity, 
//but for different precedence, associativity may vary

//Precedence is decided by first character of operator sequence 
"""
simple assignment =  
and assignments operators, += etc  have lowest precedence

First character
(all letters)						lowest
|
^
&
= !									==, != in this category
< >									<= ,>= in this category
:									
+ -									+: is higher than :+, hence 1 +: a :+ 2 === ((1 +: a) :+ 2)
* / %
(all other special characters)		highest
"""
//Note that based on above, == is lower precedence than <
val what = 5 == 8 < 4   // 5 == (8 < 4)

//Note that Assignments operators are expanded 
l += a  //is expanded to l = l + a (l is evaluated once)  
        //if l does not have += operator and l is mutable and has + operator


///*** If 
// 'if' is exactly like java if -  if (boolean) { } else { }
//Using the if Construct Like a Ternary Operator
//Both branch must have same type else return type would be Any
//

val absValue = if (a < 0) -a else a

if (true) "S"   //Return type Any

if (true) "S" else throw new Exception("Whoops!")   // Return type is String as else return type is Nothing

//Nothing -> [Int] -> ... -> AnyVal -> Any

//If no else, then put null (only for Reference type, for any other general type, throw new Exception) , such that return type comes from if block

if (true) "S" else null

//Note that Nothing does not have any instance , it is type (subtype of anything), hence can only be used in type eg List[Nothing] == Nil
//abstract final  class  Nothing extends Any 

//Null is type whose instance is null  , it is a subtype of Reference type
//abstract final  class  Null extends AnyRef 





///*** For loop

for (i <- 1 to 10) println(i)
for (i <- 1 until 10 ) println(i)
for (i <- 1 to 10 by 2 ) println(1)

//to, until are members of RichDouble etc 
//and BigInt, BigDecimal as well, but, must use with 'by' to convert to NumericRange
//1.0 until 10.0 by 0.5

//Check the code generated 
// scalac -Xprint:parse Main.scala

//Can use guard statement as well 

for {i <- 1 to 10  if i % 2 == 0
   x = i * i                     //no val, is deprecated
   j <- 1 to x    if j % 2 == 1
 }
{
  val y = i*i
  println(s"$y -> $j") 
 }




///*** While and do..while

var a = 10;

// while loop execution
while( a < 20 ){
         println( "Value of a: " + a )
         a = a + 1
}

	  
	  
//No Break in for and while loop
//Implementing break and continue by using module scala.util.control.Breaks._

import scala.util.control.Breaks._

 breakable {
        for (i <- 1 to 10) {
          println(i)
          if (i > 4) break // break out of the for loop
        }
      }


	  
	  


///*** Functions 
//Scripts can have independent functions, but otherwise, must be part of class
//in REPL, it's ok

//Nested function is possible, but function return a function must be through closure ie using _

//Function Return Type - No need to mention , Scala can infer it
//last line is the return type, don't mention return 
//Must mention 'return' when you use explicit return or it is recursive

def   modMethod(i:   Int) = i % 2 == 0
def   modMethod(i:   Int) = { i % 2 == 0 }
def   modMethod(i:   Int): Boolean = i % 2 == 0
def   modMethod(i:   Int) = { i % 2 == 0 }

//Type is Int => Boolean
modMethod _

///* Function - Parametrized function
def meth[T](x:T):T = x                   // Can only use 'Any' methods, ( != , ==, toString, ##, asInstanceOf[], isInstanceOf[], equals, hashCode)

def meth[T <:AnyRef](x:T) = x eq x       //now can use AnyRef(or java Object) methods , eq, ne ..

meth(1)
meth("Ok")




///*Function - Nested Functions 
def f(x:Int):Int = {
  def g(y:Int) = {
    x * y
  }
  g(100)
}
f(2)



///*** Call by name(defered call) , x: => Int , ie, x is Int when called   

//Creating Own Control Structures  
// two 'if' condition tests
def doubleif(test1: => Boolean)(test2: => Boolean)(codeBlock: => Unit) {
       if (test1 && test2) {
         codeBlock
       }
}
doubleif(age > 18)(numAccidents == 0) { println("Discount!") }

//Another example:
def whilst(testCondition: => Boolean)(codeBlock: => Unit) {
       while (testCondition) {
         codeBlock
       }
}

//final version
@scala.annotation.tailrec
def whilst(testCondition: => Boolean)(codeBlock: => Unit) {
           if (testCondition) {
             codeBlock
             whilst(testCondition)(codeBlock)
           }
}

var i = 0
whilst (i < 5) {
          println(i)
          i += 1
}



//Another Example: Benchmarking  Scala
def time[R](block: => R): R = {
    val t0 = System.nanoTime()
    val result = block    // call-by-name
    val t1 = System.nanoTime()
    println("Elapsed time: " + (t1 - t0) + "ns")
    result
}

// Now wrap your method calls, for example change this...
val result = 1 to 1000 sum

// ... into this
val result = time { 1 to 1000 sum }


///*** Creating Methods That Take Variable-Argument, 
//Last arg should be *
// version 1
def printAll(numbers: String*) {  //numbers becomes Seq<String>
        for (i <- numbers) println(i)
      }
      
def printAll[T](numbers: T*) {  //numbers becomes Seq<String>
        for (i <- numbers) println(i)
      }
//var args must be last and only one
def printAll(strings: String*, i: Int) //error

// these all work
printAll()
printAll("foo")
printAll("foo", "bar")
printAll("foo", "bar", "baz")


// Use : _* to adapt a sequence (Array, List, Seq, Vector, etc.)
// to unpack for sequence of strings

val fruits = List("apple", "banana", "cherry")

// pass the sequence to the varargs field
printAll(fruits: _*)

 



 
 
///*** Defining a Method That Returns Multiple Items ( use Tuples)

def getStockInfo = ("NFLX", 100.00)

val (symbol, currentPrice) = getStockInfo  //de-structuring of tupple happens only at 'val/var' definition not at 'var' reassignment

val result = getStockInfo
result._1
result._2

	  
///*** Setting Default Values for Method Parameters 
//use these parameters last

def makeConnection(timeout: Int = 5000, protocol: = "http") {
        println("timeout = %d, protocol = %s".format(timeout, protocol))
        
}



makeConnection()   //ending () is must
makeConnection(2000)
makeConnection(3000, "https")
 

makeConnection(timeout=10000)
makeConnection(protocol="https")
makeConnection(timeout=10000, protocol="https")  



///*** Usage of sys
 import sys._
 sys.error("OK")   //runtime exception
 sys.exit(1)
 sys.env     // prints env
 sys.props     // Mutable SystemProperties



///*** Predef scala functionality

//usage of ensuring, if condition fails, raises error
//Hence Any can be converted

1.ensuring { 1>0}    //OK , res31: Int = 1
{val x = 1; x } ensuring { 1 == 1 }  //res33: Int = 1

assert(cond)  // cond must be true
require(cond) // condition must be true
assume(cond)  // condition must be true
def ??? : Nothing = throw new NotImplementedError





///*** basic Types
//Int, Boolean, Double, Float, BigInt,BigDecimal, Byte, Char, Short, Long  and their Rich*
//conversion, toInt, toFloat

//Rich type has many methods(eg abs, longValue, isValidLong etc) , these are automatically called on base type

//Base classes
reflect.runtime.universe.typeOf[Int].baseClasses

//Int, RichInt Methods
reflect.runtime.universe.typeOf[Int].members.map(_.name.toString).toSet.toList.sorted
reflect.runtime.universe.typeOf[scala.runtime.RichInt].members.map(_.name.toString).toSet.toList.sorted

//Initialization
val bt:Byte = 0
val bt = 0 : Byte
val bt = 0.asInstanceOf[Byte]

//use d, f, L as suffix
BigInt("111111111111")

///* Reference 
final class RichInt extends AnyVal with ScalaNumberProxy[Int] with RangedProxy[Int] 
    def <=(that: Int): Boolean
    def >(that: Int): Boolean
    def >=(that: Int): Boolean
    def abs: Int
    def byteValue(): Byte
    def compare(y: Int): Int
    def compareTo(that: Int): Int
    def doubleValue(): Double
    def floatValue(): Float
    def getClass(): Class[_ <: AnyVal]
    def intValue(): Int
    def isValidByte: Boolean
    def isValidChar: Boolean
    def isValidInt: Boolean
    def isValidLong: Boolean
    def isValidShort: Boolean
    def isWhole(): Boolean
    def longValue(): Long
    def max(that: Int): Int
    def min(that: Int): Int
    def shortValue(): Short
    def signum: Int
    def to(end: Int, step: Int): Inclusive
    def to(end: Int): Inclusive
    def toBinaryString: String
    def toByte: Byte
    def toChar: Char
    def toDouble: Double
    def toFloat: Float
    def toHexString: String
    def toInt: Int
    def toLong: Long
    def toOctalString: String
    def toShort: Short
    def toString(): String
    def underlying(): AnyRef
    def until(end: Int, step: Int): collection.immutable.Range
    def until(end: Int): collection.immutable.Range 

//Random
reflect.runtime.universe.typeOf[scala.util.Random].members
val r = scala.util.Random
r.nextInt            //have nextDouble, nextLong ... 
r.nextInt(10)  //Int = 7
r.shuffle(List.range(1,5))


//Generic Numbering operations - it's challenging in scala (like Java)
//One option
val list:List[Number] = List(1,2,3.0, 4L)
list.map(_.doubleValue).sum

//Number is a Java class, and has no functionality aside from converting itself to primitive types. 
//Scala's AnyVal doesn't do anything aside from mark the primitive types, 
//and Numeric[T] from Scala only knows about its own type, not a mix of types
scala> def add[T:Numeric, U:Numeric](x:T, y:U):T =  implicitly[Numeric[T]].plus(x,y)
<console>:7: error: type mismatch;
 found   : y.type (with underlying type U)
 required: T
       def add[T:Numeric, U:Numeric](x:T, y:U):T =  implicitly[Numeric[T]].plus(x,y)

	   
//one solution is to use pattern matching;
//handling double and Int
def add[T <: Number, U <:Number](x:T, y:U):Double = (x:Any, y:Any) match {
  case (a:Int, b:Int) => a + b
  case _ => x.doubleValue() + y.doubleValue()
}

//Another example
val list:List[Number] = List(1,2,3.0, 4L)
list.reduceLeft((l,r) => (l: Any, r: Any) match {
  case (li: Int, ri: Int) => li + ri
  case _ => l.doubleValue + r.doubleValue
})

//casting to Any to get the Scala pattern match to do the unboxing 

   
	   
///*** Casting and asInstanceOf 
//Java (and Scala) allows you to cast a primitive double to int 
//(in Scala's case, Double to Int). 

//According the Scala Language Spec:
//The  x.asInstanceOf[T] is treated specially if T is a numeric value type  
//In this case the cast will be translated to an application of a conversion method x.toT 

//Also null behaves differently with AnyVal or AnyRef 
//even if  null can not be assigned to AnyVal as Null is from AnyRef
//But note var x:Any = null is possible, then x can take AnyVal or AnyRef

scala> null.asInstanceOf[Any]
res166: Any = null

scala> null.asInstanceOf[Int]
res167: Int = 0

scala> null.asInstanceOf[Double]
res169: Double = 0.0

scala> null.asInstanceOf[String]
res170: String = null


//But you cannot cast java.lang.Double to java.lang.Integer via asInstanceOf

//OR
//When you declare the Double as an Any, the compiler is storing the value as a boxed double 
//(i.e., java.lang.Double), then casting to Integer/Int fails

//Hence never ever store Double,Integer .. as Any 


scala> val l:Double = 10.0
l: Double = 10.0

scala> l.toInt
res11: Int = 10

scala> l.asInstanceOf[Int]
res12: Int = 10

scala> l.asInstanceOf[Any].asInstanceOf[Int]
java.lang.ClassCastException: java.lang.Double cannot be cast to java.lang.Integer

//type erasure must be considered with parameterized types.
//The expression List(3.14159).asInstanceOf[List[String]] will succeed.
//List(3.14159).isInstanceOf[List[String]] will return true


//in invocation and assignment context, below conversion is allowed 
""" 
1. a widening primitive conversion (Int -> Double, Char to Int etc..)
2. a widening reference conversion (derived to base)
3. a boxing conversion  optionally followed by widening reference conversion(Int->Integer->Number)
4. an unboxing conversion   (Integer->Int) , followed by no other conversion(eg Double/Long..)
5. A narrowing conversion from  constant expression of type Byte, Short, Char, Int 
to Byte, Char, Short if target type can hold the value without precision lost
"""

//widening
def f(x:java.lang.Double) = 2  //f: (x: Double)Int
val x:Int = 2  //x: Int = 2
f(x)  //res4: Int = 2

//specific narrowing
def f(x:Byte) = 2
f(2)      //OK
f(2786)  //Error
f(2L)    //Error
val x:Int = 2
f(x)    //Error




///*** Char and String and  interpolation
// Scala has Char type (java Character), eg 'A'

//Strings - " "  or multiline """ """
// raw string as raw"  "
// Interpolation
s"$v ${ v2.method }"
f"$v%2.2f ${v2.method}%2.2f"

//print method
println("...")  // only one arg, use + with atleast one String argument (even "") to concatenate many values. + in String context, converts all via toString
print("....")
printf("pattern string", v, v2, ...)

//Reading console
readLine  //returns a string, then use string.toInt etc for conversion
readInt // returns line as string
//Other read* methods
//above are deprecated, Use same named method in scala.io.StdIn
 
 

///*** String Methods - Immutable
//java.langString and enhanced with StringOps
//http://www.scala-lang.org/api/2.11.12/#package
reflect.runtime.universe.typeOf[String].members.map(_.name).toSet

//StringOps methods
reflect.runtime.universe.typeOf[collection.immutable.StringOps].members.map(_.name).toSet
//size, union, stripLineEnd, isEmpty, reduceRightOption,  
//contains, split, replace
//To get character str(index), length, size, > , < , 
//count(func), find(func), foreach, flatMap, map
// and all sequence operators
//conversion to other type  - toInt, toArray etc


var name = null.asInstanceOf[String]  //null String
val name:String = null

//accessing
"OK"(0)   //res19: Char = O
val a = "OK"  //a: String = OK
a(0) = 1  //<console>:10: error: value update is not a member of String
          

//splitting various ways- split takes regex
"""OK 
   OK
   OK""".split("\\n")   //res8: Array[String] = Array(OK, OK, OK)

"OKOK".split("")  //res10: Array[String] = Array(O, K, O, K)

"Ok Ok".toArray   //res22: Array[Char] = Array(O, k,  , O, k)

"OK OK".split("\\s+") //or use raw"\s+" , res11: Array[String] = Array(OK, OK)

//* Reference 
final class StringOps extends AnyVal with StringLike[String] 
    def *(n: Int): String
    def ++[B](that: GenTraversableOnce[B]): String[B]
    def ++:[B >: Char, That](that: collection.Traversable[B])(implicit bf: CanBuildFrom[String, B, That]): That
    def ++:[B](that: TraversableOnce[B]): String[B]
    def +:(elem: A): String[A]
    def /:[B](z: B)(op: (B, Char) => B): B
    def :+(elem: A): String[A]
    def :\[B](z: B)(op: (Char, B) => B): B
    def <(that: String): Boolean
    def <=(that: String): Boolean
    def >(that: String): Boolean
    def >=(that: String): Boolean
    def addString(b: StringBuilder): StringBuilder
    def addString(b: StringBuilder, sep: String): StringBuilder
    def addString(b: StringBuilder, start: String, sep: String, end: String): StringBuilder
    def aggregate[B](z: => B)(seqop: (B, Char) => B, combop: (B, B) => B): B
    def apply(index: Int): Char
    def canEqual(that: Any): Boolean
    def capitalize: String
    def charAt(arg0: Int): Char
    def codePointAt(arg0: Int): Int
    def codePointBefore(arg0: Int): Int
    def codePointCount(arg0: Int, arg1: Int): Int
    def collect[B](pf: PartialFunction[A, B]): String[B]
    def collectFirst[B](pf: PartialFunction[Char, B]): Option[B]
    def combinations(n: Int): Iterator[String]
    def compare(other: String): Int
    def compareTo(that: String): Int
    def compareToIgnoreCase(arg0: String): Int
    def concat(arg0: String): String
    def contains[A1 >: Char](elem: A1): Boolean
    def containsSlice[B](that: GenSeq[B]): Boolean
    def contentEquals(arg0: CharSequence): Boolean
    def contentEquals(arg0: StringBuffer): Boolean
    def copyToArray(xs: Array[A], start: Int, len: Int): Unit
    def copyToArray(xs: Array[A]): Unit
    def copyToArray(xs: Array[A], start: Int): Unit
    def copyToBuffer[B >: Char](dest: Buffer[B]): Unit
    def corresponds[B](that: GenSeq[B])(p: (Char, B) => Boolean): Boolean
    def count(p: (Char) => Boolean): Int
    def diff(that: collection.Seq[Char]): String[Char]
    def distinct: String
    def drop(n: Int): String
    def dropRight(n: Int): String
    def dropWhile(p: (Char) => Boolean): String
    def endsWith[B](that: GenSeq[B]): Boolean
    def equals(arg0: Any): Boolean
    def equalsIgnoreCase(arg0: String): Boolean
    def exists(p: (Char) => Boolean): Boolean
    def filter(p: (Char) => Boolean): String
    def filterNot(p: (Char) => Boolean): String
    def find(p: (Char) => Boolean): Option[Char]
    def flatMap[B](f: (A) => GenTraversableOnce[B]): String[B]
    def fold[A1 >: Char](z: A1)(op: (A1, A1) => A1): A1
    def foldLeft[B](z: B)(op: (B, Char) => B): B
    def foldRight[B](z: B)(op: (Char, B) => B): B
    def forall(p: (Char) => Boolean): Boolean
    def foreach(f: (A) => Unit): Unit
    def format(args: Any*): String
    def formatLocal(l: Locale, args: Any*): String
    def getBytes(): Array[Byte]
    def getBytes(arg0: Charset): Array[Byte]
    def getBytes(arg0: String): Array[Byte]
    def getChars(arg0: Int, arg1: Int, arg2: Array[Char], arg3: Int): Unit
    def getClass(): Class[_ <: AnyVal]
    def groupBy[K](f: (Char) => K): Map[K, String]
    def grouped(size: Int): Iterator[String]
    def hasDefiniteSize: Boolean
    def hashCode(): Int
    def head: Char
    def headOption: Option[Char]
    def indexOf(elem: Char, from: Int): Int
    def indexOf(elem: Char): Int
    def indexOfSlice[B >: Char](that: GenSeq[B], from: Int): Int
    def indexOfSlice[B >: Char](that: GenSeq[B]): Int
    def indexWhere(p: (Char) => Boolean, from: Int): Int
    def indexWhere(p: (Char) => Boolean): Int
    def indices: Range
    def init: String
    def inits: Iterator[String]
    def intern(): String
    def intersect(that: collection.Seq[Char]): String[Char]
    def isDefinedAt(idx: Int): Boolean
    def isEmpty: Boolean
    def iterator: Iterator[Char]
    def last: Char
    def lastIndexOf(elem: Char, end: Int): Int
    def lastIndexOf(elem: Char): Int
    def lastIndexOfSlice[B >: Char](that: GenSeq[B], end: Int): Int
    def lastIndexOfSlice[B >: Char](that: GenSeq[B]): Int
    def lastIndexWhere(p: (Char) => Boolean, end: Int): Int
    def lastIndexWhere(p: (Char) => Boolean): Int
    def lastOption: Option[Char]
    def length: Int
    def lengthCompare(len: Int): Int
    def lines: Iterator[String]
    def linesWithSeparators: Iterator[String]
    def map[B](f: (A) => B): String[B]
    def matches(arg0: String): Boolean
    def max: A
    def maxBy[B](f: (A) => B): A
    def min: A
    def minBy[B](f: (A) => B): A
    def mkString: String
    def mkString(sep: String): String
    def mkString(start: String, sep: String, end: String): String
    def nonEmpty: Boolean
    def offsetByCodePoints(arg0: Int, arg1: Int): Int
    def padTo(len: Int, elem: A): String[A]
    def par: ParSeq[Char]
    def partition(p: (Char) => Boolean): (String, String)
    def patch(from: Int, that: GenSeq[A], replaced: Int): String[A]
    def permutations: Iterator[String]
    def prefixLength(p: (Char) => Boolean): Int
    def product: A
    def r(groupNames: String*): Regex
    def r: Regex
    def reduce[A1 >: Char](op: (A1, A1) => A1): A1
    def reduceLeft[B >: Char](op: (B, Char) => B): B
    def reduceLeftOption[B >: Char](op: (B, Char) => B): Option[B]
    def reduceOption[A1 >: Char](op: (A1, A1) => A1): Option[A1]
    def reduceRight[B >: Char](op: (Char, B) => B): B
    def reduceRightOption[B >: Char](op: (Char, B) => B): Option[B]
    def regionMatches(arg0: Boolean, arg1: Int, arg2: String, arg3: Int, arg4: Int): Boolean
    def regionMatches(arg0: Int, arg1: String, arg2: Int, arg3: Int): Boolean
    def replace(arg0: CharSequence, arg1: CharSequence): String
    def replace(arg0: Char, arg1: Char): String
    def replaceAll(arg0: String, arg1: String): String
    def replaceAllLiterally(literal: String, replacement: String): String
    def replaceFirst(arg0: String, arg1: String): String
    def reverse: String
    def reverseIterator: Iterator[Char]
    def reverseMap[B](f: (A) => B): String[B]
    def sameElements(that: GenIterable[A]): Boolean
    def scan[B >: Char, That](z: B)(op: (B, B) => B)(implicit cbf: CanBuildFrom[String, B, That]): That
    def scanLeft[B, That](z: B)(op: (B, Char) => B)(implicit bf: CanBuildFrom[String, B, That]): That
    def scanRight[B, That](z: B)(op: (Char, B) => B)(implicit bf: CanBuildFrom[String, B, That]): That
    def segmentLength(p: (Char) => Boolean, from: Int): Int
    def seq: WrappedString
    def size: Int
    def slice(from: Int, until: Int): String
    def sliding(size: Int, step: Int): Iterator[String]
    def sliding(size: Int): Iterator[String]
    def sortBy[B](f: (Char) => B)(implicit ord: math.Ordering[B]): String
    def sortWith(lt: (Char, Char) => Boolean): String
    def sorted[B >: Char](implicit ord: math.Ordering[B]): String
    def span(p: (Char) => Boolean): (String, String)
    def split(separators: Array[Char]): Array[String]
    def split(separator: Char): Array[String]
    def splitAt(n: Int): (String, String)
    def startsWith[B](that: GenSeq[B], offset: Int): Boolean
    def startsWith[B](that: GenSeq[B]): Boolean
    def stripLineEnd: String
    def stripMargin: String
    def stripMargin(marginChar: Char): String
    def stripPrefix(prefix: String): String
    def stripSuffix(suffix: String): String
    def subSequence(arg0: Int, arg1: Int): CharSequence
    def substring(arg0: Int, arg1: Int): String
    def substring(arg0: Int): String
    def sum: A
    def tail: String
    def tails: Iterator[String]
    def take(n: Int): String
    def takeRight(n: Int): String
    def takeWhile(p: (Char) => Boolean): String
    def to[Col[_]]: Col[A]
    def toArray: Array[A]
    def toBoolean: Boolean
    def toBuffer[A1 >: Char]: Buffer[A1]
    def toByte: Byte
    def toCharArray(): Array[Char]
    def toDouble: Double
    def toFloat: Float
    def toIndexedSeq: IndexedSeq[Char]
    def toInt: Int
    def toIterable: collection.Iterable[Char]
    def toIterator: Iterator[Char]
    def toList: scala.List[Char]
    def toLong: Long
    def toLowerCase(): String
    def toLowerCase(arg0: Locale): String
    def toMap[T, U]: collection.Map[T, U]
    def toParArray: ParArray[T]
    def toSeq: collection.Seq[Char]
    def toSet[B >: Char]: Set[B]
    def toShort: Short
    def toStream: Stream[Char]
    def toString(): String
    def toTraversable: collection.Traversable[Char]
    def toUpperCase(): String
    def toUpperCase(arg0: Locale): String
    def toVector: scala.Vector[Char]
    def trim(): String
    def union(that: collection.Seq[Char]): String[Char]
    def updated(index: Int, elem: A): String[A]
    def view(from: Int, until: Int): SeqView[Char, String]
    def view: SeqView[Char, String]
    def withFilter(p: (Char) => Boolean): FilterMonadic[Char, String]
    def zip[B](that: GenIterable[B]): String[(A, B)]
    def zipAll[B](that: collection.Iterable[B], thisElem: A, thatElem: B): String[(A, B)]
    def zipWithIndex: String[(A, Int)]


///*Mutable String
//genally single operation like + for one element 
//for multiple operations like ++, for another Collection here String 
val buf = new StringBuilder
buf += 'a'     //can add only char
buf ++= "bcdef" // or use ++= for String
buf.toString

//Methods extra in StringBuilder compared to String + StringOps
Set(length_=, flatten, deleteCharAt, compose, insert, unzip3, 
clear, +=, unzip, result, setCharAt, andThen, reverseContents, 
orElse, insertAll, ensureCapacity, sizeHint, companion, mapResult, 
appendAll, delete, sizeHintBounded, underlying, genericBuilder, 
runWith, "underlying ", transpose, update, setLength, transform, 
++=, lift, applyOrElse, capacity, append)


///*** Regex methods - use raw"\s+".r or "\\s+".r

reflect.runtime.universe.typeOf[scala.util.matching.Regex].members

//Usage 
//use regex.findAllIn(String)(returns MatchInterator), regex.findFirstIn(String)

//regex.replaceAllin(String, replace), regex.replaceFirstIn(String, replace)
//String.replaceAll(regex, replace)

//String.split(regex)

//There are variants eg findAllMatchIn, 
//which returns Option[Match] object 

val numPattern = raw"[0-9]+".r  //scala.util.matching.Regex = [0-9]+
val address = "123 Main Street Suite 101"

val matches = numPattern.findAllIn(address).toArray
val result = numPattern.findFirstIn(address).getOrElse("no match")

val addressN = "123 Main Street".replaceAll(raw"[0-9]", "x")
val newAddress = numPattern.replaceAllIn("123 Main Street", "x")

val words = "hello world, this is Al".split("\\s+")

//With Pattern match  
val pattern = raw"([0-9]+) ([A-Za-z]+)".r
val pattern(count, fruit) = "100 Bananas"
//OR 
"100 Bananas" match {
   case pattern(count, fruit) => 
   case _  =>
 }
 
//Another Examples 
val date = """(\d\d\d\d)-(\d\d)-(\d\d)""".r

//To extract the capturing groups , use it as an extractor in a pattern match:
"2004-01-20" match {
  case date(year, month, day) => s"$year was a good year for PLs."
}

//To check only whether the Regex matches, 
"2004-01-20" match {
  case date(_*) => "It's a date!"
}
//OR
"2004-01-20" match {
  case date(year, _*) => s"$year was a good year for PLs."
}
//In a pattern match, Regex normally matches the entire input. 
//Use unanchored , finds the pattern anywhere in the input.
val embeddedDate = date.unanchored
"Date: 2004-01-20 17:25:18 GMT (10 years, 28 weeks, 5 days, 17 hours and 51 minutes ago)" match {
  case embeddedDate("2004", "01", "20") => "A Scala is born."
}

//To find or replace matches of the pattern, 
//Use various methods, 
//eg one  produces matched strings and another that produces Match objects.
val dates = "Important dates in history: 2004-01-20, 1958-09-05, 2010-10-06, 2011-07-15"
val firstDate = date findFirstIn dates getOrElse "No date found."
val m = date findFirstMatchIn dates //Option[scala.util.matching.Regex.Match] = Some(2004-01-20)
//Match.group(0) - entire match , goup(1) first group etc 
val firstYear = for (m <- date findFirstMatchIn dates) yield m group 1

//To find all matches:
val allYears = for (m <- date findAllMatchIn dates) yield m group 1

//findAllIn returns a special iterator of strings that can be queried for the MatchData of the last match:
val mi = date findAllIn dates
val oldies = mi filter (_ => (mi group 1).toInt < 1960) map (s => s"$s: An oldie but goodie.")

//findAllIn finds matches that donot overlap. 
val num = """(\d+)""".r
val all = (num findAllIn "123").toList  // List("123"), not List("123", "23", "3")

//Text replacement can be performed unconditionally 
//or as a function of the current match:
val redacted    = date replaceAllIn (dates, "XXXX-XX-XX")
val yearsOnly   = date replaceAllIn (dates, m => m group 1)
val months      = (0 to 11) map { i => val c = Calendar.getInstance; c.set(2014, i, 1); f"$c%tb" }
val reformatted = date replaceAllIn (dates, _ match { case date(y,m,d) => f"${months(m.toInt - 1)} $d, $y" })


//Where potential matches overlap, 
//the first possible match is returned, 
//followed by the next match that follows the input consumed by the first match:
val hat  = "hat[^a]+".r
val hathaway = "hathatthattthatttt"
val hats = (hat findAllIn hathaway).toList                     // List(hath, hattth)
val pos  = (hat findAllMatchIn hathaway map (_.start)).toList  // List(0, 7)

//To return overlapping matches, 
//Use lookahead (?=) that does not consume the overlapping region.
val madhatter = "(h)(?=(at[^a]+))".r
val madhats   = (madhatter findAllMatchIn hathaway map {
  case madhatter(x,y) => s"$x$y"
}).toList                                       // List(hath, hatth, hattth, hatttt)



	  

///** Creating a Range and for loop

reflect.runtime.universe.typeOf[Range].members

//Example 
var r = 1 to 10   
r = 1 to 10 by 2
for (i <- 1 to 5) println(i)
for (i <- 1 until 5) println(i)
val x = 1 to 10 toArray
//Can be with Double  and BigInt, BigDecimal  as well, but use  by
val x =  1.0 until 10.2  by 0.2
val x = BigDecimal("1.0") until BigDecimal("10.2") by 0.2 map { _.toDouble } toList

//All above are eager calculations, to make it lazy , use view 
val x =  1.0 until 10.2  by 0.2 view 
//then use all FP tools and then force it 
x.force

 
///*** Mutable and Immutable Collections
//by default immutable, returns new collection after operation

val s = List(1,2,3)
s(0) = 2  //<console>:9: error: value update is not a member of List[Int]

val s = scala.collection.mutable.ListBuffer(1,2,3)
s(0) = 2
val s = scala.collection.mutable.Set(1,2,3)
s.add(20)

val s = scala.collection.mutable.Map(1->2)
s(2) = 3

// Note scala simulates a 'var' immutable collection as mutable one 
//ie c += 2 is simulated as c = c + 2
var s = List(1,2)
s ++= List(2,3)


///*** Creating  collection 
"""
where S is Array, List, Seq, Set,....
Note 'object' can not take T, hence if required, mention T as part of method 
ex :  val m = collection.mutable.ListBuffer.fill(256)( null :Any); m(0) = "OK" ..

Note Type parameter is must sometimes eg 
val a = List.empty[Int]

S.empty[T] 						The empty sequence
S(x, y, z) 						A sequence consisting of elements x, y, and z - variable args 
S.concat(xs, ys, zs) 			The sequence obtained by concatenating the elements of xs, ys, and zs - variable args 
S.fill[T](n)(e) 				A sequence of length n where each element is computed by expression e
S.fill[T](m, n)(e) 				A sequence of sequences of dimension m x n where each element is computed by expression e (exists also in higher dimensions)
S.tabulate(n)(f) 				A sequence of length n where the element at each index i is computed by f(i)
S.tabulate(m, n)(f) 			A sequence of sequences of dimension m x n	where the element at each index (i, j) is computed  by f(i, j) (exists also in higher dimensions)
S.range[T](start, end) 			The sequence of integers start : : : end 
S.range[T](start, end, step) 	The sequence of integers starting with start and progressing by step increments up to, and excluding, the end value
S.iterate(x, n)(f) 				The sequence of length n with elements x, f(x), f(f(x)),...
"""
//Example 
List.empty[Int]
List.fill(2)(3)
List.tabulate[Int](2)((i)=>3)


///*** Array  - java core Array 
//fixed size, mutable, All collection methods can be used via ArrayOps implicits 
//runtime keeps Type , hence no type erasure 

reflect.runtime.universe.typeOf[Array[Int]].members.toSet
reflect.runtime.universe.typeOf[collection.mutable.ArrayOps[Int]].members.toSet


//Usage - ++, :+, +: etc 
//genally single operation like + for one element 
//for multiple operations like ++, for another Collection 
// :+ where LHS is array , +: where RHS is array 

// Can use Array.empty, Array.fill etc 
val a = Array(1,2,3,4)
println(a.min)
a(0)  //res6: Int = 1
a(0) = 2
a contains 2  //res9: Boolean = true
Array.empty[Int]
Array.empty[Int] ++ Array.empty[Int]
Array.empty[Int] :+ 2
for(i <- a)println(i)


//Array Buffers - can grow in size by appending , but fast like Array
//operationWith suffix = for updating 
val buf = scala.collection.mutable.ArrayBuffer.empty[Int]
buf += 1
buf += 10
buf.toArray


///*** Tuple - mixed type, immutable 
//For operation like Dynamic types, use Tuple or case class 
//Tuple2(x,y) or x -> y or (x,y)
//Tuple3(x,y,z) or (x,y,z) ... so on till 22 

val x = (1,2)
val x = 1 -> 2
x._1
x._2

//pattern matching 
val Tuple2(x,y) = (1,2)
//or
val (x,y) = (1,2)

//Reference 
case class Tuple2[+T1, +T2](_1: T1, _2: T2) extends Product2[T1, T2] with Product with Serializable 
    val _1: T1
    val _2:T2
    def invert[El1, CC1[X] <: TraversableOnce[X], El2, CC2[X] <: TraversableOnce[X], That](implicit w1: <:<[T1, CC1[El1]], w2: <:<[T2, CC2[El2]], bf: CanBuildFrom[CC1[_], (El1, El2), That]): That
    def productArity: Int
    def productElement(n: Int): Any
    def swap: (T2, T1)
    def toString(): String
    def zipped[El1, Repr1, El2, Repr2](implicit w1: (T1) => TraversableLike[El1, Repr1], w2: (T2) => IterableLike[El2, Repr2]): Tuple2Zipped[El1, Repr1, El2, Repr2] 
    

            
			  

///*** List - List and collection.mutable.ListBuffer
//Insertion ordered, Indexing with (Int),
reflect.runtime.universe.typeOf[List[Int]].members.toSet

//Main Methods, slice(start,until) ,contains, 
// isEmpty,  List[Int](),   ++, +:, :+, ::, ++:
//Mutable methods  +=, ++=, -=, --=
//Conversion  toList, toMap, toSet, toArray

//genally single operation like + for one element 
//for multiple operations like ++, for another Collection 
// :+ where LHS is List , +: where RHS is List  


//shift -  stripFront
//unshift(prepend) - +:
//pop - stripEnd
//push - append or :+


val L = List(1,2,3)
val L = 1 :: 2 :: Nil
val L2 = 1 +: L :+ List(55,66)
L ++ List(2,3)
val L2 = 1 +: L :+ 55

val L = scala.collection.mutable.ListBuffer[Int]()
L += 2
L(0)
L(0) = 3
L contains 3

val l = 1 :: Nil
l(0)
l.head
l.tail
List(1,2,3,4).slice(1,3)  //no step 

//Append 
val L = scala.collection.mutable.ListBuffer[Int]()
L += 2
L ++= List(2)
L.append(2)

//Iterating 
val a = List((1,2,5),(2,3,4))
for( (i,j,k) <- a) println(s"$i,$j,$k")

val a = List(1,2,3,4)
val res = collection.mutable.ListBuffer.empty[Int]
for(i <- a) {
     val z = i*i
     res += z
    }


///*** Set Method  - Set and mutable.Set
//No order and no duplicates, no indexing with (Int) (but (T) is used here for checking existance)
reflect.runtime.universe.typeOf[Set[Int]].members.toSet

//Main Methods & (intersection), &~ (difference), | (union) ,  contains, map, filter, sortBy, foreach, isEmpty, 
//Mutable +=, ++=, -=, --=
//genally single operation like + for one element 
//for multiple operations like ++, for another Collection 

val s = Set(1,2,3,4,1,2,3,4)
s contains 3  //res0: Boolean = true
for { e <- s} println(e) 
s ++ List(2,10)
s + 10
val s = collection.mutable.Set[Int]()
s.add(20)
//Set operations 
&   Intersection
&~  Difference
|   Union


///*** Map   - Map or mutable.Map 

//Main Methods - mutable +=, ++=, -=, --=  
//or immutable ++, contains, ++:, ++, 

//genally single operation like + for one element 
//for multiple operations like ++, for another Collection 
// :+ where LHS is Map , +: where RHS is Map  

//For Map - call keys(), values() to get keys and values
//toList gives tupleSet

val m = Map('o'-> 1, 'b' -> 2)
m ++ Map('c'-> 3)
m +('c'-> 3)

//Mutable 
val m = collection.mutable.Map[String,Int]()
m("OK") = 2
m += ("NOK" -> 3)
m contains "OK"
m contains "OKK"
for( (k,v) <- m) println(""+k+v)
m.keys   //res30: Iterable[Char] = Set(o, b)
m.values  //res31: Iterable[Int] = MapLike(1, 2)
m.toList  //res34: List[(Char, Int)] = List((o,1), (b,2))

//With default
val m = Map[String, Int]().withDefaultValue(0)
m("OK")  //res0: Int = 0

//mutable.HashSet and mutable.HashMap 
//- No order for iteration, but fast lookup
val map = scala.collection.mutable.HashMap.empty[Int,String]
map += (1 -> "make a web site")
map += (3 -> "profit!")
map(1)  //res44: String = make a web site
map contains 2  //res46: Boolean = false



///*** File Handling - scala.io.Source
//http://www.scala-lang.org/api/2.11.12/#scala.io.Source$
//fromFile, fromURL, fromChars, fromString, fromInputStream

val s = scala.io.Source.fromURL("http://www.google.co.in")  //s: scala.io.BufferedSource = non-empty iterator

val lines = scala.io.Source.fromFile("build.sbt").getLines.toList

val it = for {
      line <- scala.io.Source.fromFile("build.sbt").getLines
      ch <- line   if ch.isLetter   // or isDigit
    } yield ch          //to create a list


//With try block 'catch' must be in same line of '}' in repl

try {
  for (line <- scala.io.Source.fromFile("no.txt").getLines()) {
    println(line)
  }
} catch {
  case ex: Exception => println("an exception happened.")
}

//Properly closing the file

val bufferedSource = scala.io.Source.fromFile("Misc.scala")
for (line <- bufferedSource.getLines) {
        println(line.toUpperCase)
      }
bufferedSource.close




///Writing Text Files
//Use Java - Use PrintWriter(File) or FileWriter(File)

// PrintWriter - Prints to a text-output stream, buffered and manual flushing by 'flush()'
//appending a file not possible
//contains - print(type), printf(..), println(type), write(type)
import java.io._
val pw = new PrintWriter(new File("hello.txt" ))
pw.write("Hello, world")
pw.close

// FileWriter - low level writing chars, not buffered and manual flushing by 'flush()'
//FileWriter(String fileName, boolean append)
//contains append(string), write(string)
val file = new File("hello.txt")
val bw = new BufferedWriter(new FileWriter(file))
bw.write("Hello, world")
bw.close()


//Reading and Writing Binary Files
//Use Java -  by FileInputStream and FileOutputStream

import java.io._
var in = None: Option[FileInputStream]
var out = None: Option[FileOutputStream]

try {
    in = Option(new FileInputStream("/examples/Test.class"))
    out = Option(new FileOutputStream("/examples/Test.class.copy"))
    var c = 0
    while ({c = in.get.read; c != -1}) {
      out.get.write(c)
}
} catch {
    case e: IOException => e.printStackTrace
} finally {
    println("entered finally ...")
    if (in.isDefined) in.get.close
    if (out.isDefined) out.get.close
}

///Reference 
object scala.io.Source extends AnyRef
    val DefaultBufSize : Int
    def createBufferedSource (inputStream: InputStream, bufferSize: Int = DefaultBufSize, reset: () => Source = null, close: () => Unit = null)(implicit codec: Codec): BufferedSource
        Reads data from inputStream with a buffered reader, using the encoding in implicit parameter codec.
    def fromBytes (bytes: Array[Byte], enc: String): Source
    def fromBytes (bytes: Array[Byte])(implicit codec: Codec): Source
        Create a Source from array of bytes, decoding the bytes according to codec.
    def fromChar (c: Char): Source
        Creates a Source instance from a single character.
    def fromChars (chars: Array[Char]): Source
        creates Source from array of characters, with empty description.
    def fromFile (file: File, bufferSize: Int)(implicit codec: Codec): BufferedSource
        Creates Source from file, using given character encoding, setting its description to filename.
    def fromFile (file: File, enc: String, bufferSize: Int): BufferedSource
    def fromFile (file: File, enc: String): BufferedSource
        same as fromFile(file, enc, Source.
    def fromFile (file: File)(implicit codec: Codec): BufferedSource
        creates Source from file, using default character encoding, setting its description to filename.
    def fromFile (uri: URI, enc: String): BufferedSource
        creates Source from file with given file: URI
    def fromFile (uri: URI)(implicit codec: Codec): BufferedSource
        creates Source from file with given file: URI
    def fromFile (name: String, enc: String): BufferedSource
        creates Source from file with given name, using given encoding, setting its description to filename.
    def fromFile (name: String)(implicit codec: Codec): BufferedSource
        creates Source from file with given name, setting its description to filename.
    def fromInputStream (is: InputStream)(implicit codec: Codec): BufferedSource
    def fromInputStream (is: InputStream, enc: String): BufferedSource
    def fromIterable (iterable: Iterable[Char]): Source
        Creates a Source from an Iterable.
    def fromRawBytes (bytes: Array[Byte]): Source
        Create a Source from array of bytes, assuming one byte per character (ISO-8859-1 encoding.
    def fromString (s: String): Source
        creates Source from a String, with no description.
    def fromURI (uri: URI)(implicit codec: Codec): BufferedSource
        creates Source from file with given file: URI
    def fromURL (url: URL)(implicit codec: Codec): BufferedSource
        same as fromInputStream(url.
    def fromURL (url: URL, enc: String): BufferedSource
        same as fromInputStream(url.
    def fromURL (s: String)(implicit codec: Codec): BufferedSource
        same as fromURL(new URL(s))
    def fromURL (s: String, enc: String): BufferedSource
        same as fromURL(new URL(s))(Codec(enc))
    def stdin : BufferedSource
        Creates a Source from System.
        
class Source extends Iterator[Char]        
    def ++ (that: => Iterator[Char]): Iterator[Char]
        [use case] Concatenates this iterator with another.
    def ++ [B >: Char] (that: => GenTraversableOnce[B]): Iterator[B]
        Concatenates this iterator with another.
    def /: [B] (z: B)(op: (B, Char) => B): B
        Applies a binary operator to a start value and all elements of this traversable or iterator, going left to right.
    def /:\ [A1 >: Char] (z: A1)(op: (A1, A1) => A1): A1
        A syntactic sugar for out of order folding.
    def :\ [B] (z: B)(op: (Char, B) => B): B
        Applies a binary operator to all elements of this traversable or iterator and a start value, going right to left.
        object NoPositioner extends Positioner
        object RelaxedPosition extends Position
        A Position implementation which ignores errors in the positions.
        object RelaxedPositioner extends Positioner
    def addString (b: StringBuilder): StringBuilder
        Appends all elements of this traversable or iterator to a string builder.
    def addString (b: StringBuilder, sep: String): StringBuilder
        Appends all elements of this traversable or iterator to a string builder using a separator string.
    def addString (b: StringBuilder, start: String, sep: String, end: String): StringBuilder
        Appends all elements of this traversable or iterator to a string builder using start, end, and separator strings.
    def aggregate [B] (z: B)(seqop: (B, Char) => B, combop: (B, B) => B): B
        Aggregates the results of applying an operator to subsequent elements.
    def buffered : BufferedIterator[Char]
        Creates a buffered iterator from this iterator.
    def ch : Char
    def close (): Unit
        The close() method closes the underlying resource.
    def collect [B] (pf: PartialFunction[Char, B]): Iterator[B]
        Creates an iterator by transforming values produced by this iterator with a partial function, dropping those values for which the partial function is not defined.
    def collectFirst [B] (pf: PartialFunction[Char, B]): Option[B]
        Finds the first element of the traversable or iterator for which the given partial function is defined, and applies the partial function to it.
    def contains (elem: Any): Boolean
        Tests whether this iterator contains a given value as an element.
    def copyToArray (xs: Array[Char], start: Int, len: Int): Unit
        [use case] Copies selected values produced by this iterator to an array.
    def copyToArray [B >: Char] (xs: Array[B], start: Int, len: Int): Unit
        Copies selected values produced by this iterator to an array.
    def copyToArray (xs: Array[Char]): Unit
        [use case] Copies values of this traversable or iterator to an array.
    def copyToArray [B >: Char] (xs: Array[B]): Unit
        Copies values of this traversable or iterator to an array.
    def copyToArray (xs: Array[Char], start: Int): Unit
        [use case] Copies values of this traversable or iterator to an array.
    def copyToArray [B >: Char] (xs: Array[B], start: Int): Unit
        Copies values of this traversable or iterator to an array.
    def copyToBuffer [B >: Char] (dest: Buffer[B]): Unit
        Copies all elements of this traversable or iterator to a buffer.
    def count (p: (Char) => Boolean): Int
        Counts the number of elements in the traversable or iterator which satisfy a predicate.
        var descr : String
        description of this source, default empty
    def drop (n: Int): Iterator[Char]
        Advances this iterator past the first n elements, or the length of the iterator, whichever is smaller.
    def dropWhile (p: (Char) => Boolean): Iterator[Char]
        Skips longest sequence of elements of this iterator which satisfy given predicate p, and returns an iterator of the remaining elements.
    def duplicate : (Iterator[Char], Iterator[Char])
        Creates two new iterators that both iterate over the same elements as this iterator (in the same order).
    def exists (p: (Char) => Boolean): Boolean
        Tests whether a predicate holds for some of the values produced by this iterator.
    def filter (p: (Char) => Boolean): Iterator[Char]
        Returns an iterator over all the elements of this iterator that satisfy the predicate p.
    def filterNot (p: (Char) => Boolean): Iterator[Char]
        Creates an iterator over all the elements of this iterator which do not satisfy a predicate p.
    def find (p: (Char) => Boolean): Option[Char]
        Finds the first value produced by the iterator satisfying a predicate, if any.
    def flatMap [B] (f: (Char) => GenTraversableOnce[B]): Iterator[B]
        Creates a new iterator by applying a function to all values produced by this iterator and concatenating the results.
    def fold [A1 >: Char] (z: A1)(op: (A1, A1) => A1): A1
        Folds the elements of this sequence using the specified associative binary operator.
    def foldLeft [B] (z: B)(op: (B, Char) => B): B
        Applies a binary operator to a start value and all elements of this traversable or iterator, going left to right.
    def foldRight [B] (z: B)(op: (Char, B) => B): B
        Applies a binary operator to all elements of this traversable or iterator and a start value, going right to left.
    def forall (p: (Char) => Boolean): Boolean
        Tests whether a predicate holds for all values produced by this iterator.
    def foreach (f: (Char) => Unit): Unit
        [use case] Applies a function f to all values produced by this iterator.
    def foreach [U] (f: (Char) => U): Unit
        Applies a function f to all values produced by this iterator.
    def getLines (): Iterator[String]
        Returns an iterator who returns lines (NOT including newline character(s)).
    def grouped [B >: Char] (size: Int): GroupedIterator[B]
        Returns an iterator which groups this iterator into fixed size blocks.
    def hasDefiniteSize : Boolean
        Tests whether this Iterator has a known size.
    def hasNext : Boolean
        Returns true if this source has more characters.
    def indexOf [B >: Char] (elem: B): Int
        Returns the index of the first occurrence of the specified object in this iterable object.
    def indexWhere (p: (Char) => Boolean): Int
        Returns the index of the first produced value satisfying a predicate, or -1.
    def isEmpty : Boolean
        Tests whether this iterator is empty.
    def isTraversableAgain : Boolean
        Tests whether this Iterator can be repeatedly traversed.
    def length : Int
        Returns the number of elements in this iterator.
    def map [B] (f: (Char) => B): Iterator[B]
        Creates a new iterator that maps all produced values of this iterator to new values using a transformation function.
    def max : Char
        [use case] Finds the largest element.
    def max [B >: Char] (implicit cmp: Ordering[B]): Char
        Finds the largest element.
    def maxBy [B] (f: (Char) => B)(implicit cmp: Ordering[B]): Char
    def min : Char
        [use case] Finds the smallest element.
    def min [B >: Char] (implicit cmp: Ordering[B]): Char
        Finds the smallest element.
    def minBy [B] (f: (Char) => B)(implicit cmp: Ordering[B]): Char
    def mkString : String
        Displays all elements of this traversable or iterator in a string.
    def mkString (sep: String): String
        Displays all elements of this traversable or iterator in a string using a separator string.
    def mkString (start: String, sep: String, end: String): String
        Displays all elements of this traversable or iterator in a string using start, end, and separator strings.
        var nerrors : Int
    def next (): Char
        Returns next character.
    def nonEmpty : Boolean
        Tests whether the traversable or iterator is not empty.
        var nwarnings : Int
    def padTo (len: Int, elem: Char): Iterator[Char]
        [use case] Appends an element value to this iterator until a given target length is reached.
    def padTo [A1 >: Char] (len: Int, elem: A1): Iterator[A1]
        Appends an element value to this iterator until a given target length is reached.
    def partition (p: (Char) => Boolean): (Iterator[Char], Iterator[Char])
        Partitions this iterator in two iterators according to a predicate.
    def patch [B >: Char] (from: Int, patchElems: Iterator[B], replaced: Int): Iterator[B]
        Returns this iterator with patched values.
    def pos : Int
    def product : Char
        [use case] Multiplies up the elements of this collection.
    def product [B >: Char] (implicit num: Numeric[B]): B
        Multiplies up the elements of this collection.
    def reduce [A1 >: Char] (op: (A1, A1) => A1): A1
        Reduces the elements of this sequence using the specified associative binary operator.
    def reduceLeft [B >: Char] (op: (B, Char) => B): B
    def reduceLeftOption [B >: Char] (op: (B, Char) => B): Option[B]
        Optionally applies a binary operator to all elements of this traversable or iterator, going left to right.
    def reduceOption [A1 >: Char] (op: (A1, A1) => A1): Option[A1]
        Optionally reduces the elements of this sequence using the specified associative binary operator.
    def reduceRight [B >: Char] (op: (Char, B) => B): B
        Applies a binary operator to all elements of this traversable or iterator, going right to left.
    def reduceRightOption [B >: Char] (op: (Char, B) => B): Option[B]
        Optionally applies a binary operator to all elements of this traversable or iterator, going right to left.
    def report (pos: Int, msg: String, out: PrintStream): Unit
    def reportError (pos: Int, msg: String, out: PrintStream = Console.err): Unit
        Reports an error message to the output stream out.
    def reportWarning (pos: Int, msg: String, out: PrintStream = Console.out): Unit
    def reset (): Source
        The reset() method creates a fresh copy of this Source.
    def sameElements (that: Iterator[_]): Boolean
        Tests if another iterator produces the same values as this one.
    def scanLeft [B] (z: B)(op: (B, Char) => B): Iterator[B]
    def scanRight [B] (z: B)(op: (Char, B) => B): Iterator[B]
    def seq : Iterator[Char]
        A version of this collection with all of the operations implemented sequentially (i.
    def size : Int
        The size of this traversable or iterator.
    def slice (from: Int, until: Int): Iterator[Char]
        Creates an iterator returning an interval of the values produced by this iterator.
    def sliding [B >: Char] (size: Int, step: Int = 1): GroupedIterator[B]
        Returns an iterator which presents a "sliding window" view of another iterator.
    def span (p: (Char) => Boolean): (Iterator[Char], Iterator[Char])
        Splits this Iterator into a prefix/suffix pair according to a predicate.
    def sum : Char
        [use case] Sums up the elements of this collection.
    def sum [B >: Char] (implicit num: Numeric[B]): B
        Sums up the elements of this collection.
    def take (n: Int): Iterator[Char]
        Selects first n values of this iterator.
    def takeWhile (p: (Char) => Boolean): Iterator[Char]
        Takes longest prefix of values produced by this iterator that satisfy a predicate.
    def toArray : Array[Char]
        [use case] Converts this traversable or iterator to an array.
    def toArray [B >: Char] (implicit arg0: ClassManifest[B]): Array[B]
        Converts this traversable or iterator to an array.
    def toBuffer [B >: Char] : Buffer[B]
        Converts this traversable or iterator to a mutable buffer.
    def toIndexedSeq [B >: Char] : IndexedSeq[B]
        Converts this traversable or iterator to an indexed sequence.
    def toIterable : Iterable[Char]
        Converts this traversable or iterator to an iterable collection.
    def toIterator : Iterator[Char]
        Returns an Iterator over the elements in this traversable or iterator.
    def toList : List[Char]
        Converts this traversable or iterator to a list.
    def toMap [T, U] : Map[T, U]
        [use case] Converts this traversable or iterator to a map.
    def toMap [T, U] (implicit ev: <:<[Char, (T, U)]): Map[T, U]
        Converts this traversable or iterator to a map.
    def toSeq : Seq[Char]
        Converts this traversable or iterator to a sequence.
    def toSet [B >: Char] : Set[B]
        Converts this traversable or iterator to a set.
    def toStream : Stream[Char]
        Converts this traversable or iterator to a stream.
    def toString (): String
        Converts this iterator to a string.
    def toTraversable : Traversable[Char]
        Converts this traversable or iterator to an unspecified Traversable.
    def withClose (f: () => Unit): Source.this.type
    def withDescription (text: String): Source.this.type
    def withFilter (p: (Char) => Boolean): Iterator[Char]
        Creates an iterator over all the elements of this iterator that satisfy the predicate p.
    def withPositioning (pos: Positioner): Source.this.type
    def withPositioning (on: Boolean): Source.this.type
        Change or disable the positioner.
    def withReset (f: () => Source): Source.this.type
    def zip [B] (that: Iterator[B]): Iterator[(Char, B)]
        Creates an iterator formed from this iterator and another iterator by combining corresponding values in pairs.
    def zipAll [B] (that: Iterator[B], thisElem: Char, thatElem: B): Iterator[(Char, B)]
        [use case] Creates an iterator formed from this iterator and another iterator by combining corresponding elements in pairs.
    def zipAll [B, A1 >: Char, B1 >: B] (that: Iterator[B], thisElem: A1, thatElem: B1): Iterator[(A1, B1)]
        Creates an iterator formed from this iterator and another iterator by combining corresponding elements in pairs.
    def zipWithIndex : Iterator[(Char, Int)] { ... /* 2 definitions in type refinement */ }
        Creates an iterator that pairs each element produced by this iterator with its index, counting from 0.
      	  
		  


///*** Executing External Commands

import sys.process._
val exitCode = "ls -al".!    //Get exit code, displays in stdout// beware leading whitespace // beware trailing whitespace
val  out = "ls -al".!!       //as a string
out.count( c => c == '\n')   //res18: Int = 40, how many lines
val  out2 = "ls -al".lineStream.toList  // Lines in List, lineStream returns Stream , older version- use lines

//When error
val out = "ls -l fred" !     //returns exitValue
//but !! raises Exception
val out = "ls -l fred" !!     // java.lang.RuntimeException: Nonzero exit value: 1
 
//Unexpected newline characters, use trim
val dir = "pwd" !!   //contains new line
val dir = "pwd".!!.trim

//to Use glob patterns , use Seq version not String version
Seq("ls", "*.txt").!!  //in String
Seq("ls", "*.txt").lineStream.toList  //In List

//Redirecting Input/Output
//Use  #<, #> and #>>, 
//may take as input either another ProcessBuilder or java.io.File or a java.io.InputStream. 
new java.net.URL("http://www.google.co.in") #> "grep google" #>> new java.io.File("google.txt") !

//Pipe - #|  , shell and - #&&, , shell or - #||
"ls" #| "grep .scala" #&& Seq("sh", "-c", "scalac *.scala") #|| "echo nothing found" !



///*** Scala Class and Companion objects
//val - can be 'get' only , var- can be 'get' and 'set', none - can not be 'get' or 'set'
//default - public 
// def m =... , can only be called as .m 
//but def m()=.., can be called as .m or .m()

//:paste
//or :load /path/to/file 

//for Ordering for type T (your class)
//Either extends from Ordered[T] and implement 'def compare(o:T):Int'
//or implement type class(probably in companion object which is in Implicits scope)
//implicit val xyz = new Ordering[T]{ def compare(o1:T,o2:T):Int }

class Person(var name: String) extends Ordered[Person] {
   println("Body of primary constructor")

   // auxiliary constructor, must call any constructor defined earlier 
   //or primary constructor at first line of code, then do other activities 
   def this() {  this("No Name")   }   
   override def toString = s"Person(${this.name})"
   
   //Must match this signature 
   override def equals(o:Any):Boolean = o match { 
         case x:Person =>   this.hashCode == o.hashCode
		 case _    => false
		}
   //for multiple , package in Tuple and then get .hashCode 
   override def  hashCode:Int =  this.name.hashCode   
   
   //For multiple , do if and else 
   //Note tuple does not have by default ordering 
   //to get tuple ordering, use 'import scala.math.Ordering.Implicits._ ' in scope (ie in class body or in function body)
   def compare(o:Person) =   name.compare(o.name)      // returns -1, 0, +1
}
object Person {   //companion object , can see each other's private[this]
   def getPerson = { new Person() }
   def apply(n:String) = new Person(n)
   def unapply(p:Person) = Some(p.name)
}

new Person()
new Person(name="Das")
new Person("Das")
Person("Das")
val Person(x) = Person("Das")
Person.getPerson

//Singleton object is 'object' construct without any 'class' construct

//Note Constructor is just like a method, hence can be curry form  or with default param 
//or with implicit param or even can be partial applied

class A(val x:Int)(val y:Int)
class A(val x:Int, val y:Int){
  override def toString = s"$x $y"
}
val a = (new A(_:Int, 2))(3) //a: A = 3 2

///*** Case class 
//Case classes to generate all these code (along with companion object)
//Case class constructor parameters are val by default
//case class does not allow to  inherit from a case class. 
//but can inherit from a non case class

//must use () 
case class A //<console>:1: error: case classes without a parameter list are not allowed;

case object A //OK


case class Person(name: String) extends Ordered[Person] {
 def compare(o:Person) =   name.compare(o.name)      // returns -1, 0, +1
}
// the companion object, for generating auxiliary constructors
object Person extends ((String) => Person) {  //by default, companion object for case class extends FunctionN
        def apply() = new Person("<no name>")
}
	

//Case class copy

case class Foo(a: Int, b: Int, c: Int, n:Int, z:Int)

//To copy but change n
val foo = Foo(1,2,3,4,5)
foo.copy(n = 3)

///*** Companion Object 
//Generally used for apply(creation), unapply(pattern matching)
//and implicits for converting other type to this type 

//Scala's auxillary constructors (unlike Java's) can't take type parameters, 
//only the class itself can. 
//Error 
class Point(val x:Float, val y:Float) {
    this[T](x:T, y:T) = this(x.toFloat, y.toFloat)
}

//Use Object apply 
class Point(val x:Float, val y:Float)
object Point {
  def apply(x:Float, y:Float) = new Point(x,y)
  def apply[T : Numeric](x: T, y: T) = {
    val n = implicitly[Numeric[T]]
    new Point(n.toFloat(x), n.toFloat(y))
  }
}




///*** Default value for Var
//Note val must be initialized (independently or via constructor parameter)

class A {
      var x:Int = _   //for var, OK, for val, Error
}

//even it is Ok for parameter

class A[T]{
      var x:T = _
 }
	
(new A[Int]).x  //res0: Int = 0

(new A[String]).x //res1: String = null

(new A[Double]).x  //res2: Double = 0.0	




///*** Private constructor, singleton pattern
class Brain private {
        override def toString = "This is the brain."
      }

object Brain {
        private val brain = new Brain
        def getInstance = brain
}


///*** Overriding Default Accessors and Mutators
// By default , following is generated 
//val - accessor method, var- Accessor and mutator , none - nothing 
// for public - those are public, for private-those are private 
//by default, all methods are public 

//x is instance 
				expansion
x.f 			x.f or 'def f' under x
x.f = e 		x.f_=(e) 
x.f() = e 		x.f.update(e) 
x.f(i) = e 		x.f.update(i, e) 
x.f(i, j) = e 	x.f.update(i, j, e) 
x()				x.apply()
x(i)			x.apply(i)	
CLASS(i)        apply(i) inside object CLASS 		

//Example 
class Person(private var _name: String) {
        def name = _name                               // accessor
        def name_=(aName: String) { _name = aName }    // mutator
    }


val p = new Person("Jonathan")
p.name = "Jony"    // setter
println(p.name)    // getter


//Preventing Getter and Setter Methods of any val/var 
//Use private or private[this] 
class Stock( private val x:Int) {  //private getter 
        // getter and setter methods are generated
        var delayedPrice: Double = _    

        // Private to class, other instance of same class can access this
        private var currentPrice: Double = _
        def setCPrice(p: Double) { currentPrice = p }    

		//Unlike private, the field can't also be accessed by other instances of the same type
         private[this] var price: Double = _

        def isHigher(that: Stock): Boolean = this.price > that.price	//Error	
    }

	
	
///*** Assigning a Field to a Block or Function and Lazy variable

class X { val x = { Thread.sleep(2000); 15 } }

class Y { lazy val y = { Thread.sleep(2000); 13 } }
new X      //waiting
new Y      //immediate
(new X).x     //immediate
(new Y).y    //waiting



///*** Inner class 

//Inner class - Outer can not access private of Inner (unlike java)
//like java, inner can access private,private[this] of Outer 

//Inner must exist with instance of Outer
class Outer { 
      var a:Int = _
      class Inner {
          var b = Outer.this.a          //Access Outer's a
       }
     def m( x:Inner) = { x.b }			// Only take this's Inner
     def m2( x:Outer#Inner) = { x.b }   //can take any Outer's Inner
}


val a = new Outer()   // no inner is created
val a_in = new a.Inner  //Now a's Inner created, type is a.Inner
a.m(a_in)              //OK

val b = new Outer
val b_in = new b.Inner
a.m(b_in)              //NOK, as b.Inner is not equal to a.Inner
a.m2(b_in)           //OK as m2 take any Outer's inner //res54: Int = 0

//More example of Outer#Inner vs outer.Inner 

val outer = new Outer
val outer2 = new Outer
var inner: outer.Inner = new outer.Inner
var inner2: Outer#Inner = new outer2.Inner

inner = inner2     // NOK as inner is outer.Inner and not same as Outer#Inner, inner can only take outer.Inner
inner2 = inner     // OK as any Inner can be assigned

//NOTE: You can not create mutable outer instances

var outer = new Outer
val inner = new outer.Inner   //NOK

//Solution create factory
def mkInner(o: Outer): Outer#Inner = new o.Inner
val inner = mkInner(outer) //OK 


//Inner object - no this for inner 
class A {
 val a:Int = 2 
 val b:Int = InnerObj.m   //can not access private Inner variable 
 object InnerObj{ 
    val b = 2
	def m = A.this.a   //ok, but must have instance
 }
}
A#InnerObj.m  //Error
(new A).InnerObj.m  //res5: Int = 2

//Object inside Object - similar concept
object A {
  val a:Int = 2 
  val b:Int = InnerObj.m   //can not access private Inner object
  object InnerObj{ 
    val b = 2
	def m = A.a   //ok
 }
}
A.InnerObj.b


///*** Renaming this or Outer.this  - self type 
class A {
  outer =>
  private val a:Int = 0
  private class B {					// if this is private, A can access it's public member, but outside world can not
     inner =>
	  private val b:Int = 0        //since it is private, outerclass can not access (unlike Java)
	  def m = outer.a + inner.b    // outer === A.this and inner == this. Note inner class can access Outer's private 
	  }
  def m2 = { val i = new B ; i.m }  
 }
	
new A#B()  // note A#B is only a type, can not be used as constructor
val a:A#B = { val m = new A; new m.B } //a: A#B = A$B@57b59fa2  // if B is private , new m.B fails


///*** Inheritance, extends Base ( only one base class)

//leave the val or var declaration off of the fields 
//that are common to Base class


// Auxiliary constructors in subclass 
//there is no way for auxiliary constructors to call a superclass constructor.
//Auxiliary constructor can only call primary constructor or earlier defined constructor of same class

// (1) primary constructor
class Animal ( var name: String, var age: Int) {
	  // (2) auxiliary constructor
	  def this (name: String) {
		this(name, 0)
	  }

	  override def toString = s"$name is $age years old"
}

//in first line of subclass, subclass can call any ctor of base class 
// calls the Animal one-arg constructor
class Dog (name: String) extends Animal (name) {
        override def toString = s"Dog: $name is $age years old"
        def this() {this("No-name")}   //can only call own ctor 
}



///*** Exception as Inheritance 
class MyE(s:String) extends Exception(s)  //my exception

try{
 throw new MyE("OK")
}catch {             //must be in same line
	case ex: Exception => println("an exception happened.")
}finally {

}

//Declaring That a Method Can Throw an Exception

@throws(classOf[Exception])
override def play {
      // exception throwing code here ...
}

//Examples
@throws(classOf[IOException])
@throws(classOf[LineUnavailableException])
@throws(classOf[UnsupportedAudioFileException])
def playSoundFileWithJavaAudio {
  // exception throwing code here ...
}

//Diff with java 

// 1) it's not necessary to state that a method throws an exception
def boom {
  throw new Exception
}
   

// 2) it's not necessary to wrap 'boom' in a try/catch block
boom
     
"""
Rules of Java exception - not applicable for scala
Java has (a) checked exceptions, (b) descendants of Error, and (c) descendants of RuntimeException. 
The class Exception and  any subclasses that are not also subclasses of RuntimeException are checked exceptions.
Checked exceptions need to be declared in a method or constructor's throws clause if they can be thrown by the execution of the method 
or constructor and propagate outside  the method or constructor boundary.
Else all checked exceptions must be handled inside a method
"""

//With try block

try {
  for (line <- scala.io.Source.fromFile("no.txt").getLines()) {
    println(line)
  }
} catch {
  case ex: Exception => println("Bummer, an exception happened.")
}

///*** override Rules 

//Note that variables in Scala can be overridden (unlike java and c++)
//in Java, c++,  variables are static bound, but methods are dynamic bound (if virtual in c++)
//and Derived class hides Base variable if same name 
//( in c++, even derived method hides base method, but in java, scala, they create overloaded scenario)
//eg, in java, c++, Base b = new Derived, only you can access Base's variables (not derived) 
//but when you access a method, it could be Derived's if overridden.

//In scala, both variables and methods are dynamic bound (ie derived class can not hide Base's)
//(must use override if same name with type)

class A { val a = 1; var b = 2; def c = 0 }  
//val can be overridden by val, var can not be,
// def can be by def or val

val a = new A

"""
In scala interpretor, Use TAB to see all methods
scala> a.
a   asInstanceOf   b   b_=   c   isInstanceOf   toString
"""
scala.reflect.runtime.universe.typeOf[A].baseClasses  //List(class A, class Object, class Any)


class B extends A { override val a = 2}   //OK, val can be overriden
class B extends A { override val b = 2}   //NOK, can not override var by val
class B extends A { override val c = 2}   //OK, def can be override by val
class B extends A { override var b = 2}   //NOK var can not be overridden 
class B extends A { override var c = 2}	  //NOK
class B extends A { override def c = 2}   //OK, def can be overridden by def
class B extends A { override def b = 2}   //NOK
class B extends A { override def a = 2}   //NOK



///*** Abstract Class
//When to Use an Abstract Class instead of trait
1.You want to create a base class that requires constructor arguments.
2.The code will be called from Java code.

// this won't compile
trait Animal(name: String)

//Solution
abstract class Animal(name: String)

//Rules
"""
  · An abstract var field results in getter and setter methods 
    being generated for the  field.
  · An abstract val field results in a getter method being generated 
    for the field.  
  · For abstract field , the Scala compiler does not create a field 
    in the resulting code; only getter/setter
  · During object creations,  fields are assigned only once 
  · During object creation,superclass constructor is called 
    first with overridden val/var as null
"""


// initialization
//-Xcheckinit: Add runtime check to field accessors (only for testing as code size increases)

//superclass constructor is called first with overridden val/var as null
//no need to mention 'abstract' modifier, simply leave the definition
abstract class X {
        val x: String  
        println ("x is "+ x.length)  // in primary constructor, x is still null
}

object Y extends X { val x = "Hello" }   

Y   //Exeptions 

//Solution use lazy either in X or in Y 
//or use preinitialized fields 
object Y extends X { lazy val x = "Hello" }
object Y extends { val x = "Hello" } with X

Y   //OK







///*** Import 

//Packaging with the Curly Braces Style Notation

package com.pany.store {
        class Foo { override def toString = "I am com.pany.store.Foo" }
}

//OR
package com.pany.store
class Foo { override def toString = "I am com.pany.store.Foo" }


//Importing One or More Members
import java.io.File
import java.io.File
import java.io.IOException
import java.io.FileNotFoundException

//OR
import java.io.{File, IOException, FileNotFoundException}

//OR
import java.io._

//OR
import java.io.File
import java.io.PrintWriter

class Foo {
	import javax.swing.JFrame            // only visible in this class
	// ...
}

class Bar {
	import scala.util.Random             // only visible in this class

	// ...
}

//OR
class Bar {
  def doBar = {
	import scala.util.Random  //only visible in this methods 
	println("")
  }
}


//Renaming Members on Import
import java.util.{ArrayList => JavaList}

val list = new JavaList[String]

import java.util.{Date => JDate, HashMap => JHashMap}


// error: this won't compile because HashMap was renamed
// during the import process
val map = new HashMap[String, String]



//Hiding a Class During the Import Process
import java.util.{Random => _, _}
// can't access Random
val r = new Random  //NOK
// can access other members
new ArrayList
    
import java.util.{List => _, Map => _, Set => _, _}
new ArrayList  //OK
  
  
  
//Using Static Imports, don't use static,
import java.lang.Math._

val a = sin(0)
val a = cos(PI)


//instances also can be imported
case class Dog(name: String) {
 def bark() {
	println("Bow Vow")
	}
}

val d = Dog("Barnie") //d: Dog = Dog(Barnie)

//Local scoping 
locally {
	import d._
	bark()
	bark()
}
	  
//object can also be imported





///*** Controlling Method Scope

//1. Object-private scope       - private[this]   
//Only this can access own's (not others)
class Foo {

  private[this] def isFoo = true

  def doFoo(other: Foo) {
	if (other.isFoo) { // this line won't compile
		// ...
	}
  }

}

//2. Private scope
//One instance can access own and other instances 
class Foo {

	private def isFoo = true

	def doFoo(other: Foo) {
	  if (other.isFoo) { // this now compiles, but isFoo can not be accessed by subclass
		// ...
	  }

	}
}



//3. Protected scope 
//subclass can access own or other instance's protected, 
//but can not access unrelated new Baseclass's protected (like Java)

class Animal {
  protected def breathe {}
}

class Dog extends Animal {
  breathe
}

//In Java, protected methods can be accessed by other classes in the same package, 
//but this isn't true in Scala , use private[packageName]

package world {

	  class Animal {
		protected def breathe {}
	  }

	  class Jungle {
		val a = new Animal
		a.breathe    // error: this line won't compile
	  }

}
//4. Package scope - private[packageName]  
//like java package private 

 package world {

	  class Animal {
		private[world] def breathe {}
	  }

	  class Jungle {
		val a = new Animal
		a.breathe    // now compiles
	  }

}

//4.1 Package scope at various levels of packages

package com.pany.app.module {
	class Foo {
	  private[module] def doX {}
	  private[app] def doY {}
	  private[pany] def doZ {}
	}
}

import com.pany.app.module._

package com.pany.app.mod {
  class Bar {
	val f = new Foo
	f.doX // won't compile
	f.doY
	f.doZ
  }
}

package com.pany.common {
  class Bar {
	val f = new Foo
	f.doX // won't compile
	f.doY // won't compile
	f.doZ
  }
}


//5. Public scope (default)
package com.pany.app.module {
  class Foo {
	def doX {}
  }
}

package org.xyz.bar {
  class Bar {
	val f = new com.pany.app.module.Foo
	f.doX
  }
}


///*** default imports in scala program 
import java.lang._
import scala._
import scala.PreDef._  //many implicit conversio eg Int to RichInt




///*** package objects 
// Each package is allowed to have one package object. 
//Any definitions placed in a package object are considered members of the package itself. 
//For instance, they are also frequently used to hold package-wide type aliases and implicit conversions. 
//Package objects can even inherit Scala classes and traits. 

// in file gardening/fruits/Fruit.scala 
package gardening.fruits 
case class Fruit(name: String, color: String) 
object apple extends Fruit("Apple", "green") 
object plum extends Fruit("Plum", "blue") 
object banana extends Fruit("Banana", "yellow")   

// in file gardening/fruits/package.scala 
//this package object of fruits ie import like 'import gardening.fruits._'
//Note package name is gardening, but put inside 'fruits' subdir 
package gardening 
package object fruits { 
  val planted = List(apple, plum, banana)                
  def showFruit(fruit: Fruit) { 
    println(fruit.name +"s are "+ fruit.color) 
  } 
}  

// in file PrintPlanted.scala 
import gardening.fruits._ 
object PrintPlanted { 
  def main(args: Array[String]) { 
    for (fruit: Fruit <- fruits.planted) { 
      showFruit(fruit) 
    } 
  } 
}  

//Note imports in package objects are not imported 
//but val/var, def and implicit of those, defined in a package object 'fruits' are available to:
1. Any class in the fruits (or a sub-package there-of)
2. Anyone outside who issues import gardening.fruits._
3. Implicit search for type T, where T is in that 'fruits' or a sub-package






	
///*** SBT with scalaTest 
//*File:first/build.sbt
lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.example",
      scalaVersion := "2.11.11"
    )),
    name := "first"
  )

libraryDependencies ++= Seq(
  "org.specs2" %% "specs2-core" % "4.0.2" % "test",
  "org.scalatest" %% "scalatest" % "3.0.0" % "test"
)

//assembly, use assembly plugin in project/plugins.sbt 

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


//MyInt with +,- and ==  and its ScalaTestCase

class MyInt(val x:Int) extends Ordered[MyInt] {

   def this() { this(0) }
   
   override def hashCode = x.hashCode
   override def toString =  s"MyInt($x)"
   override def equals(o :Any) = o match {
          case _:MyInt =>  o.hashCode == this.hashCode
		  case _ => false
		  }    
    def +( o:MyInt ) = MyInt(this.x + o.x)
	def compare(o:MyInt)  =  this.x compare o.x
}
object MyInt {
   def apply(x:Int) = new MyInt(x) 
}

//Test 
import org.scalatest._

class MyIntTest extends FunSuite with BeforeAndAfter with Matchers {     

  // these first two instance should be equal  
  // Fixtures as reassignable variables and mutable objects
  var a:MyInt = _ 
  var c:MyInt = _ 
  var d:MyInt = _ 
  before {
      a = MyInt(1)
      c = MyInt(2)
      d = MyInt(1)
  }

  test("same == same ") { assert(a == d) }
  test("same != different") { assert(a != c ) }
  test(" + ") { assert(a + d == c) }  
}

scala> org.scalatest.run(MyIntTest)


///*** ScalaTest - Quick Matchers 
import Matchers._

result should equal (3) 
result should === (3)   
result should be (3)    
result shouldEqual 3    
result shouldBe 3       

//*Checking strings 

string should startWith ("Hello")
string should endWith ("world")
string should include ("seven")

//either a String or a scala.util.matching.Regex.
string should startWith regex "Hel*o"
string should endWith regex "wo.ld"
string should include regex "wo.ld"

string should fullyMatch regex """(-)?(\d+)(\.\d*)?"""

"abbccxxx" should startWith regex ("a(b*)(c*)" withGroups ("bb", "cc"))
"xxxabbcc" should endWith regex ("a(b*)(c*)" withGroups ("bb", "cc"))
"xxxabbccxxx" should include regex ("a(b*)(c*)" withGroups ("bb", "cc"))
"abbcc" should fullyMatch regex ("a(b*)(c*)" withGroups ("bb", "cc"))


//*Greater and less than 
//check whether any type for which an implicit Ordering[T] is available is greater than, less than, greater than or equal, or less than or equal to a value of type T. 
one should be < 7
one should be > 0
one should be <= 7
one should be >= 0
	
//* combining test 
import org.scalatest.Matchers._
val result = 8
result should (be > 0 and be < 10)                         // You can and matcher expressions together

val map = Map("hi" -> "HI", "hei" -> "HEI", "he" -> "HE")
map should (contain key ("hi") or contain key ("ho"))      // You can or matcher expressions together

val result = "Hello Word"                                  // You can negate a matcher expression
result should not be (null)

val map = Map("one" -> 1, "two" -> 2, "three" -> 3)
map should (contain key ("two") and not contain value (7)) // Another example of negation










///*** Pattern Matching

//Scala Extractors
//Pattern matching exists on 'match/case', 
//'for' comprehensions, on 'val/var' declarations ,
// on 'catch/case' and for defining PartialFunction or Function 
//But not on assignments
object Twice {                              
  def apply(x: Int): Int = x * 2
  def unapply(z: Int): Option[Int] = Some(z/2)   //Must return Option type, scala would remove Option when giving result 
}

val x = Twice(21)   // x = 42  //apply
val Twice(y) = 22   //y is 11  //unapply 
var Twice(y1) = 22   // y1 is 11 //unapply 
x match { case Twice(n) => Console.println(n) }   //unapply 

val l = List(x) 
l.collect { case x:Int => "OK" }
for { 
    x <- l    //one '<-' is must 
    Twice(n) = 22  //unapply 
    y = Twice(2)   //apply
 } println(n+x+y) //57


//For repeated parameters, 
//Use apply and unapplySeq: Option[Seq[T]] de-structures:

//:paste


object M {
  def apply(a: String*) = a.mkString(":") // makes a M from an As.
  def unapplySeq(m: String): Option[Seq[String]] = Some(m.split(":") )// retrieve the As from the M
}
val m = M("Ok", "nok")  //m: String = Ok:nok
val M(s,a) = "ok:nok"  //s: String = ok a: String = nok

m match { case M(a1, a2) => true } 
m match { case obj @ M(a, aes @ _*) => s"$obj $a $aes" } 



//Details of Pattern Match


case class Person( firstName:String, lastName:String)
case class Dog(name:String)

//x must be type Any for matching with class type eg Dog, List, s:String , Some
//for constant pattern, x might not be Any type

//first match ends the matching

//Map and Set does not have apply/unapply

//Can use Array, List, Vector, collection.mutable.ArrayBuffer, collection.mutable.ListBuffer
//can use Seq, Stream, Option -Some/None, Try -Success, Failure , Either - Left, RIght etc

def mat(x:Any) = x match {    //x Must be Any 
       // constant patterns
      case 0 => "zero"
	  case 1 | 3 | 5 | 7 | 9 => s"odd"     //Alternate 
      case Nil => "an empty List"

      // sequence patterns
      case List(0, _, _) => "a three-element list with 0 as the first element"
      case h :: t    => s"List with $h and $t"  // this is equivalent to 'case ::(h,t)'

      // tuples
      case (a, b) => s"got $a and $b"    

      // constructor patterns
      case Person(first, "Alexander") => s"found an Alexander, first name = $first"
      case first Person second => s"$first $second"  //OR Person(first, Second)
	  
      // typed patterns
      //lower case variable is taken as pure variable and gets assigned to whole match
      case s: String            => s"string: $s"
      case as: Array[String]    => s"an array of strings: ${as.mkString(",")}"  //Only Array can have types
      case d: Dog               => s"dog: ${d.name}"
      case list: List[_]        => s"List: $list"	  //for Other  collection, Type does not matter because of type erasure

	  //case x: List(1, _*) => s"$x" // doesn't compile - to match whole List 
      case x @ List(1, y @ _*) => s"$x"
	   
      //case Some(_) => "got a Some" // works, but can't access the Some
         
      case Some(x) => s"$x" 				// works, returns "foo"
           
  	  case x @ Some(_) => s"$x"               // works, returns "Some(foo)"
      case p @ Person(first, "Doe") => s"$p"   // works, returns "Person(John,Doe)"

      case Seq('s','c','a','l','a', rest @ _*) =>   s"rest is $rest"
      
	  case sq @ Seq(_*) =>      s"Seq $sq"
      
	  //with guard statement
      case a if   0 to 9 contains a =>    s"0-9 range"
      case b if   10 to 19 contains b =>  s"10-19 range: $b"

      case (x,y) if x == y => s"tuple with same elements"
      case x if (x == 2 || x == 3) => s"$x"

      case Person(name, _) if name == "Fred" => s"Person Fred"
       
      case _ => s"default"     //inplace of _ , any var would do 
}

 
///*** Backticks in Scala
//Scala allows to define identifiers that start and end with backticks 
//and can contain pretty much anything between them, including spaces
//`start date` is a valid identifier. So is, `yeah you get the point`.


def `Given I login as user`(username:String) = {
    // do something with username
}
 

//to use a Java library whose method name is actually a reserved keyword in Scala (say, val), 
foo.`val`();  // it works!
 

//use backticks in match statements to match variables beginning with lower case letters 

//(match binds identifiers starting with a lower case letter with the pattern matched, 
//while identifiers starting with an upper case letter is treated as identifiers from the outer scope)

val Foo = "foo"
val foo = "bar"
 
"foo" match {
    case foo => println("bar")  //prints this 
    case Foo => println("foo")
}

//To refer to the value of the foo variable
val Foo = "foo"
val foo = "bar"
 
"foo" match {
    case `foo` => println("bar")  
    case Foo => println("foo") //prints this 
}
"bar" match {
    case `foo` => println("bar")  //prints this
    case Foo => println("foo")  
}
 


///*** Scala Symbol - scala.Symbol 
//Used to get unique objects for equal strings. 
//symbols are interned, use eq as well == for checking 

// 'mysym  -invokes Symbol("mysym")








 ///*** Serialization of scala class

import java.io._


@SerialVersionUID(123L)
class Person(val age: Int) extends Serializable {
  override def toString = s"Person($age)"
}

val os = new ObjectOutputStream( new FileOutputStream("example.dat"))
os.writeObject(new Person(22))
os.writeObject(new Person(23))
os.close()

val is = new ObjectInputStream( new FileInputStream("example.dat"))
val obj1 = is.readObject()
val obj2 = is.readObject()
is.close()
println(obj1)
println(obj2)


///*** Value Classes and Universal Traits 
//No runtime object creation. Derives from AnyVal
class Wrapper(val underlying: Int) extends AnyVal

//Usecase, for adding implicits 
implicit class Wrapper(val underlying: Int) extends AnyVal {
  def double: Int = 2 * underlying
}


"""
The type at compile time is Wrapper, 
but at runtime, the representation is an Int. 

A Value class can have only one val

A value class can define defs, but no vals, vars, or nested traits, classes or objects

A value class can only extend universal traits 
A universal trait is a trait that extends Any, only has defs as members, and does no initialization. 
Universal traits allow basic inheritance of methods for value classes, but they incur the overhead of allocation.
"""

trait Printable extends Any {
  def print(): Unit = println(this)
}
class Wrapper(val underlying: Int) extends AnyVal with Printable

val w = new Wrapper(3)
w.print()     // actually requires instantiating a Wrapper instance

//Another example

class Meter(val value: Double) extends AnyVal {
  def +(m: Meter): Meter = new Meter(value + m.value)
}

val x = new Meter(3.4)    // no Meter class
val y = new Meter(4.3)    // no Meter class
val z = x + y



///*** Dynamic Type

import scala.language.dynamics

"""
compiler rewrites and a special marker trait: scala.Dynamic.
it’s used by implementing a few "magic" methods:
•	applyDynamic(methodName: String)(args: Any*)
•	applyDynamicNamed(name: String)(args: (String, Any)*) for keyword based arg
•	selectDynamic(name: String): Option[Any] for field or def without arg
•	updateDynamic(name: String)(value: Any) for updating field 

"""
//applyDynamic - for any methods not implemented

object OhMy extends Dynamic {
  def applyDynamic(methodName: String)(args: Any*) {
    println(s"""|  methodName: $methodName,
                |args: ${args.mkString(",")}""".stripMargin)
  }
}

OhMy.dynamicMethod("with", "some", 1337)  //methodName: dynamicMethod,  args: with,some,1337

  
//applyDynamicNamed  - args would have key name and value
  
object OhMy  extends Dynamic {
  def applyDynamicNamed(name: String)(args: (String, Any)*) {
    println(s"""Creating a $name, for:\n "${args.head._1}": "${args.head._2}" """)
  }
}

OhMy.node(nickname = "ktoso")   //


//selectDynamic  , used for methods without ()

object OhMy  extends Dynamic {
  def selectDynamic(name: String): Option[Any] ={
    println(s"""Creating a $name""");None}
}

OhMy.name


//updateDynamic  - used for update syntax of a method

object MagicBox extends Dynamic {
  private var box = collection.mutable.Map[String, Any]()

  def updateDynamic(name: String)(value: Any) { box(name) = value }
  def selectDynamic(name: String) = box(name)
}

MagicBox.banana = "banana"
MagicBox.banana
MagicBox.unknown  //NOK




///*** Enum Enumeration object 
//(not same as java enumeration interface)


object WeekDay extends Enumeration {
	type WeekDay = Value                            
	val Mon, Tue, Wed, Thu, Fri, Sat, Sun = Value    
}
  
//conversions 
//to int use instance.id
//from int   ENUM_OBJECT(int)
//to String  - instance.toString
//from String - ENUM_OBJECT.withName(string)

WeekDay.values.toList  //res2: List[WeekDay.Value] = List(Mon, Tue, Wed, Thu, Fri, Sat, Sun)

import WeekDay._                                   

def isWorkingDay(d: WeekDay) = ! (d == Sat || d == Sun)

WeekDay.values filter isWorkingDay foreach println 
  
val a = WeekDay.Mon   //a: WeekDay.Value = Mon

a.id    //Convert Enumeration to Int //res28: Int = 0

a match {
      case WeekDay.Mon => println(a)
      case _ =>
      }
//Mon


//Important methods
final  def  apply(x: Int): Value 
    The value of this enumeration with given id x 
    WeekDay(0)  //res21: WeekDay.Value = Mon
final  def  maxId: Int 
    The one higher than the highest integer amongst those used to identify values in this enumeration.
def  toString(): String 
    The name of this enumeration.
def  values: ValueSet 
    The values of this enumeration as a set. ValueSet is private to Enumeration package 
    Note ValueSet can be implicitly converted to a Collection interface
final  def  withName(s: String): Value 
    Return a Value from this Enumeration whose name matches the argument s.
    WeekDay.withName("Mon") //res22: WeekDay.Value = Mon




///*** SAM with  SCala 2.12 
//Almost same, but built with Java8
//hence , has SAM conversion, which preceeds implicits 

trait MySam { def i(): Int }
implicit def convert(fun: () => Int): MySam = new MySam { def i() = 1 }
val sam1: MySam = () => 2 // Uses SAM conversion, not the implicit
sam1.i()                  // Returns 2


//Note that SAM conversion only applies to lambda expressions, 
//not to arbitrary expressions with Scala FunctionN types:

val fun = () => 2     // Type Function0[Int]
val sam2: MySam = fun // Uses implicit conversion
sam2.i()              // Returns 1


//Note that SAM conversion in overloading resolution is always considered
//below code works ok in 2.11, but ambiguous in 2.12
scala> object T {
        def m(f: () => Unit, o: Object) = 0
        def m(r: Runnable, s: String) = 1  //Runnable is SAM with Unit => Unit
      }
defined object T

scala> T.m(() => (), "")
<console>:13: error: ambiguous reference to overloaded definition


///*** Scala symbols 

///Keywords/reserved symbols
//cannot be defined or used as method names. 

// Keywords
<-  // Used on for-comprehensions, to separate pattern from generator
=>  // Used for function types, function literals and import renaming

// Reserved
( )        // Delimit expressions and parameters
[ ]        // Delimit type parameters
{ }        // Delimit blocks
.          // Method call and path separator
// /* */   // Comments
#          // Used in type notations
:          // Type ascription or context bounds
<: >:      // Upper and lower bounds
<%         // View bounds (deprecated)
" """      // Strings
'          // Indicate symbols and characters '
@          // Annotations and variable binding on pattern matching
`          // Denote constant or enable arbitrary identifiers
,          // Parameter separator
;          // Statement separator
_*         // vararg expansion
_          // Many different meanings

//Meaning of _ 
import scala._    // Wild card -- all of Scala is imported
import scala.{ Predef => _, _ } // Exclusion, everything except Predef
def f[M[_]]       // Higher kinded type parameter
def f(m: M[_])    // Existential type
_ + _             // Anonymous function placeholder parameter
filter{ _ % 2}    //first arg 
m _               // Eta expansion of method into method object
m(_)              // Partial function application
_ => 5            // Discarded parameter
case _ =>         // Wild card pattern -- matches anything
f(xs: _*)         // Sequence xs is passed as multiple parameters to f(ys: T*)
case Seq(xs @ _*) // Identifier xs is bound to the whole matched sequence


///Common methods
//Many symbols are simply methods of a class, a trait, or an object
List(1, 2) ++ List(3, 4)
List(1, 2).++(List(3, 4))

//Methods ending in colon (:) bind to the right instead of the left. 
1 :: List(2, 3)
List(2, 3).::(1)

1 +: List(2, 3) :+ 4

//If the name ends in =, 
//look for the method called the same without = 
//This is used for updating 


//Types and objects can also have symbolic names; 
//for types with two type parameters 
//the name can be written between parameters, 
Int <:< Any //same as <:<[Int, Any].



///Methods provided by implicit conversion
//check scala.Predef and implicit scope of the code 




///Syntactic sugars/composition
class Example(arr: Array[Int] = Array.fill(5)(0)) {
  def apply(n: Int) = arr(n)
  def update(n: Int, v: Int) = arr(n) = v
  def a = arr(0); def a_=(v: Int) = arr(0) = v
  def b = arr(1); def b_=(v: Int) = arr(1) = v
  def c = arr(2); def c_=(v: Int) = arr(2) = v
  def d = arr(3); def d_=(v: Int) = arr(3) = v
  def e = arr(4); def e_=(v: Int) = arr(4) = v
  def +(v: Int) = new Example(arr map (_ + v))
  def unapply(n: Int) = if (arr.indices contains n) Some(arr(n)) else None
}

val ex = new Example
println(ex(0))  // means ex.apply(0)
ex(0) = 2       // means ex.update(0, 2)
ex.b = 3        // means ex.b_=(3)
val ex(c) = 2   // calls ex.unapply(2) and assigns result to c, if it's Some; throws MatchError if it's None
ex += 1         // means ex = ex + 1; if Example had a += method, it would be used instead
                //any symbolic method can be combined with = in that way.





