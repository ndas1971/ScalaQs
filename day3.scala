trait A {
    def m = "from A"
    def m3:String 
}

class B extends A{
    override def m = "From B"
}

class C extends B with A {
    def a = super.m
    def b = super[A].m 
    override def m = "From C"
}

scala.reflect.runtime.universe.typeOf[C].baseClasses
//List(class C, class B, trait A, class Object, class Any)
//mix 
class D 
val d1 = new D 

val d = new D with A {
        def m3 = "nw"
    }

d.m 

//pre-initialized field 

trait T2{
    val x:Int
    val y:Int 
}

val t = new T2 {
    val x = 2 
    val y = 3
}

val d2 = new D with T2 {
        val x = 2 
        val y = 3 
    }

val d3 = new {
    val x = 2
    val y = 3 
    } with T2 
    
class A extends {
        val x = 2
        val y = 3
    } with T2 {
        val z = 30 
    }

//lazy 
abstract class D {
    val x:String 
    println(x.size)
}

class DD extends D {
    val x = "Hello"
}

class DD2 extends D {
    lazy val x = "Hello"
}
//pre 
class DD3 extends {
        val x = "Hello"
    } with D 
//this 
trait T2 {
    def m = this 
}
class D extends T2 
(new D).m  //this pointer of class 

//this.type 

trait T2 {
    def m2:this.type 
}

class A extends T2 {
    def m2 = this 
    def m3 = "OK"
}
class B extends T2 {
    def m2 = this 
    def m4 = "OK"
}

(new A).m2.m3 
(new B).m2.m4 

//----------------------------------------------
class SomeClass {
    def m = "from SomeClass"
}
trait SomeType {
    def m2 = "From Sometype"
}
trait T2{
    self: SomeClass with SomeType =>    
        def m1 = self.m + self.m2 
}
class D extends SomeClass with SomeType with T2 {

}
//Ducktype 
trait T3{
    self: SomeType with AnyRef{def ok:String} =>    
        def m1 = self.m + self.ok 
}

class D1 {
    def ok = "D1"
}
class D2 extends D1 with SomeType with T3 


///Exception 
try {
    1/0
} catch {
    case ex:Exception => println(ex)
}

///HandsOn - 
1. Create class Request with one method 'get'
2. get - returns 50% of time "Hello" and other time null (use Random)
3. Create list of 10 values after calling request.get 
4. Get the sum of sizes of all string in List 


object Request {
    import scala.util._ 
    val r = new Random 
    def get = if (r.nextInt(10) < 5) "Hello" else  null
}
val ls = (1 to 10).toList.map{ e => Option(Request.get) }
ls.flatMap{o => o.map{s=>s.size}}.reduce(_+_)

//option - monadic - map, filter,...
val o = Option(Request.get) //Some[String], None 
o.map{str => str.toUpperCase}.filter{str=> str.size > 0 }.map{_.size}

scala> o.map{str => str.toUpperCase}.filter{str=> str.size > 0 }.map{_.size}
res30: Option[Int] = Some(5)

scala> res30 match {
     | case Some(x) => x
     | case None => 0
     | }
res31: Int = 5

scala> res30.getOrElse(0)
res32: Int = 5

scala> res30.get
res33: Int = 5

//scala.util.Try 
object Request {
    import scala.util._ 
    val r = new Random 
    def get = if (r.nextInt(10) < 5) "Hello" else  throw new Exception("Boom!!")
}
val ls = (1 to 10).toList.map{ e => Try(Request.get) }
//final sum 
ls.map{t => t.map{s=>s.size}}.collect{case Success(x) => x }.sum 

//Try - monadic - map, filter,...
val o = Try(Request.get) //Success[String], Failure  
o.map{str => str.toUpperCase}.filter{str=> str.size > 0 }.map{_.size}
res39: scala.util.Try[Int] = Success(5)

scala> res39 match {
     | case Success(x) => x
     | case Failure(ex) => 0
     | }
res41: Int = 5

scala> res39.toOption.getOrElse(0)
res42: Int = 5

//variences

scala> class A[+T]  //co varient, immutable , produces T 
defined class A

scala> class B[-T]  //contravarient , consumes T 
defined class B

scala> class D[T]  //mutable, no varient
defined class D


scala> val d1:D[Any] = new D[Any]
d1: D[Any] = D@5bb2b5ae

scala> val d1:A[Any] = new A[AnyVal]
d1: A[Any] = A@75b8b108

scala> val d1:B[AnyVal] = new B[Any]
d1: B[AnyVal] = B@57cf4a5a


//Bound 
class A[T <: AnyVal] 

class B[T >: AnyVal] 

