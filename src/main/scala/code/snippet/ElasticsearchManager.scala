package zazzercode

import net.liftweb._
import common._
import http._
import rest._
import net.liftweb.json.JsonDSL._
import net.liftweb.json.JsonParser._
import net.liftweb.json.JsonAST._
import net.liftweb.http.js._
import net.liftweb.http.js.JE.JsObj
import net.liftweb.http.js.JE.JsArray

import com.fasterxml.jackson.databind._
import java.util
import java.io.StringWriter

//import json.{ Js, JsObject }
import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.common.settings.ImmutableSettings
import org.elasticsearch.common.settings.Settings
import org.elasticsearch.common.transport.InetSocketTransportAddress

import org.elasticsearch.action.index.IndexRequestBuilder
import org.elasticsearch.common.xcontent.XContentBuilder
import org.elasticsearch.common.xcontent.XContentFactory
import org.elasticsearch.action.index.IndexResponse;

object ElasticsearchManager {

        //TODO make a DSL for serverconf (EsServer.scala)
  	def getClient() : TransportClient = {
    		//create Es Client
    		val settings = ImmutableSettings.settingsBuilder().put("cluster.name", EsServer.ClusterName).build();
    		val transportClient = new TransportClient(settings);
    		transportClient addTransportAddress(new InetSocketTransportAddress(EsServer.ServerHostName, Integer.valueOf(EsServer.ServerPort)));
    		return transportClient;
  	}
        
       def  insertDocuments(documentMap : Map[String, String], index : String, type_ : String) = {

        val key  = "1"
        val client = getClient()
        val json = XContentFactory.jsonBuilder();
        json.startObject();

	    documentMap.foreach { keyValue =>
            	val fieldName  = keyValue._1;
            	val fieldValue = keyValue._2;

            	json.field(fieldName, fieldValue)
            }

            json.endObject()
            val indexRequestBuilder = client.prepareIndex(index, type_, String.valueOf(key))
            println("inserted the index:" + String.valueOf(key))
            indexRequestBuilder.setCreate(false).setSource(json);
            indexRequestBuilder.execute().actionGet()

        //need to refresh indices to reflect the insertion made
        client.admin().indices().prepareRefresh().execute().actionGet()
        true
    }


    def insertResponseRecord(requestdoc_ : Map[String,String],responsedoc_ : Map[String,String], index : String , type_ : String) = {
      var client=getClient()

      val json = XContentFactory.jsonBuilder();
      json.startObject();

      requestdoc_.foreach { keyValue =>
        val fieldName  = keyValue._1;
        val fieldValue = keyValue._2;

        json.field(fieldName, fieldValue)
      }
        json.startObject("responses")
            responsedoc_.foreach { keyValue =>
              val fieldName  = keyValue._1;
              val fieldValue = keyValue._2;

              json.field(fieldName, fieldValue)
              }
              json.endObject()
      json.endObject()


//      val objectmapper: ObjectMapper = new ObjectMapper()
//      objectmapper.configure(SerializationFeature.INDENT_OUTPUT, true)

//      var stringEmp = new StringWriter();
//      objectmapper.writeValue(stringEmp, hulaki);
//      println("Employee JSON is\n"+stringEmp.toString);

      //var jsonBuilder : String = objectmapper.writeValueAsString(jsonmap)
     // println("JSON STRING ; "+jsonBuilder);

//      val indexRequestBuilder = client.prepareIndex(index, type_)
//      indexRequestBuilder.setCreate(false).setSource(json);
//      indexRequestBuilder.execute().actionGet()

      println("builder:" + json.bytes().toUtf8().toString())


      //need to refresh indices to reflect the insertion made
//      client.admin().indices().prepareRefresh().execute().actionGet()
      true


    }
}
