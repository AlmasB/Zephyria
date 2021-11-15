package com.almasb.zeph

import com.almasb.fxgl.cutscene.dialogue.FunctionCallHandler
import com.almasb.fxgl.logging.Logger
import java.lang.RuntimeException
import java.lang.reflect.Method

/**
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */




object CommandHandler : FunctionCallHandler() {

    init {
        addFunctionCallDelegate(Gameplay)
    }
}

//object CommandHandler : FunctionCallHandler {
//
//    private val log = Logger.get(javaClass)
//
//    private val methods = hashMapOf<MethodSignature, Method>()
//
//    private val stringToObject = hashMapOf<Class<*>, (String) -> Any>()
//
//    init {
//        Gameplay.javaClass.declaredMethods.forEach {
//            val signature = MethodSignature(it.name, it.parameterCount)
//            methods[signature] = it
//
//            log.info("Added cmd: $it ($signature)")
//        }
//
//        // TODO: move this to FXGL, 1) attempt to call a member function, 2) call handle()
//        // TODO: other types, no-fail alternatives?
//
//        stringToObject[String::class.java] = { it }
//        stringToObject[Int::class.java] = { it.toInt() }
//        stringToObject[Double::class.java] = { it.toDouble() }
//        stringToObject[Boolean::class.java] = {
//            when (it) {
//                "true" -> true
//                "false" -> false
//                else -> throw RuntimeException("Cannot convert $it to Boolean")
//            }
//        }
//    }
//
//    override fun handle(functionName: String, args: Array<String>): Any {
//        val cmd = functionName
//        val method = methods[MethodSignature(cmd, args.size)]
//
//        if (method != null) {
//            val argsAsObjects = method.parameterTypes.mapIndexed { index, type ->
//                val converter = stringToObject[type] ?: throw RuntimeException("No converter found from String to $type")
//                converter.invoke(args[index])
//            }
//
//            return method.invoke(Gameplay, *argsAsObjects.toTypedArray()) ?: 0
//        }
//
//        log.warning("Unrecognized command: $cmd with ${args.size} arguments")
//
//        return 0
//    }
//
//    private data class MethodSignature(val name: String, val paramCount: Int)
//}

// override fun handle(functionName: String, args: Array<String>): Any {
//        log.debug("Function call: $functionName ${args.toList()}")
//
//        val cmd = functionName.toLowerCase()
//
//        when (cmd) {
//

//

//

//
//            else -> {
//                log.warning("Unrecognized command: $cmd")
//            }
//        }
//
//        return 0
//    }