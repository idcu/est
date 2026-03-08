package ltd.idcu.est.tracing.impl;

import ltd.idcu.est.tracing.api.SpanExporter;
import ltd.idcu.est.tracing.api.TraceContext;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class FileSpanExporter implements SpanExporter {
    private final String filePath;
    private final BlockingQueue<TraceContext> queue;
    private final Thread exportThread;
    private volatile boolean running;
    private final int batchSize;
    private final long flushIntervalMs;

    public FileSpanExporter(String filePath) {
        this(filePath, 100, 5000);
    }

    public FileSpanExporter(String filePath, int batchSize, long flushIntervalMs) {
        this.filePath = filePath;
        this.batchSize = batchSize;
        this.flushIntervalMs = flushIntervalMs;
        this.queue = new LinkedBlockingQueue<>();
        this.running = true;
        this.exportThread = new Thread(this::exportLoop, "FileSpanExporter");
        this.exportThread.setDaemon(true);
        this.exportThread.start();
    }

    @Override
    public void export(TraceContext context) {
        if (running) {
            queue.offer(context);
        }
    }

    @Override
    public void exportBatch(List<TraceContext> contexts) {
        if (running) {
            queue.addAll(contexts);
        }
    }

    @Override
    public void flush() {
        List<TraceContext> batch = new ArrayList<>();
        queue.drainTo(batch);
        if (!batch.isEmpty()) {
            writeBatch(batch);
        }
    }

    @Override
    public void shutdown() {
        running = false;
        exportThread.interrupt();
        try {
            exportThread.join(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        flush();
    }

    private void exportLoop() {
        while (running) {
            try {
                List<TraceContext> batch = new ArrayList<>();
                TraceContext first = queue.poll();
                if (first != null) {
                    batch.add(first);
                    queue.drainTo(batch, batchSize - 1);
                    writeBatch(batch);
                } else {
                    Thread.sleep(flushIntervalMs);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private void writeBatch(List<TraceContext> batch) {
        if (batch.isEmpty()) {
            return;
        }
        try {
            StringBuilder sb = new StringBuilder();
            for (TraceContext context : batch) {
                sb.append(context.toJson()).append("\n");
            }
            Files.write(
                Paths.get(filePath),
                sb.toString().getBytes(StandardCharsets.UTF_8),
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND
            );
        } catch (IOException e) {
        }
    }

    public List<TraceContext> loadSpans() {
        List<TraceContext> spans = new ArrayList<>();
        try {
            File file = new File(filePath);
            if (file.exists()) {
                List<String> lines = Files.readAllLines(Paths.get(filePath), StandardCharsets.UTF_8);
                for (String line : lines) {
                    line = line.trim();
                    if (!line.isEmpty()) {
                        try {
                            spans.add(TraceContext.fromJson(line));
                        } catch (Exception e) {
                        }
                    }
                }
            }
        } catch (IOException e) {
        }
        return spans;
    }
}
