package com.my.ecomr.di


import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.ktx.auth
import com.my.ecomr.domains.services.TodoRepository
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.my.ecomr.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class TodoRef

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class OtherRef

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ProductRef
@Qualifier

@Retention(AnnotationRetention.BINARY)
annotation class CartRef


@Module
@ExperimentalCoroutinesApi
@InstallIn(SingletonComponent::class)
object FireStoreAppModule {
    // How to get FireStore Instance??
    @Provides
    fun provideFirebaseFirestore() = FirebaseFirestore.getInstance()


    //How to get FireStore Ref?? -> gets FireStore Instance first...then passed to this fun as param.
    @TodoRef //param type:FirebaseFirestore same with `provideOtherRef` fun
    @Provides
    fun provideTodosRef(db: FirebaseFirestore) = db.collection("todo_collection")
    @OtherRef //param type:FirebaseFirestore same with `provideTodosRef` fun
    @Provides
    fun provideOtherRef(db: FirebaseFirestore) = db.collection("other_collection")


    @ProductRef
    @Provides
    fun provideProductRef(db: FirebaseFirestore) = db.collection("product_collection")

    @CartRef
    @Provides
    fun  provideCartRef(db: FirebaseFirestore) = db.collection("cart_collection")


    // How to construct TodoRepository?
    @Provides
    fun provideTodosRepository(
        @TodoRef
        todoRef: CollectionReference,
    ) = TodoRepository(todoRef)

    // How to construct AuthRepository?
    @Provides
    fun provideAuth() = Firebase.auth




}
//https://github.com/alexmamo/FirestoreCleanArchitectureApp/blob/master/app/src/main/java/ro/alexmamo/firestorecleanarchitecture/data/repository/BooksRepositoryImpl.kt
//https://github.com/alexmamo/FirestoreCleanArchitectureApp/blob/master/app/src/main/java/ro/alexmamo/firestorecleanarchitecture/di/AppModule.kt