package com.bennyplo.android_mooc_graphics_3d

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import com.bennyplo.android_mooc_graphics_3d.scopes.*

/**
 * [isBlocking] - represents if recursion shoud stop if object with this tag should end
 * [mustBeExecuted] - represents if object still has to be processed
 */
data class PerObjectTransformationInfo(
        val isBlocking: Boolean = false,
        val mustBeExecuted: Boolean = true,
        val onlyTranslate: Boolean = false,
        val rotateAxes: Boolean = true
)

// TODO fix logics to take this into account
data class TransformationInfo(val objs: MutableMap<ConnectableObject, PerObjectTransformationInfo>) {
    companion object {
        fun empty() = TransformationInfo(mutableMapOf())
    }
}

data class ProjectionBesicParameters(val left: Double, val right: Double, val top: Double, val bottom: Double, val near: Double, val far: Double)

/**
 * Interface for transformations on complex interconnected objects.
 * Each method has [except] parameter, it represents objects that the operation has been already performed on.
 * It is used as safe recursion stop.
 */
interface TransformableModel {
    fun shearModel(hx: Double, hy: Double, record: TransformationInfo)

    fun rotateAxisModel(theta: Double, axis: Coordinate, record: TransformationInfo)

    fun scaleModel(sx: Double, sy: Double, sz: Double, record: TransformationInfo)

    fun translateModel(dx: Double, dy: Double, dz: Double, record: TransformationInfo)

    fun localToModel(record: TransformationInfo)

    fun modelToGlobal(record: TransformationInfo)

    /**
     * Projection can be applied only on global level
     */
    fun projectToGlobal(params: ProjectionBesicParameters, record: TransformationInfo)
}

abstract class ConnectableObject(local: Array<Coordinate?>, setupPaint: Paint.() -> Unit = {
    style = Paint.Style.STROKE //Stroke
    color = Color.RED
    strokeWidth = 3f
}) : DrawableObject(local, setupPaint), TransformableModel {
    var model: Array<Coordinate?> = local.copyOf()

    // to connect more drawables to this node, may use additional structure.
    val links = mutableMapOf<Int, HalfLink>()
    // subclasses must implement how this is drawn

    // this link is drawn in the first place - but that behavior must be customizable in the future, also it can be calculated with z
    // here must draw also children that are connected to this node via links}

    private val drawDelegate = RecursionCanvas(this)
    private val mtGDelegate = RecursionMtG(this)
    private val translateGlobalDelegate = RecursionTranslationGlobal(this)
    private val translateModelDelegate = RecursionTranslationModel(this)
    private val scaleModel = RecursionScaleModel(this)
    private val rotationAxisModel = RecursionRotateAxisModel(this)
    private val rotateAxisGlobal = RecursionRotateAxisGlobal(this)
    private val localToModel = RecursionLocalToModel(this)
    private val scaleGlobal = RecursionScaleGlobal(this)
    private val projectGlobal = RecursionProjectionGlobal(this)

    final override fun drawOnCanvas(canvas: Canvas) {
        fullDraw(canvas, createEmptyTransformation())
    }

    private fun createEmptyTransformation() = TransformationInfo(mutableMapOf())

    abstract fun rawDraw(canvas: Canvas)

    internal fun getAllOthers() = links.map { it.value.getOtherParent() }

    /**
     * [parent] - is the object that initialized drawing, we must know a pointer to him
     * to not cause recursion.
     * Now its assumed that body are connected to each other only via one link - no circles.
     * fullDraw - must be
     * if [alreadyDrawn] equals null - then its the first
     */
    internal fun fullDraw(canvas: Canvas, record: TransformationInfo) {
        drawDelegate.perform(record, getAllOthers(), ScopeCanvas(canvas))
    }

    override fun modelToGlobal(record: TransformationInfo) {
        mtGDelegate.perform(record, getAllOthers(), ScopeMtG())
    }

    override fun projectToGlobal(params: ProjectionBesicParameters, record: TransformationInfo) {
        projectGlobal.perform(record, getAllOthers(), PrjectionScope(params))
    }

    override fun localToGlobal() {
        super.localToGlobal()
        // TODO распространять по всем нодам
        Log.w("suspicious operation", "localToGlobal recommended only for" +
                "usual DrawableObject")
    }

    // Translate models as a whole
    override fun translateGlobal(dx: Double, dy: Double, dz: Double) {
        translateGlobal(dx, dy, dz, createEmptyTransformation())
    }

    internal fun translateGlobal(dx: Double, dy: Double, dz: Double, record: TransformationInfo) {
        translateGlobalDelegate.perform(record, getAllOthers(), CoordinatesScope(dx, dy, dz))
    }

    /**
     *     translates model vertices - as a part for model preparation or changes only within model system
     *     to rotate and translate all model as a whole - use global (default methods)
     */
    override fun translateModel(dx: Double, dy: Double, dz: Double, record: TransformationInfo) {
        translateModelDelegate.perform(record, getAllOthers(), CoordinatesScope(dx, dy, dz))
    }

    /**
     * Scales model
     */
    override fun scaleModel(sx: Double, sy: Double, sz: Double, record: TransformationInfo) {
        scaleModel.perform(record, getAllOthers(), CoordinatesScope(sx, sy, sz))
    }

    override fun shearModel(hx: Double, hy: Double, record: TransformationInfo) {
        TODO("Not yet implemented")
    }

    override fun rotateAxisModel(theta: Double, axis: Coordinate, record: TransformationInfo) {
        rotationAxisModel.perform(record, getAllOthers(), RotationScope(theta, axis))
    }

    override fun rotateAxisGlobal(theta: Double, axis: Coordinate) {
        rotateAxisGlobal(theta, axis, createEmptyTransformation())
    }

    internal fun rotateAxisGlobal(theta: Double, axis: Coordinate, record: TransformationInfo) {
        rotateAxisGlobal.perform(record, getAllOthers(), RotationScope(theta, axis))
    }

    override fun localToModel(record: TransformationInfo) {
        localToModel.perform(record, getAllOthers(), ScopeMtG())
    }

    /**
     * Scales all objects that are somehow connected
     */
    override fun scaleGlobal(times: Double, dy: Double, dz: Double) {
        // that is wrong - can't change model from global
        scaleGlobal(times, dy, dz, createEmptyTransformation())
    }

    internal fun scaleGlobal(times: Double, dy: Double, dz: Double, record: TransformationInfo) {
        scaleGlobal.perform(record, getAllOthers(), CoordinatesScope(times, dy, dz))
    }

    fun addHalfLink(key: Int, coordinate: Coordinate) {
        // adding new half link connected to this node
        links[key] = HalfLink(this, coordinate)
    }

    fun halfLink(key: Int): HalfLink {
        return links[key]!!
    }

    fun recordThis(perObjectTransformationInfo: PerObjectTransformationInfo): TransformationInfo {
        return TransformationInfo(mutableMapOf(this to perObjectTransformationInfo))
    }
}

