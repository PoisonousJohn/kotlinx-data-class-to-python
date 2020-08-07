package com.anna.money.test

import javax.lang.model.element.Name

data class StubName(val name: String) : Name {
    override fun get(index: Int): Char {
        return name[index]
    }

    override fun contentEquals(p0: CharSequence): Boolean {
        return name.contentEquals(p0)
    }

    override val length: Int = name.length

    override fun subSequence(startIndex: Int, endIndex: Int): CharSequence {
        return name.substring(startIndex, endIndex)
    }
}