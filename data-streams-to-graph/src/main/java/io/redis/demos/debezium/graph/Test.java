package io.redis.demos.debezium.graph;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.StreamEntry;
import redis.clients.jedis.StreamEntryID;
import redis.clients.jedis.exceptions.JedisDataException;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class Test {

    public static void main(String[] args) {

        JedisPool jedisPool = new JedisPool();

            List<String> streamList = new ArrayList();
            streamList.add("events:inventory:movies");
            String groupName = "cdc.data.syncer";
            String consumer = "cons:1";

            System.out.println(" start stream processing");

            Map<String, String> result =  new HashMap<>();

            if (streamList.isEmpty()) {
                final String msg = "No stream to process";
                result.put("msg", msg);
                System.out.println( msg );

            } else {
                final String msg = String.format("Will process %s ", streamList);
                result.put("msg", msg);

                System.out.println( msg );


                Jedis jedis = null;
                try {
                    jedis = jedisPool.getResource();

                    try {
                        // Create consumer if does not exist already
                        // start from first message
                        StreamEntryID streamEtnryId = new StreamEntryID("0-0");
                        System.out.println(streamEtnryId);

                        jedis.xgroupCreate(streamList.get(0), groupName, streamEtnryId, true);
                        System.out.println(String.format(" Consumer Group %s / %s created", streamList.get(0), groupName));

                    } catch (JedisDataException jde) {
                        if (jde.getMessage().startsWith("BUSYGROUP")) {
                            System.out.println(
                                    String.format(" Consumer Group %s / %s already exists", streamList.get(0), groupName));
                        } else {
                            jde.printStackTrace();
                        }

                    }

                    Map.Entry<String, StreamEntryID> queryStream = new AbstractMap.SimpleImmutableEntry<>(streamList.get(0),
                            StreamEntryID.UNRECEIVED_ENTRY);

                    boolean loop = true;

                    while (loop) {


                    // consume messages
                    List<Map.Entry<String, List<StreamEntry>>> events = jedis
                            .xreadGroup(groupName, consumer, 10, 0, true, queryStream);

                    if (events != null) {
                        System.out.println(events.size());
                        for (Map.Entry m : events) {
                            //System.out.println(m.getKey() + "---" + m.getValue().getClass());
                            if (m.getValue() instanceof ArrayList) {
                                List<StreamEntry> l = (List) m.getValue();
                                Map<String, String> data = l.get(0).getFields();
                                for (Map.Entry entry : data.entrySet()) {
                                    System.out.println(entry.getKey() + " :: " + entry.getValue());
                                }
                                jedis.xack(streamList.get(0), groupName, l.get(0).getID());
                            }
                        }
                    } else {
                        System.out.println("no event in stream");
                    }

                    try {
                        TimeUnit.MILLISECONDS.sleep(200);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }

                } catch (Exception e ){
                    e.printStackTrace();
                } finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }
            }


        }



}