class C[T >: AnyVal <: Any ] 

class C[T >: AnyVal <: Any : Numeric  ] 

//Abstract type 
trait T2[T]{
    val element :T
}

trait T3 {
    type T
    val element:T
}

scala> val t = new T2[Int]{ val element = 2}
t: T2[Int] = $anon$1@5b20b8bf

scala> val t1 = new T3 { type T = Int; val element = 2 }
t1: T3{type T = Int} = $anon$1@33a444c9

scala> t.element
res43: Int = 2

scala> t1.element
res44: t1.T = 2

















/// Excel - Find the year where max diff in Open index 

import org.apache.poi.ss.usermodel._
import java.io.File
import collection.JavaConversions._ // lets you iterate over a java iterable

val f = new File("../data/Nifty-17_Years_Data-V1.xlsx")

val workbook = WorkbookFactory.create(f)
val sheet = workbook.getSheetAt(0) // first sheet
//sheet is a Iterator of Row 

case class Data(Date:java.util.Date,
        Open:Double, High:Double, Low:Double, Close:Double,
        `Day Wise Variation ( points)`:Double,
        `Day Wise Variation ( %) Open to Close`:Double){
    def year = Date.getYear + 1900
    def month = Date.getMonth
}

val t1 = sheet.drop(1).map{row => 
            (row.getCell(0).getDateCellValue, 
                (1 to 6).toList.map { i =>
                        row.getCell(i).getNumericCellValue
                    }
            )
        } //Iterable[(java.util.Date, List[Double])]

val t2 = t1.map{ case (x, List(a,b,c,d,e,f)) => (x,a,b,c,d,e,f)}
val t3 = t2.map{ t => (Data.apply _).tupled(t)} //Iterable[Data]

//now groupby 
t3.map(_.month).toSet 
val t4 = t3.groupBy(_.year). //Map[Int,Iterable[Data]]
  map{case (y, v) => (y, Map("max" -> v.map(_.Open).max, "min"->v.map(_.Open).min))}
  
t4.maxBy{case (y,m) => m("max")-m("min")}


///implicits 


implicit class ABC(x:String){
    def toInt(rd:Int) = Integer.valueOf(x,rd)
}


//typeclass 
def mysum[T : Numeric](lst:List[T]):T = {
    val ev = implicitly[Numeric[T]]
    import ev._ 
    lst.sum 
}
def mysum[T](lst:List[T])(implicit ev:Numeric[T]):T = {
    import ev._ 
    lst.sum 
}


///Future 
import scala.concurrent._ 
import scala.concurrent.duration._ 

import scala.concurrent.ExecutionContext.Implicits.global 

val a = Future {
        Thread.sleep(10)
        "Hello World"
}
//1  Future[String]
val fut = a.map{str => str.toUpperCase}.filter(_.size > 0).map(_.size) //Future[Int]
val res = Await.result(fut, Duration.Inf) 

//2 
fut.foreach(println) 
//3
fut.onSuccess{
    case x:Int => println(x)
}
fut.onComplete{
    case Success(x) => println(x)
    case Failure(ex) => println(ex) 
}
//for 
val t :Future[Int] = for {
        a <- Future { 10 }
        b <- Future{ "Hello"}.mapTo[String]
        c <- Future( a+a) if a > 0

    } yield {
        a*b.size*c 
    }

t.foreach(println) 

import play.api.libs.json._ 
import scala.util._ 

val urlJson = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22nome%2C%20ak%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys"
val urlXml = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22nome%2C%20ak%22)&format=xml&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys"

val futJ = Misc.wsGet(urlJson, false).mapTo[JsObject] 
val futX = Misc.wsGet(urlXml, true).mapTo[scala.xml.Elem] 

//XML  
val p = new scala.xml.PrettyPrinter(80,4)
futX.foreach{xml => println(p.format(xml)) }

val futXX = futX.map{xml => xml \\ "forecast"}.
  map{ xml => xml.map( n => n \@ "high")}
val res = Await.result(futXX, Duration.Inf)  

//JSON 
futJ.foreach{json => println(Json.prettyPrint(json))}
futJ.map{j => j \\ "high"}. //.Future[Seq[play.api.libs.json.JsValue]]
     map{j => j.map(_.as[String])}

val res = Await.result(res31, Duration.Inf)  

  
//XML 
val xml = <a>OK<b>NOK</b></a>
cala> val xml = <a>OK<b>NOK</b></a>
ml: scala.xml.Elem = <a>OK<b>NOK</b></a>

cala> xml \ "a"
es11: scala.xml.NodeSeq = NodeSeq()

cala> xml \ "b"
es12: scala.xml.NodeSeq = NodeSeq(<b>NOK</b>)

