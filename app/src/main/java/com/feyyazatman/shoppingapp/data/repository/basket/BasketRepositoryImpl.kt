package com.feyyazatman.shoppingapp.data.repository.basket

import com.feyyazatman.shoppingapp.data.model.BasketProductItem
import com.feyyazatman.shoppingapp.data.remote.utils.Resource
import com.feyyazatman.shoppingapp.data.utils.await
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import javax.inject.Inject

class BasketRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val fireStore: FirebaseFirestore
) : BasketRepository {

    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser


    override suspend fun setBasketData(basketProductItem: BasketProductItem) {
        val product = hashMapOf(
            "category" to basketProductItem.category,
            "description" to basketProductItem.description,
            "id" to basketProductItem.id,
            "image" to basketProductItem.image,
            "price" to basketProductItem.price,
            "title" to basketProductItem.title,
            "subTotal" to FieldValue.increment((basketProductItem.price * basketProductItem.amount!!))
        )
        return try {
            return currentUser.let {
                if (it != null) {
                    fireStore.collection("users").document(it.uid).collection("basket")
                        .document(basketProductItem.id.toString()).set(product, SetOptions.merge())
                        .await()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun incrementBasketData(basketProductItem: BasketProductItem) {
        val product = hashMapOf(
            "category" to basketProductItem.category,
            "description" to basketProductItem.description,
            "id" to basketProductItem.id,
            "image" to basketProductItem.image,
            "price" to basketProductItem.price,
            "title" to basketProductItem.title,
            "subTotal" to FieldValue.increment((basketProductItem.price))
        )
        return try {
            return currentUser.let {
                if (it != null) {
                    fireStore.collection("users").document(it.uid).collection("basket")
                        .document(basketProductItem.id.toString()).set(product, SetOptions.merge())
                        .await()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override suspend fun decrementBasketData(basketProductItem: BasketProductItem) {
        val product = hashMapOf(
            "category" to basketProductItem.category,
            "description" to basketProductItem.description,
            "id" to basketProductItem.id,
            "image" to basketProductItem.image,
            "price" to basketProductItem.price,
            "title" to basketProductItem.title,
            "subTotal" to FieldValue.increment((basketProductItem.price * -1))
        )
        return try {
            return currentUser.let {
                if (it != null) {
                    fireStore.collection("users").document(it.uid).collection("basket")
                        .document(basketProductItem.id.toString()).set(product, SetOptions.merge())
                        .await()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun addSubTotal(price: Double) {
        val id = currentUser?.uid
        fireStore.collection("users").document(id.toString())
            .update("subTotalPrice", FieldValue.increment(price)).await()
    }

    override suspend fun getSubTotal(): Number {
        val result = getBasketData()
        if (result is Resource.Success) {
           val sum = result.result.fold(0.0) { total,value ->
                total + value.subTotal
            }
            return sum
        }
        return 0
    }


    override suspend fun getBasketData(): Resource<List<BasketProductItem>> {
        val id = currentUser?.uid
        return try {
            val docRef =
                fireStore.collection("users").document(id.toString()).collection("basket").get()
                    .await()
            val list = docRef.documents.map {
                BasketProductItem(
                    category = it.get("category") as String,
                    description = it.get("description") as String,
                    id = it.get("id") as String,
                    image = it.get("image") as String,
                    price = it.get("price") as Double,
                    title = it.get("title") as String,
                    amount = it.get("amount") as Long?,
                    subTotal = it.get("subTotal") as Double
                )
            }
            Resource.Success(list)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e.message)
        }
    }

    override suspend fun deleteBasketData() {
        val id = currentUser?.uid
        return try {
            val docRef =
                fireStore.collection("users").document(id.toString()).collection("basket").get().await()
            docRef.documents.forEach {
                val itemid = it.get("id")
                fireStore.collection("users").document(id.toString()).collection("basket")
                    .document(itemid.toString()).delete()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}
