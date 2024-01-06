package com.sdu.moneyapp.databases

import com.google.firebase.firestore.*
import com.sdu.moneyapp.model.*

object GroupDatabase : DatabaseManager() {
    
    fun createGroup(name : String, desc : String, participants : List<String>) {
        var success = false;
        val id = getGroupsCollection().document().id
        var group = Group(id, name, desc, participants)
        updateGroup(group)
        for (user in group.participants){
            var userOb = UserDatabase.getUserById(user)
            if (userOb != null) {
                userOb.addGroup(id)
                UserDatabase.updateUser(userOb)
            }
        }
    }

    fun getGroupById(groupId : String) : Group? = 
        getGroupsCollection().document(groupId).get().result.toObject(Group::class.java)
    fun updateGroup(data : Group) =
        getGroupsCollection().document(data.uid).set(data)
}