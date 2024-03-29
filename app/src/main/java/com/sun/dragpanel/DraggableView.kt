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
package com.sun.dragpanel

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.RelativeLayout
import androidx.core.view.MotionEventCompat
import androidx.core.view.ViewCompat
import androidx.customview.widget.ViewDragHelper
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.sun.dragpanel.transformer.Transformer
import com.sun.dragpanel.transformer.TransformerFactory
import kotlin.math.abs

/**
 * Class created to extends a ViewGroup and simulate the YoutubeLayoutComponent
 *
 * @author Pedro Vicente Gómez Sánchez
 */
class DraggableView : RelativeLayout {

    private var activePointerId = INVALID_POINTER
    private var lastTouchActionDownXPosition: Float = 0.toFloat()

    private var dragView: View? = null
    private var secondView: View? = null

    private var fragmentManager: FragmentManager? = null
    private var viewDragHelper: ViewDragHelper? = null
    private var transformer: Transformer? = null

    private var enableHorizontalAlphaEffect: Boolean = false
    private var topViewResize: Boolean = false
    /**
     * Return if user can maximize minimized view on click.
     */
    /**
     * Enable or disable click to maximize view when dragged view is minimized
     * If your content have a touch/click listener (like YoutubePlayer), you
     * need disable it to active this feature.
     *
     * @param enableClickToMaximize to enable or disable the click.
     */
    var isClickToMaximizeEnabled: Boolean = false
    /**
     * Return if user can minimize maximized view on click.
     */
    /**
     * Enable or disable click to minimize view when dragged view is maximized
     * If your content have a touch/click listener (like YoutubePlayer), you
     * need disable it to active this feature.
     *
     * @param enableClickToMinimize to enable or disable the click.
     */
    var isClickToMinimizeEnabled: Boolean = false
    /**
     * Return if touch listener is enable or disable
     */
    /**
     * Enable or disable the touch listener
     *
     * @param touchEnabled to enable or disable the touch event.
     */
     var isTouchEnabled: Boolean = false

    private var listener: DraggableListener? = null
    private var topViewHeight: Int = 0
    private var scaleFactorX: Float = 0.toFloat()
    private var scaleFactorY: Float = 0.toFloat()
    private var marginBottom: Int = 0
    private var marginRight: Int = 0
    private var dragViewId: Int = 0
    private var secondViewId: Int = 0

    /**
     * Checks if the top view is minimized.
     *
     * @return true if the view is minimized.
     */
    val isMinimized: Boolean
        get() = isDragViewAtBottom && isDragViewAtRight

    /**
     * Checks if the top view is maximized.
     *
     * @return true if the view is maximized.
     */
    val isMaximized: Boolean
        get() = isDragViewAtTop

    /**
     * Checks if the top view closed at the right place.
     *
     * @return true if the view is closed at right.
     */
    val isClosedAtRight: Boolean
        get() = dragView!!.getLeft() >= getWidth()

    /**
     * Checks if the top view is closed at the left place.
     *
     * @return true if the view is closed at left.
     */
    val isClosedAtLeft: Boolean
        get() = dragView!!.getRight() <= 0

    /**
     * Checks if the top view is closed at the right or left place.
     *
     * @return true if the view is closed.
     */
    val isClosed: Boolean
        get() = isClosedAtLeft || isClosedAtRight

    /**
     * Check if dragged view is above the middle of the custom view.
     *
     * @return true if dragged view is above the middle of the custom view or false if is below.
     */
     val isDragViewAboveTheMiddle: Boolean
        get() = transformer!!.isAboveTheMiddle

    /**
     * Check if dragged view is next to the left bound.
     *
     * @return true if dragged view right position is behind the right half of the custom view.
     */
     val isNextToLeftBound: Boolean
        get() = transformer!!.isNextToLeftBound

    /**
     * Check if dragged view is next to the right bound.
     *
     * @return true if dragged view left position is behind the left quarter of the custom view.
     */
     val isNextToRightBound: Boolean
        get() = transformer!!.isNextToRightBound

