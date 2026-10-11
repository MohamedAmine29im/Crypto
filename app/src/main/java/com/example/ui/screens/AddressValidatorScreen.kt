package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.BenchmarkUiState
import com.example.ui.BenchmarkViewModel
import com.example.ui.theme.BitcoinGold
import com.example.ui.theme.BlockchainEmerald
import com.example.ui.theme.CryptoBorder
import com.example.ui.theme.CryptoDarkCard
import com.example.ui.theme.CryptoDarkSurface
import com.example.ui.theme.CryptoRed
import com.example.ui.theme.CryptoTextPrimary
import com.example.ui.theme.CryptoTextSecondary

@Composable
fun AddressValidatorScreen(
    uiState: BenchmarkUiState,
    viewModel: BenchmarkViewModel,
    modifier: Modifier = Modifier
) {
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    val addressInfo = uiState.addressInfo

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column {
                Text(
                    text = "فاحص عناوين البيتكوين (للعرض والتحقق)",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = CryptoTextPrimary
                )
                Text(
                    text = "التحقق التقني من بنية ونوع عناوين البيتكوين بدون حفظ أو وصاية على أي أموال",
                    style = MaterialTheme.typography.bodySmall,
                    color = CryptoTextSecondary
                )
            }
        }

        // Zero-Custody Security Banner
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = CryptoDarkCard),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BlockchainEmerald.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(BlockchainEmerald.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = BlockchainEmerald,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Text(
                        text = "🔒 خصوصية وأمان مطلق: هذا الحقل للعرض والتحقق الصوري فقط. التطبيق لا يطلب ولا يمتلك أي وصول لمفاتيحك الخاصة أو عبارات الاسترداد.",
                        style = MaterialTheme.typography.bodySmall,
                        color = CryptoTextSecondary,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        // Input Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CryptoDarkCard),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CryptoBorder, RoundedCornerShape(16.dp))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "عنوان البيتكوين (Bitcoin Public Address):",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = CryptoTextPrimary
                    )

                    OutlinedTextField(
                        value = uiState.addressInputText,
                        onValueChange = { viewModel.onAddressInputChanged(it) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("btc_address_input"),
                        placeholder = { Text("bc1q... أو 1... أو 3...") },
                        trailingIcon = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (uiState.addressInputText.isNotEmpty()) {
                                    IconButton(onClick = { viewModel.onAddressInputChanged("") }) {
                                        Icon(imageVector = Icons.Default.Clear, contentDescription = "مسح")
                                    }
                                }
                                IconButton(
                                    onClick = {
                                        clipboardManager.getText()?.text?.let {
                                            viewModel.onAddressInputChanged(it)
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ContentPaste,
                                        contentDescription = "لصق",
                                        tint = BitcoinGold
                                    )
                                }
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BitcoinGold,
                            unfocusedBorderColor = CryptoBorder,
                            focusedTextColor = CryptoTextPrimary,
                            unfocusedTextColor = CryptoTextPrimary
                        ),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    // Quick test samples chips
                    Text(
                        text = "عناوين تجريبية للاختبار السريع:",
                        style = MaterialTheme.typography.labelSmall,
                        color = CryptoTextSecondary
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SampleAddressChip(
                            label = "SegWit (bc1q)",
                            address = "bc1qxy2kgdygjrsqtzq2n0yrf2493p83kkfjhx0wlh",
                            onClick = { viewModel.onAddressInputChanged(it) }
                        )
                        SampleAddressChip(
                            label = "Taproot (bc1p)",
                            address = "bc1p5d7rjq7g6rd22r325ep4sq6hgzerxtxazzvxwdncxqmrd6gtde3hp5gedz",
                            onClick = { viewModel.onAddressInputChanged(it) }
                        )
                        SampleAddressChip(
                            label = "Legacy (1...)",
                            address = "1A1zP1eP5QGefi2DMPTfTL5SLmv7DivfNa",
                            onClick = { viewModel.onAddressInputChanged(it) }
                        )
                    }
                }
            }
        }

        // Analysis Result Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CryptoDarkCard),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.dp,
                        if (addressInfo.isValid) BlockchainEmerald.copy(alpha = 0.6f) else CryptoBorder,
                        RoundedCornerShape(16.dp)
                    )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "نتائج التحقق التقني:",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = CryptoTextPrimary
                        )

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (addressInfo.isValid) BlockchainEmerald.copy(alpha = 0.2f) else CryptoRed.copy(alpha = 0.2f)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = if (addressInfo.isValid) Icons.Default.CheckCircle else Icons.Default.Error,
                                    contentDescription = null,
                                    tint = if (addressInfo.isValid) BlockchainEmerald else CryptoRed,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = if (addressInfo.isValid) "عنوان صحيح ومعتمد ✓" else "صيغة غير صالحة",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = if (addressInfo.isValid) BlockchainEmerald else CryptoRed
                                )
                            }
                        }
                    }

                    DetailRow(label = "نوع العنوان:", value = addressInfo.typeDescription)
                    DetailRow(label = "نوع السكريبت:", value = addressInfo.scriptType)
                    DetailRow(
                        label = "دعم SegWit:",
                        value = if (addressInfo.isSegwit) "نعم (رسوم منخفضة وحماية ضد الخطأ)" else "لا (عنوان قياسي تقليدي)"
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = addressInfo.securityNotes,
                        style = MaterialTheme.typography.bodySmall,
                        color = CryptoTextSecondary,
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun SampleAddressChip(
    label: String,
    address: String,
    onClick: (String) -> Unit
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = CryptoDarkSurface,
        modifier = Modifier
            .border(1.dp, CryptoBorder, RoundedCornerShape(8.dp))
            .clickable { onClick(address) }
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = BitcoinGold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
        )
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodySmall, color = CryptoTextSecondary)
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
            color = CryptoTextPrimary
        )
    }
}
