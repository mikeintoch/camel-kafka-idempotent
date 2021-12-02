// camel-k: language=java property-file=consumer.properties
// camel-k: dependency=camel:gson

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;



public class DataConsumer extends RouteBuilder {

    @Override
    public void configure() throws Exception {
         
        from("kafka:filtered-orders-data")
        .unmarshal().json(JsonLibrary.Gson)           
          //Process filter orders
        .log("Order Received: ${body[ORDERNUMBER]}");
    }

}