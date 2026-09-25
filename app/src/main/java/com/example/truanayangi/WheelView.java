package com.example.truanayangi;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class WheelView extends View {
    private final List<Food> foods = new ArrayList<>();
    private final Paint segmentPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint separatorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint centerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint pointerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final RectF wheelBounds = new RectF();
    private final Path pointerPath = new Path();
    private final int[] segmentColors;
    private float rotationAngle;
    private ValueAnimator animator;

    public WheelView(Context context) {
        this(context, null);
    }

    public WheelView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WheelView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        segmentColors = new int[]{
                ContextCompat.getColor(context, R.color.wheel_orange),
                ContextCompat.getColor(context, R.color.wheel_red),
                ContextCompat.getColor(context, R.color.wheel_green),
                ContextCompat.getColor(context, R.color.wheel_blue),
                ContextCompat.getColor(context, R.color.wheel_purple),
                ContextCompat.getColor(context, R.color.wheel_brown)
        };
        separatorPaint.setColor(ContextCompat.getColor(context, R.color.wheel_separator));
        separatorPaint.setStyle(Paint.Style.STROKE);
        separatorPaint.setStrokeWidth(dp(1));
        textPaint.setColor(ContextCompat.getColor(context, R.color.wheel_text));
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setFakeBoldText(true);
        centerPaint.setColor(ContextCompat.getColor(context, R.color.surface_color));
        pointerPaint.setColor(ContextCompat.getColor(context, R.color.primary_dark));
    }

    public void setFoods(List<Food> newFoods) {
        foods.clear();
        foods.addAll(newFoods);
        rotationAngle = 0;
        invalidate();
    }

    public void pointTo(int index) {
        if (index < 0 || index >= foods.size()) {
            return;
        }
        float sweepAngle = 360f / foods.size();
        rotationAngle = normalize(-(index * sweepAngle + sweepAngle / 2f));
        invalidate();
    }

    public void spinTo(int index, Runnable onFinished) {
        if (index < 0 || index >= foods.size()) {
            return;
        }
        if (animator != null) {
            animator.cancel();
        }

        float sweepAngle = 360f / foods.size();
        float desiredAngle = normalize(-(index * sweepAngle + sweepAngle / 2f));
        float currentAngle = normalize(rotationAngle);
        float distance = normalize(desiredAngle - currentAngle);
        float targetAngle = rotationAngle + 1800f + distance;

        animator = ValueAnimator.ofFloat(rotationAngle, targetAngle);
        animator.setDuration(2800);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(animation -> {
            rotationAngle = (float) animation.getAnimatedValue();
            invalidate();
        });
        animator.addListener(new AnimatorListenerAdapter() {
            private boolean cancelled;

            @Override
            public void onAnimationCancel(Animator animation) {
                cancelled = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                rotationAngle = normalize(rotationAngle);
                invalidate();
                if (!cancelled) {
                    onFinished.run();
                }
            }
        });
        animator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (foods.isEmpty()) {
            return;
        }

        float centerX = getWidth() / 2f;
        float centerY = getHeight() / 2f + dp(4);
        float radius = Math.min(getWidth(), getHeight()) * 0.39f;
        wheelBounds.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
        float sweepAngle = 360f / foods.size();
        textPaint.setTextSize(textSizeFor(foods.size()));

        for (int index = 0; index < foods.size(); index++) {
            float startAngle = rotationAngle - 90f + index * sweepAngle;
            segmentPaint.setColor(segmentColors[index % segmentColors.length]);
            canvas.drawArc(wheelBounds, startAngle, sweepAngle, true, segmentPaint);
            canvas.drawArc(wheelBounds, startAngle, sweepAngle, true, separatorPaint);

            double textAngle = Math.toRadians(startAngle + sweepAngle / 2f);
            float textRadius = radius * 0.62f;
            float textX = centerX + (float) Math.cos(textAngle) * textRadius;
            float textY = centerY + (float) Math.sin(textAngle) * textRadius;
            textY -= (textPaint.ascent() + textPaint.descent()) / 2f;
            canvas.drawText(shorten(foods.get(index).getName()), textX, textY, textPaint);
        }

        canvas.drawCircle(centerX, centerY, radius * 0.14f, centerPaint);
        canvas.drawCircle(centerX, centerY, radius * 0.055f, pointerPaint);
        drawPointer(canvas, centerX, centerY - radius);
    }

    private void drawPointer(Canvas canvas, float centerX, float wheelTop) {
        pointerPath.reset();
        pointerPath.moveTo(centerX - dp(17), wheelTop - dp(18));
        pointerPath.lineTo(centerX + dp(17), wheelTop - dp(18));
        pointerPath.lineTo(centerX, wheelTop + dp(13));
        pointerPath.close();
        canvas.drawPath(pointerPath, pointerPaint);
    }

    private String shorten(String name) {
        int limit;
        if (foods.size() <= 6) {
            limit = 12;
        } else if (foods.size() <= 10) {
            limit = 9;
        } else if (foods.size() <= 18) {
            limit = 6;
        } else {
            limit = 4;
        }
        return name.length() <= limit ? name : name.substring(0, limit - 1) + "…";
    }

    private float textSizeFor(int count) {
        if (count <= 6) {
            return sp(14);
        }
        if (count <= 10) {
            return sp(12);
        }
        if (count <= 18) {
            return sp(10);
        }
        return sp(8);
    }

    private float normalize(float angle) {
        return (angle % 360f + 360f) % 360f;
    }

    private float dp(float value) {
        return value * getResources().getDisplayMetrics().density;
    }

    private float sp(float value) {
        return value * getResources().getDisplayMetrics().scaledDensity;
    }

    @Override
    protected void onDetachedFromWindow() {
        if (animator != null) {
            animator.cancel();
        }
        super.onDetachedFromWindow();
    }
}