    /**
     * Check if dragged view is at the top of the custom view.
     *
     * @return true if dragged view top position is equals to zero.
     */
     val isDragViewAtTop: Boolean
        get() = transformer!!.isViewAtTop

    /**
     * Check if dragged view is at the right of the custom view.
     *
     * @return true if dragged view right position is equals to custom view width.
     */
     val isDragViewAtRight: Boolean
        get() = transformer!!.isViewAtRight

    /**
     * Check if dragged view is at the bottom of the custom view.
     *
     * @return true if dragged view bottom position is equals to custom view height.
     */
     val isDragViewAtBottom: Boolean
        get() = transformer!!.isViewAtBottom

    /**
     * @return configured dragged view margin right configured.
     */
    private val dragViewMarginRight: Int
        get() = transformer!!.marginRight

    /**
     * @return configured dragged view margin bottom.
     */
    private val dragViewMarginBottom: Int
        get() = transformer!!.marginBottom

    /**
     * Calculate the dragged view left position normalized between 1 and 0.
     *
     * @return absolute value between the dragged view  left position divided by custon view width
     */
    private val horizontalDragOffset: Float
        get() = (abs(dragView!!.left) / width ).toFloat()

    /**
     * Calculate the dragged view  top position normalized between 1 and 0.
     *
     * @return dragged view top divided by vertical drag range.
     */
    private val verticalDragOffset: Float
        get() = dragView!!.getTop() / verticalDragRange

    /**
     * Calculate the vertical drag range between the custom view and dragged view.
     *
     * @return the difference between the custom view height and the dragged view height.
     */
    private val verticalDragRange: Float
        get() = (getHeight() - transformer!!.minHeightPlusMargin).toFloat()

    val draggedViewHeightPlusMarginTop: Int
        get() = transformer!!.minHeightPlusMargin

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initializeAttributes(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        initializeAttributes(attrs)
    }

    /**
     * Slide the view based on scroll of the nav drawer.
     * "setEnableTouch" user prevents click to expand while the drawer is moving, it will be
     * set to false when the @slideOffset is bigger than MIN_SLIDE_OFFSET.
     * When the slideOffset is bigger than 0.1 and dragView isn't close, set the dragView
     * to minimized.
     * It's only possible to maximize the view when @slideOffset is equals to 0.0,
     * in other words, closed.
     *
     * @param slideOffset Value between 0 and 1, represent the value of slide:
     * 0.0 is equal to close drawer and 1.0 equals open drawer.
     * @param drawerPosition Represent the position of nav drawer on X axis.
     * @param width Width of nav drawer
     */
    fun slideHorizontally(slideOffset: Float, drawerPosition: Float, width: Int) {
        if (slideOffset > MIN_SLIDE_OFFSET && !isClosed && isMaximized) {
            minimize()
        }
        isTouchEnabled = slideOffset <= MIN_SLIDE_OFFSET
        ViewHelper.setX(this, width - abs(drawerPosition))
    }

    /**
     * Configure the horizontal scale factor applied when the view is dragged to the bottom of the
     * custom view.
     */
    fun setXTopViewScaleFactor(xScaleFactor: Float) {
        transformer!!.xScaleFactor=xScaleFactor
    }

    /**
     * Configure the vertical scale factor applied when the view is dragged to the bottom of the
     * custom view.
     */
    fun setYTopViewScaleFactor(yScaleFactor: Float) {
        transformer!!.yScaleFactor=yScaleFactor
    }

    /**
     * Configure the dragged view margin right applied when the dragged view is minimized.
     *
     * @param topFragmentMarginRight in pixels.
     */
    fun setTopViewMarginRight(topFragmentMarginRight: Int) {
        transformer!!.marginRight=topFragmentMarginRight
    }

    /**
     * Configure the dragView margin bottom applied when the dragView is minimized.
     */
    fun setTopViewMarginBottom(topFragmentMarginBottom: Int) {
        transformer!!.marginBottom=topFragmentMarginBottom
    }

