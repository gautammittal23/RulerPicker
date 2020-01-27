package com.a.ruler

import android.content.Context
import android.widget.HorizontalScrollView
import androidx.annotation.Nullable


/**
 * @author Gautam Mittal
 * 28/1/20
 */

class ObservableHorizontalScrollView(
    context: Context?,
    scrollChangedListener: ScrollChangedListener
) : HorizontalScrollView(context) {
    companion object {
        private var NEW_CHECK_DURATION = 100L
        private var mLastScrollUpdateMills: Long = -1
    }


    @Nullable
    private val mScrollChangedListener: ScrollChangedListener? = null

    private val mScrollerTask: Runnable = object : Runnable {
        override fun run() {
            if (System.currentTimeMillis() - mLastScrollUpdateMills > NEW_CHECK_DURATION) {
                mLastScrollUpdateMills = -1
                mScrollChangedListener!!.onScrollStopped()
            } else { //Post next delay
                postDelayed(this, NEW_CHECK_DURATION)
            }
        }
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)

        if (mScrollChangedListener == null) return
        mScrollChangedListener.onScrollChanged()


        /*  if (mLastScrollUpdateMills.compareTo(-1)) {
              postDelayed(mScrollerTask, NEW_CHECK_DURATION)
          }*/


        mLastScrollUpdateMills = System.currentTimeMillis();
    }


    interface ScrollChangedListener {
        /**
         * Called upon change in scroll position.
         */
        fun onScrollChanged()

        /**
         * Called when the scrollview stops scrolling.
         */
        fun onScrollStopped()
    }

}

