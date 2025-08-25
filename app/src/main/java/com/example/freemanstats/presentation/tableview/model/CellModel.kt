package com.example.freemanstats.presentation.tableview.model

import com.evrencoskun.tableview.sort.ISortableModel

class CellModel(
    private val mId: String,
    private val mData: Any?
) : ISortableModel {

    fun getData(): Any? = mData

    override fun getId(): String = mId

    override fun getContent(): Any? = mData
}