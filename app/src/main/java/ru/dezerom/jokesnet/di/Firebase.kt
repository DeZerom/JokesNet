package ru.dezerom.jokesnet.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.dezerom.jokesnet.net.FirestoreNames
import javax.inject.Qualifier

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    fun provideFirestoreRef(): FirebaseFirestore {
        return Firebase.firestore
    }

    @Provides
    fun provideAuth(): FirebaseAuth {
        return Firebase.auth
    }

    @Provides
    @UsersReference
    fun provideUsersRef(db: FirebaseFirestore): CollectionReference {
        return db.collection(FirestoreNames.USERS_COLLECTION)
    }

    @Provides
    @JokesReference
    fun provideJokesRef(db: FirebaseFirestore): CollectionReference {
        return db.collection(FirestoreNames.JOKES_COLLECTION)
    }

}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class UsersReference

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class JokesReference