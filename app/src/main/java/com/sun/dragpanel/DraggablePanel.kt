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

import android.content.Context


import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager


/**
 * Custom view created to handle DraggableView using fragments. With this custom view the client
 * code can configure the
 * top and bottom fragment and other elements like: top fragment height, top fragment margin right,
 * top fragment x
 * scale factor, top fragment y scale factor, top fragment margin bottom and enable or disable
 * horizontal alpha effect.
 *
 * @author Pedro Vicente Gómez Sánchez.
 */
class DraggablePanel : FrameLayout {

    private var draggableView: DraggableView? = null
    private var draggableListener: DraggableListener? = null

    private var fragmentManager: FragmentManager? = null
    private var topFragment: Fragment? = null
    private var bottomFragment: Fragment? = null
    private var topFragmentHeight: Int = 0
    private var topFragmentMarginRight: Int = 0
    private var topFragmentMarginBottom: Int = 0
    private var xScaleFactor: Float = 0.toFloat()
    private var yScaleFactor: Float = 0.toFloat()
    private var enableHorizontalAlphaEffect: Boolean = false
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
    private var enableTouchListener: Boolean = false

    /**
     * Checks if the top Fragment is maximized.
     *
     * @return true if the view is maximized.
     */
    val isMaximized: Boolean
        get() = draggableView!!.isMaximized

    /**
     * Checks if the top Fragment is minimized.
     *
     * @return true if the view is minimized.
     */
    val isMinimized: Boolean
        get() = draggableView!!.isMinimized

    /**
     * Checks if the top Fragment closed at the right place.
     *
     * @return true if the view is closed at right.
     */
    val isClosedAtRight: Boolean
        get() = draggableView!!.isClosedAtRight

    /**
     * Checks if the top Fragment is closed at the left place.
     *
     * @return true if the view is closed at left.
     */
    val isClosedAtLeft: Boolean
        get() = draggableView!!.isClosedAtLeft

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initializeAttrs(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        initializeAttrs(attrs)
    }

    /**
     * Configure the FragmentManager used to attach top and bottom fragment inside the view.
     */
    fun setFragmentManager(fragmentManager: FragmentManager) {
        this.fragmentManager = fragmentManager
    }

    /**
     * Configure the Fragment that will work as draggable element inside this custom view. This
     * Fragment has to be configured before initialize the view.
     *
     * @param topFragment used as draggable element.
     */
    fun setTopFragment(topFragment: Fragment) {
        this.topFragment = topFragment
    }

    /**
     * Configure the Fragment that will work as secondary element inside this custom view. This
     * Fragment has to be configured before initialize the view.
     *
     * @param bottomFragment used as secondary element.
     */
    fun setBottomFragment(bottomFragment: Fragment) {
        this.bottomFragment = bottomFragment
    }

    /**
     * Configure the height associated to the top Fragment used inside the view as draggable element.
     *
     * @param topFragmentHeight in pixels.
     */
    fun setTopViewHeight(topFragmentHeight: Int) {
        this.topFragmentHeight = topFragmentHeight
    }

    /**
     *
     * Slide the view based on scroll of the nav drawer.
     * "setEnableTouch" user prevents click to expand while the drawer is moving.
     * It's only possible to maximize the view when @slideOffset is equals to 0.0,
     * in other words, closed.
     *
     * @param slideOffset Value between 0 and 1, represent the value of slide:
     * 0.0 is equal to close drawer and 1.0 equals open drawer.
     * @param drawerPosition Represent the position of nav drawer on X axis.
     * @param width Width of nav drawer
     */
    fun slideHorizontally(slideOffset: Float, drawerPosition: Float, width: Int) {
        draggableView!!.slideHorizontally(slideOffset, drawerPosition, width)
    }

    /**
     * Configure the horizontal scale factor applied when the top fragment is dragged to the bottom
     * of the custom view.
     */
    fun setXScaleFactor(xScaleFactor: Float) {
        this.xScaleFactor = xScaleFactor
    }

    /**
     * Configure the vertical scale factor applied when the top fragment is dragged to the bottom of
     * the custom view.
     */
    fun setYScaleFactor(yScaleFactor: Float) {
        this.yScaleFactor = yScaleFactor
    }

    /**
     * Configure the top Fragment margin right applied when the view has been minimized.
     *
     * @param topFragmentMarginRight in pixels.
     */
    fun setTopFragmentMarginRight(topFragmentMarginRight: Int) {
        this.topFragmentMarginRight = topFragmentMarginRight
    }

    /**
     * Configure the top Fragment margin bottom applied when the view has been minimized.
     *
     * @param topFragmentMarginBottom in pixels.
     */
    fun setTopFragmentMarginBottom(topFragmentMarginBottom: Int) {
        this.topFragmentMarginBottom = topFragmentMarginBottom
    }

    /**
     * Configure the DraggableListener that is going to be invoked when the view be minimized,
     * maximized, closed to the left or right.
     */
    fun setDraggableListener(draggableListener: DraggableListener) {
        this.draggableListener = draggableListener
    }

    /**
     * Configure the disabling of the alpha effect applied when the view is being dragged
     * horizontally.
     *
     * @param enableHorizontalAlphaEffect to enable or disable the effect.
     */
    fun setEnableHorizontalAlphaEffect(enableHorizontalAlphaEffect: Boolean) {
        this.enableHorizontalAlphaEffect = enableHorizontalAlphaEffect
    }

    /**
     * Configure the top Fragment to resize instead of scale it.
     */
    fun setTopFragmentResize(topViewResize: Boolean) {
        draggableView!!.setTopViewResize(topViewResize)
    }

