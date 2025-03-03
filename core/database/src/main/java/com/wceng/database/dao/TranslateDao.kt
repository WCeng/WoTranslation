package com.wceng.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.wceng.database.model.TranslateEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TranslateDao {

    @Upsert
    suspend fun insertOrUpdateTranslateEntity(trans: TranslateEntity)

    @Query("delete from translates where id = :id")
    suspend fun deleteTranslateEntityById(id: String)

    @Query(
        value = """
            SELECT * FROM translates
             WHERE original_text = :originalText
                    AND original_language_code = :originalLanguageCode
                    AND target_language_code = :targetLanguageCode
                    LIMIT 1
        """
    )
//    @Query(
//        value = """
//            SELECT * FROM translates
//                WHERE id IN (
//                    SELECT rowid FROM translates_fts
//                    WHERE original_text match :originalText
//                    AND original_language_code = :originalLanguageCode
//                    AND target_language_code = :targetLanguageCode
//                    LIMIT 1)
//            """
//    )
    fun getTranslateEntityBySrcAndCode(
        originalText: String,
        originalLanguageCode: String,
        targetLanguageCode: String
    ): Flow<TranslateEntity?>

    @Query("select * from translates where id = :id")
    fun getTranslateEntityById(id: String): Flow<TranslateEntity>

    @Query("select * from translates")
    fun getTranslateEntities(): Flow<List<TranslateEntity>>

    @Query("select * from translates order by translated_date desc limit :limit")
    fun getLatestTranslateEntities(limit: Int): Flow<List<TranslateEntity>>

    @Query("select * from translates where id in (:ids)")
    fun getTranslateEntities(ids: Set<String>): Flow<List<TranslateEntity>>

    @Query(""" 
            SELECT EXISTS(
                SELECT 1 FROM translates    
                    WHERE original_text = :originalText 
                    and original_language_code = :originalLanguageCode
                    and target_language_code = :targetLanguageCode 
                    LIMIT 1
            )
            """)
    fun isTranslateEntityExists(
        originalText: String,
        originalLanguageCode: String,
        targetLanguageCode: String
    ): Flow<Boolean>
}