cala> xml \\ "b"
es13: scala.xml.NodeSeq = NodeSeq(<b>NOK</b>)

cala> val xml = <a>OK<b name="df">NOK</b></a>
ml: scala.xml.Elem = <a>OK<b name="df">NOK</b></a>

cala> xml \ "b"
es14: scala.xml.NodeSeq = NodeSeq(<b name="df">NOK</b>)

cala> xml \ "b" \@ "name"
es15: String = df

//xml file 
val xml = scala.xml.XML.loadFile("../data/example.xml")

scala> xml \ "country" \ "rank"
res16: scala.xml.NodeSeq = NodeSeq(<rank>1</rank>, <rank>4</rank>, <rank>68</rank>)

scala> xml \ "country" \ "rank" map(_.text)
res17: scala.collection.immutable.Seq[String] = List(1, 4, 68)

scala> xml \ "country" map(_ \@ "name")
res18: scala.collection.immutable.Seq[String] = List(Liechtenstein, Singapore, Panama)

scala> xml \\ "rank"
res19: scala.xml.NodeSeq = NodeSeq(<rank>1</rank>, <rank>4</rank>, <rank>68</rank>)

xml \ "country" map{ n => (n \@ "name") -> (n \ "neighbor").map(ni => ni \@ "name")}
res20: scala.collection.immutable.Seq[(String, scala.collection.immutable.Seq[String])] =
List((Liechtenstein,List(Austria, Switzerland)), (Singapore,List(Malaysia)), (Panama,List(
Costa Rica, Colombia)))


//JSON 
import play.api.libs.json._ 

val f = new java.io.FileInputStream("../data/example.json") 
val json = Json.parse(f) 
f.close() 

scala> json \ 1 \ "empId"
res22: play.api.libs.json.JsLookupResult = JsDefined(20)

scala> (json \ 1 \ "empId").as[Int]
res23: Int = 20

scala> (json \ 0 \ "empId").as[Int]
res24: Int = 1

scala> val jsarray = json.as[JsArray]
jsarray.value.map{_.as[JsObject]}.map{o => (o \ "empId").as[Int]}
res25: IndexedSeq[Int] = ArrayBuffer(1, 20)

//Give all FullNames 
val first = jsarray.value.map{_.as[JsObject]}.
              map{o => (o \ "details" \ "firstName").as[String]}

val last = jsarray.value.map{_.as[JsObject]}.
              map{o => (o \ "details" \ "lastName").as[String]}
              
first.zip(last).map{case (f,l) => f+l}


//JSON 
futJ.foreach{json => println(Json.prettyPrint(json))}
futJ.map{j => j \\ "high"}. //.Future[Seq[play.api.libs.json.JsValue]]
     map{j => j.map(_.as[String])}

val res = Await.result(res31, Duration.Inf)  
/*  
"forecast" : [ {
  "code" : "26",
  "date" : "05 Sep 2018",
  "day" : "Wed",
  "high" : "50",
  "low" : "48",
  "text" : "Cloudy"
}, {
  "code" : "28",
  "date" : "06 Sep 2018",
  "day" : "Thu",
  "high" : "54",
  "low" : "46",
  "text" : "Mostly Cloudy"
}, {

*/

//Akka Actor 

import akka.actor._ 
import akka.util._ 
import scala.concurrent.duration._ 
import scala.concurrent._ 
import akka.pattern._ 
import akka.event._ 
import scala.util._ 

import scala.concurrent.ExecutionContext.Implicits.global 

class MyActor/*(a:ActorRef) */extends Actor with ActorLogging{
    log.info(self.path.toString) 
    def receive = {
        case "test" =>
            log.info("handling test")
            sender() ! "Hello World"
        case _ => 
            log.info("Sorry I can not help")
    }
}
val system = ActorSystem("experiment")
val aref = system.actorOf(Props(new MyActor(/* args*/), "test-actor")
aref ! "test"

//Convert to future 
implicit val timeout = Timeout(5.seconds)
(aref ? "test").mapTo[String].foreach(println)

system.terminate


///Ping-Pong 
class MyActor(val max:Int) extends Actor with ActorLogging{    
    var times = 0 
    def receive = {
        case "doit" =>
                self ! "ping"
        case "ping" => 
            times += 1 
            if (times >= max){
                //context.stop(self)
                self ! PoisonPill
            }
            println("pong")
            self ! "pong"
        case "pong" =>
            Thread.sleep(1000)
            println("ping")
            self ! "ping"
    }
}
val system = ActorSystem("experiment")
val aref = system.actorOf(Props(new MyActor(10)), "test-actor")
aref ! "doit"
system.terminate