    /**
     * Close the custom view applying an animation to close the view to the left side of the screen.
     */
    fun closeToLeft() {
        draggableView!!.closeToLeft()
    }

    /**
     * Close the custom view applying an animation to close the view to the right side of the screen.
     */
    fun closeToRight() {
        draggableView!!.closeToRight()
    }

    /**
     * Maximize the custom view applying an animation to return the view to the initial position.
     */
    fun maximize() {
        draggableView!!.maximize()
    }

    /**
     * Minimize the custom view applying an animation to put the top fragment on the bottom right
     * corner of the screen.
     */
    fun minimize() {
        draggableView!!.minimize()
    }

    /**
     * Apply all the custom view configuration and inflate the main widgets. The view won't be
     * visible if this method is not called.
     *
     *
     * FragmentManager, top Fragment and bottom Fragment have to be configured before initialize this
     * view. If not, this method will throw and IllegalStateException.
     */
    fun initializeView() {
        checkFragmentConsistency()
        checkSupportFragmentManagerConsistency()

        inflate(context, R.layout.draggable_panel, this)
        draggableView = findViewById<DraggableView>(R.id.draggable_view)
        draggableView!!.setTopViewHeight(topFragmentHeight)
        draggableView!!.setFragmentManager(fragmentManager!!)
        draggableView!!.attachTopFragment(topFragment!!)
        draggableView!!.setXTopViewScaleFactor(xScaleFactor)
        draggableView!!.setYTopViewScaleFactor(yScaleFactor)
        draggableView!!.setTopViewMarginRight(topFragmentMarginRight)
        draggableView!!.setTopViewMarginBottom(topFragmentMarginBottom)
        draggableView!!.attachBottomFragment(bottomFragment!!)
        draggableView!!.setDraggableListener(draggableListener!!)
        draggableView!!.setHorizontalAlphaEffectEnabled(enableHorizontalAlphaEffect)
        draggableView!!.isClickToMaximizeEnabled=(isClickToMaximizeEnabled)
        draggableView!!.isClickToMinimizeEnabled=(isClickToMinimizeEnabled)
        draggableView!!.isTouchEnabled=(enableTouchListener)
    }

    /**
     * Initialize the xml configuration based on styleable attributes
     *
     * @param attrs to analyze.
     */
    private fun initializeAttrs(attrs: AttributeSet) {
        val attributes = getContext().obtainStyledAttributes(attrs, R.styleable.draggable_panel)
        this.topFragmentHeight = attributes.getDimensionPixelSize(
            R.styleable.draggable_panel_top_fragment_height,
            DEFAULT_TOP_FRAGMENT_HEIGHT
        )
        this.xScaleFactor = attributes.getFloat(R.styleable.draggable_panel_x_scale_factor, DEFAULT_SCALE_FACTOR)
        this.yScaleFactor = attributes.getFloat(R.styleable.draggable_panel_y_scale_factor, DEFAULT_SCALE_FACTOR)
        this.topFragmentMarginRight = attributes.getDimensionPixelSize(
            R.styleable.draggable_panel_top_fragment_margin_right,
            DEFAULT_TOP_FRAGMENT_MARGIN
        )
        this.topFragmentMarginBottom = attributes.getDimensionPixelSize(
            R.styleable.draggable_panel_top_fragment_margin_bottom,
            DEFAULT_TOP_FRAGMENT_MARGIN
        )
        this.enableHorizontalAlphaEffect = attributes.getBoolean(
            R.styleable.draggable_panel_enable_horizontal_alpha_effect,
            DEFAULT_ENABLE_HORIZONTAL_ALPHA_EFFECT
        )
        this.isClickToMaximizeEnabled = attributes.getBoolean(
            R.styleable.draggable_panel_enable_click_to_maximize_panel,
            DEFAULT_ENABLE_CLICK_TO_MAXIMIZE
        )
        this.isClickToMinimizeEnabled = attributes.getBoolean(
            R.styleable.draggable_panel_enable_click_to_minimize_panel,
            DEFAULT_ENABLE_CLICK_TO_MINIMIZE
        )
        this.enableTouchListener = attributes.getBoolean(
            R.styleable.draggable_panel_enable_touch_listener_panel,
            DEFAULT_ENABLE_TOUCH_LISTENER
        )
        attributes.recycle()
    }

    /**
     * Validate FragmentManager configuration. If is not initialized, this method will throw an
     * IllegalStateException.
     */
    private fun checkSupportFragmentManagerConsistency() {
        if (fragmentManager == null) {
            throw IllegalStateException(
                "You have to set the support FragmentManager before initialize DraggablePanel"
            )
        }
    }

    /**
     * Validate top and bottom Fragment configuration. If are not initialized, this method will throw
     * an IllegalStateException.
     */
    private fun checkFragmentConsistency() {
        if (topFragment == null || bottomFragment == null) {
            throw IllegalStateException(
                "You have to set top and bottom fragment before initialize DraggablePanel"
            )
        }
    }

    companion object {

        private val DEFAULT_TOP_FRAGMENT_HEIGHT = 200
        private val DEFAULT_TOP_FRAGMENT_MARGIN = 0
        private val DEFAULT_SCALE_FACTOR = 2f
        private val DEFAULT_ENABLE_HORIZONTAL_ALPHA_EFFECT = true
        private val DEFAULT_ENABLE_CLICK_TO_MAXIMIZE = false
        private val DEFAULT_ENABLE_CLICK_TO_MINIMIZE = false
        private val DEFAULT_ENABLE_TOUCH_LISTENER = true
        private val DEFAULT_TOP_FRAGMENT_RESIZE = false
    }
}
