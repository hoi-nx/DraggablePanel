package com.sun.dragpanel

import android.view.View

import com.sun.dragpanel.AnimatorProxy.Companion.NEEDS_PROXY
import com.sun.dragpanel.AnimatorProxy.Companion.wrap

/**
 * Created by nguyenxuanhoi on 2019-08-04.
 *
 * @author nguyen.xuan.hoi@sun-asterisk.com
 */
object ViewHelper {

    fun getAlpha(view: View): Float {
        return if (NEEDS_PROXY) wrap(view).alpha else Honeycomb.getAlpha(view)
    }

    fun setAlpha(view: View, alpha: Float) {
        if (NEEDS_PROXY) {
            wrap(view).alpha = (alpha)
        } else {
            Honeycomb.setAlpha(view, alpha)
        }
    }

    fun getPivotX(view: View): Float {
        return if (NEEDS_PROXY) wrap(view).pivotX else Honeycomb.getPivotX(view)
    }

    fun setPivotX(view: View, pivotX: Float) {
        if (NEEDS_PROXY) {
            wrap(view).pivotX = (pivotX)
        } else {
            Honeycomb.setPivotX(view, pivotX)
        }
    }

    fun getPivotY(view: View): Float {
        return if (NEEDS_PROXY) wrap(view).pivotY else Honeycomb.getPivotY(view)
    }

    fun setPivotY(view: View, pivotY: Float) {
        if (NEEDS_PROXY) {
            wrap(view).pivotY = (pivotY)
        } else {
            Honeycomb.setPivotY(view, pivotY)
        }
    }

    fun getRotation(view: View): Float {
        return if (NEEDS_PROXY) wrap(view).rotation else Honeycomb.getRotation(view)
    }

    fun setRotation(view: View, rotation: Float) {
        if (NEEDS_PROXY) {
            wrap(view).rotation = (rotation)
        } else {
            Honeycomb.setRotation(view, rotation)
        }
    }

    fun getRotationX(view: View): Float {
        return if (NEEDS_PROXY) wrap(view).rotationX else Honeycomb.getRotationX(view)
    }

    fun setRotationX(view: View, rotationX: Float) {
        if (NEEDS_PROXY) {
            wrap(view).rotationX = (rotationX)
        } else {
            Honeycomb.setRotationX(view, rotationX)
        }
    }

    fun getRotationY(view: View): Float {
        return if (NEEDS_PROXY) wrap(view).rotationY else Honeycomb.getRotationY(view)
    }

    fun setRotationY(view: View, rotationY: Float) {
        if (NEEDS_PROXY) {
            wrap(view).rotationY = (rotationY)
        } else {
            Honeycomb.setRotationY(view, rotationY)
        }
    }

    fun getScaleX(view: View): Float {
        return if (NEEDS_PROXY) AnimatorProxy.wrap(view).scaleX else Honeycomb.getScaleX(view)
    }

    fun setScaleX(view: View, scaleX: Float) {
        if (NEEDS_PROXY) {
            wrap(view).scaleX = (scaleX)
        } else {
            Honeycomb.setScaleX(view, scaleX)
        }
    }

    fun getScaleY(view: View): Float {
        return if (NEEDS_PROXY) wrap(view).scaleY else Honeycomb.getScaleY(view)
    }

    fun setScaleY(view: View, scaleY: Float) {
        if (NEEDS_PROXY) {
            wrap(view).scaleY = (scaleY)
        } else {
            Honeycomb.setScaleY(view, scaleY)
        }
    }

    fun getScrollX(view: View): Float {
        return if (NEEDS_PROXY) wrap(view).scrollX.toFloat() else Honeycomb.getScrollX(view)
    }

    fun setScrollX(view: View, scrollX: Int) {
        if (NEEDS_PROXY) {
            wrap(view).scaleX = (scrollX.toFloat())
        } else {
            Honeycomb.setScrollX(view, scrollX)
        }
    }

    fun getScrollY(view: View): Float {
        return if (NEEDS_PROXY) wrap(view).scrollY.toFloat() else Honeycomb.getScrollY(view)
    }

