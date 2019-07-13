package com.sanmiaderibigbe.snap2pay.repo

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.sanmiaderibigbe.snap2pay.model.Transaction
import com.sanmiaderibigbe.snap2pay.model.User
import durdinapps.rxfirebase2.RxFirebaseAuth
import durdinapps.rxfirebase2.RxFirebaseDatabase
import durdinapps.rxfirebase2.RxFirebaseUser
import io.reactivex.Completable
import io.reactivex.Maybe

class FirebaseRepository(private val firebaseAuth: FirebaseAuth, private val firebaseDatabase: FirebaseDatabase) {


    /***
     * authenticate user
     * @param email email of user
     * @param password password of user
     *
     * @return Maybe observable containing authresult.
     */
    fun signIn(email: String, password: String) : Maybe<AuthResult>{
        return RxFirebaseAuth.signInWithEmailAndPassword(firebaseAuth, email, password)
    }

    fun registerUser(email : String, password : String ): Maybe<AuthResult> {
        return RxFirebaseAuth.createUserWithEmailAndPassword(firebaseAuth, email, password )
    }

    fun uploadUserData(user: User) : Completable {
        return  RxFirebaseDatabase.setValue(
            firebaseDatabase.getReference(USER_PATH).child(getCurrentUser()?.uid!!), user
        )
    }

    fun uploadTransactionData(transaction: Transaction): Completable {
        return RxFirebaseDatabase.setValue(
            firebaseDatabase.getReference(TRANSACTION_PATH).child(getCurrentUser()?.uid!!).child(transaction.transactionId),
            transaction
        )
    }

    fun getCurrentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    fun isEmailVerified(): Boolean? {
        getCurrentUser()?.reload()
        return getCurrentUser()?.isEmailVerified

    }

    fun sendEmailVerification(): Completable {
        return RxFirebaseUser.sendEmailVerification(getCurrentUser()!!)
    }

    fun getUserData(): Maybe<User> {
        return RxFirebaseDatabase.observeSingleValueEvent(
            firebaseDatabase.getReference(USER_PATH).child(getCurrentUser()?.uid!!),
            User::class.java
        )
    }

    fun signOut() {
        firebaseAuth.signOut()
    }


    companion object {
        private const val USER_PATH = "users"
        private const val TRANSACTION_PATH = "transactions"
    }
}