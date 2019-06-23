package ru.is2si.sisi.base.extension



fun asStringParam(param: Boolean): String {
    return if (param) {
        "Y"
    } else {
        "N"
    }
}

