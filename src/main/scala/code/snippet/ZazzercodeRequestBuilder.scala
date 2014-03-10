package zazzercode

import org.elasticsearch.node.NodeBuilder._
import org.elasticsearch.index.query.QueryBuilders._
import collection.JavaConversions._

/**
  * @author : prayagupd
  */

object ZazzercodeRequestBuilder {

  def executeQuery(query_: String) : String {
    val node = nodeBuilder().client(true).node()
    val client = node.client()

    val query = queryString(query_)
    val response = client
      .prepareSearch("gccount")
      .setTypes("Customer")
      .setQuery(query)
      .execute()
      .actionGet()

    val hits = response.getHits

    println("Found %d hits for query '%s'".format(hits.getTotalHits, query_))
    println()

    hits.getHits.foreach(hit =>
      println("* %s".format(hit.sourceAsMap()("text")))
    )

    client.close()
    node.close()

    return hits.getTotalHits+""
  }

}
