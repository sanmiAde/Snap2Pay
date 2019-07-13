package com.sanmiaderibigbe.snap2pay.model

/***
 * Pay stack transaction object.
 * @param transactionId id of transaction
 * @param amountCharged  amount to be charged from the account
 * @param productDescription Description of product.
 * @param transactionSuccessFul transaction status.
 * @param Date date of transaction
 */
data class Transaction(
    val transactionId: String = "",
    val amountCharged: String = "",
    val productDescription: String = "",
    val transactionSuccessFul: String = "",
    val Date: String = ""
)