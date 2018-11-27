package bgu.spl.mics.application.services;

import bgu.spl.mics.MessageBus;
import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.MessageBusImplTest;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.passiveObjects.Customer;
import bgu.spl.mics.application.passiveObjects.OrderReceipt;
import bgu.spl.mics.application.passiveObjects.OrderSchedule;
import bgu.spl.mics.example.ServiceCreator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class ServicesTest {

    MessageBus mb;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {
        mb = null;
    }

    @Test
    public void testTimerService(){

    }
}