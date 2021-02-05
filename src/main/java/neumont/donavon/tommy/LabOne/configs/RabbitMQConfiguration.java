package neumont.donavon.tommy.LabOne.configs;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {

    private static final String QUEUE = "emailQueue";
    private static final String TOPIC = "emailExchange";

    @Bean
    public Queue queue()
    {
        return new Queue(QUEUE, false);
    }

    @Bean
    public TopicExchange exchange()
    {
        return new TopicExchange(TOPIC);
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange)
    {
        return BindingBuilder.bind(queue).to(exchange).with("email.*");
    }
}
