package ru.dezerom.jokesnet.di

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    fun provideFirestoreRef(): FirebaseFirestore {
        return Firebase.firestore
    }

    @Provides
    @UsersReference
    fun provideUsersRef(db: FirebaseFirestore): CollectionReference {
        return db.collection("users")
    }

    @Provides
    @JokesReference
    fun provideJokesRef(db: FirebaseFirestore): CollectionReference {
        return db.collection("jokes")
    }

}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class UsersReference

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class JokesReference