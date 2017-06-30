package testApp

import com.typesafe.config.{ConfigFactory, ConfigRenderOptions}
import spray.json._

/**
  * Created by gifa on 30/06/2017.
  */
object ConfigLibrary {

  lazy val config: MyConfig = this.loadConfig()

  case class Field(fieldName: String, fieldValue: String, fieldType: String)
  case class MyConfig(fields: Seq[Field])

  private[this] object ConfigJsonProtocol extends DefaultJsonProtocol {
    implicit val fieldFormat: JsonFormat[Field] = jsonFormat3(Field)
    implicit val myConfigFormat: JsonFormat[MyConfig] = jsonFormat1(MyConfig)
  }

  private[this] def loadConfig(): MyConfig = {
    import ConfigJsonProtocol._
    ConfigFactory.load("app")
      .getObject("config")
      .render(ConfigRenderOptions.concise())
      .parseJson
      .convertTo[MyConfig]
  }

}