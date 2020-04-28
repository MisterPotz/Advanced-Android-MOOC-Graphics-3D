package com.bennyplo.android_mooc_graphics_3d

class ActionExecutor(val standardDelay: Long, val onStopCallback: (ActionExecutor) -> Unit) {
    private var isActive: Boolean = false

    /**
     * accepts current step index
     */
    private var currentAction: ((Long) -> Unit)? = null
    private var stepsLeft: Long = 0

    fun startExecuting(totalTime: Long, action: (Long) -> Unit) {
        isActive = true
        stepsLeft = totalTime / standardDelay
        currentAction = action
    }

    /**
     * returns [true] if still active
     */
    fun executeIfActive() : Boolean{
        if (isActive) {
            currentAction!!.invoke(stepsLeft)
            stepsLeft -= 1
            if (stepsLeft < 0) {
                stepsLeft = 0;
                isActive = false
                currentAction = null
                onStopCallback(this)
            }
        }
        return isActive
    }
}

class MediatorActionExecutor(val standardDelay: Long) {
    private val executorsList: MutableList<ActionExecutor> = mutableListOf()

    fun postAction(totalTime: Long, action: (Long) -> Unit) {
        // on stop remove from list
        val actionExecutor = ActionExecutor(standardDelay) {

        }
        executorsList.add(actionExecutor)
        actionExecutor.startExecuting(totalTime, action)
    }

    fun postAction(totalTime: Long, action: (Long) -> Unit, callback: () -> Unit) {
        val actionExecutor = ActionExecutor(standardDelay) {
            callback()
        }
        executorsList.add(actionExecutor)
        actionExecutor.startExecuting(totalTime, action)
    }


    fun execute() {
        executorsList.filter {
            !it.executeIfActive()
        }.let {
            executorsList.removeAll(it)
        }
    }
}