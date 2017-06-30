package testApp

import java.io.{File, PrintWriter}
import java.text.SimpleDateFormat

import spray.json._
import testApp.ConfigLibrary.Field


/**
  * Created by gifa on 30/06/2017.
  */
object MyApp {

  private[this] val _intType = "INT"
  private[this] val _stringType = "STR"
  private[this] val _dateType = "DTT"

  private[this] val validator = new SimpleDateFormat("yyyy-MM-dd")
  private[this] val formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")

  case class CodeLine(code: String, error: Option[String])
  case class Code(lines: Seq[CodeLine])

  object CodeJsonProtocol extends DefaultJsonProtocol {
    implicit val codeLineFormat: JsonFormat[CodeLine] = jsonFormat2(CodeLine)
    implicit val codeFormat: JsonFormat[Code] = jsonFormat1(Code)
  }

  import CodeJsonProtocol._

  val convertField: Field => Option[String] = f => f.fieldType match {
    case `_intType` => Option(s"val ${f.fieldName}: Int = ${f.fieldValue.toInt}")
    case `_stringType` => Option(s"""val ${f.fieldName}: String = "${f.fieldValue}" """)
    case `_dateType` => Option(s"val ${f.fieldName}: Date = '${formatter.format(validator.parse(f.fieldValue))}'")
    case _ => None
  }

  val generateOutput: Option[String] => CodeLine = _ match {
    case Some(value) => CodeLine(value, None)
    case None => CodeLine("", Some[String]("An error occurred in the field's parsing."))
  }

  def main(args: Array[String]): Unit = {
    println("Loading the app.conf file for the configuration...")
    val allFields = ConfigLibrary.config

    println("Applying transformations...")
    val obj = allFields.fields
      .map(convertField)
      .map(generateOutput)
    val json = Code(obj).toJson.prettyPrint

    println("Saving results to file...")
    new PrintWriter("." + File.separator + "appOutput.json"){write(json); close()}

    println("DONE.")
  }

}