package com.example.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Power
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.Upgrade
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.RigUpgradeEntity
import com.example.ui.MiningUiState
import com.example.ui.MiningViewModel
import com.example.ui.theme.BinanceYellow
import com.example.ui.theme.BlockchainEmerald
import com.example.ui.theme.CryptoBorder
import com.example.ui.theme.CryptoDarkCard
import com.example.ui.theme.CryptoDarkSurfaceVariant
import com.example.ui.theme.CryptoGreen
import com.example.ui.theme.CryptoTextMuted
import com.example.ui.theme.CryptoTextPrimary
import com.example.ui.theme.CryptoTextSecondary
import java.util.Locale

@Composable
fun HardwareUpgradesScreen(
    uiState: MiningUiState,
    viewModel: MiningViewModel,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Column {
                Text(
                    text = "سيرفرات التعدين والعتاد",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = CryptoTextPrimary
                )
                Text(
                    text = "قم بترقية خوادم التعدين لزيادة معدل الهاش ريت ومضاعفة أرباحك اليومية",
                    style = MaterialTheme.typography.bodySmall,
                    color = CryptoTextSecondary
                )
            }
        }

        // Summary Rig Level & Current Base Hashrate
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BinanceYellow.copy(alpha = 0.5f), RoundedCornerShape(18.dp)),
                colors = CardDefaults.cardColors(containerColor = CryptoDarkCard),
                shape = RoundedCornerShape(18.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "مستوى منشأة التعدين (Rig Level):",
                            style = MaterialTheme.typography.bodySmall,
                            color = CryptoTextSecondary
                        )
                        Text(
                            text = "المستوى ${uiState.profile.rigLevel} ★",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                            color = BinanceYellow
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "الهاش ريت الإجمالي المخصص:",
                            style = MaterialTheme.typography.bodySmall,
                            color = CryptoTextSecondary
                        )
                        Text(
                            text = "${String.format(Locale.US, "%.1f", uiState.profile.baseHashrate)} MH/s",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                            color = BlockchainEmerald
                        )
                    }
                }
            }
        }

        items(uiState.upgrades) { upgrade ->
            UpgradeCard(
                upgrade = upgrade,
                onUpgrade = { viewModel.upgradeRig(upgrade) }
            )
        }
    }
}

@Composable
private fun UpgradeCard(
    upgrade: RigUpgradeEntity,
    onUpgrade: () -> Unit
) {
    val isMaxLevel = upgrade.level >= upgrade.maxLevel

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, CryptoBorder, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = CryptoDarkCard),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .border(1.dp, BinanceYellow.copy(alpha = 0.4f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = when (upgrade.category) {
                                "ASIC" -> Icons.Default.Speed
                                "COOLING" -> Icons.Default.Thermostat
                                "POWER" -> Icons.Default.Power
                                else -> Icons.Default.Memory
                            },
                            contentDescription = null,
                            tint = BinanceYellow,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Column {
                        Text(
                            text = upgrade.nameAr,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = CryptoTextPrimary
                        )
                        Text(
                            text = upgrade.nameEn,
                            style = MaterialTheme.typography.labelSmall,
                            color = CryptoTextMuted
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (upgrade.level > 0) BlockchainEmerald.copy(alpha = 0.15f) else CryptoTextMuted.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = "المستوى ${upgrade.level}/${upgrade.maxLevel}",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = if (upgrade.level > 0) BlockchainEmerald else CryptoTextMuted,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = upgrade.descriptionAr,
                style = MaterialTheme.typography.bodySmall,
                color = CryptoTextSecondary
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "+${upgrade.hashrateBoostMh.toInt()} MH/s",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = BlockchainEmerald
                    )
                    Text(
                        text = "زيادة إنتاج",
                        style = MaterialTheme.typography.labelSmall,
                        color = CryptoTextSecondary
                    )
                }

                Button(
                    onClick = onUpgrade,
                    enabled = !isMaxLevel,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BinanceYellow,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("upgrade_${upgrade.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.Upgrade,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isMaxLevel) "أقصى مستوى" else "ترقية (${upgrade.costUsdt.toInt()} USDT)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}
