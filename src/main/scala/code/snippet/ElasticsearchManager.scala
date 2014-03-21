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

//import json.{ Js, JsObject }
import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.common.settings.ImmutableSettings
import org.elasticsearch.common.settings.Settings
import org.elasticsearch.common.transport.InetSocketTransportAddress

import org.elasticsearch.action.index.IndexRequestBuilder
import org.elasticsearch.common.xcontent.XContentBuilder
import org.elasticsearch.common.xcontent.XContentFactory

object ElasticsearchManager {

        //TODO make a DSL for serverconf (EsServer.scala)
  	def getClient() : TransportClient = {
    		//create Es Client
    		val settings = ImmutableSettings.settingsBuilder().put("cluster.name", EsServer.ClusterName).build();
    		val transportClient = new TransportClient(settings);
    		transportClient addTransportAddress(new InetSocketTransportAddress(EsServer.ServerHostName, Integer.valueOf(EsServer.ServerPort)));
    		return transportClient;
  	}
        
       def  insertDocuments(documentMap : Map[String, String], 
	                    index : String, 
		            type_ : String) = {
        
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
}
