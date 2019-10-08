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
import android.widget.RelativeLayout
import com.sun.dragpanel.ViewHelper
import kotlin.math.roundToInt

/**
 * Abstract class created to be implemented by different classes are going to change the size of a
 * view. The most basic one is going to scale the view and the most complex used with VideoView is
 * going to change the size of the view.
 *
 *
 * The view used in this class has to be contained by a RelativeLayout.
 *
 *
 * This class also provide information about the size of the view and the position because
 * different Transformer implementations could change the size of the view but not the position,
 * like ScaleTransformer does.
 *
 * @author Pedro Vicente Gómez Sánchez
 */
abstract class Transformer(protected val view: View, protected val parentView: View) {

    var marginRight: Int = 0
        set(marginRight) {
            field = marginRight.toDouble().roundToInt()
        }
    var marginBottom: Int = 0
        set(marginBottom) {
            field = marginBottom.toDouble().roundToInt()
        }

    var xScaleFactor: Float = 0.toFloat()
    var yScaleFactor: Float = 0.toFloat()

    private var originalHeight: Int = 0
    private var originalWidth: Int = 0

    val isViewAtTop: Boolean
        get() = view.top == 0

    val isAboveTheMiddle: Boolean
        get() {
            val parentHeight = parentView.height
            val viewYPosition = ViewHelper.getY(view) + view.height * 0.5f
            return viewYPosition < parentHeight * 0.5
        }

    abstract val isViewAtRight: Boolean

    abstract val isViewAtBottom: Boolean

    abstract val isNextToRightBound: Boolean

    abstract val isNextToLeftBound: Boolean

    /**
     * @return min possible height, after apply the transformation, plus the margin right.
     */
    abstract val minHeightPlusMargin: Int

    /**
     * @return min possible width, after apply the transformation.
     */
    abstract val minWidthPlusMarginRight: Int

    /**
     * Change view height using the LayoutParams of the view.
     *
     * @param newHeight to change..
     */
    fun setViewHeight(newHeight: Int) {
        if (newHeight > 0) {
            originalHeight = newHeight
            val layoutParams = view.layoutParams as RelativeLayout.LayoutParams
            layoutParams.height = newHeight
            view.layoutParams = layoutParams
        }
    }

    abstract fun updatePosition(verticalDragOffset: Float)

    abstract fun updateScale(verticalDragOffset: Float)

    /**
     * @return height of the view before it has change the size.
     */
    fun getOriginalHeight(): Int {
        if (originalHeight == 0) {
            originalHeight = view.measuredHeight
        }
        return originalHeight
    }

    /**
     * @return width of the view before it has change the size.
     */
    fun getOriginalWidth(): Int {
        if (originalWidth == 0) {
            originalWidth = view.measuredWidth
        }
        return originalWidth
    }
}
