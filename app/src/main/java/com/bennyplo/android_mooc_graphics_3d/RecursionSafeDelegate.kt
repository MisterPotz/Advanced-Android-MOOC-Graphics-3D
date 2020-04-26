package com.bennyplo.android_mooc_graphics_3d

import java.lang.IllegalStateException

abstract class RecursionSafeDelegate<T : ParameterScope>(
        private val connectableObject: ConnectableObject,
        private val performSafelyBlock: (obj: ConnectableObject, scope: T) -> Unit,
        private val performOnOthersBlock: (obj: ConnectableObject, scope: T, record: TransformationInfo) -> Unit) {

    fun wasAlreadyPerformed(record: TransformationInfo): Boolean {
        if (connectableObject in record.objs.keys &&
                (!record.objs[connectableObject]!!.mustBeExecuted ||
                        (record.objs[connectableObject]!!.isBlocking && !record.objs[connectableObject]!!.mustBeExecuted))) {
            return true
        }
        return false
    }

    private fun performSafely(
            record: TransformationInfo,
            parameterScope: T
    ): TransformationInfo {
        if (!wasAlreadyPerformed(record)) {
            performSafelyBlock(connectableObject, parameterScope)
        } else {
            // if this object was already once drawn - escape drawing recursion
            throw IllegalStateException("this object was already processed")
        }
        return record.apply {
            reputOnExecution(this)
        }
    }


    private fun reputOnExecution(record: TransformationInfo) {
        if (connectableObject in record.objs) {
            if (record.objs[connectableObject]!!.mustBeExecuted) {
                val info = record.objs[connectableObject]!!
                record.objs.remove(connectableObject)
                record.objs[connectableObject] = info.copy(mustBeExecuted = false)
            } else {
                throw IllegalStateException("Was already executed - mustBeExecuted = false")
            }
        } else {
            record.objs[connectableObject] = PerObjectTransformationInfo(isBlocking = false, mustBeExecuted = false)
        }
    }

    private fun shallContinue(record: TransformationInfo) : Boolean{
        if (connectableObject in record.objs) {
            if (record.objs[connectableObject]!!.isBlocking) {
                return false
            }
        }
        return true
    }

    private fun performOnOthers(others: Collection<ConnectableObject?>, record: TransformationInfo, scope: T) {
        for (i in others) {
            i?.let {
                performOnOthersBlock(it, scope, record)
            }
        }
    }

    /**
     * If given functions are enough to perform an action
     */
    fun perform(record: TransformationInfo, others: Collection<ConnectableObject?>, scope: T) {
        if (wasAlreadyPerformed(record)) {
            return
        }
        val exceptNew = performSafely(record, scope)
        if (shallContinue(exceptNew)) {
            performOnOthers(others, exceptNew, scope)
        }
    }
}

interface ParameterScope