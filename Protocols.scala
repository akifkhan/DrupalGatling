package drupal
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._
import io.gatling.http.Headers.Names._

object Protocols {

    val webheads = System.getProperty("webheads", "http://127.0.0.1").split(",")

    //val httpProtocol = http.baseURLs(webheads(0), webheads(1), webheads.drop(2):_*)
    val httpProtocol = http
        .acceptCharsetHeader("ISO-8859-1,utf-8;q=0.7,*;q=0.7")
        .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
        .acceptEncodingHeader("gzip, deflate")
        .acceptLanguageHeader("en-us;q=0.9,en;q=0.3")
        .disableFollowRedirect
}
