package com.ataulm.whatsnext.model

import java.io.Serializable

data class Contributor(val type: String, val person: Person) : Serializable
