package com.example.lab4_trafficlight;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class TrafficLightView extends View {
    private final Paint paint = new Paint();

    public enum LightState { OFF, RED, YELLOW, GREEN }
    private LightState state = LightState.OFF;

    private static final LightState[] LIGHTS = { LightState.RED, LightState.YELLOW, LightState.GREEN };
    private static final int[] ON_COLORS = { Color.RED, Color.YELLOW, Color.GREEN };
    private static final int OFF_COLOR = Color.rgb(40, 40, 40);

    private boolean running = false;
    private long startTime;

    private static final LightState[] PHASES = { LightState.RED, LightState.YELLOW, LightState.GREEN, LightState.YELLOW };
    private static final long[] DURATIONS_MS = { 4000, 2000, 6000, 2000 };
    private static final long CYCLE_MS = 14000;


    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        float w = getWidth();
        float h = getHeight();

        paint.setColor(Color.DKGRAY);
        canvas.drawRect(0, 0, w, h, paint);

        float band = h / 3f;
        float r = Math.min(w / 2f, band / 2f) * 0.8f ;

        if (running) {
            state = getCurrentLightState();
        }

        for (int i = 0; i < LIGHTS.length; i++) {
            paint.setColor(state == LIGHTS[i] ? ON_COLORS[i] : OFF_COLOR);
            canvas.drawCircle(w / 2f, band * (i + 0.5f), r, paint);
        }

        if (running) {
            postInvalidateOnAnimation();
        }
    }

    private LightState getCurrentLightState() {
        long t = (SystemClock.elapsedRealtime() - startTime) % CYCLE_MS;
        for (int i = 0; i < PHASES.length; i++) {
            if (t < DURATIONS_MS[i]) {
                return PHASES[i];
            }
            t -= DURATIONS_MS[i];
        }
        return PHASES[0];
    }

    public TrafficLightView(Context context) {
        super(context);
    }

    public TrafficLightView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TrafficLightView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void start() {
        running = true;
        state = LightState.RED;
        startTime = SystemClock.elapsedRealtime();

        invalidate();
    }

    public void stop() {
        running = false;
        state = LightState.OFF;

        invalidate();
    }
}
