package com.rysanek.diary.data.repositories

import com.rysanek.diary.data.models.Diary
import com.rysanek.diary.utils.Constants.APP_ID
import io.realm.kotlin.Realm
import io.realm.kotlin.log.LogLevel
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.sync.SyncConfiguration

object MongoDb: MongoRepository {
    private val app = App.create(APP_ID)
    private val user = app.currentUser
    private lateinit var realm: Realm
    
    override fun configureRealm() {
    
        user?.let {
            val config = SyncConfiguration.Builder(user, setOf(Diary::class))
                .initialSubscriptions {sub ->
                    add(
                        query = sub.query(
                            query = "ownerId == $0",
                            args = arrayOf(user.id),
                            clazz = Diary::class
                        ),
                        name = "User's Diaries"
                    )
                }
                .log(LogLevel.ALL)
                .build()
            
            realm = Realm.open(config)
        }
    
    }
}