package com.zulfa.eabsensi.presentation.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.zulfa.eabsensi.core.domain.model.AnnouncementDomain
import com.zulfa.eabsensi.presentation.helper.formatDateToIndonesian
import com.zulfa.eabsensi.presentation.helper.formatDateToIndonesian2

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AnnouncementDialog(
    announcements: List<AnnouncementDomain>,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Text(
                text = "Pengumuman",
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            LazyColumn {
                items(announcements) { item ->
                    Column(modifier = Modifier.padding(vertical = 8.dp)) {
                        Text(
                            text = item.content,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(4.dp))

                        val start = formatDateToIndonesian2(item.startDate)
                        val end = formatDateToIndonesian2(item.endDate)

                        val berlakuText = if (start == end) {
                            "Berlaku:\n$start"
                        } else {
                            "Berlaku:\n$start - $end"
                        }
                        Text(
                            text = berlakuText,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                        HorizontalDivider(
                            Modifier,
                            DividerDefaults.Thickness,
                            color = Color.Black
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Tutup")
            }
        }
    )
}
