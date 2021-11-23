package r2.llc.sch.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import r2.llc.sch.db.AppDatabase
import r2.llc.sch.db.entity.message.MessageDao
import javax.inject.Singleton


@[Module InstallIn(SingletonComponent::class)]
open class AppDatabaseModule {

    @[Provides Singleton]
    open fun getRoomDatabase(
        @ApplicationContext context: Context,
    ): AppDatabase = Room.inMemoryDatabaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
    ).build()

    @[Provides Singleton]
    open fun getMessageDao(db: AppDatabase): MessageDao = db.messageDao
}