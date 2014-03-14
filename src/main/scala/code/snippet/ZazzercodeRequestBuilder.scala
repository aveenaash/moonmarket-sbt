package zazzercode

import org.elasticsearch.client.Client
import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.common.settings.ImmutableSettings
import org.elasticsearch.common.settings.Settings
import org.elasticsearch.common.transport.InetSocketTransportAddress
import org.elasticsearch.node.NodeBuilder._
import org.elasticsearch.index.query.QueryBuilders
import org.elasticsearch.index.query.QueryBuilder
import org.elasticsearch.index.query.QueryBuilders._
import collection.JavaConversions._

/**
  * @author : prayagupd
  */

class ZazzercodeRequestBuilder extends AbstractRequestBuilder {

  def executeQuery(q : String) : String = {
    
    val client = getClient()
    val termQueryBuilder     = QueryBuilders.termQuery("firstName", q)
    val matchAllQueryBuilder = QueryBuilders.matchAllQuery()
    println("Query => " + matchAllQueryBuilder)

    val response = client
      .prepareSearch("gccount") //TODO define a Constants class
      .setTypes("Customer")
      .setQuery(termQueryBuilder)
      .execute()
      .actionGet()

    val hits = response.getHits
    
    
    println("Found %d hits for query '%s'".format(hits.getTotalHits, q))
    println()

    hits.getHits.foreach(hit =>
      println("* %s".format(hit.sourceAsMap()("firstName")))
    )

    client.close()

    return hits.getTotalHits+""
  }

  def execute() : String = {
    return "purchase successful"
  }

  //TODO move this method to a generic class, Let's call it ElasticsearchManager
  //TODO make a DSL for serverconf (EsServer.scala)
  def getClient() : TransportClient = {
    //create Es Client
    val settings = ImmutableSettings.settingsBuilder().put("cluster.name", "moonmarket").build();
    val transportClient = new TransportClient(settings);
    transportClient.addTransportAddress(new InetSocketTransportAddress("localhost", 9300));
    return transportClient;
  }
}
