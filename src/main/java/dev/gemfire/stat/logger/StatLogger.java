package dev.gemfire.stat.logger;

import org.apache.geode.StatisticDescriptor;
import org.apache.geode.Statistics;
import org.apache.geode.StatisticsType;
import org.apache.geode.cache.execute.Function;
import org.apache.geode.cache.execute.FunctionContext;
import org.apache.geode.distributed.DistributedSystem;
import org.apache.geode.logging.internal.log4j.api.LogService;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

public class StatLogger implements Function {
    public static final String ID = "StatLogger";
    private static final Logger logger = LogService.getLogger();
    private List<StatsHolder> listOfStats = new CopyOnWriteArrayList<>();
    private Timer timer;
    private long timerInterval = 1000;

    @Override
    public boolean isHA() {
        return false;
    }

    @Override
    public boolean hasResult() {
        return true;
    }

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public void execute(FunctionContext functionContext) {

        String[] args = ((String[]) functionContext.getArguments())[0].split(" ");
        if ("add".compareToIgnoreCase(args[0]) == 0) {
            String[] statsNames = args[1].split("[.]", 0);
            addStats(functionContext.getCache().getDistributedSystem(), statsNames[0], statsNames[1]);
            ensureTimerRunning();
            functionContext.getResultSender().lastResult("Logging Metric count " + listOfStats.size());
        } else if ("clear".compareToIgnoreCase(args[0]) == 0) {
            cancelTimer();
            listOfStats.clear();
            functionContext.getResultSender().lastResult("Cleared list and stopped logging");
        } else if ("interval".compareToIgnoreCase(args[0]) ==0) {
            timerInterval = Long.parseLong(args[1]);
            cancelTimer();
            ensureTimerRunning();
            functionContext.getResultSender().lastResult("Timer interval set to " + timerInterval + " ms");
        } else {
            functionContext.getResultSender().lastResult("No operation");
        }
    }

    private void cancelTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private void ensureTimerRunning() {
        if (timer == null && !listOfStats.isEmpty()) {
            timer = new Timer("StatLogger", true);
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    for (StatsHolder curr : listOfStats) {
                        logger.info(curr.toString());
                    }
                }
            }, 0, timerInterval);
        }
    }

    private void addStats(DistributedSystem ds, String groupName, String statName) {
        StatisticsType type = ds.findType(groupName);
        for (Statistics currStatistics : ds.findStatisticsByType(type)) {
            if (currStatistics.getTextId().compareToIgnoreCase(groupName) == 0) {
                for (StatisticDescriptor currDesciptor : type.getStatistics()) {
                    if (currDesciptor.getName().compareToIgnoreCase(statName) == 0) {
                        listOfStats.add(new StatsHolder(groupName + "." + statName,  currStatistics, currDesciptor));
                    }
                }
            }
        }
    }

    private class StatsHolder {

        private Statistics statistics;
        private StatisticDescriptor statisticDescriptor;
        private String prettyName;

        public StatsHolder(String prettyName, Statistics statistics, StatisticDescriptor statisticDescriptor) {
            this.prettyName = prettyName;
            this.statistics = statistics;
            this.statisticDescriptor = statisticDescriptor;
        }

        @Override
        public String toString() {
            return prettyName + " = " + statistics.get(statisticDescriptor);
        }
    }
}
