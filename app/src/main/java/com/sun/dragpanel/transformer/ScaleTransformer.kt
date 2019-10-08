/*
 * Copyright (C) 2014 Pedro Vicente Gómez Sánchez.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sun.dragpanel.transformer

import android.view.View
import com.sun.dragpanel.ViewHelper

/**
 * Transformer extension created to scale the view instead of resize it as the other
 * implementation does. This implementation is based on Nineoldanroids library to scale
 * the view.
 *
 * @author Pedro Vicente Gómez Sánchez
 */
 class ScaleTransformer(view: View, parent: View) : Transformer(view, parent) {

    /**
     * @return true if the right corner of the view matches with the parent view width.
     */
    override val isViewAtRight: Boolean
        @Override get() = view.right == parentView.width

    /**
     * @return true if the bottom corner of the view matches with the parent view height.
     */
    override val isViewAtBottom: Boolean
        @Override get() = view.bottom == parentView.height

    /**
     * @return true if the left position of the view is to the left of sixty percent of the parent
     * width.
     */
    override val isNextToLeftBound: Boolean
        @Override get() = view.right - marginRight < parentView.width * 0.6

    /**
     * @return true if the right position of the view is to the right of the one hundred twenty five
     * five percent of the parent view width.
     */
    override val isNextToRightBound: Boolean
        @Override get() = view.right - marginRight > parentView.width * 1.25

    /**
     * @return min view height taking into account the configured margin.
     */
    override val minHeightPlusMargin: Int
        @Override get() = view.height

    /**
     * @return min view width.
     */
    override val minWidthPlusMarginRight: Int
        @Override get() = getOriginalWidth()

    /**
     * Uses Nineoldandroids to change the scale.
     *
     * @param verticalDragOffset used to calculate the new scale.
     */
    override fun updateScale(verticalDragOffset: Float) {
        ViewHelper.setScaleX(view
            , 1 - verticalDragOffset / xScaleFactor)
        ViewHelper.setScaleY(view, 1 - verticalDragOffset / yScaleFactor)
    }

    /**
     * Uses Nineoldandroids to change the position of the view.
     *
     * @param verticalDragOffset used to calculate the new position.
     */
    override fun updatePosition(verticalDragOffset: Float) {
        ViewHelper.setPivotX(view, (view.width - marginRight).toFloat())
        ViewHelper.setPivotY(view , (view.height - marginBottom).toFloat())
    }

}
