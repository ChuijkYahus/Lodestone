package team.lodestar.lodestone.modules.rendering.particle.visual.types.trail;

import team.lodestar.lodestone.modules.rendering.particle.runtime.ParticleView;

public class TrailVisualStorage {
    private final int capacity;
    private final int maxPoints;

    private final double[][] xHistory;
    private final double[][] yHistory;
    private final double[][] zHistory;
    private final int[] historyCount;
    private final int[] lastCaptureAge;

    public TrailVisualStorage(int capacity, int maxPoints) {
        this.capacity = capacity;
        this.maxPoints = maxPoints;

        this.xHistory = new double[capacity][maxPoints];
        this.yHistory = new double[capacity][maxPoints];
        this.zHistory = new double[capacity][maxPoints];
        this.historyCount = new int[capacity];
        this.lastCaptureAge = new int[capacity];

        for (int i = 0; i < capacity; i++) lastCaptureAge[i] = -1;
    }

    public void capture(ParticleView view, int liveCount, int targetVisualId) {
        double[] x = view.x(), y = view.y(), z = view.z();
        int[] age = view.age();
        int[] visualIds = view.visualIds();

        for (int i = 0; i < liveCount; i++) {
            if (visualIds[i] != targetVisualId) continue;

            int currentAge = age[i];
            if (lastCaptureAge[i] == currentAge) continue;
            lastCaptureAge[i] = currentAge;

            int count = historyCount[i];
            if (count < maxPoints) {
                xHistory[i][count] = x[i];
                yHistory[i][count] = y[i];
                zHistory[i][count] = z[i];
                historyCount[i] = count + 1;
            } else {
                for (int j = 0; j < maxPoints - 1; j++) {
                    xHistory[i][j] = xHistory[i][j + 1];
                    yHistory[i][j] = yHistory[i][j + 1];
                    zHistory[i][j] = zHistory[i][j + 1];
                }
                xHistory[i][maxPoints - 1] = x[i];
                yHistory[i][maxPoints - 1] = y[i];
                zHistory[i][maxPoints - 1] = z[i];
            }
        }
    }

    public void onSwapRemove(int deadIndex, int movedIndex) { // TODO: idk maybe plug this in somewhere
        historyCount[deadIndex] = historyCount[movedIndex];
        lastCaptureAge[deadIndex] = lastCaptureAge[movedIndex];

        System.arraycopy(xHistory[movedIndex], 0, xHistory[deadIndex], 0, maxPoints);
        System.arraycopy(yHistory[movedIndex], 0, yHistory[deadIndex], 0, maxPoints);
        System.arraycopy(zHistory[movedIndex], 0, zHistory[deadIndex], 0, maxPoints);
    }

    public int capacity() {
        return capacity;
    }

    public int maxPoints() {
        return maxPoints;
    }

    public double[][] xHistory() {
        return xHistory;
    }

    public double[][] yHistory() {
        return yHistory;
    }

    public double[][] zHistory() {
        return zHistory;
    }

    public int[] historyCount() {
        return historyCount;
    }
}