object Twice{
    def apply(x:Int) = x*x 
    def unapply(x:Int) = Some(x/2)
}
case class Person(first:String, sec:String)
case object Request 

def extract(o:Any) = o match {
    case 0 | 1 | 2 => s"alternate"
    case Request => s"Request"
    case Twice(x) => s"Twice $x"
    case List(1,_,_) => s"List three"
    case a @ List(x,y @ _*) => s"$a $x $y"
    case Person("X", s ) => s"Person $s"
    case (x,y) if x == y => s"tuple same"
    case (x,y)  => "tuple"
    case x:String => "string"
    case x: Array[String] => "array string"
    case l : Seq[_]  => "seq"
    case ((x,y),(z,a),b)  => s"nested"
    case _ => "none"
}

def mysum(x:List[Int]):Int = if (x.isEmpty) 0 else x(0)+mysum(x.tail)

def mysum2(x:List[Int]): Int = x match {
        case h :: t => h + mysum2(t)
        case Nil => 0
}

@annotation.tailrec
def mysum3(x:List[Int], acc:Int=0):Int = x match {
    case h::t => mysum3(t, h+acc)
    case Nil => acc 
}

def mysum4(x:List[Int]) = {
    def _sum(y:List[Int], acc:Int):Int = y match {
        case h::t => _sum(t, h+acc)
        case Nil => acc 
    }
    _sum(x,0)
}

///HandsOn: Finding max for an list 
def max(ints: List[Int]): Int = { 
    @tailrec
    def maxAccum(ints: List[Int], theMax: Int): Int = {
      ints match {
        case Nil => theMax
        case x :: tail => maxAccum(tail, if (x > theMax) x else theMax)
      }
    }
    maxAccum(ints, ints.head)
  }
  
//FP

def add(x:Int, y:Int) = x + y

add _ 

val add1 = (x:Int, y:Int) => x+y 

type AddType = (Int,Int) => Int 

val add1 = (x:Int) => (y:Int) => x+y 
add1(2)(3)

scala> add _
res21: (Int, Int) => Int = <function2>

scala> add(2, _:Int)
res19: Int => Int = <function1>

scala> (add _).tupled
res22: ((Int, Int)) => Int = <function1>

scala> (add _).tupled( (2,3) )
res23: Int = 5

scala> val t = (1,2)
t: (Int, Int) = (1,2)

scala> case class Two(x:Int, y:Int)
defined class Two

scala> Two(t._1, t._2)
res26: Two = Two(1,2)

scala> (Two.apply _).tupled(t)
res27: Two = Two(1,2)


val dou = (x:Int) => x*x 
val ai = (x:Int) => x+5

(dou andThen ai )(5)
(dou compose ai)(5)

val pf:PartialFunction[Int,Int] = {case x:Int if x!=0 => 42/x}


scala> pf(0)
scala.MatchError: 0 (of class java.lang.Integer)
  at scala.PartialFunction$$anon$1.apply(PartialFunction.scala:253)
  at scala.PartialFunction$$anon$1.apply(PartialFunction.scala:251)
  at $anonfun$1.applyOrElse(<console>:12)
  at $anonfun$1.applyOrElse(<console>:12)
  at scala.runtime.AbstractPartialFunction$mcII$sp.apply$mcII$sp(AbstractPartialFunction.s
cala:36)
  ... 40 elided

scala> pf(1)
res29: Int = 42

scala> pf.isDefinedAt(0)
res30: Boolean = false

scala> pf.isDefinedAt(1)
res31: Boolean = true

val pf1:PartialFunction[Int,Int] = {case x:Int if x == 0 => 0}

scala> (pf orElse pf1)(0)
res33: Int = 0

scala> val f:Function[Int,Int] = pf orElse pf1
f: Function[Int,Int] = <function1>







//FP with Function 
___ SHOW DESCRIPTION  __

val ls = List(1,2,3)
val map = List("OK"->1, "Nok" -> 2) 


//foreach 
val p = (x:Int) => println(x) 

ls.foreach(p) 
ls.foreach(  println(_) )

println _ 
ls.foreach(println _ ) 
ls.foreach(println)
ls foreach println

map foreach println 
map foreach ( (x,y) => println(x)) //Nok 
map.foreach{case(x,y) => println(x)}

