package client;

/**
 * Created by vika on 03.04.15.
 */

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * user 1: user user
 * user 2: guest guest
 */

public class ChatClient implements MessageListener {

    private TopicSession pubSession;
    private TopicSession subSession;
    private TopicPublisher publisher;
    private TopicConnection connection;
    private String username;
    private String password;
    private static final String DEFAULT_CONNECTION_FACTORY = "jms/RemoteConnectionFactory";
    private static final String DEFAULT_DESTINATION = "jms/topic/mytopic";
    private static final String INITIAL_CONTEXT_FACTORY = "org.jboss.naming.remote.client.InitialContextFactory";
    private static final String PROVIDER_URL = "http-remoting://127.0.0.1:8080";

    public ChatClient() {
    }

    public ChatClient(String username, String password)
            throws Exception {
        Properties env = new Properties();
        env.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY);
        env.put(Context.PROVIDER_URL, System.getProperty(Context.PROVIDER_URL, PROVIDER_URL));

        InitialContext jndi = new InitialContext(env);
        String destinationString = System.getProperty("destination", DEFAULT_DESTINATION);
        String connectionFactoryString = System.getProperty("connection.factory", DEFAULT_CONNECTION_FACTORY);
        TopicConnectionFactory conFactory =
                (TopicConnectionFactory) jndi.lookup(connectionFactoryString);

        TopicConnection connection =
                conFactory.createTopicConnection(username, password);

        TopicSession pubSession =
                connection.createTopicSession(false,
                        Session.AUTO_ACKNOWLEDGE);
        TopicSession subSession =
                connection.createTopicSession(false,
                        Session.AUTO_ACKNOWLEDGE);

        Topic chatTopic = (Topic) jndi.lookup(destinationString);

        TopicPublisher publisher =
                pubSession.createPublisher(chatTopic);
        TopicSubscriber subscriber =
                subSession.createSubscriber(chatTopic);
        subscriber.setMessageListener(this);
        set(connection, pubSession, subSession, publisher, username);
        connection.start();
    }

    public void set(TopicConnection con, TopicSession pubSess,
                    TopicSession subSess, TopicPublisher pub,
                    String username) {
        this.connection = con;
        this.pubSession = pubSess;
        this.subSession = subSess;
        this.publisher = pub;
        this.username = username;
    }

    public void onMessage(Message message) {
        try {
            TextMessage textMessage = (TextMessage) message;
            String text = textMessage.getText();
            System.out.println(text);
        } catch (JMSException jmse) {
            jmse.printStackTrace();
        }
    }

    protected void writeMessage(String text) throws JMSException {
        TextMessage message = pubSession.createTextMessage();
        message.setText(username + " : " + text);
        publisher.publish(message);
    }

    public void close() throws JMSException {
        connection.close();
    }

    public String getUser(String username, BufferedReader commandLine) {
        System.out.print(" Write your username: ");
        try {
            username = commandLine.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return username;
    }

    public String getPassword(String password, BufferedReader commandLine) {
        System.out.print("Write your password: ");
        try {
            password = commandLine.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return password;
    }

    public static void main(String[] args) {
        ChatClient chat = new ChatClient();
        BufferedReader commandLine = new
                java.io.BufferedReader(new InputStreamReader(System.in));
        try {
            ChatClient chatAuthentication = null;
            while (chat.username == null || chat.password == null) {
                chat.username = chat.getUser(chat.username, commandLine);
                chat.password = chat.getPassword(chat.password, commandLine);

                try {
                    chatAuthentication = new ChatClient(chat.username, chat.password);
                } catch (JMSSecurityException e) {
                    System.out.println("Your username or password is not correct");
                    chat.username = null;
                    chat.password = null;
                }
            }
            System.out.println("Write a message please");

            while (true) {

                String s = commandLine.readLine();
                if (s.equalsIgnoreCase("exit")) {
                    assert chatAuthentication != null;
                    chatAuthentication.close();
                    System.exit(0);
                } else {
                    assert chatAuthentication != null;
                    chatAuthentication.writeMessage(s);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

