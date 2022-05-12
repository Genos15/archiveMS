package com.thintwice.archive.user.configurations.object_mapping

import com.beust.klaxon.Klaxon

object JSON {
    val parser: Klaxon
        get() = Klaxon()
}