scala> map.foreach{println(_._1)}
<console>:13: error: missing parameter type for expanded function ((x$1) => x$1._1)
       map.foreach{println(_._1)}
                           ^

scala> map.foreach{t:Tuple2[String,Int] => println(t._1)}
OK

//in terms of for loop 
for{
    i <- ls
    j = i*i 
    k <- i to j if j %2 == 0
}
{
    println(s"$i $j $k")
}


//Map 
ls.map(x => x*x) 
map.map{case (x,y) => x+y}

//in terms of for loop 
val out = for{
        i <- ls    
    } yield {
        i*i
    }
    

//flatMap 
scala> ls.flatMap(x => List(x,x*x))
res47: List[Int] = List(1, 1, 2, 4, 3, 9)


scala> map.flatMap{case (x,y) => Map(x->y*y)}
res48: List[(String, Int)] = List((OK,1), (Nok,4))

scala> map.flatMap{case (x,y) => Map(x->y*y)}.toMap
res49: scala.collection.immutable.Map[String,Int] = Map(OK -> 1, Nok -> 4)

//with for loops 
val first = List(1, 2)
val next = List(8, 9)
val last = List("ab", "cde", "fghi")

for {
  i <- first  if i %2 ==0   //flatmap with filter 
  j <- next      //flatmap
  k <- last      //map 
} yield {
    val len = k.size 
    i * j * len 
}

//Equivalent to Monadic form
first withFilter (t => t%2 == 0) flatMap {
  i => next flatMap {
    j => last map {
      k => 
        val len = k.size 
        i * j * len 
      
    }
  }
}


//filter 
ls.filter(x => x % 2 == 0)
map.filter{case (x,y) => y%2 == 0}


//collect 
ls.collect{ case x:Int if x%2==0 => x*x }
map.collect{case (x,y) if y%2 == 0  => (x,y*y)}



//reduce 
ls.reduce{ (r,t) => r+t }
map.reduce{(r,t) => (r._1+t._1, r._2+t._2) }

//foldLeft
ls.foldLeft(16){ (r,t) => r+t }
map.foldLeft( ("pre",10) ){(r,t) => (r._1+t._1, r._2+t._2) }

map.foldLeft( Map[String,Int]() ){ (r,t) => r ++ Map(t._1 -> t._2*t._2)}



///HandsOn - read file with deliminator of any number of \n 
val lines = scala.io.Source.fromFile("xy.txt").getLines.toList
//List(qbcd, abcd, abcd, "", cdfe, cdef, "", "", "", abcd, abcs, "", "", abcd)

//List(List(qbcd, abcd, abcd), List(cdfe, cdef), List(abcd, abcs), List(abcd))


//xy.txt:
qbcd
abcd
abcd

cdfe
cdef



abcd
abcs


abcd

























//answer 
val lines = scala.io.Source.fromFile("xy.txt").getLines.toList

import collection.mutable.ListBuffer 

def proc(r:ListBuffer[ListBuffer[String]], e:String) =
    {if (! e.isEmpty) r.last.append(e) else if(e.isEmpty && r.last.isEmpty) r else r.append(ListBuffer[String]());r}
    
lines.foldLeft( ListBuffer[ListBuffer[String]](ListBuffer[String]()))(proc _)


//read file with deliminator of any number of \n 
val lns = scala.io.Source.fromFile("xy.txt").getLines.toList

def pr( r:collection.mutable.ListBuffer[collection.mutable.ListBuffer[String]], e:String)=
    {if (! e.isEmpty ) r.last.append(e) else if(e.isEmpty && r.last.isEmpty) r  else r.append(collection.mutable.ListBuffer[String]());r}

scala> lns.foldLeft(ListBuffer[ListBuffer[String]](ListBuffer[String]()))(pr _)
res46: scala.collection.mutable.ListBuffer[scala.collection.mutable.ListBuffer[String]] = ListBuffer(ListBuffer(qbcd, abcd, abcd), 
ListBuffer(cdfe, cdef), ListBuffer(abcd, abcs), ListBuffer(abcd))





//sortBy 
ls.sorted 
map.sorted 

