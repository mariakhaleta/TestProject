package com.example.movingcircle.logic;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Handler;
import android.view.animation.Animation;

import java.util.ArrayList;
import java.util.Random;

public class CirclesManager extends Animation {
    private static final short CIRCLES_COUNT = 15;
    private static final int[] COLORS = {Color.BLUE, Color.GREEN, Color.MAGENTA, Color.RED,
            Color.CYAN};
    private static final int CIRCLE_MIN_RADIUS = 20;
    private static final int CIRCLE_MAX_RADIUS = 70;
    private static final int CIRCLE_MAX_SPEED = 10;
    private static final int CIRCLE_MIN_SPEED = 5;
    private static final int FULL_DEGREE = 360;

    private static final int UPDATE_FREQUENCY = 30;

    private ArrayList<Circle> mCircles;
    private int mWidth;
    private int mHeight;
    private Random mRandom;

    private MoveListener mMoveListener;

    private Handler mAnimationHandler = new Handler();
    private Runnable mAnimationRunnable = new Runnable() {
        @Override
        public void run() {
            for (Circle circle : mCircles) {
                circle.moveCircle();
                resolveCollisionWithEdge(circle);
                ResolveCirclesCollisions();
            }

            mMoveListener.onCircleMoved();
            mAnimationHandler.postDelayed(this, UPDATE_FREQUENCY);
        }
    };

    public interface MoveListener {
        void onCircleMoved();
    }

    public CirclesManager(int width, int height, MoveListener moveListener) {
        mCircles = new ArrayList<>(CIRCLES_COUNT);
        mHeight = height;
        mWidth = width;
        mRandom = new Random();
        mMoveListener = moveListener;
    }

    public void startCirclesMove() {
        mAnimationHandler.post(mAnimationRunnable);
    }

    public void randomizeCircles() {
        Paint paint;

        for (int i = 0; i < CIRCLES_COUNT; i++) {
            paint = new Paint();
            paint.setColor(COLORS[mRandom.nextInt(COLORS.length)]);
            int radius = mRandom.nextInt(CIRCLE_MAX_RADIUS - CIRCLE_MIN_RADIUS) + CIRCLE_MIN_RADIUS;
            int speed = mRandom.nextInt(CIRCLE_MAX_SPEED - CIRCLE_MIN_SPEED) + CIRCLE_MIN_SPEED;

            Circle newCircle = Circle.newBuilder()
                    .setWayDegree(mRandom.nextInt(FULL_DEGREE))
                    .setPaint(paint)
                    .setRadius(radius)
                    .setSpeed(new Point(speed, speed))
                    .build();

            checkAndAddCircle(newCircle);
        }
    }

    private void checkAndAddCircle(Circle circle) {
        circle.setPoint(getRandomPoint(circle.getRadius()));

        if (mCircles.size() == 0) {
            setStartPoint(circle);
            return;
        }

        if (isIntersectedWithCircles(circle)) {
            checkAndAddCircle(circle);
        } else {
            setStartPoint(circle);
        }
    }

    private void setStartPoint(Circle circle) {
        Point initPoint = circle.getInitialPoint();
        double radian = circle.getWayDegree() * Math.PI / 180;
        Point speed = circle.getSpeed();

        int dx = initPoint.x + (int) (speed.x * Math.cos(radian));
        int dy = initPoint.y + (int) (speed.y * Math.sin(radian));
        circle.setDxPoint(new Point(dx, dy));

        mCircles.add(circle);
    }

    private void ResolveCirclesCollisions() {
        for(int i = 0; i < mCircles.size(); i++) {
            for (int j = i + 1; j < mCircles.size(); j++) {
                Circle circle1 = mCircles.get(i);
                Circle circle2 = mCircles.get(j);

                int x1 = circle1.getDxPoint().x;
                int y1 = circle1.getDxPoint().y;
                int r1 = circle1.getRadius();

                int x2 = circle2.getDxPoint().x;
                int y2 = circle2.getDxPoint().y;
                int r2 = circle2.getRadius();

                int distSq = (x1 - x2) * (x1 - x2) +
                        (y1 - y2) * (y1 - y2);
                int radSumSq = (r1 + r2) * (r1 + r2);

                if (distSq <= radSumSq) {
                    circle1.flipDirection();
                    circle2.flipDirection();
                }
            }
        }
    }

    private boolean isIntersectedWithCircles(Circle circle) {
        int x1 = circle.getInitialPoint().x;
        int y1 = circle.getInitialPoint().y;
        int r1 = circle.getRadius();

        for (Circle existsCircle : mCircles) {
            if (circle == existsCircle) {
                continue;
            }

            int x2 = existsCircle.getInitialPoint().x;
            int y2 = existsCircle.getInitialPoint().y;
            int r2 = existsCircle.getRadius();

            int distSq = (x1 - x2) * (x1 - x2) +
                    (y1 - y2) * (y1 - y2);
            int radSumSq = (r1 + r2) * (r1 + r2);

            if (distSq <= radSumSq) {
                return true;
            }
        }

        return false;
    }

    private void resolveCollisionWithEdge(Circle circle) {
        Point dxPoint = circle.getDxPoint();
        int radius = circle.getRadius();
        Point speed = circle.getSpeed();

        if (dxPoint.x - radius <= 0 || dxPoint.x + radius >= mWidth) {
            circle.setSpeed(new Point(-speed.x, speed.y));
        }

        if (dxPoint.y - radius <= 0 || dxPoint.y + radius >= mHeight) {
            circle.setSpeed(new Point(speed.x, -speed.y));
        }

        if (dxPoint.y + radius > mHeight) {
            dxPoint.y = mHeight - radius;
        }

        if (dxPoint.y - radius < 0) {
            dxPoint.y = radius;
        }

        if (dxPoint.x + radius > mWidth) {
            dxPoint.x = mWidth - radius;
        }

        if (dxPoint.x - radius < 0) {
            dxPoint.x = radius;
        }
    }

    private Point getRandomPoint(int radius) {
        return new Point(
                mRandom.nextInt(mWidth - (radius * 2)) + radius,
                mRandom.nextInt(mHeight - (radius * 2)) + radius);
    }

    public ArrayList<Circle> getCircles() {
        return mCircles;
    }
}
