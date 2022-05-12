package com.thintwice.archive.auth.configurations.object_mapping

import com.beust.klaxon.Klaxon

object JSON {
    val parser: Klaxon
        get() = Klaxon()
}