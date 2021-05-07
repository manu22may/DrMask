package com.capgemini.drmask.model

data class User(
    val id:String? = null,
    val name:String? = null,
    val phone:String? = null,
    val age:String? = null
)/*{
    constructor():this("","","","")//important for firebase to work
}*/