    /**
     * Configure the dragged view height.
     *
     * @param topFragmentHeight in pixels
     */
    fun setTopViewHeight(topFragmentHeight: Int) {
        transformer!!.setViewHeight(topFragmentHeight)
    }

    /**
     * Configure the disabling of the alpha effect applied when the dragView is dragged horizontally.
     */
    fun setHorizontalAlphaEffectEnabled(enableHorizontalAlphaEffect: Boolean) {
        this.enableHorizontalAlphaEffect = enableHorizontalAlphaEffect
    }

    /**
     * Configure the DraggableListener notified when the view is minimized, maximized, closed to the
     * right or closed to the left.
     */
    fun setDraggableListener(listener: DraggableListener) {
        this.listener = listener
    }

    /**
     * Configure DraggableView to resize top view instead of scale it.
     */
    fun setTopViewResize(topViewResize: Boolean) {
        this.topViewResize = topViewResize
        initializeTransformer()
    }

    /**
     * To ensure the animation is going to work this method has been override to call
     * postInvalidateOnAnimation if the view is not settled yet.
     */
    override fun computeScroll() {
        if (!isInEditMode && viewDragHelper!!.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }

    /**
     * Maximize the custom view applying an animation to return the view to the initial position.
     */
    fun maximize() {
        smoothSlideTo(SLIDE_TOP)
        notifyMaximizeToListener()
    }

    /**
     * Minimize the custom view applying an animation to put the top fragment on the bottom right
     * corner of the screen.
     */
    fun minimize() {
        smoothSlideTo(SLIDE_BOTTOM)
        notifyMinimizeToListener()
    }

    /**
     * Close the custom view applying an animation to close the view to the right side of the screen.
     */
    fun closeToRight() {
        if (viewDragHelper!!.smoothSlideViewTo(dragView!!, transformer!!.getOriginalWidth(),
                        height - transformer!!.minHeightPlusMargin)) {
            ViewCompat.postInvalidateOnAnimation(this)
            notifyCloseToRightListener()
        }
    }

    /**
     * Close the custom view applying an animation to close the view to the left side of the screen.
     */
    fun closeToLeft() {
        if (viewDragHelper!!.smoothSlideViewTo(dragView!!, -transformer!!.getOriginalWidth(),
                        height - transformer!!.minHeightPlusMargin)) {
            ViewCompat.postInvalidateOnAnimation(this)
            notifyCloseToLeftListener()
        }
    }

    /**
     * Override method to intercept only touch events over the drag view and to cancel the drag when
     * the action associated to the MotionEvent is equals to ACTION_CANCEL or ACTION_UP.
     *
     * @param ev captured.
     * @return true if the view is going to process the touch event or false if not.
     */
    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        if (!isEnabled) {
            return false
        }
        when (MotionEventCompat.getActionMasked(ev) and MotionEventCompat.ACTION_MASK) {
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                viewDragHelper!!.cancel()
                return false
            }
            MotionEvent.ACTION_DOWN -> {
                val index = MotionEventCompat.getActionIndex(ev)
                activePointerId = MotionEventCompat.getPointerId(ev, index)
                if (activePointerId == INVALID_POINTER) {
                    return false
                }
            }
            else -> {
            }
        }
        val interceptTap = viewDragHelper!!.isViewUnder(dragView, ev.getX().toInt(), ev.getY().toInt())
        return viewDragHelper!!.shouldInterceptTouchEvent(ev) || interceptTap
    }

    /**
     * Override method to dispatch touch event to the dragged view.
     *
     * @param ev captured.
     * @return true if the touch event is realized over the drag or second view.
     */
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent): Boolean {
        val actionMasked = MotionEventCompat.getActionMasked(ev)
        if (actionMasked and MotionEventCompat.ACTION_MASK == MotionEvent.ACTION_DOWN) {
            activePointerId = MotionEventCompat.getPointerId(ev, actionMasked)
        }
        if (activePointerId == INVALID_POINTER) {
            return false
        }
        viewDragHelper!!.processTouchEvent(ev)
        if (isClosed) {
            return false
        }
        val isDragViewHit = isViewHit(dragView!!, ev.x.toInt(), ev.y.toInt())
        val isSecondViewHit = isViewHit(secondView!!, ev.x.toInt(), ev.y.toInt())
        analyzeTouchToMaximizeIfNeeded(ev, isDragViewHit)
        if (isMaximized) {
            dragView!!.dispatchTouchEvent(ev)
        } else {
            dragView!!.dispatchTouchEvent(cloneMotionEventWithAction(ev, MotionEvent.ACTION_CANCEL))
        }
        return isDragViewHit || isSecondViewHit
    }

    private fun analyzeTouchToMaximizeIfNeeded(ev: MotionEvent, isDragViewHit: Boolean) {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> lastTouchActionDownXPosition = ev.x
            MotionEvent.ACTION_UP -> {
                val clickOffset = ev.x - lastTouchActionDownXPosition
                if (shouldMaximizeOnClick(ev, clickOffset, isDragViewHit)) {
                    if (isMinimized && isClickToMaximizeEnabled) {
                        maximize()
                    } else if (isMaximized && isClickToMinimizeEnabled) {
                        minimize()
                    }
                }
            }
            else -> {
            }
        }
    }

    private fun shouldMaximizeOnClick(ev: MotionEvent, deltaX: Float, isDragViewHit: Boolean): Boolean {
        return (abs(deltaX) < MIN_SLIDING_DISTANCE_ON_CLICK
                && ev.action != MotionEvent.ACTION_MOVE
                && isDragViewHit)
    }

    /**
     * Clone given motion event and set specified action. This method is useful, when we want to
     * cancel event propagation in child views by sending event with [ ][android.view.MotionEvent.ACTION_CANCEL]
     * action.
     *
     * @param event event to clone
     * @param action new action
     * @return cloned motion event
     */
    private fun cloneMotionEventWithAction(event: MotionEvent, action: Int): MotionEvent {
        return MotionEvent.obtain(event.getDownTime(), event.getEventTime(), action, event.getX(),
                event.getY(), event.getMetaState())
    }

    /**
     * Override method to configure the dragged view and secondView layout properly.
     */
     override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        when {
            isInEditMode() -> super.onLayout(changed, left, top, right, bottom)
            isDragViewAtTop -> {
                dragView!!.layout(left, top, right, transformer!!.getOriginalHeight())
                secondView!!.layout(left, transformer!!.getOriginalHeight(), right, bottom)
                ViewHelper.setY(dragView!!, top.toFloat())
                ViewHelper.setY(secondView!!, transformer!!.getOriginalHeight().toFloat())
            }
            else -> secondView!!.layout(left, transformer!!.getOriginalHeight(), right, bottom)
        }
    }

    /**
     * Override method to map dragged view, secondView to view objects, to configure dragged
     * view height and to initialize DragViewHelper.
     */
    override fun onFinishInflate() {
        super.onFinishInflate()
        if (!isInEditMode) {
            mapGUI()
            initializeTransformer()
            initializeViewDragHelper()
        }
    }

    private fun mapGUI() {
        dragView = findViewById(dragViewId)
        secondView = findViewById(secondViewId)
    }

    /**
     * Configure the FragmentManager used to attach top and bottom Fragments to the view. The
     * FragmentManager is going to be provided only by DraggablePanel view.
     */
     fun setFragmentManager(fragmentManager: FragmentManager) {
        this.fragmentManager = fragmentManager
    }

    /**
     * Attach one fragment to the dragged view.
     *
     * @param topFragment to be attached.
     */
     fun attachTopFragment(topFragment: Fragment) {
        addFragmentToView(R.id.drag_view, topFragment)
    }

    /**
     * Attach one fragment to the secondView.
     *
     * @param bottomFragment to be attached.
     */
     fun attachBottomFragment(bottomFragment: Fragment) {
        addFragmentToView(R.id.second_view, bottomFragment)
    }

    /**
     * Modify dragged view pivot based on the dragged view vertical position to simulate a horizontal
     * displacement while the view is dragged.
     */
     fun changeDragViewPosition() {
        transformer!!.updatePosition(verticalDragOffset)
    }

    /**
     * Modify secondView position to be always below dragged view.
     */
     fun changeSecondViewPosition() {
        ViewHelper.setY(secondView!!, dragView!!.bottom.toFloat())
    }

    /**
     * Modify dragged view scale based on the dragged view vertical position and the scale factor.
     */
     fun changeDragViewScale() {
        transformer!!.updateScale(verticalDragOffset)
    }

    /**
     * Modify the background alpha if has been configured to applying an alpha effect when the view
     * is dragged.
     */
     fun changeBackgroundAlpha() {
        val background = background
        if (background != null) {
            val newAlpha = (ONE_HUNDRED * (1 - verticalDragOffset)).toInt()
            background.alpha = newAlpha
        }
    }

    /**
     * Modify the second view alpha based on dragged view vertical position.
     */
     fun changeSecondViewAlpha() {
        ViewHelper.setAlpha(secondView!!, 1 - verticalDragOffset)
    }

    /**
     * Modify dragged view alpha based on the horizontal position while the view is being
     * horizontally dragged.
     */
     fun changeDragViewViewAlpha() {
        if (enableHorizontalAlphaEffect) {
            var alpha = 1 - horizontalDragOffset
            if (alpha == 0f) {
                alpha = 1f
            }
            ViewHelper.setAlpha(dragView!!, alpha)
        }
    }

    /**
     * Restore view alpha to 1
     */
     fun restoreAlpha() {
        if (enableHorizontalAlphaEffect && ViewHelper.getAlpha(dragView!!) < 1) {
            ViewHelper.setAlpha(dragView!!, 1F)
        }
    }

    /**
     * Calculate if one position is above any view.
     *
     * @param view to analyze.
     * @param x position.
     * @param y position.
     * @return true if x and y positions are below the view.
     */
    private fun isViewHit(view: View, x: Int, y: Int): Boolean {
        val viewLocation = IntArray(2)
        view.getLocationOnScreen(viewLocation)
        val parentLocation = IntArray(2)
        this.getLocationOnScreen(parentLocation)
        val screenX = parentLocation[0] + x
        val screenY = parentLocation[1] + y
        return (screenX >= viewLocation[0]
                && screenX < viewLocation[0] + view.getWidth()
                && screenY >= viewLocation[1]
                && screenY < viewLocation[1] + view.getHeight())
    }

    /**
     * Use FragmentManager to attach one fragment to one view using the viewId.
     *
     * @param viewId used to obtain the view.
     * @param fragment to be attached.
     */
    private fun addFragmentToView(viewId: Int, fragment: Fragment) {
        fragmentManager!!.beginTransaction().replace(viewId, fragment).commit()
    }

    /**
     * Initialize the viewDragHelper.
     */
    private fun initializeViewDragHelper() {
        viewDragHelper = ViewDragHelper.create(this, SENSITIVITY, DraggableViewCallback(this, dragView!!))
    }

    /**
     * Initialize Transformer with a scalable or change width/height implementation.
     */
    private fun initializeTransformer() {
        val transformerFactory = TransformerFactory()
        transformer = transformerFactory.getTransformer(topViewResize, dragView!!, this)
        transformer!!.setViewHeight(topViewHeight)
        transformer!!.xScaleFactor=(scaleFactorX)
        transformer!!.yScaleFactor=(scaleFactorY)
        transformer!!.marginRight=(marginRight)
        transformer!!.marginBottom=(marginBottom)
    }

    /**
     * Initialize XML attributes.
     *
     * @param attrs to be analyzed.
     */
    private fun initializeAttributes(attrs: AttributeSet) {
        val attributes = getContext().obtainStyledAttributes(attrs, R.styleable.draggable_view)
        this.enableHorizontalAlphaEffect = attributes.getBoolean(R.styleable.draggable_view_enable_minimized_horizontal_alpha_effect,
                DEFAULT_ENABLE_HORIZONTAL_ALPHA_EFFECT)
        this.isClickToMaximizeEnabled = attributes.getBoolean(R.styleable.draggable_view_enable_click_to_maximize_view,
                DEFAULT_ENABLE_CLICK_TO_MAXIMIZE)
        this.isClickToMinimizeEnabled = attributes.getBoolean(R.styleable.draggable_view_enable_click_to_minimize_view,
                DEFAULT_ENABLE_CLICK_TO_MINIMIZE)
        this.topViewResize = attributes.getBoolean(R.styleable.draggable_view_top_view_resize, DEFAULT_TOP_VIEW_RESIZE)
        this.topViewHeight = attributes.getDimensionPixelSize(R.styleable.draggable_view_top_view_height,
                DEFAULT_TOP_VIEW_HEIGHT)
        this.scaleFactorX = attributes.getFloat(R.styleable.draggable_view_top_view_x_scale_factor,
            DEFAULT_SCALE_FACTOR.toFloat()
        )
        this.scaleFactorY = attributes.getFloat(R.styleable.draggable_view_top_view_y_scale_factor,
            DEFAULT_SCALE_FACTOR.toFloat()
        )
        this.marginBottom = attributes.getDimensionPixelSize(R.styleable.draggable_view_top_view_margin_bottom,
                DEFAULT_TOP_VIEW_MARGIN)
        this.marginRight = attributes.getDimensionPixelSize(R.styleable.draggable_view_top_view_margin_right,
                DEFAULT_TOP_VIEW_MARGIN)
        this.dragViewId = attributes.getResourceId(R.styleable.draggable_view_top_view_id, R.id.drag_view)
        this.secondViewId = attributes.getResourceId(R.styleable.draggable_view_bottom_view_id, R.id.second_view)
        attributes.recycle()
    }

    /**
     * Realize an smooth slide to an slide offset passed as argument. This method is the base of
     * maximize, minimize and close methods.
     *
     * @param slideOffset to apply
     * @return true if the view is slided.
     */
    private fun smoothSlideTo(slideOffset: Float): Boolean {
        val topBound = paddingTop
        val x = (slideOffset * (width - transformer!!.minWidthPlusMarginRight)).toInt()
        val y = (topBound + slideOffset * verticalDragRange).toInt()
        if (viewDragHelper!!.smoothSlideViewTo(dragView!!, x, y)) {
            ViewCompat.postInvalidateOnAnimation(this)
            return true
        }
        return false
    }

    /**
     * Notify te view is maximized to the DraggableListener
     */
    private fun notifyMaximizeToListener() {
        if (listener != null) {
            listener!!.onMaximized()
        }
    }

    /**
     * Notify te view is minimized to the DraggableListener
     */
    private fun notifyMinimizeToListener() {
        if (listener != null) {
            listener!!.onMinimized()
        }
    }

    /**
     * Notify te view is closed to the right to the DraggableListener
     */
    private fun notifyCloseToRightListener() {
        if (listener != null) {
            listener!!.onClosedToRight()
        }
    }

    /**
     * Notify te view is closed to the left to the DraggableListener
     */
    private fun notifyCloseToLeftListener() {
        if (listener != null) {
            listener!!.onClosedToLeft()
        }
    }

    companion object {

        private val DEFAULT_SCALE_FACTOR = 2
        private val DEFAULT_TOP_VIEW_MARGIN = 30
        private val DEFAULT_TOP_VIEW_HEIGHT = -1
        private val SLIDE_TOP = 0f
        private val SLIDE_BOTTOM = 1f
        private val MIN_SLIDE_OFFSET = 0.1f
        private val DEFAULT_ENABLE_HORIZONTAL_ALPHA_EFFECT = true
        private val DEFAULT_ENABLE_CLICK_TO_MAXIMIZE = false
        private val DEFAULT_ENABLE_CLICK_TO_MINIMIZE = false
        private val DEFAULT_ENABLE_TOUCH_LISTENER = true
        private val MIN_SLIDING_DISTANCE_ON_CLICK = 10
        private val ONE_HUNDRED = 100
        private val SENSITIVITY = 1f
        private val DEFAULT_TOP_VIEW_RESIZE = false
        private val INVALID_POINTER = -1
    }
}
