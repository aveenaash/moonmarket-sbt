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
import java.lang.Object
import java.util.UUID

//import json.{ Js, JsObject }
import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.common.settings.ImmutableSettings
import org.elasticsearch.common.settings.Settings
import org.elasticsearch.common.transport.InetSocketTransportAddress

import org.elasticsearch.action.index.IndexRequestBuilder
import org.elasticsearch.common.xcontent.XContentBuilder
import org.elasticsearch.common.xcontent.XContentFactory
import org.elasticsearch.action.index.IndexResponse
import org.elasticsearch.action.update.UpdateRequestBuilder
import org.elasticsearch.index.query.QueryBuilders
import org.elasticsearch.search.facet.FacetBuilders
import org.elasticsearch.search.SearchHit
import org.elasticsearch.search.facet.terms.TermsFacet

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


  def getId(index : String ,request : String) : String = {
      val termQueryBuilder= QueryBuilders.termQuery("request",request)
      println("Request => " + request)

      val termsFacet = FacetBuilders.termsFacet("tag").field("requestId");

      val searchResponse =getClient().prepareSearch(index)
                                     .setQuery(termQueryBuilder)
                                     .addFacet(termsFacet)
                                     .execute().actionGet()

    var f  =  searchResponse.getFacets().facetsAsMap().get("tag");
    var fterms : TermsFacet = f.asInstanceOf[TermsFacet]
    var result : Array[SearchHit] = searchResponse.getHits().getHits()
    var id : String  =""
    if(result.length>0) {
      if(fterms.getTotalCount()>0) {
        id = result(0).getId()
        println(id)
      }
    }
    println(id)
    return id
   }

    def insertResponseRecord(requestdoc_ : Map[String,String],
	                     responsedoc_ : Map[String,String], 
			     index : String, 
			     type_ : String) = {
      var client = getClient()
      var id     = getId(index,requestdoc_.get("request").mkString)
      println(id)
      if (id=="") {
	      println("Insert => "+id)
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

        val indexRequestBuilder = client.prepareIndex(index, type_)
        indexRequestBuilder.setCreate(false).setSource(json);
        indexRequestBuilder.execute().actionGet()

      } else {
        val updateObject = new util.HashMap[String , Object]
        var map = new util.HashMap[String, String]
        map.put("responseId", "1");
        map.put("response", responsedoc_.get("response").toString());

//        var reqids : Array[util.Map[String, String]] = new Array[util.Map[String, String]](2)
//        reqids(0) = map
//        reqids(1) = map_
        println("[iofo] : Update : " + id)
        updateObject.put("responses", map);

        val updateRequestBuilder =client.prepareUpdate(index, type_, id)
        updateRequestBuilder.setScript("ctx._source.responses += responses;")
          .setScriptParams(updateObject).execute().actionGet();

      }
     //println("builder => " + updateRequestBuilder.toString())

      //need to refresh indices to reflect the insertion made
      client.admin().indices().prepareRefresh().execute().actionGet()
      true
    }
}
