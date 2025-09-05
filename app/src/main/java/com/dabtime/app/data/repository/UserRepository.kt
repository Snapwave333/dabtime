package com.dabtime.app.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import com.dabtime.app.data.model.User
import com.dabtime.app.data.model.DabSession
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) {
    
    private val usersCollection = firestore.collection("users")
    private val dabSessionsCollection = firestore.collection("dab_sessions")
    
    suspend fun createOrUpdateUser(user: User): Result<Unit> {
        return try {
            val currentUser = firebaseAuth.currentUser
            if (currentUser != null) {
                usersCollection.document(currentUser.uid)
                    .set(user)
                    .await()
                Result.success(Unit)
            } else {
                Result.failure(Exception("No authenticated user"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getUser(userId: String): Result<User?> {
        return try {
            val document = usersCollection.document(userId).get().await()
            val user = document.toObject(User::class.java)
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getCurrentUser(): Result<User?> {
        return try {
            val currentUser = firebaseAuth.currentUser
            if (currentUser != null) {
                getUser(currentUser.uid)
            } else {
                Result.failure(Exception("No authenticated user"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun saveDabSession(dabSession: DabSession): Result<String> {
        return try {
            val currentUser = firebaseAuth.currentUser
            if (currentUser != null) {
                val sessionWithUserId = dabSession.copy(userId = currentUser.uid)
                val documentRef = dabSessionsCollection.add(sessionWithUserId).await()
                Result.success(documentRef.id)
            } else {
                Result.failure(Exception("No authenticated user"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getUserDabSessions(userId: String): Result<List<DabSession>> {
        return try {
            val querySnapshot = dabSessionsCollection
                .whereEqualTo("userId", userId)
                .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .get()
                .await()
            
            val sessions = querySnapshot.documents.mapNotNull { document ->
                document.toObject(DabSession::class.java)?.copy(id = document.id)
            }
            
            Result.success(sessions)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getCurrentUserDabSessions(): Result<List<DabSession>> {
        return try {
            val currentUser = firebaseAuth.currentUser
            if (currentUser != null) {
                getUserDabSessions(currentUser.uid)
            } else {
                Result.failure(Exception("No authenticated user"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun updateUserStats(userId: String, totalDabs: Int, precisionScore: Double): Result<Unit> {
        return try {
            val updates = mapOf(
                "totalDabs" to totalDabs,
                "precisionScore" to precisionScore,
                "lastUpdated" to System.currentTimeMillis()
            )
            
            usersCollection.document(userId)
                .update(updates)
                .await()
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun addBadgeToUser(userId: String, badgeId: String): Result<Unit> {
        return try {
            val userDoc = usersCollection.document(userId)
            val user = userDoc.get().await().toObject(User::class.java)
            
            if (user != null) {
                val updatedBadges = user.badges.toMutableList()
                if (!updatedBadges.contains(badgeId)) {
                    updatedBadges.add(badgeId)
                    userDoc.update("badges", updatedBadges).await()
                }
                Result.success(Unit)
            } else {
                Result.failure(Exception("User not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getFriends(userId: String): Result<List<User>> {
        return try {
            val userDoc = usersCollection.document(userId).get().await()
            val user = userDoc.toObject(User::class.java)
            
            if (user != null && user.friends.isNotEmpty()) {
                val friendDocs = usersCollection
                    .whereIn("id", user.friends)
                    .get()
                    .await()
                
                val friends = friendDocs.documents.mapNotNull { doc ->
                    doc.toObject(User::class.java)
                }
                
                Result.success(friends)
            } else {
                Result.success(emptyList())
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}