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

/***
 * Firebase repositoru class handles all transactions with firebase.
 * @param firebaseAuth Firbase Authentication object.
 * @param firebaseDatabase Firebase Database object.
 *
 * @author OLuwasanm Aderibigbe
 */
class FirebaseRepository(private val firebaseAuth: FirebaseAuth, private val firebaseDatabase: FirebaseDatabase) {


    /***
     * Login user
     * @param email email of user
     * @param password password of user
     *
     * @return Maybe observable containing authresult.
     */
    fun signIn(email: String, password: String) : Maybe<AuthResult>{
        return RxFirebaseAuth.signInWithEmailAndPassword(firebaseAuth, email, password)
    }

    /***
     * Creates new user
     * @param email Email of user
     * @param password User password
     *
     * @return Maybe observable containing authresult
     */
    fun registerUser(email : String, password : String ): Maybe<AuthResult> {
        return RxFirebaseAuth.createUserWithEmailAndPassword(firebaseAuth, email, password )
    }

    /***
     *
     * uploads user sign up data after creating new user account.
     * @param user Created user containing user detail.
     *
     * @return completable observable
     */
    fun uploadUserData(user: User) : Completable {
        return  RxFirebaseDatabase.setValue(
            firebaseDatabase.getReference(USER_PATH).child(getCurrentUser()?.uid!!), user
        )
    }

    /***
     * Uploads each transaction taken by the user.
     * @param transaction user transaction
     *
     * @return Completable  observable
     */
    fun uploadTransactionData(transaction: Transaction): Completable {
        return RxFirebaseDatabase.setValue(
            firebaseDatabase.getReference(TRANSACTION_PATH).child(getCurrentUser()?.uid!!).child(transaction.transactionId),
            transaction
        )
    }

    /***
     * Get current user if user is signed in.
     *
     * @return  current signed user.
     */
    fun getCurrentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    /***
     * Checks if email has been verified. User can not user app features if email has not been verified.
     * @return  is verified.
     */
    fun isEmailVerified(): Boolean? {
        getCurrentUser()?.reload()
        return getCurrentUser()?.isEmailVerified

    }

    /***
     * Sends email verification to user email after user accoount creation.
     * @return completable observable.
     */

    fun sendEmailVerification(): Completable {
        return RxFirebaseUser.sendEmailVerification(getCurrentUser()!!)
    }

    /***
     * Get user data from firebase.
     * @return Maybe observable.
     */

    fun getUserData(): Maybe<User> {
        return RxFirebaseDatabase.observeSingleValueEvent(
            firebaseDatabase.getReference(USER_PATH).child(getCurrentUser()?.uid!!),
            User::class.java
        )
    }

    /***
     * Sign out user
     */

    fun signOut() {
        firebaseAuth.signOut()
    }


    companion object {
        private const val USER_PATH = "users"
        private const val TRANSACTION_PATH = "transactions"
    }
}