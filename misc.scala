object Misc{
    def wsGet(url:String, xml:Boolean = true ) = {
        import akka.actor._
        import akka.stream.ActorMaterializer
        import play.api.libs.ws._
        import play.api.libs.ws.ahc._
        import play.api.libs.json._

        import scala.concurrent._ 
        import scala.concurrent.duration._

        import play.api.libs.ws.DefaultBodyReadables._
        import play.api.libs.ws.XMLBodyReadables._
        import play.api.libs.ws.JsonBodyReadables._

        import scala.concurrent.ExecutionContext.Implicits._
        implicit val system = ActorSystem()
        //system.registerOnTermination {
        //  System.exit(0)
        //}
        implicit val materializer = ActorMaterializer()

        // Create the standalone WS client
        // no argument defaults to a AhcWSClientConfig created from
        // "AhcWSClientConfigFactory.forConfig(ConfigFactory.load, this.getClass.getClassLoader)"
        val ws = StandaloneAhcWSClient()

        def callXml(wsClient: StandaloneWSClient)(url:String): Future[scala.xml.Elem] = {
            wsClient.url(url).get().map { response =>
              val statusText: String = response.statusText
              response.body[scala.xml.Elem]      
            }
        }

        def callJson(wsClient: StandaloneWSClient)(url:String): Future[JsValue] = {
            wsClient.url(url).get().map { response =>
              val statusText: String = response.statusText
              response.body[JsValue]      
            }
        }
    if(xml) {
        callXml(ws)(url).andThen { case _ => ws.close() }
            .andThen { case _ => system.terminate() 
            } 
      }
    else {
        callJson(ws)(url).andThen { case _ => ws.close() }
            .andThen { case _ => system.terminate() }
         }
    }
    
    import org.apache.spark._ 
    import org.apache.spark.rdd._ 
    import org.apache.spark.sql._
    import org.apache.spark.ml.linalg._
    
    def loadIris(filePath: String)(implicit spark: SparkSession): DataFrame = { 
        val irisData = spark.sparkContext.textFile(filePath).zipWithIndex.filter{case (d,idx) => idx != 0 }.map{case (d,idx) => d}.flatMap { text => 
          text.split("\n").toList.map(_.split(",")).collect { 
            case Array(sepalLength, sepalWidth, petalLength, petalWidth, irisType) => 
              (Vectors.dense(sepalLength.toDouble, sepalWidth.toDouble, petalLength.toDouble, petalWidth.toDouble), irisType) 
          } 
        } 
        spark.createDataFrame(irisData).toDF("features", "Name") 
      }
    
    
    import org.xml.sax._ 
    import java.io._ 
    import java.net._ 


    def get(url: String) = scala.io.Source.fromURL(url, "ISO-8859-1").getLines.toList.mkString

    def downloadURLAndGetAllHref(base:String):Seq[String] = {
        val parserFactory = new org.ccil.cowan.tagsoup.jaxp.SAXFactoryImpl
        val parser = parserFactory.newSAXParser()
        val source = new InputSource( new StringReader( get(base) ) )
        val adapter = new scala.xml.parsing.NoBindingFactoryAdapter
        val xml = adapter.loadXML(source, parser)
        (xml \\ "a" ).map { n => n \@ "href"}.map{ url => if(url.startsWith("http")) url else (new URL(new URL(base),url)).toString}
    }
    
}