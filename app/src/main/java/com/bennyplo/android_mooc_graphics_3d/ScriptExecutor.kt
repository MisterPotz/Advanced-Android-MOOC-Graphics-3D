package com.bennyplo.android_mooc_graphics_3d

/**
 * Executes given commands one after other
 */
class ScriptExecutor(val mediatorExecutor: MediatorActionExecutor) {
    /**
     * Long - time for execution
     */
    private val commandList = mutableListOf<Pair<Long, (Long) -> Unit>>()
    private var current : Int = 0
    private var isExecuting = false
    private var stopCallback : (ScriptExecutor.() -> Unit)? = null
    private var otherToLaunch : ScriptExecutor? = null

    private fun hasNext() : Boolean {
        return current >= commandList.size
    }

    private fun execNext() {
        if (hasNext()) {
            isExecuting = false
            stopCallback?.invoke(this)
            return
        } else {
            val time = commandList[current].first
            val action = commandList[current].second

            mediatorExecutor.postAction(time, action) {
                current += 1
                execNext()
            }
        }
    }

    fun rewind() {
        current = 0
        isExecuting = true
        execNext()
    }

    fun startExec(callback: ScriptExecutor.() -> Unit) {
        current = 0
        stopCallback = callback
        isExecuting = true
        execNext()
    }

    fun justStart() {
        current = 0
        isExecuting = true
        execNext()
    }

    fun bindCallback(callback: ScriptExecutor.() -> Unit) {
        current = 0
        stopCallback = callback
    }

    fun launchOtherAfter(scriptExecutor: ScriptExecutor) {
        otherToLaunch = scriptExecutor
        current = 0
        isExecuting = false
        otherToLaunch!!.justStart()
    }

    fun registerAll(block : ScriptExecutor.() -> Unit) {
        block()
    }

    fun register(time: Long, action: (Long) -> Unit) {
        commandList.add(Pair(time, action))
    }

    fun execIfActive() {
        if (isExecuting) {
            mediatorExecutor.execute()
        }
        else{
            otherToLaunch?.execIfActive()
        }
    }
}