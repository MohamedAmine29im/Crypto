package com.example.data.benchmark

import com.example.data.model.CandidateBlock
import com.example.data.model.DifficultyLevel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.security.MessageDigest
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicLong
import kotlin.random.Random

data class BenchmarkEngineState(
    val isRunning: Boolean = false,
    val hashrateHps: Double = 0.0,
    val totalHashes: Long = 0L,
    val elapsedSeconds: Long = 0L,
    val lastCandidateHash: String = "0000000000000000000000000000000000000000000000000000000000000000",
    val currentNonce: Long = 0L,
    val solvedBlocks: List<CandidateBlock> = emptyList(),
    val activeThreads: Int = 2,
    val selectedDifficulty: DifficultyLevel = DifficultyLevel.EASY
)

class Sha256BenchmarkEngine(
    private val scope: CoroutineScope
) {
    private val _engineState = MutableStateFlow(BenchmarkEngineState())
    val engineState = _engineState.asStateFlow()

    private var benchmarkJob: Job? = null
    private var tickerJob: Job? = null

    private val isRunningFlag = AtomicBoolean(false)
    private val totalHashesAtomic = AtomicLong(0L)
    private val currentNonceAtomic = AtomicLong(0L)
    private val solvedBlocksList = mutableListOf<CandidateBlock>()

    private var benchmarkStartTimeMs = 0L
    private var lastCalculationTimeMs = 0L
    private var lastCalculationHashCount = 0L

    fun startBenchmark(
        difficulty: DifficultyLevel,
        threads: Int = 2,
        throttleDelayMs: Long = 5L
    ) {
        if (isRunningFlag.get()) return

        isRunningFlag.set(true)
        val clampedThreads = threads.coerceIn(1, Runtime.getRuntime().availableProcessors().coerceAtLeast(1))
        benchmarkStartTimeMs = System.currentTimeMillis()
        lastCalculationTimeMs = benchmarkStartTimeMs
        lastCalculationHashCount = totalHashesAtomic.get()

        _engineState.value = _engineState.value.copy(
            isRunning = true,
            selectedDifficulty = difficulty,
            activeThreads = clampedThreads
        )

        // Ticker loop for calculating H/s and updating elapsed time every 400ms
        tickerJob?.cancel()
        tickerJob = scope.launch(Dispatchers.Main) {
            while (isActive && isRunningFlag.get()) {
                delay(400)
                val now = System.currentTimeMillis()
                val elapsedSec = ((now - benchmarkStartTimeMs) / 1000).coerceAtLeast(0)
                val currentTotal = totalHashesAtomic.get()

                val timeDeltaSec = ((now - lastCalculationTimeMs) / 1000.0).coerceAtLeast(0.001)
                val hashesDelta = currentTotal - lastCalculationHashCount

                val currentHps = (hashesDelta / timeDeltaSec).coerceAtLeast(0.0)
                lastCalculationTimeMs = now
                lastCalculationHashCount = currentTotal

                _engineState.value = _engineState.value.copy(
                    hashrateHps = currentHps,
                    totalHashes = currentTotal,
                    elapsedSeconds = elapsedSec,
                    currentNonce = currentNonceAtomic.get()
                )
            }
        }

        // Launch worker threads
        benchmarkJob?.cancel()
        benchmarkJob = scope.launch(Dispatchers.Default) {
            val workers = (0 until clampedThreads).map { workerId ->
                launch {
                    runBenchmarkWorker(
                        workerId = workerId,
                        totalWorkers = clampedThreads,
                        difficulty = difficulty,
                        throttleDelayMs = throttleDelayMs
                    )
                }
            }
        }
    }

    private suspend fun runBenchmarkWorker(
        workerId: Int,
        totalWorkers: Int,
        difficulty: DifficultyLevel,
        throttleDelayMs: Long
    ) {
        // Prepare realistic simulated Bitcoin block header template (80 bytes)
        // 4 bytes: version (0x20000000)
        // 32 bytes: prev block hash
        // 32 bytes: merkle root
        // 4 bytes: time
        // 4 bytes: bits
        // 4 bytes: nonce
        val headerBytes = ByteArray(80)
        val headerBuffer = ByteBuffer.wrap(headerBytes).order(ByteOrder.LITTLE_ENDIAN)

        headerBuffer.putInt(0x20000000) // Version
        // Simulated prev block hash
        for (i in 0 until 32) headerBuffer.put(Random.nextInt(0, 256).toByte())
        // Simulated Merkle root
        for (i in 0 until 32) headerBuffer.put(Random.nextInt(0, 256).toByte())
        headerBuffer.putInt((System.currentTimeMillis() / 1000).toInt()) // Time
        headerBuffer.putInt(0x1d00ffff) // Difficulty bits

        val md1 = MessageDigest.getInstance("SHA-256")
        val md2 = MessageDigest.getInstance("SHA-256")

        var localNonce = workerId.toLong() * 1000000L
        val batchSize = 600

        while (isRunningFlag.get()) {
            for (i in 0 until batchSize) {
                localNonce++
                headerBuffer.putInt(76, localNonce.toInt())

                // Double SHA-256: SHA-256(SHA-256(headerBytes))
                md1.reset()
                val firstRound = md1.digest(headerBytes)
                md2.reset()
                val doubleHashBytes = md2.digest(firstRound)

                totalHashesAtomic.incrementAndGet()
                currentNonceAtomic.set(localNonce)

                // Fast byte-level prefix check before hex conversion
                if (matchesDifficultyPrefix(doubleHashBytes, difficulty.bits)) {
                    val hashHex = bytesToHexReversed(doubleHashBytes)
                    val now = System.currentTimeMillis()
                    val durationMs = (now - benchmarkStartTimeMs).coerceAtLeast(1)
                    val avgKh = (totalHashesAtomic.get().toDouble() / durationMs)

                    val solvedBlock = CandidateBlock(
                        blockIndex = solvedBlocksList.size + 1,
                        timestamp = now,
                        nonce = localNonce,
                        hashHex = hashHex,
                        difficultyBits = difficulty.bits,
                        difficultyLabel = difficulty.nameAr,
                        hashesTried = totalHashesAtomic.get(),
                        durationMs = durationMs,
                        averageHashrateKh = avgKh
                    )

                    synchronized(solvedBlocksList) {
                        solvedBlocksList.add(0, solvedBlock)
                    }

                    _engineState.value = _engineState.value.copy(
                        lastCandidateHash = hashHex,
                        solvedBlocks = solvedBlocksList.toList()
                    )
                }

                // Periodically update preview hash
                if (i == 0 && workerId == 0) {
                    val hashHex = bytesToHexReversed(doubleHashBytes)
                    _engineState.value = _engineState.value.copy(
                        lastCandidateHash = hashHex
                    )
                }
            }

            if (throttleDelayMs > 0) {
                delay(throttleDelayMs)
            }
        }
    }

    private fun matchesDifficultyPrefix(hashBytes: ByteArray, bits: Int): Boolean {
        // Bitcoin hashes are read in reverse byte order (big-endian display)
        val fullBytesToCheck = bits / 8
        val remainingBits = bits % 8

        for (i in 0 until fullBytesToCheck) {
            val byteIndex = hashBytes.size - 1 - i
            if (hashBytes[byteIndex] != 0.toByte()) return false
        }

        if (remainingBits > 0) {
            val byteIndex = hashBytes.size - 1 - fullBytesToCheck
            val mask = (0xFF shl (8 - remainingBits)) and 0xFF
            val value = hashBytes[byteIndex].toInt() and 0xFF
            if ((value and mask) != 0) return false
        }

        return true
    }

    private fun bytesToHexReversed(bytes: ByteArray): String {
        // Bitcoin convention: display hash bytes in reverse order (RPC display)
        val hexChars = CharArray(bytes.size * 2)
        var charIndex = 0
        for (i in bytes.indices.reversed()) {
            val v = bytes[i].toInt() and 0xFF
            hexChars[charIndex++] = HEX_ARRAY[v ushr 4]
            hexChars[charIndex++] = HEX_ARRAY[v and 0x0F]
        }
        return String(hexChars)
    }

    fun stopBenchmark() {
        isRunningFlag.set(false)
        benchmarkJob?.cancel()
        tickerJob?.cancel()

        _engineState.value = _engineState.value.copy(
            isRunning = false,
            hashrateHps = 0.0
        )
    }

    fun resetStats() {
        stopBenchmark()
        totalHashesAtomic.set(0L)
        currentNonceAtomic.set(0L)
        synchronized(solvedBlocksList) {
            solvedBlocksList.clear()
        }
        _engineState.value = BenchmarkEngineState(
            selectedDifficulty = _engineState.value.selectedDifficulty,
            activeThreads = _engineState.value.activeThreads
        )
    }

    companion object {
        private val HEX_ARRAY = "0123456789abcdef".toCharArray()
    }
}
