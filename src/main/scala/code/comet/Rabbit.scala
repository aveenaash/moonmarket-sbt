package code.comet//zazzercode

import net.liftmodules.amqp._
import com.rabbitmq.client._

import zazzercode.Constants

object Rabbit {

  val factory = new ConnectionFactory {
    import ConnectionFactory._
    setHost(Constants.RabbitServerHost)
    setPort(DEFAULT_AMQP_PORT)
  }

  val exchange = "lift.chat"
  val routing = ""
  val durable = true
  val autoAck = false

  object RemoteSender extends AMQPSender[String](factory, exchange, routing) {
    def configure(channel: Channel) =
      channel.exchangeDeclare(exchange, "fanout", durable)
  }

  object RemoteReceiver extends AMQPDispatcher[String](factory) {
    def configure(channel: Channel) = {

      channel.exchangeDeclare(exchange, "fanout", durable)
      val queueName = channel.queueDeclare().getQueue()

      channel.queueBind(queueName, exchange, routing)

      channel.basicConsume(queueName, autoAck,
        new SerializedConsumer(channel, this) )
    }
  }

}
