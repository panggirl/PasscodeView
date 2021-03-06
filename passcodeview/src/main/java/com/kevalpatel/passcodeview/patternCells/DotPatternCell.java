/*
 * Copyright 2017 Keval Patel.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kevalpatel.passcodeview.patternCells;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.Dimension;
import android.support.annotation.NonNull;

import com.kevalpatel.passcodeview.PatternView;
import com.kevalpatel.passcodeview.PinView;
import com.kevalpatel.passcodeview.R;

/**
 * Created by Keval on 06-Apr-17.
 *
 * @author 'https://github.com/kevalpatel2106'
 */

public final class DotPatternCell extends PatternCell {

    @NonNull
    private final Builder mBuilder;
    private boolean isDisplayError;

    DotPatternCell(@NonNull PatternView patternView,
                   @NonNull Rect bound,
                   @NonNull DotPatternCell.Builder builder,
                   int index) {
        super(patternView, bound, builder, index);
        mBuilder = builder;
    }

    /**
     * Draw the indicator.
     *
     * @param canvas     Canvas of {@link PinView}.
     * @param isSelected True if to display selectedL indicator.
     */
    @Override
    public void draw(@NonNull Canvas canvas, boolean isSelected) {
        canvas.drawCircle(getBound().exactCenterX(),
                getBound().exactCenterY(),
                mBuilder.getRadius(),
                isDisplayError ? mBuilder.getErrorCellPaint() :
                        isSelected ? mBuilder.getSelectedCellPaint() : mBuilder.getNormalCellPaint());
    }

    @Override
    public void onAuthFailed() {
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isDisplayError = false;
                getRootView().invalidate();
            }
        }, 400);
        isDisplayError = true;
    }

    @Override
    public void onAuthSuccess() {
        //Do nothing
    }

    @Override
    public boolean isIndicatorTouched(float touchX, float touchY) {
        //Check if the click is between the width bounds
        if (touchX > getBound().exactCenterX() - mBuilder.getRadius()
                && touchX < getBound().exactCenterX() + mBuilder.getRadius()) {

            //Check if the click is between the height bounds
            if (touchY > getBound().exactCenterY() - mBuilder.getRadius()
                    && touchY < getBound().exactCenterY() + mBuilder.getRadius()) {
                return true;
            }
        }
        return false;
    }

    public static class Builder extends PatternCell.Builder {
        @ColorInt
        private int mCellColor;              //Empty indicator stroke color
        @ColorInt
        private int mSelectedColor;              //Filled indicator stroke color
        @Dimension
        private float mRadius;

        private Paint mCellPaint;             //Empty indicator color
        private Paint mSelectedCellPaint;             //Solid indicator color
        private Paint mErrorCellPaint;             //Error indicator color


        public Builder(@NonNull PatternView patternView) {
            super(patternView);
        }

        @Dimension
        @Override
        public float getCellRadius() {
            return mRadius * 2;
        }

        @Override
        public DotPatternCell.Builder build() {

            //Set empty dot paint
            mCellPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mCellPaint.setColor(mCellColor);

            //Set filled dot paint
            mSelectedCellPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mSelectedCellPaint.setColor(mSelectedColor);

            //Set filled dot paint
            mErrorCellPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mErrorCellPaint.setColor(Color.RED);
            return this;
        }

        @Override
        protected void setDefaults(@NonNull Context context) {
            mRadius = getContext().getResources().getDimension(R.dimen.lib_indicator_radius);
            mSelectedColor = getContext().getResources().getColor(R.color.lib_indicator_filled_color);
            mCellColor = getContext().getResources().getColor(R.color.lib_indicator_stroke_color);
        }

        @Override
        public PatternCell getCell(@NonNull Rect bound, int index) {
            return new DotPatternCell(getRootView(), bound, this, index);
        }

        @ColorInt
        public int getCellColor() {
            return mCellColor;
        }

        @NonNull
        public DotPatternCell.Builder setCellColor(@ColorInt int normalColor) {
            mCellColor = normalColor;
            return this;
        }

        @NonNull
        public DotPatternCell.Builder setCellColorResource(@ColorRes int indicatorStrokeColor) {
            mCellColor = getContext().getResources().getColor(indicatorStrokeColor);
            return this;
        }

        @ColorInt
        public int getSelectedColor() {
            return mSelectedColor;
        }

        @NonNull
        public DotPatternCell.Builder setSelectedColor(@ColorInt int selectedColor) {
            mSelectedColor = selectedColor;
            return this;
        }

        @NonNull
        public DotPatternCell.Builder setSelectedCellColorResource(@ColorRes int indicatorFilledColor) {
            mSelectedColor = getContext().getResources().getColor(indicatorFilledColor);
            return this;
        }

        @Dimension
        public float getRadius() {
            return mRadius;
        }

        @NonNull
        public DotPatternCell.Builder setRadius(@DimenRes int indicatorRadius) {
            mRadius = getContext().getResources().getDimension(indicatorRadius);
            return this;
        }

        @NonNull
        public DotPatternCell.Builder setRadius(@Dimension float radius) {
            mRadius = radius;
            return this;
        }

        @NonNull
        public Paint getNormalCellPaint() {
            return mCellPaint;
        }

        @NonNull
        public Paint getSelectedCellPaint() {
            return mSelectedCellPaint;
        }

        @NonNull
        public Paint getErrorCellPaint() {
            return mErrorCellPaint;
        }
    }
}
