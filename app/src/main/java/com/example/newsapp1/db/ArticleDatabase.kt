package com.example.newsapp1.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.newsapp1.models.Article
import java.util.concurrent.locks.Lock

@Database(
    entities = [Article::class],
    version = 1
)

@TypeConverters(Convertors::class)

abstract class ArticleDatabase: RoomDatabase() {

    abstract fun getArticleDao() : ArticleDao

    companion object{
        @Volatile
        private var instance : ArticleDatabase? = null
        private val Lock = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(Lock) {
            instance ?: createDataBase(context).also{
                instance = it
            }
        }

        private fun createDataBase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ArticleDatabase::class.java,
                "article_db.db"
            ).build()
    }

}