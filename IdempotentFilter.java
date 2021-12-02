// camel-k: language=java property-file=consumer.properties
// camel-k: dependency=camel:gson

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

import org.apache.camel.processor.idempotent.kafka.KafkaIdempotentRepository;



public class IdempotentFilter extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        //Configure Idempotent Repository
    KafkaIdempotentRepository kafkaIdempotentRepository = new KafkaIdempotentRepository("idempotent-orders", "broker-kafka-bootstrap:9092");
         
        from("kafka:duplicate-topic-data")
        //unmarshal to manipulate message
        .unmarshal().json(JsonLibrary.Gson)
         // Configure Idempotent consumer
           .idempotentConsumer(simple("${body[ORDERNUMBER]}"), kafkaIdempotentRepository)    
          //Process filter orders
        .log("Order Sent: ${body[ORDERNUMBER]}")
        //marshal again to sent kafka broker
        .marshal().json(JsonLibrary.Gson)
        .to("kafka:filtered-orders-data");
    }

}