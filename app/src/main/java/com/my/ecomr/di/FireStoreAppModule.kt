package com.my.ecomr.di


import com.my.ecomr.domains.services.TodoRepository
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class TodoRef

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class OtherRef

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


    // How to construct TodoRepository?
    @Provides
    fun provideTodosRepository(
        @TodoRef
        todoRef: CollectionReference,
    ) = TodoRepository(todoRef)


}
//https://github.com/alexmamo/FirestoreCleanArchitectureApp/blob/master/app/src/main/java/ro/alexmamo/firestorecleanarchitecture/data/repository/BooksRepositoryImpl.kt
//https://github.com/alexmamo/FirestoreCleanArchitectureApp/blob/master/app/src/main/java/ro/alexmamo/firestorecleanarchitecture/di/AppModule.kt