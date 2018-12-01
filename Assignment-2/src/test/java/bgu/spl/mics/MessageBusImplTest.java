package bgu.spl.mics;

import bgu.spl.mics.Future;
import bgu.spl.mics.MessageBus;
import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.example.ServiceCreator;
import bgu.spl.mics.example.messages.ExampleBroadcast;
import bgu.spl.mics.example.messages.ExampleEvent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class MessageBusImplTest {

    MessageBus mb;
    Map<String, ServiceCreator> serviceCreators = new HashMap<>();
    String currentThreadName;

    @Before
    public void setUp() throws Exception {
        mb =  MessageBusImpl.getInstance();
        serviceCreators.put("ev-handler", MyEventHandlerService::new);
        serviceCreators.put("brod-listener", MyBroadcastListenerService::new);
        serviceCreators.put("dual-listener", MyDualListenerService::new);
        serviceCreators.put("sender", MyMessageSenderService::new);
    }

    @After
    public void tearDown() throws Exception {
        mb = null;
    }

    @Test
    public void testSendingOneEvent() throws InterruptedException {
        testSendingEventOrBroadcast("ev-handler",1);
    }

    @Test
    public void testSendingTenEvent() throws InterruptedException {
        testSendingEventOrBroadcast("ev-handler",10);
    }

    @Test
    public void testSendingOneBroadcast() throws InterruptedException {
        testSendingEventOrBroadcast("brod-listener",1);
    }

    @Test
    public void testSendingTenBroadcast() throws InterruptedException {
        testSendingEventOrBroadcast("brod-listener",10);
    }

    @Test
    public void testSendingEventAndBroadcast() throws InterruptedException {
        testSendingEventAndBroadcast(2);
    }

    @Test
    public void testSendingTenEventAndBroadcast() throws InterruptedException {
        testSendingEventAndBroadcast(10);
    }

    private void testSendingEventAndBroadcast(int messageNum) throws InterruptedException {
        ServiceCreator creator1 = serviceCreators.get("dual-listener");
        Thread sub = new Thread(creator1.create("sub", new String[]{String.valueOf(messageNum)}));
        ServiceCreator creator = serviceCreators.get("sender");
        Thread[]services = new Thread[messageNum];
        sub.start();
        Thread.sleep(20);
        for (int i = 0; i < messageNum; i++) {
            String name = i % 2 ==0 ? "event" : "broadcast";
            currentThreadName = "a"+(i+1);
            services[i] = new Thread(creator.create(currentThreadName, new String[]{name}));
            services[i].start();
            Thread.sleep(20);
        }

        sub.isInterrupted();
        for (int i = 0; i < messageNum; i++) {
            services[i].interrupt();
        }
    }

    private void testSendingEventOrBroadcast(String serviceName, int messageNum) throws InterruptedException {
        ServiceCreator creator1 = serviceCreators.get(serviceName);
        Thread sub = new Thread(creator1.create("sub", new String[]{String.valueOf(messageNum)}));
        ServiceCreator creator = serviceCreators.get("sender");
        Thread[]services = new Thread[messageNum];
        sub.start();
        Thread.sleep(20);
        String name = serviceName.startsWith("ev") ? "event" : "broadcast";
        for (int i = 0; i < messageNum; i++) {
            currentThreadName = "a"+(i+1);
            services[i] = new Thread(creator.create(currentThreadName, new String[]{name}));
            services[i].start();
            Thread.sleep(20);
        }

        sub.isInterrupted();
        for (int i = 0; i < messageNum; i++) {
            services[i].interrupt();
        }
    }

    private class MyEventHandlerService extends MicroService {
        /**
         * @param name the micro-service name (used mainly for debugging purposes -
         *             does not have to be unique)
         */
        int mbt;
        public MyEventHandlerService(String name, String[] args) {
            super(name);
            mbt = Integer.parseInt(args[0]);
        }


        @Override
        protected void initialize() {
            subscribeEvent(ExampleEvent.class, ev -> {
                mbt--;
                assertEquals(ev.getSenderName(),currentThreadName);
                complete(ev, "Hello from " + ev.getSenderName());
                if (mbt == 0) {
                    terminate();
                }
            });
        }
    }

    private class MyBroadcastListenerService extends MicroService {
        /**
         * @param name the micro-service name (used mainly for debugging purposes -
         *             does not have to be unique)
         */
        int mbt;
        public MyBroadcastListenerService(String name, String[] args) {
            super(name);
            mbt = Integer.parseInt(args[0]);
        }

        @Override
        protected void initialize() {
            subscribeBroadcast(ExampleBroadcast.class, message -> {
                mbt--;
                assertEquals(message.getSenderId(),currentThreadName);
                if (mbt == 0) {
                    terminate();
                }
            });
        }
    }

    private class MyMessageSenderService extends MicroService{
        private boolean broadcast;
        /**
         * @param name the micro-service name (used mainly for debugging purposes -
         *             does not have to be unique)
         */
        public MyMessageSenderService(String name, String[] args) {
            super(name);
            this.broadcast = args[0].equals("broadcast");
        }

        @Override
        protected void initialize() {
            if (broadcast) {
                sendBroadcast(new ExampleBroadcast(getName()));
                terminate();
            } else {
                Future<String> futureObject = (Future<String>)sendEvent(new ExampleEvent(getName()));
                if (futureObject != null) {
                    String resolved = futureObject.get(100, TimeUnit.MILLISECONDS);
                    if (resolved != null) assertEquals("Hello from "+currentThreadName,resolved);
                    else fail("Time has elapsed, no services has resolved the event - terminating");

                }
                else fail("No Micro-Service has registered to handle ExampleEvent events! The event cannot be processed");
            }
        }
    }

    private class MyDualListenerService extends MicroService {
        int mbt;

        /**
         * @param name the micro-service name (used mainly for debugging purposes -
         *             does not have to be unique)
         */
        public MyDualListenerService(String name, String[] args) {
            super(name);
            mbt = Integer.parseInt(args[0]);
        }

        @Override
        protected void initialize() {

            subscribeBroadcast(ExampleBroadcast.class, message -> {
                assertEquals(message.getSenderId(),currentThreadName);
                mbt--;
                if (mbt == 0) terminate();
            });
            subscribeEvent(ExampleEvent.class, ev -> {
                mbt--;
                assertEquals(ev.getSenderName(),currentThreadName);
                complete(ev, "Hello from " + ev.getSenderName());
                if (mbt == 0) terminate();
            });
        }
    }




}