/**
 * [drawableObject] - object that this link is currently attached to
 * [coordinate] - coordinate where the half link is attached
 *
 * WHAT IS NOT ALLOWED CURRENTLY:
 * 1) Connecting after scaling have been already performed
 *
 */
class HalfLink(val parent: ConnectableObject, val local: Coordinate) {
    var model: Coordinate = local.copy()
    var anotherLink: HalfLink? = null

    private val axes: MutableMap<Int, RotationAxis> = mutableMapOf()

    // currenct coordinate
    var global: Coordinate = model.copy()

    fun addRotationAxis(key: Int, axis: Coordinate) {
        axes[key] = RotationAxis(axis)
    }

    fun rotationAxis(key: Int): RotationAxis {
        return axes[key]!!
    }


    fun getOtherParent(): ConnectableObject? {
        return anotherLink?.parent
    }

    // may add here rotational axis and other shit
    fun connectTo(halfLink: HalfLink) {
        // two-sided linked connection, also objects must be translated so two half links are coincided
        anotherLink = halfLink
        halfLink.anotherLink = this

        halfLink.performConnection(this)
    }

    /**
     * Accepts coordinates of joint, that is connecting this joint,
     * so parent of connecting joint goes as the base and object of this current joint
     * is being translated to the place of connection.
     * Connection goes on model level
     */
    private fun performConnection(halfLink: HalfLink) {
        // All coordinate actions within links must be caused to perform from external sources
        // links can only cause parents do something
//        model = halfLink.model.copy()
        updateParentModel(halfLink)
    }

    /**
     * Called after model of parent was scaled. Model of joint must satisfy new size
     */
    fun scaleModel(sx: Double, sy: Double, sz: Double) {
        // TODO возможно нужно ввести какую-то переменную, которая бы сообщала, - вот были изменения в модели, давайка переделаем глобал...
        // recalculate model vertex of this joint with respect to scaling
        model = scale(arrayOf(model), sx, sy, sz)[0]!!
        global = model.copy()
    }

    fun translateModel(dx: Double, dy: Double, dz: Double) {
        model = translate(arrayOf(model), dx, dy, dz)[0]!!
        global = model.copy()
    }

    fun translateGlobal(dx: Double, dy: Double, dz: Double) {
        global = translate(arrayOf(global), dx, dy, dz)[0]!!
    }

    fun rotateAxisModel(theta: Double, axis: Coordinate, rotateAxes: Boolean) {
        model = rotateAxis(arrayOf(model), theta, axis)[0]!!
        rotateAxesModel(theta, axis, rotateAxes)
        global = model.copy()
    }

    private fun rotateAxesModel(theta: Double, axis: Coordinate, rotateAxes: Boolean) {
        if (rotateAxes) {
            for (i in axes) {
                i.value.rotateModel(theta, axis)
            }
        }
    }

    private fun rotateAxesGlobal(theta: Double, axis: Coordinate, rotateAxes: Boolean) {
        for (i in axes) {
            i.value.rotateGlobal(theta, axis)
        }
    }