    fun setScrollY(view: View, scrollY: Int) {
        if (NEEDS_PROXY) {
            wrap(view).scrollY = (scrollY)
        } else {
            Honeycomb.setScrollY(view, scrollY)
        }
    }

    fun getTranslationX(view: View): Float {
        return if (NEEDS_PROXY) wrap(view).translationX else Honeycomb.getTranslationX(
            view
        )
    }

    fun setTranslationX(view: View, translationX: Float) {
        if (NEEDS_PROXY) {
            wrap(view).translationX = (translationX)
        } else {
            Honeycomb.setTranslationX(view, translationX)
        }
    }

    fun getTranslationY(view: View): Float {
        return if (NEEDS_PROXY) wrap(view).translationY else Honeycomb.getTranslationY(
            view
        )
    }

    fun setTranslationY(view: View, translationY: Float) {
        if (NEEDS_PROXY) {
            wrap(view).translationY = (translationY)
        } else
            Honeycomb.setTranslationY(view, translationY)
    }


    fun getX(view: View): Float {
        return if (NEEDS_PROXY) wrap(view).x else Honeycomb.getX(view)
    }

    fun setX(view: View, x: Float) {
        if (NEEDS_PROXY) {
            wrap(view).x = (x)
        } else {
            Honeycomb.setX(view, x)
        }
    }

    fun getY(view: View): Float {
        return if (NEEDS_PROXY) wrap(view).y else Honeycomb.getY(view)
    }

    fun setY(view: View, y: Float) {
        if (NEEDS_PROXY) {
            wrap(view).y = y
        } else {
            Honeycomb.setY(view, y)
        }
    }

    private object Honeycomb {
         fun getAlpha(view: View): Float {
            return view.alpha
        }

         fun setAlpha(view: View, alpha: Float) {
            view.alpha = alpha
        }

         fun getPivotX(view: View): Float {
            return view.pivotX
        }

         fun setPivotX(view: View, pivotX: Float) {
            view.pivotX = pivotX
        }

         fun getPivotY(view: View): Float {
            return view.pivotY
        }

         fun setPivotY(view: View, pivotY: Float) {
            view.pivotY = pivotY
        }

         fun getRotation(view: View): Float {
            return view.rotation
        }

         fun setRotation(view: View, rotation: Float) {
            view.rotation = rotation
        }

         fun getRotationX(view: View): Float {
            return view.rotationX
        }

         fun setRotationX(view: View, rotationX: Float) {
            view.rotationX = rotationX
        }

         fun getRotationY(view: View): Float {
            return view.rotationY
        }

         fun setRotationY(view: View, rotationY: Float) {
            view.rotationY = rotationY
        }

         fun getScaleX(view: View): Float {
            return view.scaleX
        }

         fun setScaleX(view: View, scaleX: Float) {
            view.scaleX = scaleX
        }

         fun getScaleY(view: View): Float {
            return view.scaleY
        }

         fun setScaleY(view: View, scaleY: Float) {
            view.scaleY = scaleY
        }

         fun getScrollX(view: View): Float {
            return view.scrollX.toFloat()
        }

         fun setScrollX(view: View, scrollX: Int) {
            view.scrollX = scrollX
        }

         fun getScrollY(view: View): Float {
            return view.scrollY.toFloat()
        }

         fun setScrollY(view: View, scrollY: Int) {
            view.scrollY = scrollY
        }

         fun getTranslationX(view: View): Float {
            return view.translationX
        }

         fun setTranslationX(view: View, translationX: Float) {
            view.translationX = translationX
        }

         fun getTranslationY(view: View): Float {
            return view.translationY
        }

         fun setTranslationY(view: View, translationY: Float) {
            view.translationY = translationY
        }

         fun getX(view: View): Float {
            return view.x
        }

         fun setX(view: View, x: Float) {
            view.x = x
        }

         fun getY(view: View): Float {
            return view.y
        }

         fun setY(view: View, y: Float) {
            view.y = y
        }
    }
}

