package zazzercode
import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.common.settings.ImmutableSettings
import org.elasticsearch.common.settings.Settings
import org.elasticsearch.common.transport.InetSocketTransportAddress

object ElasticsearchManager {

        //TODO make a DSL for serverconf (EsServer.scala)
  	def getClient() : TransportClient = {
    		//create Es Client
    		val settings = ImmutableSettings.settingsBuilder().put("cluster.name", "moonmarket").build();
    		val transportClient = new TransportClient(settings);
    		transportClient.addTransportAddress(new InetSocketTransportAddress("localhost", 9300));
    		return transportClient;
  	}

}
