package lk.epicgreen.erp.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lk.epicgreen.erp.common.constants.AppConstants;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ configuration
 * Configures message queues, exchanges, and bindings for async messaging
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Configuration
public class RabbitMQConfig {
    
    @Value("${spring.rabbitmq.host:localhost}")
    private String rabbitHost;
    
    @Value("${spring.rabbitmq.port:5672}")
    private int rabbitPort;
    
    @Value("${spring.rabbitmq.username:guest}")
    private String rabbitUsername;
    
    @Value("${spring.rabbitmq.password:guest}")
    private String rabbitPassword;
    
    @Value("${spring.rabbitmq.virtual-host:/}")
    private String rabbitVirtualHost;
    
    // Exchange names
    public static final String MAIN_EXCHANGE = "epicgreen.exchange";
    
    /**
     * Connection factory
     */
    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(rabbitHost);
        connectionFactory.setPort(rabbitPort);
        connectionFactory.setUsername(rabbitUsername);
        connectionFactory.setPassword(rabbitPassword);
        connectionFactory.setVirtualHost(rabbitVirtualHost);
        
        // Connection settings
        connectionFactory.setConnectionTimeout(30000);
        connectionFactory.setRequestedHeartBeat(60);
        
        return connectionFactory;
    }
    
    /**
     * RabbitMQ template
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, 
                                        MessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        rabbitTemplate.setExchange(MAIN_EXCHANGE);
        return rabbitTemplate;
    }
    
    /**
     * Message converter for JSON serialization
     */
    @Bean
    public MessageConverter jsonMessageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }
    
    /**
     * Rabbit listener container factory
     */
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            MessageConverter messageConverter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter);
        factory.setConcurrentConsumers(3);
        factory.setMaxConcurrentConsumers(10);
        factory.setPrefetchCount(10);
        return factory;
    }
    
    // ==================== Exchanges ====================
    
    /**
     * Main topic exchange
     */
    @Bean
    public TopicExchange mainExchange() {
        return new TopicExchange(MAIN_EXCHANGE, true, false);
    }
    
    // ==================== Queues ====================
    
    /**
     * Email queue
     */
    @Bean
    public Queue emailQueue() {
        return QueueBuilder.durable(AppConstants.QUEUE_EMAIL)
            .withArgument("x-dead-letter-exchange", MAIN_EXCHANGE + ".dlx")
            .withArgument("x-dead-letter-routing-key", "dlx.email")
            .build();
    }
    
    /**
     * SMS queue
     */
    @Bean
    public Queue smsQueue() {
        return QueueBuilder.durable(AppConstants.QUEUE_SMS)
            .withArgument("x-dead-letter-exchange", MAIN_EXCHANGE + ".dlx")
            .withArgument("x-dead-letter-routing-key", "dlx.sms")
            .build();
    }
    
    /**
     * Notification queue
     */
    @Bean
    public Queue notificationQueue() {
        return QueueBuilder.durable(AppConstants.QUEUE_NOTIFICATION)
            .withArgument("x-dead-letter-exchange", MAIN_EXCHANGE + ".dlx")
            .withArgument("x-dead-letter-routing-key", "dlx.notification")
            .build();
    }
    
    /**
     * Report generation queue
     */
    @Bean
    public Queue reportQueue() {
        return QueueBuilder.durable(AppConstants.QUEUE_REPORT_GENERATION)
            .withArgument("x-dead-letter-exchange", MAIN_EXCHANGE + ".dlx")
            .withArgument("x-dead-letter-routing-key", "dlx.report")
            .build();
    }
    
    /**
     * Export queue
     */
    @Bean
    public Queue exportQueue() {
        return QueueBuilder.durable(AppConstants.QUEUE_EXPORT)
            .withArgument("x-dead-letter-exchange", MAIN_EXCHANGE + ".dlx")
            .withArgument("x-dead-letter-routing-key", "dlx.export")
            .build();
    }
    
    /**
     * Import queue
     */
    @Bean
    public Queue importQueue() {
        return QueueBuilder.durable(AppConstants.QUEUE_IMPORT)
            .withArgument("x-dead-letter-exchange", MAIN_EXCHANGE + ".dlx")
            .withArgument("x-dead-letter-routing-key", "dlx.import")
            .build();
    }
    
    /**
     * Mobile sync queue
     */
    @Bean
    public Queue mobileSyncQueue() {
        return QueueBuilder.durable(AppConstants.QUEUE_MOBILE_SYNC)
            .withArgument("x-dead-letter-exchange", MAIN_EXCHANGE + ".dlx")
            .withArgument("x-dead-letter-routing-key", "dlx.sync")
            .build();
    }
    
    /**
     * Audit queue
     */
    @Bean
    public Queue auditQueue() {
        return QueueBuilder.durable(AppConstants.QUEUE_AUDIT)
            .withArgument("x-dead-letter-exchange", MAIN_EXCHANGE + ".dlx")
            .withArgument("x-dead-letter-routing-key", "dlx.audit")
            .build();
    }
    
    // ==================== Bindings ====================
    
    /**
     * Bind email queue to exchange
     */
    @Bean
    public Binding emailBinding(Queue emailQueue, TopicExchange mainExchange) {
        return BindingBuilder.bind(emailQueue)
            .to(mainExchange)
            .with("email.*");
    }
    
    /**
     * Bind SMS queue to exchange
     */
    @Bean
    public Binding smsBinding(Queue smsQueue, TopicExchange mainExchange) {
        return BindingBuilder.bind(smsQueue)
            .to(mainExchange)
            .with("sms.*");
    }
    
    /**
     * Bind notification queue to exchange
     */
    @Bean
    public Binding notificationBinding(Queue notificationQueue, TopicExchange mainExchange) {
        return BindingBuilder.bind(notificationQueue)
            .to(mainExchange)
            .with("notification.*");
    }
    
    /**
     * Bind report queue to exchange
     */
    @Bean
    public Binding reportBinding(Queue reportQueue, TopicExchange mainExchange) {
        return BindingBuilder.bind(reportQueue)
            .to(mainExchange)
            .with("report.*");
    }
    
    /**
     * Bind export queue to exchange
     */
    @Bean
    public Binding exportBinding(Queue exportQueue, TopicExchange mainExchange) {
        return BindingBuilder.bind(exportQueue)
            .to(mainExchange)
            .with("export.*");
    }
    
    /**
     * Bind import queue to exchange
     */
    @Bean
    public Binding importBinding(Queue importQueue, TopicExchange mainExchange) {
        return BindingBuilder.bind(importQueue)
            .to(mainExchange)
            .with("import.*");
    }
    
    /**
     * Bind mobile sync queue to exchange
     */
    @Bean
    public Binding mobileSyncBinding(Queue mobileSyncQueue, TopicExchange mainExchange) {
        return BindingBuilder.bind(mobileSyncQueue)
            .to(mainExchange)
            .with("sync.*");
    }
    
    /**
     * Bind audit queue to exchange
     */
    @Bean
    public Binding auditBinding(Queue auditQueue, TopicExchange mainExchange) {
        return BindingBuilder.bind(auditQueue)
            .to(mainExchange)
            .with("audit.*");
    }
    
    // ==================== Dead Letter Queue ====================
    
    /**
     * Dead letter exchange for failed messages
     */
    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(MAIN_EXCHANGE + ".dlx", true, false);
    }
    
    /**
     * Dead letter queue
     */
    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable(AppConstants.QUEUE_DEAD_LETTER).build();
    }
    
    /**
     * Bind dead letter queue to dead letter exchange
     */
    @Bean
    public Binding deadLetterBinding(Queue deadLetterQueue, DirectExchange deadLetterExchange) {
        return BindingBuilder.bind(deadLetterQueue)
            .to(deadLetterExchange)
            .with("dlx.*");
    }
}
