package ru.bmstu.akka.lab5;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;

public class CacheActor extends AbstractActor {
    private final Map<Pair<String, Integer>, Double> cache = new HashMap<>();

    public static class StoreMsg {
        private final String url;
        private final int count;
        private final double average;
        public StoreMsg(String url, int count, double average) {
            this.url = url;
            this.count = count;
            this.average = average;
        }
        public String getUrl() {
            return url;
        }
        public int getCount() {
            return count;
        }
        public double getAverage() {
            return average;
        }
    }

    public static class GetMsg {
        private final String url;
        private final int count;
        public GetMsg(String url, int count) {
            this.url = url;
            this.count = count;
        }
        public int getCount() {
            return count;
        }
        public String getUrl() {
            return url;
        }
    }

    public static class ResMsg {
        private final boolean hasResult;
        private final double averageTime;
        public ResMsg(boolean has, double average) {
            this.hasResult = has;
            this.averageTime = average;
        }
        public double getAverageTime() {
            return averageTime;
        }
        public boolean hasResult() {
            return hasResult;
        }
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().
                match(GetMsg.class, msg -> {
                    double average = cache.getOrDefault(
                            new Pair<>(msg.getUrl(), msg.getCount()), -1.);
                    getSender().tell(new ResMsg(average >= 0, average),
                                        ActorRef.noSender());
                }).
                match(StoreMsg.class,
                        msg -> {
                    cache.put(new Pair<>(msg.getUrl(),
                                            msg.getCount()),
                                    msg.getAverage());
                    System.out.println("storing" + msg.getAverage());
                        })
                .build();
    }
}
