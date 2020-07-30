package com.almasb.zeph

import com.almasb.fxgl.cutscene.dialogue.FunctionCallHandler
import com.almasb.fxgl.logging.Logger
import java.lang.RuntimeException
import java.lang.reflect.Method

/**
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
object CommandHandler : FunctionCallHandler {

    private val log = Logger.get(javaClass)

    private val methods = hashMapOf<String, Method>()

    private val stringToObject = hashMapOf<Class<*>, (String) -> Any>()

    init {
        javaClass.declaredMethods.forEach {
            methods[it.name] = it
        }

        // TODO: move this to FXGL, 1) attempt to call a member function, 2) call handle()
        // TODO: other types
        stringToObject[String::class.java] = { it }
        stringToObject[Int::class.java] = { it.toInt() }
    }

    override fun handle(functionName: String, args: Array<String>): Any {
        val cmd = functionName.toLowerCase()
        val method = methods[cmd]

        if (method != null) {

            if (method.parameterCount != args.size) {
                // TODO: no match

                log.warning("No match for $cmd")
            } else {

                val argsAsObjects = method.parameterTypes.mapIndexed { index, type ->
                    val converter = stringToObject[type] ?: throw RuntimeException("No converter found from String to $type")
                    converter.invoke(args[index])
                }

                method.invoke(this, *argsAsObjects.toTypedArray())
            }

        } else {
            log.warning("Unrecognized command: $cmd")
        }

        return 0
    }

    fun has_item(s: String, i: Int): Boolean {

        log.infof("Called has_item with %s and %d", s, i)

        return false
    }
}