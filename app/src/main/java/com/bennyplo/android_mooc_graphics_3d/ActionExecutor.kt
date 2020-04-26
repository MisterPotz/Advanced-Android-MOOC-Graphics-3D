package com.bennyplo.android_mooc_graphics_3d

class ActionExecutor(val standardDelay: Long) {
    private var isActive: Boolean = false

    /**
     * accepts current step index
     */
    private var currentAction: ((Long) -> Unit)? = null
    private var stepsLeft : Long = 0

    fun startExecuting(totalTime : Long, action : (Long) -> Unit) {
        isActive = true
        stepsLeft = totalTime / standardDelay
        currentAction = action
    }

    fun executeIfActive() {
        if (isActive) {
            currentAction!!.invoke(stepsLeft)
            stepsLeft -= 1
            if (stepsLeft < 0) {
                stepsLeft = 0;
                isActive = false
                currentAction = null
            }
        }
    }
}