package com.sdu.moneyapp.model

class Notification (
    val fromUid: String,
    val toUid: String,
    val type: Int,
    val msg: String,
    val time: Long,
) {

}