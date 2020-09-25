package com.example.movingcircle;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.movingcircle.logic.Circle;
import com.example.movingcircle.logic.CirclesManager;

public class CirclesView extends View implements CirclesManager.MoveListener {

    private CirclesManager mCirclesManager;

    public CirclesView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mCirclesManager = new CirclesManager(w, h, this);
        mCirclesManager.randomizeCircles();
        mCirclesManager.startCirclesMove();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (Circle circle : mCirclesManager.getCircles()) {
            canvas.drawCircle(circle.getDxPoint().x,
                    circle.getDxPoint().y,
                    circle.getRadius(),
                    circle.getPaint());
        }
    }

    @Override
    public void onCircleMoved() {
        invalidate();
    }
}
