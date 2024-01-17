package com.itangcent.idea.plugin.api.export.core

import com.itangcent.common.constant.Attrs
import com.itangcent.common.model.Param
import com.itangcent.common.utils.GsonUtils
import com.itangcent.common.utils.copy

/**
 * @author xiangqingshan048@hellobike.com
 * @since  2024/1/17
 */

fun methodReturnToPrettyJson(ret: Any?): String? {
    val retCopy = ret?.copy()
    retCopy?.removeAttr()
    return retCopy?.let { GsonUtils.prettyJson(it) }
}

fun methodParamToPrettyJson(params: Collection<Param>?): String? {
    val list = params?.map { it.toParamMap() }
    return list?.let { GsonUtils.prettyJson(list) }
}

private fun Param.toParamMap(): MutableMap<String, Any?> {
    val res = LinkedHashMap<String, Any?>()
    val paramCopy = this.value.copy()
    paramCopy.removeAttr()
    res[this.name!!] = paramCopy
    return res
}

private fun Any?.removeAttr() {
    if (this == null || this !is MutableMap<*, *>) {
        return
    }
    val keys = ArrayList(this.keys)

    for (key in keys) {
        if (key !is String
            || key == Attrs.COMMENT_ATTR
            || key == Attrs.REQUIRED_ATTR
            || key == Attrs.DEMO_ATTR
            || key == Attrs.DEFAULT_VALUE_ATTR
        ) {
            this.remove(key)
        } else {
            when (val value = this[key]) {
                is Array<*> -> {
                    value.forEach {
                        it.removeAttr()
                    }
                }

                is Collection<*> -> {
                    value.forEach {
                        it.removeAttr()
                    }
                }

                else -> {
                    value.removeAttr()
                }
            }
        }
    }
}