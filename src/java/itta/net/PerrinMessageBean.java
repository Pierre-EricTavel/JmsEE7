/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package itta.net;


import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.Destination;
import javax.jms.JMSContext;
import javax.jms.JMSDestinationDefinition;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 *
 * @author Administrator
 */
@JMSDestinationDefinition(name = "java:app/MyQueue", interfaceName = "javax.jms.Queue", resourceAdapter = "jmsra", destinationName = "MyQueue")
@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "java:app/MyQueue")
    , @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
    , @ActivationConfigProperty(propertyName = "connectionFactoryLookup", propertyValue = "jms/MyConnectionFactory")
})
public class PerrinMessageBean implements MessageListener {
    
   @Inject
   private JMSContext jms;
    
    @Override
    public void onMessage(Message message) {
   
       try {
           Destination replyto= message.getJMSReplyTo();
           if(replyto==null)
               return;
           
           String contenu = ((TextMessage)message).getText();
           
           System.out.println("contenu='"+contenu+"'");
           
           String reponse = "Response='"+contenu.toUpperCase()+"'";
           jms.createProducer().send(replyto, reponse);
       } catch (JMSException ex) {
           System.out.println(ex);
       }

        
    }
    
}