ls.sortBy(x => -x) 
map.toList.sortBy{case(x,y) => y}






//HandsOn - Given a Dir, find out fileName of max size 

import java.io.File 

def getMax(dir:File) = {
    def getFiles(dirs:Array[File], acc:Array[File]=Array.empty[File]):Array[File] = {
        val files = dirs.flatMap{ f => f.listFiles.filter(_.isFile)}
        val subs = dirs.flatMap{ f => f.listFiles.filter(_.isDirectory)}
        if(subs.isEmpty) acc ++ files else getFiles(subs, acc ++ files)
    }
    val allFiles = getFiles(Array(dir))
    val map = allFiles.map{f => (f, f.length)}.toMap 
    val smap = map.toList.sortBy{case(x,y) => y}
    smap.last 
  }







//maxBy 
ls.maxBy(identity) 
map.toList.maxBy{case(x,y) => y}



//groupBy 
ls.groupBy( x => x % 2 )
map.groupBy{case (x,y) => x.size}

















//Iris.CSV 
val lines = scala.io.Source.fromFile("../data/iris.csv").getLines.toList 
val lines2 = lines.drop(1).map{_.split(",")}.map{arr => (arr.slice(0,4).map(_.toDouble),arr.last)}.map{case(Array(a,b,c,d),e)=>(a,b,c,d,e)}

case class Row(sl:Double, sw:Double, pl:Double, pw:Double, name:String)

val rows = lines2.map{t => (Row.apply _).tupled(t)}

//get unique value of Name 
val un = rows.map(_.name).toSet 
rows.groupBy(_.name)

//get max of sl of each Name 
rows.groupBy(_.name).map{case(n,lst) => (n -> lst.map(_.sl).max) }
rows.groupBy(_.name).map{case(n,lst) => (n -> Map("sl"->lst.map(_.sl).max, "sw" -> lst.map(_.sw).max)) }


//Dump into DB and check above max 


Class.forName("org.sqlite.JDBC")
import java.sql._


implicit val con = DriverManager.getConnection("jdbc:sqlite:iris2.db")
val stmt = con.createStatement()
stmt.executeUpdate("drop table if exists iris")
stmt.executeUpdate("create table iris (sl double, sw double, pl double, pw double, name String)")
//con.commit() //autocommit 


import anorm._ 

case class Row(sl:Double, sw:Double, pl:Double, pw:Double, name:String)
object Row{
    implicit def to  = Macro.toParameters[Row]
    val parser = Macro.namedParser[Row]

}
val rows = lines2.map{t => (Row.apply _).tupled(t)}
rows.foreach(row => SQL("insert into iris values({sl},{sw},{pl},{pw},{name})").bind(row).executeInsert())



import anorm.SqlParser._ 
SQL("select * from iris").as{ Row.parser.*}
SQL("select name, max(sl),max(sw) from iris group by name").as{ (str(1)~ double(2) ~ double(3)).map {
    case n ~ m1 ~ m2 => (n,m1,m2)
}.*}
SQL("select name, max(sl),max(sw) from iris group by name").as{str(1)~double(2)~double(3) map flatten *}


///EXCEL sheet 



import org.apache.poi.ss.usermodel._ 
import java.io.File 
import collection.JavaConversions._ 

val f = new File("../data/Nifty-17_Years_Data-V1.xlsx")
val wb = WorkbookFactory.create(f)
val sh = wb.getSheetAt(0) 
//sheet contains rows, each row contains cell, cell has .getDateCellValue, .getNumericCellValue

sh.drop(1).map(row => (row.getCell(0).getDateCellValue, row.getCell(1).getNumericCellValue))
val lines = sh.drop(1).map(row => (row.getCell(0).getDateCellValue, (1 to 6).map{row.getCell(_).getNumericCellValue}.toList))

lines.map{case(a,List(b,c,d,e,f,g)) =>(a,b,c,d,e,f,g)}

case class Data(Date:java.util.Date,Open:Double,High:Double,Low:Double,Close:Double, variation:Double,`Day Wise Variation    Open to Close`:Double) {
    def year = Date.getYear+1900
    def month = Date.getMonth 
}

