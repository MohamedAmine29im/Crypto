package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.MiningProfileEntity
import com.example.data.model.RigUpgradeEntity
import com.example.data.model.SavedAddressEntity
import com.example.data.model.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MiningDao {

    @Query("SELECT * FROM mining_profile WHERE id = 1 LIMIT 1")
    fun getProfileFlow(): Flow<MiningProfileEntity?>

    @Query("SELECT * FROM mining_profile WHERE id = 1 LIMIT 1")
    suspend fun getProfile(): MiningProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertProfile(profile: MiningProfileEntity)

    // Transactions
    @Query("SELECT * FROM transactions ORDER BY timestamp DESC")
    fun getAllTransactionsFlow(): Flow<List<TransactionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity): Long

    // Rig Upgrades
    @Query("SELECT * FROM rig_upgrades ORDER BY costUsdt ASC")
    fun getAllUpgradesFlow(): Flow<List<RigUpgradeEntity>>

    @Query("SELECT * FROM rig_upgrades")
    suspend fun getAllUpgrades(): List<RigUpgradeEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUpgrades(upgrades: List<RigUpgradeEntity>)

    @Update
    suspend fun updateUpgrade(upgrade: RigUpgradeEntity)

    // Saved Addresses
    @Query("SELECT * FROM saved_addresses ORDER BY id DESC")
    fun getSavedAddressesFlow(): Flow<List<SavedAddressEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSavedAddress(address: SavedAddressEntity)

    @Query("DELETE FROM saved_addresses WHERE id = :id")
    suspend fun deleteSavedAddress(id: Long)
}
