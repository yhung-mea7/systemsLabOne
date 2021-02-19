package neumont.donavon.tommy.LabOne.configs;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {

    private static final String QUEUE = "emailQueue";
    private static final String TOPIC = "emailExchange";
    private static final String serviceQueue = "serviceQueue";

    @Bean
    public Queue queue()
    {
        return new Queue(QUEUE, true);
    }

    @Bean
    public Queue serviceQueue()
    {
        return new Queue(serviceQueue, true);
    }

    @Bean
    public TopicExchange exchange()
    {
        return new TopicExchange(TOPIC);
    }

    @Bean
    public Binding binding(TopicExchange exchange)
    {
        return BindingBuilder.bind(queue()).to(exchange).with("email.*");
    }

    @Bean
    public Binding serviceBinding(TopicExchange exchange)
    {
        return BindingBuilder.bind(serviceQueue()).to(exchange).with("service.*");
    }

}