    fun rotateAxisGlobal(theta: Double, axis: Coordinate, rotateAxes: Boolean = true) {
        global = rotateAxis(arrayOf(global), theta, axis)[0]!!
        rotateAxesGlobal(theta, axis, rotateAxes)
    }

    // to change only global
    fun scaleGlobal(sx: Double, sy: Double, sz: Double) {
        // recalculate model vertex of this joint with respect to scaling
        global = scale(arrayOf(global), sx, sy, sz)[0]!!
    }

    /**
     * Rotate parent of connected half-link (and all its children) around axis that is set
     * to be at this joint.
     * [key] - sets the axis that will be rotated around
     */
    fun rotateOtherAroundAxisModel(key: Int, theta: Double) {
        val other = getOtherParent() ?: return
        // making this joint to be center of rotation
        other.translateModel(-model.x, -model.y, -model.z, parent.recordBlocking())
        // set this parent as blocking for this operation, when it will be tried to rotated it will not begin
        // the rotation operation
        other.rotateAxisModel(theta, axes[key]!!.model, parent.recordBlocking())

        other.translateModel(model.x, model.y, model.z, parent.recordBlocking())
    }

    private fun ConnectableObject.recordBlocking(): TransformationInfo {
        return parent.recordThis(PerObjectTransformationInfo(isBlocking = true, mustBeExecuted = false))
    }

    fun rotateOtherAroundAxisGlobal(key: Int, theta: Double) {
        val other = getOtherParent() ?: return
        // making this joint to be center of rotation
        other.translateGlobal(-global.x, -global.y, -global.z, parent.recordBlocking())
        // set this parent as blocking for this operation, when it will be tried to rotated it will not begin
        // the rotation operation
        other.rotateAxisGlobal(theta, axes[key]!!.global, parent.recordBlocking())

        other.translateGlobal(global.x, global.y, global.z, parent.recordBlocking())
    }

    // Rotates only linked object, objects that are connected with it will be translated to new position
    // but not rotated
    fun rotateOtherSilentlyModel(key: Int, theta: Double) {
        fun recordOnlyOtherAsExecutable(): TransformationInfo {
            val other = getOtherParent()!!
            val blocked = other.getAllOthers().filterNotNull()
            // mapping blocked transformations
            val blockedMap = blocked
                    .fold(mutableMapOf<ConnectableObject, PerObjectTransformationInfo>()) { map, obj ->
                        map.put(obj, PerObjectTransformationInfo(isBlocking = true, mustBeExecuted = false, onlyTranslate = true)); map
                    }
            return TransformationInfo(blockedMap)
        }

        val other = getOtherParent() ?: return

        // saving old links positions to calculate the difference after
        val saved = other.links.toList().fold(mutableMapOf<Int, Coordinate>()) { map, halflink ->
            map.put(halflink.first, halflink.second.model.copy())
            map
        }

        // making this joint to be center of rotation, also block all linked nodes - they will not be rotated
        other.translateModel(-model.x, -model.y, -model.z, recordOnlyOtherAsExecutable())
        // set this parent as blocking for this operation, when it will be tried to rotated it will not begin
        // the rotation operation
        other.rotateAxisModel(theta, axes[key]!!.model.copy(), recordOnlyOtherAsExecutable().apply {
            objs.put(other, PerObjectTransformationInfo(true, mustBeExecuted = true,
                    rotateAxes = false))
        })

        other.translateModel(model.x, model.y, model.z, recordOnlyOtherAsExecutable())

        // now calculate difference between old positions and new
        val newPos = other.links.toList().fold(mutableMapOf<Int, Coordinate>()) { map, halflink ->
            map.put(halflink.first, halflink.second.model.copy())
            map
        }
        // вычисляем разницу для каждого узла и перемещаем вещи в новые места
        saved.forEach {
            val newCoordinate = newPos[it.key]
            val curr = it.value
            val dx = newCoordinate!!.x - curr.x
            val dy = newCoordinate!!.y - curr.y
            val dz = newCoordinate!!.z - curr.z
            val w = 1
            other.links[it.key]!!.getOtherParent()!!.let {
                it.translateModel(dx, dy, dz, other.recordThis(PerObjectTransformationInfo(isBlocking = true, mustBeExecuted = false)))
            }
        }
    }

    /**
     * Move parents model to the right place of connection. Parent is being translated.
     * - local - means setting joint as a center
     * + model - translating from center to connection place
     * [applyToGlobal] - save changes to global vertices
     * this joints parent must be translated against parent of [against] and [against]
     * must not be moved from its position - so put into perobjecttransformation info, that [against]'s
     * parent must not be executed and its blocking
     */
    protected fun updateParentModel(against: HalfLink) {
        // make joint a center and then translate all body so joints coincide
        val dx = -local.x + against.model.x
        val dy = -local.y + against.model.y
        val dz = -local.z + against.model.z
        parent.translateModel(dx, dy, dz, against.parent.recordThis(PerObjectTransformationInfo(isBlocking = true, mustBeExecuted = false)))
    }
}