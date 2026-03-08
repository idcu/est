package ltd.idcu.est.plugin.api;

public final class PluginStats {
    
    private final int totalPlugins;
    private final int runningPlugins;
    private final int stoppedPlugins;
    private final int errorPlugins;
    private final long totalLoadTime;
    private final long totalStartTime;
    
    public PluginStats(int totalPlugins, int runningPlugins, int stoppedPlugins,
                       int errorPlugins, long totalLoadTime, long totalStartTime) {
        this.totalPlugins = totalPlugins;
        this.runningPlugins = runningPlugins;
        this.stoppedPlugins = stoppedPlugins;
        this.errorPlugins = errorPlugins;
        this.totalLoadTime = totalLoadTime;
        this.totalStartTime = totalStartTime;
    }
    
    public int getTotalPlugins() {
        return totalPlugins;
    }
    
    public int getRunningPlugins() {
        return runningPlugins;
    }
    
    public int getStoppedPlugins() {
        return stoppedPlugins;
    }
    
    public int getErrorPlugins() {
        return errorPlugins;
    }
    
    public long getTotalLoadTime() {
        return totalLoadTime;
    }
    
    public long getTotalStartTime() {
        return totalStartTime;
    }
    
    public double getAverageLoadTime() {
        return totalPlugins > 0 ? (double) totalLoadTime / totalPlugins : 0;
    }
    
    public double getAverageStartTime() {
        return totalPlugins > 0 ? (double) totalStartTime / totalPlugins : 0;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private int totalPlugins;
        private int runningPlugins;
        private int stoppedPlugins;
        private int errorPlugins;
        private long totalLoadTime;
        private long totalStartTime;
        
        public Builder totalPlugins(int totalPlugins) {
            this.totalPlugins = totalPlugins;
            return this;
        }
        
        public Builder runningPlugins(int runningPlugins) {
            this.runningPlugins = runningPlugins;
            return this;
        }
        
        public Builder stoppedPlugins(int stoppedPlugins) {
            this.stoppedPlugins = stoppedPlugins;
            return this;
        }
        
        public Builder errorPlugins(int errorPlugins) {
            this.errorPlugins = errorPlugins;
            return this;
        }
        
        public Builder totalLoadTime(long totalLoadTime) {
            this.totalLoadTime = totalLoadTime;
            return this;
        }
        
        public Builder totalStartTime(long totalStartTime) {
            this.totalStartTime = totalStartTime;
            return this;
        }
        
        public PluginStats build() {
            return new PluginStats(totalPlugins, runningPlugins, stoppedPlugins,
                                   errorPlugins, totalLoadTime, totalStartTime);
        }
    }
}
