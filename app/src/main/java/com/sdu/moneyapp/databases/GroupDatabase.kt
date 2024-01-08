package com.sdu.moneyapp.databases

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.sdu.moneyapp.model.*
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.tasks.await
import javax.security.auth.callback.Callback

object GroupDatabase : DatabaseManager() {

    private val savedGroups = mutableMapOf<String, Group>()
    private val savedGroupsAge = mutableMapOf<String, Long>()
    
    fun createGroup(name : String, desc : String, participants : List<String>, callback: (String) -> Unit = {}) {
        val id = getGroupsCollection().document().id
        setGroup(Group(id, name, desc, participants)){ callback(id) }
    }

    fun addUserToGroup(userId : String, groupId : String) {
        getGroupById(groupId) { group ->
            group.addParticipant(userId)
            setGroup(group)
        }
    }

    fun getGroupsByUser(userId : String, callback: (Group) -> Unit) {
        getGroupsCollection().whereArrayContains("participants", userId).get().addOnSuccessListener { snapshot ->
            snapshot.documents.map { document ->
                val group = document.toObject(Group::class.java) ?: throw Exception("Group ${document.id} failed to be imported")
                savedGroups[group.uid] = group
                savedGroupsAge[group.uid] = System.currentTimeMillis()
                callback(group)
            }
        }.addOnFailureListener { throw Exception("Failed to find groups for User $userId: " + it.message) }
    }

    fun getGroupById(groupId : String, callback: (Group) -> Unit) {
        if (savedGroups.containsKey(groupId)) {
            if (System.currentTimeMillis() - savedGroupsAge.getValue(groupId) > SUITABLE_AGE) {
                savedGroups.remove(groupId)
                savedGroupsAge.remove(groupId)
            }else {
                callback(savedGroups.getValue(groupId))
            }
        }
        getGroupsCollection().document(groupId).get().addOnSuccessListener{
            val group = it.toObject(Group::class.java) ?: throw Exception("Group $groupId failed to be found")
            savedGroups[groupId] = group
            savedGroupsAge[groupId] = System.currentTimeMillis()
            callback(group)
        }.addOnFailureListener() {throw Exception("Group $groupId failed to be found: " + it.message)}
    }

    fun setGroup(data : Group, callbackSuccess: () -> Unit = {}) {
        getGroupsCollection().document(data.uid).set(data).addOnSuccessListener {
            savedGroups[data.uid] = data
            savedGroupsAge[data.uid] = System.currentTimeMillis()
            callbackSuccess()
        }.addOnFailureListener() {
            throw Exception("Group " + data.uid + " failed to set: " + it.message)
        }
    }

}