val rows = lines.map{case(a,List(b,c,d,e,f,g)) =>(a,b,c,d,e,f,g)}.map{t => (Data.apply _).tupled(t)}
//find max diff in year 
rows.groupBy(_.year).map{case(y,lst) => (y, lst.map(_.Open).max - lst.map(_.Open).min) }.maxBy{case(y,d) => d}




//Stream 

def fib(a:BigInt, b:BigInt):Stream[BigInt] = a #:: fib(b, a+b)
fib(0,1).take(10).toList

lazy val fib2:Stream[BigInt] = BigInt(0) #:: BigInt(1) #:: fib2.zip(fib2.tail).map{ case(a,b) => a+b}

Stream.iterate( (0,1) ) {case (a,b) => (b, a+b)}
res29.take(100).toList.map{case (x,y) => x}



//HandsOn  the value where  cos(x) == x .







import scala.math._
val cosines = Stream.iterate(1.0)(cos)
def pairsOf(xs:Stream[Double]) = xs zip xs.tail   //Iterator does not have tail
val result = for { (x,y) <- pairsOf cosines if x == y } yield x 
result.head


//Iterator 

class Fib extends Iterator[BigInt]{
    var (a,b) = (0:BigInt, 1:BigInt)
    def hasNext = true 
    def next = {
        val tmp = a; a=b;b=tmp+b;
        b
    }

}
(new Fib).take(10).toList 

Iterator.iterate( (0,1) ){case (x,y) =>(y,x+y)}
res33.take(10).map{case (x,y) => x}.toList

//Lazy 

(0 to 1000).view.filter(_ % 2 == 0).map(_*2).scanLeft(0)( (r,t) => r+t )

scala> (0 to 1000).view.filter(_ % 2 == 0).map(_*2).scanLeft(0)( (r,t) => r+t ).take(10).force

//Par 
(0 to 10).par.foreach(println)

(0 to 10).par.map(_*2).seq.foreach(println)



//Hands On - find out max n where factorial(n) < Long.MaxValue 
def factorial(n:Int) = (1 to n).foldLeft(BigInt(1))(_*_)
(1 to 100) takeWhile (factorial(_) <= Long.MaxValue) last





///HANDSON-  Create a csv file 
name,age,salary
ABC,2,3.0
XYZ,2,4.0
ABCD,10,9.0
AXYZ,10,18.0
Read this file and convert to immutable.List[Person]
where Person is case class with attributes name,age,salary 
Then find  age vs sum of salary (ie groupby on age)


///HANDSON-  
Write a case class Complex(not generic) with handling of + and comparison operation 
Complex has two args real, img and + adds real part and imaginary part of this with other
Then Create a List of few Complex numbers where real/img comes are Random 
and do sorting on them in general and by img part 
Then create a List of few Complex and do an operation which sums all real parts and all imaginary parts 
























ANS:

case class Person(name:String,age:Int,salary:Double)
val lines = scala.io.Source.fromFile("../../QA/csv.txt").getLines.drop(1).map(_.trim).map(_.split(",")).map{case Array(x,y,z) =>(x,y.toInt,z.toDouble) }
val answer = lines.map{r => (Person.apply _).tupled(r)}.toList
answer.groupBy(_.age).map{case(k,v) => k -> v.map(_.salary).sum}


ANS:

case class Complex(re:Double, im:Double) extends Ordered[Complex]{
    def +(o:Complex) = Complex(this.re+o.re, this.im+o.im)
    def compare(o:Complex) = implicitly[Ordering[Tuple2[Double, Double]]].compare((this.re, this.im), (o.re,o.im))
    override def toString = s"""${this.re} ${if(this.im < 0)'-' else '+'} ${this.im.abs}j"""
}

//Then Create a List of few Complex numbers and do sorting on them 
import scala.util._ 
val rnd = Random
val lst = (1 to 10).toList.map{ _ => Complex(rnd.nextInt(10),rnd.nextInt(10))}
lst.sorted
lst.sortBy(_.im)
//Then create a List of few Numbers and do an operation which sum's all real parts and all imaginary parts 
lst.fold(Complex(0,0)){(r,e) => r+e}
//Or tuple way 
lst.map( Complex.unapply(_).get).foldLeft( (0.0,0.0) ){ case ( (r,i), (er,ei) ) => (r+er, i+ei) }

