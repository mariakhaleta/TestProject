package com.example.movingcircle.logic;

import android.graphics.Paint;
import android.graphics.Point;

public class Circle {
    private int mRadius;
    private Point mInitialPoint;
    private int mWayDegree;
    private Point mSpeed;
    private Paint mPaint;
    private Point mDxPoint;

    private Circle() {

    }

    public static Builder newBuilder() {
        return new Circle().new Builder();
    }

    public Paint getPaint() {
        return mPaint;
    }

    public int getRadius() {
        return mRadius;
    }

    public Point getInitialPoint() {
        return mInitialPoint;
    }

    public void setPoint(Point point) {
        mInitialPoint = point;
    }

    public Point getDxPoint() {
        return mDxPoint;
    }

    public void setDxPoint(Point dxPoint) {
        mDxPoint = dxPoint;
    }

    public int getWayDegree() {
        return mWayDegree;
    }

    public Point getSpeed() {
        return mSpeed;
    }

    public void setSpeed(Point speed) {
        mSpeed = speed;
    }

    public void moveCircle() {
        mDxPoint.set(mDxPoint.x + mSpeed.x, mDxPoint.y + mSpeed.y);
    }

    public void flipDirection() {
        mSpeed.set(-mSpeed.x, -mSpeed.y);
    }

    public class Builder {
        private Builder() {

        }

        public Builder setSpeed(Point speed) {
            Circle.this.mSpeed = speed;

            return this;
        }

        public Builder setPaint(Paint paint) {
            Circle.this.mPaint = paint;

            return this;
        }

        public Builder setWayDegree(int wayDegree) {
            Circle.this.mWayDegree = wayDegree;

            return this;
        }

        public Builder setRadius(int radius) {
            Circle.this.mRadius = radius;

            return this;
        }

        public Circle build() {
            return Circle.this;
        }
    }
}
